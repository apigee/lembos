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

import io.apigee.lembos.utils.JavaScriptUtils;
import io.apigee.trireme.core.NodeException;
import org.apache.hadoop.conf.Configuration;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

/**
 * This class will construct an environment that all Lembos MapReduce components will consult/use during the MapReduce
 * lifecycle.
 */
public class LembosMapReduceEnvironment extends LembosNodeEnvironment {

    private Function combineFunction;
    private Function combineCleanupFunction;
    private Function combineSetupFunction;
    private Function groupFunction;
    private Function groupCleanupFunction;
    private Function groupSetupFunction;
    private Function jobSetupFunction;
    private Function jobCleanupFunction;
    private Function mapFunction;
    private Function mapCleanupFunction;
    private Function mapSetupFunction;
    private Function partitionFunction;
    private Function partitionCleanupFunction;
    private Function partitionSetupFunction;
    private Function reduceFunction;
    private Function reduceCleanupFunction;
    private Function reduceSetupFunction;
    private Function sortFunction;
    private Function sortCleanupFunction;
    private Function sortSetupFunction;
    private NativeObject configuration;

    /**
     * Constructor.
     *
     * @param moduleName the Node.js module name
     * @param modulePath the Node.js module location/path
     * @param moduleArgs the Node.js module arguments (CLI arguments)
     */
    public LembosMapReduceEnvironment(final String moduleName, final File modulePath,
                                      @Nullable final String[] moduleArgs) {
        super(moduleName, modulePath, moduleArgs);
    }

