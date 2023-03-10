package ch.epfl.javions.p3;

import ch.epfl.javions.demodulation.PowerComputer;
import ch.epfl.javions.demodulation.SamplesDecoder;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

public class PowerComputerTest {

    @Test
    public void testReadBatch() throws Exception {
        // Prepare input stream
        byte[] inputBytes = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);

        // Create PowerComputer object and read batch
        int batchSize = 8;
        PowerComputer powerComputer = new PowerComputer(inputStream, batchSize);
        int[] batch = new int[batchSize];
        int numBatches = powerComputer.readBatch(batch);

        // Verify results
        assertEquals(2, numBatches);
        assertEquals(4844548, batch[0]);
    }

    @Test
    public void testSamplesDecoder() throws Exception {
        // Prepare input stream
        byte[] inputBytes = {0x00, (byte) 0x80, 0x01, (byte) 0x81, 0x02, (byte) 0x82, 0x03, (byte) 0x83};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);

        // Create SamplesDecoder object and read batch
        int batchSize = 4;
        SamplesDecoder samplesDecoder = new SamplesDecoder(inputStream, batchSize);
        short[] batch = new short[batchSize];
        int numSamples = samplesDecoder.readBatch(batch);

        // Verify results
        assertEquals(4, numSamples);
        assertEquals(-2048, batch[0]);
        assertEquals(-1791, batch[1]);
        assertEquals(-1534, batch[2]);
        assertEquals(-1277, batch[3]);
    }
}


