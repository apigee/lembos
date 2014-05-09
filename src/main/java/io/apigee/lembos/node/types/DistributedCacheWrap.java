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

package io.apigee.lembos.node.types;

import io.apigee.lembos.mapreduce.LembosMessages;
import io.apigee.lembos.utils.JavaScriptUtils;
import io.apigee.trireme.core.Utils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;
import org.mozilla.javascript.annotations.JSStaticFunction;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Java implementation of the {@link DistributedCache} JavaScript object.
 */
public final class DistributedCacheWrap extends ScriptableObject {

    private static final long serialVersionUID = -92460111884329989L;
    public static final String CLASS_NAME = "DistributedCache";

    /* JavaScript Methods */

    /**
     * Java wrapper for {@link DistributedCache#addArchiveToClassPath(Path, Configuration)} and
     * {@link DistributedCache#addArchiveToClassPath(Path, Configuration, org.apache.hadoop.fs.FileSystem)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     */
    @JSStaticFunction
    public static void addArchiveToClassPath(final Context ctx, final Scriptable thisObj, final Object[] args,
                                             final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;
        final Object arg1 = args.length >= 2 ? args[1] : Undefined.instance;

        if (args.length < 2) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.TWO_ARGS_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!JavaScriptUtils.isDefined(arg1)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_REQUIRED);
        } else if (!(arg1 instanceof ConfigurationWrap)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_MUST_BE_CONF);
        }

        final Configuration conf = ((ConfigurationWrap)arg1).getConf();
        final Path path = new Path(URI.create(arg0.toString()));

        try {
            DistributedCache.addArchiveToClassPath(path, conf, path.getFileSystem(conf));
        } catch (IOException e) {
            throw Utils.makeError(ctx, thisObj, e.getMessage());
        }
    }

    /**
     * Java wrapper for {@link DistributedCache#addCacheArchive(URI, Configuration)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     */
    @JSStaticFunction
    public static void addCacheArchive(final Context ctx, final Scriptable thisObj, final Object[] args,
                                       final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;
        final Object arg1 = args.length >= 2 ? args[1] : Undefined.instance;

        if (args.length < 2) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.TWO_ARGS_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!JavaScriptUtils.isDefined(arg1)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_REQUIRED);
        } else if (!(arg1 instanceof ConfigurationWrap)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_MUST_BE_CONF);
        }

        DistributedCache.addCacheArchive(URI.create(arg0.toString()), ((ConfigurationWrap)arg1).getConf());
    }

    /**
     * Java wrapper for {@link DistributedCache#addCacheFile(URI, Configuration)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     */
    @JSStaticFunction
    public static void addCacheFile(final Context ctx, final Scriptable thisObj, final Object[] args,
                                    final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;
        final Object arg1 = args.length >= 2 ? args[1] : Undefined.instance;

        if (args.length < 2) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.TWO_ARGS_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!JavaScriptUtils.isDefined(arg1)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_REQUIRED);
        } else if (!(arg1 instanceof ConfigurationWrap)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_MUST_BE_CONF);
        }

        DistributedCache.addCacheFile(URI.create(arg0.toString()), ((ConfigurationWrap)arg1).getConf());
    }

    /**
     * Java wrapper for {@link DistributedCache#addFileToClassPath(Path, Configuration)} and
     * {@link DistributedCache#addFileToClassPath(Path, Configuration, org.apache.hadoop.fs.FileSystem)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     */
    @JSStaticFunction
    public static void addFileToClassPath(final Context ctx, final Scriptable thisObj, final Object[] args,
                                          final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;
        final Object arg1 = args.length >= 2 ? args[1] : Undefined.instance;

        if (args.length < 2) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.TWO_ARGS_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!JavaScriptUtils.isDefined(arg1)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_REQUIRED);
        } else if (!(arg1 instanceof ConfigurationWrap)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_MUST_BE_CONF);
        }

        final Configuration conf = ((ConfigurationWrap)arg1).getConf();
        final Path path = new Path(URI.create(arg0.toString()));

        try {
            DistributedCache.addFileToClassPath(path, conf, path.getFileSystem(conf));
        } catch (IOException e) {
            throw Utils.makeError(ctx, thisObj, e.getMessage());
        }
    }

    /**
     * Java wrapper for {@link DistributedCache#addLocalArchives(Configuration, String)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     */
    @JSStaticFunction
    public static void addLocalArchives(final Context ctx, final Scriptable thisObj, final Object[] args,
                                        final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;
        final Object arg1 = args.length >= 2 ? args[1] : Undefined.instance;

        if (args.length < 2) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.TWO_ARGS_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!JavaScriptUtils.isDefined(arg1)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_REQUIRED);
        } else if (!(arg0 instanceof ConfigurationWrap)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_MUST_BE_CONF);
        }

        DistributedCache.addLocalArchives(((ConfigurationWrap)arg0).getConf(), arg1.toString());
    }

    /**
     * Java wrapper for {@link DistributedCache#addLocalFiles(Configuration, String)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     */
    @JSStaticFunction
    public static void addLocalFiles(final Context ctx, final Scriptable thisObj, final Object[] args,
                                     final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;
        final Object arg1 = args.length >= 2 ? args[1] : Undefined.instance;

        if (args.length < 2) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.TWO_ARGS_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!JavaScriptUtils.isDefined(arg1)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_REQUIRED);
        } else if (!(arg0 instanceof ConfigurationWrap)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_MUST_BE_CONF);
        }

        DistributedCache.addLocalFiles(((ConfigurationWrap)arg0).getConf(), arg1.toString());
    }

    /**
     * Java wrapper for {@link DistributedCache#checkURIs(URI[], URI[])}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     *
     * @return whether or not the uris are without conflict or issue
     */
    @JSStaticFunction
    public static boolean checkURIs(final Context ctx, final Scriptable thisObj, final Object[] args,
                                    final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;
        final Object arg1 = args.length >= 2 ? args[1] : Undefined.instance;

        if (args.length < 2) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.TWO_ARGS_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!JavaScriptUtils.isDefined(arg1)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_REQUIRED);
        } else if (!(arg0 instanceof NativeArray)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_MUST_BE_ARRAY);
        } else if (!(arg1 instanceof NativeArray)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_MUST_BE_ARRAY);
        }

        final NativeArray jsFileUris = (NativeArray)arg0;
        final NativeArray jsArchiveUris = (NativeArray)arg1;
        final URI[] fileUris = new URI[(int)jsFileUris.getLength()];
        final URI[] archiveUris = new URI[(int)jsArchiveUris.getLength()];

        for (int i = 0; i < fileUris.length; i++) {
            fileUris[i] = URI.create(jsFileUris.get(i).toString());
        }

        for (int i = 0; i < archiveUris.length; i++) {
            archiveUris[i] = URI.create(jsArchiveUris.get(i).toString());
        }

        return DistributedCache.checkURIs(fileUris, archiveUris);
    }

    /**
     * Java wrapper for {@link DistributedCache#createAllSymlink(Configuration, File, File)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     */
    @JSStaticFunction
    public static void createAllSymlink(final Context ctx, final Scriptable thisObj, final Object[] args,
                                        final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;
        final Object arg1 = args.length >= 2 ? args[1] : Undefined.instance;
        final Object arg2 = args.length >= 3 ? args[2] : Undefined.instance;

        if (args.length < 3) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.THREE_ARGS_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!JavaScriptUtils.isDefined(arg1)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_REQUIRED);
        } else if (!JavaScriptUtils.isDefined(arg2)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.THIRD_ARG_REQUIRED);
        } else if (!(arg0 instanceof ConfigurationWrap)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_MUST_BE_CONF);
        }

        try {
            DistributedCache.createAllSymlink(((ConfigurationWrap)arg0).getConf(),
                                              new File(arg1.toString()),
                                              new File(arg2.toString()));
        } catch (IOException e) {
            throw Utils.makeError(ctx, thisObj, e.getMessage());
        }
    }

    /**
     * Java wrapper for {@link DistributedCache#createSymlink(Configuration)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     */
    @JSStaticFunction
    public static void createSymlink(final Context ctx, final Scriptable thisObj, final Object[] args,
                                     final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;

        if (args.length < 1) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!(arg0 instanceof ConfigurationWrap)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_MUST_BE_CONF);
        }

        DistributedCache.createSymlink(((ConfigurationWrap)arg0).getConf());
    }

    /**
     * Java wrapper for {@link DistributedCache#getArchiveClassPaths(Configuration)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     *
     * @return array of archive class paths
     */
    @JSStaticFunction
    public static Object getArchiveClassPaths(final Context ctx, final Scriptable thisObj, final Object[] args,
                                              final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;

        if (args.length < 1) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!(arg0 instanceof ConfigurationWrap)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_MUST_BE_CONF);
        }

        final Configuration conf = ((ConfigurationWrap)arg0).getConf();
        final Path[] archiveClassPaths = DistributedCache.getArchiveClassPaths(conf);
        final List<String> pathStrings = new ArrayList<>();

        if (archiveClassPaths != null) {
            for (final Path path : archiveClassPaths) {
                pathStrings.add(path.toUri().toString());
            }
        }

        return JavaScriptUtils.asArray(thisObj, pathStrings);
    }

    /**
     * Java wrapper for {@link DistributedCache#getArchiveTimestamps(Configuration)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     *
     * @return array of archive timestamps
     */
    @JSStaticFunction
    public static Object getArchiveTimestamps(final Context ctx, final Scriptable thisObj, final Object[] args,
                                              final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;

        if (args.length < 1) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!(arg0 instanceof ConfigurationWrap)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_MUST_BE_CONF);
        }

        return JavaScriptUtils.asArray(thisObj, DistributedCache.getArchiveTimestamps(
                ((ConfigurationWrap)arg0).getConf()));
    }

    /**
     * Java wrapper for {@link DistributedCache#getCacheArchives(Configuration)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     *
     * @return array of cache archive paths
     */
    @JSStaticFunction
    public static Object getCacheArchives(final Context ctx, final Scriptable thisObj, final Object[] args,
                                          final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;

        if (args.length < 1) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!(arg0 instanceof ConfigurationWrap)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_MUST_BE_CONF);
        }

        final Configuration conf = ((ConfigurationWrap)arg0).getConf();
        URI[] cacheArchives;

        try {
            cacheArchives = DistributedCache.getCacheArchives(conf);
        } catch (IOException e) {
            throw Utils.makeError(ctx, thisObj, e.getMessage());
        }

        final List<String> cacheArchiveStrs = new ArrayList<>();

        if (cacheArchives != null) {
            for (final URI uri : cacheArchives) {
                cacheArchiveStrs.add(uri.toString());
            }
        }

        return JavaScriptUtils.asArray(thisObj, cacheArchiveStrs);
    }

    /**
     * Java wrapper for {@link DistributedCache#getCacheFiles(Configuration)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     *
     * @return array of cache file paths
     */
    @JSStaticFunction
    public static Object getCacheFiles(final Context ctx, final Scriptable thisObj, final Object[] args,
                                       final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;

        if (args.length < 1) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!(arg0 instanceof ConfigurationWrap)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_MUST_BE_CONF);
        }

        final Configuration conf = ((ConfigurationWrap)arg0).getConf();
        URI[] cacheFiles;

        try {
            cacheFiles = DistributedCache.getCacheFiles(conf);
        } catch (IOException e) {
            throw Utils.makeError(ctx, thisObj, e.getMessage());
        }

        final List<String> cacheFileStrs = new ArrayList<>();

        if (cacheFiles != null) {
            for (final URI uri : cacheFiles) {
                cacheFileStrs.add(uri.toString());
            }
        }

        return JavaScriptUtils.asArray(thisObj, cacheFileStrs);
    }

    /**
     * Java wrapper for {@link DistributedCache#getFileClassPaths(Configuration)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     *
     * @return array of file class paths
     */
    @JSStaticFunction
    public static Object getFileClassPaths(final Context ctx, final Scriptable thisObj, final Object[] args,
                                           final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;

        if (args.length < 1) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!(arg0 instanceof ConfigurationWrap)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_MUST_BE_CONF);
        }

        final Configuration conf = ((ConfigurationWrap)arg0).getConf();
        final Path[] fileClassPaths = DistributedCache.getFileClassPaths(conf);
        final List<String> pathStrings = new ArrayList<>();

        if (fileClassPaths != null) {
            for (final Path path : fileClassPaths) {
                pathStrings.add(path.toUri().toString());
            }
        }

        return JavaScriptUtils.asArray(thisObj, pathStrings);
    }

    /**
     * Java wrapper for {@link DistributedCache#getFileStatus(Configuration, URI)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     *
     * @return array of archive class paths
     */
    @JSStaticFunction
    public static Object getFileStatus(final Context ctx, final Scriptable thisObj, final Object[] args,
                                       final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;
        final Object arg1 = args.length >= 2 ? args[1] : Undefined.instance;

        if (args.length < 2) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.TWO_ARGS_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!JavaScriptUtils.isDefined(arg1)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_REQUIRED);
        } else if (!(arg0 instanceof ConfigurationWrap)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_MUST_BE_CONF);
        }

        final URI hdfsUri = URI.create(arg1.toString());
        FileStatus status;

        try {
            status = DistributedCache.getFileStatus(((ConfigurationWrap)arg0).getConf(), hdfsUri);
        } catch (IOException e) {
            throw Utils.makeError(ctx, thisObj, e.getMessage());
        }

        if (status == null) {
            throw Utils.makeError(ctx, thisObj, "Unable to get file status for HDFS uri: " + hdfsUri.toString());
        }

        final Scriptable jsStatus = ctx.newObject(thisObj);

        ScriptableObject.defineProperty(jsStatus, "accessTime", status.getAccessTime(), ScriptableObject.READONLY);
        ScriptableObject.defineProperty(jsStatus, "blockSize", status.getBlockSize(), ScriptableObject.READONLY);
        ScriptableObject.defineProperty(jsStatus, "group", status.getGroup(), ScriptableObject.READONLY);
        ScriptableObject.defineProperty(jsStatus, "len", status.getLen(), ScriptableObject.READONLY);
        ScriptableObject.defineProperty(jsStatus, "modificationTime", status.getModificationTime(),
                                        ScriptableObject.READONLY);
        ScriptableObject.defineProperty(jsStatus, "owner", status.getOwner(), ScriptableObject.READONLY);
        ScriptableObject.defineProperty(jsStatus, "path", status.getPath().toString(), ScriptableObject.READONLY);
        ScriptableObject.defineProperty(jsStatus, "permission", status.getPermission().toString(),
                                        ScriptableObject.READONLY);
        ScriptableObject.defineProperty(jsStatus, "replication", status.getReplication(), ScriptableObject.READONLY);

        return jsStatus;
    }

    /**
     * Java wrapper for {@link DistributedCache#getFileTimestamps(Configuration)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     *
     * @return array of file timestamps
     */
    @JSStaticFunction
    public static Object getFileTimestamps(final Context ctx, final Scriptable thisObj, final Object[] args,
                                           final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;

        if (args.length < 1) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!(arg0 instanceof ConfigurationWrap)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_MUST_BE_CONF);
        }

        return JavaScriptUtils.asArray(thisObj, DistributedCache.getFileTimestamps(
                ((ConfigurationWrap)arg0).getConf()));
    }

    /**
     * Java wrapper for {@link DistributedCache#getLocalCacheArchives(Configuration)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     *
     * @return array of cache archive paths
     */
    @JSStaticFunction
    public static Object getLocalCacheArchives(final Context ctx, final Scriptable thisObj, final Object[] args,
                                               final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;

        if (args.length < 1) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!(arg0 instanceof ConfigurationWrap)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_MUST_BE_CONF);
        }

        final Configuration conf = ((ConfigurationWrap)arg0).getConf();
        Path[] cacheArchives;

        try {
            cacheArchives = DistributedCache.getLocalCacheArchives(conf);
        } catch (IOException e) {
            throw Utils.makeError(ctx, thisObj, e.getMessage());
        }

        final List<String> cacheArchiveStrs = new ArrayList<>();

        if (cacheArchives != null) {
            for (final Path path : cacheArchives) {
                cacheArchiveStrs.add(path.toUri().toString());
            }
        }

        return JavaScriptUtils.asArray(thisObj, cacheArchiveStrs);
    }

    /**
     * Java wrapper for {@link DistributedCache#getLocalCacheFiles(Configuration)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     *
     * @return array of cache file paths
     */
    @JSStaticFunction
    public static Object getLocalCacheFiles(final Context ctx, final Scriptable thisObj, final Object[] args,
                                            final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;

        if (args.length < 1) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!(arg0 instanceof ConfigurationWrap)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_MUST_BE_CONF);
        }

        final Configuration conf = ((ConfigurationWrap)arg0).getConf();
        Path[] cacheFiles;

        try {
            cacheFiles = DistributedCache.getLocalCacheFiles(conf);
        } catch (IOException e) {
            throw Utils.makeError(ctx, thisObj, e.getMessage());
        }

        final List<String> cacheFileStrs = new ArrayList<>();

        if (cacheFiles != null) {
            for (final Path path : cacheFiles) {
                cacheFileStrs.add(path.toUri().toString());
            }
        }

        return JavaScriptUtils.asArray(thisObj, cacheFileStrs);
    }

    /**
     * Java wrapper for {@link DistributedCache#getSymlink(Configuration)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     *
     * @return whether or not symlinks are to be created
     */
    @JSStaticFunction
    public static Object getSymlink(final Context ctx, final Scriptable thisObj, final Object[] args,
                                    final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;

        if (args.length < 1) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!(arg0 instanceof ConfigurationWrap)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_MUST_BE_CONF);
        }

        return DistributedCache.getSymlink(((ConfigurationWrap)arg0).getConf());
    }

    /**
     * Java wrapper for {@link DistributedCache#getTimestamp(Configuration, URI)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     *
     * @return the timestamp of the cache entry
     */
    @JSStaticFunction
    public static Object getTimestamp(final Context ctx, final Scriptable thisObj, final Object[] args,
                                      final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;
        final Object arg1 = args.length >= 2 ? args[1] : Undefined.instance;

        if (args.length < 2) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.TWO_ARGS_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!JavaScriptUtils.isDefined(arg1)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_REQUIRED);
        } else if (!(arg0 instanceof ConfigurationWrap)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_MUST_BE_CONF);
        }

        try {
            return DistributedCache.getTimestamp(((ConfigurationWrap)arg0).getConf(),
                                                 URI.create(arg1.toString()));
        } catch (IOException e) {
            throw Utils.makeError(ctx, thisObj, e.getMessage());
        }
    }

    /**
     * Java wrapper for {@link DistributedCache#setArchiveTimestamps(Configuration, String)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     */
    @JSStaticFunction
    public static void setArchiveTimestamps(final Context ctx, final Scriptable thisObj, final Object[] args,
                                            final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;
        final Object arg1 = args.length >= 2 ? args[1] : Undefined.instance;

        if (args.length < 2) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.TWO_ARGS_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!JavaScriptUtils.isDefined(arg1)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_REQUIRED);
        } else if (!(arg0 instanceof ConfigurationWrap)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_MUST_BE_CONF);
        }

        DistributedCache.setArchiveTimestamps(((ConfigurationWrap)arg0).getConf(), arg1.toString());
    }

    /**
     * Java wrapper for {@link DistributedCache#setCacheArchives(URI[], Configuration)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     */
    @JSStaticFunction
    public static void setCacheArchives(final Context ctx, final Scriptable thisObj, final Object[] args,
                                        final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;
        final Object arg1 = args.length >= 2 ? args[1] : Undefined.instance;

        if (args.length < 2) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.TWO_ARGS_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!JavaScriptUtils.isDefined(arg1)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_REQUIRED);
        } else if (!(arg0 instanceof NativeArray)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_MUST_BE_ARRAY);
        } else if (!(arg1 instanceof ConfigurationWrap)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_MUST_BE_CONF);
        }

        final NativeArray jsArchiveUris = (NativeArray)arg0;
        final URI[] archiveUris = new URI[(int)jsArchiveUris.getLength()];

        for (int i = 0; i < archiveUris.length; i++) {
            archiveUris[i] = URI.create(jsArchiveUris.get(i).toString());
        }

        DistributedCache.setCacheArchives(archiveUris, ((ConfigurationWrap)arg1).getConf());
    }

    /**
     * Java wrapper for {@link DistributedCache#setCacheFiles(URI[], Configuration)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     */
    @JSStaticFunction
    public static void setCacheFiles(final Context ctx, final Scriptable thisObj, final Object[] args,
                                     final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;
        final Object arg1 = args.length >= 2 ? args[1] : Undefined.instance;

        if (args.length < 2) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.TWO_ARGS_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!JavaScriptUtils.isDefined(arg1)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_REQUIRED);
        } else if (!(arg0 instanceof NativeArray)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_MUST_BE_ARRAY);
        } else if (!(arg1 instanceof ConfigurationWrap)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_MUST_BE_CONF);
        }

        final NativeArray jsFileUris = (NativeArray)arg0;
        final URI[] fileUris = new URI[(int)jsFileUris.getLength()];

        for (int i = 0; i < fileUris.length; i++) {
            fileUris[i] = URI.create(jsFileUris.get(i).toString());
        }

        DistributedCache.setCacheFiles(fileUris, ((ConfigurationWrap)arg1).getConf());
    }

    /**
     * Java wrapper for {@link DistributedCache#setFileTimestamps(Configuration, String)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     */
    @JSStaticFunction
    public static void setFileTimestamps(final Context ctx, final Scriptable thisObj, final Object[] args,
                                         final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;
        final Object arg1 = args.length >= 2 ? args[1] : Undefined.instance;

        if (args.length < 2) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.TWO_ARGS_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!JavaScriptUtils.isDefined(arg1)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_REQUIRED);
        } else if (!(arg0 instanceof ConfigurationWrap)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_MUST_BE_CONF);
        }

        DistributedCache.setFileTimestamps(((ConfigurationWrap)arg0).getConf(), arg1.toString());
    }

    /**
     * Java wrapper for {@link DistributedCache#setLocalArchives(Configuration, String)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     */
    @JSStaticFunction
    public static void setLocalArchives(final Context ctx, final Scriptable thisObj, final Object[] args,
                                        final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;
        final Object arg1 = args.length >= 2 ? args[1] : Undefined.instance;

        if (args.length < 2) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.TWO_ARGS_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!JavaScriptUtils.isDefined(arg1)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_REQUIRED);
        } else if (!(arg0 instanceof ConfigurationWrap)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_MUST_BE_CONF);
        }

        DistributedCache.setLocalArchives(((ConfigurationWrap)arg0).getConf(), arg1.toString());
    }

    /**
     * Java wrapper for {@link DistributedCache#setLocalFiles(Configuration, String)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     */
    @JSStaticFunction
    public static void setLocalFiles(final Context ctx, final Scriptable thisObj, final Object[] args,
                                     final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;
        final Object arg1 = args.length >= 2 ? args[1] : Undefined.instance;

        if (args.length < 2) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.TWO_ARGS_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!JavaScriptUtils.isDefined(arg1)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_REQUIRED);
        } else if (!(arg0 instanceof ConfigurationWrap)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_MUST_BE_CONF);
        }

        DistributedCache.setLocalFiles(((ConfigurationWrap)arg0).getConf(), arg1.toString());
    }

    /* Java Methods */

    /**
     * {@inheritDoc}
     */
    @Override
    public String getClassName() {
        return CLASS_NAME;
    }

}
