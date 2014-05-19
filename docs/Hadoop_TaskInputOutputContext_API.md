The purpose of the `TaskInputOutputContext` object is to provide programmatic access to the
[Hadoop TaskInputOutputContext][hadoop-taskinputoutputcontext] object.  Below is a list of exposed object properties.

## TaskInputOutputContext

For example usage of all available APIs, please see the [unit tests][hadoop-taskinputoutputcontext-tests].

```javascript
/* No public constructor. */

/**
 * Return the configuration for the job.
 *
 * @returns {Configuration}
 */
TaskInputOutputContext.prototype.getConfiguration = function () { /* ... */ };

/**
 * Returns the counter for the given group name and counter name or creates a new one.
 *
 * @param {string} groupName - The group name to search for, or create with
 * @param {string} counterName - The counter name to search for, or create with
 *
 * @returns {Counter}
 */
TaskInputOutputContext.prototype.getCounter = function (groupName, counterName) { /* ... */ };

/**
 * Generate an output key/value pair.
 *
 * @param {?} key - The output key
 * @param {?} value - The output value
 *
 * @returns {Configuration}
 */
TaskInputOutputContext.prototype.write = function (key, value) { /* ... */ };
```

[hadoop-taskinputoutputcontext]: http://hadoop.apache.org/docs/r1.0.4/api/org/apache/hadoop/mapreduce/TaskInputOutputContext.html
[hadoop-taskinputoutputcontext-tests]: https://github.com/apigee/lembos/blob/master/src/test/resources/node_modules/TaskInputOutputContextWrapTest-testTaskInputOutputContext/index.js
