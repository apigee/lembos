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
import org.apache.hadoop.mapreduce.lib.db.DBOutputFormat;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;
import org.mozilla.javascript.annotations.JSStaticFunction;

import java.io.IOException;

/**
 * Java implementation of the {@link DBOutputFormat} JavaScript object.
 *
 * <b>Note:</b> Only exposes the static methods
 */
public final class DBOutputFormatWrap extends ScriptableObject {

    private static final long serialVersionUID = -7664839016790669948L;
    public static final String CLASS_NAME = "DBOutputFormat";

    /* JavaScript Methods */

    /**
     * Java wrapper for {@link DBOutputFormat#setOutput(org.apache.hadoop.mapreduce.Job, String, int)} and
     * {@link DBOutputFormat#setOutput(org.apache.hadoop.mapreduce.Job, String, String...)}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     */
    @JSStaticFunction
    public static void setOutput(final Context ctx, final Scriptable thisObj, final Object[] args,
                                 final Function func) {
        final Object arg0 = args.length >= 1 ? args[0] : Undefined.instance;
        final Object arg1 = args.length >= 2 ? args[1] : Undefined.instance;
        final Object arg2 = args.length >= 3 ? args[2] : Undefined.instance;

        if (args.length != 3) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.THREE_ARGS_EXPECTED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
        } else if (!JavaScriptUtils.isDefined(arg1)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.SECOND_ARG_REQUIRED);
        } else if (!JavaScriptUtils.isDefined(arg0)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.THIRD_ARG_REQUIRED);
        } else if (!(arg0 instanceof JobWrap)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_MUST_BE_JOB);
        } else if (!(arg2 instanceof NativeArray) && !(arg2 instanceof Number)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.THIRD_ARG_MUST_BE_ARR_OR_NUM);
        }

        try {
            if (arg2 instanceof NativeArray) {
                final NativeArray jsFieldNames = (NativeArray)arg2;
                final String[] fieldNames = new String[jsFieldNames.size()];

                for (int i = 0; i < jsFieldNames.size(); i++) {
                    fieldNames[i] = jsFieldNames.get(i).toString();
                }

                DBOutputFormat.setOutput(((JobWrap)arg0).getJob(), arg1.toString(), fieldNames);
            } else {
                DBOutputFormat.setOutput(((JobWrap)arg0).getJob(), arg1.toString(),
                                         JavaScriptUtils.fromNumber(arg2).intValue());

            }
        } catch (IOException e) {
            throw Utils.makeError(ctx, thisObj, e.getMessage());
        }
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
