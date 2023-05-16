package ch.epfl.javions.p6;

import ch.epfl.javions.ByteString;
import org.junit.jupiter.api.Test;

import ch.epfl.javions.adsb.AirborneVelocityMessage;
import ch.epfl.javions.adsb.RawMessage;
import ch.epfl.javions.aircraft.IcaoAddress;
import org.junit.jupiter.api.Test;

import java.util.HexFormat;

import static org.junit.jupiter.api.Assertions.*;

class GptTestVitesse {

    @Test
    void testConstructor() {
        assertThrows(NullPointerException.class, () -> new AirborneVelocityMessage(0, null, 100, 180));
        assertThrows(IllegalArgumentException.class, () -> new AirborneVelocityMessage(-1, new IcaoAddress("111111"), -100, -180));
    }

    @Test
    void testOf() {
        String message = "8D4D2480990CBA00280809F8528A";
        RawMessage rawMessage = RawMessage.of(100, HexFormat.of().parseHex(message));
        AirborneVelocityMessage airborneVelocityMessage = AirborneVelocityMessage.of(rawMessage);
        System.out.println(airborneVelocityMessage);
    }
}

