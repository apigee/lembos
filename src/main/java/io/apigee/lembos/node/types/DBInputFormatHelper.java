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
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.db.DBInputFormat;
import org.apache.hadoop.mapreduce.lib.db.DBWritable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Undefined;

/**
 * Helper for invoking {@link DBInputFormat} and its subclasses methods.
 */
public class DBInputFormatHelper {

    /**
     * Constructor.
     */
    protected DBInputFormatHelper() { }

    /**
     * Java wrapper for {@link DBInputFormat#setInput(Job, Class, String, String)} and
     * {@link DBInputFormat#setInput(Job, Class, String, String, String, String...)}.
     *
     * @param clazz the class to invoke the method of
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     */
    public static void setInput(final Class<?> clazz, final Context ctx, final Scriptable thisObj, final Object[] args,
                                final Function func) {
        validateClass(clazz, ctx, thisObj);

        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;
        final Object arg1 = args.length >= 2 ? args[1] : Undefined.instance;
        final Object arg2 = args.length >= 3 ? args[2] : Undefined.instance;
        final Object arg3 = args.length >= 4 ? args[3] : Undefined.instance;

        if (args.length != 4 && args.length != 6) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FOUR_OR_SIX_ARGS_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!JavaScriptUtils.isDefined(arg1)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_REQUIRED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.THIRD_ARG_REQUIRED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FOURTH_ARG_REQUIRED);
        } else if (!(arg0 instanceof JobWrap)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_MUST_BE_JOB);
        }

        final String className = arg1.toString();
        final Class dbWritableClass;

        try {
            dbWritableClass = Class.forName(className);

            if (!DBWritable.class.isAssignableFrom(dbWritableClass)) {
                throw Utils.makeError(ctx, thisObj, LembosMessages.makeInvalidClassErrorMessage(DBWritable.class,
                                                                                                dbWritableClass));
            }
        } catch (ClassNotFoundException e) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.CLASS_NOT_FOUND);
        }

        final Job job = ((JobWrap)arg0).getJob();
        final Class<?>[] setInputArgTypes;
        final Object[] setInputArgs;

        if (args.length == 4) {
            setInputArgTypes = new Class<?>[] {
                    Job.class, Class.class, String.class, String.class
            };
            setInputArgs = new Object[] {
                    job,
                    (Class<? extends DBWritable>)dbWritableClass,
                    arg2.toString(),
                    arg3.toString()
            };
        } else {
            final Object arg4 = args[4];
            final Object arg5 = args[5];

            if (!JavaScriptUtils.isDefined(arg4)) {
                throw Utils.makeError(ctx, thisObj, LembosMessages.FIFTH_ARG_REQUIRED);
            } else if (!JavaScriptUtils.isDefined(arg5)) {
                throw Utils.makeError(ctx, thisObj, LembosMessages.SIXTH_ARG_REQUIRED);
            } else if (!(arg5 instanceof NativeArray)) {
                throw Utils.makeError(ctx, thisObj, LembosMessages.SIXTH_ARG_MUST_BE_ARRAY);
            }

            final NativeArray jsFieldNames = (NativeArray)arg5;
            final String[] fieldNames = new String[jsFieldNames.size()];

            for (int i = 0; i < jsFieldNames.size(); i++) {
                fieldNames[i] = jsFieldNames.get(i).toString();
            }

            setInputArgTypes = new Class<?>[] {
                    Job.class, Class.class, String.class, String.class, String.class, String[].class
            };
            setInputArgs = new Object[] {
                    job,
                    (Class<? extends DBWritable>)dbWritableClass,
                    arg2.toString(),
                    arg3.toString(),
                    arg4.toString(),
                    fieldNames
            };
        }

        try {
            ReflectionUtils.invokeStatic(clazz, "setInput", setInputArgTypes, setInputArgs);
        } catch (Exception e) {
            e.printStackTrace();
            throw Utils.makeError(ctx, thisObj, e.getMessage());
        }
    }

    /**
     * Validate whether class is {@link DBInputFormat} or a subclass.
     *
     * @param clazz the class to validate
     * @param ctx the context to throw the error in if class is invalid
     * @param scope the scope to throw the error in if class is invalid
     */
    public static void validateClass(final Class<?> clazz, final Context ctx, final Scriptable scope) {
        if (!ReflectionUtils.isClassOrSubclass(DBInputFormat.class, clazz)) {
            throw Utils.makeError(ctx, scope, LembosMessages.makeInvalidClassErrorMessage(DBInputFormat.class,
                                                                                          clazz));
        }
    }

}
