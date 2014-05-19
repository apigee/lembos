The purpose of the `hadoop-input` module is to provide programmatic access to the following objects:

* [Hadoop DBOutputFormat][hadoop-dboutputformat] object
* [Hadoop FileOutputFormat][hadoop-fileoutputformat] object
* [Hadoop SequenceFileOutputFormat][hadoop-sequencefileoutputformat] object
* [Hadoop SequenceFileAsBinaryOutputFormat][hadoop-sequencefileasbinaryoutputformat] object
* [Hadoop TextOutputFormat][hadoop-textoutputformat] object

Below is a list of exposed module properties.

## DBOutputFormat

For example usage of all available APIs, please see the [unit tests][hadoop-output-dboutputformat-tests].

```javascript
/**
 * Represents a Hadoop DBOutputFormat.
 */
var DBOutputFormat = {};

/**
 * Initializes the reduce-part of the job with the appropriate output settings
 *
 * @param {Job} job - The Hadoop job
 * @param {string} tableName - The table to insert data into
 * @param {number|string[]} fieldCountOrFieldNames - If number, the field count.  If string array, field names.
 */
DBInputFormat.setInput = function (job, tableName, fieldCountOrFieldNames) { /* ... */ };
```

## FileOutputFormat

For example usage of all available APIs, please see the [unit tests][hadoop-output-fileoutputformat-tests].

```javascript
/**
 * Represents a Hadoop FileOutputFormat.
 */
var FileOutputFormat = {};

/**
 * Is the job output compressed?
 *
 * @param {Job} job - The Hadoop job
 *
 * @returns {boolean}
 */
FileOutputFormat.getCompressOutput = function (job) { /* ... */ };

/**
 * Get the CompressionCodec for compressing the job outputs.
 *
 * @param {Job} job - The Hadoop job
 * @param {string} defaultValue - The CompressionCodec to return if not set
 *
 * @returns {string}
 */
FileOutputFormat.getOutputCompressorClass = function (job, defaultValue) { /* ... */ };

/**
 * Get the path to the output directory for the map-reduce job.
 *
 * @param {Job} job - The Hadoop job
 *
 * @returns {string}
 */
FileOutputFormat.getOutputPath = function (job) { /* ... */ };

/**
 * Helper function to generate a path for a file that is unique for the task within the job output directory.
 *
 * @param {Job} job - The Hadoop job
 * @param {string} name - The name of the file
 * @param {string} ext - The extension of the file
 *
 * @returns {string}
 */
FileOutputFormat.getPathForWorkFile = function (job, name, ext) { /* ... */ };

/**
 * Get the path to the task's temporary output directory for the map-reduce job
 *
 * @param {TaskInputOutputContext} context - The Hadoop task context
 *
 * @returns {string}
 */
FileOutputFormat.getWorkOutputPath = function (context) { /* ... */ };

/**
 * Set whether the output of the job is compressed.
 *
 * @param {Job} job - The Hadoop job
 * @param {boolean} flag - The flag value
 */
FileOutputFormat.setCompressOutput = function (job, flag) { /* ... */ };

/**
 * Set the CompressionCodec to be used to compress job outputs.
 *
 * @param {Job} job - The Hadoop job
 * @param {string} className - The CompressionCodec to return if not set
 */
FileOutputFormat.setOutputCompressorClass = function (job, className) { /* ... */ };

/**
 * Set the path to the output directory for the map-reduce job.
 *
 * @param {Job} job - The Hadoop job
 * @param {string} path - The output path
 *
 * @returns {string}
 */
FileOutputFormat.setOutputPath = function (job) { /* ... */ };
```

## SequenceFileOutputFormat

For example usage of all available APIs, please see the [unit tests][hadoop-output-sequencefileoutputformat-tests].

