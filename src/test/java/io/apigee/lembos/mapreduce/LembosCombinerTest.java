package io.apigee.lembos.mapreduce;

import com.google.common.collect.ImmutableList;
import io.apigee.lembos.utils.TestUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
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
 * Unit tests for {@link LembosCombiner}.
 */
public class LembosCombinerTest {

    private ReduceDriver<WritableComparable<?>, Writable, WritableComparable<?>, Writable> driver;
    private LembosCombiner combiner;

    /**
     * Prepares the tests to run.
     *
     * @throws Exception if anything goes wrong
     */
    @Before
    public void beforeTest() throws Exception {
        combiner = new LembosCombiner();
        driver = new ReduceDriver<>();

        driver.withReducer(combiner);
    }

    /**
     * Tests the proper error is thrown when the specified module doesn't expose a 'combine' property.
     *
     * @throws Exception if anything goes wrong.
     */
    @Test
    public void testMissingCombineFunction() throws Exception {
        final String moduleName = "LembosCombinerTest-testMissingCombineFunction";

        driver.getConfiguration().set(LembosConstants.MR_MODULE_NAME, moduleName);
        driver.getConfiguration().set(LembosConstants.MR_MODULE_PATH, TestUtils.getModulePath(moduleName));

        final LembosMapReduceEnvironment env = LembosMapReduceEnvironment.fromConf(driver.getConfiguration());
        final WritableComparable<?> key = new Text("total");
        final Writable val = new IntWritable(1);

        env.cleanup();

        driver.withInput(key, ImmutableList.of(val, val, val, val, val));

        try {
            driver.run();

            fail("The line above should had failed");
        } catch (RuntimeException e) {
            assertEquals("MapReduce function 'combine' is not defined", e.getMessage());
        }
    }

    /**
     * Tests that the combiner works as expected.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testCombiner() throws Exception {
        final String moduleName = "LembosCombinerTest-testCombiner";

        driver.getConfiguration().set(LembosConstants.MR_MODULE_NAME, moduleName);
        driver.getConfiguration().set(LembosConstants.MR_MODULE_PATH, TestUtils.getModulePath(moduleName));

        final LembosMapReduceEnvironment env = LembosMapReduceEnvironment.fromConf(driver.getConfiguration());
        final WritableComparable<?> key = new Text("total");
        final Writable val = new IntWritable(1);

        env.cleanup();

        driver.withInput(key, ImmutableList.of(val, val, val, val, val));

        final List<Pair<WritableComparable<?>, Writable>> outputs = driver.run();

        assertEquals(1, outputs.size());

        final Pair<WritableComparable<?>, Writable> output = outputs.get(0);

        assertEquals("total", output.getFirst().toString());
        assertEquals(5, ((IntWritable)output.getSecond()).get());
    }

}
