The purpose of the `hadoop-distributed-cache` module is to provide programmatic access to the
[Hadoop DistributedCache][hadoop-distributedcache] object.  Below is a list of exposed module properties.

## DistributedCache

For example usage of all available APIs, please see the [unit tests][hadoop-distributedcache-tests].

```javascript
/**
 * Represents a Hadoop DistributedCache.
 */
var DistributedCache = {};

/**
 * Add an archive path to the current set of classpath entries.
 *
 * @param {string} archivePath - The path of the archive to be added
 * @param {Configuration} conf - The Hadoop configuration
 */
DistributedCache.addArchiveToClassPath = function (archivePath, conf) { /* ... */ };

/**
 * Add an archive to be localized to the configuration.  Intended to be used by user code.
 *
 * @param {string} uri - The URI of the cache to be localized
 * @param {Configuration} conf - The Configuration to add the cache to
 */
DistributedCache.addCacheArchive = function (uri, conf) { /* ... */ };

/**
 * Add a file to be localized to the configuration.  Intended to be used by user code.
 *
 * @param {string} uri - The URI of the cache to be localized
 * @param {Configuration} conf - The Configuration to add the cache to
 */
DistributedCache.addCacheFile = function (uri, conf) { /* ... */ };

/**
 * Add a file path to the current set of classpath entries.  It adds the file to cache as well.  Intended to be used by
 * user code.
 *
 * @param {string} filePath - The path of the file to be added
 * @param {Configuration} conf - The Hadoop configuration
 */
DistributedCache.addFileToClassPath = function (filePath, conf) { /* ... */ };

/**
 * Add an archive that has been localized to the conf.  Used by internal DistributedCache code.
 *
 * @param {Configuration} conf - The Hadoop configuration
 * @param {string} str - The comma separated list of local archives
 */
DistributedCache.addLocalArchives = function (conf, str) { /* ... */ };

/**
 * Add a file that has been localized to the conf.  Used by internal DistributedCache code.
 *
 * @param {Configuration} conf - The Hadoop configuration
 * @param {string} str - The comma separated list of local files
 */
DistributedCache.addLocalFiles = function (conf, str) { /* ... */ };

/**
 * This method checks if there is a conflict in the fragment names of the uris.  Also makes sure that each uri has a
 * fragment.  It is only to be called if you want to create symlinks for the various archives and files.  May be used
 * by user code.
 *
 * @param {string[]} uriFiles - The array of file uris
 * @param {string[]} uriArchives - The array of archive uris
 */
DistributedCache.checkURIs = function (uriFiles, uriArchives) { /* ... */ };

/**
 * This method create symlinks for all files in a given dir in another directory.
 *
 * @param {Configuration} conf - The Hadoop configuration
 * @param {string} jobCacheDir - The target directory for creating symlinks
 * @param {string} workDir - The directory in which the symlinks are created
 *
 * @deprecated
 */
DistributedCache.createAllSymlink = function (conf, jobCacheDir, workDir) { /* ... */ };

/**
 * This method allows you to create symlinks in the current working directory of the task to all the cache
 * files/archives.  Intended to be used by user code.
 *
 * @param {Configuration} conf - The Hadoop configuration
 */
DistributedCache.createSymlink = function (conf) { /* ... */ };

/**
 * Get the archive entries in classpath as an array of strings.  Used by internal DistributedCache code.
 *
 * @param {Configuration} conf - The Hadoop configuration
 *
 * @returns {string[]}
 */
DistributedCache.getArchiveClassPaths = function (conf) { /* ... */ };

/**
 * Get the timestamps of the archives.  Used by internal DistributedCache and MapReduce code.
 *
 * @param {Configuration} conf - The Hadoop configuration
 *
 * @returns {number[]}
 */
DistributedCache.getArchiveTimestamps = function (conf) { /* ... */ };

/**
 * Get cache archives set in the Configuration.  Used by internal DistributedCache and MapReduce code.
 *
 * @param {Configuration} conf - The Hadoop configuration
 *
 * @returns {string[]}
 */
DistributedCache.getCacheArchives = function (conf) { /* ... */ };

/**
 * Get cache files set in the Configuration.  Used by internal DistributedCache and MapReduce code.
 *
 * @param {Configuration} conf - The Hadoop configuration
 *
 * @returns {string[]}
 */
DistributedCache.getCacheFiles = function (conf) { /* ... */ };

/**
 * Get the file entries in classpath as an array of string.  Used by internal DistributedCache code.
 *
 * @param {Configuration} conf - The Hadoop configuration
 *
 * @returns {string[]}
 */
DistributedCache.getFileClassPaths = function (conf) { /* ... */ };

/**
 * Returns FileStatus-like representation of a given cache file on hdfs.  Internal to MapReduce.
 *
 * @param {Configuration} conf - The Hadoop configuration
 * @param {string} cacheUri - The uri to the cachefile
 *
 * @returns {object}
 */
DistributedCache.getFileClassPaths = function (conf, cacheUri) { /* ... */ };

/**
 * Get the timestamps of the files.  Used by internal DistributedCache and MapReduce code.
 *
 * @param {Configuration} conf - The Hadoop configuration
 *
 * @returns {number[]}
 */
DistributedCache.getFileTimestamps = function (conf) { /* ... */ };

/**
 * Return the path array of the localized cache archives.  Intended to be used by user code.
 *
 * @param {Configuration} conf - The Hadoop configuration
 *
 * @returns {string[]}
 */
DistributedCache.getLocalCacheArchives = function (conf) { /* ... */ };

/**
 * Return the path array of the localized cache files.  Intended to be used by user code.
 *
 * @param {Configuration} conf - The Hadoop configuration
 *
 * @returns {string[]}
 */
DistributedCache.getLocalCacheFiles = function (conf) { /* ... */ };

/**
 * This method checks to see if symlinks are to be create for the localized cache files in the current working
 * directory.  Used by internal DistributedCache code.
 *
 * @param {Configuration} conf - The Hadoop configuration
 *
 * @returns {boolean}
 */
DistributedCache.getSymlink = function (conf) { /* ... */ };

/**
 * Returns mtime of a given cache file on hdfs.  Internal to MapReduce.
 *
 * @param {Configuration} conf - The Hadoop configuration
 * @param {string} cacheUri - The ache file URI
 *
 * @returns {number}
 */
DistributedCache.getSymlink = function (conf, cacheUri) { /* ... */ };

/**
 * This is to check the timestamp of the archives to be localized.  Used by internal MapReduce code.
 *
 * @param {Configuration} conf - The Hadoop configuration
 * @param {string} timestamps - The comma separated list of timestamps of archives.  The order should be the same as
 *                              the order in which the archives are added.
 */
DistributedCache.setArchiveTimestamps = function (conf, timestamps) { /* ... */ };

/**
 * Set the configuration with the given set of archives.  Intended to be used by user code.
 *
 * @param {string[]} archives - The list of archives that need to be localized
 * @param {Configuration} conf - The Hadoop configuration
 */
DistributedCache.setCacheArchives = function (archives, conf) { /* ... */ };

/**
 * Set the configuration with the given set of files.  Intended to be used by user code.
 *
 * @param {string[]} files - The list of files that need to be localized
 * @param {Configuration} conf - The Hadoop configuration
 */
DistributedCache.setCacheArchives = function (files, conf) { /* ... */ };

/**
 * This is to check the timestamp of the files to be localized.  Used by internal MapReduce code.
 *
 * @param {Configuration} conf - The Hadoop configuration
 * @param {string} timestamps - The comma separated list of timestamps of files.  The order should be the same as
 *                              the order in which the archives are added.
 */
DistributedCache.setFileTimestamps = function (conf, timestamps) { /* ... */ };

/**
 * Set the conf to contain the location for localized archives.  Used by internal DistributedCache code.
 *
 * @param {Configuration} conf - The Hadoop configuration
 * @param @param {string} str - The comma separated list of local archives
 */
DistributedCache.setLocalArchives = function (conf, str) { /* ... */ };

/**
 * Set the conf to contain the location for localized files.  Used by internal DistributedCache code.
 *
 * @param {Configuration} conf - The Hadoop configuration
 * @param @param {string} str - The comma separated list of local files
 */
DistributedCache.setLocalFiles = function (conf, str) { /* ... */ };
```

[hadoop-distributedcache]: http://hadoop.apache.org/docs/r1.0.4/api/org/apache/hadoop/filecache/DistributedCache.html
[hadoop-distributedcache-tests]: https://github.com/apigee/lembos/blob/master/src/test/resources/node_modules/HadoopDistributedCacheTest-testHadoopDistributedCache/index.js
