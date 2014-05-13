package io.apigee.lembos.node.types;

import com.google.common.collect.ImmutableList;
import io.apigee.lembos.mapreduce.LembosMapper;
import io.apigee.lembos.mapreduce.LembosConstants;
import io.apigee.lembos.utils.TestUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.types.Pair;
import org.junit.Before;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link TaskInputOutputContextWrap}.
 */
public class TaskInputOutputContextWrapTest {

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
     * Tests the usage of {@link TaskInputOutputContextWrap#write(Context, Scriptable, Object[], Function)} with the wrong
     * number of arguments.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testWriteWithWrongNumberOfArguments() throws Exception {
        final String moduleName = "TaskInputOutputContextWrapTest-testWriteWithWrongNumberOfArguments";

        driver.getConfiguration().set(LembosConstants.MR_MODULE_NAME, moduleName);
        driver.getConfiguration().set(LembosConstants.MR_MODULE_PATH, TestUtils.getModulePath(moduleName));
        driver.withAll(ImmutableList.of(
                new Pair<WritableComparable<?>, Writable>(new Text(Long.toString(new Date().getTime())),
                                                          new Text("Alice"))
        ));

        try {
            driver.run();
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Two arguments expected"));
        }
    }

    /**
     * Tests the usage of {@link TaskInputOutputContextWrap#getConfiguration(Context, Scriptable, Object[], Function)}.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testGetConfiguration() throws Exception {
        final String moduleName = "TaskInputOutputContextWrapTest-testGetConfiguration";

        driver.getConfiguration().set(LembosConstants.MR_MODULE_NAME, moduleName);
        driver.getConfiguration().set(LembosConstants.MR_MODULE_PATH, TestUtils.getModulePath(moduleName));
        driver.withAll(ImmutableList.of(
                new Pair<WritableComparable<?>, Writable>(new Text(Long.toString(new Date().getTime())),
                                                          new Text("Alice"))
        ));

        driver.run();

        assertEquals("Hello", driver.getConfiguration().get("new.value"));
    }

    /**
     * Tests the usage of {@link TaskInputOutputContextWrap#getCounter(Context, Scriptable, Object[], Function)}.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testGetCounter() throws Exception {
        final String moduleName = "TaskInputOutputContextWrapTest-testGetCounter";

        driver.getConfiguration().set(LembosConstants.MR_MODULE_NAME, moduleName);
        driver.getConfiguration().set(LembosConstants.MR_MODULE_PATH, TestUtils.getModulePath(moduleName));
        driver.withAll(ImmutableList.of(
                new Pair<WritableComparable<?>, Writable>(new Text(Long.toString(new Date().getTime())),
                                                          new Text("Alice"))
        ));

        driver.run();
    }

}
