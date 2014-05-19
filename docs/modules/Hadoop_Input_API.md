The purpose of the `hadoop-input` module is to provide programmatic access to the following objects:

* [Hadoop CombineFileInputFormat][hadoop-combinefileinputformat] object
* [Hadoop DataDrivenDBInputFormat][hadoop-datadrivendbinputformat] object
* [Hadoop DBInputFormat][hadoop-dbinputformat] object
* [Hadoop FileInputFormat][hadoop-fileinputformat] object
* [Hadoop KeyValueTextInputFormat][hadoop-keyvaluetextinputformat] object
* [Hadoop NLineInputFormat][hadoop-nlineinputformat] object
* [Hadoop OracleDataDrivenDBInputFormat][hadoop-oracledatadrivendbinputformat] object
* [Hadoop SequenceFileInputFormat][hadoop-sequencefileinputformat] object
* [Hadoop SequenceFileAsBinaryInputFormat][hadoop-sequencefileasbinaryinputformat] object
* [Hadoop SequenceFileAsTextInputFormat][hadoop-sequencefileastextinputformat] object
* [Hadoop SequenceFileInputFilter][hadoop-sequencefileinputfilter] object
* [Hadoop TextInputFormat][hadoop-textinputformat] object

Below is a list of exposed module properties.

## CombineFileInputFormat

For example usage of all available APIs, please see the [unit tests][hadoop-input-combinefileinputformat-tests].

```javascript
/**
 * Represents a Hadoop CombineFileInputFormat.
 */
var CombineFileInputFormat = {};

/* Same APIs as the FileInputFormat */
```

## DataDrivenDBInputFormat

For example usage of all available APIs, please see the [unit tests][hadoop-input-datbasedrivendbinputformat-tests].

```javascript
/**
 * Represents a Hadoop DataDrivenDBInputFormat.
 */
var DataDrivenDBInputFormat = {};

/**
 * Set the user-defined bounding query to use with a user-defined query.
 *
 * @param {Configuration} conf - The Hadoop configuration
 * @param {string} query - Set the user-defined bounding query to use with a user-defined query. This *must* include
 *                         the substring "$CONDITIONS" (DataDrivenDBInputFormat.SUBSTITUTE_TOKEN) inside the WHERE
 *                         clause, so that DataDrivenDBInputFormat knows where to insert split clauses. e.g., "SELECT
 *                         foo FROM mytable WHERE $CONDITIONS" This will be expanded to something like: SELECT foo
 *                         FROM mytable WHERE (id > 100) AND (id < 250) inside each split.
 */
DataDrivenDBInputFormat.setBoundingQuery = function (conf, query) { /* ... */ };

/* Same APIs as the DBInputFormat */
```

## DBInputFormat

For example usage of all available APIs, please see the [unit tests][hadoop-input-dbinputformat-tests].

```javascript
/**
 * Represents a Hadoop DBInputFormat.
 */
var DBInputFormat = {};

/**
 * Initializes the map-part of the job with the appropriate input settings.
 *
 * @param {Job} job - The Hadoop job
 * @param {string} inputClass - The class object implementing DBWritable, which is the Java object holding tuple fields
 * @param {string} inputQueryOrTableName - The input query for the four argument version or the table name for the six
 *                                         argument version
 * @param {string} conditionsOrInputCountQuery - The conditions for the four argument verison or the input count query
 *                                               for the six argument verison
 * @param {string} [orderBy] - The fieldNames in the orderBy clause
 * @param {string[]} [fieldNames] - The field names in the table
 */
DBInputFormat.setInput = function (job, inputClass, inputQueryOrTableName, conditionsOrInputCountQuery, orderBy,
                                   fieldNames) { /* ... */ };
```

## FileInputFormat

For example usage of all available APIs, please see the [unit tests][hadoop-input-fileinputformat-tests].

