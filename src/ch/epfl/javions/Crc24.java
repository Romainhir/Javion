package ch.epfl.javions;

public final class Crc24 {
    public static final int GENERATOR = 0xFFF409;
    public static final int CrcLength = 24;
    private static int[] table;
    private final static int maskCrc = 0x00FFFFFF;

    public Crc24(int generator) {
        generator = generator <<8;
        generator = generator >>> 8;
        table = buildTable(generator);
    }

    public static int crc_bitwise(int generator, byte[] data) {
        int[] tab = {0,generator};
        int crc = 0;
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

    private static int[] buildTable(int generator){
        int[] tab = new int[256];
        for(int i = 0; i < 256; ++i){
           tab[i] = Crc24.crc_bitwise(generator, new byte[]{(byte) i});
            }
        return tab;
    }
    public static int crc(byte[] data) {
        int crc = 0;
        for (byte bytes : data) {
            crc = ((crc << 8) |  Byte.toUnsignedInt(bytes)) ^ table[(crc & maskCrc) >>> 16];
        }

        for(int i = 0; i < 3; ++i){
            crc = (crc << 8) ^ table[(crc & maskCrc) >>> 16];
        }
        return crc & maskCrc;
    }


}
