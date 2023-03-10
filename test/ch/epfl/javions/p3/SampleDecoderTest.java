package ch.epfl.javions.p3;

import ch.epfl.javions.demodulation.SamplesDecoder;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class SamplesDecoderTest {

    @Test
    void testReadBatch() throws IOException {
        InputStream stream = new ByteArrayInputStream(new byte[]{0x00, 0x00, (byte) 0xff, (byte) 0xff});
        SamplesDecoder decoder = new SamplesDecoder(stream, 2);
        short[] batch = new short[2];
        int bytesRead = decoder.readBatch(batch);
        assertEquals(2, bytesRead);
        assertEquals(-2048, batch[0]);
        assertEquals(2047, batch[1]);
    }

    @Test
    void testConstructorThrowsExceptionForNegativeBatchSize() {
        assertThrows(IllegalArgumentException.class, () -> {
            InputStream stream = new ByteArrayInputStream(new byte[]{});
            SamplesDecoder decoder = new SamplesDecoder(stream, -1);
        });
    }

    @Test
    void testConstructorThrowsExceptionForNullInputStream() {
        assertThrows(NullPointerException.class, () -> {
            SamplesDecoder decoder = new SamplesDecoder(null, 2);
        });
    }

    @Test
    void testReadBatchThrowsExceptionForMismatchedBatchSize() {
        assertThrows(IllegalArgumentException.class, () -> {
            InputStream stream = new ByteArrayInputStream(new byte[]{});
            SamplesDecoder decoder = new SamplesDecoder(stream, 2);
            short[] batch = new short[3];
            decoder.readBatch(batch);
        });
    }
}

