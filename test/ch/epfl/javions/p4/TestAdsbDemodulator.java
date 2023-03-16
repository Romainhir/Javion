package ch.epfl.javions.p4;

import ch.epfl.javions.adsb.RawMessage;
import ch.epfl.javions.demodulation.AdsbDemodulator;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestAdsbDemodulator {
    @Test
    void test() throws Exception {
        String f = "resources/samples_20230304_1442.bin";
        try (InputStream s = new FileInputStream(f)) {
            AdsbDemodulator d = new AdsbDemodulator(s);
            RawMessage m;
            int k = 0;
            while ((m = d.nextMessage()) != null) {
                k++;
                System.out.println(m);
            }
            assertEquals(384, k);
        }
    }
}
