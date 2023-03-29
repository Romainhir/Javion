package ch.epfl.javions.p6;

import ch.epfl.javions.adsb.AircraftStateAccumulator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestonsAircraftStateAccum {
    @Test
    void testConscrutor(){
        assertThrows(NullPointerException.class,() -> new AircraftStateAccumulator<>(null));
    }
}
