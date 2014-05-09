package io.apigee.lembos.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.ByteWritable;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MD5Hash;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.SortedMapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.UTF8;
import org.apache.hadoop.io.VIntWritable;
import org.apache.hadoop.io.VLongWritable;
import org.apache.hadoop.io.Writable;
import org.junit.Test;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

import java.io.File;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests that {@link ConversionUtils} works as expected.
 */
public class ConversionUtilsTest {

    private final Scriptable script = TestUtils.createScriptable("ConversionUtilsTest.js",
                                                                 StringUtils.join(new String[] {
            "var s = 'String';",
            "var i = 1;",
            "var d = 1.1;",
            "var l = 2147483648;",
            "var b = true;",
            "var n = null;",
            "var u = undefined;",
            "var sa = [1,2,3];",
            "var so = {'s': s};",
            "var a = [s, i, d, b, so, sa, n, u];",
            "var ak = ['s', 'i', 'd', 'b', 'so', 'sa', 'n', 'u'];",
            "var o = {'s': s, 'i': i, 'd': d, 'l': l, 'b': b, 'sa': sa, 'so': so, 'n': n, 'u': u}",
    }, "\n"));
    private final Set<String> scriptVarNames = ImmutableSet.of(
            "s", "i", "d", "l", "d", "n", "u", "a", "o"
    );

    /**
     * Unit tests for {@link ConversionUtils#writableToJS(Writable, Scriptable)} and
     * {@link ConversionUtils#jsToWritable(Object, Scriptable)}.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testWritableToJSAndJSToWritable() throws Exception {
        // Test values that come from JavaScript
        for (final String name : scriptVarNames) {
            final Writable jsWritable = ConversionUtils.jsToWritable(script.get(name, script), script);

            validateWritable(name, jsWritable);
            validateJS(name, ConversionUtils.writableToJS(jsWritable, script));
        }

        // Test Hadoop Writables to JS
        final Writable aWritable = ConversionUtils.jsToWritable(new int[] {1, 2, 3}, script);
        final Writable bWritable = ConversionUtils.jsToWritable((byte)0, script);
        final Writable bsWritable = ConversionUtils.jsToWritable("String".getBytes(), script);
        final Writable fWritable = ConversionUtils.jsToWritable(1.1F, script);
        final Writable lWritable = ConversionUtils.jsToWritable(ImmutableList.of("a", "b", "c"), script);
        final Writable mWritable = ConversionUtils.jsToWritable(ImmutableMap.of(
                "a", 1,
                "b", 2,
                "c", 3
        ), script);
        final Writable sWritable = ConversionUtils.jsToWritable(ImmutableSet.of("a", "b", "c"), script);
        final SortedMapWritable smWritable = new SortedMapWritable();
        final VIntWritable viWritable = new VIntWritable(1);
        final VLongWritable vlWritable = new VLongWritable(1L);

        smWritable.put(new Text("key"), new IntWritable(1));

        assertTrue(aWritable instanceof ArrayWritable); // Ensure arrays are handled
        assertTrue(bWritable instanceof ByteWritable);
        System.out.println(bsWritable.getClass());
        assertTrue(bsWritable instanceof BytesWritable);
        assertTrue(fWritable instanceof FloatWritable);
        assertTrue(lWritable instanceof ArrayWritable);
        assertTrue(mWritable instanceof MapWritable);
        assertTrue(sWritable instanceof ArrayWritable);

        final Object aJS = ConversionUtils.writableToJS(aWritable, script);
        final Object bJS = ConversionUtils.writableToJS(bWritable, script);
        final Object bsJS = ConversionUtils.writableToJS(bsWritable, script);
        final Object fJS = ConversionUtils.writableToJS(fWritable, script);
        final Object lJS = ConversionUtils.writableToJS(lWritable, script);
        final Object sJS = ConversionUtils.writableToJS(sWritable, script);
        final Object smJS = ConversionUtils.writableToJS(smWritable, script);
        final Object viJS = ConversionUtils.writableToJS(viWritable, script);
        final Object vlJS = ConversionUtils.writableToJS(vlWritable, script);

        assertTrue(aJS instanceof NativeArray);
        assertTrue(bJS instanceof Byte);
        assertTrue(bsJS instanceof byte[]);
        assertTrue(fJS instanceof Float);
        assertTrue(lJS instanceof NativeArray);
        assertTrue(sJS instanceof NativeArray);
        assertTrue(smJS instanceof NativeObject);
        assertTrue(viJS instanceof Integer);
        assertTrue(vlJS instanceof Long);

        // Try unconvertible Writable to JavaScript
        try {
            ConversionUtils.writableToJS(new MD5Hash(), script);
        } catch (Exception e) {
            assertEquals("No Writable to JavaScript converter found for class: "
                                 + MD5Hash.class.getCanonicalName(), e.getMessage());
        }

        // Try unconvertible WritableComparable to JavaScript
        try {
            ConversionUtils.writableComparableToJS(new UTF8(), script);
        } catch (Exception e) {
            assertEquals("No WritableComparable to JavaScript converter found for class: "
                                 + UTF8.class.getCanonicalName(), e.getMessage());
        }

        // Try unconvertible JavaScript to Writable
        try {
            ConversionUtils.jsToWritable(new File("."), script);
        } catch (Exception e) {
            assertEquals("No JavaScript to Writable converter found for class: "
                                 + File.class.getCanonicalName(), e.getMessage());
        }

        // Try unconvertible JavaScript to WritableComparable
        try {
            ConversionUtils.jsToWritableComparable(new File("."), script);
        } catch (Exception e) {
            assertEquals("No JavaScript to WritableComparable converter found for class: "
                                 + File.class.getCanonicalName(), e.getMessage());
        }
    }

    /**
     * Validates the {@link Writable} value based on {@link #script} values.
     *
     * @param key the key in the script
     * @param writable the value to test
     */
    private void validateWritable(final String key, final Writable writable) {
        switch (key) {
            case "s":
                assertTrue(writable instanceof Text);
                assertEquals("String", writable.toString());

                break;

            case "i":
                assertTrue(writable instanceof IntWritable);
                assertEquals(1, ((IntWritable)writable).get());

                break;

            case "d":
                assertTrue(writable instanceof DoubleWritable);
                assertEquals(1.1, ((DoubleWritable)writable).get(), 0);

                break;

            case "l":
                assertTrue(writable instanceof LongWritable);
                assertEquals(2147483648L, ((LongWritable)writable).get());

                break;

            case "b":
                assertTrue(writable instanceof BooleanWritable);
                assertTrue(((BooleanWritable)writable).get());

                break;

            case "sa":
                assertTrue(writable instanceof ArrayWritable);

                final Writable[] saWritables = ((ArrayWritable)writable).get();

                for (int i = 0; i < 3; i++) {
                    assertEquals(i + 1, ((IntWritable)saWritables[i]).get());
                }

                break;

            case "a":
                assertTrue(writable instanceof ArrayWritable);

                final Writable[] aWritables = ((ArrayWritable)writable).get();
                final Object[] arrayKeys = ((NativeArray)script.get("ak", script)).toArray();

                for (int i = 0; i < aWritables.length; i++) {
                    validateWritable(arrayKeys[i].toString(), aWritables[i]);
                }

                break;

            case "o":
            case "so":
                assertTrue(writable instanceof MapWritable);

                final MapWritable mWritable = (MapWritable)writable;

                for (final Map.Entry<Writable, Writable> mwEntry : mWritable.entrySet()) {
                    validateWritable(mwEntry.getKey().toString(), mwEntry.getValue());
                }

                break;

            case "n":
            case "u":
                assertTrue(writable instanceof NullWritable);

                break;

            default:
                fail("Unexpected key: " + key);
        }
    }

