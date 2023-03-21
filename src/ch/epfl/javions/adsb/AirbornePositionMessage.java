package ch.epfl.javions.adsb;

import ch.epfl.javions.Bits;
import ch.epfl.javions.GeoPos;
import ch.epfl.javions.Preconditions;
import ch.epfl.javions.Units;
import ch.epfl.javions.aircraft.IcaoAddress;

import java.util.Objects;

public record AirbornePositionMessage(long timeStampNs, IcaoAddress icaoAddress, double altitude, int parity, double x,
                                      double y) implements Message {

    public AirbornePositionMessage {
        Objects.requireNonNull(icaoAddress);
        Preconditions.checkArgument((timeStampNs >= 0) && ((parity == 0) || (parity == 1)) &&
                (x >= 0) && (x < 1) && (y >= 0) && (y < 1));
    }

    public static AirbornePositionMessage of(RawMessage rawMessage) {
        long payload = rawMessage.payload();
        int lat_cpr = Bits.extractUInt(payload, 0, 17);
        int lon_cpr = Bits.extractUInt(payload, 17, 17);
        int format = Bits.extractUInt(payload, 34, 1);
        int alt = Bits.extractUInt(payload, 36, 12);
        try {
//            AirbornePositionMessage apm = new AirbornePositionMessage(rawMessage.timeStampNs(), rawMessage.icaoAddress(),
//                    )
        } catch (Exception e) {

        }
        return null;
    }

    private double decodeAltitude(int rawAlt) {
        byte q = (byte) ((rawAlt >>> 4) & 1);
        if (q == 1) {
            rawAlt = rawAlt ^ 8;
            return Units.convert(rawAlt * 25 - 1000, Units.Length.FOOT, Units.Length.METER);
        } else {
            int sort = 0;
            byte[] index = {4, 10, 5, 11};
            for (int i = 0; i < index.length; i++) {
                int j = index[i];
                for (int k = j; k < j + 6; k += 2) {
                    sort = sort & (rawAlt >>> k);
                    sort = sort << 1;
                }
            }
            
        }
        return 0;
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
