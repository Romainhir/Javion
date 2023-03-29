package ch.epfl.javions.p6;

import ch.epfl.javions.adsb.AirborneVelocityMessage;
import ch.epfl.javions.adsb.RawMessage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.HexFormat;

public class TestonsVitesse {
    @Test
    void testonsVitesse() {
        String message = "8D485020994409940838175B284F";
        RawMessage rawMessage = RawMessage.of(100, HexFormat.of().parseHex(message));
        AirborneVelocityMessage airborneVelocityMessage = AirborneVelocityMessage.of(rawMessage);
        System.out.println(airborneVelocityMessage);

        String message2 = "8DA05F219B06B6AF189400CBC33F";
        RawMessage rawMessage2 = RawMessage.of(100, HexFormat.of().parseHex(message2));
        AirborneVelocityMessage airborneVelocityMessage2 = AirborneVelocityMessage.of(rawMessage2);
        System.out.println(airborneVelocityMessage2);
    }
}
