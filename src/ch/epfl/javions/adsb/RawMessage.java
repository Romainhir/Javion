package ch.epfl.javions.adsb;

import ch.epfl.javions.Bits;
import ch.epfl.javions.ByteString;
import ch.epfl.javions.Crc24;
import ch.epfl.javions.Preconditions;
import ch.epfl.javions.aircraft.IcaoAddress;

/**
 * Record that represent a raw message decoded by the ADSB demodulator, where the ME attribute has not been analysed.
 * The raw message contain the data in ByteString and the timestamp of the decoding(long value). This information was
 * given in parameter of the constructor.
 *
 * @param timeStampNs (long) : the timestamp of the decoding
 * @param bytes       (ByteString) : the data of the raw message
 *
 * @author Romain Hirschi (Sciper: 359286)
 * @author Moussab Ibrahim  (Sciper: 363888)
 */
public record RawMessage(long timeStampNs, ByteString bytes) {

    /**
     * The length of a raw message
     */
    public static final int LENGTH = 14;
    private static final Crc24 calculator = new Crc24(Crc24.GENERATOR);
    private static final int TYPECODE_SIZE = 5;
    private static final int DF_VALUE = 17;
    private static final int CA_SIZE = 3;

    /**
     * Constructor of the raw message. In parameter are passed the data in ByteString and the timestamp of the decoding
     * (long value)
     *
     * @param timeStampNs (long) : the timestamp of the decoding
     * @param bytes       (ByteString) : the data of the raw message
     */
    public RawMessage {
        Preconditions.checkArgument(timeStampNs >= 0 && bytes.size() == LENGTH);
    }

    /**
     * Static method to construct easily a new raw message, with the timestamp and the data given in parameter.
     * Return null if the crc of the information is not 0 (meaning that the data has been corrupted).
     *
     * @param timeStampNs (long) : the timestamp of the decoding
     * @param bytes       (byte[]) : the array of byte containing the data of the raw message
     * @return (RawMessage) : a new raw message, with the given value or null if the crc is not 0
     */
    public static RawMessage of(long timeStampNs, byte[] bytes) {
        if (calculator.crc(bytes) != 0) {
            return null;
        } else {
            return new RawMessage(timeStampNs, new ByteString(bytes));
        }
    }

    /**
     * Return the size of a raw message if the DF attribute in the given byte is 17, or 0 otherwise.
     *
     * @param byte0 (byte) : the byte with the DF attribute ot check
     * @return (int) : the size of the raw message if the DF is known (is equal to 17) or 0 otherwise
     */
    public static int size(byte byte0) {
        if ((Byte.toUnsignedInt(byte0) >>> CA_SIZE) == DF_VALUE) {
            return LENGTH;
        } else {
            return 0;
        }
    }

    /**
     * Return the type code (int) of the payload given in parameter
     *
     * @param payload (long) : the payload where the code will be extracted
     * @return (int) : the type code of the payload
     */
    public static int typeCode(long payload) {
        return Bits.extractUInt(payload, 51, TYPECODE_SIZE);
    }

    /**
     * Return the DownlinkFormat (DF) attribute of the data, stored if the first byte.
     *
     * @return (int) : the DF attribute of the data
     */
    public int downLinkFormat() {
        return (bytes.byteAt(0) >>> CA_SIZE);
    }

    /**
     * Return the ICAO address of the raw message.
     *
     * @return (IcaoAddress) : the ICAO address of the message
     */
    public IcaoAddress icaoAddress() {
        return new IcaoAddress(new ByteString(new byte[]
                {(byte) bytes.byteAt(1), (byte) bytes.byteAt(2), (byte) bytes.byteAt(3)}).toString());
    }

    /**
     * Return the payload of the raw message, also named as the ME attribute.
     *
     * @return (long) : the payload of the data
     */
    public long payload() {
        return bytes.bytesInRange(4, 11);
    }

    /**
     * Return the type code of the raw message.
     *
     * @return (int) : the type code of the raw message
     */
    public int typeCode() {
        return typeCode(payload());
    }
}
