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
import org.apache.hadoop.mapreduce.CounterGroup;
import org.apache.hadoop.mapreduce.Counters;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;
import org.mozilla.javascript.annotations.JSFunction;

/**
 * Java implementation of the {@link Counters} JavaScript object.
 *
 * <b>Unsupported APIs:</b>
 * <ul>
 *   <li>{@link Counters#Counters()}</li>
 *   <li>{@link Counters#Counters(org.apache.hadoop.mapred.Counters)}</li>
 *   <li>{@link Counters#findCounter(Enum)}</li>
 *   <li>{@link Counters#iterator()}</li>
 *   <li>{@link Counters#readFields(java.io.DataInput)}</li>
 *   <li>{@link Counters#write(java.io.DataOutput)}</li>
 * </ul>
 */
public final class CountersWrap extends ScriptableObject {

    private static final long serialVersionUID = -4011352005215545938L;
    public static final String CLASS_NAME = "Counters";

    // These transient fields are to please Findbugs.  I realize why the errors come up but I don't see us ever
    // serializing this object.  It will always be constructed during the MapReduce component setup phase.

    private transient Counters counters;
    private transient NodeRuntime runtime;

    /**
     * Creates an instance of {@link CountersWrap}.  (Intended to be used only for constructing {@link CountersWrap}
     * objects via Java calls.)
     *
     * @param runtime the Node.js runtime
     * @param counters the Hadoop counters to wrap
     *
     * @return the created counter wrapper
     */
    public static CountersWrap getInstance(final NodeRuntime runtime, final Counters counters) {
        Context ctx = Context.getCurrentContext();

        if (ctx == null) {
            ctx = Context.enter();
        }

        final Scriptable countersModule = HadoopInternal.loadSelf(runtime);
        final CountersWrap countersWrapper = (CountersWrap)ctx.newObject(countersModule, CLASS_NAME);

        countersWrapper.counters = counters;
        countersWrapper.runtime = runtime;

        return countersWrapper;
    }

    /* JavaScript Methods */

    /**
     * Wraps {@link Counters#countCounters()}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return the number of counters
     */
    @JSFunction
    public static Object countCounters(final Context ctx, final Scriptable thisObj, final Object[] args,
                                       final Function func) {
        return ((CountersWrap)thisObj).counters.countCounters();
    }

    /**
     * Wraps {@link Counters#findCounter(String, String)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return the counter
     */
    @JSFunction
    public static Object findCounter(final Context ctx, final Scriptable thisObj, final Object[] args,
                                     final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;
        final Object arg1 = args.length >= 2 ? args[1] : Undefined.instance;

        if (args.length < 2) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.TWO_ARGS_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!JavaScriptUtils.isDefined(arg1)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_REQUIRED);
        }

        final CountersWrap self = (CountersWrap)thisObj;
        final Counter counter = self.counters.findCounter(arg0.toString(), arg1.toString());
        CounterWrap counterWrap = null;

        if (counter != null) {
            counterWrap = CounterWrap.getInstance(self.runtime, counter);
        }

        return counterWrap == null ? Undefined.instance : counterWrap;
    }

    /**
     * Wraps {@link Counters#getGroup(String)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return the counter group
     */
    @JSFunction
    public static Object getGroup(final Context ctx, final Scriptable thisObj, final Object[] args,
                                  final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;

        if (args.length < 1) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        }

        final CountersWrap self = (CountersWrap)thisObj;
        final CounterGroup counterGroup = self.counters.getGroup(arg0.toString());
        CounterGroupWrap counterGroupWrap = null;

        if (counterGroup != null) {
            counterGroupWrap = CounterGroupWrap.getInstance(self.runtime, counterGroup);
        }

        return counterGroupWrap == null ? Undefined.instance : counterGroupWrap;
    }

    /**
     * Wraps {@link Counters#getGroupNames()}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return the counter group names
     */
    @JSFunction
    public static Object getGroupNames(final Context ctx, final Scriptable thisObj, final Object[] args,
                                       final Function func) {
        return JavaScriptUtils.asArray(thisObj, ((CountersWrap)thisObj).counters.getGroupNames());
    }

    /**
     * Wraps {@link Counters#incrAllCounters(Counters)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return this
     */
    @JSFunction
    public static Object incrAllCounters(final Context ctx, final Scriptable thisObj, final Object[] args,
                                         final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;

        if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        } else if (!(arg0 instanceof CountersWrap)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_MUST_BE_COUNTERS);
        }

        ((CountersWrap)thisObj).counters.incrAllCounters(((CountersWrap)arg0).counters);

        return thisObj;
    }

    /* Java Methods */

    @Override
    public String getClassName() {
        return CLASS_NAME;
    }

}
