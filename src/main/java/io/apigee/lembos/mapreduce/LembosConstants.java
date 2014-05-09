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

/**
 * Class containing the constants in the Hadoop {@link org.apache.hadoop.conf.Configuration}.
 */
public final class LembosConstants {

    /** This is the symlink name used for the MapReduce Node.js DistributedCache archive. */
    public static final String MR_DISTRIBUTED_CACHE_SYMLINK = "MR_MODULE_ROOT";

    /** This are the CLI arguments to pass to the Node.js module. */
    public static final String MR_MODULE_ARGS = "io.apigee.lembos.node.moduleArgs";

    /** This is the name of the Node.js module. */
    public static final String MR_MODULE_NAME = "io.apigee.lembos.mapreduce.moduleName";

    /** This is the path to the Node.js module. (Only useful for testing and LembosMapReduceRunner) */
    public static final String MR_MODULE_PATH = "io.apigee.lembos.mapreduce.modulePath";

    /**
     * Private constructor.
     */
    private LembosConstants() { }

}
