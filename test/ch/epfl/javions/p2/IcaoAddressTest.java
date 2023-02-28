package ch.epfl.javions.p2;

import ch.epfl.javions.aircraft.IcaoAddress;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IcaoAddressTest {

    @Test
    void IcaoConstructSuccess() {
        assertDoesNotThrow(() -> {
            new IcaoAddress("6789AB");
        });
        assertDoesNotThrow(() -> {
            new IcaoAddress("012345");
        });
        assertDoesNotThrow(() -> {
            new IcaoAddress("FCDEFF");
        });
    }

    @Test
    void IcaoConstructFailure() {
        assertThrows(IllegalArgumentException.class, () -> {
            new IcaoAddress("SK1 T1");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new IcaoAddress("FFFFFFFFFFFFFF");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new IcaoAddress("AMENO");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new IcaoAddress("A B CD");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new IcaoAddress("");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new IcaoAddress(null);
        });
    }
}
