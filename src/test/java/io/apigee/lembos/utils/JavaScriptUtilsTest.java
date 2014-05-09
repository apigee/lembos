package io.apigee.lembos.utils;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.mozilla.javascript.Scriptable;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests that {@link JavaScriptUtils} works as expected.
 */
public final class JavaScriptUtilsTest {

    /**
     * Unit test for {@link JavaScriptUtils#isDefined(Object)}.
     *
     * @throws Exception if anything goes wrong
     */
    @Test
    public void testIsDefined() throws Exception {
        final Scriptable script = TestUtils.createScriptable("JavaScriptUtilsTest.js", StringUtils.join(new String[] {
                "var isNull = null;",
                "var isUndefined = undefined;",
                "var isDefined = 'I am defined!';"
        }, "\n"));

        assertFalse(JavaScriptUtils.isDefined(script.get("isNull", script)));
        assertFalse(JavaScriptUtils.isDefined(script.get("isUndefined", script)));
        assertFalse(JavaScriptUtils.isDefined(script.get("isMissing", script)));
        assertTrue(JavaScriptUtils.isDefined(script.get("isDefined", script)));
    }

}