```javascript
/**
 * Represents a Hadoop FileInputFormat.
 */
var FileInputFormat = {};

/**
 * Add a path to the list of inputs for the map-reduce job.
 *
 * @param {Job} job - The Hadoop job
 * @param {string} path - The path to be added to the list of inputs for the map-reduce job
 */
FileInputFormat.addInputPath = function (job, path) { /* ... */ };

/**
 * Add the given comma separated paths to the list of inputs for the map-reduce job.
 *
 * @param {Job} job - The Hadoop job
 * @param {string} paths - The comma separated paths to be added to the list of inputs for the map-reduce job
 */
FileInputFormat.addInputPaths = function (job, paths) { /* ... */ };

/**
 * Get a PathFilter class name of the filter set for the input paths.
 *
 * @param {Job} job - The Hadoop job
 *
 * @returns {string}
 */
FileInputFormat.getInputPathFilter = function (job) { /* ... */ };

/**
 * Get the list of input paths for the map-reduce job.
 *
 * @param {Job} job - The Hadoop job
 *
 * @returns {string[]}
 */
FileInputFormat.getInputPaths = function (job) { /* ... */ };

/**
 * Get the maximum split size.
 *
 * @param {Job} job - The Hadoop job
 *
 * @returns {number}
 */
FileInputFormat.getMaxSplitSize = function (job) { /* ... */ };

/**
 * Get the minimum split size.
 *
 * @param {Job} job - The Hadoop job
 *
 * @returns {number}
 */
FileInputFormat.getMinSplitSize = function (job) { /* ... */ };

/**
 * Set the PathFilter by class name to be applied to the input paths for the map-reduce job.
 *
 * @param {Job} job - The Hadoop job
 * @param {string} filterClass - The PathFilter class name to use
 */
FileInputFormat.setInputPathFilter = function (job, filterClass) { /* ... */ };

/**
 * Sets the given comma separated paths as the list of inputs for the map-reduce job.
 *
 * @param {Job} job - The Hadoop job
 * @param {string} paths - The comma separated paths to be set as the list of inputs for the map-reduce job
 *
 * @returns {string[]}
 */
FileInputFormat.setInputPaths = function (job, paths) { /* ... */ };

/**
 * Set the maximum split size.
 *
 * @param {Job} job - The Hadoop job
 * @param {number} splitSize - The maximum split size
 *
 * @returns {number}
 */
FileInputFormat.setMaxSplitSize = function (job, splitSize) { /* ... */ };

/**
 * Set the minimum split size.
 *
 * @param {Job} job - The Hadoop job
 * @param {number} splitSize - The minimum split size
 *
 * @returns {number}
 */
FileInputFormat.setMinSplitSize = function (job, splitSize) { /* ... */ };
```

## KeyValueTextInputFormat

For example usage of all available APIs, please see the [unit tests][hadoop-input-keyvaluetextinputformat-tests].

```javascript
/**
 * Represents a Hadoop KeyValueTextInputFormat.
 */
var KeyValueTextInputFormat = {};

/* Same APIs as the FileInputFormat */
```

## NLineInputFormat

For example usage of all available APIs, please see the [unit tests][hadoop-input-nlineinputformat-tests].

```javascript
/**
 * Represents a Hadoop NLineInputFormat.
 */
var NLineInputFormat = {};

/**
 * Get the number of lines per split.
 *
 * @param {Job} job - The Hadoop job
 *
 * @returns {number}
 */
NLineInputFormat.getNumLinesPerSplit = function (job) { /* ... */ };

/**
 * Get the number of splits for the given file.
 *
 * @param {string} path - The input path
 * @param {Configuration} conf - The Hadoop configuration
 * @param {number} numLinesPerSplit - The number of lines per split
 *
 * @returns {number}
 */
NLineInputFormat.getSplitsForFile = function (path, conf, numLinesPerSplit) { /* ... */ };

/**
 * Set the number of lines per split.
 *
 * @param {Job} job - The Hadoop job
 * @param {number} numLines - The number of lines per split
 *
 * @returns {number}
 */
NLineInputFormat.setNumLinesPerSplit = function (job, numLines) { /* ... */ };

/* Same APIs as the FileInputFormat */
```

## OracleDataDrivenDBInputFormat

For example usage of all available APIs, please see the
[unit tests][hadoop-input-oracledatbasedrivendbinputformat-tests].

```javascript
/**
 * Represents a Hadoop OracleDataDrivenDBInputFormat.
 */
var OracleDataDrivenDBInputFormat = {};

/* Same APIs as the DataDrivenDBInputFormat */
```

## SequenceFileInputFormat

For example usage of all available APIs, please see the [unit tests][hadoop-input-sequencefileinputformat-tests].

```javascript
/**
 * Represents a Hadoop SequenceFileInputFormat.
 */
var SequenceFileInputFormat = {};

/* Same APIs as the FileInputFormat */
```

## SequenceFileAsBinaryInputFormat

For example usage of all available APIs, please see the
[unit tests][hadoop-input-sequencefileasbinaryinputformat-tests].

```javascript
/**
 * Represents a Hadoop SequenceFileAsBinaryInputFormat.
 */
var SequenceFileAsBinaryInputFormat = {};

/* Same APIs as the SequenceFileInputFormat */
```

## SequenceFileAsTextInputFormat

For example usage of all available APIs, please see the [unit tests][hadoop-input-sequencefileastextinputformat-tests].

```javascript
/**
 * Represents a Hadoop SequenceFileAsTextInputFormat.
 */
var SequenceFileAsTextInputFormat = {};

/* Same APIs as the SequenceFileInputFormat */
```

## SequenceFileInputFilter

For example usage of all available APIs, please see the [unit tests][hadoop-input-sequencefileinputfilter-tests].

```javascript
/**
 * Represents a Hadoop SequenceFileInputFilter.
 */
var SequenceFileInputFilter = {};

/**
 * Set the filter class.
 *
 * @param {Job} job - The Hadoop job
 * @param {string} filterClass - The PathFilter class name
 */
SequenceFileInputFilter.setFilterClass = function (job, filterClass) { /* ... */ };

/* Same APIs as the SequenceFileInputFormat */
```

