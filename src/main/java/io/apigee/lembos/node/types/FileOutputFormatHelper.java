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
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.TaskInputOutputContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Undefined;

import java.net.URI;

/**
 * Helper for invoking {@link FileOutputFormat} and its subclasses methods.
 *
 * <b>Note:</b> Only exposes the static methods
 *
 * <b>Unsupported APIs:</b>
 * <ul>
 *   <li>{@link FileOutputFormat#getUniqueFile(org.apache.hadoop.mapreduce.TaskAttemptContext, String, String)}</li>
 * </ul>
 */
public class FileOutputFormatHelper {

    /**
     * Constructor.
     */
    protected FileOutputFormatHelper() { }

    /**
     * Java wrapper for {@link FileOutputFormat#getCompressOutput(JobContext)}.
     *
     * @param clazz the class to invoke the method of
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     *
     * @return whether or not the output is compressed
     */
    public static Object getCompressOutput(final Class<?> clazz, final Context ctx, final Scriptable thisObj,
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

        try {
            return ReflectionUtils.invokeStatic(clazz, "getCompressOutput", new Class<?>[] {
                    JobContext.class
            }, new Object[] {
                    ((JobWrap)arg0).getJob()
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw Utils.makeError(ctx, thisObj, e.getMessage());
        }
    }

    /**
     * Java wrapper for {@link FileOutputFormat#getOutputCompressorClass(JobContext, Class)}.
     *
     * @param clazz the class to invoke the method of
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     *
     * @return the output compressor class
     */
    public static Object getOutputCompressorClass(final Class<?> clazz, final Context ctx, final Scriptable thisObj,
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

        final String className = arg1.toString();
        Class compressorClass;

        try {
            compressorClass = Class.forName(className);

            if (!CompressionCodec.class.isAssignableFrom(compressorClass)) {
                throw Utils.makeError(ctx, thisObj, LembosMessages.makeInvalidClassErrorMessage(CompressionCodec.class,
                                                                                                compressorClass));
            }
        } catch (ClassNotFoundException e) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.CLASS_NOT_FOUND);
        }

        try {
            return ((Class<? extends CompressionCodec>)ReflectionUtils.invokeStatic(clazz, "getOutputCompressorClass",
                                                                                    new Class<?>[] {
                                                                                            JobContext.class,
                                                                                            Class.class
                                                                                    },
                                                                                    new Object[] {
                                                                                            ((JobWrap)arg0).getJob(),
                                                                                            compressorClass
                                                                                    })).getCanonicalName();
        } catch (Exception e) {
            e.printStackTrace();
            throw Utils.makeError(ctx, thisObj, e.getMessage());
        }
    }

    /**
     * Java wrapper for {@link FileOutputFormat#getOutputPath(JobContext)}.
     *
     * @param clazz the class to invoke the method of
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     *
     * @return the output path
     */
    public static Object getOutputPath(final Class<?> clazz, final Context ctx, final Scriptable thisObj,
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

        Object outputPath;

        try {
            outputPath =  ReflectionUtils.invokeStatic(clazz, "getOutputPath", new Class<?>[] {
                    JobContext.class
            }, new Object[] {
                    ((JobWrap)arg0).getJob()
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw Utils.makeError(ctx, thisObj, e.getMessage());
        }

        return outputPath == null ? Undefined.instance : outputPath.toString();
    }

    /**
     * Java wrapper for {@link FileOutputFormat#getPathForWorkFile(TaskInputOutputContext, String, String)}.
     *
     * @param clazz the class to invoke the method of
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     *
     * @return the path for the work file
     */
    public static Object getPathForWorkFile(final Class<?> clazz, final Context ctx, final Scriptable thisObj,
                                            final Object[] args) {
        validateClass(clazz, ctx, thisObj);

        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;
        final Object arg1 = args.length >= 2 ? args[1] : Undefined.instance;
        final Object arg2 = args.length >= 3 ? args[2] : Undefined.instance;

        if (args.length < 3) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.THREE_ARGS_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!JavaScriptUtils.isDefined(arg1)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_REQUIRED);
        } else if (!JavaScriptUtils.isDefined(arg2)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.THIRD_ARG_REQUIRED);
        } else if (!(arg0 instanceof TaskInputOutputContextWrap)) {
            throw Utils.makeError(ctx, thisObj,
                                  LembosMessages.makeInvalidClassErrorMessage(TaskInputOutputContext.class,
                                                                              arg0.getClass()));
        }

