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

import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.annotations.JSStaticFunction;

/**
 * Java implementation of the {@link TextOutputFormat} JavaScript object.
 *
 * <b>Note:</b> Only exposes the static methods
 */
public final class TextOutputFormatWrap extends ScriptableObject {

    private static final long serialVersionUID = -2466565119551973628L;
    public static final String CLASS_NAME = "TextOutputFormat";

    /* JavaScript Methods */

    /**
     * Java wrapper for {@link TextOutputFormat#getCompressOutput(org.apache.hadoop.mapreduce.JobContext)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     *
     * @return whether or not the output is compressed
     */
    @JSStaticFunction
    public static Object getCompressOutput(final Context ctx, final Scriptable thisObj, final Object[] args,
                                         final Function func) {
        return FileOutputFormatHelper.getCompressOutput(TextOutputFormat.class, ctx, thisObj, args);
    }

    /**
     * Java wrapper for
     * {@link TextOutputFormat#getOutputCompressorClass(org.apache.hadoop.mapreduce.JobContext, Class)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     *
     * @return the output compressor class
     */
    @JSStaticFunction
    public static Object getOutputCompressorClass(final Context ctx, final Scriptable thisObj, final Object[] args,
                                           final Function func) {
        return FileOutputFormatHelper.getOutputCompressorClass(TextOutputFormat.class, ctx, thisObj, args);
    }

    /**
     * Java wrapper for {@link TextOutputFormat#getOutputPath(org.apache.hadoop.mapreduce.JobContext)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     *
     * @return the output path
     */
    @JSStaticFunction
    public static Object getOutputPath(final Context ctx, final Scriptable thisObj, final Object[] args,
                                       final Function func) {
        return FileOutputFormatHelper.getOutputPath(TextOutputFormat.class, ctx, thisObj, args);
    }

    /**
     * Java wrapper for {@link TextOutputFormat#getPathForWorkFile(org.apache.hadoop.mapreduce.TaskInputOutputContext,
     *                                                             String, String)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     *
     * @return the path for the work file
     */
    @JSStaticFunction
    public static Object getPathForWorkFile(final Context ctx, final Scriptable thisObj, final Object[] args,
                                            final Function func) {
        return FileOutputFormatHelper.getPathForWorkFile(TextOutputFormat.class, ctx, thisObj, args);
    }

    /**
     * Java wrapper for {@link TextOutputFormat#getWorkOutputPath(org.apache.hadoop.mapreduce.TaskInputOutputContext)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     *
     * @return the path for the work output
     */
    @JSStaticFunction
    public static Object getWorkOutputPath(final Context ctx, final Scriptable thisObj, final Object[] args,
                                           final Function func) {
        return FileOutputFormatHelper.getWorkOutputPath(TextOutputFormat.class, ctx, thisObj, args);
    }

    /**
     * Java wrapper for {@link TextOutputFormat#setCompressOutput(org.apache.hadoop.mapreduce.Job, boolean)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     */
    @JSStaticFunction
    public static void setCompressOutput(final Context ctx, final Scriptable thisObj, final Object[] args,
                                         final Function func) {
        FileOutputFormatHelper.setCompressOutput(TextOutputFormat.class, ctx, thisObj, args);
    }

    /**
     * Java wrapper for {@link TextOutputFormat#setOutputCompressorClass(org.apache.hadoop.mapreduce.Job, Class)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     */
    @JSStaticFunction
    public static void setOutputCompressorClass(final Context ctx, final Scriptable thisObj, final Object[] args,
                                                final Function func) {
        FileOutputFormatHelper.setOutputCompressorClass(TextOutputFormat.class, ctx, thisObj, args);
    }

    /**
     * Java wrapper for
     * {@link TextOutputFormat#setOutputPath(org.apache.hadoop.mapreduce.Job, org.apache.hadoop.fs.Path)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     */
    @JSStaticFunction
    public static void setOutputPath(final Context ctx, final Scriptable thisObj, final Object[] args,
                                     final Function func) {
        FileOutputFormatHelper.setOutputPath(TextOutputFormat.class, ctx, thisObj, args);
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
