package ch.epfl.javions;

public final class Crc24 {
    public static final int GENERATOR = 0xFFF409;
    public static final int CRC_LENGTH = 24;
    private final int[] table;
    private final static int MASK_CRC = 0x00FFFFFF;

    /**
     * Constructor of the CRC24 calculator with a given generator
     * @param generator (int)
     */
    public Crc24(int generator) {
        generator = generator & MASK_CRC;
        table = buildTable(generator);
    }

    /**
     * Calculate the CRC24 of a message bit per bit with a given generator
     * @param generator (int) generator of 24 bit
     * @param data (data[]) the message stored in a tab of byte
     * @return (int) CRC24
     */
    private static int crc_bitwise(int generator, byte[] data) {
        int[] tab = {0, generator};
        int crc = 0;
        for (byte bytes : data) {
            for (int j = 7; 0 <= j; --j) {
                crc = ((crc << 1) | (Bits.testBit(bytes, j) ? 1 : 0))
                        ^ tab[Bits.testBit(crc, CRC_LENGTH - 1) ? 1 : 0];

            }
        }
        // Add 24 bits zero at the end of the message
        for (int i = 0; i < CRC_LENGTH; ++i) {
            crc = (crc << 1) ^ tab[Bits.testBit(crc, 23) ? 1 : 0];
        }
        return crc & MASK_CRC;
    }

    /**
     * Build a table with a given generator, and it is used in the method crc below
     * @param generator (int)
     * @return (int[]) the table
     */
    private static int[] buildTable(int generator) {
        int[] tab = new int[256];
        for (int i = 0; i < 256; ++i) {
            tab[i] = Crc24.crc_bitwise(generator, new byte[]{(byte) i});
        }
        return tab;
    }

    /**
     * Calculate the CRC24 of a message byte per byte with a given data
     * @param data (byte[]) the message stored in a tab of byte
     * @return (int) CRC24
     */
    public int crc(byte[] data) {
        int crc = 0;
        for (byte bytes : data) {
            crc = ((crc << 8) | Byte.toUnsignedInt(bytes)) ^ table[(crc & MASK_CRC) >>> 16];
        }

        for (int i = 0; i < 3; ++i) {
            crc = (crc << 8) ^ table[(crc & MASK_CRC) >>> 16];
        }
        return crc & MASK_CRC;
    }


}
