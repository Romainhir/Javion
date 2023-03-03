package ch.epfl.javions.p2;

import ch.epfl.javions.aircraft.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;

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

    @Test
    void aircraftDatabaseGetCorrectICAO() throws IOException {
        AircraftDatabase base = new AircraftDatabase("/aircraft.zip");
        AircraftData data = base.get(new IcaoAddress("00B100"));
        assertEquals(new AircraftData(new AircraftRegistration("ZS-SNV"), new AircraftTypeDesignator("E135"),
                "EMBRAER ERJ-135", new AircraftDescription("L2J"), WakeTurbulenceCategory.of("M")), data);
    }

    @Test
    void aircraftDatabaseGetNotFound() throws IOException{
        AircraftDatabase base = new AircraftDatabase("/aircraft.zip");
        AircraftData data = base.get(new IcaoAddress("999999"));
        assertEquals(null, data);
    }
}