## TextInputFormat

For example usage of all available APIs, please see the [unit tests][hadoop-input-textinputformat-tests].

```javascript
/**
 * Represents a Hadoop TextInputFormat.
 */
var TextInputFormat = {};

/* Same APIs as the FileInputFormat */
```

[hadoop-combinefileinputformat]: http://hadoop.apache.org/docs/r1.0.4/api/org/apache/hadoop/mapreduce/lib/input/CombineFileInputFormat.html
[hadoop-datadrivendbinputformat]: http://hadoop.apache.org/docs/r1.0.4/api/org/apache/hadoop/mapreduce/lib/db/DataDrivenDBInputFormat.html
[hadoop-dbinputformat]: http://hadoop.apache.org/docs/r1.0.4/api/org/apache/hadoop/mapreduce/lib/db/DBInputFormat.html
[hadoop-fileinputformat]: http://hadoop.apache.org/docs/r1.0.4/api/org/apache/hadoop/mapreduce/lib/input/FileInputFormat.html
[hadoop-input-combinefileinputformat-tests]: https://github.com/apigee/lembos/blob/master/src/test/resources/node_modules/HadoopInputTest-testCombineFileInputFormat/index.js
[hadoop-input-datbasedrivendbinputformat-tests]: https://github.com/apigee/lembos/blob/master/src/test/resources/node_modules/HadoopInputTest-testDataDrivenDBInputFormat/index.js
[hadoop-input-dbinputformat-tests]: https://github.com/apigee/lembos/blob/master/src/test/resources/node_modules/HadoopInputTest-testDBInputFormat/index.js
[hadoop-input-fileinputformat-tests]: https://github.com/apigee/lembos/blob/master/src/test/resources/node_modules/HadoopInputTest-testFileInputFormat/index.js
[hadoop-input-keyvaluetextinputformat-tests]: https://github.com/apigee/lembos/blob/master/src/test/resources/node_modules/HadoopInputTest-testKeyValueTextInputFormat/index.js
[hadoop-input-nlineinputformat-tests]: https://github.com/apigee/lembos/blob/master/src/test/resources/node_modules/HadoopInputTest-testNLineInputFormat/index.js
[hadoop-input-oracledatbasedrivendbinputformat-tests]: https://github.com/apigee/lembos/blob/master/src/test/resources/node_modules/HadoopInputTest-testOracleDataDrivenDBInputFormat/index.js
[hadoop-input-sequencefileinputformat-tests]: https://github.com/apigee/lembos/blob/master/src/test/resources/node_modules/HadoopInputTest-testSequenceFileInputFormat/index.js
[hadoop-input-sequencefileasbinaryinputformat-tests]: https://github.com/apigee/lembos/blob/master/src/test/resources/node_modules/HadoopInputTest-testSequenceFileAsBinaryInputFormat/index.js
[hadoop-input-sequencefileastextinputformat-tests]: https://github.com/apigee/lembos/blob/master/src/test/resources/node_modules/HadoopInputTest-testSequenceFileAsTextInputFormat/index.js
[hadoop-input-sequencefileinputfilter-tests]: https://github.com/apigee/lembos/blob/master/src/test/resources/node_modules/HadoopInputTest-testSequenceFileInputFilter/index.js
[hadoop-input-textinputformat-tests]: https://github.com/apigee/lembos/blob/master/src/test/resources/node_modules/HadoopInputTest-testTextInputFormat/index.js
[hadoop-keyvaluetextinputformat]: http://hadoop.apache.org/docs/r1.0.4/api/org/apache/hadoop/mapreduce/lib/input/KeyValueTextInputFormat.html
[hadoop-nlineinputformat]: http://hadoop.apache.org/docs/r1.0.4/api/org/apache/hadoop/mapreduce/lib/input/NLineInputFormat.html
[hadoop-oracledatadrivendbinputformat]: http://hadoop.apache.org/docs/r1.0.4/api/org/apache/hadoop/mapreduce/lib/db/OracleDataDrivenDBInputFormat.html
[hadoop-sequencefileinputformat]: http://hadoop.apache.org/docs/r1.0.4/api/org/apache/hadoop/mapreduce/lib/input/SequenceFileInputFormat.html
[hadoop-sequencefileasbinaryinputformat]: http://hadoop.apache.org/docs/r1.0.4/api/org/apache/hadoop/mapreduce/lib/input/SequenceFileAsBinaryInputFormat.html
[hadoop-sequencefileastextinputformat]: http://hadoop.apache.org/docs/r1.0.4/api/org/apache/hadoop/mapreduce/lib/input/SequenceFileAsTextInputFormat.html
[hadoop-sequencefileinputfilter]: http://hadoop.apache.org/docs/r1.0.4/api/org/apache/hadoop/mapreduce/lib/input/SequenceFileInputFilter.html
[hadoop-textinputformat]: http://hadoop.apache.org/docs/r1.0.4/api/org/apache/hadoop/mapreduce/lib/input/TextInputFormat.html
