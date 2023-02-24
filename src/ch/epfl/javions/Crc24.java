package ch.epfl.javions;

public final class Crc24 {
    public static final int GENERATOR = 0xFFF409;

    public Crc24(int generator) {
        generator = generator <<8;
        generator = generator >>> 8;
    }

    public static int crc_bitwise(int generator, byte[] data) {
        int[] tab = {0,generator & 0xFFFFFF};
        int crc = 0;
        for (byte bytes : data) {
            for (int j = 0; j < 8; ++j) {
                crc = ((crc << 1) | bytes) ^ tab[crc >>> 23];
            }
        }
        return crc;
    }
}
