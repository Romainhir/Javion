package ch.epfl.javions.adsb;

import ch.epfl.javions.Bits;
import ch.epfl.javions.Preconditions;
import ch.epfl.javions.aircraft.IcaoAddress;

import java.util.Objects;

/**
 * Record that represent an airborne identification message. We store in the record the timestamp of the message,
 * the ICAO address, the category and the callsign of the aircraft.
 *
 * @param timeStampNs (long) : the timestamp of the message
 * @param icaoAddress (IcaoAddress) : the ICAO address of the aircraft
 * @param category    (int) : the category of the aircraft
 * @param callSign    (CallSign) : the callsign of the aircraft
 * @author Romain Hirschi
 * @author Moussab Tasnim Ibrahim
 */
public record AircraftIdentificationMessage(long timeStampNs, IcaoAddress icaoAddress, int category,
                                            CallSign callSign) implements Message {

    private static final int MESSAGE_SIZE = 48;
    private static final int CATEGORY_SIZE = 3;
    private static final int CALLSIGN_CHAR_SIZE = 6;

    /**
     * Constructor of the record. In parameter is given he record the timestamp of the message,
     * the ICAO address, the category and the calldign of the aircraft.
     *
     * @param timeStampNs (long) : the timestamp of the message
     * @param icaoAddress (IcaoAddress) : the ICAO address of the aircraft
     * @param category    (int) : the category of the aircraft
     * @param callSign    (CallSign) : the callsign of the aircraft
     */
    public AircraftIdentificationMessage {
        Objects.requireNonNull(icaoAddress);
        Objects.requireNonNull(callSign);
        Preconditions.checkArgument(timeStampNs >= 0);
    }

    /**
     * Decode the given raw message and create an airborne identification message corresponding to the decoded value.
     *
     * @param rawMessage (RawMessage) : the raw message to decode
     * @return (AirborneIdentificationMessage) : the airborne identification message
     */
    public static AircraftIdentificationMessage of(RawMessage rawMessage) {
        long payload = rawMessage.payload();
        int typeCode = RawMessage.typeCode(payload);
        typeCode = 14 - typeCode;
        int category = (typeCode << 4) + Bits.extractUInt(payload, MESSAGE_SIZE, CATEGORY_SIZE);
        StringBuilder callString = new StringBuilder();
        char extractedChar = ' ';
        for (int i = MESSAGE_SIZE - CALLSIGN_CHAR_SIZE; i >= 0; i -= CALLSIGN_CHAR_SIZE) {
            extractedChar = AircraftIdentificationMessage.fromSixBitsToChar(Bits.extractUInt(payload, i, CALLSIGN_CHAR_SIZE));
            if (extractedChar == 0) {
                return null;
            }
//            if (extractedChar != ' ') {
                callString.append(extractedChar);
//            }
        }
        CallSign sign = new CallSign(callString.toString().trim());
        return new AircraftIdentificationMessage(rawMessage.timeStampNs(), rawMessage.icaoAddress(), category, sign);
    }

    private static char fromSixBitsToChar(int sixBits) {
        if (0 < sixBits && sixBits <= 26) {
            return (char) (sixBits + 64);
        } else if (sixBits == 32) {
            return ' ';
        } else if (sixBits >= 48 && sixBits <= 57) {
            return (char) sixBits;
        } else {
            return (char) 0;
        }
    }

    /**
     * Return the timestamp of the current message.
     *
     * @return (long) : the timestamp of the message
     */
    @Override
    public long timeStampNs() {
        return timeStampNs;
    }

    /**
     * Return the ICAO address of the aircraft
     *
     * @return (IcaoAddress) : the ICAO address of the aircraft
     */
    @Override
    public IcaoAddress icaoAddress() {
        return icaoAddress;
    }
}
