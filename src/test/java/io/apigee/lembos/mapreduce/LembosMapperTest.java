package io.apigee.lembos.mapreduce;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import io.apigee.lembos.utils.ConversionUtils;
import io.apigee.lembos.utils.TestUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.types.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link LembosMapper}.
 */
public class LembosMapperTest {

    private final List<Pair<WritableComparable<?>, Writable>> inputs = ImmutableList.of(
            new Pair<WritableComparable<?>, Writable>(new Text(Long.toString(new Date().getTime())),
                                                             new Text("Alice")),
            new Pair<WritableComparable<?>, Writable>(new Text(Long.toString(new Date().getTime())),
                                                             new Text("Bob")),
            new Pair<WritableComparable<?>, Writable>(new Text(Long.toString(new Date().getTime())),
                                                             new Text("Sally")),
            new Pair<WritableComparable<?>, Writable>(new Text(Long.toString(new Date().getTime())),
                                                             new Text("Bob")),
            new Pair<WritableComparable<?>, Writable>(new Text(Long.toString(new Date().getTime())),
                                                             new Text("Alice"))
    );

    private MapDriver<WritableComparable<?>, Writable, WritableComparable<?>, Writable> driver;
    private LembosMapper mapper;

    /**
     * Prepares the tests to run.
     *
     * @throws Exception if anything goes wrong
     */
    @Before
    public void beforeTest() throws Exception {
        mapper = new LembosMapper();
        driver = new MapDriver<>();

        driver.withMapper(mapper);
    }

    /**
     * Tests that the mapper works as expected when ran with a reducer.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testMapper() throws Exception {
        final String moduleName = "LembosMapperTest-testMapper";

        driver.getConfiguration().set(LembosConstants.MR_MODULE_NAME, moduleName);
        driver.getConfiguration().set(LembosConstants.MR_MODULE_PATH, TestUtils.getModulePath(moduleName));
        driver.withAll(inputs);

        final List<Pair<WritableComparable<?>, Writable>> outputs = driver.run();
        final Set<String> seenValues = Sets.newHashSet();
        final Set<String> expectedValues = ImmutableSet.of(
                "Alice",
                "Bob",
                "Sally"
        );

        assertEquals(3, outputs.size());

        for (Pair<WritableComparable<?>, Writable> output : outputs) {
            assertTrue(output.getFirst() instanceof Text);
            assertTrue(output.getSecond() instanceof Text);

            final Object key = ConversionUtils.writableComparableToJS(output.getFirst(), null);
            final Object val = ConversionUtils.writableToJS(output.getSecond(), null);

            assertTrue(key instanceof String);
            assertTrue(val instanceof String);

            assertEquals("uniques", key.toString());
            assertTrue(expectedValues.contains(val.toString()));
            assertTrue(!seenValues.contains(val.toString()));

            seenValues.add(val.toString());
        }
    }

}
