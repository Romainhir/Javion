package ch.epfl.javions;

import java.util.Objects;

public final class Bits {

    private Bits() {
    }

    //TODO : A tester avec JUnit
    public int extractUInt(long value, int start, int size) throws IndexOutOfBoundsException, IllegalArgumentException {
        if (size <= 0) {
            throw new IllegalArgumentException("Extraction interrupted, invalid size");
        }
        Objects.checkFromIndexSize(size, size, Long.SIZE);
        int extracted = (int) (value >>> start);
        int mask = 1 >> Long.SIZE - size;
        int invertedMask = mask ^ (1 >>> (Integer.SIZE-1));
        extracted = extracted & invertedMask;
        return extracted;
    }

    public boolean testBit(long value, int index) throws IndexOutOfBoundsException {
        Objects.checkIndex(index, Long.SIZE);
        short mask = 1;
        return ((value >>> index) & mask) == 1;
    }
}
