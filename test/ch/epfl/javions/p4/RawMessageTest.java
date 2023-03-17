package ch.epfl.javions.p4;

import static org.junit.jupiter.api.Assertions.*;

import ch.epfl.javions.ByteString;
import ch.epfl.javions.Crc24;
import ch.epfl.javions.adsb.RawMessage;
import org.junit.jupiter.api.Test;

public class RawMessageTest {

    @Test
    public void testConstruction(){
        byte[] list = new byte[]
                {(byte) 0x8f, (byte) 0xa1, 0x23, 0x45, 0x67, (byte) 0x89,
                        (byte) 0xab, (byte) 0xcd, (byte) 0xef, 0x01, 0x23, (byte) 0xca, (byte) 0x8d, 0x73};
        assertDoesNotThrow(() -> new RawMessage(1234567890L, new ByteString(list)));
        assertThrows(IllegalArgumentException.class,
                () -> new RawMessage(-1L, new ByteString(list)));
        assertThrows(IllegalArgumentException.class,
                () -> new RawMessage(1234567890L,  new ByteString(new byte[] {1, 2, 3})));
    }

    @Test
    public void testOfValidMessage() {
        byte[] bytes = new byte[] {(byte) 0x8f, (byte) 0xa1, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xab, (byte) 0xcd,
                (byte) 0xef, 0x01, 0x23, (byte) 0xca, (byte) 0x8d, 0x73};
        RawMessage message = RawMessage.of(1234567890L, bytes);
        assertNotNull(message);
        assertNull(RawMessage.of(1234567890L, new byte[] {1, 2, 3}));
        assertEquals(1234567890L, message.timeStampNs());
        assertEquals(14, message.bytes().size());
        assertEquals(17, message.downLinkFormat());
        assertEquals(0x6789abcdef0123L, message.payload());
        assertEquals(0b01100, message.typeCode());
    }

    @Test
    public void testOfInvalidMessage() {
        byte[] bytes = new byte[] {(byte) 0x8f, (byte) 0xa1, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef, 0x01, 0x23, 0x45, 0x67, (byte) 0x88};
        RawMessage message = RawMessage.of(1234567890L, bytes);
        assertNull(message);
    }

    @Test
    public void testSize() {
        assertEquals(0, RawMessage.size((byte) 0xb4));
        assertEquals(RawMessage.LENGTH, RawMessage.size((byte) 0x8f));
    }

    @Test
    public void testTypeCode() {
        assertEquals(0b00110, RawMessage.typeCode(0x3456789abcdefaL));
    }

}

