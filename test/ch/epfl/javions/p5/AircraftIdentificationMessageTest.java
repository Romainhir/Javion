package ch.epfl.javions.p5;

import ch.epfl.javions.adsb.AircraftIdentificationMessage;
import ch.epfl.javions.demodulation.AdsbDemodulator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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
                System.out.println(aim.icaoAddress() + " " + aim.callSign() + " " + aim.timeStampNs() + " " +
                        aim.category());
                compteur++;
            }
        }
    }
}
