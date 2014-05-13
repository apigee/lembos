package io.apigee.lembos.node.modules;

import com.google.common.collect.ImmutableList;
import io.apigee.lembos.mapreduce.LembosConstants;
import io.apigee.lembos.mapreduce.LembosMapper;
import io.apigee.lembos.utils.TestUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.types.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

/**
 * Unit tests for {@link HadoopInternal}.
 */
public class HadoopInternalTest {

    private final List<Pair<WritableComparable<?>, Writable>> inputs = ImmutableList.of(
            new Pair<WritableComparable<?>, Writable>(new Text(Long.toString(new Date().getTime())),
                                                             new Text("Alice"))
    );

    private MapDriver<WritableComparable<?>, Writable, WritableComparable<?>, Writable> driver;

    /**
     * Prepares the tests to run.
     *
     * @throws Exception if anything goes wrong
     */
    @Before
    public void beforeTest() throws Exception {
        driver = new MapDriver<>();

        driver.withMapper(new LembosMapper());
    }

    /**
     * Tests that the mapper works as expected when ran with a reducer.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testCounter() throws Exception {
        final String moduleName = "HadoopInternalTest-testCounter";

        driver.getConfiguration().set(LembosConstants.MR_MODULE_NAME, moduleName);
        driver.getConfiguration().set(LembosConstants.MR_MODULE_PATH, TestUtils.getModulePath(moduleName));
        driver.withAll(inputs);

        // Call JavaScript-based Tests
        driver.run();
    }

}