        try {
            return ReflectionUtils.invokeStatic(clazz, "getPathForWorkFile", new Class<?>[] {
                    TaskInputOutputContext.class, String.class, String.class
            }, new Object[] {
                    ((TaskInputOutputContextWrap)arg0).getContext(), arg1.toString(), arg2.toString()
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw Utils.makeError(ctx, thisObj, e.getMessage());
        }
    }

    /**
     * Java wrapper for {@link FileOutputFormat#getWorkOutputPath(TaskInputOutputContext)}.
     *
     * @param clazz the class to invoke the method of
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     *
     * @return the path for the work output
     */
    public static Object getWorkOutputPath(final Class<?> clazz, final Context ctx, final Scriptable thisObj,
                                          final Object[] args) {
        validateClass(clazz, ctx, thisObj);

        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;

        if (args.length < 1) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!(arg0 instanceof TaskInputOutputContextWrap)) {
            throw Utils.makeError(ctx, thisObj,
                                  LembosMessages.makeInvalidClassErrorMessage(TaskInputOutputContext.class,
                                                                              arg0.getClass()));
        }

        try {
            return ReflectionUtils.invokeStatic(clazz, "getWorkOutputPath", new Class<?>[] {
                    TaskInputOutputContext.class
            }, new Object[] {
                    ((TaskInputOutputContextWrap)arg0).getContext()
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw Utils.makeError(ctx, thisObj, e.getMessage());
        }
    }

    /**
     * Java wrapper for {@link FileOutputFormat#setCompressOutput(Job, boolean)}.
     *
     * @param clazz the class to invoke the method of
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     */
    public static void setCompressOutput(final Class<?> clazz, final Context ctx, final Scriptable thisObj,
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
        } else if (!(arg1 instanceof Boolean)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_MUST_BE_BOOL);
        }

        try {
            ReflectionUtils.invokeStatic(clazz, "setCompressOutput", new Class<?>[] {
                    Job.class, boolean.class
            }, new Object[] {
                    ((JobWrap)arg0).getJob(), arg1
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw Utils.makeError(ctx, thisObj, e.getMessage());
        }
    }

    /**
     * Java wrapper for {@link FileOutputFormat#setOutputCompressorClass(Job, Class)}.
     *
     * @param clazz the class to invoke the method of
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     */
    public static void setOutputCompressorClass(final Class<?> clazz, final Context ctx, final Scriptable thisObj,
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

        final String className = arg1.toString();
        Class compressorClass;

        try {
            compressorClass = Class.forName(className);

            if (!CompressionCodec.class.isAssignableFrom(compressorClass)) {
                throw Utils.makeError(ctx, thisObj, LembosMessages.makeInvalidClassErrorMessage(CompressionCodec.class,
                                                                                                compressorClass));
            }
        } catch (ClassNotFoundException e) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.CLASS_NOT_FOUND);
        }

        try {
            ReflectionUtils.invokeStatic(clazz, "setOutputCompressorClass", new Class<?>[] {
                    Job.class, Class.class
            }, new Object[] {
                    ((JobWrap)arg0).getJob(), compressorClass
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw Utils.makeError(ctx, thisObj, e.getMessage());
        }
    }

    /**
     * Java wrapper for {@link FileOutputFormat#setOutputPath(Job, Path)}.
     *
     * @param clazz the class to invoke the method of
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     */
    public static void setOutputPath(final Class<?> clazz, final Context ctx, final Scriptable thisObj,
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

        try {
            ReflectionUtils.invokeStatic(clazz, "setOutputPath", new Class<?>[] {
                    Job.class, Path.class
            }, new Object[] {
                    ((JobWrap)arg0).getJob(), new Path(URI.create(arg1.toString()))
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw Utils.makeError(ctx, thisObj, e.getMessage());
        }
    }

    /**
     * Validate whether class is {@link FileOutputFormat} or a subclass.
     *
     * @param clazz the class to validate
     * @param ctx the context to throw the error in if class is invalid
     * @param scope the scope to throw the error in if class is invalid
     */
    public static void validateClass(final Class<?> clazz, final Context ctx, final Scriptable scope) {
        if (!ReflectionUtils.isClassOrSubclass(FileOutputFormat.class, clazz)) {
            throw Utils.makeError(ctx, scope, LembosMessages.makeInvalidClassErrorMessage(FileOutputFormat.class,
                                                                                          clazz));
        }
    }

}
