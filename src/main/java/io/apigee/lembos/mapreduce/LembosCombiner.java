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

package io.apigee.lembos.mapreduce;

import io.apigee.lembos.node.types.ReducerValuesIterableWrap;
import io.apigee.lembos.node.types.TaskInputOutputContextWrap;
import io.apigee.lembos.utils.ConversionUtils;
import io.apigee.lembos.utils.JavaScriptUtils;
import io.apigee.trireme.core.NodeException;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Reducer;
import org.mozilla.javascript.Function;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Extension of {@link Reducer} that allows a Node.js MapReduce job author to export a <strong>combine</strong>
 * function to implement the combine functionality for their job.
 */
public class LembosCombiner extends Reducer<WritableComparable<?>, Writable, WritableComparable<?>, Writable> {

    // TODO: Rewrite this to not be a copy/paste/refactor version of LembosReducer

    private Function combineFunction;
    private LembosMapReduceEnvironment env;
    private TaskInputOutputContextWrap ctxWrapper;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reduce(final WritableComparable<?> key, final Iterable<Writable> values, final Context context)
            throws IOException, InterruptedException {

        env.callFunctionSync(combineFunction, new Object[] {
                ConversionUtils.writableComparableToJS(key, env.getModule()),
                ReducerValuesIterableWrap.getInstance(env.getModule(), values),
                ctxWrapper
        });

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setup(final Context context) throws IOException, InterruptedException {
        super.setup(context);

        try {
            env = LembosMapReduceEnvironment.fromConf(context.getConfiguration());
            combineFunction = env.getCombineFunction();

            if (!JavaScriptUtils.isDefined(combineFunction)) {
                throw new RuntimeException("MapReduce function 'combine' is not defined");
            }

            // Create the context wrapper
            ctxWrapper = TaskInputOutputContextWrap.getInstance(env.getModule(), env.getRuntime(), context);

            // Call the setup if available
            if (JavaScriptUtils.isDefined(env.getCombineSetupFunction())) {
                env.callFunctionSync(env.getCombineSetupFunction(), new Object[] {
                        ctxWrapper
                });
            }
        } catch (ExecutionException | NodeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void cleanup(final Context context) throws IOException, InterruptedException {
        super.cleanup(context);

        if (JavaScriptUtils.isDefined(env.getCombineCleanupFunction())) {
            env.callFunctionSync(env.getCombineCleanupFunction(), new Object[] {
                    ctxWrapper
            });
        }

        env.cleanup();
    }

    /**
     * @return the {@link LembosMapReduceEnvironment} used
     */
    public LembosMapReduceEnvironment getEnv() {
        return env;
    }

}
