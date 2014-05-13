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
import io.apigee.lembos.node.modules.HadoopJob;
import io.apigee.lembos.utils.JavaScriptUtils;
import io.apigee.trireme.core.NodeModule;
import io.apigee.trireme.core.NodeRuntime;
import io.apigee.trireme.core.Utils;
import io.apigee.trireme.core.internal.ModuleRegistry;
import io.apigee.trireme.core.internal.ScriptRunner;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.RawComparator;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Counters;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobID;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;
import org.mozilla.javascript.annotations.JSConstructor;
import org.mozilla.javascript.annotations.JSFunction;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;

/**
 * Java implementation of the {@link Job} JavaScript object.
 *
 * <b>Unsupported APIs:</b>
 * <ul>
 *   <li>{@link Job#failTask(org.apache.hadoop.mapreduce.TaskAttemptID)}</li>
 *   <li>{@link Job#getCredentials()}</li>
 *   <li>{@link Job#getGroupingComparator()} => getGroupingComparatorClass() (Returns the class name)</li>
 *   <li>{@link Job#getJobID()} => getJobId() (Returns the string representation)</li>
 *   <li>{@link Job#getSortComparator()} => getSortComparatorClass() (Returns the class name)</li>
 *   <li>{@link Job#getTaskCompletionEvents(int)}</li>
 *   <li>{@link Job#killTask(org.apache.hadoop.mapreduce.TaskAttemptID)}</li>
 * </ul>
 */
public final class JobWrap extends ScriptableObject {

    private static final long serialVersionUID = 2169116870033741441L;
    public static final String CLASS_NAME = "Job";

    // These transient fields are to please Findbugs.  I realize why the errors come up but I don't see us ever
    // serializing this object.  It will always be constructed during the MapReduce component setup phase.

    private transient Job job;
    private transient NodeRuntime runtime;

    private Scriptable jsConf;

    /**
     * Creates an instance of {@link JobWrap}.  (Intended to be used only for constructing {@link JobWrap} objects via
     * Java calls.)
     *
     * @param runtime the Node.js runtime
     * @param job the Hadoop job to wrap
     *
     * @return the created job wrapper
     */
    public static JobWrap getInstance(final NodeRuntime runtime, final Job job) {
        final ModuleRegistry moduleRegistry = ((ScriptRunner)runtime).getRegistry();
        NodeModule rawModule = moduleRegistry.get(HadoopJob.MODULE_NAME);
        Context ctx = Context.getCurrentContext();

        if (ctx == null) {
            ctx = Context.enter();
        }

        if (rawModule == null) {
            runtime.require(HadoopJob.MODULE_NAME, ctx);

            rawModule = moduleRegistry.get(HadoopJob.MODULE_NAME);
        }

        if (rawModule == null) {
            throw new RuntimeException("Unable to load the " + HadoopJob.MODULE_NAME + " for internal use");
        }

        final HadoopJob module = (HadoopJob)rawModule;
        Scriptable exports = module.getExports();

        if (!JavaScriptUtils.isDefined(exports)) {
            try {
                exports = (Scriptable)((ScriptRunner)runtime).initializeModule(HadoopJob.MODULE_NAME,
                                                                               false, ctx,
                                                                               ((ScriptRunner)runtime)
                                                                                       .getScriptScope());
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                throw new RuntimeException("Unable to load the " + HadoopJob.MODULE_NAME
                                                   + " for internal use");
            }

        }

        final JobWrap wrapper = (JobWrap)ctx.newObject(exports, CLASS_NAME, new Object[] {
                ConfigurationWrap.getInstance(runtime, job.getConfiguration())
        });

        wrapper.job = job;
        wrapper.runtime = runtime;

        return wrapper;
    }

