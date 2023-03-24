package ch.epfl.javions.p5;

import ch.epfl.javions.ByteString;
import ch.epfl.javions.adsb.AirbornePositionMessage;
import ch.epfl.javions.adsb.AircraftIdentificationMessage;
import ch.epfl.javions.adsb.CallSign;
import ch.epfl.javions.adsb.RawMessage;
import ch.epfl.javions.aircraft.IcaoAddress;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AircraftPositionMessageTest {

    @Test
    void aircraftPositionMessageConstructionFailure() {
        assertThrows(NullPointerException.class, () -> {
            new AirbornePositionMessage(123, null, 1, 1, 0, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new AirbornePositionMessage(-1234, new IcaoAddress("998655"), 1, 1, 0, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new AirbornePositionMessage(-1234, new IcaoAddress("998655"), 1, 918237, 0, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new AirbornePositionMessage(-1234, new IcaoAddress("998655"), 1, 1, -123, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new AirbornePositionMessage(-1234, new IcaoAddress("998655"), 1, 1, 0, 998);
        });
    }

    @Test
    void aircraftPositionMessageIsNuuull() {
        byte[] bytes = {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
        assertNull(AirbornePositionMessage.of(new RawMessage(12334556, new ByteString(bytes))));
    }
}
