package ch.epfl.javions.p2;

import ch.epfl.javions.aircraft.AircraftDescription;
import ch.epfl.javions.aircraft.AircraftRegistration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AircraftDescriptionTest {
    @Test
    void AircraftDescriptionConstructionSuccess() {
        assertDoesNotThrow(() -> {
            new AircraftDescription("V6P");
        });
        assertDoesNotThrow(() -> {
            new AircraftDescription("-0-");
        });
        assertDoesNotThrow(() -> {
            new AircraftDescription("V8J");
        });
    }

    @Test
    void AircraftDescriptionConstructionFailure() {
        /*assertThrows(IllegalArgumentException.class, () -> {
            new AircraftDescription(null);
        });*/
        assertThrows(IllegalArgumentException.class, () -> {
            new AircraftDescription("%@*°^^");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new AircraftDescription("çàléèöüä");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new AircraftDescription("objection");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new AircraftDescription("");
        });
    }
}
