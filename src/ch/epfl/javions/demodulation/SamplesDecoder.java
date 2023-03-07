package ch.epfl.javions.demodulation;

import ch.epfl.javions.Preconditions;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class SamplesDecoder {

    private final InputStream stream;
    private byte[] data;
    public SamplesDecoder(InputStream stream, int batchSize) throws Exception{
        Preconditions.checkArgument(batchSize > 0);
        if(stream == null){
            throw new NullPointerException("the stream is null");
        }
        this.stream = stream;
        data = new byte[2 * batchSize];
    }

    public int readBatch(short[] batch) throws IOException {
        Preconditions.checkArgument(batch.length == data.length / 2);
        int read = 0;
        try(stream){
            read = stream.readNBytes(data, 0, data.length);
            // Unsigned the byte is useful because since byte are signed if the last byte the first one would be "crushed" by 1s
            for(int i = 0; i < data.length; i += 2){
                batch[i / 2] = (short) (((Byte.toUnsignedInt(data[i])) | (data[i + 1] << 8)) - 2048);
            }
        }
        return read / 2;
    }
}
