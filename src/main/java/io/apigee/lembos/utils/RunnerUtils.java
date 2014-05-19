/*
 * Copyright 2014 Apigee Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.apigee.lembos.utils;

import io.apigee.lembos.mapreduce.LembosConstants;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;

/**
 * Various utilities for interacting with files specifically {@link org.apache.hadoop.util.ToolRunner}.
 *
 * <b>Note:</b> These have been broken out to allow people to write their own runners but not have to rewrite the
 * scaffolding things required.
 */
public final class RunnerUtils {

    private static final String TMP_ARCHIVE_CONF_NAME = "tmparchives";

    // http://nodejs.org/api/modules.html#modules_file_modules
    private static final String[] KNOWN_NODE_MODULE_EXTENSIONS = new String[] {
            "",
            ".js",
            ".json",
            ".node"
    };

    /**
     * Private constructor.
     */
    private RunnerUtils() { }

    /**
     * Adds the "-libjars" entries, if any, to the {@link ClassLoader}.
     *
     * @param conf the Hadoop configuration
     *
     * @throws IOException if there is an issue creating the new ClassLoader
     */
    public static void addLibJarsToClassLoader(final Configuration conf) throws IOException {
        final URL[] libJars = GenericOptionsParser.getLibJars(conf);

        if (libJars != null && libJars.length > 0) {
            final ClassLoader loader = new URLClassLoader(libJars, conf.getClassLoader());

            Thread.currentThread().setContextClassLoader(loader);

            conf.setClassLoader(loader);
        }
    }

    /**
     * Prepare the Node.js module to be submitted with the Hadoop job.
     *
     * @param conf hadoop configuration
     *
     * @throws IOException if there is a problem creating a copy of the Node.js module or zipping it up
     */
    public static void prepareModuleForJob(final Configuration conf) throws IOException {
        // Create local/copy of the Node.js module
        final File tmpModulePath = createLocalCopyOfModule(conf);

        // Create archive of the Node.js module
        final File moduleZip = FileUtils.createZipFile(tmpModulePath);

        // Clean up when the process exits
        moduleZip.deleteOnExit();
        tmpModulePath.deleteOnExit();

        // Update the Node.js module path to point to our local copy
        conf.set(LembosConstants.MR_MODULE_PATH, tmpModulePath.getAbsolutePath());

        // Add the Node.js module zip to the tmparchives (JobClient uses this to orchestrate DistributedCache)
        final StringBuilder tmpArchivesBuilder = new StringBuilder(conf.get(TMP_ARCHIVE_CONF_NAME) == null
                                                                           ? ""
                                                                           : conf.get(TMP_ARCHIVE_CONF_NAME));

        if (!tmpArchivesBuilder.toString().isEmpty()) {
            tmpArchivesBuilder.append(',');
        }

        tmpArchivesBuilder.append(moduleZip.toURI().toString());
        tmpArchivesBuilder.append('#');
        tmpArchivesBuilder.append(LembosConstants.MR_DISTRIBUTED_CACHE_SYMLINK);

        conf.set(TMP_ARCHIVE_CONF_NAME, tmpArchivesBuilder.toString());
    }

    /**
     * Takes a module path, which could be a local filesystem path or a url, and returns the local path to the module.
     *
     * <b>Note:</b> If the value is a URL, the URL will be downloaded locally to create the necessary filesystem
     * location for the Node.js module to allow for archiving and adding to DistributedCache.
     *
     * @param conf the Hadoop configuration
     *
     * @return the local filesystem path to the module
     *
     * @throws IOException if anything goes wrong
     */
    public static File createLocalCopyOfModule(final Configuration conf) throws IOException {
        final String moduleName = conf.get(LembosConstants.MR_MODULE_NAME);
        final String modulePath = conf.get(LembosConstants.MR_MODULE_PATH);
        File localTempModule = null;

        if (moduleName != null && !moduleName.trim().isEmpty() && modulePath != null && !modulePath.trim().isEmpty()) {
            URL moduleUrl;

            // Test if this is a URL or a file
            try {
                moduleUrl = new URL(modulePath);
            } catch (MalformedURLException e) {
                // This is to be expected if the configuration path is not a URL
                moduleUrl = null;
            }

            // Create a local temporary directory to contain the Node.js module
            final java.nio.file.Path tmpDir = Files.createTempDirectory("LembosMapReduceModule");
            FileSystem fs;

            // Delete the temp directory
            tmpDir.toFile().deleteOnExit();

            // Create the proper FileSystem
            if (moduleUrl == null) {
                fs = FileSystem.getLocal(conf);
            } else {
                try {
                    fs = FileSystem.get(moduleUrl.toURI(), conf);
                } catch (URISyntaxException e) {
                    throw new IOException(e);
                }
            }

            final org.apache.hadoop.fs.Path pathObj = new org.apache.hadoop.fs.Path(modulePath);

            if (fs.exists(pathObj)) {
                final org.apache.hadoop.fs.Path tmpPathObj =
                        new org.apache.hadoop.fs.Path(tmpDir.toAbsolutePath().toString());

                // Copy the local/remote file(s) to the temporary directory
                fs.copyToLocalFile(pathObj, tmpPathObj);

                final File moduleFile = new File(new org.apache.hadoop.fs.Path(tmpPathObj,
                                                                               pathObj.getName()).toString());

                // Set the MapReduce module path accordingly
                if (moduleFile.isFile()) {
                    final String fileName = moduleFile.getName();
                    boolean wasArchive = false;

                    if (fileName.endsWith(".tar") || fileName.endsWith(".tar.gz") || fileName.endsWith(".tgz")) {
                        FileUtil.unTar(moduleFile, tmpDir.toFile());
                        wasArchive = true;
                    } else if (fileName.endsWith(".zip")) {
                        FileUtil.unZip(moduleFile, tmpDir.toFile());
                        wasArchive = true;
                    }

                    if (wasArchive) {
                        for (final String extension : KNOWN_NODE_MODULE_EXTENSIONS) {
                            final File potentialModuleFile = new File(tmpDir.toFile(), moduleName + extension);

                            if (potentialModuleFile.exists()) {
                                localTempModule = potentialModuleFile;
                                break;
                            }
                        }
                    } else {
                        localTempModule = moduleFile;
                    }
                } else {
                    localTempModule = new File(tmpDir.toFile(), moduleName);
                }
            } else {
                throw new RuntimeException("Unable to create/locate Node.js module locally: " + modulePath);
            }
        }

        if (localTempModule == null) {
            throw new RuntimeException("Unable to create local copy of Node.js module from path: "
                                               + conf.get(LembosConstants.MR_MODULE_PATH));
        }

        return localTempModule;
    }

    /**
     * Loads the {@link FsUrlStreamHandlerFactory}.
     *
     * @param conf the configuration to use
     *
     * @throws IOException if something goes wrong
     */
    public static void loadFsUrlStreamHandler(final Configuration conf) throws IOException {
        // Here to avoid https://issues.apache.org/jira/browse/HADOOP-9041
        FileSystem.get(conf);

        // Hook up the HDFS URL scheme handler
        // noinspection ErrorNotRethrown
        try {
            URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
        } catch (final Error e) {
            // This can happen if the handler has already been loaded so ignore
            System.err.println("The HDFS URL scheme handler has already been loaded");
        }
    }

}
