package ch.epfl.javions;

import java.util.Objects;

/**
 * Class dedicated for bits manipulation
 *
 * @author Romain Hirschi
 * @author Moussab Tasnim Ibrahim
 */
public final class Bits {

    private Bits() {
    }

    /**
     * Extract some bits in a long value, from a specific start bit, and with a precise length. These
     * arguments are given in parameter. Throw an exception if the size is not strictly between 0 and 32
     * or if the interval can fit in a long value (start + size bigger than the size of la long).
     *
     * @param value (long) : the number targeted for the extraction
     * @param start (int) : the position of the start bit
     * @param size  (int) : the number of bits to extract
     * @return (int) : the bits extracted
     * @throws IndexOutOfBoundsException if the range start + size is bigger than the size of a long
     * @throws IllegalArgumentException  if the size is not strictly between 0 and 32
     */
    public static int extractUInt(long value, int start, int size) throws IndexOutOfBoundsException, IllegalArgumentException {
        Preconditions.checkArgument((size > 0) && (size < Integer.SIZE));
        Objects.checkFromIndexSize(start, size, Long.SIZE);
        int extracted = (int) (value >>> start);
        int mask = (1 << size) - 1;
        return extracted & mask;
    }

    /**
     * Test if in the given value, the bit's value in the provided index is equl to 1. Throw an Exception if
     * the index is not between 0 (included) and the long size (64) (excluded).
     *
     * @param value (long) : the value targeted for the test
     * @param index (int) : the index of the bit to check
     * @return (boolean) : true if the bit in question is 1, false otherwise
     * @throws IndexOutOfBoundsException
     */
    public static boolean testBit(long value, int index) throws IndexOutOfBoundsException {
        Objects.checkIndex(index, Long.SIZE);
        short mask = 1;
        return ((value >>> index) & mask) == 1;
    }
}
