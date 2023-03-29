package ch.epfl.javions.demodulation;

import ch.epfl.javions.Preconditions;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Class that represent a sample decoder. It transforms the bytes sended by an AirSpy radio in samples of 12
 * signed bits.
 *
 * @author Romain Hirschi
 * @author Moussab Tasnim Ibrahim
 */
public final class SamplesDecoder {

    private final InputStream stream;
    private byte[] data;
    private final static short mask = 0x0fff;

    /**
     * Constructor of the sample decoder. Set the input stream given in parameter in an attribute. It need
     * the size of a batch, given in parameter too. Throw an Exception if the batch size is not (strictly) positive
     * of if the stream is null.
     *
     * @param stream    (InputStream) : The input stream
     * @param batchSize (int) : The size of a batch
     */
    public SamplesDecoder(InputStream stream, int batchSize) {
        Preconditions.checkArgument(batchSize > 0);
        if (stream == null) {
            throw new NullPointerException("the stream is null");
        }
        this.stream = stream;
        data = new byte[2 * batchSize];
    }

    /**
     * Read a batch of data from the input stream given int the constructor and store them in a short array.
     * The length of the array must be 2 time larger than the length of the array stored in attribute.
     * Return the number of sample decoded. Throw an Exception if something went wrong during the reading of the stream.
     *
     * @param batch (short[]) : the array of short to store the batch
     * @return (int) : the number of sample decoded
     * @throws IOException if something went wrong during the reading of the stream
     */
    public int readBatch(short[] batch) throws IOException {
        Preconditions.checkArgument(batch.length == data.length / 2);
        int read;
        read = stream.readNBytes(data, 0, data.length);
        // Unsigned the byte is useful because since byte are signed if the last byte the first one would be "crushed" by 1s
        for (int i = 0; i < read; i += 2) {
            batch[i / 2] = (short) (((Byte.toUnsignedInt(data[i]) |
                    ((Byte.toUnsignedInt(data[i + 1]) << 8))) & mask) - 2048);

        }
        return read / 2;
    }
}
