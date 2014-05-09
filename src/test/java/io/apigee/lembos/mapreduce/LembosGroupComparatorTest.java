package io.apigee.lembos.mapreduce;

import io.apigee.lembos.utils.ConversionUtils;
import io.apigee.lembos.utils.TestUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.WritableComparable;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link LembosGroupComparator}.
 */
public class LembosGroupComparatorTest {

    /**
     * Tests the proper error is thrown when the group comparator configuration is missing.  (Should never happen)
     *
     * @throws Exception if anything goes wrong.
     */
    @Test
    public void testMissingConfiguration() throws Exception {
        try {
            new LembosGroupComparator().compare(null, null);

            fail("The line above should had failed");
        } catch (Exception e) {
            assertEquals("Hadoop configuration cannot be null", e.getMessage());
        }
    }

    /**
     * Tests the proper error is thrown when the specified module doesn't expose a 'group' property.
     *
     * @throws Exception if anything goes wrong.
     */
    @Test
    public void testMissingGroupFunction() throws Exception {
        try {
            runGroupComparator("LembosGroupComparatorTest-testMissingGroupFunction", 1, 3);

            fail("The line above should had failed");
        } catch (Exception e) {
            assertEquals("MapReduce function 'group' is not defined", e.getMessage());
        }
    }

    /**
     * Tests the proper error is thrown when the group comparator returns null;
     *
     * @throws Exception if anything goes wrong.
     */
    @Test
    public void testReturningNull() throws Exception {
        try {
            runGroupComparator("LembosGroupComparatorTest-testReturningNull", 1, 3);

            fail("The line above should had failed");
        } catch (Exception e) {
            assertEquals("MapReduce function 'group' cannot return null/undefined", e.getMessage());
        }
    }

    /**
     * Tests the proper error is thrown when the group comparator returns undefined;
     *
     * @throws Exception if anything goes wrong.
     */
    @Test
    public void testReturningUndefined() throws Exception {
        try {
            runGroupComparator("LembosGroupComparatorTest-testReturningUndefined", 1, 3);

            fail("The line above should had failed");
        } catch (Exception e) {
            assertEquals("MapReduce function 'group' cannot return null/undefined", e.getMessage());
        }
    }

    /**
     * Tests the proper error is thrown when the group comparator returns the wrong type;
     *
     * @throws Exception if anything goes wrong.
     */
    @Test
    public void testReturningWrongType() throws Exception {
        try {
            runGroupComparator("LembosGroupComparatorTest-testReturningWrongType", 1, 3);

            fail("The line above should had failed");
        } catch (Exception e) {
            assertEquals("MapReduce function 'group' must return an integer", e.getMessage());
        }
    }

    /**
     * Tests that the group comparator works as expected.
     *
     * @throws Exception if anything goes wrong.
     */
    @Test
    public void testGroupComparator() throws Exception {
        assertEquals(-1, runGroupComparator("LembosGroupComparatorTest-testGroupComparator", 1, 3));
    }

    /**
     * Runs the group comparator.
     *
     * @param moduleName the module name
     * @param key1 the key
     * @param key2 the value
     *
     * @return the partition
     *
     * @throws Exception if anything goes wrong
     */
    private int runGroupComparator(final String moduleName, final Object key1, final Object key2)
            throws Exception {
        final Configuration conf = new Configuration();
        final LembosGroupComparator groupComparator = new LembosGroupComparator();

        conf.set(LembosConstants.MR_MODULE_NAME, moduleName);
        conf.set(LembosConstants.MR_MODULE_PATH, TestUtils.getModulePath(moduleName));

        groupComparator.setConf(conf);

        final WritableComparable<?> writable1 = ConversionUtils.jsToWritableComparable(key1, null);
        final WritableComparable<?> writable2 = ConversionUtils.jsToWritableComparable(key2, null);

        try {
            return groupComparator.compare(writable1, writable2);
        } finally {
            groupComparator.close();
        }
    }

}
