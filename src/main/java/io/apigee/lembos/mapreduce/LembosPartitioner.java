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
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Partitioner;
import org.mozilla.javascript.Scriptable;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Extension of {@link Partitioner} that allows a Node.js MapReduce job author to export a <strong>partition</strong>
 * function to implement the partition functionality for their job.
 */
public class LembosPartitioner extends Partitioner<WritableComparable<?>, Writable> implements Closeable, Configurable {

    private Configuration conf;
    private LembosMapReduceEnvironment env;
    private Scriptable jsConf;

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPartition(final WritableComparable<?> key, final Writable value, final int numPartitions) {
        if (conf == null) {
            // Should never happen
            throw new RuntimeException("Hadoop configuration cannot be null");
        }

        if (env == null) {
            try {
                env = LembosMapReduceEnvironment.fromConf(conf);
                jsConf = ConfigurationWrap.getInstance(env.getRuntime(), conf);

                if (!JavaScriptUtils.isDefined(env.getPartitionFunction())) {
                    throw new RuntimeException("MapReduce function 'partition' is not defined");
                }

                if (JavaScriptUtils.isDefined(env.getPartitionSetupFunction())) {
                    env.callFunctionSync(env.getPartitionSetupFunction(), new Object[] {
                            jsConf
                    });
                }
            } catch (ExecutionException | IOException | InterruptedException | NodeException e) {
                throw new RuntimeException(e);
            }
        }

        final WritableComparable<?> jsPartition = ConversionUtils.jsToWritableComparable(
                env.callFunctionSync(env.getPartitionFunction(),
                                     new Object[] {
                                             ConversionUtils.writableComparableToJS(key, env.getModule()),
                                             ConversionUtils.writableToJS(value, env.getModule()),
                                             numPartitions
                                     }), env.getModule());

        if (!JavaScriptUtils.isDefined(jsPartition) || jsPartition instanceof NullWritable) {
            throw new RuntimeException("MapReduce function 'partition' cannot return null/undefined");
        }

        if (!(jsPartition instanceof IntWritable)) {
            throw new RuntimeException("MapReduce function 'partition' must return an integer");
        }

        // We'll let Hadoop handle any invalid responses
        return ((IntWritable)jsPartition).get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        if (env != null) {
            if (JavaScriptUtils.isDefined(env.getPartitionCleanupFunction())) {
                env.callFunctionSync(env.getPartitionCleanupFunction(), new Object[] {
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
