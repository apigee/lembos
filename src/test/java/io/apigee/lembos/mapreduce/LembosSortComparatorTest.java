package io.apigee.lembos.mapreduce;

import io.apigee.lembos.utils.ConversionUtils;
import io.apigee.lembos.utils.TestUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.WritableComparable;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link LembosSortComparator}.
 */
public class LembosSortComparatorTest {

    /**
     * Tests the proper error is thrown when the sort comparator configuration is missing.  (Should never happen)
     *
     * @throws Exception if anything goes wrong.
     */
    @Test
    public void testMissingConfiguration() throws Exception {
        try {
            new LembosSortComparator().compare(null, null);

            fail("The line above should had failed");
        } catch (Exception e) {
            assertEquals("Hadoop configuration cannot be null", e.getMessage());
        }
    }

    /**
     * Tests the proper error is thrown when the specified module doesn't expose a 'sort' property.
     *
     * @throws Exception if anything goes wrong.
     */
    @Test
    public void testMissingSortFunction() throws Exception {
        try {
            runSortComparator("LembosSortComparatorTest-testMissingSortFunction", 1, 3);

            fail("The line above should had failed");
        } catch (Exception e) {
            assertEquals("MapReduce function 'sort' is not defined", e.getMessage());
        }
    }

    /**
     * Tests the proper error is thrown when the sort comparator returns null;
     *
     * @throws Exception if anything goes wrong.
     */
    @Test
    public void testReturningNull() throws Exception {
        try {
            runSortComparator("LembosSortComparatorTest-testReturningNull", 1, 3);

            fail("The line above should had failed");
        } catch (Exception e) {
            assertEquals("MapReduce function 'sort' cannot return null/undefined", e.getMessage());
        }
    }

    /**
     * Tests the proper error is thrown when the sort comparator returns undefined;
     *
     * @throws Exception if anything goes wrong.
     */
    @Test
    public void testReturningUndefined() throws Exception {
        try {
            runSortComparator("LembosSortComparatorTest-testReturningUndefined", 1, 3);

            fail("The line above should had failed");
        } catch (Exception e) {
            assertEquals("MapReduce function 'sort' cannot return null/undefined", e.getMessage());
        }
    }

    /**
     * Tests the proper error is thrown when the sort comparator returns the wrong type;
     *
     * @throws Exception if anything goes wrong.
     */
    @Test
    public void testReturningWrongType() throws Exception {
        try {
            runSortComparator("LembosSortComparatorTest-testReturningWrongType", 1, 3);

            fail("The line above should had failed");
        } catch (Exception e) {
            assertEquals("MapReduce function 'sort' must return an integer", e.getMessage());
        }
    }

    /**
     * Tests that the sort comparator works as expected.
     *
     * @throws Exception if anything goes wrong.
     */
    @Test
    public void testSortComparator() throws Exception {
        assertEquals(-1, runSortComparator("LembosSortComparatorTest-testSortComparator", 1, 3));
    }

    /**
     * Runs the sort comparator.
     *
     * @param moduleName the module name
     * @param key1 the key
     * @param key2, final boolean isJS the value
     *
     * @return the partition
     *
     * @throws Exception if anything goes wrong
     */
    private int runSortComparator(final String moduleName, final Object key1, final Object key2)
            throws Exception {
        final Configuration conf = new Configuration();
        final LembosSortComparator sortComparator = new LembosSortComparator();

        conf.set(LembosConstants.MR_MODULE_NAME, moduleName);
        conf.set(LembosConstants.MR_MODULE_PATH, TestUtils.getModulePath(moduleName));

        sortComparator.setConf(conf);

        final WritableComparable<?> writable1 = ConversionUtils.jsToWritableComparable(key1, null);
        final WritableComparable<?> writable2 = ConversionUtils.jsToWritableComparable(key2, null);

        try {
            return sortComparator.compare(writable1, writable2);
        } finally {
            sortComparator.close();
        }
    }

}
