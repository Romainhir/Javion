package ch.epfl.javions;

public final class Crc24 {
    public static final int GENERATOR = 0xFFF409;
    public static final int CrcLength = 24;

    public Crc24(int generator) {
        generator = generator <<8;
        generator = generator >>> 8;
    }

    public static int crc_bitwise(int generator, byte[] data) {
        int[] tab = {0,generator};
        int crc = 0;
        int maskCrc = 0xFFFFFF;

        for (byte bytes : data) {
            for (int j = 7; 0 <= j; --j) {
                if (Bits.testBit(bytes, j)) {
                    crc = ((crc << 1) | 1) ^ tab[Bits.testBit(crc, CrcLength-1) ? 1 : 0];
                } else {
                    crc = (crc << 1) ^ tab[Bits.testBit(crc, CrcLength-1) ? 1 : 0];
                }
            }
        }
        // Add 24 bits zero at the end of the message
        for(int i = 0; i < CrcLength; ++i){
            crc = (crc << 1) ^ tab[Bits.testBit(crc, 23) ? 1 : 0];
        }
        return crc & maskCrc;
    }
}
