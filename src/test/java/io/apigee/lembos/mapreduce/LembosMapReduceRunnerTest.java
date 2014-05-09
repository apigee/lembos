package io.apigee.lembos.mapreduce;

import io.apigee.lembos.utils.TestUtils;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.partition.HashPartitioner;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link LembosMapReduceRunner}.
 */
public class LembosMapReduceRunnerTest {

    /**
     * Test {@link LembosMapReduceRunner#getConf()} works as expected.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testGetConf() throws Exception {
        assertNull(new LembosMapReduceRunner().getConf());
    }

    /**
     * Test {@link LembosMapReduceRunner#initJob(String[])} works as expected for a map only job.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testMapOnlyJob() throws Exception {
        final String moduleName = "LembosMapReduceRunnerTest-testMapOnlyJob";
        final String modulePath = TestUtils.getModulePath(moduleName);
        final Job job = getJob(moduleName, modulePath, null, null);

        assertNotNull(job.getMapperClass());
        assertNull(job.getCombinerClass());
        // assertNull(job.getGroupingComparator()); // Throws an exception because our map output key is
                                                    // WritableComparable and can't subclass itself
        assertEquals(HashPartitioner.class, job.getPartitionerClass());
        assertEquals(Reducer.class, job.getReducerClass()); // Defaults to the Hadoop Reducer
        // assertNull(job.getSortComparator()); // Throws an exception because our map output key is
                                                // WritableComparable and can't subclass itself

        assertNull(job.getConfiguration().get("boolean"));
        assertNull(job.getConfiguration().get("double"));
        assertNull(job.getConfiguration().get("float"));
        assertNull(job.getConfiguration().get("int"));
        assertNull(job.getConfiguration().get("long"));
        assertNull(job.getConfiguration().get("string"));
    }

    /**
     * Test {@link LembosMapReduceRunner#initJob(String[])} works as expected for a full job.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testCompleteJob() throws Exception {
        final String moduleName = "LembosMapReduceRunnerTest-testCompleteJob";
        final String modulePath = TestUtils.getModulePath(moduleName);
        final Job job = getJob(moduleName, modulePath, null, null);

        assertNotNull(job.getMapperClass());

        assertEquals(LembosCombiner.class, job.getCombinerClass());
        assertEquals(LembosGroupComparator.class, job.getGroupingComparator().getClass());
        assertEquals(LembosPartitioner.class, job.getPartitionerClass());
        assertEquals(LembosReducer.class, job.getReducerClass());
        assertEquals(LembosSortComparator.class, job.getSortComparator().getClass());

        assertTrue(job.getConfiguration().getBoolean("boolean", false));
        assertEquals(1.1, Double.valueOf(Float.toString(job.getConfiguration().getFloat("double", 0.0f))), 0);
        assertEquals(1.23456789f, job.getConfiguration().getFloat("float", 0.0f), 0);
        assertEquals(1, job.getConfiguration().getInt("int", 0));
        assertEquals(2147483648L, job.getConfiguration().getLong("long", 0L));
        assertEquals("hello", job.getConfiguration().get("string"));
    }

    /**
     * Test {@link LembosMapReduceRunner#initJob(String[])} works as expected when -libjars was supplied.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testLibJars() throws Exception {
        final String moduleName = "LembosMapReduceRunnerTest-testLibJars";
        final String modulePath = TestUtils.getModulePath(moduleName);

        // If this call completes successfully, our test is successful (We have the actual test in the JavaScript code)
        getJob(moduleName, modulePath, new File[] {
                new File(modulePath + File.separator + "jars" + File.separator +
                                 "lembos-mapreduce-test-libjars-1.0.jar")
        }, null);
    }

    /**
     * Test {@link LembosMapReduceRunner#initJob(String[])} works as expected with application arguments.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testApplicationCLIArgs() throws Exception {
        final String moduleName = "LembosMapReduceRunnerTest-testApplicationCLIArgs";
        final String modulePath = TestUtils.getModulePath(moduleName);

        // If this call completes successfully, our test is successful (We have the actual test in the JavaScript code)
        getJob(moduleName, modulePath, null, new String[] {
                "test"
        });
    }

    /**
     * Helper to create a job the same way the {@link LembosMapReduceRunner} would.
     *
     * @param moduleName the module name
     * @param modulePath the module path
     * @param libJars the libjars to use
     * @param cliArgs the command line arguments
     *
     * @return the job
     *
     * @throws Exception if anything goes wrong.
     */
    private Job getJob(final String moduleName, final String modulePath, final File[] libJars, final String[] cliArgs)
            throws Exception {
        final List<String> args = new ArrayList<>();

        args.add("-D");
        args.add(LembosConstants.MR_MODULE_NAME + "=" + moduleName);
        args.add("-D");
        args.add(LembosConstants.MR_MODULE_PATH + "=" + modulePath);

        if (libJars != null) {
            final StringBuilder libJarsBuilder = new StringBuilder();

            for (final File libJar : libJars) {
                if (libJarsBuilder.toString().length() > 0) {
                    libJarsBuilder.append(",");
                }

                libJarsBuilder.append(libJar.toURI().toURL().toExternalForm());
            }

            args.add("-libjars");
            args.add(libJarsBuilder.toString());
        }

        if (cliArgs != null) {
            Collections.addAll(args, cliArgs);
        }

        return new LembosMapReduceRunner().initJob(args.toArray(new String[args.size()]));
    }

}
