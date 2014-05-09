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

import io.apigee.lembos.node.types.ConfigurationWrap;
import io.apigee.lembos.utils.ConversionUtils;
import io.apigee.lembos.utils.JavaScriptUtils;
import io.apigee.trireme.core.NodeException;
import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Extension of {@link WritableComparator} that allows a Node.js MapReduce job author to export a <strong>sort</strong>
 * function to implement the sort comparator functionality for their job.
 */
public class LembosSortComparator extends WritableComparator implements Configurable, Closeable {

    private Configuration conf;
    private Function sortFunction;
    private LembosMapReduceEnvironment env;
    private Scriptable jsConf;

    /**
     * Constructor.
     */
    public LembosSortComparator() {
        super(BytesWritable.class, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(final WritableComparable key1, final WritableComparable key2) {
        if (conf == null) {
            // Should never happen
            throw new RuntimeException("Hadoop configuration cannot be null");
        }

        if (!JavaScriptUtils.isDefined(sortFunction)) {
            try {
                env = LembosMapReduceEnvironment.fromConf(conf);
                jsConf = ConfigurationWrap.getInstance(env.getRuntime(), conf);
                sortFunction = env.getSortFunction();

                if (!JavaScriptUtils.isDefined(sortFunction)) {
                    throw new RuntimeException("MapReduce function 'sort' is not defined");
                }

                if (JavaScriptUtils.isDefined(env.getSortSetupFunction())) {
                    env.callFunctionSync(env.getSortSetupFunction(), new Object[] {
                            jsConf
                    });
                }
            } catch (ExecutionException | IOException | InterruptedException | NodeException e) {
                throw new RuntimeException(e);
            }
        }

        final WritableComparable<?> jsSort = ConversionUtils.jsToWritableComparable(
                env.callFunctionSync(sortFunction,
                                     new Object[] {
                                             ConversionUtils.writableComparableToJS(key1, env.getModule()),
                                             ConversionUtils.writableComparableToJS(key2, env.getModule())
                                     }), env.getModule());

        if (!JavaScriptUtils.isDefined(jsSort) || jsSort instanceof NullWritable) {
            throw new RuntimeException("MapReduce function 'sort' cannot return null/undefined");
        }

        if (!(jsSort instanceof IntWritable)) {
            throw new RuntimeException("MapReduce function 'sort' must return an integer");
        }

        // We'll let Hadoop handle any invalid responses
        return ((IntWritable)jsSort).get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        if (env != null) {
            if (JavaScriptUtils.isDefined(env.getSortCleanupFunction())) {
                env.callFunctionSync(env.getSortCleanupFunction(), new Object[] {
                        jsConf
                });
            }

            env.cleanup();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setConf(final Configuration conf) {
        this.conf = conf;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Configuration getConf() {
        return conf;
    }

}
