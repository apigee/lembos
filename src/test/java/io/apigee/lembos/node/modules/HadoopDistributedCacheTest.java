package io.apigee.lembos.node.modules;

import io.apigee.lembos.mapreduce.LembosNodeEnvironment;
import io.apigee.lembos.utils.TestUtils;
import org.junit.Test;
import org.mozilla.javascript.Function;

import java.io.File;

/**
 * Unit tests for {@link HadoopDistributedCache}.
 */
public class HadoopDistributedCacheTest {

    /**
     * Tests the usage of {@link HadoopDistributedCache}.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testHadoopDistributedCache() throws Exception {
        final String moduleName = "HadoopDistributedCacheTest-testHadoopDistributedCache";
        final LembosNodeEnvironment env = new LembosNodeEnvironment(moduleName,
                                                                      new File(TestUtils.getModulePath(moduleName)),
                                                                      null);

        env.initialize();

        // Call JavaScript-based Tests
        env.callFunctionSync((Function)env.getModule().get("testHadoopDistributedCache", env.getModule()),
                             new Object[] {});
    }

}
