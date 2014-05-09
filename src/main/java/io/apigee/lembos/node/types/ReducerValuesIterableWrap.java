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

package io.apigee.lembos.node.types;

import io.apigee.lembos.utils.ConversionUtils;
import org.apache.hadoop.io.Writable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.annotations.JSFunction;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

/**
 * {@link Iterator} wrapper for JavaScript-based MapReduce reduce component.
 */
public final class ReducerValuesIterableWrap extends ScriptableObject {

    public static final String CLASS_NAME = "ReducerValuesIterableWrap";

    private static final long serialVersionUID = -5468741009503757050L;

    private transient Iterator<Writable> values;
    private Scriptable scope;

    /**
     * Creates an instance of {@link ReducerValuesIterableWrap} and registers it in the JavaScript
     * {@link Scriptable} scope.
     *
     * @param scope the JavaScript scope associate the TaskInputOutputContextWrap with
     * @param iterable the iterable to wrap
     *
     * @return the created context wrapper
     */
    public static ReducerValuesIterableWrap getInstance(final Scriptable scope,
                                                     final Iterable<Writable> iterable) {
        final Context jsCtx = Context.enter();
        final Scriptable parent = scope.getParentScope() == null ? scope : scope.getParentScope();

        try {
            if (!ScriptableObject.hasProperty(parent, CLASS_NAME)) {
                try {
                    ScriptableObject.defineClass(parent, ReducerValuesIterableWrap.class);
                } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    // This should never happen at runtime but we have to throw something
                    throw new RuntimeException(e);
                }
            }

            final ReducerValuesIterableWrap itw =
                    (ReducerValuesIterableWrap)jsCtx.newObject(scope, CLASS_NAME);

            itw.values = iterable.iterator();
            itw.scope = scope;

            return itw;
        } finally {
            Context.exit();
        }
    }

    /**
     * @return the class name
     */
    @Override
    public String getClassName() {
        return CLASS_NAME;
    }

    /** Exposed JavaScript functions **/

    /**
     * Returns whether or not the iterator has more items.
     *
     * @see java.util.Iterator#hasNext()
     *
     * @return Whether or not the iterator has more items
     */
    @JSFunction
    public boolean hasNext() {
        return values.hasNext();
    }

    /**
     * Returns the next item in the iterator.
     *
     * @see java.util.Iterator#next()
     *
     * @return The next item in the iterator
     */
    @JSFunction
    public Object next() {
        return ConversionUtils.writableToJS(values.next(), scope);
    }

    /**
     * Removes the current item from the iterator.
     *
     * @see java.util.Iterator#remove()
     */
    @JSFunction
    public void remove() {
        values.remove();
    }

}
