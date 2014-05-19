The purpose of the `Counter` object is to provide programmatic access to the [Hadoop CounterGroup][hadoop-countergroup]
object.  Below is a list of exposed object properties.

## CounterGroup

For example usage of all available APIs, please see the [unit tests][hadoop-countergroup-tests].

```javascript
/* No public constructor. */

/**
 * Find counter by name or create one with the given name.
 *
 * @param {string} name - The name of the counter to find or create
 *
 * @returns {Counter}
 */
CounterGroup.prototype.findCounter = function (name) { /* ... */ };

/**
 * Get the display name of the group.
 *
 * @returns {string}
 */
CounterGroup.prototype.getDisplayName = function () { /* ... */ };

/**
 * Get the name of the group.
 *
 * @returns {string}
 */
CounterGroup.prototype.getName = function () { /* ... */ };

/**
 * Increments multiple counters by their amounts in another CounterGroup instance.
 *
 * @param {CounterGroup} other - The other group of counters
 *
 * @returns {CounterGroup} this
 */
CounterGroup.prototype.incrAllCounters = function (other) { /* ... */ };

/**
 * Returns the number of counters in this group.
 *
 * @returns {number}
 */
CounterGroup.prototype.size = function () { /* ... */ };
```

[hadoop-countergroup]: http://hadoop.apache.org/docs/r1.0.4/api/org/apache/hadoop/mapreduce/CounterGroup.html
[hadoop-countergroup-tests]: https://github.com/apigee/lembos/blob/master/src/test/resources/node_modules/HadoopInternalITest-testCounters/index.js
