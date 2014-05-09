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

package io.apigee.lembos.mapreduce.converters;

import org.apache.hadoop.io.Writable;
import org.mozilla.javascript.Scriptable;

/**
 * Interface to be implemented by all {@link Writable} type converters.
 *
 * @param <T> type extending {@link Writable}
 */
public interface WritableToJSConverter<T extends Writable> {

    /**
     * Takes a {@link Writable} from a Java-based MapReduce component, like an
     * {@link org.apache.hadoop.mapreduce.InputFormat} for example, and converts it to a Java/JavaScript type.
     *
     * <b>Note:</b> You can pass in the {@link Writable} as-is, that is the default for unknown types
     *
     * @param scope the JavaScript scope
     * @param writable the writable to convert
     *
     * @return the converted Java/JavaScript type
     */
    Object toJavaScript(final Scriptable scope, final T writable);

    /**
     * Returns whether or not the {@link Writable} object can be converted by this converter.
     *
     * @param jsObject the object to convert
     *
     * @return whether or not this converter can convert the object
     */
    boolean canConvert(final Object jsObject);

}
