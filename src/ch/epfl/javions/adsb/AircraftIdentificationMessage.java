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
        int category = Bits.extractUInt(payload, 48, 8);
        String callString = "";
        for (int i = 42; i >= 0; i -= 6) {
            callString = callString + "" + Bits.extractUInt(payload, i, 6);
        }
        CallSign sign = new CallSign(callString);
        try {
            AircraftIdentificationMessage aim = new AircraftIdentificationMessage(rawMessage.timeStampNs(),
                    rawMessage.icaoAddress(), category, sign);
            return aim;
        } catch (Exception n) {
            return null;
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
