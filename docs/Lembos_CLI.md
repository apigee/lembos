# Lembos Command Line Interface

**Note:** The JAR packaging of Lembos is configured to have a main class in the JAR manifest.  This is to make using
`hadoop jar` easier and it is not intended for you to use via `java -jar`.  If you do try to execute the JAR using
`java -jar`, it will not work as the JAR does not include the Hadoop classes necessary to run.  To summarize, you
**must** use `hadoop jar` to run Lembos.

The Lembos `runner` is an implementation of the standard [Hadoop Tool][hadoop-tool] interface.  The Hadoop Tool
interface is a simple contract that allows all adhering to it the same capabilities, like configuring your Hadoop job,
Hadoop cluster environment, etc. the same.  So [Hadoop Streaming][hadoop-streaming] and Lembos have a
[common set of command line options][common-cli-options] and the Hadoop tooling uses those the same.  That being said,
Lembos does not deviate or extend the common command line options at this time.  The only real requirement that Lembos
has is that somewhere within the Hadoop Job configuration, two properties need to be set:

* **io.apigee.lembos.mapreduce.moduleName:** This is the Node.js module name that will be used by Lembos
* **io.apigee.lembos.mapreduce.modulePath:** This is the local filesystem path or HDFS URL pointing to your Node.js
module

The way the Hadoop Tool interface works is this can be done a few ways.  You can either pass these properties on the
command line like this:

```
hadoop jar target/lembos-1.0-SNAPSHOT.jar \
  -D io.apigee.lembos.mapreduce.moduleName=wordcount -D io.apigee.lembos.mapreduce.modulePath=examples/wordcount
```

Or you could have those values set in your Hadoop job configuration file and use the `-conf` CLI option.  Either way,
that is the only real requirement of Lembos.  The rest of your Hadoop configuration options and CLI options will be
dependent upon your environment and needs.  Below is a list of generic CLI options supported by Hadoop's tool
tool interface:

* `-conf <configuration file>`: The path to your Hadoop job configuration file
* `-D <property=value>`: Specify job configuration properties
* `-fs <local|namenode:port>`: Specify which namenode to use
* `-jt <local|jobtracker:port>`: Specify which job tracker to use
* `-files <comma separated list of files>`: Comma-separated list of files to be copied to DistributedCache and made
available on each worker node
* `-libjars <comma separated list of jars>`: Comma-separated list of JAR file to be copied to DistributedCache and
added to the runtime classpath
* `-archives <comma separated list of archives>`: Comma-separated list of archive files (.tar.gz, .tgz, .zip) to be
added to DistributedCache and expanded on each worker node

[hadoop-streaming]: http://wiki.apache.org/hadoop/HadoopStreaming
[hadoop-tool]: http://hadoop.apache.org/docs/r1.2.1/api/org/apache/hadoop/util/Tool.html
[common-cli-options]: http://hadoop.apache.org/docs/r1.2.1/api/org/apache/hadoop/util/GenericOptionsParser.html#GenericOptions
