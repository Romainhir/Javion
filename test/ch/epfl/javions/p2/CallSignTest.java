package ch.epfl.javions.p2;

import ch.epfl.javions.adsb.CallSign;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CallSignTest {
    @Test
    void callSignConstructionSuccess() {
        assertDoesNotThrow(() -> {
            new CallSign("");
        });
        assertDoesNotThrow(() -> {
            new CallSign("A");
        });
        assertDoesNotThrow(() -> {
            new CallSign("BC");
        });
        assertDoesNotThrow(() -> {
            new CallSign("DEF");
        });
        assertDoesNotThrow(() -> {
            new CallSign("GHIJ");
        });
        assertDoesNotThrow(() -> {
            new CallSign("KLMNO");
        });
        assertDoesNotThrow(() -> {
            new CallSign("PQRSTU");
        });
        assertDoesNotThrow(() -> {
            new CallSign("VWXYZ12");
        });
        assertDoesNotThrow(() -> {
            new CallSign("34567890");
        });
    }

    @Test
    void callSignConstructionFailure() {
        assertThrows(NullPointerException.class, () -> {
            new CallSign(null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new CallSign("null");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new CallSign("éàè");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new CallSign("AAAAAAAAAAAAAAAAAAAA");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new CallSign("à$à$à$à$");
        });
    }
}
