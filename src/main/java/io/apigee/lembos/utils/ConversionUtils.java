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

import io.apigee.lembos.mapreduce.converters.JSToWritableConverter;
import io.apigee.lembos.mapreduce.converters.WritableToJSConverter;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.mozilla.javascript.Scriptable;

import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * Various utilities for converting to/from {@link Writable} and {@link WritableComparable}.
 */
public final class ConversionUtils {

    // Useful Hadoop Javadoc URLs:
    //
    //   http://hadoop.apache.org/docs/r1.2.1/api/org/apache/hadoop/io/Writable.html
    //   http://hadoop.apache.org/docs/r1.2.1/api/org/apache/hadoop/io/WritableComparable.html

    private static final Set<WritableToJSConverter> WRITABLE_CONVERTERS;
    private static final Set<JSToWritableConverter> TO_WRITABLE_CONVERTERS;

    static {
        WRITABLE_CONVERTERS = new HashSet<>();

        for (final WritableToJSConverter impl : ServiceLoader.load(WritableToJSConverter.class)) {
            WRITABLE_CONVERTERS.add(impl);
        }

        TO_WRITABLE_CONVERTERS = new HashSet<>();

        for (final JSToWritableConverter impl : ServiceLoader.load(JSToWritableConverter.class)) {
            TO_WRITABLE_CONVERTERS.add(impl);
        }
    }

    /**
     * Private constructor.
     */
    private ConversionUtils() { }

    /**
     * Convert a {@link Writable} to its JavaScript equivalent.
     *
     * <b>Note:</b> If there is no known/supported JavaScript equivalent, the {@link Writable} is returned.
     *
     * @param writable the {@link Writable} to convert
     * @param scope the scope to serialize the object
     *
     * @return the JavaScript equivalent of the {@link Writable} or the {@link Writable} passed in
     */
    public static Object writableToJS(final Writable writable, final Scriptable scope) {
        Object jsObject;

        if (writable == null) {
            jsObject = null;
        } else {
            WritableToJSConverter converter = null;

            for (final WritableToJSConverter entry : WRITABLE_CONVERTERS) {
                if (entry.canConvert(writable)) {
                    converter = entry;
                }
            }

            if (converter == null) {
                throw new RuntimeException("No Writable to JavaScript converter found for class: "
                                                   + writable.getClass().getCanonicalName());
            } else {
                //noinspection unchecked
                jsObject = converter.toJavaScript(scope, writable);
            }
        }

        return jsObject;
    }

    /**
     * Convert a JavaScript object to its {@link Writable} equivalent or throws an exception if a converter isn't found.
     *
     * @param jsObject the JavaScript object to convert
     * @param scope the scope to serialize the object
     *
     * @return the {@link Writable} equivalent of the JavaScript object
     */
    public static Writable jsToWritable(final Object jsObject, final Scriptable scope) {
        Writable writable = null;

        if (JavaScriptUtils.isDefined(jsObject)) {
            JSToWritableConverter converter = null;

            for (final JSToWritableConverter entry : TO_WRITABLE_CONVERTERS) {
                if (entry.canConvert(jsObject)) {
                    converter = entry;
                }
            }

            if (converter == null) {
                throw new RuntimeException("No JavaScript to Writable converter found for class: "
                                                   + jsObject.getClass().getCanonicalName());
            } else {
                //noinspection unchecked
                writable = converter.fromJavaScript(scope, jsObject);
            }
        } else {
            writable = NullWritable.get();
        }

        return writable;
    }

    /**
     * Convert a {@link WritableComparable} to its JavaScript equivalent.
     *
     * <b>Note:</b> If there is no known/supported JavaScript equivalent, the {@link WritableComparable} is returned.
     *
     * @param writableComparable the {@link WritableComparable} to convert
     * @param scope the scope to serialize the object
     *
     * @return the JavaScript equivalent of the {@link WritableComparable} or the {@link WritableComparable} passed in
     */
    public static Object writableComparableToJS(final WritableComparable<?> writableComparable,
                                                final Scriptable scope) {
        try {
            return writableToJS(writableComparable, scope);
        } catch (Exception e) {
            if (e.getMessage().contains("Writable to JavaScript")) {
                throw new RuntimeException(e.getMessage().replaceFirst("Writable", "WritableComparable"));
            } else {
                throw e;
            }
        }
    }

    /**
     * Convert a JavaScript object to its {@link WritableComparable} equivalent or throws an exception if a converter
     * isn't found.
     *
     * @param jsObject the JavaScript object to convert
     * @param scope the scope to serialize the object

     *
     * @return the {@link WritableComparable} equivalent of the JavaScript object
     */
    public static WritableComparable<?> jsToWritableComparable(final Object jsObject, final Scriptable scope) {
        try {
            return (WritableComparable<?>)jsToWritable(jsObject, scope);
        } catch (Exception e) {
            if (e.getMessage().contains("JavaScript to Writable")) {
                throw new RuntimeException(e.getMessage().replaceFirst("Writable", "WritableComparable"));
            } else {
                throw e;
            }
        }
    }

}
