package ch.epfl.javions.p2;

import ch.epfl.javions.aircraft.AircraftRegistration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AircraftRegistrationTest {

    @Test
    void aircraftRegistrationConstructionSuccess() {
        assertDoesNotThrow(() -> {
            new AircraftRegistration("A9.?/_+-");
        });
        assertDoesNotThrow(() -> {
            new AircraftRegistration("J");
        });
    }
}
