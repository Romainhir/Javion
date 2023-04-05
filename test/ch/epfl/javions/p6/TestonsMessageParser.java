package ch.epfl.javions.p6;

import ch.epfl.javions.adsb.MessageParser;
import ch.epfl.javions.adsb.RawMessage;
import org.junit.jupiter.api.Test;

import java.util.HexFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestonsMessageParser {
    @Test
    void testParse(){
        String message = "0000000000000000000000000000";
        RawMessage rawMessage = RawMessage.of(100, HexFormat.of().parseHex(message));
        assertEquals(null, MessageParser.parse(rawMessage));
    }
}
