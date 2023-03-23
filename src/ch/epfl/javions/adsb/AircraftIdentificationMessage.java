package ch.epfl.javions.adsb;

import ch.epfl.javions.Bits;
import ch.epfl.javions.Preconditions;
import ch.epfl.javions.aircraft.IcaoAddress;

import java.util.Objects;

public record AircraftIdentificationMessage(long timeStampNs, IcaoAddress icaoAddress, int category,
                                            CallSign callSign) implements Message {

    public AircraftIdentificationMessage {
        Objects.requireNonNull(icaoAddress);
        Objects.requireNonNull(callSign);
        Preconditions.checkArgument(timeStampNs >= 0);
    }

    public static AircraftIdentificationMessage of(RawMessage rawMessage) {
        long payload = rawMessage.payload();
        if(!isIdentificationMessage(payload)){
            return null;
        }
        int typeCode = RawMessage.typeCode(payload);
        typeCode = 14 - typeCode;
        int category = (typeCode << 4) + Bits.extractUInt(payload, 48, 3);
        StringBuilder callString = new StringBuilder();
        char extractedChar = ' ';
        for (int i = 42; i >= 0; i -= 6) {
            if (extractedChar == 0) {
                return null;
            }
            extractedChar = AircraftIdentificationMessage.fromSixBitsToChar(Bits.extractUInt(payload, i, 6));
            if (extractedChar != ' '){
                callString.append(extractedChar);

            }
        }
        CallSign sign = new CallSign(callString.toString());
    return new AircraftIdentificationMessage(rawMessage.timeStampNs(), rawMessage.icaoAddress(), category, sign);
    }

    private static char fromSixBitsToChar(int sixBits){
        if(sixBits <= 26){
            return (char) (sixBits + 64);
        } else if (sixBits == 32){
            return ' ';
        } else if (sixBits >= 48 && sixBits <= 57){
            return (char) sixBits ;
        } else {
            return (char) 0;
        }
    }

    private static boolean isIdentificationMessage(long payload){
        long typecode = RawMessage.typeCode(payload);
        return (typecode == 1) || (typecode == 2) || (typecode == 3) || (typecode == 4);
    }

    @Override
    public long timeStampNs() {
        return timeStampNs;
    }

    @Override
    public IcaoAddress icaoAddress() {
        return icaoAddress;
    }
}
