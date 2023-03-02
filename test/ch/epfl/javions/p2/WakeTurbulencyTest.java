package ch.epfl.javions.p2;

import ch.epfl.javions.aircraft.WakeTurbulenceCategory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WakeTurbulencyTest {

    @Test
    void WakeTurbulencyLight() {
        assertEquals(WakeTurbulenceCategory.LIGHT, WakeTurbulenceCategory.of("L"));
    }

    @Test
    void WakeTurbulencyMedium() {
        assertEquals(WakeTurbulenceCategory.MEDIUM, WakeTurbulenceCategory.of("M"));
    }

    @Test
    void WakeTurbulencyLarge() {
        assertEquals(WakeTurbulenceCategory.HEAVY, WakeTurbulenceCategory.of("H"));
    }

    @Test
    void WakeTurbulencyUnknown() {
        assertEquals(WakeTurbulenceCategory.UNKNOWN, WakeTurbulenceCategory.of(""));
        assertEquals(WakeTurbulenceCategory.UNKNOWN, WakeTurbulenceCategory.of("AS"));
    }

    @Test
    void WakeTurbulencyError() {
        assertThrows(NullPointerException.class, () -> {
            WakeTurbulenceCategory.of(null);
        });

    }
}
