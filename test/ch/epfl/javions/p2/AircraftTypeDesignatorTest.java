package ch.epfl.javions.p2;

import static org.junit.jupiter.api.Assertions.*;

import ch.epfl.javions.aircraft.AircraftTypeDesignator;
import org.junit.jupiter.api.Test;

public class AircraftTypeDesignatorTest {

    @Test
    void typeDesignatorCreationSuccess() {
        assertDoesNotThrow(() -> {
            new AircraftTypeDesignator("AB");
        });
        assertDoesNotThrow(() -> {
            new AircraftTypeDesignator("12Y");
        });
        assertDoesNotThrow(() -> {
            new AircraftTypeDesignator("TIF");
        });
    }

    @Test
    void typeDesignatorCreationFailure() {
        assertThrows(IllegalArgumentException.class, () -> {
            new AircraftTypeDesignator(null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new AircraftTypeDesignator("");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new AircraftTypeDesignator("F");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new AircraftTypeDesignator("bowl");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new AircraftTypeDesignator("GITGUD");
        });
        assertThrows(IllegalArgumentException.class,() -> {
            new AircraftTypeDesignator("é-?");
        });
    }
}