    /**
     * Validates the JavaScript value based on {@link #script} values.
     *
     * <b>Note:</b> For numbers, we have to double convert because JavaScript always returns a {@link Double} for any
     * numerical value and we handle this in {@link ConversionUtils#jsToWritable(Object, Scriptable)}.
     *
     * @param key the key in the script
     * @param value the value to test
     */
    private void validateJS(final String key, final Object value) {
        switch (key) {
            case "s":
                assertTrue(value instanceof String);
                assertEquals("String", value);

                break;

            case "i":
                final Writable iWritable = ConversionUtils.jsToWritable(value, script);

                assertTrue(iWritable instanceof IntWritable);
                assertEquals(1, ((IntWritable)iWritable).get());

                break;

            case "d":
                assertTrue(value instanceof Double);
                assertEquals(1.1, value);

                break;

            case "l":
                final Writable lWritable = ConversionUtils.jsToWritable(value, script);

                assertTrue(lWritable instanceof LongWritable);
                assertEquals(2147483648L, ((LongWritable)lWritable).get());

                break;

            case "b":
                assertTrue(value instanceof Boolean);
                assertTrue(Boolean.valueOf(value.toString()));

                break;

            case "sa":
                assertTrue(value instanceof NativeArray);

                final NativeArray simpleArray = (NativeArray)value;

                for (int i = 0; i < 3; i++) {
                    assertEquals(i + 1, ((IntWritable)ConversionUtils.jsToWritable(simpleArray.get(i), script)).get());
                }

                break;

            case "a":
                assertTrue(value instanceof NativeArray);

                final Object[] array = ((NativeArray)value).toArray();
                final Object[] arrayKeys = ((NativeArray)script.get("ak", script)).toArray();

                for (int i = 0; i < array.length; i++) {
                    validateJS(arrayKeys[i].toString(), array[i]);
                }

                break;

            case "o":
            case "so":
                assertTrue(value instanceof NativeObject);

                final NativeObject object = (NativeObject)value;

                for (final Object keyName : object.getAllIds()) {
                    validateJS(keyName.toString(), object.get(keyName));
                }

                break;

            case "n":
            case "u":
                assertNull(value);

                break;

            default:
                fail("Unexpected key: " + key);
        }
    }

}
