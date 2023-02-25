package ch.epfl.javions;

public final class Crc24 {
    public static final int GENERATOR = 0xFFF409;

    public Crc24(int generator) {
        generator = generator <<8;
        generator = generator >>> 8;
    }

    public static int crc_bitwise(int generator, byte[] data) {
        int[] tab = {0,generator};
        int crc = 0;
        byte[] dataEnhanced = new byte[data.length + 3];
        for(int i = 0; i < dataEnhanced.length; ++i){
            if(i < data.length) {
                dataEnhanced[i] = data[i];
            } else {
                dataEnhanced[i] = 0;
            }
        }

        for (byte bytes : dataEnhanced) {
            for (int j = 7; 0 <= j; --j) {
                if (Bits.testBit(bytes, j)) {
                    crc = ((crc << 1) | 1) ^ tab[Bits.testBit(crc, 23) ? 1 : 0];
                } else {
                    crc = (crc << 1) ^ tab[Bits.testBit(crc, 23) ? 1 : 0];
                }
            }
        }
        return crc;
    }
}
