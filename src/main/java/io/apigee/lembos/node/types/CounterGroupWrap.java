package io.apigee.lembos.node.types;

import io.apigee.lembos.mapreduce.LembosMessages;
import io.apigee.lembos.node.modules.HadoopInternal;
import io.apigee.lembos.utils.JavaScriptUtils;
import io.apigee.trireme.core.NodeRuntime;
import io.apigee.trireme.core.Utils;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.CounterGroup;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;
import org.mozilla.javascript.annotations.JSFunction;

/**
 * Java implementation of the {@link CounterGroup} JavaScript object.
 *
 * <b>Unsupported APIs:</b>
 * <ul>
 *   <li>{@link CounterGroup#iterator()}</li>
 *   <li>{@link CounterGroup#readFields(java.io.DataInput)}</li>
 *   <li>{@link CounterGroup#write(java.io.DataOutput)}</li>
 * </ul>
 */
public final class CounterGroupWrap extends ScriptableObject {

    private static final long serialVersionUID = 6268286011728404705L;
    public static final String CLASS_NAME = "CounterGroup";

    // These transient fields are to please Findbugs.  I realize why the errors come up but I don't see us ever
    // serializing this object.  It will always be constructed during the MapReduce component setup phase.

    private transient CounterGroup counterGroup;
    private transient NodeRuntime runtime;

    /**
     * Creates an instance of {@link CounterGroupWrap}.  (Intended to be used only for constructing
     * {@link CounterGroupWrap} objects via Java calls.)
     *
     * @param runtime the Node.js runtime
     * @param counterGroup the Hadoop counter to wrap
     *
     * @return the created counter wrapper
     */
    public static CounterGroupWrap getInstance(final NodeRuntime runtime, final CounterGroup counterGroup) {
        Context ctx = Context.getCurrentContext();

        if (ctx == null) {
            ctx = Context.enter();
        }

        final Scriptable countersModule = HadoopInternal.loadSelf(runtime);
        final CounterGroupWrap counterGroupWrapper = (CounterGroupWrap)ctx.newObject(countersModule, CLASS_NAME);

        counterGroupWrapper.counterGroup = counterGroup;
        counterGroupWrapper.runtime = runtime;

        return counterGroupWrapper;
    }

    /* JavaScript Methods */

    /**
     * Wraps {@link CounterGroup#findCounter(String)}.
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

        if (args.length < 1) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        }

        final CounterGroupWrap self = (CounterGroupWrap)thisObj;
        final Counter counter = self.counterGroup.findCounter(arg0.toString());
        CounterWrap counterWrap = null;

        if (counter != null) {
            counterWrap = CounterWrap.getInstance(self.runtime, counter);
        }

        return counterWrap == null ? Undefined.instance : counterWrap;
    }

    /**
     * Wraps {@link CounterGroup#getDisplayName()}.
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
        final String displayName = ((CounterGroupWrap)thisObj).counterGroup.getDisplayName();

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
        final String name = ((CounterGroupWrap)thisObj).counterGroup.getName();

        return name == null ? Undefined.instance : name;
    }

    /**
     * Wraps {@link CounterGroup#incrAllCounters(CounterGroup)}.
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
        } else if (!(arg0 instanceof CounterGroupWrap)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_MUST_BE_COUNTER_GROUP);
        }

        ((CounterGroupWrap)thisObj).counterGroup.incrAllCounters(((CounterGroupWrap)arg0).counterGroup);

        return thisObj;
    }

    /**
     * Wraps {@link CounterGroup#size()}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return the size
     */
    @JSFunction
    public static Object size(final Context ctx, final Scriptable thisObj, final Object[] args, final Function func) {
        return ((CounterGroupWrap)thisObj).counterGroup.size();
    }

    /* Java Methods */

    @Override
    public String getClassName() {
        return CLASS_NAME;
    }

}
