package ch.epfl.javions.adsb;

import ch.epfl.javions.Bits;
import ch.epfl.javions.GeoPos;
import ch.epfl.javions.Preconditions;
import ch.epfl.javions.Units;
import ch.epfl.javions.aircraft.IcaoAddress;

import java.util.Objects;

public record AirbornePositionMessage(long timeStampNs, IcaoAddress icaoAddress, double altitude, int parity, double x,
                                      double y) implements Message {


    private final static int POSITION_SIZE = (1 << 17);

    public AirbornePositionMessage {
        Objects.requireNonNull(icaoAddress);
        Preconditions.checkArgument((timeStampNs >= 0) && ((parity == 0) || (parity == 1)) &&
                (x >= 0) && (x < 1) && (y >= 0) && (y < 1));
    }

    public static AirbornePositionMessage of(RawMessage rawMessage) {
        long payload = rawMessage.payload();
        if (!isPositioningMessage(payload)) {
            return null;
        }
        double lat_cpr = (double) Bits.extractUInt(payload, 0, 17) / POSITION_SIZE;
        double lon_cpr = (double) Bits.extractUInt(payload, 17, 17) / POSITION_SIZE;
        int format = Bits.extractUInt(payload, 34, 1);
        double alt = decodeAltitude(Bits.extractUInt(payload, 36, 12));

        if (alt < 0 || !isValidAlt(alt)) {
            return null;
        }
        return new AirbornePositionMessage(rawMessage.timeStampNs(),
                rawMessage.icaoAddress(), alt, format, lat_cpr, lon_cpr);
    }

    private static boolean isPositioningMessage(long payload) {
        long typecode = RawMessage.typeCode(payload);
        return (9 <= typecode && typecode <= 18) || (20 <= typecode && typecode <= 22);
    }

    private static double decodeAltitude(int rawAlt) {
        byte q = (byte) ((rawAlt >>> 4) & 1);
        byte tmp = (byte) (rawAlt & 0xf);
        if (q == 1) {
            rawAlt = (rawAlt >>> 5);
            int reformated = (tmp | (rawAlt << 4)) & 0x7ff;

            return Units.convert(reformated * 25 - 1000, Units.Length.FOOT, Units.Length.METER);
        } else {
            int sorted = rearrange(rawAlt);
            int mostWeightBits = grayToBinary(Bits.extractUInt(sorted, 3, 9), 9);
            int leastWeightBits = grayToBinary(Bits.extractUInt(sorted, 0, 3), 3);

            if (leastWeightBits == 0 || leastWeightBits == 5 || leastWeightBits == 6) {
                return Double.MAX_VALUE;
            } else if (leastWeightBits == 7) {
                leastWeightBits = 5;
            } else {
                leastWeightBits = 6 - leastWeightBits;
            }
            return Units.convert(-1300 + leastWeightBits * 100 + mostWeightBits * 500,
                    Units.Length.FOOT, Units.Length.METER);
        }

    }

    private static boolean isValidAlt(double alt) {
        return alt != Double.MAX_VALUE;
    }

    private static int rearrange(int rawAlt) {
        int sorted = 0;
        byte[] index = {4, 10, 5, 11};
        for (int i = 0; i < index.length; i++) {
            int j = index[i];
            for (int k = j; j - 4 <= k; k -= 2) {
                sorted = sorted << 1;
                sorted = sorted | Bits.extractUInt(rawAlt, k, 1);
            }
        }
        return sorted;
    }

    private static int grayToBinary(int gray, int size) {
        int binary = gray;
        for (int i = 1; i < size; ++i) {
            binary = binary ^ (gray >>> i);
        }
        return binary;
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
