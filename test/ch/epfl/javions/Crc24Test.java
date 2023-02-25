package ch.epfl.javions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Crc24Test {
    @Test
    void bitwiseTest(){
        assertEquals(0, Crc24.crc_bitwise(0xFFFFFF, new byte[]{0x10, 0x00, 0x00}));
    }
}
