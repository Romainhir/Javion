package ch.epfl.javions.p5;

import ch.epfl.javions.ByteString;
import ch.epfl.javions.GeoPos;
import ch.epfl.javions.Units;
import ch.epfl.javions.adsb.AirbornePositionMessage;
import ch.epfl.javions.adsb.AircraftIdentificationMessage;
import ch.epfl.javions.adsb.CprDecoder;
import ch.epfl.javions.adsb.RawMessage;
import ch.epfl.javions.demodulation.AdsbDemodulator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

public class AircraftIdentificationMessageTest {


    @Test
    void aircraftIdentificationMessageOfSuccess() throws IOException {
        InputStream in;
        AdsbDemodulator ad;

        in = new FileInputStream("resources/samples_20230304_1442.bin");
        ad = new AdsbDemodulator(in);

        int compteur = 0;
        int compteur2 = 0;
        while (compteur < 5) {
            AircraftIdentificationMessage aim = AircraftIdentificationMessage.of(ad.nextMessage());
            if (aim != null) {
                System.out.println(aim);
                compteur++;
            }
        }
        System.out.println("");


        while (compteur2 < 5) {
            AirbornePositionMessage apm = AirbornePositionMessage.of(ad.nextMessage());

            if (apm != null) {
                System.out.println(apm);
                compteur2++;
            }
        }
        System.out.println("");
        RawMessage rm = new RawMessage(0L,
                ByteString.ofHexadecimalString("8D39203559B225F07550ADBE328F"));

        RawMessage rm2 = new RawMessage(0L,
                ByteString.ofHexadecimalString("8DAE02C85864A5F5DD4975A1A3F5"));
        System.out.println(AirbornePositionMessage.of(rm));
        System.out.println(AirbornePositionMessage.of(rm2));

    }

    @Test
    void isNanTest(){
        double a = Math.acos(1.01);
        assertEquals(a, Double.NaN);
        if(Double.isNaN(a)){
            System.out.println("a is NaN");
            a = 1;
            System.out.println(a);
        }
    }

    @Test
    void cprTest(){
        System.out.println(CprDecoder.decodePosition(0.851440d, 0.720558d, 0.830574d, 0.591721d, 0));
        double longitude = Units.convert((1 / 41.0) * (0 + 0.851440d), Units.Angle.TURN, Units.Angle.T32);
        double latitude = Units.convert((1 / 60.0) * (7 + 0.720558d), Units.Angle.TURN, Units.Angle.T32);
        GeoPos expectedMostRecent0 = new GeoPos((int) Math.rint(longitude), (int) Math.rint(latitude));
        GeoPos expectedMostRecent1 = new GeoPos((int) longitude, (int) latitude);
        System.out.println(expectedMostRecent1);
        System.out.println(expectedMostRecent0);
    }


}
