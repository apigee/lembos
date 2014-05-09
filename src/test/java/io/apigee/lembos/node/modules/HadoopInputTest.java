package io.apigee.lembos.node.modules;

import io.apigee.lembos.mapreduce.LembosNodeEnvironment;
import io.apigee.lembos.node.types.CombineFileInputFormatWrap;
import io.apigee.lembos.node.types.DBInputFormatWrap;
import io.apigee.lembos.node.types.DataDrivenDBInputFormatWrap;
import io.apigee.lembos.node.types.FileInputFormatWrap;
import io.apigee.lembos.node.types.KeyValueTextInputFormatWrap;
import io.apigee.lembos.node.types.NLineInputFormatWrap;
import io.apigee.lembos.node.types.SequenceFileAsBinaryInputFormatWrap;
import io.apigee.lembos.node.types.SequenceFileAsTextInputFormatWrap;
import io.apigee.lembos.node.types.SequenceFileInputFilterWrap;
import io.apigee.lembos.node.types.SequenceFileInputFormatWrap;
import io.apigee.lembos.node.types.TextInputFormatWrap;
import io.apigee.lembos.utils.TestUtils;
import org.junit.Test;
import org.mozilla.javascript.Function;

import java.io.File;

/**
 * Unit tests for {@link HadoopInput}.
 */
public class HadoopInputTest {

