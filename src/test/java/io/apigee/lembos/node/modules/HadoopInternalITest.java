package io.apigee.lembos.node.modules;

import io.apigee.lembos.mapreduce.LembosConstants;
import io.apigee.lembos.mapreduce.LembosMapReduceRunner;
import io.apigee.lembos.utils.TestUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Integration tests for {@link HadoopInternal}.
 */
public class HadoopInternalITest {

    /**
     * Tests that the mapper works as expected when ran with a reducer.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testCounters() throws Exception {
        final String moduleName = "HadoopInternalITest-testCounters";
        final Configuration conf = TestUtils.getConfiguration();
        int results = 1;

        try {
            results = ToolRunner.run(conf, new LembosMapReduceRunner(), new String[] {
                    "-fs",
                    conf.get("fs.default.name"),
                    "-jt",
                    conf.get("mapred.job.tracker"),
                    "-D",
                    LembosConstants.MR_MODULE_NAME + "=" + moduleName,
                    "-D",
                    LembosConstants.MR_MODULE_PATH + "=" + TestUtils.getModulePath(moduleName),
                    "-D",
                    "mapred.jar=" + System.getProperty("mapred.jar"),
                    "-D",
                    "HADOOP_USER_NAME=" + conf.get("hadoop.user")
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(0, results);
    }

}
