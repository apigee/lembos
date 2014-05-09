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
import io.apigee.lembos.utils.JavaScriptUtils;
import io.apigee.trireme.core.Utils;
import org.apache.hadoop.mapreduce.lib.input.NLineInputFormat;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;
import org.mozilla.javascript.annotations.JSStaticFunction;

/**
 * Java implementation of the {@link NLineInputFormat} JavaScript object.
 *
 * <b>Note:</b> Only exposes the static methods
 *
 * * <b>Unsupported APIs:</b>
 * <ul>
 *   <li>{@link NLineInputFormat#getSplitsForFile(org.apache.hadoop.fs.FileStatus, org.apache.hadoop.conf.Configuration,
 *                                                int)}</li>
 * </ul>
 */
public final class NLineInputFormatWrap extends ScriptableObject {

    private static final long serialVersionUID = -7181148429301262025L;
    public static final String CLASS_NAME = "NLineInputFormat";

    /* JavaScript Methods */

    /**
     * Java wrapper for
     * {@link NLineInputFormat#addInputPath(org.apache.hadoop.mapreduce.Job, org.apache.hadoop.fs.Path)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     */
    @JSStaticFunction
    public static void addInputPath(final Context ctx, final Scriptable thisObj, final Object[] args,
                                    final Function func) {
        FileInputFormatHelper.addInputPath(NLineInputFormat.class, ctx, thisObj, args);
    }

    /**
     * Java wrapper for {@link NLineInputFormat#addInputPaths(org.apache.hadoop.mapreduce.Job, String)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     */
    @JSStaticFunction
    public static void addInputPaths(final Context ctx, final Scriptable thisObj, final Object[] args,
                                     final Function func) {
        FileInputFormatHelper.addInputPaths(NLineInputFormat.class, ctx, thisObj, args);
    }

    /**
     * Java wrapper for {@link NLineInputFormat#getInputPathFilter(org.apache.hadoop.mapreduce.JobContext)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     *
     * @return class name for the input path filter or undefined
     */
    @JSStaticFunction
    public static Object getInputPathFilter(final Context ctx, final Scriptable thisObj, final Object[] args,
                                            final Function func) {
        return FileInputFormatHelper.getInputPathFilter(NLineInputFormat.class, ctx, thisObj, args);
    }

    /**
     * Java wrapper for {@link NLineInputFormat#getInputPaths(org.apache.hadoop.mapreduce.JobContext)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     *
     * @return array of input paths
     */
    @JSStaticFunction
    public static Object getInputPaths(final Context ctx, final Scriptable thisObj, final Object[] args,
                                       final Function func) {
        return FileInputFormatHelper.getInputPaths(NLineInputFormat.class, ctx, thisObj, args);
    }

    /**
     * Java wrapper for {@link NLineInputFormat#getMaxSplitSize(org.apache.hadoop.mapreduce.JobContext)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     *
     * @return the max split size
     */
    @JSStaticFunction
    public static Object getMaxSplitSize(final Context ctx, final Scriptable thisObj, final Object[] args,
                                         final Function func) {
        return FileInputFormatHelper.getMaxSplitSize(NLineInputFormat.class, ctx, thisObj, args);
    }

    /**
     * Java wrapper for {@link NLineInputFormat#getMinSplitSize(org.apache.hadoop.mapreduce.JobContext)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     *
     * @return the max split size
     */
    @JSStaticFunction
    public static Object getMinSplitSize(final Context ctx, final Scriptable thisObj, final Object[] args,
                                         final Function func) {
       return FileInputFormatHelper.getMinSplitSize(NLineInputFormat.class, ctx, thisObj, args);
    }

    /**
     * Wraps {@link NLineInputFormat#setInputPathFilter(org.apache.hadoop.mapreduce.Job, Class)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     */
    @JSStaticFunction
    public static void setInputPathFilter(final Context ctx, final Scriptable thisObj, final Object[] args,
                                            final Function func) {
        FileInputFormatHelper.setInputPathFilter(NLineInputFormat.class, ctx, thisObj, args);
    }

    /**
     * Java wrapper for
     * {@link NLineInputFormat#setInputPaths(org.apache.hadoop.mapreduce.Job, org.apache.hadoop.fs.Path...)} and
     * {@link NLineInputFormat#setInputPaths(org.apache.hadoop.mapreduce.Job, String)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     */
    @JSStaticFunction
    public static void setInputPaths(final Context ctx, final Scriptable thisObj, final Object[] args,
                                     final Function func) {
        FileInputFormatHelper.setInputPaths(NLineInputFormat.class, ctx, thisObj, args);
    }

    /**
     * Java wrapper for {@link NLineInputFormat#setMaxInputSplitSize(org.apache.hadoop.mapreduce.Job, long)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     */
    @JSStaticFunction
    public static void setMaxInputSplitSize(final Context ctx, final Scriptable thisObj, final Object[] args,
                                            final Function func) {
        FileInputFormatHelper.setMaxInputSplitSize(NLineInputFormat.class, ctx, thisObj, args);
    }

    /**
     * Java wrapper for {@link NLineInputFormat#setMinInputSplitSize(org.apache.hadoop.mapreduce.Job, long)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     */
    @JSStaticFunction
    public static void setMinInputSplitSize(final Context ctx, final Scriptable thisObj, final Object[] args,
                                            final Function func) {
        FileInputFormatHelper.setMinInputSplitSize(NLineInputFormat.class, ctx, thisObj, args);
    }

    /**
     * Java wrapper for {@link NLineInputFormat#getNumLinesPerSplit(org.apache.hadoop.mapreduce.JobContext)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     *
     * @return the number of lines per split
     */
    @JSStaticFunction
    public static Object getNumLinesPerSplit(final Context ctx, final Scriptable thisObj, final Object[] args,
                                            final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;

        if (args.length < 1) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!(arg0 instanceof JobWrap)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_MUST_BE_JOB);
        }

        return NLineInputFormat.getNumLinesPerSplit(((JobWrap)arg0).getJob());
    }

    /**
     * Java wrapper for {@link NLineInputFormat#setNumLinesPerSplit(org.apache.hadoop.mapreduce.Job, int)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function called (unused)
     */
    @JSStaticFunction
    public static void setNumLinesPerSplit(final Context ctx, final Scriptable thisObj, final Object[] args,
                                           final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;
        final Object arg1 = args.length >= 2 ? args[1] : Undefined.instance;

        if (args.length < 2) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.TWO_ARGS_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!JavaScriptUtils.isDefined(arg1)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_REQUIRED);
        } else if (!(arg0 instanceof JobWrap)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_MUST_BE_JOB);
        } else if (!(arg1 instanceof Number)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_ARG_MUST_BE_NUM);
        }

        NLineInputFormat.setNumLinesPerSplit(((JobWrap)arg0).getJob(), JavaScriptUtils.fromNumber(arg1).intValue());
    }

    /* Java Methods */

    /**
     * {@inheritDoc}
     */
    @Override
    public String getClassName() {
        return CLASS_NAME;
    }

}
