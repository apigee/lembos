/*
 * Copyright 2014 Apigee Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.apigee.lembos.utils;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Undefined;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * Various utilities for interacting with JavaScript.
 */
public final class JavaScriptUtils {

    /**
     * Private constructor.
     */
    private JavaScriptUtils() { }

    /**
     * Returns whether or not the object is defined.
     *
     * @param object the object to check
     *
     * @return false if the object is null or {@link Scriptable#NOT_FOUND}, true otherwise
     */
    public static boolean isDefined(final Object object) {
        return object != null && object != Scriptable.NOT_FOUND && object != Undefined.instance;
    }

    /**
     * Converts a JavaScript number to the proper Java type.
     *
     * @param number the number
     *
     * @return the Java type (Double, Integer or Long)
     */
    public static Number fromNumber(final Object number) {
        if (number == null) {
            throw new IllegalArgumentException("number cannot be null");
        } else if (!(number instanceof Number)) {
            throw new IllegalArgumentException("number must be a number object");
        }

        Number numberValue = (Number)number;

        if (number instanceof Double && numberValue.doubleValue() % 1 == 0) {
            final Long longValue = Math.round((Double)number);

            if (longValue >= Integer.MIN_VALUE && longValue <= Integer.MAX_VALUE) {
                numberValue = longValue.intValue();
            } else {
                numberValue = longValue;
            }
        }

        return numberValue;
    }

    /**
     * Converts a collection into a JavaScript array.
     *
     * @param scope the JavaScript scope
     * @param collection the collection (array or anything extending collection)
     *
     * @return the JavaScript array
     */
    public static Scriptable asArray(final Scriptable scope, final Object collection) {
        Scriptable array = null;

        if (collection != null) {
            final Object[] rawArray;
            final Object[] rawEntries;

            if (collection instanceof Collection) {
                final Collection defaultsCol = (Collection)collection;

                rawArray = new Object[defaultsCol.size()];
                rawEntries = defaultsCol.toArray(new Object[defaultsCol.size()]);
            } else if (collection.getClass().isArray()) {
                rawArray = new Object[Array.getLength(collection)];
                rawEntries = (Object[])collection;
            } else {
                throw new IllegalArgumentException("Expected an array or a collection");
            }

            System.arraycopy(rawEntries, 0, rawArray, 0, rawArray.length);

            array = Context.getCurrentContext().newArray(scope, rawArray);
        }

        return array;
    }

    /**
     * Converts a map into a JavaScript object.
     *
     * @param scope the JavaScript scope
     * @param map the map to convert
     *
     * @return the JavaScript object
     */
    public static Scriptable asObject(final Scriptable scope, final Map<?, ?> map) {
        final Scriptable newObject = Context.getCurrentContext().newObject(scope);

        for (final Map.Entry<?, ?> mapEntry : map.entrySet()) {
            newObject.put(mapEntry.getKey().toString(), newObject, mapEntry.getValue());
        }

        return newObject;
    }

}
