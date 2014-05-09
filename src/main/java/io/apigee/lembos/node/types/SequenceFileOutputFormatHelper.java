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
import io.apigee.lembos.utils.ReflectionUtils;
import io.apigee.trireme.core.Utils;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Undefined;

/**
 * Helper for invoking {@link SequenceFileOutputFormat} and its subclasses methods.
 */
public class SequenceFileOutputFormatHelper {

    /**
     * Constructor.
     */
    protected SequenceFileOutputFormatHelper() { }

    /**
     * Java wrapper for {@link SequenceFileOutputFormat#getOutputCompressionType(JobContext)}.
     *
     * @param clazz the class to invoke the method of
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     *
     * @return the output compression type
     */
    public static Object getOutputCompressionType(final Class<?> clazz, final Context ctx, final Scriptable thisObj,
                                                  final Object[] args) {
        validateClass(clazz, ctx, thisObj);

        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;

        if (args.length < 1) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!(arg0 instanceof JobWrap)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_MUST_BE_JOB);
        }

        Object compressionType;
        try {
            compressionType = ReflectionUtils.invokeStatic(clazz, "getOutputCompressionType", new Class<?>[] {
                    JobContext.class
            }, new Object[] {
                    ((JobWrap)arg0).getJob()
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw Utils.makeError(ctx, thisObj, e.getMessage());
        }

        return compressionType == null ? Undefined.instance : compressionType.toString();
    }

    /**
     * Java wrapper for {@link SequenceFileOutputFormat#setOutputCompressionType(Job, SequenceFile.CompressionType)}.
     *
     * @param clazz the class to invoke the method of
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     */
    public static void setOutputCompressionType(final Class<?> clazz, final Context ctx, final Scriptable thisObj,
                                                final Object[] args) {
        validateClass(clazz, ctx, thisObj);

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
        }

        SequenceFile.CompressionType compressionType;

        try {
            compressionType = SequenceFile.CompressionType.valueOf(arg1.toString());
        } catch (IllegalArgumentException e) {
            throw Utils.makeError(ctx, thisObj, arg1.toString() + " is not a valid "
                    + "org.apache.hadoop.io.SequenceFile.CompressionType");
        }

        try {
            ReflectionUtils.invokeStatic(clazz, "setOutputCompressionType", new Class<?>[] {
                    Job.class, SequenceFile.CompressionType.class
            }, new Object[] {
                    ((JobWrap)arg0).getJob(), compressionType
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw Utils.makeError(ctx, thisObj, e.getMessage());
        }
    }

    /**
     * Validate whether class is {@link org.apache.hadoop.mapreduce.lib.output.FileOutputFormat} or a subclass.
     *
     * @param clazz the class to validate
     * @param ctx the context to throw the error in if class is invalid
     * @param scope the scope to throw the error in if class is invalid
     */
    public static void validateClass(final Class<?> clazz, final Context ctx, final Scriptable scope) {
        if (!ReflectionUtils.isClassOrSubclass(SequenceFileOutputFormat.class, clazz)) {
            throw Utils.makeError(ctx, scope,
                                  LembosMessages.makeInvalidClassErrorMessage(SequenceFileOutputFormat.class, clazz));
        }
    }

}
