package io.apigee.lembos.mapreduce;

import com.google.common.collect.ImmutableList;
import io.apigee.lembos.utils.TestUtils;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.types.Pair;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link LembosMapReduceEnvironment}.
 */
public class LembosMapReduceEnvironmentTest {

    /**
     * Tests the proper error is thrown when the specified module is null or an empty string.
     *
     * @throws Exception if anything goes wrong.
     */
    @Test
    public void testNullAndEmptyModule() throws Exception {
        try {
            LembosMapReduceEnvironment.fromConf(new Configuration());

            fail("The line above should had failed");
        } catch (Exception e) {
            assertEquals("Module name cannot be null", e.getMessage());
        }

        try {
            final Configuration conf = new Configuration();

            conf.set(LembosConstants.MR_MODULE_NAME, "     ");

            LembosMapReduceEnvironment.fromConf(conf);

            fail("The line above should had failed");
        } catch (Exception e) {
            assertEquals("Module name cannot be null", e.getMessage());
        }
    }

    /**
     * Tests the proper error is thrown when the specified module doesn't exist, or can't be found.
     *
     * <b>Note:</b> This test is really just here to improve the code coverage report as there is no real need in unit
     * testing Trireme.
     *
     * @throws Exception if anything goes wrong.
     */
    @Test
    public void testMissingModule() throws Exception {
        final String moduleName = "LembosMapReduceEnvironmentTest-testMissingModule";

        try {
            final Configuration conf = new Configuration();

            conf.set(LembosConstants.MR_MODULE_NAME, moduleName);
            conf.set(LembosConstants.MR_MODULE_PATH, TestUtils.getModulePath("/"));

            LembosMapReduceEnvironment.fromConf(conf);

            fail("The line above should had failed");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Cannot find module '" + moduleName + "'"));
        }
    }

    /**
     * Tests the proper error is thrown when the specified module's 'config' property is of the wrong type.
     *
     * @throws Exception if anything goes wrong.
     */
    @Test
    public void testConfigObjectWrongType() throws Exception {
        try {
            final Configuration conf = new Configuration();
            final String moduleName = "LembosMapReduceEnvironmentTest-testConfigObjectWrongType";

            conf.set(LembosConstants.MR_MODULE_NAME, moduleName);
            conf.set(LembosConstants.MR_MODULE_PATH, TestUtils.getModulePath(moduleName));

            LembosMapReduceEnvironment.fromConf(conf);

            fail("The line above should had failed");
        } catch (Exception e) {
            assertEquals("MapReduce object (config) is not a object", e.getMessage());
        }
    }

    /**
     * Tests the proper error is thrown when the specified module doesn't expose a 'map' property.
     *
     * @throws Exception if anything goes wrong.
     */
    @Test
    public void testMissingMapFunction() throws Exception {
        try {
            final Configuration conf = new Configuration();
            final String moduleName = "LembosMapReduceEnvironmentTest-testMissingMapFunction";

            conf.set(LembosConstants.MR_MODULE_NAME, moduleName);
            conf.set(LembosConstants.MR_MODULE_PATH, TestUtils.getModulePath(moduleName));

            LembosMapReduceEnvironment.fromConf(conf);

            fail("The line above should had failed");
        } catch (Exception e) {
            assertEquals("Required MapReduce function (map) not found", e.getMessage());
        }
    }

    /**
     * Tests the proper error is thrown when the specified module's 'map' property is of the wrong type.
     *
     * @throws Exception if anything goes wrong.
     */
    @Test
    public void testMapFunctionWrongType() throws Exception {
        try {
            final Configuration conf = new Configuration();
            final String moduleName = "LembosMapReduceEnvironmentTest-testMapFunctionWrongType";

            conf.set(LembosConstants.MR_MODULE_NAME, moduleName);
            conf.set(LembosConstants.MR_MODULE_PATH, TestUtils.getModulePath(moduleName));

            LembosMapReduceEnvironment.fromConf(conf);

            fail("The line above should had failed");
        } catch (Exception e) {
            assertEquals("MapReduce function (map) is not a function", e.getMessage());
        }
    }

    /**
     * Tests a minimal working {@link LembosMapReduceEnvironment}.
     *
     * @throws Exception if anything goes wrong.
     */
    @Test
    public void testMinimalLembosMapReduceEnvironment() throws Exception {
        final Configuration conf = new Configuration();
        final String moduleName = "LembosMapReduceEnvironmentTest-testMinimalLembosMapReduceEnvironment";

        conf.set(LembosConstants.MR_MODULE_NAME, moduleName);
        conf.set(LembosConstants.MR_MODULE_PATH, TestUtils.getModulePath(moduleName));

        final LembosMapReduceEnvironment env = LembosMapReduceEnvironment.fromConf(conf);

        assertNotNull(env.getMapFunction());
        assertNotNull(env.getModule());
        assertNotNull(env.getRunningScript());

        assertNull(env.getCombineCleanupFunction());
        assertNull(env.getCombineFunction());
        assertNull(env.getCombineSetupFunction());
        assertNull(env.getConfiguration());
        assertNull(env.getGroupCleanupFunction());
        assertNull(env.getGroupFunction());
        assertNull(env.getGroupSetupFunction());
        assertNull(env.getJobCleanupFunction());
        assertNull(env.getJobSetupFunction());
        assertNull(env.getMapCleanupFunction());
        assertNull(env.getMapSetupFunction());
        assertNull(env.getPartitionCleanupFunction());
        assertNull(env.getPartitionFunction());
        assertNull(env.getPartitionSetupFunction());
        assertNull(env.getReduceCleanupFunction());
        assertNull(env.getReduceFunction());
        assertNull(env.getReduceSetupFunction());
        assertNull(env.getSortCleanupFunction());
        assertNull(env.getSortFunction());
        assertNull(env.getSortSetupFunction());
    }

