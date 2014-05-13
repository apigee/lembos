package io.apigee.lembos.mapreduce;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import java.io.IOException;

/**
 * Implementation of {@link RecordReader} to generate one record.
 */
public final class RunOnceRecordReader extends RecordReader<LongWritable, LongWritable> {

    private boolean hasWritten;

    @Override
    public void initialize(final InputSplit inputSplit, final TaskAttemptContext taskAttemptContext)
            throws IOException, InterruptedException {
        hasWritten = false;
    }

    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        boolean hasNext = !hasWritten;

        hasWritten = true;

        return hasNext;
    }

    @Override
    public LongWritable getCurrentKey() throws IOException, InterruptedException {
        return new LongWritable(0);
    }

    @Override
    public LongWritable getCurrentValue() throws IOException, InterruptedException {
        return new LongWritable(0);
    }

    @Override
    public float getProgress() throws IOException, InterruptedException {
        return hasWritten ? 1.0f : 0.0f;
    }

    @Override
    public void close() throws IOException {
        hasWritten = true;
    }

}