    /**
     * JavaScript constructor.
     *
     * @param ctx the JavaScript context
     * @param args the constructor arguments
     * @param ctorObj the constructor function
     * @param newExpr if a new expression caused the call
     *
     * @return the newly constructed time wrapper
     */
    @JSConstructor
    public static Object wrapConstructor(final Context ctx, final Object[] args, final Function ctorObj,
                                         final boolean newExpr) {
        Job job;
        ConfigurationWrap jsConf = null;

        try {
            if (args.length == 0 || !JavaScriptUtils.isDefined(args[0])) {
                job = new Job();
            } else if (args.length >= 1) {
                if (args[0] instanceof ConfigurationWrap) {
                    jsConf = (ConfigurationWrap)args[0];
                    job = new Job(jsConf.getConf());
                } else {
                    throw Utils.makeError(ctx, ctorObj, LembosMessages.FIRST_ARG_MUST_BE_CONF);
                }

                if (args.length >= 2 && JavaScriptUtils.isDefined(args[1])) {
                    job = new Job(jsConf.getConf(), args[1].toString());
                }
            } else {
                throw Utils.makeError(ctx, ctorObj, LembosMessages.ZERO_ONE_OR_TWO_ARGS_EXPECTED);
            }
        } catch (IOException e) {
            throw Utils.makeError(ctx, ctorObj, e.getMessage());
        }

        if (!JavaScriptUtils.isDefined(jsConf)) {
            jsConf = ConfigurationWrap.getInstance((ScriptRunner)ctx.getThreadLocal(ScriptRunner.RUNNER),
                                                   job.getConfiguration());
        }

        final JobWrap wrapper;

        if (newExpr) {
            wrapper = new JobWrap();
        } else {
            wrapper = (JobWrap)ctx.newObject(ctorObj, CLASS_NAME);
        }

        wrapper.job = job;
        wrapper.jsConf = jsConf;
        wrapper.runtime = (ScriptRunner)ctx.getThreadLocal(ScriptRunner.RUNNER);

        return wrapper;
    }

