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

package io.apigee.lembos.mapreduce.converters.output;

import io.apigee.lembos.mapreduce.converters.JSToWritableConverter;
import io.apigee.lembos.utils.ConversionUtils;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Writable;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of {@link JSToWritableConverter} for {@link Map}.
 */
public final class MapToWritableConverter implements JSToWritableConverter {

    /**
     * Takes in a {@link Map} and returns an {@link MapWritable}.
     *
     * @param scope the JavaScript scope
     * @param jsObject the value to convert
     *
     * @return the {@link MapWritable}
     */
    @Override
    public Writable fromJavaScript(final Scriptable scope, final Object jsObject) {
        MapWritable writable = null;
        Set<Map.Entry<Object, Object>> entries = null;

        if (jsObject instanceof NativeObject) {
            entries = new HashSet<>();

            final NativeObject nativeObject = (NativeObject)jsObject;

            for (final Object key : nativeObject.getAllIds()) {
                entries.add(new AbstractMap.SimpleEntry<>(key, nativeObject.get(key)));
            }
        } else if (jsObject instanceof Map) {
            //noinspection unchecked
            entries = ((Map<Object, Object>)jsObject).entrySet();
        }

        if (entries != null) {
            final MapWritable mapWritable = new MapWritable();

            for (final Map.Entry<Object, Object> entry : entries) {
                mapWritable.put(ConversionUtils.jsToWritable(entry.getKey(), scope),
                                ConversionUtils.jsToWritable(entry.getValue(), scope));
            }

            writable = mapWritable;
        }

        return writable;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canConvert(final Object jsObject) {
        return jsObject instanceof Map;
    }

}
