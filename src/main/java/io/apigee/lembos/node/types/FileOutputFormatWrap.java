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

import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.annotations.JSStaticFunction;

/**
 * Java implementation of the {@link FileOutputFormat} JavaScript object.
 *
 * <b>Note:</b> Only exposes the static methods
 */
public final class FileOutputFormatWrap extends ScriptableObject {

    private static final long serialVersionUID = -3958365800538907504L;
    public static final String CLASS_NAME = "FileOutputFormat";

    /* JavaScript Methods */

    /**
     * Java wrapper for {@link FileOutputFormat#getCompressOutput(org.apache.hadoop.mapreduce.JobContext)}.
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
        return FileOutputFormatHelper.getCompressOutput(FileOutputFormat.class, ctx, thisObj, args);
    }

    /**
     * Java wrapper for
     * {@link FileOutputFormat#getOutputCompressorClass(org.apache.hadoop.mapreduce.JobContext, Class)}.
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
        return FileOutputFormatHelper.getOutputCompressorClass(FileOutputFormat.class, ctx, thisObj, args);
    }

    /**
     * Java wrapper for {@link FileOutputFormat#getOutputPath(org.apache.hadoop.mapreduce.JobContext)}.
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
        return FileOutputFormatHelper.getOutputPath(FileOutputFormat.class, ctx, thisObj, args);
    }

    /**
     * Java wrapper for
     * {@link FileOutputFormat#getPathForWorkFile(org.apache.hadoop.mapreduce.TaskInputOutputContext, String, String)}.
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
        return FileOutputFormatHelper.getPathForWorkFile(FileOutputFormat.class, ctx, thisObj, args);
    }

    /**
     * Java wrapper for {@link FileOutputFormat#getWorkOutputPath(org.apache.hadoop.mapreduce.TaskInputOutputContext)}.
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
        return FileOutputFormatHelper.getWorkOutputPath(FileOutputFormat.class, ctx, thisObj, args);
    }

    /**
     * Java wrapper for {@link FileOutputFormat#setCompressOutput(org.apache.hadoop.mapreduce.Job, boolean)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     */
    @JSStaticFunction
    public static void setCompressOutput(final Context ctx, final Scriptable thisObj, final Object[] args,
                                         final Function func) {
        FileOutputFormatHelper.setCompressOutput(FileOutputFormat.class, ctx, thisObj, args);
    }

    /**
     * Java wrapper for {@link FileOutputFormat#setOutputCompressorClass(org.apache.hadoop.mapreduce.Job, Class)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     */
    @JSStaticFunction
    public static void setOutputCompressorClass(final Context ctx, final Scriptable thisObj, final Object[] args,
                                                final Function func) {
        FileOutputFormatHelper.setOutputCompressorClass(FileOutputFormat.class, ctx, thisObj, args);
    }

    /**
     * Java wrapper for
     * {@link FileOutputFormat#setOutputPath(org.apache.hadoop.mapreduce.Job, org.apache.hadoop.fs.Path)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     */
    @JSStaticFunction
    public static void setOutputPath(final Context ctx, final Scriptable thisObj, final Object[] args,
                                     final Function func) {
        FileOutputFormatHelper.setOutputPath(FileOutputFormat.class, ctx, thisObj, args);
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
