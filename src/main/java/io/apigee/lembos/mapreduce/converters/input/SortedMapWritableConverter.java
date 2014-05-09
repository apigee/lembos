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
import org.apache.hadoop.io.SortedMapWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.mozilla.javascript.Scriptable;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of {@link WritableToJSConverter} for {@link SortedMapWritable}.
 */
public final class SortedMapWritableConverter implements WritableToJSConverter<SortedMapWritable> {

    /**
     * Takes in a {@link SortedMapWritable} and returns a {@link Scriptable} map.
     *
     * @param scope the JavaScript scope
     * @param writable the value to convert
     *
     * @return the {@link Scriptable} map equivalent
     */
    @Override
    public Object toJavaScript(final Scriptable scope, final SortedMapWritable writable) {
        final Map<Object, Object> writableMap = new HashMap<>();

        for (final Map.Entry<WritableComparable, Writable> mapEntry : writable.entrySet()) {
            writableMap.put(ConversionUtils.writableComparableToJS(mapEntry.getKey(), scope),
                            ConversionUtils.writableToJS(mapEntry.getValue(), scope));
        }

        return JavaScriptUtils.asObject(scope, writableMap);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canConvert(final Object jsObject) {
        return jsObject instanceof SortedMapWritable;
    }

}
