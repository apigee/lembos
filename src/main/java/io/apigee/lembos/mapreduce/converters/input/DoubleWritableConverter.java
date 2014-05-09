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
import org.apache.hadoop.io.DoubleWritable;
import org.mozilla.javascript.Scriptable;

/**
 * Implementation of {@link WritableToJSConverter} for {@link DoubleWritable}.
 */
public final class DoubleWritableConverter implements WritableToJSConverter<DoubleWritable> {

    /**
     * Takes in a {@link DoubleWritable} and returns a {@link Double}.
     *
     * @param scope the JavaScript scope
     * @param writable the value to convert
     *
     * @return the {@link Double} equivalent
     */
    @Override
    public Object toJavaScript(final Scriptable scope, final DoubleWritable writable) {
        return writable.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canConvert(final Object jsObject) {
        return jsObject instanceof DoubleWritable;
    }

}
