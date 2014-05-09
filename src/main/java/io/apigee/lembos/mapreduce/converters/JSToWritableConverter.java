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
 * Interface to be implemented by all to {@link Writable} type converters.
 */
public interface JSToWritableConverter {

    /**
     * Takes a Java/JavaScript object and converts it to the appropriate {@link Writable}.
     *
     *
     * @param scope the JavaScript scope
     * @param jsObject the object to convert
     *
     * @return the appropriate {@link Writable}
     */
    Writable fromJavaScript(final Scriptable scope, final Object jsObject);

    /**
     * Returns whether or not the Java/JavaScript object can be converted by this converter.
     *
     * @param jsObject the object to convert
     *
     * @return whether or not this converter can convert the object
     */
    boolean canConvert(final Object jsObject);

}
