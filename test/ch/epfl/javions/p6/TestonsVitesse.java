package ch.epfl.javions.p6;

import ch.epfl.javions.Units;
import ch.epfl.javions.adsb.AirborneVelocityMessage;
import ch.epfl.javions.adsb.RawMessage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.HexFormat;

import static org.junit.jupiter.api.Assertions.*;

public class TestonsVitesse {
    @Test
    void testonsVitesse() {
        String message = "8D485020994409940838175B284F";
        RawMessage rawMessage = RawMessage.of(100, HexFormat.of().parseHex(message));
        AirborneVelocityMessage airborneVelocityMessage = AirborneVelocityMessage.of(rawMessage);
        assertEquals(159, (int)Units.convertTo(airborneVelocityMessage.speed(), Units.Speed.KNOT));
        assertEquals(182, (int)Units.convertTo(airborneVelocityMessage.trackOrHeading(), Units.Angle.DEGREE));
        System.out.println(airborneVelocityMessage);

        String message2 = "8DA05F219B06B6AF189400CBC33F";
        RawMessage rawMessage2 = RawMessage.of(100, HexFormat.of().parseHex(message2));
        AirborneVelocityMessage airborneVelocityMessage2 = AirborneVelocityMessage.of(rawMessage2);
        assertEquals(375, (int)Units.convertTo(airborneVelocityMessage2.speed(), Units.Speed.KNOT));
        assertEquals(243, (int)Units.convertTo(airborneVelocityMessage2.trackOrHeading(), Units.Angle.DEGREE));
        System.out.println(airborneVelocityMessage2);
    }
}
