package ch.epfl.javions.demodulation;

import ch.epfl.javions.Preconditions;

import java.io.IOException;
import java.io.InputStream;

public final class PowerComputer {
    private final short[] batch;
    private final SamplesDecoder decoder;

    public PowerComputer(InputStream stream, int batchSize) throws Exception {
        Preconditions.checkArgument(batchSize > 0 && batchSize % 8 == 0);
        batch = new short[batchSize];
        decoder = new SamplesDecoder(stream, batchSize);
    }

    //TODO
    public int readBatch(int[] batch) throws IOException {
        return 0;
    }
}
