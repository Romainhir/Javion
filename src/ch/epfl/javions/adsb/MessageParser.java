package ch.epfl.javions.adsb;

/**
 * Class used to parse the raw message to the correct type of message
 *
 * @author Romain Hirschi (Sciper: 359286)
 * @author Moussab Ibrahim  (Sciper: 363888)
 */
public class MessageParser {

    private MessageParser() {
    }

    /**
     * Parse the raw message given in parameter to the correct type of message, returned by the method.
     *
     * @param rawMessage (RawMessage) : the raw message to parse
     * @return (Message) : the message parsed in the correct type of message
     */
    public static Message parse(RawMessage rawMessage) {
        return switch (rawMessage.typeCode()) {
            case 1, 2, 3, 4 -> AircraftIdentificationMessage.of(rawMessage);
            case 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 20, 21, 22 -> AirbornePositionMessage.of(rawMessage);
            case 19 -> AirborneVelocityMessage.of(rawMessage);
            default -> null;
        };
    }


}
