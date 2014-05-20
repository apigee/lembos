# Lembos Architecture

Lembos is composed of two parts:

* **The *Runner*:** This is the main class that will construct your Hadoop MapReduce job, configure it and submit it
* **The *Runtime*:** These are the MapReduce components that are used by Hadoop to *drive* your Node.js MapReduce job

Each of these will be described in greater detail below but before we do, it is worth mentioning that all of this is
possible thanks to [Trireme][trireme].  Trireme is what provides the Node.js runtime within the JVM allowing you to
write your MapReduce jobs as Node.js modules and have Hadoop drive the MapReduce lifecycle as if you had written your
job using Java.  Let's get into the details so some of this becomes more clear.

## The Runner

The Lembos runner is an implementation of the standard Hadoop Tool interface.  Hadoop's Tool interface provides us with
a standard set of CLI options that Hadoop will use to handle common tool features like classpath handling, cluster
configuration, DistributedCache integration, etc.  By using the Hadoop Tool interface Lembos did not have to reinvent
the wheel and should provide a familiar interface for those that have experience with Hadoop tooling already.

The Lembos runner, once invoked, has a lifecycle.  The gist of the Lembos runner is to take your Node.js module, load
it, construct a MapReduce job based on your Node.js based job definition, package up your Node.js module and submit the
job.  To get a better idea of how this works, below is the list of lifecycle steps:

1. Create a copy of your Node.js module in a temporary location
    1. You can specify an HDFS URL and it will be downloaded
2. If necessary, create a custom ClassLoader based on the existence of the `-libjars` CLI option
3. Load your Node.js module into the JVM
4. Create a ZIP file of your Node.js module
5. Add the ZIP file to the DistributedCache as an archive
6. Create the Hadoop job and configure it based on the job definition in your Node.js module
7. If necessary, call the `jobSetup` method of your Node.js job definition
8. Submit the Hadoop job
9. If necessary, call the `jobCleanup` method of your Node.js job definition

The Lembos runner provides the necessary orchestration for taking your Node.js job definition, packaging it up,
configuring the Hadoop job accordingly and then submitting the job to the Hadoop cluster.  Using the facilities
provided by Hadoop, the Lembos runner has a familiar interface and should be familiar to those of you that have used
Hadoop tools before.  Now that we know the purpose of the Lembos runner and have a high level idea of how it works,
let's look at the runtime.

**Note:** At this point there might understandably be questions about how the Lembos runner uses your Node.js module to
configure the Hadoop job.  The details about the interface your Node.js module must adhere to will be discussed below.
**(Add link to documentation here)**

## The Runtime

While the Lembos runner provides critical value in orchestrating the job configuration and submission, the Lembos
Runtime is where the real work happens.  Once a MapReduce job has been submitted, the MapReduce components come into
play.  The lifecycle of the Java-based MapReduce component wrapper is as follows:

1. A Java-based MapReduce component wrapper is instantiated by Hadoop *(Mapper, Reducer, ...)*
2. Load your Node.js module into the JVM using DistributedCache
3. If your Node.js job defines a setup handler for the component, it is called  *(For example: A `Mapper` component will
look for a `mapSetup` function to be defined in your Node.js job and if it is defined, the function will be called.)*
4. The component wrapper works as it normally does, processing the inputs Hadoop gives it *(More details on exactly how
this works below)*
5. If your Node.js job defines a cleanup handler for the component, it is called  *(For example: A `Mapper` component
will look for a `mapCleanup` function to be defined in your Node.js job and if it is defined, the function will be
called.)*

Step `#4` above is extremely important here and requires a little more detail.  Whenever Hadoop calls the Java-based
method corresponding to the MapReduce component, the Lembos runtime performs the following steps:

1. Given the Java inputs, a Java -> JavaScript converter will convert the Java input value to a JavaScript equivalent
2. Call the corresponding Node.js job method with the appropriately converted values and a callback
3. Await the callback to be called

## But Wait, There's More

