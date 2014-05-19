# Anatomy of a Node.js MapReduce Job

The interface Lembos requires for your Node.js module is that it must export an object and that object must contain at
least a `map` function.  *(While `map` is the only required function to be exported, it is highly likely that you will
need to expose a `jobSetup` function as well as this is how you will programmatically configure your job.)*  Beyond
that, your module can be structured however you want.

## Supported MapReduce Components

A typical Java-based MapReduce job is composed of at minimium a Mapper but there are also many other components that
Hadoop allows you to implement.  At this point, Lembos supports the following MapReduce components, all of which are
documented below in the API Reference:

* Combiner
* GroupComparator
* Mapper
* Partitioner
* Reducer
* SortComparator

## Node.js MapReduce Job API Reference

Below is a complete reference of all supported job properties, their type and if they are functions, their signature.

```javascript
// The module must export an object with the naming described below
module.exports = {

  // This is a list of key/value pairs where the key is a string and the value
  // is any value the Hadoop Configuration allows via a setXXX method.
  config: {},

  /**
   * Called when the job has finished running.
   *
   * @param {object} job - Hadoop Job wrapper
   * @param {function} cb - Completed callback
   */
  jobCleanup: function (job, cb) { /* ... */ },

  /**
   * Called when the job is being setup.
   *
   * Note: This is called before Lembos configures the job components (combiner, mapper, etc.)
   *
   * @param {object} job - Hadoop Job wrapper
   * @param {function} cb - Completed callback
   */
  jobSetup: function (job, cb) { /* ... */ },

  /**
   * Called when the mapper's cleanup function is called by Hadoop.
   *
   * @param {object} context - Hadoop TaskInputOutputContext wrapper
   * @param {function} cb - Completed callback
   */
  mapCleanup: function (context, cb) { /* ... */ },

  /**
   * Called when the mapper's map function is called by Hadoop.
   *
   * @param {?} key - Map key (Converted WritableComparable to JavaScript)
   * @param {?} value - Map value (Converted Writable to JavaScript)
   * @param {object} context - Hadoop TaskInputOutputContext wrapper
   * @param {function} cb - Completed callback
   */
  map: function (key, value, context, cb) { /* ... */ },

  /**
   * Called when the mapper's setup function is called by Hadoop.
   *
   * @param {object} context - Hadoop TaskInputOutputContext wrapper
   * @param {function} cb - Completed callback
   */
  mapSetup: function (context, cb) { /* ... */ },

  /**
   * Called when the reducer's cleanup function is called by Hadoop.
   *
   * @param {object} context - Hadoop TaskInputOutputContext wrapper
   * @param {function} - Completed callback
   */
  reduceCleanup: function (context, cb) { /* ... */ },

  /**
   * Called when the reducer's reduce function is called by Hadoop.
   *
   * @param {?} key - Reduce key (Converted WritableComparable to JavaScript)
   * @param {object} values - Reduce values (Iterator)
   * @param {object} context - Hadoop TaskInputOutputContext wrapper
   * @param {function} cb - Completed callback
   */
  reduce: function (key, values, context, cb) { /* ... */ },

  /**
   * Called when the reducer's setup function is called by Hadoop.
   *
   * @param {object} context - Hadoop TaskInputOutputContext wrapper
   * @param {function} cb - Completed callback
   */
  reduceSetup: function (context, cb) { /* ... */ },

  /**
   * Called when the combiner's cleanup function is called by Hadoop.
   *
   * @param {object} context - Hadoop TaskInputOutputContext wrapper
   * @param {function} cb - Completed callback
   */
  combineCleanup: function (context, cb) { /* ... */ },

  /**
   * Called when the combiner's combine function is called by Hadoop.
   *
   * @param {?} key - Combine key (Converted WritableComparable to JavaScript)
   * @param {object} values - Combine values (Iterator)
   * @param {object} context - Hadoop TaskInputOutputContext wrapper
   * @param {function} cb - Completed callback
   */
  combine: function (key, values, context, cb) { /* ... */ },

  /**
   * Called when the combiner's setup function is called by Hadoop.
   *
   * @param {object} context - Hadoop TaskInputOutputContext wrapper
   * @param {function} cb - Completed callback
   */
  combineSetup: function (context, cb) { /* ... */ },

  /**
   * Called when the partitioner's close function is called by Hadoop.
   *
   * @param {object} context - Hadoop TaskInputOutputContext wrapper
   * @param {function} cb - Completed callback
   */
  partitionCleanup: function (conf, cb) { /* ... */ },

  /**
   * Called when the partitioner's partition function is called by Hadoop.
   *
   * @param {?} key - Partition key (Converted WritableComparable to JavaScript)
   * @param {?} value - Partition value (Converted Writable to JavaScript)
   * @param {number} numPartitions - The number of partitions (integer)
   * @param {function} - Completed callback
   *
   * @return {number} the partition as an integer
   */
  partition: function (key, value, numPartitions, cb) { /* ... */ },

  /**
   * Called when the partitioner's partition function is first called by Hadoop.
   *
   * @param {object} conf - Hadoop Configuration wrapper
   * @param {function} cb - Completed callback
   */
  partitionSetup: function (conf, cb) { /* ... */ },

  /**
   * Called when the group sorter's close function is called by Hadoop.
   *
   * @param {object} conf - Hadoop Configuration wrapper
   * @param {function} cb - Completed callback
   */
  groupCleanup: function (conf, cb) {
    cb();
  },

  /**
   * Called when the group sorter's compare function is called by Hadoop.
   *
   * @param {?} key1 - The first key (Converted WritableComparable to JavaScript)
   * @param {?} key2 - The second key (ConvertedWritableComparable to JavaScript)
   * @param {function} - Completed callback
   *
   * @return {number} the partition as an integer
   */
  group: function (key1, key2, cb) { /* ... */ },

  /**
   * Called when the group sorter's compare function is first called by Hadoop.
   *
   * @param {object} conf - Hadoop Configuration wrapper
   * @param {function} cb - Completed callback
   */
  groupSetup: function (conf, cb) { /* ... */ },

  /**
   * Called when the secondary sorter's close function is called by Hadoop.
   *
   * @param {object} conf - Hadoop Configuration wrapper
   * @param {function} cb - Completed callback
   */
  sortCleanup: function (conf, cb) { /* ... */ },

  /**
   * Called when the secondary sorter's compare function is called by Hadoop.
   *
   * @param {?} key1 - The first key (Converted WritableComparable to JavaScript)
   * @param {?} key2 - The second key (Converted WritableComparable to JavaScript)
   * @param {function} - Completed callback
   *
   * @return {number} the partition as an integer
   */
  sort: function (key1, key2, cb) { /* ... */ },

  /**
   * Called when the secondary sorter's compare function is first called by Hadoop.
   *
   * @param {object} conf - Hadoop Configuration wrapper
   * @param {function} cb - Completed callback
   */
  sortSetup: function (conf, cb) { /* ... */ }
};
```