```javascript
/**
 * Represents a Hadoop SequenceFileOutputFormat.
 */
var SequenceFileOutputFormat = {};

/**
 * Get the SequenceFile.CompressionType for the output SequenceFile.
 *
 * @param {Job} job - The Hadoop job
 *
 * @returns {string}
 */
SequenceFileOutputFormat.getOutputCompressionType = function (job) { /* ... */ };

/**
 * Set the SequenceFile.CompressionType for the output SequenceFile.
 *
 * @param {Job} job - The Hadoop job
 * @param {string} compressionType - The SequenceFile.CompressionType for the output SequenceFile
 */
SequenceFileOutputFormat.setOutputCompressionType = function (job, compressionType) { /* ... */ };

/* Same APIs as the FileOutputFormat */
```

## SequenceFileAsBinaryOutputFormat

For example usage of all available APIs, please see the
[unit tests][hadoop-output-sequencefileasbinaryoutputformat-tests].

```javascript
/**
 * Represents a Hadoop SequenceFileAsBinaryOutputFormat.
 */
var SequenceFileAsBinaryOutputFormat = {};

/**
 * Get the key class for the SequenceFile.
 *
 * @param {Job} job - The Hadoop job
 *
 * @returns {string}
 */
SequenceFileAsBinaryOutputFormat.getSequenceFileOutputKeyClass = function (job) { /* ... */ };

/**
 * Get the value class for the SequenceFile.
 *
 * @param {Job} job - The Hadoop job
 *
 * @returns {string}
 */
SequenceFileAsBinaryOutputFormat.getSequenceFileOutputValueClass = function (job) { /* ... */ };

/**
 * Set the key class for the SequenceFile.
 *
 * @param {Job} job - The Hadoop job
 * @param {string} className - The SequenceFile output key class
 */
SequenceFileAsBinaryOutputFormat.setSequenceFileOutputKeyClass = function (job, className) { /* ... */ };

/**
 * Set the value class for the SequenceFile.
 *
 * @param {Job} job - The Hadoop job
 * @param {string} className - The SequenceFile output value class
 */
SequenceFileAsBinaryOutputFormat.setSequenceFileOutputValueClass = function (job, className) { /* ... */ };

/* Same APIs as the SequenceFileOutputFormat */
```

## TextOutputFormat

For example usage of all available APIs, please see the [unit tests][hadoop-output-textoutputformat-tests].

```javascript
/**
 * Represents a Hadoop TextOutputFormat.
 */
var TextOutputFormat = {};

/* Same APIs as the FileOutputFormat */
```

[hadoop-dboutputformat]: http://hadoop.apache.org/docs/r1.0.4/api/org/apache/hadoop/mapreduce/lib/db/DBOutputFormat.html
[hadoop-fileoutputformat]: http://hadoop.apache.org/docs/r1.0.4/api/org/apache/hadoop/mapreduce/lib/output/FileOutputFormat.html
[hadoop-output-dboutputformat-tests]: https://github.com/apigee/lembos/blob/master/src/test/resources/node_modules/HadoopOutputTest-testDBOutputFormat/index.js
[hadoop-output-fileoutputformat-tests]: https://github.com/apigee/lembos/blob/master/src/test/resources/node_modules/HadoopOutputTest-testFileOutputFormat/index.js
[hadoop-output-sequencefileoutputformat-tests]: https://github.com/apigee/lembos/blob/master/src/test/resources/node_modules/HadoopOutputTest-testSequenceFileOutputFormat/index.js
[hadoop-output-sequencefileasbinaryoutputformat-tests]: https://github.com/apigee/lembos/blob/master/src/test/resources/node_modules/HadoopOutputTest-testSequenceFileAsBinaryOutputFormat/index.js
[hadoop-output-textoutputformat-tests]: https://github.com/apigee/lembos/blob/master/src/test/resources/node_modules/HadoopOutputTest-testTextOutputFormat/index.js
[hadoop-sequencefileoutputformat]: http://hadoop.apache.org/docs/r1.0.4/api/org/apache/hadoop/mapreduce/lib/output/SequenceFileOutputFormat.html
[hadoop-sequencefileasbinaryoutputformat]: http://hadoop.apache.org/docs/r1.0.4/api/org/apache/hadoop/mapreduce/lib/output/SequenceFileAsBinaryOutputFormat.html
[hadoop-textoutputformat]: http://hadoop.apache.org/docs/r1.0.4/api/org/apache/hadoop/mapreduce/lib/output/TextOutputFormat.html