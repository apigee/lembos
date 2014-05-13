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

import io.apigee.lembos.mapreduce.LembosMessages;
import io.apigee.lembos.node.modules.HadoopInternal;
import io.apigee.lembos.utils.JavaScriptUtils;
import io.apigee.trireme.core.NodeRuntime;
import io.apigee.trireme.core.Utils;
import org.apache.hadoop.mapreduce.Counter;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;
import org.mozilla.javascript.annotations.JSFunction;

/**
 * Java implementation of the {@link Counter} JavaScript object.
 *
 * <b>Unsupported APIs:</b>
 * <ul>
 *   <li>{@link Counter#readFields(java.io.DataInput)}</li>
 *   <li>{@link Counter#write(java.io.DataOutput)}</li>
 * </ul>
 */
public final class CounterWrap extends ScriptableObject {

    private static final long serialVersionUID = 7165107608851723579L;
    public static final String CLASS_NAME = "Counter";

    // These transient fields are to please Findbugs.  I realize why the errors come up but I don't see us ever
    // serializing this object.  It will always be constructed during the MapReduce component setup phase.

    private transient Counter counter;

    /**
     * Creates an instance of {@link CounterWrap}.  (Intended to be used only for constructing {@link CounterWrap}
     * objects via Java calls.)
     *
     * @param runtime the Node.js runtime
     * @param counter the Hadoop counter to wrap
     *
     * @return the created counter wrapper
     */
    public static CounterWrap getInstance(final NodeRuntime runtime, final Counter counter) {
        Context ctx = Context.getCurrentContext();

        if (ctx == null) {
            ctx = Context.enter();
        }

        final Scriptable countersModule = HadoopInternal.loadSelf(runtime);
        final CounterWrap counterWrapper = (CounterWrap)ctx.newObject(countersModule, CLASS_NAME);

        counterWrapper.counter = counter;

        return counterWrapper;
    }

    /* JavaScript Methods */

    /**
     * Wraps {@link Counter#getDisplayName()}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return the display name
     */
    @JSFunction
    public static Object getDisplayName(final Context ctx, final Scriptable thisObj, final Object[] args,
                                        final Function func) {
        final String displayName = ((CounterWrap)thisObj).counter.getDisplayName();

        return displayName == null ? Undefined.instance : displayName;
    }

    /**
     * Wraps {@link Counter#getName()}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return the name
     */
    @JSFunction
    public static Object getName(final Context ctx, final Scriptable thisObj, final Object[] args,
                                 final Function func) {
        final String name = ((CounterWrap)thisObj).counter.getName();

        return name == null ? Undefined.instance : name;
    }

    /**
     * Wraps {@link Counter#getValue()}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return the value
     */
    @JSFunction
    public static Object getValue(final Context ctx, final Scriptable thisObj, final Object[] args,
                                  final Function func) {
        return ((CounterWrap)thisObj).counter.getValue();
    }

    /**
     * Wraps {@link Counter#increment(long)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return this
     */
    @JSFunction
    public static Object increment(final Context ctx, final Scriptable thisObj, final Object[] args,
                                   final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;

        if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        } else if (!(arg0 instanceof Number)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_MUST_BE_NUM);
        }

        ((CounterWrap)thisObj).counter.increment(JavaScriptUtils.fromNumber(arg0).longValue());

        return thisObj;
    }

    /**
     * Wraps {@link Counter#setValue(long)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return this
     */
    @JSFunction
    public static Object setValue(final Context ctx, final Scriptable thisObj, final Object[] args,
                                  final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;

        if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        } else if (!(arg0 instanceof Number)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_MUST_BE_NUM);
        }

        ((CounterWrap)thisObj).counter.setValue(JavaScriptUtils.fromNumber(arg0).longValue());

        return thisObj;
    }

    /**
     * @param counter the counter to wrap
     */
    public void setCounter(final Counter counter) {
        this.counter = counter;
    }

    /* Java Methods */

    @Override
    public String getClassName() {
        return CLASS_NAME;
    }

}
