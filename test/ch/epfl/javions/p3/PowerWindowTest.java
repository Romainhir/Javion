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
    void powerWindowAvanceSuccessFirstTab() throws Exception {
        byte[] tab = new byte[8];
        tab[1] = 9;
        InputStream in = new ByteArrayInputStream(tab);
        PowerWindow powerWindow = new PowerWindow(in, 8);
        powerWindow.advance();
        assertEquals(5308416, powerWindow.get(0));
    }

    @Test
    void powerWindowAvanceBySuccessFirstTab() throws Exception {
        byte[] tab = new byte[16];
        tab[3] = 9;
        InputStream in = new ByteArrayInputStream(tab);
        PowerWindow powerWindow = new PowerWindow(in, 8);
        powerWindow.advanceBy(3);
        assertEquals(5308416, powerWindow.get(0));
    }

    @Test
    void powerWindowAvanceBySuccessMixedTab() throws Exception {
        byte[] inputBytes = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
                0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x00, 0x11,
                0x22, 0x33, 0x44, 0x05, 0x06, 0x07, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        PowerWindow powerWindow = new PowerWindow(inputStream, 8);
        powerWindow.advanceBy(3);
        assertEquals(8323712, powerWindow.get(5));
    }

    @Test
    void powerWindowAvanceBySuccessSecondTab() throws Exception {
        byte[] inputBytes = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
                0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x00, 0x11,
                0x22, 0x33, 0x44, 0x05, 0x06, 0x07, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        PowerWindow powerWindow = new PowerWindow(inputStream, 8);
        powerWindow.advanceBy(8);
        assertEquals(8323712, powerWindow.get(0));
    }

    @Test
    void powerWindowAvanceBySuccessThirdTab() throws Exception {
        byte[] inputBytes = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
                0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x00, 0x11,
                0x22, 0x33, 0x44, 0x05, 0x06, 0x07, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x00, 0x01, 0x02, 0x03,
                0x04, 0x05, 0x06, 0x07, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05,
                0x06, 0x07, 0x11, 0x22, 0x33, 0x79, 0x05, 0x06, 0x79,
                0x79, 0x22, 0x33, 0x44, 0x79, 0x04, 0x05, 0x22, 0x79, 0x44, 0x01, 0x02, 0x03, 0x22, 0x33, 0x44, 0x07, 0x00, 0x11,
                0x22, 0x79, 0x44, 0x05, 0x06, 0x07, 0x00, 0x01, 0x79, 0x03, 0x22, 0x33, 0x44, 0x79, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        PowerWindow powerWindow = new PowerWindow(inputStream, 8);

        assertEquals(4844548, powerWindow.get(0));
        powerWindow.advanceBy(9);
        assertEquals(8323712, powerWindow.get(0));
        powerWindow.advanceBy(9);
        assertEquals(8454272, powerWindow.get(0));
        powerWindow.advanceBy(9);
        assertEquals(3456136, powerWindow.get(0));
    }

    @Test
    void powerWindowGetFirstTable() throws Exception {
        byte[] tab = new byte[8];
        tab[1] = 9;
        InputStream in = new ByteArrayInputStream(tab);
        PowerWindow powerWindow = new PowerWindow(in, 8);
        assertEquals(5308416, powerWindow.get(1));
    }

}
