The purpose of the `Counters` object is to provide programmatic access to the [Hadoop Counters][hadoop-counters] object.
Below is a list of exposed object properties.

## Counters

For example usage of all available APIs, please see the [unit tests][hadoop-counters-tests].

```javascript
/* No public constructor. */

/**
 * Returns the total number of counters, by summing the number of counters in each group.
 *
 * @returns {number}
 */
Counters.prototype.countCounters = function () { /* ... */ };

/**
 * Returns the counter for the given group name and counter name or creates a new one.
 *
 * @param {string} groupName - The group name to search for, or create with
 * @param {string} counterName - The counter name to search for, or create with
 *
 * @returns {Counter}
 */
Counters.prototype.findCounter = function (groupName, counterName) { /* ... */ };

/**
 * Returns the named counter group, or an empty group if there is none with the specified name.
 *
 * @param {string} name - The group name to search for, or create with
 *
 * @returns {CounterGroup}
 */
Counters.prototype.getGroup = function (name) { /* ... */ };

/**
 * Returns the names of all counter classes.
 *
 * @returns {string[]}
 */
Counters.prototype.getGroupNames = function () { /* ... */ };

/**
 * Increments multiple counters by their amounts in another Counters instance.
 *
 * @param {Counters} other - The other counters
 *
 * @returns {Counters}
 */
Counters.prototype.incrAllCounters = function (other) { /* ... */ };
```

[hadoop-counters]: http://hadoop.apache.org/docs/r1.0.4/api/org/apache/hadoop/mapreduce/Counters.html
[hadoop-counters-tests]: https://github.com/apigee/lembos/blob/master/src/test/resources/node_modules/HadoopInternalITest-testCounters/index.js
