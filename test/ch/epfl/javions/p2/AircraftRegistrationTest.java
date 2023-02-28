package ch.epfl.javions.p2;

import ch.epfl.javions.aircraft.AircraftRegistration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AircraftRegistrationTest {

    @Test
    void aircraftRegistrationConstructionSuccess() {
        assertDoesNotThrow(() -> {
            new AircraftRegistration("QWERTZUIOPASDFGHJKLYXCVBNM1234566789.?/_+-");
        });
        assertDoesNotThrow(() -> {
            new AircraftRegistration("JAVAJIVE");
        });
        assertDoesNotThrow(() -> {
            new AircraftRegistration("K");
        });
    }

    @Test
    void aircraftRegistrationConstructionFailure() {
        assertThrows(IllegalArgumentException.class, () -> {
            new AircraftRegistration(null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new AircraftRegistration("%@*°^^");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new AircraftRegistration("çàléèöüä");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new AircraftRegistration("objection");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new AircraftRegistration("");
        });
    }
}
