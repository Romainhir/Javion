package ch.epfl.javions.adsb;

import ch.epfl.javions.ByteString;
import ch.epfl.javions.Crc24;
import ch.epfl.javions.Preconditions;
import ch.epfl.javions.aircraft.IcaoAddress;

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

    public static int size(byte byte0){
        if((Byte.toUnsignedInt(byte0) >>> 3) == 17){
            return LENGTH;
        }else{
            return 0;
        }
    }

    public static int typeCode(long payload){
        int i = (int)(payload >>> 51);
        return (int)(payload >>> 51);
    }

    public int downLinkFormat(){
        return (bytes.byteAt(0) >>> 3);
    }

    public IcaoAddress icaoAddress(){
        //TODO
        return null;
    }
}
