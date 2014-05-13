package io.apigee.lembos.mapreduce;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.InputSplit;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Implementation of {@link InputSplit} for generating one split.
 */
public final class RunOnceInputSplit extends InputSplit implements Writable {

    @Override
    public long getLength() throws IOException, InterruptedException {
        return 1L;
    }

    @Override
    public String[] getLocations() throws IOException, InterruptedException {
        return new String[] {};
    }

    @Override
    public void write(final DataOutput dataOutput) throws IOException {
        // Nothing to do
    }

    @Override
    public void readFields(final DataInput dataInput) throws IOException {
        // Nothing to do
    }

}
