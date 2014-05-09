package io.apigee.lembos.mapreduce;

import io.apigee.lembos.utils.ConversionUtils;
import io.apigee.lembos.utils.TestUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link LembosPartitioner}.
 */
public class LembosPartitionerTest {

    /**
     * Tests the proper error is thrown when the partitioner configuration is missing.  (Should never happen)
     *
     * @throws Exception if anything goes wrong.
     */
    @Test
    public void testMissingConfiguration() throws Exception {
        try {
            new LembosPartitioner().getPartition(null, null, 1);

            fail("The line above should had failed");
        } catch (Exception e) {
            assertEquals("Hadoop configuration cannot be null", e.getMessage());
        }
    }

    /**
     * Tests the proper error is thrown when the specified module doesn't expose a 'partition' property.
     *
     * @throws Exception if anything goes wrong.
     */
    @Test
    public void testMissingPartitionFunction() throws Exception {
        try {
            runPartitioner("LembosPartitionerTest-testMissingPartitionFunction", "count", 1, 3);

            fail("The line above should had failed");
        } catch (Exception e) {
            assertEquals("MapReduce function 'partition' is not defined", e.getMessage());
        }
    }

    /**
     * Tests the proper error is thrown when the partitioner returns null;
     *
     * @throws Exception if anything goes wrong.
     */
    @Test
    public void testReturningNull() throws Exception {
        try {
            runPartitioner("LembosPartitionerTest-testReturningNull", "count", 1, 3);

            fail("The line above should had failed");
        } catch (Exception e) {
            assertEquals("MapReduce function 'partition' cannot return null/undefined", e.getMessage());
        }
    }

    /**
     * Tests the proper error is thrown when the partitioner returns undefined;
     *
     * @throws Exception if anything goes wrong.
     */
    @Test
    public void testReturningUndefined() throws Exception {
        try {
            runPartitioner("LembosPartitionerTest-testReturningUndefined", "count", 1, 3);

            fail("The line above should had failed");
        } catch (Exception e) {
            assertEquals("MapReduce function 'partition' cannot return null/undefined", e.getMessage());
        }
    }

    /**
     * Tests the proper error is thrown when the partitioner returns the wrong type;
     *
     * @throws Exception if anything goes wrong.
     */
    @Test
    public void testReturningWrongType() throws Exception {
        try {
            runPartitioner("LembosPartitionerTest-testReturningWrongType", "count", 1, 3);

            fail("The line above should had failed");
        } catch (Exception e) {
            assertEquals("MapReduce function 'partition' must return an integer", e.getMessage());
        }
    }

    /**
     * Tests that the partitioner works as expected.
     *
     * @throws Exception if anything goes wrong.
     */
    @Test
    public void testPartitioner() throws Exception {
        assertEquals(1, runPartitioner("LembosPartitionerTest-testPartitioner", "count", 1, 3));
    }

    /**
     * Runs the partitioner.
     *
     * @param moduleName the module name
     * @param key the key
     * @param value the value
     * @param numPartitions the number of partitions
     *
     * @return the partition
     *
     * @throws Exception if anything goes wrong
     */
    private int runPartitioner(final String moduleName, final Object key, final Object value, final int numPartitions)
            throws Exception {
        final Configuration conf = new Configuration();
        final LembosPartitioner partitioner = new LembosPartitioner();

        conf.set(LembosConstants.MR_MODULE_NAME, moduleName);
        conf.set(LembosConstants.MR_MODULE_PATH, TestUtils.getModulePath(moduleName));

        partitioner.setConf(conf);

        final WritableComparable<?> writableKey = ConversionUtils.jsToWritableComparable(key, null);
        final Writable writableValue = ConversionUtils.jsToWritableComparable(value, null);

        try {
            return partitioner.getPartition(writableKey, writableValue, numPartitions);
        } finally {
            partitioner.close();
        }
    }

}
