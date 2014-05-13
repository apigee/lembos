package io.apigee.lembos.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Properties;

/**
 * Utilities for testing.
 */
public class TestUtils {

    /**
     * Returns the full path to the examples directory.
     *
     * <b>Note:</b> Kind of hacky but no better way to do it in a way that works with all different ways to run tests.
     *
     * @return the examples path
     */
    public static String getExamplesPath() {
        final File startPath = new File(TestUtils.class.getResource("/").getPath());
        final String[] pathSegments = startPath.getAbsolutePath().split(File.separator);
        String examplesPath = null;

        for (int i = pathSegments.length - 1; i > 0; i--) {
            final File currDir = new File(StringUtils.join(Arrays.copyOf(pathSegments, i), File.separator));
            final File[] files = currDir.listFiles();

            if (files != null) {
                for (final File file : files) {
                    if (file.isDirectory() && file.getName().equals("examples")) {
                        final File[] examples = file.listFiles();

                        if (examples != null) {
                            for (final File example : examples) {
                                if (example.getName().equals("wordcount")) {
                                    examplesPath = file.getAbsolutePath();
                                    break;
                                }
                            }
                        }
                    }

                    if (examplesPath != null) {
                        break;
                    }
                }
            }
        }

        return examplesPath;
    }

    /**
     * Returns the full path to the Node.js module.
     *
     * @param moduleName the name of the module
     *
     * @return the module path
     */
    public static String getModulePath(final String moduleName) {
        return TestUtils.class.getResource("/").getPath() + "node_modules/" + moduleName;
    }

    /**
     * Creates and returns a {@link Scriptable} representing a root scope.
     *
     * @param scriptName the script name
     * @param scriptContent the script content
     *
     * @return the root scope
     */
    public static Scriptable createScriptable(final String scriptName, final String scriptContent) {
        final Context ctx = Context.enter();
        final String realScriptName = scriptName == null ? "<cmd>" : scriptName;
        final String realScriptContent = scriptContent == null ? "" : scriptContent;

        try {
            // Turn off wrapping of primitives
            ctx.getWrapFactory().setJavaPrimitiveWrap(false);
            // Set the language level to JavaScript 1.8
            ctx.setLanguageVersion(Context.VERSION_1_8);

            final Scriptable scope = ctx.initStandardObjects();

            ctx.evaluateString(scope, realScriptContent, realScriptName, 1, null);

            return scope;
        } finally {
            Context.exit();
        }
    }

    public static Configuration getConfiguration() throws IOException {
        final Properties itProperties = new Properties();
        final URL propsUrl = TestUtils.class.getResource("/integration-test.properties");
        String fsDefaultName = null;
        String jobTracker = null;
        String userName = null;

        if (propsUrl != null) {
            itProperties.load(propsUrl.openStream());

            fsDefaultName = itProperties.getProperty("fs.default.name");
            jobTracker = itProperties.getProperty("mapred.job.tracker");
            userName = itProperties.getProperty("user.name");
        }

        if (fsDefaultName == null) {
            fsDefaultName = System.getProperty("fs.default.name", "hdfs://localhost:54310");
        }

        if (jobTracker == null) {
            jobTracker = System.getProperty("mapred.job.tracker", "localhost:54311");
        }

        if (userName == null) {
            userName = System.getProperty("user.name");
        }

        final Configuration conf = new Configuration();

        conf.set("fs.default.name", fsDefaultName);
        conf.set("mapred.job.tracker", jobTracker);
        conf.set("hadoop.user", userName);

        return conf;
    }

}
