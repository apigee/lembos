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
import org.apache.hadoop.fs.PathFilter;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Undefined;
import org.mozilla.javascript.annotations.JSStaticFunction;

import java.net.URI;

/**
 * Helper for invoking {@link FileInputFormat} and its subclasses methods.
 */
public class FileInputFormatHelper {

    /**
     * Constructor.
     */
    protected FileInputFormatHelper() { }

    /**
     * Java wrapper for {@link FileInputFormat#addInputPath(Job, Path)}.
     *
     * @param clazz the class to invoke the method of
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     */
    public static void addInputPath(final Class<?> clazz, final Context ctx, final Scriptable thisObj,
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
            ReflectionUtils.invokeStatic(clazz, "addInputPath", new Class<?>[] {
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
     * Java wrapper for {@link FileInputFormat#addInputPaths(Job, String)}.
     *
     * @param clazz the class to invoke the method of
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     */
    public static void addInputPaths(final Class<?> clazz, final Context ctx, final Scriptable thisObj,
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
            ReflectionUtils.invokeStatic(clazz, "addInputPaths", new Class<?>[] {
                    Job.class, String.class
            }, new Object[] {
                    ((JobWrap)arg0).getJob(), arg1.toString()
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw Utils.makeError(ctx, thisObj, e.getMessage());
        }
    }

    /**
     * Java wrapper for {@link FileInputFormat#getInputPathFilter(JobContext)}.
     *
     * @param clazz the class to invoke the method of
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     *
     * @return class name for the input path filter or undefined
     */
    public static Object getInputPathFilter(final Class<?> clazz, final Context ctx, final Scriptable thisObj,
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

        Object pathFilter;

        try {
            pathFilter = ReflectionUtils.invokeStatic(clazz, "getInputPathFilter", new Class<?>[] {
                    JobContext.class
            }, new Object[] {
                    ((JobWrap)arg0).getJob()
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw Utils.makeError(ctx, thisObj, e.getMessage());
        }

        return pathFilter == null ? Undefined.instance : pathFilter.getClass().getCanonicalName();
    }

    /**
     * Java wrapper for {@link FileInputFormat#getInputPaths(JobContext)}.
     *
     * @param clazz the class to invoke the method of
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     *
     * @return array of input paths
     */
    public static Object getInputPaths(final Class<?> clazz, final Context ctx, final Scriptable thisObj,
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

        Path[] inputPaths;

        try {
            inputPaths = (Path[])ReflectionUtils.invokeStatic(clazz, "getInputPaths", new Class<?>[] {
                    JobContext.class
            }, new Object[] {
                    ((JobWrap)arg0).getJob()
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw Utils.makeError(ctx, thisObj, e.getMessage());
        }

        final String[] inputPathStrs = new String[inputPaths == null ? 0 : inputPaths.length];

        if (inputPaths != null) {
            for (int i = 0; i < inputPaths.length; i++) {
                inputPathStrs[i] = inputPaths[i].toString();
            }
        }

        return JavaScriptUtils.asArray(thisObj, inputPathStrs);
    }

    /**
     * Java wrapper for {@link FileInputFormat#getMaxSplitSize(JobContext)}.
     *
     * @param clazz the class to invoke the method of
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     *
     * @return the max split size
     */
    public static Object getMaxSplitSize(final Class<?> clazz, final Context ctx, final Scriptable thisObj,
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
            return ReflectionUtils.invokeStatic(clazz, "getMaxSplitSize", new Class<?>[] {
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
     * Java wrapper for {@link FileInputFormat#getMinSplitSize(JobContext)}.
     *
     * @param clazz the class to invoke the method of
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     *
     * @return the max split size
     */
    public static Object getMinSplitSize(final Class<?> clazz, final Context ctx, final Scriptable thisObj,
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
            return ReflectionUtils.invokeStatic(clazz, "getMinSplitSize", new Class<?>[] {
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
     * Wraps {@link FileInputFormat#setInputPathFilter(Job, Class)}.
     *
     * @param clazz the class to invoke the method of
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     */
    public static void setInputPathFilter(final Class<?> clazz, final Context ctx, final Scriptable thisObj,
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
        Class pathFilterClass;

        try {
            pathFilterClass = Class.forName(className);

            if (!PathFilter.class.isAssignableFrom(pathFilterClass)) {
                throw Utils.makeError(ctx, thisObj, LembosMessages.makeInvalidClassErrorMessage(PathFilter.class,
                                                                                                pathFilterClass));
            }
        } catch (ClassNotFoundException e) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.CLASS_NOT_FOUND);
        }

        try {
            ReflectionUtils.invokeStatic(clazz, "setInputPathFilter", new Class<?>[] {
                    Job.class, Class.class
            }, new Object[] {
                    ((JobWrap)arg0).getJob(), pathFilterClass
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw Utils.makeError(ctx, thisObj, e.getMessage());
        }
    }

    /**
     * Java wrapper for {@link FileInputFormat#setInputPaths(Job, Path...)} and
     * {@link FileInputFormat#setInputPaths(Job, String)}.
     *
     * @param clazz the class to invoke the method of
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     */
    public static void setInputPaths(final Class<?> clazz, final Context ctx, final Scriptable thisObj,
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

        final Job job = ((JobWrap)arg0).getJob();
        Class<?>[] methodTypes;
        Object[] methodArgs;

        if (arg1 instanceof NativeArray) {
            final NativeArray jsInputPaths = (NativeArray)arg1;
            final Path[] inputPaths = new Path[jsInputPaths.size()];

            for (int i = 0; i < jsInputPaths.size(); i++) {
                inputPaths[i] = new Path(URI.create(jsInputPaths.get(i).toString()));
            }

            methodTypes = new Class<?>[] {
                    Job.class, Path[].class
            };
            methodArgs = new Object[] {
                    job, inputPaths
            };
        } else {
            methodTypes = new Class<?>[] {
                    Job.class, String.class
            };
            methodArgs = new Object[] {
                    job, arg1.toString()
            };
        }

        try {
            ReflectionUtils.invokeStatic(clazz, "setInputPaths", methodTypes, methodArgs);
        } catch (Exception e) {
            e.printStackTrace();
            throw Utils.makeError(ctx, thisObj, e.getMessage());
        }
    }

    /**
     * Java wrapper for {@link FileInputFormat#setMaxInputSplitSize(Job, long)}.
     *
     * @param clazz the class to invoke the method of
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     */
    public static void setMaxInputSplitSize(final Class<?> clazz, final Context ctx, final Scriptable thisObj,
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
        } else if (!(arg1 instanceof Number)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_ARG_MUST_BE_NUM);
        }

        try {
            ReflectionUtils.invokeStatic(clazz, "setMaxInputSplitSize", new Class<?>[] {
                    Job.class, long.class
            }, new Object[] {
                    ((JobWrap)arg0).getJob(), JavaScriptUtils.fromNumber(arg1).longValue()
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw Utils.makeError(ctx, thisObj, e.getMessage());
        }
    }

    /**
     * Java wrapper for {@link FileInputFormat#setMinInputSplitSize(Job, long)}.
     *
     * @param clazz the class to invoke the method of
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     */
    @JSStaticFunction
    public static void setMinInputSplitSize(final Class<?> clazz, final Context ctx, final Scriptable thisObj,
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
        } else if (!(arg1 instanceof Number)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_ARG_MUST_BE_NUM);
        }

        try {
            ReflectionUtils.invokeStatic(clazz, "setMinInputSplitSize", new Class<?>[] {
                    Job.class, long.class
            }, new Object[] {
                    ((JobWrap)arg0).getJob(), JavaScriptUtils.fromNumber(arg1).longValue()
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw Utils.makeError(ctx, thisObj, e.getMessage());
        }
    }

    /**
     * Validate whether class is {@link FileInputFormat} or a subclass.
     *
     * @param clazz the class to validate
     * @param ctx the context to throw the error in if class is invalid
     * @param scope the scope to throw the error in if class is invalid
     */
    public static void validateClass(final Class<?> clazz, final Context ctx, final Scriptable scope) {
        if (!ReflectionUtils.isClassOrSubclass(FileInputFormat.class, clazz)) {
            throw Utils.makeError(ctx, scope, LembosMessages.makeInvalidClassErrorMessage(FileInputFormat.class,
                                                                                          clazz));
        }
    }

}
