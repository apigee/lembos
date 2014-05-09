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
import io.apigee.trireme.core.NodeRuntime;
import io.apigee.trireme.core.Utils;
import org.apache.hadoop.mapreduce.TaskInputOutputContext;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.annotations.JSFunction;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * {@link TaskInputOutputContext} wrapper for JavaScript-based MapReduce components.
 */
public final class TaskInputOutputContextWrap extends ScriptableObject {

    private static final long serialVersionUID = -458056708802284387L;
    public static final String CLASS_NAME = "TaskInputOutputContextWrap";

    // These transient fields are to please Findbugs.  I realize why the errors come up but I don't see us ever
    // serializing this object.  It will always be constructed during the MapReduce component setup phase.

    private transient TaskInputOutputContext context;

    private Scriptable scope;
    private Scriptable jsConf;

    /**
     * Creates an instance of {@link TaskInputOutputContextWrap}, registers it in the JavaScript {@link Scriptable}
     * scope and sets up the Java<->JavaScript bridge for the {@link TaskInputOutputContext}.
     *
     * @param scope the JavaScript scope associate the TaskInputOutputContextWrap with
     * @param runtime the Node.js runtime
     * @param context the Hadoop context being wrapped
     *
     * @return the created context wrapper
     */
    public static TaskInputOutputContextWrap getInstance(final Scriptable scope, final NodeRuntime runtime,
                                                         final TaskInputOutputContext context) {
        final Scriptable parent = scope.getParentScope() == null ? scope : scope.getParentScope();
        Context ctx = Context.getCurrentContext();

        if (ctx == null) {
            ctx = Context.enter();
        }

        try {
            if (!ScriptableObject.hasProperty(parent, CLASS_NAME)) {
                try {
                    ScriptableObject.defineClass(parent, TaskInputOutputContextWrap.class);
                } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    // This should never happen at runtime but we have to throw something
                    throw new RuntimeException(e);
                }
            }

            final TaskInputOutputContextWrap contextWrapper = (TaskInputOutputContextWrap)ctx.newObject(scope,
                                                                                                          CLASS_NAME);

            contextWrapper.jsConf = ConfigurationWrap.getInstance(runtime, context.getConfiguration());
            contextWrapper.context = context;
            contextWrapper.scope = scope;

            return contextWrapper;
        } finally {
            Context.exit();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getClassName() {
        return CLASS_NAME;
    }

    /** Exposed JavaScript Functions **/

    /**
     * Wraps {@link TaskInputOutputContext#getConfiguration()}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call (unused)
     * @param func the function called (unused)
     *
     * @return the configuration wrapper
     */
    @JSFunction
    @SuppressWarnings({
            "unchecked" // Unavoidable
    })
    public static Scriptable getConfiguration(final Context ctx, final Scriptable thisObj, final Object[] args,
                                              final Function func) {
        return ((TaskInputOutputContextWrap)thisObj).jsConf;
    }

    /**
     * Wraps {@link TaskInputOutputContext#write(Object, Object)} to conditionally serialize the key and value to
     * the appropriate {@link org.apache.hadoop.io.WritableComparable} and {@link org.apache.hadoop.io.Writable}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call (unused)
     * @param func the function called (unused)
     */
    @JSFunction
    @SuppressWarnings({
            "unchecked" // Unavoidable
    })
    public static void write(final Context ctx, final Scriptable thisObj, final Object[] args, final Function func) {
        if (args.length != 2) {
            throw Utils.makeError(ctx, thisObj, "Two arguments expected");
        }

        try {
            final TaskInputOutputContextWrap self = (TaskInputOutputContextWrap)thisObj;
            final Object jsKey = args[0];
            final Object jsVal = args[1];

            self.context.write(ConversionUtils.jsToWritableComparable(jsKey, self.scope),
                               ConversionUtils.jsToWritable(jsVal, self.scope));
        } catch (InterruptedException | IOException e) {
            throw Utils.makeError(ctx, thisObj, "Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * @return the raw context
     */
    public TaskInputOutputContext getContext() {
        return context;
    }

}
