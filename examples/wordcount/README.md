This is the ubiquitous Hadoop Word Count example but written in the Node.js equivalent.  (For details on how this might
look in Java, take a look at the [Wordcount Example][mapreduce-wordcount-wiki] on the Hadoop Wiki.)  Below are
instructions on how to run this example.

**Note:** All commands are written assuming you are in the lembos source root.

## Compile/Package Lembos

trieme-mapreduce uses [Maven][maven] as its build system so to build a binary of lembos, run the following:
`mvn clean package`.  This should create a file called `lembos-{LEMBOS_VERSION}.jar`.

## Create Sample Data

We already have an sample file to run this example against.  To put the file into HDFS, run the following:
`hadoop dfs -copyFromLocal examples/wordcount/4300.txt /tmp/wordcount/4300.txt`

Feel free to update your version of the command above to work against whatever HDFS URL/path you want.

## Running the Example

To run the example, we have to tell lembos what the name of our Node.js module is and where to find it
locally.  To do this, run the following:
`hadoop jar target/lembos-{LEMBOS_VERSION}.jar \
-D io.apigee.lembos.mapreduce.moduleName=wordcount \
-D io.apigee.lembos.mapreduce.modulePath={FULL_PATH_TO_LEMBOS_SOURCE_ROOT}/examples/wordcount/`

If you changed the HDFS URL/path in the previous step, update this step accordingly by passing
`-D mapred.input.dir={PATH_YOU_USED_ABOVE}` and/or `-D mapred.output.dir={PATH_YOU_USED_ABOVE}`.

**Note:** The default mode for a MapReduce job is `local`.  When this is the case, the job is ran in a single process
and things like DistributedCache do not work.  If you want to run against a cluster, or in pseudo-distributed mode, to
run in a more traditional way, set the `mapred.job.tracker` property in your job configuration or pass the `-jt` CLI
option.

For more detail on the available CLI arguments/options, please see the [GenericOptions][generic-options] documentation
or run with the `--help` flag.

## Inspect the Results

Run the following to page through the results: `hadoop dfs -cat /tmp/wordcount/uniques/part-r-00000 | less`.

You can even run the following to see them sorted in descending order:
`hadoop fs -cat /tmp/wordcount/uniques/part-r-00000 | sort -rn -k 2 | less`

If you changed the HDFS URL/path in the previous step, update this step accordingly.

[generic-options]: http://hadoop.apache.org/docs/r1.0.4/api/org/apache/hadoop/util/GenericOptionsParser.html#GenericOptions
[mapreduce-wordcount-wiki]: http://wiki.apache.org/hadoop/WordCount
[maven]: http://maven.apache.org