    /**
     * Tests a complete working {@link LembosMapReduceEnvironment}.
     *
     * @throws Exception if anything goes wrong.
     */
    @Test
    public void testCompleteLembosMapReduceEnvironment() throws Exception {
        final Configuration conf = new Configuration();
        final String moduleName = "LembosMapReduceEnvironmentTest-testCompleteLembosMapReduceEnvironment";

        conf.set(LembosConstants.MR_MODULE_NAME, moduleName);
        conf.set(LembosConstants.MR_MODULE_PATH, TestUtils.getModulePath(moduleName));

        final LembosMapReduceEnvironment env = LembosMapReduceEnvironment.fromConf(conf);

        assertNotNull(env.getMapFunction());
        assertNotNull(env.getModule());
        assertNotNull(env.getRunningScript());
        assertNotNull(env.getCombineCleanupFunction());
        assertNotNull(env.getCombineFunction());
        assertNotNull(env.getCombineSetupFunction());
        assertNotNull(env.getConfiguration());
        assertNotNull(env.getGroupCleanupFunction());
        assertNotNull(env.getGroupFunction());
        assertNotNull(env.getGroupSetupFunction());
        assertNotNull(env.getJobCleanupFunction());
        assertNotNull(env.getJobSetupFunction());
        assertNotNull(env.getMapCleanupFunction());
        assertNotNull(env.getMapSetupFunction());
        assertNotNull(env.getPartitionCleanupFunction());
        assertNotNull(env.getPartitionFunction());
        assertNotNull(env.getPartitionSetupFunction());
        assertNotNull(env.getReduceCleanupFunction());
        assertNotNull(env.getReduceFunction());
        assertNotNull(env.getReduceSetupFunction());
        assertNotNull(env.getSortCleanupFunction());
        assertNotNull(env.getSortFunction());
        assertNotNull(env.getSortSetupFunction());
    }

    /**
     * Tests that an error thrown from JavaScript works as expected.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testThrowingErrorsWorks() throws Exception {
        final MapDriver<WritableComparable<?>, Writable, WritableComparable<?>, Writable> driver =
                new MapDriver<>();
        final LembosMapper mapper = new LembosMapper();
        final String moduleName = "LembosMapReduceEnvironmentTest-testThrowingErrorsWorks";

        driver.withMapper(mapper);

        driver.getConfiguration().set(LembosConstants.MR_MODULE_NAME, moduleName);
        driver.getConfiguration().set(LembosConstants.MR_MODULE_PATH, TestUtils.getModulePath(moduleName));

        final List<Pair<WritableComparable<?>, Writable>> inputs = ImmutableList.of(
                new Pair<WritableComparable<?>, Writable>(new Text(Long.toString(new Date().getTime())),
                                                                 new Text("Alice")),
                new Pair<WritableComparable<?>, Writable>(new Text(Long.toString(new Date().getTime())),
                                                                 new Text("Bob")),
                new Pair<WritableComparable<?>, Writable>(new Text(Long.toString(new Date().getTime())),
                                                                 new Text("Sally")),
                new Pair<WritableComparable<?>, Writable>(new Text(Long.toString(new Date().getTime())),
                                                                 new Text("Bob")),
                new Pair<WritableComparable<?>, Writable>(new Text(Long.toString(new Date().getTime())),
                                                                 new Text("Alice"))
        );

        driver.withAll(inputs);

        try {
            driver.run();

            fail("The line above should had failed");
        } catch (RuntimeException e) {
            assertEquals("Error: This is an Error!", e.getMessage());
        }
    }

    /**
     * Tests using a Node.js module works as expected.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testUsingNodeJSModule() throws Exception {
        final MapDriver<WritableComparable<?>, Writable, WritableComparable<?>, Writable> driver =
                new MapDriver<>();
        final LembosMapper mapper = new LembosMapper();
        final String moduleName = "LembosMapReduceEnvironmentTest-testUsingNodeJSModule";

        driver.withMapper(mapper);

        driver.getConfiguration().set(LembosConstants.MR_MODULE_NAME, moduleName);
        driver.getConfiguration().set(LembosConstants.MR_MODULE_PATH, TestUtils.getModulePath(moduleName));

        final List<Pair<WritableComparable<?>, Writable>> inputs = ImmutableList.of(
                new Pair<WritableComparable<?>, Writable>(new Text(Long.toString(new Date().getTime())),
                                                                 new Text("Alice"))
        );

        driver.withAll(inputs);

        final List<Pair<WritableComparable<?>, Writable>> outputs = driver.run();

        assertEquals(1, outputs.size());

        final Pair<WritableComparable<?>, Writable> output = outputs.get(0);
        final String key = output.getFirst().toString();
        final String val = output.getSecond().toString();

        assertEquals("ip", key);
        assertTrue(InetAddressValidator.getInstance().isValidInet4Address(val));
    }

}
