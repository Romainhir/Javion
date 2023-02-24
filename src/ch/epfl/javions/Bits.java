package ch.epfl.javions;

import java.util.Objects;

public final class Bits {

    private Bits() {
    }

    public static int extractUInt(long value, int start, int size) throws IndexOutOfBoundsException, IllegalArgumentException {
        if (size <= 0 || 32 <= size) {
            throw new IllegalArgumentException("Extraction interrupted, invalid size");
        }
        Objects.checkFromIndexSize(start, size, Long.SIZE);
        int extracted = (int) (value >>> start);
        int mask = (1 << size) - 1;
        return extracted & mask;
    }

    public static boolean testBit(long value, int index) throws IndexOutOfBoundsException {
        Objects.checkIndex(index, Long.SIZE);
        short mask = 1;
        return ((value >>> index) & mask) == 1;
    }
}
