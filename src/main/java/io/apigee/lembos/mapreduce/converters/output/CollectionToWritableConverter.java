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
import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.Writable;
import org.mozilla.javascript.Scriptable;

import java.util.Collection;

/**
 * Implementation of {@link JSToWritableConverter} for {@link Collection}.
 */
public final class CollectionToWritableConverter implements JSToWritableConverter {

    /**
     * Takes in a {@link Collection} and returns an {@link ArrayWritable}.
     *
     * @param scope the JavaScript scope
     * @param jsObject the value to convert
     *
     * @return the {@link ArrayWritable}
     */
    @Override
    public Writable fromJavaScript(final Scriptable scope, final Object jsObject) {
        final Object[] entries = ((Collection)jsObject).toArray();
        final Writable[] writables = new Writable[entries.length];

        for (int i = 0; i < writables.length; i++) {
            writables[i] = ConversionUtils.jsToWritable(entries[i], scope);
        }

        return new ArrayWritable(Writable.class, writables);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canConvert(final Object jsObject) {
        return jsObject instanceof Collection;
    }

}
