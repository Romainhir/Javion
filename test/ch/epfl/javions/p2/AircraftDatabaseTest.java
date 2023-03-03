package ch.epfl.javions.p2;

import ch.epfl.javions.aircraft.AircraftDatabase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AircraftDatabaseTest {

    @Test
    void aircraftDatabaseConstructionSuccess() {
        assertDoesNotThrow(() -> {
            new AircraftDatabase("/file.bat");
        });
    }

    @Test
    void aircraftDatabaseConstructionFailure() {
        assertThrows(NullPointerException.class, () -> {
            new AircraftDatabase(null);
        });
    }
}
