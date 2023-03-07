package ch.epfl.javions.adsb;

import ch.epfl.javions.ByteString;
import ch.epfl.javions.Crc24;
import ch.epfl.javions.Preconditions;

public record RawMessage(long timeStampNs, ByteString bytes) {
    public final static int LENGTH = 14;
    private static final Crc24 calculator = new Crc24(Crc24.GENERATOR);
    public RawMessage {
        Preconditions.checkArgument(timeStampNs >= 0 && bytes.size() == LENGTH);
    }

    public static RawMessage of(long timeStampNs, byte[] bytes){
        if(calculator.crc(bytes) != 0){
            return null;
        }else {
            return new RawMessage(timeStampNs, new ByteString(bytes));
        }
    }


}
