package ch.epfl.javions.p4;

import ch.epfl.javions.ByteString;
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
            int j = 0;
            while ((m = d.nextMessage()) != null) {
                k++;
                /*if (AirbornePositionMessage.of(m) != null) {
                    System.out.println(AirbornePositionMessage.of(m));
                    ++j;
                }*/

                if (AircraftIdentificationMessage.of(m) != null) {
                    System.out.println(AircraftIdentificationMessage.of(m));
                    ++j;
                }
            }
            System.out.println(j);
            assertEquals(384, k);
        }
    }

    @Test
    void test2(){
        RawMessage m = new RawMessage(0L,
                ByteString.ofHexadecimalString("8D39203559B225F07550ADBE328F"));

        RawMessage n = new RawMessage(0L,
                ByteString.ofHexadecimalString("8DAE02C85864A5F5DD4975A1A3F5"));

        System.out.println(AirbornePositionMessage.of(m));
        System.out.println(AirbornePositionMessage.of(n));
    }
}
