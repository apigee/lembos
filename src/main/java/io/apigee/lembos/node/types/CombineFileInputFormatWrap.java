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

import org.apache.hadoop.mapreduce.lib.input.CombineFileInputFormat;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.annotations.JSStaticFunction;

/**
 * Java implementation of the {@link CombineFileInputFormat} JavaScript object.
 *
 * <b>Note:</b> Only exposes the static methods
 */
public final class CombineFileInputFormatWrap extends ScriptableObject {

    private static final long serialVersionUID = -2029058519361140014L;
    public static final String CLASS_NAME = "CombineFileInputFormat";

    /* JavaScript Methods */

    /**
     * Java wrapper for
     * {@link CombineFileInputFormat#addInputPath(org.apache.hadoop.mapreduce.Job, org.apache.hadoop.fs.Path)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     */
    @JSStaticFunction
    public static void addInputPath(final Context ctx, final Scriptable thisObj, final Object[] args,
                                    final Function func) {
        FileInputFormatHelper.addInputPath(CombineFileInputFormat.class, ctx, thisObj, args);
    }

    /**
     * Java wrapper for
     * {@link CombineFileInputFormat#addInputPaths(org.apache.hadoop.mapreduce.Job, String)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     */
    @JSStaticFunction
    public static void addInputPaths(final Context ctx, final Scriptable thisObj, final Object[] args,
                                     final Function func) {
        FileInputFormatHelper.addInputPaths(CombineFileInputFormat.class, ctx, thisObj, args);
    }

    /**
     * Java wrapper for {@link CombineFileInputFormat#getInputPathFilter(org.apache.hadoop.mapreduce.JobContext)}.
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
        return FileInputFormatHelper.getInputPathFilter(CombineFileInputFormat.class, ctx, thisObj, args);
    }

    /**
     * Java wrapper for {@link CombineFileInputFormat#getInputPaths(org.apache.hadoop.mapreduce.JobContext)}.
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
        return FileInputFormatHelper.getInputPaths(CombineFileInputFormat.class, ctx, thisObj, args);
    }

    /**
     * Java wrapper for {@link CombineFileInputFormat#getMaxSplitSize(org.apache.hadoop.mapreduce.JobContext)}.
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
        return FileInputFormatHelper.getMaxSplitSize(CombineFileInputFormat.class, ctx, thisObj, args);
    }

    /**
     * Java wrapper for {@link CombineFileInputFormat#getMinSplitSize(org.apache.hadoop.mapreduce.JobContext)}.
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
       return FileInputFormatHelper.getMinSplitSize(CombineFileInputFormat.class, ctx, thisObj, args);
    }

    /**
     * Wraps {@link CombineFileInputFormat#setInputPathFilter(org.apache.hadoop.mapreduce.Job, Class)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     */
    @JSStaticFunction
    public static void setInputPathFilter(final Context ctx, final Scriptable thisObj, final Object[] args,
                                            final Function func) {
        FileInputFormatHelper.setInputPathFilter(CombineFileInputFormat.class, ctx, thisObj, args);
    }

    /**
     * Java wrapper for
     * {@link CombineFileInputFormat#setInputPaths(org.apache.hadoop.mapreduce.Job, org.apache.hadoop.fs.Path...)} and
     * {@link CombineFileInputFormat#setInputPaths(org.apache.hadoop.mapreduce.Job, String)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     */
    @JSStaticFunction
    public static void setInputPaths(final Context ctx, final Scriptable thisObj, final Object[] args,
                                     final Function func) {
        FileInputFormatHelper.setInputPaths(CombineFileInputFormat.class, ctx, thisObj, args);
    }

    /**
     * Java wrapper for {@link CombineFileInputFormat#setMaxInputSplitSize(org.apache.hadoop.mapreduce.Job, long)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     */
    @JSStaticFunction
    public static void setMaxInputSplitSize(final Context ctx, final Scriptable thisObj, final Object[] args,
                                            final Function func) {
        FileInputFormatHelper.setMaxInputSplitSize(CombineFileInputFormat.class, ctx, thisObj, args);
    }

    /**
     * Java wrapper for {@link CombineFileInputFormat#setMinInputSplitSize(org.apache.hadoop.mapreduce.Job, long)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     */
    @JSStaticFunction
    public static void setMinInputSplitSize(final Context ctx, final Scriptable thisObj, final Object[] args,
                                            final Function func) {
        FileInputFormatHelper.setMinInputSplitSize(CombineFileInputFormat.class, ctx, thisObj, args);
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