    /**
     * Wraps {@link Job#getCombinerClass()}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return the path to the job jar
     */
    @JSFunction
    public static Object getCombinerClass(final Context ctx, final Scriptable thisObj, final Object[] args,
                                          final Function func) {
        try {
            final Class<?> combinerClass = ((JobWrap)thisObj).job.getCombinerClass();

            return combinerClass == null ? Context.getUndefinedValue() : combinerClass.getCanonicalName();
        } catch (ClassNotFoundException e) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.CLASS_NOT_FOUND);
        }
    }

    /**
     * Wraps {@link Job#getCounters()}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return the counters wrapper
     */
    @JSFunction
    public static Object getCounters(final Context ctx, final Scriptable thisObj, final Object[] args,
                                     final Function func) {
        final JobWrap self = (JobWrap)thisObj;
        Counters counters;

        try {
            counters = self.job.getCounters();
        } catch (IOException e) {
            throw Utils.makeError(ctx, thisObj, e.getMessage());
        }

        CountersWrap countersWrap = null;

        if (counters != null) {
            countersWrap = CountersWrap.getInstance(self.runtime, counters);
        }

        return countersWrap == null ? Undefined.instance : countersWrap;
    }

    /**
     * Wraps {@link Job#getConfiguration()}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return the configuration wrapper
     */
    @JSFunction
    public static Object getConfiguration(final Context ctx, final Scriptable thisObj, final Object[] args,
                                          final Function func) {
        return ((JobWrap)thisObj).jsConf;
    }

    /**
     * Wraps {@link Job#getGroupingComparator()}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return the group comparator class
     */
    @JSFunction
    public static Object getGroupingComparatorClass(final Context ctx, final Scriptable thisObj, final Object[] args,
                                                    final Function func) {
        final RawComparator<?> groupingComparator = ((JobWrap)thisObj).job.getGroupingComparator();

        return groupingComparator == null
                ? Context.getUndefinedValue()
                : groupingComparator.getClass().getCanonicalName();
    }

    /**
     * Wraps {@link Job#getInputFormatClass()}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return the input format class
     */
    @JSFunction
    public static Object getInputFormatClass(final Context ctx, final Scriptable thisObj, final Object[] args,
                                             final Function func) {
        try {
            final Class<?> inputFormatClass = ((JobWrap)thisObj).job.getInputFormatClass();

            return inputFormatClass == null ? Context.getUndefinedValue() : inputFormatClass.getCanonicalName();
        } catch (ClassNotFoundException e) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.CLASS_NOT_FOUND);
        }
    }

    /**
     * Wraps {@link Job#getJar()}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return the path to the job jar
     */
    @JSFunction
    public static Object getJar(final Context ctx, final Scriptable thisObj, final Object[] args,
                                  final Function func) {
        final String jarPath = ((JobWrap)thisObj).job.getJar();

        return jarPath == null ? Context.getUndefinedValue() : jarPath;
    }

    /**
     * Wraps {@link Job#getJobID()}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return the job id
     */
    @JSFunction
    public static Object getJobId(final Context ctx, final Scriptable thisObj, final Object[] args,
                                  final Function func) {
        final JobID jobId = ((JobWrap)thisObj).job.getJobID();

        return jobId == null ? Context.getUndefinedValue() : jobId.toString();
    }

    /**
     * Wraps {@link Job#getJobName()}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return the job name
     */
    @JSFunction
    public static Object getJobName(final Context ctx, final Scriptable thisObj, final Object[] args,
                                  final Function func) {
        final String jobName = ((JobWrap)thisObj).job.getJobName();

        return jobName == null ? Context.getUndefinedValue() : jobName;
    }

    /**
     * Wraps {@link Job#getMapOutputKeyClass()}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return the map output key class
     */
    @JSFunction
    public static Object getMapOutputKeyClass(final Context ctx, final Scriptable thisObj, final Object[] args,
                                          final Function func) {
        final Class<?> mapOutputKeyClass = ((JobWrap)thisObj).job.getMapOutputKeyClass();

        return mapOutputKeyClass == null ? Context.getUndefinedValue() : mapOutputKeyClass.getCanonicalName();
    }

    /**
     * Wraps {@link Job#getMapOutputValueClass()}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return the map output value class
     */
    @JSFunction
    public static Object getMapOutputValueClass(final Context ctx, final Scriptable thisObj, final Object[] args,
                                                final Function func) {
        final Class<?> mapOutputValueClass = ((JobWrap)thisObj).job.getMapOutputValueClass();

        return mapOutputValueClass == null ? Context.getUndefinedValue() : mapOutputValueClass.getCanonicalName();
    }

    /**
     * Wraps {@link Job#getMapperClass()}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return the mapper class
     */
    @JSFunction
    public static Object getMapperClass(final Context ctx, final Scriptable thisObj, final Object[] args,
                                        final Function func) {
        try {
            final Class<?> mapperClass = ((JobWrap)thisObj).job.getMapperClass();

            return mapperClass == null ? Context.getUndefinedValue() : mapperClass.getCanonicalName();
        } catch (ClassNotFoundException e) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.CLASS_NOT_FOUND);
        }
    }

    /**
     * Wraps {@link Job#getNumReduceTasks()}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return the number of reduce tasks
     */
    @JSFunction
    public static Object getNumReduceTasks(final Context ctx, final Scriptable thisObj, final Object[] args,
                                           final Function func) {
        return ((JobWrap)thisObj).job.getNumReduceTasks();
    }

    /**
     * Wraps {@link Job#getOutputFormatClass()}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return the input format class
     */
    @JSFunction
    public static Object getOutputFormatClass(final Context ctx, final Scriptable thisObj, final Object[] args,
                                              final Function func) {
        try {
            final Class<?> outputFormatClass = ((JobWrap)thisObj).job.getOutputFormatClass();

            return outputFormatClass == null ? Context.getUndefinedValue() : outputFormatClass.getCanonicalName();
        } catch (ClassNotFoundException e) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.CLASS_NOT_FOUND);
        }
    }

    /**
     * Wraps {@link Job#getOutputKeyClass()}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return the output key class
     */
    @JSFunction
    public static Object getOutputKeyClass(final Context ctx, final Scriptable thisObj, final Object[] args,
                                           final Function func) {
        final Class<?> outputKeyClass = ((JobWrap)thisObj).job.getOutputKeyClass();

        return outputKeyClass == null ? Context.getUndefinedValue() : outputKeyClass.getCanonicalName();
    }

    /**
     * Wraps {@link Job#getOutputValueClass()}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return the output value class
     */
    @JSFunction
    public static Object getOutputValueClass(final Context ctx, final Scriptable thisObj, final Object[] args,
                                             final Function func) {
        final Class<?> outputValueClass = ((JobWrap)thisObj).job.getOutputValueClass();

        return outputValueClass == null ? Context.getUndefinedValue() : outputValueClass.getCanonicalName();
    }

    /**
     * Wraps {@link Job#getPartitionerClass()}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return the partitioner class
     */
    @JSFunction
    public static Object getPartitionerClass(final Context ctx, final Scriptable thisObj, final Object[] args,
                                             final Function func) {
        try {
            final Class<?> partitionerClass = ((JobWrap)thisObj).job.getPartitionerClass();

            return partitionerClass == null ? Context.getUndefinedValue() : partitionerClass.getCanonicalName();
        } catch (ClassNotFoundException e) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.CLASS_NOT_FOUND);
        }
    }

    /**
     * Wraps {@link Job#getReducerClass()}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return the reducer class
     */
    @JSFunction
    public static Object getReducerClass(final Context ctx, final Scriptable thisObj, final Object[] args,
                                         final Function func) {
        try {
            final Class<?> reducerClass = ((JobWrap)thisObj).job.getReducerClass();

            return reducerClass == null ? Context.getUndefinedValue() : reducerClass.getCanonicalName();
        } catch (ClassNotFoundException e) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.CLASS_NOT_FOUND);
        }
    }

    /**
     * Wraps {@link Job#getSortComparator()}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return the sort comparator class
     */
    @JSFunction
    public static Object getSortComparatorClass(final Context ctx, final Scriptable thisObj, final Object[] args,
                                                final Function func) {
        final RawComparator<?> sortComparator = ((JobWrap)thisObj).job.getSortComparator();

        return sortComparator == null
                ? Context.getUndefinedValue()
                : sortComparator.getClass().getCanonicalName();
    }

    /**
     * Wraps {@link Job#getTrackingURL()}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return the tracking url
     */
    @JSFunction
    public static Object getTrackingURL(final Context ctx, final Scriptable thisObj, final Object[] args,
                                        final Function func) {
        final String trackingUrl = ((JobWrap)thisObj).job.getTrackingURL();

        return trackingUrl == null ? Context.getUndefinedValue() : trackingUrl;
    }

    /**
     * Wraps {@link Job#getWorkingDirectory()}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return the working directory
     */
    @JSFunction
    public static Object getWorkingDirectory(final Context ctx, final Scriptable thisObj, final Object[] args,
                                             final Function func) {
        try {
            final Path workingDirectory = ((JobWrap)thisObj).job.getWorkingDirectory();

            return workingDirectory == null ? Context.getUndefinedValue() : workingDirectory.toUri().toString();
        } catch (IOException e) {
            throw Utils.makeError(ctx, thisObj, e.getMessage());
        }
    }

    /**
     * Wraps {@link Job#isComplete()}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return whether or not the job is complete
     */
    @JSFunction
    public static Object isComplete(final Context ctx, final Scriptable thisObj, final Object[] args,
                                    final Function func) {
        try {
            return ((JobWrap)thisObj).job.isComplete();
        } catch (IOException e) {
            throw Utils.makeError(ctx, thisObj, e.getMessage());
        }
    }

    /**
     * Wraps {@link Job#isSuccessful()}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return whether or not the job is successful
     */
    @JSFunction
    public static Object isSuccessful(final Context ctx, final Scriptable thisObj, final Object[] args,
                                      final Function func) {
        try {
            return ((JobWrap)thisObj).job.isSuccessful();
        } catch (IOException e) {
            throw Utils.makeError(ctx, thisObj, e.getMessage());
        }
    }

    /**
     * Wraps {@link Job#killJob()}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     */
    @JSFunction
    public static void killJob(final Context ctx, final Scriptable thisObj, final Object[] args, final Function func) {
        try {
            ((JobWrap)thisObj).job.killJob();
        } catch (IOException e) {
            throw Utils.makeError(ctx, thisObj, e.getMessage());
        }
    }

    /**
     * Wraps {@link Job#mapProgress()}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return the map progress
     */
    @JSFunction
    public static Object mapProgress(final Context ctx, final Scriptable thisObj, final Object[] args,
                                     final Function func) {
        try {
            // Converted to double because Float is not something supported by JavaScript
            return Double.valueOf(Float.toString(((JobWrap)thisObj).job.mapProgress()));
        } catch (IOException e) {
            throw Utils.makeError(ctx, thisObj, e.getMessage());
        }
    }

    /**
     * Wraps {@link Job#reduceProgress()}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return the reduce progress
     */
    @JSFunction
    public static Object reduceProgress(final Context ctx, final Scriptable thisObj, final Object[] args,
                                     final Function func) {
        try {
            // Converted to double because Float is not something supported by JavaScript
            return Double.valueOf(Float.toString(((JobWrap)thisObj).job.reduceProgress()));
        } catch (IOException e) {
            throw Utils.makeError(ctx, thisObj, e.getMessage());
        }
    }

    /**
     * Wraps {@link Job#setCancelDelegationTokenUponJobCompletion(boolean)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return this
     */
    @JSFunction
    public static Object setCancelDelegationTokenUponJobCompletion(final Context ctx, final Scriptable thisObj,
                                                                   final Object[] args, final Function func) {
        if (args.length < 1) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        } else if (!(args[0] instanceof Boolean)) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_MUST_BE_BOOL);
        }

        ((JobWrap)thisObj).job.setCancelDelegationTokenUponJobCompletion(Boolean.valueOf(args[0].toString()));

        return thisObj;
    }

    /**
     * Wraps {@link Job#setCombinerClass(Class)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return this
     */
    @JSFunction
    public static Object setCombinerClass(final Context ctx, final Scriptable thisObj, final Object[] args,
                                          final Function func) {
        if (args.length == 1) {
            if (!JavaScriptUtils.isDefined(args[0])) {
                throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
            }
        } else {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        }

        final String className = args[0].toString();

        try {
            final Class combinerClass = Class.forName(className);

            if (!Reducer.class.isAssignableFrom(combinerClass)) {
                throw Utils.makeError(ctx, thisObj,
                                      LembosMessages.makeInvalidClassErrorMessage(Reducer.class, combinerClass));
            }

            ((JobWrap)thisObj).job.setCombinerClass((Class<? extends Reducer>)combinerClass);
        } catch (ClassNotFoundException e) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.CLASS_NOT_FOUND);
        }

        return thisObj;
    }

    /**
     * Wraps {@link Job#setGroupingComparatorClass(Class)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return this
     */
    @JSFunction
    public static Object setGroupingComparatorClass(final Context ctx, final Scriptable thisObj, final Object[] args,
                                                    final Function func) {
        if (args.length == 1) {
            if (!JavaScriptUtils.isDefined(args[0])) {
                throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
            }
        } else {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        }

        final String className = args[0].toString();

        try {
            final Class groupingComparatorClass = Class.forName(className);

            if (!RawComparator.class.isAssignableFrom(groupingComparatorClass)) {
                throw Utils.makeError(ctx, thisObj,
                                      LembosMessages.makeInvalidClassErrorMessage(RawComparator.class,
                                                                                  groupingComparatorClass));
            }

            ((JobWrap)thisObj).job.setGroupingComparatorClass((Class<? extends RawComparator>)groupingComparatorClass);
        } catch (ClassNotFoundException e) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.CLASS_NOT_FOUND);
        }

        return thisObj;
    }

    /**
     * Wraps {@link Job#setInputFormatClass(Class)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return this
     */
    @JSFunction
    public static Object setInputFormatClass(final Context ctx, final Scriptable thisObj, final Object[] args,
                                             final Function func) {
        if (args.length == 1) {
            if (!JavaScriptUtils.isDefined(args[0])) {
                throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
            }
        } else {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        }

        final String className = args[0].toString();

        try {
            final Class inputFormatClass = Class.forName(className);

            if (!InputFormat.class.isAssignableFrom(inputFormatClass)) {
                throw Utils.makeError(ctx, thisObj,
                                      LembosMessages.makeInvalidClassErrorMessage(InputFormat.class,
                                                                                  inputFormatClass));
            }

            ((JobWrap)thisObj).job.setInputFormatClass((Class<? extends InputFormat>)inputFormatClass);
        } catch (ClassNotFoundException e) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.CLASS_NOT_FOUND);
        }

        return thisObj;
    }

    /**
     * Wraps {@link Job#setJarByClass(Class)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return this
     */
    @JSFunction
    public static Object setJarByClass(final Context ctx, final Scriptable thisObj, final Object[] args,
                                       final Function func) {
        if (args.length == 1) {
            if (!JavaScriptUtils.isDefined(args[0])) {
                throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
            }
        } else {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        }

        final String className = args[0].toString();

        try {
            ((JobWrap)thisObj).job.setJarByClass(Class.forName(className));
        } catch (ClassNotFoundException e) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.CLASS_NOT_FOUND);
        }

        return thisObj;
    }

    /**
     * Wraps {@link Job#setJobName(String)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return this
     */
    @JSFunction
    public static Object setJobName(final Context ctx, final Scriptable thisObj, final Object[] args,
                                    final Function func) {
        if (args.length == 1) {
            if (!JavaScriptUtils.isDefined(args[0])) {
                throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
            }
        } else {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        }

        ((JobWrap)thisObj).job.setJobName(args[0].toString());

        return thisObj;
    }

    /**
     * Wraps {@link Job#setMapOutputKeyClass(Class)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return this
     */
    @JSFunction
    public static Object setMapOutputKeyClass(final Context ctx, final Scriptable thisObj, final Object[] args,
                                              final Function func) {
        if (args.length == 1) {
            if (!JavaScriptUtils.isDefined(args[0])) {
                throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
            }
        } else {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        }

        final String className = args[0].toString();

        try {
            final Class mapOutputKeyClass = Class.forName(className);

            if (!WritableComparable.class.isAssignableFrom(mapOutputKeyClass)) {
                throw Utils.makeError(ctx, thisObj,
                                      LembosMessages.makeInvalidClassErrorMessage(WritableComparable.class,
                                                                                  mapOutputKeyClass));
            }

            ((JobWrap)thisObj).job.setMapOutputKeyClass((Class<? extends WritableComparable>)mapOutputKeyClass);
        } catch (ClassNotFoundException e) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.CLASS_NOT_FOUND);
        }

        return thisObj;
    }

    /**
     * Wraps {@link Job#setMapOutputValueClass(Class)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return this
     */
    @JSFunction
    public static Object setMapOutputValueClass(final Context ctx, final Scriptable thisObj, final Object[] args,
                                                final Function func) {
        if (args.length == 1) {
            if (!JavaScriptUtils.isDefined(args[0])) {
                throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
            }
        } else {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        }

        final String className = args[0].toString();

        try {
            final Class mapOutputValueClass = Class.forName(className);

            if (!Writable.class.isAssignableFrom(mapOutputValueClass)) {
                throw Utils.makeError(ctx, thisObj,
                                      LembosMessages.makeInvalidClassErrorMessage(Writable.class,
                                                                                  mapOutputValueClass));
            }

            ((JobWrap)thisObj).job.setMapOutputValueClass((Class<? extends Writable>)mapOutputValueClass);
        } catch (ClassNotFoundException e) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.CLASS_NOT_FOUND);
        }

        return thisObj;
    }

    /**
     * Wraps {@link Job#setMapperClass(Class)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return this
     */
    @JSFunction
    public static Object setMapperClass(final Context ctx, final Scriptable thisObj, final Object[] args,
                                        final Function func) {
        if (args.length == 1) {
            if (!JavaScriptUtils.isDefined(args[0])) {
                throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
            }
        } else {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        }

        final String className = args[0].toString();

        try {
            final Class mapperClass = Class.forName(className);

            if (!Mapper.class.isAssignableFrom(mapperClass)) {
                throw Utils.makeError(ctx, thisObj,
                                      LembosMessages.makeInvalidClassErrorMessage(Mapper.class, mapperClass));
            }

            ((JobWrap)thisObj).job.setMapperClass((Class<? extends Mapper>)mapperClass);
        } catch (ClassNotFoundException e) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.CLASS_NOT_FOUND);
        }

        return thisObj;
    }

    /**
     * Wraps {@link Job#setMapSpeculativeExecution(boolean)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return this
     */
    @JSFunction
    public static Object setMapSpeculativeExecution(final Context ctx, final Scriptable thisObj,
                                                    final Object[] args, final Function func) {
        if (args.length == 1) {
            if (JavaScriptUtils.isDefined(args[0])) {
                if (!(args[0] instanceof Boolean)) {
                    throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_MUST_BE_BOOL);
                }
            } else {
                throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
            }
        } else {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        }

        ((JobWrap)thisObj).job.setMapSpeculativeExecution(Boolean.valueOf(args[0].toString()));

        return thisObj;
    }

    /**
     * Wraps {@link Job#setNumReduceTasks(int)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return this
     */
    @JSFunction
    public static Object setNumReduceTasks(final Context ctx, final Scriptable thisObj, final Object[] args,
                                          final Function func) {
        if (args.length == 1) {
            if (JavaScriptUtils.isDefined(args[0])) {
                if (!(args[0] instanceof Number)) {
                    throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_MUST_BE_NUM);
                }
            } else {
                throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
            }
        } else {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        }

        ((JobWrap)thisObj).job.setNumReduceTasks(JavaScriptUtils.fromNumber(args[0]).intValue());

        return thisObj;
    }

    /**
     * Wraps {@link Job#setOutputFormatClass(Class)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return this
     */
    @JSFunction
    public static Object setOutputFormatClass(final Context ctx, final Scriptable thisObj, final Object[] args,
                                              final Function func) {
        if (args.length == 1) {
            if (!JavaScriptUtils.isDefined(args[0])) {
                throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
            }
        } else {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        }

        final String className = args[0].toString();

        try {
            final Class outputFormatClass = Class.forName(className);

            if (!OutputFormat.class.isAssignableFrom(outputFormatClass)) {
                throw Utils.makeError(ctx, thisObj, LembosMessages.makeInvalidClassErrorMessage(OutputFormat.class,
                                                                                                outputFormatClass));
            }

            ((JobWrap)thisObj).job.setOutputFormatClass((Class<? extends OutputFormat>)outputFormatClass);
        } catch (ClassNotFoundException e) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.CLASS_NOT_FOUND);
        }

        return thisObj;
    }

    /**
     * Wraps {@link Job#setOutputKeyClass(Class)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return this
     */
    @JSFunction
    public static Object setOutputKeyClass(final Context ctx, final Scriptable thisObj, final Object[] args,
                                           final Function func) {
        if (args.length == 1) {
            if (!JavaScriptUtils.isDefined(args[0])) {
                throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
            }
        } else {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        }

        final String className = args[0].toString();

        try {
            final Class outputKeyClass = Class.forName(className);

            if (!WritableComparable.class.isAssignableFrom(outputKeyClass)) {
                throw Utils.makeError(ctx, thisObj,
                                      LembosMessages.makeInvalidClassErrorMessage(WritableComparable.class,
                                                                                  outputKeyClass));
            }

            ((JobWrap)thisObj).job.setOutputKeyClass((Class<? extends WritableComparable>)outputKeyClass);
        } catch (ClassNotFoundException e) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.CLASS_NOT_FOUND);
        }

        return thisObj;
    }

    /**
     * Wraps {@link Job#setOutputValueClass(Class)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return this
     */
    @JSFunction
    public static Object setOutputValueClass(final Context ctx, final Scriptable thisObj, final Object[] args,
                                             final Function func) {
        if (args.length == 1) {
            if (!JavaScriptUtils.isDefined(args[0])) {
                throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
            }
        } else {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        }

        final String className = args[0].toString();

        try {
            final Class outputValueClass = Class.forName(className);

            if (!Writable.class.isAssignableFrom(outputValueClass)) {
                throw Utils.makeError(ctx, thisObj, LembosMessages.makeInvalidClassErrorMessage(Writable.class,
                                                                                                outputValueClass));
            }

            ((JobWrap)thisObj).job.setOutputValueClass((Class<? extends Writable>)outputValueClass);
        } catch (ClassNotFoundException e) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.CLASS_NOT_FOUND);
        }

        return thisObj;
    }

    /**
     * Wraps {@link Job#setPartitionerClass(Class)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return this
     */
    @JSFunction
    public static Object setPartitionerClass(final Context ctx, final Scriptable thisObj, final Object[] args,
                                             final Function func) {
        if (args.length == 1) {
            if (!JavaScriptUtils.isDefined(args[0])) {
                throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
            }
        } else {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        }

        final String className = args[0].toString();

        try {
            final Class partitionerClass = Class.forName(className);

            if (!Partitioner.class.isAssignableFrom(partitionerClass)) {
                throw Utils.makeError(ctx, thisObj, LembosMessages.makeInvalidClassErrorMessage(Partitioner.class,
                                                                                                partitionerClass));
            }

            ((JobWrap)thisObj).job.setPartitionerClass((Class<? extends Partitioner>)partitionerClass);
        } catch (ClassNotFoundException e) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.CLASS_NOT_FOUND);
        }

        return thisObj;
    }

    /**
     * Wraps {@link Job#setReducerClass(Class)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return this
     */
    @JSFunction
    public static Object setReducerClass(final Context ctx, final Scriptable thisObj, final Object[] args,
                                         final Function func) {
        if (args.length == 1) {
            if (!JavaScriptUtils.isDefined(args[0])) {
                throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
            }
        } else {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        }

        final String className = args[0].toString();

        try {
            final Class reducerClass = Class.forName(className);

            if (!Reducer.class.isAssignableFrom(reducerClass)) {
                throw Utils.makeError(ctx, thisObj, LembosMessages.makeInvalidClassErrorMessage(Reducer.class,
                                                                                                reducerClass));
            }

            ((JobWrap)thisObj).job.setReducerClass((Class<? extends Reducer>)reducerClass);
        } catch (ClassNotFoundException e) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.CLASS_NOT_FOUND);
        }

        return thisObj;
    }

    /**
     * Wraps {@link Job#setReduceSpeculativeExecution(boolean)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return this
     */
    @JSFunction
    public static Object setReduceSpeculativeExecution(final Context ctx, final Scriptable thisObj,
                                                       final Object[] args, final Function func) {
        if (args.length == 1) {
            if (JavaScriptUtils.isDefined(args[0])) {
                if (!(args[0] instanceof Boolean)) {
                    throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_MUST_BE_BOOL);
                }
            } else {
                throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
            }
        } else {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        }

        ((JobWrap)thisObj).job.setReduceSpeculativeExecution(Boolean.valueOf(args[0].toString()));

        return thisObj;
    }

    /**
     * Wraps {@link Job#setSortComparatorClass(Class)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return this
     */
    @JSFunction
    public static Object setSortComparatorClass(final Context ctx, final Scriptable thisObj, final Object[] args,
                                                final Function func) {
        if (args.length == 1) {
            if (!JavaScriptUtils.isDefined(args[0])) {
                throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
            }
        } else {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        }

        final String className = args[0].toString();

        try {
            final Class sortComparatorClass = Class.forName(className);

            if (!RawComparator.class.isAssignableFrom(sortComparatorClass)) {
                throw Utils.makeError(ctx, thisObj, LembosMessages.makeInvalidClassErrorMessage(RawComparator.class,
                                                                                                sortComparatorClass));
            }

            ((JobWrap)thisObj).job.setSortComparatorClass((Class<? extends RawComparator>)sortComparatorClass);
        } catch (ClassNotFoundException e) {
            throw Utils.makeError(ctx, thisObj, LembosMessages.CLASS_NOT_FOUND);
        }

        return thisObj;
    }

    /**
     * Wraps {@link Job#setSpeculativeExecution(boolean)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return this
     */
    @JSFunction
    public static Object setSpeculativeExecution(final Context ctx, final Scriptable thisObj, final Object[] args,
                                                 final Function func) {
        if (args.length == 1) {
            if (JavaScriptUtils.isDefined(args[0])) {
                if (!(args[0] instanceof Boolean)) {
                    throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_MUST_BE_BOOL);
                }
            } else {
                throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
            }
        } else {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        }

        ((JobWrap)thisObj).job.setSpeculativeExecution(Boolean.valueOf(args[0].toString()));

        return thisObj;
    }

    /**
     * Wraps {@link Job#setupProgress()}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return the setup progress
     */
    @JSFunction
    public static Object setupProgress(final Context ctx, final Scriptable thisObj, final Object[] args,
                                       final Function func) {
        try {
            // Converted to double because Float is not something supported by JavaScript
            return Double.valueOf(Float.toString(((JobWrap)thisObj).job.setupProgress()));
        } catch (IOException e) {
            throw Utils.makeError(ctx, thisObj, e.getMessage());
        }
    }

    /**
     * Wraps {@link Job#setWorkingDirectory(Path)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return this
     */
    @JSFunction
    public static Object setWorkingDirectory(final Context ctx, final Scriptable thisObj, final Object[] args,
                                             final Function func) {
        if (args.length == 1) {
            if (!JavaScriptUtils.isDefined(args[0])) {
                throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
            }
        } else {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        }

        try {
            ((JobWrap)thisObj).job.setWorkingDirectory(new Path(URI.create(args[0].toString())));
        } catch (IOException e) {
            throw Utils.makeError(ctx, thisObj, e.getMessage());
        }

        return thisObj;
    }

    /**
     * Wraps {@link Job#submit()}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     */
    @JSFunction
    public static void submit(final Context ctx, final Scriptable thisObj, final Object[] args,
                              final Function func) {
        try {
            ((JobWrap)thisObj).job.submit();
        } catch (ClassNotFoundException | InterruptedException | IOException e) {
            throw Utils.makeError(ctx, thisObj, e.getMessage());
        }
    }

    /**
     * Wraps {@link Job#waitForCompletion(boolean)}.
     *
     * @param ctx the JavaScript context (unused)
     * @param thisObj the 'this' object of the caller
     * @param args the arguments for the call
     * @param func the function called (unused)
     *
     * @return the result of the job
     */
    @JSFunction
    public static Object waitForCompletion(final Context ctx, final Scriptable thisObj, final Object[] args,
                                           final Function func) {
        if (args.length == 1) {
            if (JavaScriptUtils.isDefined(args[0])) {
                if (!(args[0] instanceof Boolean)) {
                    throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_MUST_BE_BOOL);
                }
            } else {
                throw Utils.makeError(ctx, thisObj, LembosMessages.FIRST_ARG_REQUIRED);
            }
        } else {
            throw Utils.makeError(ctx, thisObj, LembosMessages.ONE_ARG_EXPECTED);
        }

        try {
            return ((JobWrap)thisObj).job.waitForCompletion(Boolean.valueOf(args[0].toString()));
        } catch (ClassNotFoundException | InterruptedException | IOException e) {
            throw Utils.makeError(ctx, thisObj, e.getMessage());
        }
    }

    /**
     * Wraps {@link Job#toString()}.
     *
     * @param ctx the JavaScript context
     * @param thisObj the 'this' object
     * @param args the function arguments
     * @param func the function being called
     *
     * @return the string representation
     */
    @JSFunction("toString")
    public static Object jsToString(final Context ctx, final Scriptable thisObj, final Object[] args,
                                    final Function func) {
        return ((JobWrap)thisObj).job.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getClassName() {
        return CLASS_NAME;
    }

    /**
     * @return the wrapped Hadoop job
     */
    public Job getJob() {
        return job;
    }

    /**
     * @param job the Hadoop job to wrap
     */
    public void setJob(final Job job) {
        this.job = job;
    }

}
