package io.apigee.lembos.node.modules;

import io.apigee.lembos.mapreduce.LembosNodeEnvironment;
import io.apigee.lembos.node.types.DBOutputFormatWrap;
import io.apigee.lembos.node.types.FileOutputFormatWrap;
import io.apigee.lembos.node.types.SequenceFileAsBinaryOutputFormatWrap;
import io.apigee.lembos.node.types.SequenceFileOutputFormatWrap;
import io.apigee.lembos.node.types.TextOutputFormatWrap;
import io.apigee.lembos.utils.TestUtils;
import org.junit.Test;
import org.mozilla.javascript.Function;

import java.io.File;

/**
 * Unit tests for {@link HadoopOutput}.
 */
public class HadoopOutputTest {

    /**
     * Tests the usage of {@link DBOutputFormatWrap}.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testDBOutputFormat() throws Exception {
        final String moduleName = "HadoopOutputTest-testDBOutputFormat";
        final LembosNodeEnvironment env = new LembosNodeEnvironment(moduleName,
                                                                      new File(TestUtils.getModulePath(moduleName)),
                                                                      null);

        env.initialize();

        // Call JavaScript-based Tests
        env.callFunctionSync((Function)env.getModule().get("testDBOutputFormat", env.getModule()), new Object[0]);
    }

    /**
     * Tests the usage of {@link FileOutputFormatWrap}.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testFileOutputFormat() throws Exception {
        final String moduleName = "HadoopOutputTest-testFileOutputFormat";
        final LembosNodeEnvironment env = new LembosNodeEnvironment(moduleName,
                                                                      new File(TestUtils.getModulePath(moduleName)),
                                                                      null);

        env.initialize();

        // Call JavaScript-based Tests
        env.callFunctionSync((Function)env.getModule().get("testFileOutputFormat", env.getModule()), new Object[0]);
    }

    /**
     * Tests the usage of {@link SequenceFileOutputFormatWrap}.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testSequenceFileOutputFormat() throws Exception {
        final String moduleName = "HadoopOutputTest-testSequenceFileOutputFormat";
        final LembosNodeEnvironment env = new LembosNodeEnvironment(moduleName,
                                                                      new File(TestUtils.getModulePath(moduleName)),
                                                                      null);

        env.initialize();

        // Call JavaScript-based Tests
        env.callFunctionSync((Function)env.getModule().get("testSequenceFileOutputFormat", env.getModule()),
                             new Object[0]);
    }

    /**
     * Tests the usage of {@link SequenceFileAsBinaryOutputFormatWrap}.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testSequenceFileAsBinaryOutputFormat() throws Exception {
        final String moduleName = "HadoopOutputTest-testSequenceFileAsBinaryOutputFormat";
        final LembosNodeEnvironment env = new LembosNodeEnvironment(moduleName,
                                                                      new File(TestUtils.getModulePath(moduleName)),
                                                                      null);

        env.initialize();

        // Call JavaScript-based Tests
        env.callFunctionSync((Function)env.getModule().get("testSequenceFileAsBinaryOutputFormat", env.getModule()),
                             new Object[0]);
    }

    /**
     * Tests the usage of {@link TextOutputFormatWrap}.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testTextOutputFormat() throws Exception {
        final String moduleName = "HadoopOutputTest-testTextOutputFormat";
        final LembosNodeEnvironment env = new LembosNodeEnvironment(moduleName,
                                                                      new File(TestUtils.getModulePath(moduleName)),
                                                                      null);

        env.initialize();

        // Call JavaScript-based Tests
        env.callFunctionSync((Function)env.getModule().get("testTextOutputFormat", env.getModule()),
                             new Object[0]);
    }

}
