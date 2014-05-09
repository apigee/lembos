/*
 * Copyright 2014 Apigee Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * This is the standard Hadoop WordCount example located on the Hadoop wiki (http://wiki.apache.org/hadoop/WordCount)
 * but written in Node.js.
 *
 * This tutorial was written against Hadoop 1.0.4 so if you're using a different version, you might need to alter the
 * job configuration to suit.
 */

'use strict';

var FileInputFormat = require('hadoop-input').FileInputFormat;
var FileOutputFormat = require('hadoop-output').FileOutputFormat;

module.exports = {

  jobSetup: function (job, cb) {
    // Default to /tmp/wordcount/4300.txt for input if not already specified (CLI or conf file)
    if (FileInputFormat.getInputPaths(job).length === 0) {
      FileInputFormat.setInputPaths(job, '/tmp/wordcount/4300.txt');
    }

    // Default to /tmp/wordcount/uniques for output if not already specified (CLI or conf file)
    if (!FileOutputFormat.getOutputPath(job)) {
      FileOutputFormat.setOutputPath(job, '/tmp/wordcount/uniques/');
    }

    // Set the map and reduce output key/value types since they cannot be inferred
    job.setMapOutputKeyClass('org.apache.hadoop.io.Text')
       .setMapOutputValueClass('org.apache.hadoop.io.IntWritable')
       .setOutputKeyClass('org.apache.hadoop.io.Text')
       .setOutputValueClass('org.apache.hadoop.io.IntWritable');

    cb();
  },

  map: function (key, value, context, cb) {
    var line = value;

    // Replace all characters that aren't a letter or a hyphen with a space
    line = line.replace(/[^a-zA-Z0-9\-]/g, ' ');

    // Collapse all whitespace
    line = line.replace(/s+/g, ' ');

    // Split the line by space and iterate over each word
    line.split(' ').forEach(function (word) {
      // Emit the word and a 1
      context.write(word, 1);
    });

    // Call the callback
    cb();
  },

  reduce: function (key, values, context, cb) {
    var count = 0;

    // Sum up the count of this word
    while (values.hasNext()) {
      count += values.next();
    }

    // Emit the count
    context.write(key, count);

    // Call the callback
    cb();
  }

};
