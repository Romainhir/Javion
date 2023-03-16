package ch.epfl.javions.demodulation;

import ch.epfl.javions.Preconditions;

import java.io.*;

public final class PowerComputer {
    private final short[] batch;
    private final SamplesDecoder decoder;
    private short[] sample_data;

    public PowerComputer(InputStream stream, int batchSize) throws IOException {
        Preconditions.checkArgument((batchSize > 0) && (batchSize % 8 == 0));
        batch = new short[2 * batchSize];
        decoder = new SamplesDecoder(stream, 2 * batchSize);
        sample_data = new short[8];
    }

    private int calculatePower(short[] data) {
        int reel = data[1] - data[3] + data[5] - data[7];
        int imaginary = data[0] - data[2] + data[4] - data[6];


        return reel * reel + imaginary * imaginary;
    }

    /**
     * Remove the last element of the list and move others to the next index
     * Add the data at the first index of the list
     *
     * @param data_tab (short[]) The tab of data
     * @param data     (short) data
     */
    private void addFirst(short[] data_tab, short data) {
        for (int i = data_tab.length - 1; 0 < i; --i) {
            data_tab[i] = data_tab[i - 1];
        }
        data_tab[0] = data;
    }

    public int readBatch(int[] batch) throws IOException {
        Preconditions.checkArgument(batch.length == this.batch.length / 2);
        int decodedNumber = decoder.readBatch(this.batch);
        for (int j = 0; j < decodedNumber; j += 2) {
            for (int i = 0; i < 2; ++i) {
                addFirst(sample_data, this.batch[j + i]);
            }
            batch[j / 2] = calculatePower(sample_data);
        }
        return decodedNumber / 2;
    }
}
