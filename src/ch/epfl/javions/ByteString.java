package ch.epfl.javions;

import java.util.Arrays;
import java.util.HexFormat;
import java.util.Objects;

/**
 * Class represent a byte String. This is basically an array of bytes but bytes are unsigned and the objects
 * created are immutable.
 *
 * @author Romain Hirschi
 * @author Moussab Tasnim Ibrahim
 */
public final class ByteString {

    private final byte[] bytes;

    /**
     * Constructor of the Byte String. An array of byte is needed in parameter to store it in the attribute.
     *
     * @param bytes (byte[]) : the array of byte to store in the attribute
     */
    public ByteString(byte[] bytes) {
        this.bytes = bytes.clone();
    }

    /**
     * Return the Byte String version of the hexadecimal value given in parameter, stored as a String.
     * Throw an exception if the String is not a hexadecimal number.
     *
     * @param hexString (String) : the String to convert in ByteString
     * @return (ByteString) : the object with the value of the String stored in
     * @throws IllegalArgumentException if the String does not contain a hexadecimal value
     */
    public static ByteString ofHexadecimalString(String hexString) throws IllegalArgumentException {
        Preconditions.checkArgument(hexString.length() % 2 == 0);
        HexFormat hexFormat = HexFormat.of().withUpperCase();
        byte[] value = hexFormat.parseHex(hexString);
        return new ByteString(value);
    }

    /**
     * Give the size of the array of byte.
     *
     * @return (int) : the size of the array of byte.
     */
    public int size() {
        return bytes.length;
    }

    /**
     * Give the value of the byte at the index given in parameter. Throw an Exception if the index is out of
     * the bound of the array.
     *
     * @param index (int) : the index of the byte
     * @return (int) : the value of the byte
     * @throws IndexOutOfBoundsException if the index is out of the bound of the array.
     */
    public int byteAt(int index) throws IndexOutOfBoundsException {
        Objects.checkIndex(index, size());
        return Byte.toUnsignedInt(bytes[index]);
    }

    /**
     * Return the byte between the index "FromIndex" (include) and the index "toIndex" (exclude). These bytes
     * are stored in a long. Throw an Exception if the range of the indexes is out of the bound of the array
     * or if the difference between the "toIndex" and the "fromIndex" is bigger than the size of a long.
     *
     * @param fromIndex (int) : the begin index ("fromIndex")
     * @param toIndex   (int) : the end index ("toIndex)
     * @return (long) : the bytes extracted, stored in a long value
     * @throws IndexOutOfBoundsException if the range of the indexes is out of the bound of the array
     * @throws IllegalArgumentException  if the difference between the "toIndex" and the "fromIndex" is bigger than
     *                                   the size of a long
     */
    public long bytesInRange(int fromIndex, int toIndex) throws IndexOutOfBoundsException, IllegalArgumentException {
        Objects.checkFromToIndex(fromIndex, toIndex, size());
        Objects.checkIndex(toIndex - fromIndex, Long.SIZE);
        HexFormat hexFormat = HexFormat.of().withUpperCase();
        String value = hexFormat.formatHex(bytes, fromIndex, toIndex);
        return Long.decode("0x" + value);
    }

    /**
     * Redefine equals to compare two Byte String. Return true if the object in parameter is a ByteString
     * and has the same array. Return false otherwise.
     *
     * @param obj (Object) : the object to compare.
     * @return (boolean) : true if the object is from the same class and has the same array, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ByteString byteString) {
            return Arrays.equals(byteString.bytes, bytes);
        } else {
            return false;
        }
    }

    /**
     * Redefine hashCode to return the hash code of the array of byte.
     *
     * @return (int) : the hash code of the array
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(bytes);
    }


    /**
     * Redefine toString to convert the array of byte in a String value.
     *
     * @return (String) : a String value of the array of byte
     */
    @Override
    public String toString() {
        HexFormat hexFormat = HexFormat.of().withUpperCase();
        return hexFormat.formatHex(bytes);
    }

}

