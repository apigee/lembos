Lembos a Java-based library that provides an ecosystem allowing you to write your [MapReduce][mapreduce] jobs using
Node.js and have them run natively within Hadoop as if written using Java.  The project is packaged as a JAR file
that is intended to be ran via `hadoop jar` just as you would had you written your MapReduce job in Java.  *(Note:
There is an option to build a standalone executable JAR but that would require you to package up the JAR with the
necessary Hadoop JARs in a [shaded][shade] JAR.  Since the build would be Hadoop version specific, we're not
building those right now.  For more details on how to do this, read the **Getting Started (Developer)**
documentation below.)*

## Getting Started (User)

First things first, we need to get a build of Lembos.  You can do this by downloading the
[latest release][releases] or by cloning the project and building it yourself.  Once you have a Lembos build and you
have built your [Node.js MapReduce Job][anatomy-of-a-job], we need to use `hadoop jar` to run the Lembos runner.
*(For those of you not familiar with running `hadoop jar`, here is its usage: `hadoop jar <jar> [mainClass] args...`)*
Below is an example of how to run the word count example shipped with Lembos:

```
hadoop jar target/lembos-1.0-SNAPSHOT.jar io.apigee.lembos.mapreduce.LembosMapReduceRunner \
  -D io.apigee.lembos.mapreduce.moduleName=wordcount \
  -D io.apigee.lembos.mapreduce.modulePath=examples/wordcount
```

It's that simple but before moving on, let's make sure we know what is going on here.  The Lembos runner, and runtime,
use the two Hadoop configuration properties above.  They are required an there is no way about it.
`io.apigee.lembos.mapreduce.moduleName` is used to specify the name of the module, used by the Lembos runtime to load
your module, and `io.apigee.lembos.mapreduce.modulePath` is used to specify the path to your module.  *(Note: The path
can be an HDFS URL or a local filesystem path.)*  At this point, the Lembos runner will load your module and based on
your job definition, the job will be configured and submitted to the Hadoop cluster.

## Getting Started (Developer)

If you are interestd in contributing to Lembos or you need to build against Lembos, the process is pretty straight
forward.  As with any GitHub project, just clone the project.  Here is the full Git command to save you some trouble:
`git clone https://github.com/apigee/lembos.git`.  At this point, you'll notice that we're using [Maven][maven] for our
development process.  As with most Maven projects, the commands are pretty standard:

* `mvn test`: Run the unit tests *(Does not requiring a running Hadoop instance)*
* `mvn integration-test`: Run the integration tests *(Requires a running Hadoop instance to connect to)*
* `mvn site`: Build the Maven project documentation *(Most useful when ran after `mvn test` or `mvn integration-test`
as the code coverage reports will be built)*
* `mvn package`: Build a JAR file of Lembos

If you're building against Lembos, the project is submitted to Maven central so you just need to update your `pom.xml`
to have something like this in your dependencies:

```xml
<!-- ... -->
<dependency>
  <groupId>io.apigee.lembos</groupId>
  <artifactId>lembos</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
<!-- ... -->
```

## Further Reading

While running the Lembos runner is quite simple, there is a lot going on behind the scenes that is not explained above.
Most of this is related to how Hadoop works and we've documented the important stuff.  Below is a list of useful
resources that can help explain what is going on:

* [Anatomy of a Node.js MapReduce Job][anatomy-of-a-job]
* [Lembos Architecture][lembos-architecture]
* [Lembos Command Line Interface][lembos-cli]

[anatomy-of-a-job]: https://github.com/apigee/lembos/blob/master/docs/Anatomy_of_a_NodeJS_MapReduce_Job.md
[releases]: https://github.com/apigee/lembos/releases
[lembos-architecture]: https://github.com/apigee/lembos/blob/master/docs/Lembos_Architecture.md
[lembos-cli]: https://github.com/apigee/lembos/blob/master/docs/Lembos_CLI.md
[mapreduce]: http://en.wikipedia.org/wiki/MapReduce
[maven]: http://maven.apache.org/
[shade]: http://maven.apache.org/plugins/maven-shade-plugin/