There were a number of things mentioned above that need more detail, like input/output type conversion and how
asynchronous code is handled.  There are also some things that have not yet been mentioned like how Hadoop APIs are
accessed from within your Node.js module.  Below is the information necessary to fill in the gaps.

### Command Line Arguments

Sometimes you will want to pass arguments/options to your Node.js code and Lembos handles this very simply.  The usage
pattern for running Lembos is using `hadoop jar {LEMBOS_JAR} [hadoop options] [application options]`.  If you adhere to
this model, anything that is not a Hadoop argument will be passed to Node.js so that you can use `process.argv` just as
you would had you invoked your Node.js code from the command line.  So for example, if I were to run Lembos using this:

`hadoop jar target/lembos-1.1-SNAPSHOT.jar \
  -D io.apigee.lembos.mapreduce.moduleName=wordcount \
  -D io.apigee.lembos.mapreduce.modulePath=examples/wordcount \
  -flag0 --opt0 val0 arg0
`

`process.argv` would look like this:

`
[ './node',
  'Lembos-Node-Wrapper-3618924926057998904.js',
  '-flag0',
  '--opt0',
  'val0',
  'arg0' ]
`

This means you can use existing Node.js patterns and tooling, like [minimist][minimist], to process your CLI arguments.

**Note:** We create a wrapper script that will import your module and the naming convention for that file is
`Lembos-Node-Wrapper-{timestamp_job_created}.js` so the second array entry for `process.argv` will be similar but not
the same.

### Handling Asynchronous JavaScript Code

MapReduce components operate in a synchronous fashion.  When a Hadoop Mapper is processing data, it will fully process
an input key/value pair before moving on to the next input key/value pair. JavaScript on the other hand allows for
asynchronous operation and Lembos supports this by making sure all JavaScript code called from Java is handed a
callback.  So for example in Java the method signature for the `map` function looks like this:

```java
// ...
protected void map(final WritableComparable key, final Writable value, final Context context)
    throws IOException, InterruptedException {
    // ...
}
// ...
```

So in Java land, there are three arguments passed to the `map` function.  The equivalent in JavaScript looks like this:

```javascript
// ...
map: function (key, value, context, callback) {
  // ...
  callback();
}
// ...
```

This is how it works for all MapReduce components that Lembos supports.  The importance of this callback is that in
Java land, we will wait until your JavaScript code calls the callback before allowing Hadoop to move on to the next
input key/value pair.  Doing things like this allows Hadoop to operate as it expects (synchronous) without limiting
your JavaScript code and allowing it to be synchronous or asynchronous based on its needs.

### Input/Output Type Converters

Type converters are used to make sure that when Hadoop gives your Job a Java-based
input, the input is converted to the appropriate JavaScript type.  The same goes for when your JavaScript code emits
values back to Hadoop, the converters are used to take the JavaScript output and convert it to the appropriate Java
type.  Hadoop has a number of input/output types and for all of the ones that ship with Hadoop and can be mapped to
a JavaScript type, Lembos provides implementations already.  Chances are good you will not have to worry about this
as it happens behind the scenes.  Just know that when values get passed from Java to JavaScript, they will be
JavaScript values and not Java values.  *(For example: If my job is passed a LongWritable as a key and a Text as a
value, the values passed to JavaScript will be a Number and a String.)*

For more details on what input/output type conversion is provided out of the box, look at the sources below:

* **input (Java -> JavaScript):**[src/main/java/io/apigee/lembos/mapreduce/converters/input][input-src]
* **output (JavaScript -> Java):**[src/main/java/io/apigee/lembos/mapreduce/converters/output][output-src]

**TODO:** Add documentation on how to write a custom input/output type converter

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
[input-src]: https://github.com/apigee/lembos/tree/master/src/main/java/io/apigee/lembos/mapreduce/converters/input
[java-iterator-api]: https://github.com/apigee/lembos/blob/master/docs/types/Java_Iterator_API.md
[minimist]: https://github.com/substack/minimist
[output-src]: https://github.com/apigee/lembos/tree/master/src/main/java/io/apigee/lembos/mapreduce/converters/output
[trireme]: https://github.com/apigee/trireme