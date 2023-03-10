package ch.epfl.javions.p3;

import ch.epfl.javions.demodulation.PowerWindow;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

public class PowerWindowTest {

    @Test
    void powerWindowConstructionSuccess() {
        assertDoesNotThrow(() -> new PowerWindow(new FileInputStream("D:\\intellij\\Javion\\resources\\samples.bin"), 8));
    }

    @Test
    void powerWindowConstructionFailure() {
        assertThrows(IllegalArgumentException.class, () -> new PowerWindow(new FileInputStream("D:\\intellij\\Javion\\resources\\samples.bin"), -1));
        assertThrows(IllegalArgumentException.class, () -> new PowerWindow(new FileInputStream("D:\\intellij\\Javion\\resources\\samples.bin"), Integer.MAX_VALUE));
    }

    @Test
    void powerWindowIsNotFull() throws Exception {
        byte[] tab = {1, 2, 3, 4};
        InputStream stream = new ByteArrayInputStream(tab);
        PowerWindow window = new PowerWindow(stream, 8);
        assertFalse(window.isFull());
    }

    @Test
    void powerWindowIsFull() throws Exception {
        byte[] tab = new byte[32];
        InputStream stream = new ByteArrayInputStream(tab);
        PowerWindow window = new PowerWindow(stream, 8);
        assertTrue(window.isFull());
    }

    @Test
    void powerWindowGetFirstTable() throws Exception{
        byte[] tab = new byte[8];
        tab[1] = 9;
        InputStream in = new ByteArrayInputStream(tab);
        PowerWindow powerWindow = new PowerWindow(in, 8);
        assertEquals(9, powerWindow.get(1));
    }
}
