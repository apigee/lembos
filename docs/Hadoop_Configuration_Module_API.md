The purpose of the `hadoop-configuration` module is to provide programmatic access to the
[Hadoop Configuration][hadoop-configuration] object.  Below is a list of exposed module properties.

## Configuration

```javascript
/**
 * Represents a Hadoop Configuration.
 *
 * @param {(boolean|Configuration)} [boolOrConf] - If boolean, indicates whether or not to load default resources.  If
 *                                                 it is a Configuration object, clone the passed in Configuration.
 *
 * @constructor Configuration
 */
var Configuration = function (boolOrConf) { /* ... */ };

/**
 * Add a default resource. Resources are loaded in the order of the resources added.
 *
 * @param {string} name - The file name, file should be present in the classpath.
 */
Configuration.addDefaultResource = function (name) { /* ... */ };

/**
 * Add a configuration resource. The properties of this resource will override properties of previously added
 * resources, unless they were marked final.
 *
 * @param {string} nameOrURLOrFile - The classpath name, the URL to the resource or the filesystem path to the
 *                                   resource.
 *
 * @returns {Configuration} this
 */
Configuration.prototype.addResource = function (nameOrURLOrFile) { /* ... */ };

/**
 * Clears all keys from the configuration.
 *
 * @returns {Configuration} this
 */
Configuration.prototype.clear = function () { /* ... */ };

/**
 * Get the value of the named property as a string.
 *
 * @param {string} propName - The name of the property
 * @param {string} [defaultValue] - The default value to return if there is no property with the name provided
 *
 * @returns {string}
 */
Configuration.prototype.get = function (propName, defaultValue) { /* ... */ };

/**
 * Get the value of the named property as a boolean.
 *
 * @param {string} propName - The name of the property
 * @param {boolean} defaultValue - The default value to return if there is no property with the name provided
 *
 * @returns {boolean}
 */
Configuration.prototype.getBoolean = function (propName, defaultValue) { /* ... */ };

/**
 * Get the value of the named property as a float.
 *
 * @param {string} propName - The name of the property
 * @param {number} defaultValue - The default value to return if there is no property with the name provided
 *
 * @returns {number}
 */
Configuration.prototype.getFloat = function (propName, defaultValue) { /* ... */ };

/**
 * Get the value of the named property as a integer.
 *
 * @param {string} propName - The name of the property
 * @param {number} defaultValue - The default value to return if there is no property with the name provided
 *
 * @returns {number}
 */
Configuration.prototype.getInt = function (propName, defaultValue) { /* ... */ };

/**
 * Get the value of the named property as a long.
 *
 * @param {string} propName - The name of the property
 * @param {number} defaultValue - The default value to return if there is no property with the name provided
 *
 * @returns {number}
 */
Configuration.prototype.getLong = function (propName, defaultValue) { /* ... */ };

/**
 * Get the value of the named property without variable expansion.
 *
 * @param {string} propName - The name of the property
 * @param {number} defaultValue - The default value to return if there is no property with the name provided
 *
 * @returns {number}
 */
Configuration.prototype.getInt = function (propName, defaultValue) { /* ... */ };

/**
 * Get the value of the named property without variable expansion.
 *
 * @param {string} propName - The name of the property
 * @param {string} defaultValue - The default value to return if there is no property with the name provided
 *
 * @returns {string}
 */
Configuration.prototype.getRaw = function (propName, defaultValue) { /* ... */ };

/**
 * Get the comma delimited values of the named property as an array of strings.
 *
 * @param {string} propName - The name of the property
 *
 * @returns {string[]}
 */
Configuration.prototype.getStringCollection = function (propName) { /* ... */ };

/**
 * Get the comma delimited values of the named property as an array of strings.
 *
 * @param {string} propName - The name of the property
 * @param {string} [defaultValue] - The default value to return if there is no property with the name provided
 *
 * @returns {string[]}
 */
Configuration.prototype.getStrings = function (propName, defaultValue) { /* ... */ };

/**
 * Get keys matching the the regex, with values.
 *
 * @param {regexp} regex - The regex to match configuration property names
 *
 * @returns {object}
 */
Configuration.prototype.getValByRegex = function (propName) { /* ... */ };

/**
 * Reload configuration from previously added resources.
 *
 * @returns {Configuration} this
 */
Configuration.prototype.reloadConfiguration = function () { /* ... */ };

/**
 * Set the value of the named property.
 *
 * @param {string} propName - The name of the property
 * @param {string} propValue - The value of the property
 *
 * @returns {Configuration} this
 */
Configuration.prototype.set = function (propName, propValue) { /* ... */ };

/**
 * Set the value of the named property to a boolean.
 *
 * @param {string} propName - The name of the property
 * @param {boolean} propValue - The value of the property
 *
 * @returns {Configuration} this
 */
Configuration.prototype.setBoolean = function (propName, propValue) { /* ... */ };

/**
 * Set the value of the named property to a boolean if it is not already set.
 *
 * @param {string} propName - The name of the property
 * @param {boolean} propValue - The value of the property
 *
 * @returns {Configuration} this
 */
Configuration.prototype.setBooleanIfUnset = function (propName, propValue) { /* ... */ };

/**
 * Set the value of the named property to the name of the class implementing the given interface.
 *
 * @param {string} propName - The name of the property
 * @param {string} theClass - The class name
 * @param {string} theInterface - The interface name
 *
 * @returns {Configuration} this
 */
Configuration.prototype.setClass = function (propName, theClass, theInterface) { /* ... */ };

/**
 * Set the value of the named property to a float.
 *
 * @param {string} propName - The name of the property
 * @param {number} propValue - The value of the property
 *
 * @returns {Configuration} this
 */
Configuration.prototype.setFloat = function (propName, propValue) { /* ... */ };

/**
 * Set the value of the named property if it is not already set.
 *
 * @param {string} propName - The name of the property
 * @param {string} propValue - The value of the property
 *
 * @returns {Configuration} this
 */
Configuration.prototype.setIfUnset = function (propName, propValue) { /* ... */ };

/**
 * Set the value of the named property to an integer.
 *
 * @param {string} propName - The name of the property
 * @param {number} propValue - The value of the property
 *
 * @returns {Configuration} this
 */
Configuration.prototype.setInt = function (propName, propValue) { /* ... */ };

/**
 * Set the value of the named property to a long.
 *
 * @param {string} propName - The name of the property
 * @param {number} propValue - The value of the property
 *
 * @returns {Configuration} this
 */
Configuration.prototype.setLong = function (propName, propValue) { /* ... */ };

/**
 * Set the quietness-mode.
 *
 * @param {boolean} quietMode - The quiet mode
 *
 * @returns {Configuration} this
 */
Configuration.prototype.setQuietMode = function (quietMode) { /* ... */ };

/**
 * Set the array of string values for the named property as as comma delimited values.
 *
 * @param {string} propName - The name of the property
 * @param {array} propValues - The value of the property
 *
 * @returns {Configuration} this
 */
Configuration.prototype.setStrings = function (propName, propValues) { /* ... */ };

/**
 * Return the number of keys in the configuration.
 *
 * @returns {number}
 */
Configuration.prototype.size = function () { /* ... */ };
```

[hadoop-configuration]: http://hadoop.apache.org/docs/r1.0.4/api/org/apache/hadoop/conf/Configuration.html
