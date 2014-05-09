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

import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.annotations.JSStaticFunction;

/**
 * Java implementation of the {@link TextInputFormat} JavaScript object.
 *
 * <b>Note:</b> Only exposes the static methods
 */
public final class TextInputFormatWrap extends ScriptableObject {

    private static final long serialVersionUID = 2284556022194450491L;
    public static final String CLASS_NAME = "TextInputFormat";

    /* JavaScript Methods */

    /**
     * Java wrapper for
     * {@link TextInputFormat#addInputPath(org.apache.hadoop.mapreduce.Job, org.apache.hadoop.fs.Path)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     */
    @JSStaticFunction
    public static void addInputPath(final Context ctx, final Scriptable thisObj, final Object[] args,
                                    final Function func) {
        FileInputFormatHelper.addInputPath(TextInputFormat.class, ctx, thisObj, args);
    }

    /**
     * Java wrapper for {@link TextInputFormat#addInputPaths(org.apache.hadoop.mapreduce.Job, String)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     */
    @JSStaticFunction
    public static void addInputPaths(final Context ctx, final Scriptable thisObj, final Object[] args,
                                     final Function func) {
        FileInputFormatHelper.addInputPaths(TextInputFormat.class, ctx, thisObj, args);
    }

    /**
     * Java wrapper for {@link TextInputFormat#getInputPathFilter(org.apache.hadoop.mapreduce.JobContext)}.
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
        return FileInputFormatHelper.getInputPathFilter(TextInputFormat.class, ctx, thisObj, args);
    }

    /**
     * Java wrapper for {@link TextInputFormat#getInputPaths(org.apache.hadoop.mapreduce.JobContext)}.
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
        return FileInputFormatHelper.getInputPaths(TextInputFormat.class, ctx, thisObj, args);
    }

    /**
     * Java wrapper for {@link TextInputFormat#getMaxSplitSize(org.apache.hadoop.mapreduce.JobContext)}.
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
        return FileInputFormatHelper.getMaxSplitSize(TextInputFormat.class, ctx, thisObj, args);
    }

    /**
     * Java wrapper for {@link TextInputFormat#getMinSplitSize(org.apache.hadoop.mapreduce.JobContext)}.
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
       return FileInputFormatHelper.getMinSplitSize(TextInputFormat.class, ctx, thisObj, args);
    }

    /**
     * Wraps {@link TextInputFormat#setInputPathFilter(org.apache.hadoop.mapreduce.Job, Class)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     */
    @JSStaticFunction
    public static void setInputPathFilter(final Context ctx, final Scriptable thisObj, final Object[] args,
                                            final Function func) {
        FileInputFormatHelper.setInputPathFilter(TextInputFormat.class, ctx, thisObj, args);
    }

    /**
     * Java wrapper for
     * {@link TextInputFormat#setInputPaths(org.apache.hadoop.mapreduce.Job, org.apache.hadoop.fs.Path...)} and
     * {@link TextInputFormat#setInputPaths(org.apache.hadoop.mapreduce.Job, String)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     */
    @JSStaticFunction
    public static void setInputPaths(final Context ctx, final Scriptable thisObj, final Object[] args,
                                     final Function func) {
        FileInputFormatHelper.setInputPaths(TextInputFormat.class, ctx, thisObj, args);
    }

    /**
     * Java wrapper for {@link TextInputFormat#setMaxInputSplitSize(org.apache.hadoop.mapreduce.Job, long)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     */
    @JSStaticFunction
    public static void setMaxInputSplitSize(final Context ctx, final Scriptable thisObj, final Object[] args,
                                            final Function func) {
        FileInputFormatHelper.setMaxInputSplitSize(TextInputFormat.class, ctx, thisObj, args);
    }

    /**
     * Java wrapper for {@link TextInputFormat#setMinInputSplitSize(org.apache.hadoop.mapreduce.Job, long)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     */
    @JSStaticFunction
    public static void setMinInputSplitSize(final Context ctx, final Scriptable thisObj, final Object[] args,
                                            final Function func) {
        FileInputFormatHelper.setMinInputSplitSize(TextInputFormat.class, ctx, thisObj, args);
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
