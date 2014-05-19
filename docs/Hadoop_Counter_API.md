The purpose of the `Counter` object is to provide programmatic access to the [Hadoop Counter][hadoop-counter] object.
Below is a list of exposed object properties.

## Counter

For example usage of all available APIs, please see the [unit tests][hadoop-counter-tests].

```javascript
/* No public constructor. */

/**
 * Get the display name of the counter.
 *
 * @returns {string}
 */
Counter.prototype.getDisplayName = function () { /* ... */ };

/**
 * Get the name of the counter.
 *
 * @returns {string}
 */
Counter.prototype.getName = function () { /* ... */ };

/**
 * Get the name of the counter.
 *
 * @returns {number}
 */
Counter.prototype.getValue = function () { /* ... */ };

/**
 * Increment this counter by the given value
 *
 * @param {number} value - The amount to increment by
 *
 * @returns {Counter} this
 */
Counter.prototype.increment = function (value) { /* ... */ };

/**
 * Set this counter by the given value.
 *
 * @param {number} value - The value to set
 *
 * @returns {Counter} this
 */
Counter.prototype.setValue = function (value) { /* ... */ };
```

[hadoop-counter]: http://hadoop.apache.org/docs/r1.0.4/api/org/apache/hadoop/mapreduce/Counter.html
[hadoop-counter-tests]: https://github.com/apigee/lembos/blob/master/src/test/resources/node_modules/HadoopInternalTest-testCounter/index.js
