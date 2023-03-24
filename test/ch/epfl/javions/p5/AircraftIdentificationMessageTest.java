package ch.epfl.javions.p5;

import ch.epfl.javions.ByteString;
import ch.epfl.javions.adsb.AircraftIdentificationMessage;
import ch.epfl.javions.adsb.CallSign;
import ch.epfl.javions.adsb.RawMessage;
import ch.epfl.javions.aircraft.IcaoAddress;
import ch.epfl.javions.demodulation.AdsbDemodulator;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

public class AircraftIdentificationMessageTest {


    @Test
    void aircraftIdentificationMessageOfSuccess() throws IOException {
        InputStream in;
        AdsbDemodulator ad;

        in = new FileInputStream("../samples_20230304_1442.bin");
        ad = new AdsbDemodulator(in);

        int compteur = 0;
        while (compteur < 5) {
            AircraftIdentificationMessage aim = AircraftIdentificationMessage.of(ad.nextMessage());
            if (aim != null) {
                System.out.println(aim);
                compteur++;
            }
        }
    }

    @Test
    void aircraftIdentificationMessageConstructionFailure() {
        assertThrows(NullPointerException.class, () -> {
            new AircraftIdentificationMessage(12973, null, 4, new CallSign(""));
        });
        assertThrows(NullPointerException.class, () -> {
            new AircraftIdentificationMessage(12973, new IcaoAddress("098765"), 4, null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new AircraftIdentificationMessage(-2973, new IcaoAddress("987654"), 4, new CallSign(""));
        });
    }

    @Test
    void aircraftIdentificationMessageOfNuuulll() {
        byte[] bytes = {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
        assertNull(AircraftIdentificationMessage.of(new RawMessage(12334556, new ByteString(bytes))));
    }
}
