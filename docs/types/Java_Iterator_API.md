The purpose of the `Iterator` object is to provide programmatic access to the [Java Iterator][java-iterator] object.
Below is a list of exposed object properties.

## Iterator

For example usage of all available APIs, please see the [unit tests][java-iterator-tests].

```javascript
/* No public constructor. */

/**
 * Returns true if the iteration has more elements.
 *
 * @returns {boolean}
 */
Iterator.prototype.hasNext = function () { /* ... */ };

/**
 * Returns the next element in the iteration.
 *
 * @returns {?}
 */
Iterator.prototype.next = function () { /* ... */ };

/**
 * Removes the current element from the underlying collection.
 */
Iterator.prototype.remove = function () { /* ... */ };
```

[java-iterator]: http://docs.oracle.com/javase/7/docs/api/java/util/Iterator.html
[java-iterator-tests]: https://github.com/apigee/lembos/blob/master/src/test/resources/node_modules/LembosReducerTest-testReducer/index.js
