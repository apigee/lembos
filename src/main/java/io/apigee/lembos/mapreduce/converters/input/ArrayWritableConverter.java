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

package io.apigee.lembos.mapreduce.converters.input;

import io.apigee.lembos.mapreduce.converters.WritableToJSConverter;
import io.apigee.lembos.utils.ConversionUtils;
import io.apigee.lembos.utils.JavaScriptUtils;
import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.Writable;
import org.mozilla.javascript.Scriptable;

/**
 * Implementation of {@link WritableToJSConverter} for {@link ArrayWritable}.
 */
public final class ArrayWritableConverter implements WritableToJSConverter<ArrayWritable> {

    /**
     * Takes in an {@link ArrayWritable} and returns a {@link Scriptable} array.
     *
     * @param scope the JavaScript scope
     * @param writable the value to convert
     *
     * @return the {@link Scriptable} array equivalent
     */
    @Override
    public Object toJavaScript(final Scriptable scope, final ArrayWritable writable) {
        final Writable[] entries = writable.get();
        final Object[] jsArrayEntries = new Object[entries.length];

        for (int i = 0; i < entries.length; i++) {
            jsArrayEntries[i] = ConversionUtils.writableToJS(entries[i], scope);
        }

        return JavaScriptUtils.asArray(scope, jsArrayEntries);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canConvert(final Object jsObject) {
        return jsObject instanceof ArrayWritable;
    }

}
