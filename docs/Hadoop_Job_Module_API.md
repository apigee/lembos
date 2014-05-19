The purpose of the `hadoop-job` module is to provide programmatic access to the
[Hadoop Job][hadoop-job] object.  Below is a list of exposed module properties.

## Job

For example usage of all available APIs, please see the [unit tests][hadoop-job-tests].

```javascript
/**
 * Represents a Hadoop Job.
 *
 * @param {Configuration} [conf] - The Hadoop configuration
 * @param {string} [jobName] - The job name
 *
 * @constructor Configuration
 */
var Job = function (conf, jobName) { /* ... */ };

/**
 * Get the combiner class for the job.
 *
 * @returns {string}
 */
Job.prototype.getCombinerClass = function () { /* ... */ };

/**
 * Gets the counters for this job.
 *
 * @returns {Counters}
 */
Job.prototype.getCounters = function () { /* ... */ };

/**
 * Return the configuration for the job.
 *
 * @returns {Configuration}
 */
Job.prototype.getConfiguration = function () { /* ... */ };

/**
 * Get the user defined RawComparator comparator for grouping keys of inputs to the reduce.
 *
 * @returns {string}
 */
Job.prototype.getGroupingComparatorClass = function () { /* ... */ };

/**
 * Get the InputFormat class for the job.
 *
 * @returns {string}
 */
Job.prototype.getInputFormatClass = function () { /* ... */ };

/**
 * Get the pathname of the job's jar.
 *
 * @returns {string}
 */
Job.prototype.getJar = function () { /* ... */ };

/**
 * Get the unique ID for the job.
 *
 * @returns {string}
 */
Job.prototype.getJobID = function () { /* ... */ };

/**
 * Get the user-specified job name.
 *
 * @returns {string}
 */
Job.prototype.getJobName = function () { /* ... */ };

/**
 * Get the key class for the map output data.
 *
 * @returns {string}
 */
Job.prototype.getMapOutputKeyClass = function () { /* ... */ };

/**
 * Get the value class for the map output data.
 *
 * @returns {string}
 */
Job.prototype.getMapOutputValueClass = function () { /* ... */ };

/**
 * Get the Mapper class for the job.
 *
 * @returns {string}
 */
Job.prototype.getMapperClass = function () { /* ... */ };

/**
 * Get configured the number of reduce tasks for this job.
 *
 * @returns {number}
 */
Job.prototype.getNumReduceTasks = function () { /* ... */ };

/**
 * Get the OutputFormat class for the job.
 *
 * @returns {string}
 */
Job.prototype.getOutputFormatClass = function () { /* ... */ };

/**
 * Get the key class for the job output data.
 *
 * @returns {string}
 */
Job.prototype.getOutputKeyClass = function () { /* ... */ };

/**
 * Get the value class for the job output data.
 *
 * @returns {string}
 */
Job.prototype.getOutputValueClass = function () { /* ... */ };

/**
 * Get the Partitioner class for the job.
 *
 * @returns {string}
 */
Job.prototype.getPartitionerClass = function () { /* ... */ };

/**
 * Get the Reducer class for the job.
 *
 * @returns {string}
 */
Job.prototype.getReducerClass = function () { /* ... */ };

/**
 * Get the RawComparator comparator used to compare keys.
 *
 * @returns {string}
 */
Job.prototype.getSortComparatorClass = function () { /* ... */ };

/**
 * Get the URL where some job progress information will be displayed.
 *
 * @returns {string}
 */
Job.prototype.getTrackingURL = function () { /* ... */ };

/**
 * Get the current working directory for the default file system.
 *
 * @returns {string}
 */
Job.prototype.getWorkingDirectory = function () { /* ... */ };

/**
 * Check if the job is finished or not.
 *
 * @returns {boolean}
 */
Job.prototype.isComplete = function () { /* ... */ };

/**
 * Check if the job completed successfully.
 *
 * @returns {boolean}
 */
Job.prototype.isSuccessful = function () { /* ... */ };

/**
 * Kill the running job.
 */
Job.prototype.killJob = function () { /* ... */ };

/**
 * Get the progress of the job's map-tasks, as a float between 0.0 and 1.0.
 *
 * @returns {number}
 */
Job.prototype.mapProgress = function () { /* ... */ };

/**
 * Get the progress of the job's reduce-tasks, as a float between 0.0 and 1.0.
 *
 * @returns {number}
 */
Job.prototype.reduceProgress = function () { /* ... */ };

/**
 * Sets the flag that will allow the JobTracker to cancel the HDFS delegation tokens upon job completion.
 *
 * @param {boolean} value - The flag value
 *
 * @returns {Job} this
 */
Job.prototype.setCancelDelegationTokenUponJobCompletion = function (value) { /* ... */ };

/**
 * Set the combiner class for the job.
 *
 * @param {string} className - The combiner class to use
 *
 * @returns {Job} this
 */
Job.prototype.setCombinerClass = function (className) { /* ... */ };

/**
 * Define the comparator that controls which keys are grouped together for a single call to reduce.
 *
 * @param {string} className - The raw comparator class to use
 *
 * @returns {Job} this
 */
Job.prototype.setGroupingComparatorClass = function (className) { /* ... */ };

/**
 * Set the InputFormat for the job.
 *
 * @param {string} className - The input format class to use
 *
 * @returns {Job} this
 */
Job.prototype.setInputFormatClass = function (className) { /* ... */ };

/**
 * Set the Jar by finding where a given class came from.
 *
 * @param {string} className - The input format class to use
 *
 * @returns {Job} this
 */
Job.prototype.setJarByClass = function (className) { /* ... */ };

/**
 * Set the user-specified job name.
 *
 * @param {string} jobName - The job name to use
 *
 * @returns {Job} this
 */
Job.prototype.setJobName = function (jobName) { /* ... */ };

/**
 * Set the key class for the map output data.
 *
 * @param {string} className - The map output key class to use
 *
 * @returns {Job} this
 */
Job.prototype.setMapOutputKeyClass = function (className) { /* ... */ };

/**
 * Set the value class for the map output data.
 *
 * @param {string} className - The map output value class to use
 *
 * @returns {Job} this
 */
Job.prototype.setMapOutputValueClass = function (className) { /* ... */ };

/**
 * Set the Mapper for the job.
 *
 * @param {string} className - The mapper class to use
 *
 * @returns {Job} this
 */
Job.prototype.setMapperClass = function (className) { /* ... */ };

/**
 * Turn speculative execution on or off for this job for map tasks.
 *
 * @param {boolean} value - The flag value
 *
 * @returns {Job} this
 */
Job.prototype.setMapSpeculativeExecution = function (value) { /* ... */ };

/**
 * Set the number of reduce tasks for the job.
 *
 * @param {boolean} numReduceTasks - The number of reduce tasks
 *
 * @returns {Job} this
 */
Job.prototype.setNumReduceTasks = function (numReduceTasks) { /* ... */ };

/**
 * Set the OutputFormat for the job.
 *
 * @param {string} className - The output format class to use
 *
 * @returns {Job} this
 */
Job.prototype.setOutputFormatClass = function (className) { /* ... */ };

/**
 * Set the key class for the output data.
 *
 * @param {string} className - The output key class to use
 *
 * @returns {Job} this
 */
Job.prototype.setOutputKeyClass = function (className) { /* ... */ };

/**
 * Set the value class for the output data.
 *
 * @param {string} className - The output value class to use
 *
 * @returns {Job} this
 */
Job.prototype.setOutputValueClass = function (className) { /* ... */ };

/**
 * Set the Partitioner for the job.
 *
 * @param {string} className - The partitioner class to use
 *
 * @returns {Job} this
 */
Job.prototype.setPartitionerClass = function (className) { /* ... */ };

/**
 * Set the Reducer for the job.
 *
 * @param {string} className - The reducer class to use
 *
 * @returns {Job} this
 */
Job.prototype.setReducerClass = function (className) { /* ... */ };

/**
 * Turn speculative execution on or off for this job for reduce tasks.
 *
 * @param {boolean} value - The flag value
 *
 * @returns {Job} this
 */
Job.prototype.setReduceSpeculativeExecution = function (value) { /* ... */ };

/**
 * Define the comparator that controls how the keys are sorted before they are passed to the Reducer.
 *
 * @param {string} className - The raw comparator class to use
 *
 * @returns {Job} this
 */
Job.prototype.setSortComparatorClass = function (className) { /* ... */ };

/**
 * Turn speculative execution on or off for this job.
 *
 * @param {boolean} value - The flag value
 *
 * @returns {Job} this
 */
Job.prototype.setSpeculativeExecution = function (value) { /* ... */ };

/**
 * Get the progress of the job's setup, as a float between 0.0 and 1.0.
 *
 * @returns {number}
 */
Job.prototype.setupProgress = function () { /* ... */ };

/**
 * Set the current working directory for the default file system.
 *
 * @param {string} dir - The new current working directory
 *
 * @returns {Job} this
 */
Job.prototype.setWorkingDirectory = function (dir) { /* ... */ };

/**
 * Submit the job to the cluster and return immediately.
 */
Job.prototype.submit = function () { /* ... */ };

/**
 * Submit the job to the cluster and wait for it to finish.
 *
 * @param {boolean} value - The flag value
 *
 * @returns {Job} this
 */
Job.prototype.waitForCompletion = function (value) { /* ... */ };
```

[hadoop-job]: https://hadoop.apache.org/docs/r1.0.4/api/org/apache/hadoop/mapreduce/Job.html
[hadoop-job-tests]: https://github.com/apigee/lembos/blob/master/src/test/resources/node_modules/HadoopJobTest-testHadoopJob/index.js
