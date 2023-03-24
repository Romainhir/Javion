package ch.epfl.javions.p5;

import ch.epfl.javions.ByteString;

import ch.epfl.javions.adsb.CallSign;
import ch.epfl.javions.aircraft.IcaoAddress;

import ch.epfl.javions.GeoPos;
import ch.epfl.javions.Units;
import ch.epfl.javions.adsb.AirbornePositionMessage;
import ch.epfl.javions.adsb.AircraftIdentificationMessage;
import ch.epfl.javions.adsb.CprDecoder;
import ch.epfl.javions.adsb.RawMessage;

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
        GeoPos pos = CprDecoder.decodePosition(0.8514404296875, 0.7205581665039062, 0.8305740356445312, 0.5917205810546875, 0);
        System.out.println(pos);
        assertEquals("(7.476062346249819°, 46.323349038138986°)", pos.toString());

    }


}
