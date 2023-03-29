package ch.epfl.javions.adsb;

import ch.epfl.javions.Bits;
import ch.epfl.javions.Preconditions;
import ch.epfl.javions.aircraft.IcaoAddress;

import java.util.Objects;

/**
 * Record that represent an airborne identification message. We store in the record the timestamp of the message,
 * the ICAO address, the category and the calldign of the aircraft.
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
        /*if(!isIdentificationMessage(payload)){
            return null;
        }*/
        int typeCode = RawMessage.typeCode(payload);
        typeCode = 14 - typeCode;
        int category = (typeCode << 4) + Bits.extractUInt(payload, 48, 3);
        StringBuilder callString = new StringBuilder();
        char extractedChar = ' ';
        for (int i = 42; i >= 0; i -= 6) {
            extractedChar = AircraftIdentificationMessage.fromSixBitsToChar(Bits.extractUInt(payload, i, 6));
            if (extractedChar == 0) {
                return null;
            }
            if (extractedChar != ' ') {
                callString.append(extractedChar);
            }
        }
        CallSign sign = new CallSign(callString.toString());
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

    private static boolean isIdentificationMessage(long payload) {
        long typecode = RawMessage.typeCode(payload);
        return (typecode == 1) || (typecode == 2) || (typecode == 3) || (typecode == 4);
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
