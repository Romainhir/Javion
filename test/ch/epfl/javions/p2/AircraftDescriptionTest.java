package ch.epfl.javions.p2;

import ch.epfl.javions.aircraft.AircraftDescription;
<<<<<<< HEAD
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AircraftDescriptionTest {

    @Test
    void aircraftDescriptionConstructionSuccess() {
        assertDoesNotThrow(() -> {new AircraftDescription("A0E");});
        assertDoesNotThrow(() -> {new AircraftDescription("B1J");});
        assertDoesNotThrow(() -> {new AircraftDescription("D2P");});
        assertDoesNotThrow(() -> {new AircraftDescription("G3T");});
        assertDoesNotThrow(() -> {new AircraftDescription("H4-");});
        assertDoesNotThrow(() -> {new AircraftDescription("L6-");});
        assertDoesNotThrow(() -> {new AircraftDescription("P8-");});
        assertDoesNotThrow(() -> {new AircraftDescription("R8-");});
        assertDoesNotThrow(() -> {new AircraftDescription("S8-");});
        assertDoesNotThrow(() -> {new AircraftDescription("T4-");});
        assertDoesNotThrow(() -> {new AircraftDescription("V8-");});
        assertDoesNotThrow(() -> {new AircraftDescription("-1-");});
    }

    @Test
    void aircraftDescriptionConstructionFailure() {
        assertThrows(NullPointerException.class, () -> {new AircraftDescription(null);});
        assertThrows(IllegalArgumentException.class, () -> {new AircraftDescription("");});
        assertThrows(IllegalArgumentException.class, () -> {new AircraftDescription("A");});
        assertThrows(IllegalArgumentException.class, () -> {new AircraftDescription("A1");});
        assertThrows(IllegalArgumentException.class, () -> {new AircraftDescription("9-");});
        assertThrows(IllegalArgumentException.class, () -> {new AircraftDescription("EA1");});
        assertThrows(IllegalArgumentException.class, () -> {new AircraftDescription("BITE");});
        assertThrows(IllegalArgumentException.class, () -> {new AircraftDescription("é$à");});
=======
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
>>>>>>> origin/main
    }
}