    /**
     * Tests the usage of {@link FileInputFormatWrap}.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testFileInputFormat() throws Exception {
        final String moduleName = "HadoopInputTest-testFileInputFormat";
        final LembosNodeEnvironment env = new LembosNodeEnvironment(moduleName,
                                                                      new File(TestUtils.getModulePath(moduleName)),
                                                                      null);

        env.initialize();

        // Call JavaScript-based Tests
        env.callFunctionSync((Function)env.getModule().get("testFileInputFormat", env.getModule()), new Object[0]);
    }

    /**
     * Tests the usage of {@link DBInputFormatWrap}.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testDBInputFormat() throws Exception {
        final String moduleName = "HadoopInputTest-testDBInputFormat";
        final LembosNodeEnvironment env = new LembosNodeEnvironment(moduleName,
                                                                      new File(TestUtils.getModulePath(moduleName)),
                                                                      null);

        env.initialize();

        // Call JavaScript-based Tests
        env.callFunctionSync((Function)env.getModule().get("testDBInputFormat", env.getModule()), new Object[0]);
    }

    /**
     * Tests the usage of {@link DataDrivenDBInputFormatWrap}.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testDataDrivenDBInputFormat() throws Exception {
        final String moduleName = "HadoopInputTest-testDataDrivenDBInputFormat";
        final LembosNodeEnvironment env = new LembosNodeEnvironment(moduleName,
                                                                      new File(TestUtils.getModulePath(moduleName)),
                                                                      null);

        env.initialize();

        // Call JavaScript-based Tests
        env.callFunctionSync((Function)env.getModule().get("testDataDrivenDBInputFormat", env.getModule()),
                             new Object[0]);
    }

    /**
     * Tests the usage of {@link CombineFileInputFormatWrap}.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testCombineFileInputFormat() throws Exception {
        final String moduleName = "HadoopInputTest-testCombineFileInputFormat";
        final LembosNodeEnvironment env = new LembosNodeEnvironment(moduleName,
                                                                      new File(TestUtils.getModulePath(moduleName)),
                                                                      null);

        env.initialize();

        // Call JavaScript-based Tests
        env.callFunctionSync((Function)env.getModule().get("testCombineFileInputFormat", env.getModule()),
                             new Object[0]);
    }

    /**
     * Tests the usage of {@link KeyValueTextInputFormatWrap}.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testKeyValueTextInputFormat() throws Exception {
        final String moduleName = "HadoopInputTest-testKeyValueTextInputFormat";
        final LembosNodeEnvironment env = new LembosNodeEnvironment(moduleName,
                                                                      new File(TestUtils.getModulePath(moduleName)),
                                                                      null);

        env.initialize();

        // Call JavaScript-based Tests
        env.callFunctionSync((Function)env.getModule().get("testKeyValueTextInputFormat", env.getModule()),
                             new Object[0]);
    }

    /**
     * Tests the usage of {@link NLineInputFormatWrap}.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testNLineInputFormat() throws Exception {
        final String moduleName = "HadoopInputTest-testNLineInputFormat";
        final LembosNodeEnvironment env = new LembosNodeEnvironment(moduleName,
                                                                      new File(TestUtils.getModulePath(moduleName)),
                                                                      null);

        env.initialize();

        // Call JavaScript-based Tests
        env.callFunctionSync((Function)env.getModule().get("testNLineInputFormat", env.getModule()),
                             new Object[0]);
    }

    /**
     * Tests the usage of {@link SequenceFileInputFormatWrap}.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testSequenceFileInputFormat() throws Exception {
        final String moduleName = "HadoopInputTest-testSequenceFileInputFormat";
        final LembosNodeEnvironment env = new LembosNodeEnvironment(moduleName,
                                                                      new File(TestUtils.getModulePath(moduleName)),
                                                                      null);

        env.initialize();

        // Call JavaScript-based Tests
        env.callFunctionSync((Function)env.getModule().get("testSequenceFileInputFormat", env.getModule()),
                             new Object[0]);
    }

    /**
     * Tests the usage of {@link SequenceFileAsBinaryInputFormatWrap}.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testSequenceFileAsBinaryInputFormat() throws Exception {
        final String moduleName = "HadoopInputTest-testSequenceFileAsBinaryInputFormat";
        final LembosNodeEnvironment env = new LembosNodeEnvironment(moduleName,
                                                                      new File(TestUtils.getModulePath(moduleName)),
                                                                      null);

        env.initialize();

        // Call JavaScript-based Tests
        env.callFunctionSync((Function)env.getModule().get("testSequenceFileAsBinaryInputFormat", env.getModule()),
                             new Object[0]);
    }

    /**
     * Tests the usage of {@link SequenceFileAsTextInputFormatWrap}.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testSequenceFileAsTextInputFormat() throws Exception {
        final String moduleName = "HadoopInputTest-testSequenceFileAsTextInputFormat";
        final LembosNodeEnvironment env = new LembosNodeEnvironment(moduleName,
                                                                      new File(TestUtils.getModulePath(moduleName)),
                                                                      null);

        env.initialize();

        // Call JavaScript-based Tests
        env.callFunctionSync((Function)env.getModule().get("testSequenceFileAsTextInputFormat", env.getModule()),
                             new Object[0]);
    }

    /**
     * Tests the usage of {@link SequenceFileInputFilterWrap}.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testSequenceFileInputFilter() throws Exception {
        final String moduleName = "HadoopInputTest-testSequenceFileInputFilter";
        final LembosNodeEnvironment env = new LembosNodeEnvironment(moduleName,
                                                                      new File(TestUtils.getModulePath(moduleName)),
                                                                      null);

        env.initialize();

        // Call JavaScript-based Tests
        env.callFunctionSync((Function)env.getModule().get("testSequenceFileInputFilter", env.getModule()),
                             new Object[0]);
    }

    /**
     * Tests the usage of {@link TextInputFormatWrap}.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testTextInputFormat() throws Exception {
        final String moduleName = "HadoopInputTest-testTextInputFormat";
        final LembosNodeEnvironment env = new LembosNodeEnvironment(moduleName,
                                                                      new File(TestUtils.getModulePath(moduleName)),
                                                                      null);

        env.initialize();

        // Call JavaScript-based Tests
        env.callFunctionSync((Function)env.getModule().get("testTextInputFormat", env.getModule()),
                             new Object[0]);
    }

}
