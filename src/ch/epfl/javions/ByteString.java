package ch.epfl.javions;

import java.util.Arrays;
import java.util.HexFormat;
import java.util.Objects;

public final class ByteString {

    private final byte[] bytes;

    public ByteString(byte[] bytes) {
        this.bytes = bytes.clone();
    }

    public static ByteString ofHexadecimalString(String hexString) throws IllegalArgumentException {
        if (hexString.length() % 2 == 1) {
            throw new IllegalArgumentException("Not a Hexadecimal number");
        }
        HexFormat hexFormat = HexFormat.of().withUpperCase();
        byte[] value = hexFormat.parseHex(hexString);
        return new ByteString(value);
    }

    public int size() {
        return bytes.length;
    }

    public int byteAt(int index) throws IndexOutOfBoundsException {
        Objects.checkIndex(index, size());
        return bytes[index];
    }


    public long bytesInRange(int fromIndex, int toIndex) throws IndexOutOfBoundsException, IllegalArgumentException {
        Objects.checkFromToIndex(fromIndex, toIndex, size());
        Objects.checkIndex(toIndex - fromIndex, Long.SIZE);
        HexFormat hexFormat = HexFormat.of().withUpperCase();
        String value = hexFormat.formatHex(bytes, fromIndex, toIndex);
        return Long.parseLong(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ByteString byteString) {
           return Arrays.equals(byteString.bytes, bytes);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bytes);
    }


    @Override
    public String toString() {
        HexFormat hexFormat = HexFormat.of().withUpperCase();
        return hexFormat.formatHex(bytes);
    }

}

