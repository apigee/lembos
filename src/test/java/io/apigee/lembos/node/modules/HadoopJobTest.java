package io.apigee.lembos.node.modules;

import io.apigee.lembos.mapreduce.LembosNodeEnvironment;
import io.apigee.lembos.node.types.JobWrap;
import io.apigee.lembos.utils.TestUtils;
import org.apache.hadoop.mapreduce.Job;
import org.junit.Test;
import org.mozilla.javascript.Function;

import java.io.File;

/**
 * Unit tests for {@link HadoopJob}.
 */
public class HadoopJobTest {

    /**
     * Tests the usage of {@link HadoopJob}.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testHadoopJob() throws Exception {
        final String moduleName = "HadoopJobTest-testHadoopJob";
        final LembosNodeEnvironment env = new LembosNodeEnvironment(moduleName,
                                                                      new File(TestUtils.getModulePath(moduleName)),
                                                                      null);

        env.initialize();

        // Call JavaScript-based Tests
        env.callFunctionSync((Function)env.getModule().get("testHadoopJob", env.getModule()),
                             new Object[] {
                                     JobWrap.getInstance(env.getRuntime(), new Job())
                             });
    }

}
