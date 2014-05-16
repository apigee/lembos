Lembos a Java-based library that provides an ecosystem allowing you to write your [MapReduce](mapreduce) jobs using
[Node.js](nodejs) and have them run natively within [Hadoop](hadoop) as if written using [Java](java).

# Architecture

Lembos is composed of two parts:

* **The *Runner*:** This is the main class that will construct your Hadoop MapReduce job, configure it and submit it
* **The *Runtime*:** These are the MapReduce components that are used by Hadoop to *drive* your Node.js MapReduce job

Each of these will be described in greater detail below but before we do, it is worth mentioning that all of this is
possible thanks to [Trireme](trireme).  Trireme is what provides the Node.js runtime within the JVM allowing you to
write your MapReduce jobs as Node.js modules and have Hadoop drive the MapReduce lifecycle as if you had written your
job using Java.  Let's get into the details so some of this becomes more clear.

## The Runner

The Lembos runner is an implementation of the standard [Hadoop Tool interface](hadoop-tool).  Hadoop's Tool interface
provides us with a standard set of [CLI options](hadoop-tool-options) that Hadoop will use to handle common tool
features like classpath handling, cluster configuration, [DistributedCache](hadoop-distributed-cache) integration, etc.
By using the Hadoop Tool interface Lembos did not have to reinvent the wheel and should provide a familiar interface for
those that have experience with Hadoop tooling already.

The Lembos runner, once invoked, has a lifecycle.  The gist of the Lembos runner is to take your Node.js module, load
it, construct a MapReduce job based on your Node.js based job definition, package up your Node.js module and submit the
job.  To get a better idea of how this works, below is the list of lifecycle steps:

1. Create a copy of your Node.js module in a temporary location
    1. You can specify an [HDFS](hdfs) URL and it will be downloaded
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

This process is performed for each call to the Java-based MapReduce component wrapper.  Below are more details about
of these steps.

### Input/Output Type Converters

Type converters are used to make sure that when Hadoop gives your Job a Java-based
input, the input is converted to the appropriate JavaScript type.  The same goes for when your JavaScript code emits
values back to Hadoop, the converters are used to take the JavaScript output and convert it to the appropriate Java
type.  Out of the box, all major input/output types are handled for you but there is also a Java-based plugin system you
could latch into if/when necessary. **(Add link to documentation here)**

### Calling Your Node.js Job Function

MapReduce components operate in a synchronous fashion.  When Hadoop iterates over the Mapper input key/value pairs, it
does not process a key/value pair until the previous pair was fully processed.  JavaScript on the other hand allows for
asynchronous operation and must be supported.  To support this, the Lembos runtime will call every Node.js function with
a callback and will wait for that callback to be called before allowing the Java side of things to proceed.  This means
your JavaScript code can do anything it wants, synchronous or not, using the same conventions common in Node.js
development.

At this point you should have a high level idea of how Lembos works but up to this point, it's all been explained
without example.  The following sections will tell you how to obtain and use Lembos.

**(Add documentation here)**

[hadoop]: http://hadoop.apache.org/
[hadoop-distributed-cache]: http://hadoop.apache.org/docs/r1.2.1/api/org/apache/hadoop/filecache/DistributedCache.html
[hadoop-tool]: http://hadoop.apache.org/docs/r1.2.1/api/org/apache/hadoop/util/Tool.html
[hadoop-tool-options]: http://hadoop.apache.org/docs/r1.2.1/api/org/apache/hadoop/util/GenericOptionsParser.html#GenericOptions
[hdfs]: http://wiki.apache.org/hadoop/HDFS
[java]: http://www.oracle.com/us/technologies/java/overview/index.html
[lembos]: http://en.wikipedia.org/wiki/Hellenistic-era_warships#Lembos
[mapreduce]: http://en.wikipedia.org/wiki/MapReduce
[nodejs]: http://nodejs.org
[trireme]: https://github.com/apigee/trireme
