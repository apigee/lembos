### JavaScript Hadoop Modules

Lembos provides a number of Node.js modules that basically provide access to Hadoop types/APIs from within your Node.js
module.  For example, there is a `hadoop-input` module that provides access to the supported Hadoop input format types
and their appropriate APIs.  Below is a list of Node.js modules provided to you by Lembos:

* `hadoop-configuration`: Provides access to the Hadoop Configuration object
*([API documentation][hadoop-configuration-api])*
* `hadoop-distributed-cache`: Provides access to the Hadoop DistributedCache object
*([API documentation][hadoop-distributed-cache-api])*
* `hadoop-input`: Provides access to the various Hadoop InputFormat implementation objects
*([API documentation][hadoop-input-api])*
* `hadoop-job`: Provides access to the Hadoop Job object *([API documentation][hadoop-job-api])*
* `hadoop-output`: Provides access to the various Hadoop OutputFormat implementation objects
*([API documentation][hadoop-output-api])*

The first pass at these APIs was to provide a one-to-one mapping of Java-based APIs to JavaScript as that will be allow
for consistency between Java MapReduce jobs and Node.js MapReduce jobs.  Going forward we would like to create
abstractions and APIs that are more JavaScript/Node.js oriented.

It is important to note that at this time, you can only access the modules when using the Java-based Lembos runner.
We are currently working on figuring out a way to access these modules in a pure JavaScript Node.js module without
Lembos for unit testing and such.

### JavaScript Hadoop Types

Lembos provides a few Node.js types that provide access to Hadoop types.  For example, when your `map` function is
called, a `TaskInputOutputContext` type is passed or whenever you call `job.getCounters` you are returned a `Counters`
object.  These objects are not instantiable and are created/provided by the underlying Hadoop APIs.  Below is a list of
the Node.js types provided to you by Lembos that are referenced throughout various places in the API documentation but
are not available from a module:

* `Counter` *([API documentation][hadoop-counter-api])*
* `CounterGroup` *([API documentation][hadoop-countergroup-api])*
* `Counters` *([API documentation][hadoop-counters-api])*
* `Iterator` *([API documentation][java-iterator-api])*
* `TaskInputOutputContext` *([API documentation][hadoop-taskinputoutputcontext-api])*

[hadoop-counter-api]: https://github.com/apigee/lembos/blob/master/docs/types/Hadoop_Counter_API.md
[hadoop-countergroup-api]: https://github.com/apigee/lembos/blob/master/docs/types/Hadoop_CounterGroup_API.md
[hadoop-counters-api]: https://github.com/apigee/lembos/blob/master/docs/types/Hadoop_Counters_API.md
[hadoop-configuration-api]: https://github.com/apigee/lembos/blob/master/docs/modules/Hadoop_Configuration_API.md
[hadoop-distributed-cache-api]: https://github.com/apigee/lembos/blob/master/docs/modules/Hadoop_Distributed_Cache_API.md
[hadoop-input-api]: https://github.com/apigee/lembos/blob/master/docs/modules/Hadoop_Input_API.md
[hadoop-job-api]: https://github.com/apigee/lembos/blob/master/docs/modules/Hadoop_Job_API.md
[hadoop-output-api]: https://github.com/apigee/lembos/blob/master/docs/modules/Hadoop_Output_API.md
[hadoop-taskinputoutputcontext-api]: https://github.com/apigee/lembos/blob/master/docs/types/Hadoop_TaskInputOutputContext_API.md
[java-iterator-api]: https://github.com/apigee/lembos/blob/master/docs/types/Java_Iterator_API.md
