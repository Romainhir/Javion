package ch.epfl.javions.p4;

import ch.epfl.javions.adsb.AirbornePositionMessage;
import ch.epfl.javions.adsb.AircraftIdentificationMessage;
import ch.epfl.javions.adsb.CprDecoder;
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
                if (AirbornePositionMessage.of(m) != null)
                    System.out.println(AirbornePositionMessage.of(m));

                /*if (AircraftIdentificationMessage.of(m) != null)
                    System.out.println(AircraftIdentificationMessage.of(m));*/
            }
            System.out.println(CprDecoder.decodePosition(0.851440, 0.720558,
                    0.830574, 0.591721, 0));
            assertEquals(384, k);
        }
    }
}
