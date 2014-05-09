package io.apigee.lembos.node.modules;

import io.apigee.lembos.mapreduce.LembosNodeEnvironment;
import io.apigee.lembos.node.types.ConfigurationWrap;
import io.apigee.lembos.utils.TestUtils;
import org.apache.hadoop.conf.Configuration;
import org.junit.Test;
import org.mozilla.javascript.Function;

import java.io.File;

/**
 * Unit tests for {@link HadoopConfiguration}.
 */
public class HadoopConfigurationTest {

    /**
     * Tests the usage of {@link HadoopConfiguration}.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testHadoopConfiguration() throws Exception {
        final String moduleName = "HadoopConfigurationTest-testHadoopConfiguration";
        final LembosNodeEnvironment env = new LembosNodeEnvironment(moduleName,
                                                                      new File(TestUtils.getModulePath(moduleName)),
                                                                      null);

        env.initialize();

        // Call JavaScript-based Tests
        env.callFunctionSync((Function)env.getModule().get("testHadoopConfiguration", env.getModule()),
                             new Object[] {
                                     ConfigurationWrap.getInstance(env.getRuntime(), new Configuration())
                             });
    }

}