    /**
     * Retrieves an initialized MapReduce environment.
     *
     * @param conf the Hadoop configuration
     *
     * @return the initialized MapReduce environment
     *
     * @throws ExecutionException if something goes wrong executing the Node.js module
     * @throws IOException if something goes wrong setting up the environment
     * @throws InterruptedException if the execution of the Node.js module gets interrupted
     * @throws NodeException if there is an issue with the Node.js module
     */
    public static LembosMapReduceEnvironment fromConf(final Configuration conf)
            throws ExecutionException, InterruptedException, IOException, NodeException {
        File modulePath = new File(LembosConstants.MR_DISTRIBUTED_CACHE_SYMLINK);

        if (modulePath.exists()) {
            // Locate module in DistributedCache
            final File[] children = modulePath.listFiles();

            if (children == null) {
                throw new RuntimeException("Unable to find Node.js module in DistributedCache");
            }

            for (final File child : children) {
                final String moduleName = conf.get(LembosConstants.MR_MODULE_NAME);
                final String[] possibleNames = new String[] {
                        moduleName,
                        moduleName + ".js",
                        moduleName + ".json",
                        moduleName + ".node"
                };

                if (Arrays.asList(possibleNames).contains(child.getName())) {
                    modulePath = child;
                    break;
                }
            }
        } else if (!modulePath.exists() && conf.get(LembosConstants.MR_MODULE_PATH) != null) {
            // Default to module path for testing and for LocalJobRunner (DistributedCache does not work in local mode)
            modulePath = new File(conf.get(LembosConstants.MR_MODULE_PATH));
        }

        final LembosMapReduceEnvironment mrEnv =
                new LembosMapReduceEnvironment(conf.get(LembosConstants.MR_MODULE_NAME), modulePath,
                                                conf.getStrings(LembosConstants.MR_MODULE_ARGS));

        mrEnv.initialize();

        return mrEnv;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize() throws ExecutionException, InterruptedException, IOException, NodeException {
        super.initialize();

        this.combineFunction = this.getByNameAndType("combine", Function.class, false);
        this.combineCleanupFunction = this.getByNameAndType("combineCleanup", Function.class, false);
        this.combineSetupFunction = this.getByNameAndType("combineSetup", Function.class, false);
        this.configuration = this.getByNameAndType("config", NativeObject.class, false);
        this.groupFunction = this.getByNameAndType("group", Function.class, false);
        this.groupCleanupFunction = this.getByNameAndType("groupCleanup", Function.class, false);
        this.groupSetupFunction = this.getByNameAndType("groupSetup", Function.class, false);
        this.jobSetupFunction = this.getByNameAndType("jobSetup", Function.class, false);
        this.jobCleanupFunction = this.getByNameAndType("jobCleanup", Function.class, false);
        this.mapFunction = this.getByNameAndType("map", Function.class, true);
        this.mapCleanupFunction = this.getByNameAndType("mapCleanup", Function.class, false);
        this.mapSetupFunction = this.getByNameAndType("mapSetup", Function.class, false);
        this.partitionFunction = this.getByNameAndType("partition", Function.class, false);
        this.partitionCleanupFunction = this.getByNameAndType("partitionCleanup", Function.class, false);
        this.partitionSetupFunction = this.getByNameAndType("partitionSetup", Function.class, false);
        this.reduceFunction = this.getByNameAndType("reduce", Function.class, false);
        this.reduceCleanupFunction = this.getByNameAndType("reduceCleanup", Function.class, false);
        this.reduceSetupFunction = this.getByNameAndType("reduceSetup", Function.class, false);
        this.sortFunction = this.getByNameAndType("sort", Function.class, false);
        this.sortCleanupFunction = this.getByNameAndType("sortCleanup", Function.class, false);
        this.sortSetupFunction = this.getByNameAndType("sortSetup", Function.class, false);
    }

    /**
     * @return the combine function
     */
    public Function getCombineFunction() {
        return combineFunction;
    }

    /**
     * @return the combine cleanup function
     */
    public Function getCombineCleanupFunction() {
        return combineCleanupFunction;
    }

    /**
     * @return the combine setup function
     */
    public Function getCombineSetupFunction() {
        return combineSetupFunction;
    }

    /**
     * @return the combine function
     */
    public NativeObject getConfiguration() {
        return configuration;
    }

    /**
     * @return the group function
     */
    public Function getGroupFunction() {
        return groupFunction;
    }

    /**
     * @return the group cleanup function
     */
    public Function getGroupCleanupFunction() {
        return groupCleanupFunction;
    }

    /**
     * @return the group setup function
     */
    public Function getGroupSetupFunction() {
        return groupSetupFunction;
    }

    /**
     * @return the job cleanup function
     */
    public Function getJobCleanupFunction() {
        return jobCleanupFunction;
    }

    /**
     * @return the job setup function
     */
    public Function getJobSetupFunction() {
        return jobSetupFunction;
    }

    /**
     * @return the map function
     */
    public Function getMapFunction() {
        return mapFunction;
    }

    /**
     * @return the map cleanup function
     */
    public Function getMapCleanupFunction() {
        return mapCleanupFunction;
    }

    /**
     * @return the map setup function
     */
    public Function getMapSetupFunction() {
        return mapSetupFunction;
    }

    /**
     * @return the partition function
     */
    public Function getPartitionFunction() {
        return partitionFunction;
    }

    /**
     * @return the partition cleanup function
     */
    public Function getPartitionCleanupFunction() {
        return partitionCleanupFunction;
    }

    /**
     * @return the partition setup function
     */
    public Function getPartitionSetupFunction() {
        return partitionSetupFunction;
    }

    /**
     * @return the reduce function
     */
    public Function getReduceFunction() {
        return reduceFunction;
    }

    /**
     * @return the reduce cleanup function
     */
    public Function getReduceCleanupFunction() {
        return reduceCleanupFunction;
    }

    /**
     * @return the reduce setup function
     */
    public Function getReduceSetupFunction() {
        return reduceSetupFunction;
    }

    /**
     * @return the sort function
     */
    public Function getSortFunction() {
        return sortFunction;
    }

    /**
     * @return the sort cleanup function
     */
    public Function getSortCleanupFunction() {
        return sortCleanupFunction;
    }

    /**
     * @return the sort setup function
     */
    public Function getSortSetupFunction() {
        return sortSetupFunction;
    }

    /**
     * Returns the {@link Function} with the name or null if it's not found.
     *
     * @param name the property name
     * @param type the value type
     * @param required whether or not the value is required to exist
     *
     * @param <T> The type of the object to validate and return
     *
     * @return the function
     */
    private <T> T getByNameAndType(final String name, final Class<T> type, final boolean required) {
        final Object propValue = ScriptableObject.getProperty(this.getModule(), name);
        final boolean isDefined = JavaScriptUtils.isDefined(propValue);
        final String simpleTypeName = type.getSimpleName().startsWith("Native")
                ? type.getSimpleName().replaceFirst("Native", "").toLowerCase()
                : type.getSimpleName().toLowerCase();

        if (required && !isDefined) {
            throw new RuntimeException("Required MapReduce " + simpleTypeName + " (" + name + ") not found");
        } else if (isDefined && !type.isInstance(propValue)) {
            throw new RuntimeException("MapReduce " + simpleTypeName + " (" + name + ") is not a " + simpleTypeName);
        }

        //noinspection unchecked
        return isDefined ? (T)propValue : null;
    }

}
