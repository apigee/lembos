package io.apigee.lembos.mapreduce;

import com.google.common.collect.ImmutableList;
import io.apigee.lembos.utils.ConversionUtils;
import io.apigee.lembos.utils.TestUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.apache.hadoop.mrunit.types.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link LembosReducer}.
 */
public class LembosReducerTest {

    private ReduceDriver<WritableComparable<?>, Writable, WritableComparable<?>, Writable> driver;
    private LembosReducer reducer;

    /**
     * Prepares the tests to run.
     *
     * @throws Exception if anything goes wrong
     */
    @Before
    public void beforeTest() throws Exception {
        reducer = new LembosReducer();
        driver = new ReduceDriver<>();

        driver.withReducer(reducer);
    }

    /**
     * Tests the proper error is thrown when the specified module doesn't expose a 'reduce' property.
     *
     * @throws Exception if anything goes wrong.
     */
    @Test
    public void testMissingReduceFunction() throws Exception {
        final String moduleName = "LembosReducerTest-testMissingReduceFunction";

        driver.getConfiguration().set(LembosConstants.MR_MODULE_NAME, moduleName);
        driver.getConfiguration().set(LembosConstants.MR_MODULE_PATH, TestUtils.getModulePath(moduleName));

        final LembosMapReduceEnvironment env = LembosMapReduceEnvironment.fromConf(driver.getConfiguration());
        final WritableComparable<?> key = ConversionUtils.jsToWritableComparable("total", null);
        final Writable val = ConversionUtils.jsToWritable("total", null);

        env.cleanup();

        driver.withInput(key, ImmutableList.of(val, val, val, val, val));

        try {
            driver.run();

            fail("The line above should had failed");
        } catch (RuntimeException e) {
            assertEquals("MapReduce function 'reduce' is not defined", e.getMessage());
        }
    }

    /**
     * Tests that the reducer works as expected.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testReducer() throws Exception {
        final String moduleName = "LembosReducerTest-testReducer";

        driver.getConfiguration().set(LembosConstants.MR_MODULE_NAME, moduleName);
        driver.getConfiguration().set(LembosConstants.MR_MODULE_PATH, TestUtils.getModulePath(moduleName));

        final LembosMapReduceEnvironment env = LembosMapReduceEnvironment.fromConf(driver.getConfiguration());
        final WritableComparable<?> key = ConversionUtils.jsToWritableComparable("total", null);
        final Writable val = ConversionUtils.jsToWritable(1, null);

        env.cleanup();

        driver.withInput(key, ImmutableList.of(val, val, val, val, val));

        final List<Pair<WritableComparable<?>, Writable>> outputs = driver.run();

        assertEquals(1, outputs.size());

        final Pair<WritableComparable<?>, Writable> output = outputs.get(0);

        assertEquals("total", output.getFirst().toString());
        assertEquals(5, ((IntWritable)output.getSecond()).get());
    }

}
