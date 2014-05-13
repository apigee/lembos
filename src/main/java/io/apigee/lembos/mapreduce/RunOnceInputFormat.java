package io.apigee.lembos.mapreduce;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link InputFormat} to run once with a generated output.
 */
public final class RunOnceInputFormat extends InputFormat<LongWritable, LongWritable> {

    @Override
    public List<InputSplit> getSplits(final JobContext jobContext) throws IOException, InterruptedException {
        List<InputSplit> splits = new ArrayList<>();

        splits.add(new RunOnceInputSplit());

        return splits;
    }

    @Override
    public RecordReader<LongWritable, LongWritable> createRecordReader(final InputSplit inputSplit,
                                                                       final TaskAttemptContext taskAttemptContext)
            throws IOException, InterruptedException {
        return new RunOnceRecordReader();
    }

}
