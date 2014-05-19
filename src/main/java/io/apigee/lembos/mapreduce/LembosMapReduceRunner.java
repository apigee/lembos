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

import io.apigee.lembos.node.types.JobWrap;
import io.apigee.lembos.utils.ConversionUtils;
import io.apigee.lembos.utils.JavaScriptUtils;
import io.apigee.lembos.utils.RunnerUtils;
import io.apigee.trireme.core.NodeException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.mozilla.javascript.Scriptable;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Implementation of {@link Tool} to allow running a Node.js MapReduce job from the CLI.
 */
public class LembosMapReduceRunner implements Tool {

    private Configuration conf;
    private LembosMapReduceEnvironment mrEnv;
    private Scriptable jobWrapper;

    /**
     * CLI entry point.
     *
     * @param args the command line arguments
     *
     * @throws Exception if anything goes wrong
     */
    public static void main(final String[] args) throws Exception {
        final List<String> argsList = Arrays.asList(args);

        // Display help output when requested
        if (argsList.contains("/?") || argsList.contains("--help") || argsList.contains("help")) {
            ToolRunner.printGenericCommandUsage(System.out);
            System.exit(0);
        }

        // Call run the job
        System.exit(ToolRunner.run(new Configuration(), new LembosMapReduceRunner(), args));
    }

    /**
     * Returns a properly configured, ready to run Hadoop {@link Job}.
     *
     * @param args the command line arguments as supported by {@link GenericOptionsParser}
     *
     * @return the configured job
     *
     * @throws IOException if there is a problem creating the job
     * @throws ExecutionException if there is an issue running the Node.js module
     * @throws InterruptedException if the execution of the Node.js module gets interrupted
     * @throws NodeException if there is an issue with the Node.js module
     */
    public Job initJob(final String[] args)
            throws ExecutionException, InterruptedException, IOException, NodeException {
        final GenericOptionsParser gop = new GenericOptionsParser(args);

        // If ran from ToolRunner, conf should already be set but if not, set it manually
        if (conf == null) {
            setConf(gop.getConfiguration());
        }

        // Load the Hadoop FS URL handler
        RunnerUtils.loadFsUrlStreamHandler(getConf());

        // Persist the non-Runner CLI arguments
        conf.setStrings(LembosConstants.MR_MODULE_ARGS, gop.getRemainingArgs());

        // Package the Node.js module and prepare it to be submitted with the Job
        RunnerUtils.prepareModuleForJob(conf);

        // Add "-libjars" to the current ClassLoader if necessary
        RunnerUtils.addLibJarsToClassLoader(conf);

        // Create Node.js environment for local use
        mrEnv = LembosMapReduceEnvironment.fromConf(conf);

        if (JavaScriptUtils.isDefined(mrEnv.getConfiguration())) {
            for (final Map.Entry<Object, Object> propertyEntry : mrEnv.getConfiguration().entrySet()) {
                final String key = propertyEntry.getKey().toString();
                final Writable value = ConversionUtils.jsToWritable(propertyEntry.getValue(), mrEnv.getModule());

                // Do not set these as we'll be setting them later from values we were passed from the CLI
                if (key.equals(LembosConstants.MR_MODULE_NAME)) {
                    continue;
                }

                if (value instanceof BooleanWritable) {
                    conf.setBoolean(key, ((BooleanWritable)value).get());
                } else if (value instanceof DoubleWritable || value instanceof FloatWritable) {
                    conf.setFloat(key, Float.valueOf(value.toString()));
                } else if (value instanceof IntWritable) {
                    conf.setInt(key, ((IntWritable)value).get());
                } else if (value instanceof LongWritable) {
                    conf.setLong(key, ((LongWritable)value).get());
                } else if (value instanceof Text) {
                    conf.set(key, value.toString());
                } else {
                    System.err.println("Cannot convert JavaScript (" + value.getClass().getName()
                                               + ") to Configuration, using String");
                    conf.set(key, value.toString());
                }
            }
        }

        // Create Job
        final String jobName = "LembosMapReduceJob-" + mrEnv.getModuleName();
        final Job job = new Job(conf, jobName);

        jobWrapper = JobWrap.getInstance(mrEnv.getRuntime(), job);

        if (JavaScriptUtils.isDefined(mrEnv.getJobSetupFunction())) {
            mrEnv.callFunctionSync(mrEnv.getJobSetupFunction(), new Object[] {
                    jobWrapper
            });
        }

        // Always set the mapper
        job.setMapperClass(LembosMapper.class);

        // Conditionally set the combiner
        if (JavaScriptUtils.isDefined(mrEnv.getCombineFunction())) {
            job.setCombinerClass(LembosCombiner.class);
        }

        // Conditionally set the group comparator
        if (JavaScriptUtils.isDefined(mrEnv.getGroupFunction())) {
            job.setGroupingComparatorClass(LembosGroupComparator.class);
        }

        // Conditionally set the partitioner
        if (JavaScriptUtils.isDefined(mrEnv.getPartitionFunction())) {
            job.setPartitionerClass(LembosPartitioner.class);
        }

        // Conditionally set the reducer
        if (JavaScriptUtils.isDefined(mrEnv.getReduceFunction())) {
            job.setReducerClass(LembosReducer.class);
        } else {
            job.setNumReduceTasks(0);
        }

        // Conditionally set the sort comparator
        if (JavaScriptUtils.isDefined(mrEnv.getSortFunction())) {
            job.setSortComparatorClass(LembosSortComparator.class);
        }

        // This could potentially be unsafe but for testing, we need to set this based on the path to the built JAR
        if (job.getJar() == null) {
            job.setJarByClass(LembosMapReduceRunner.class);
        }

        // MapReduce configuration reference:
        //
        // http://hadoop.apache.org/docs/stable/hadoop-mapreduce-client/hadoop-mapreduce-client-core/mapred-default.xml
        // org.apache.hadoop.mapreduce.MRConfig
        // org.apache.hadoop.mapreduce.MRJobConfig

        return job;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int run(final String[] args) throws Exception {
        final Job job = initJob(args);
        // Should we allow you to configure the verbosity?  It's on for now to allow for better debugging.
        final boolean result = job.waitForCompletion(true);

        if (JavaScriptUtils.isDefined(mrEnv.getJobCleanupFunction())) {
            mrEnv.callFunctionSync(mrEnv.getJobCleanupFunction(), new Object[] {
                    jobWrapper
            });
        }

        return result ? 0 : 1;
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
