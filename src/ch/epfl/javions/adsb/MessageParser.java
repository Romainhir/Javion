package ch.epfl.javions.adsb;

public class MessageParser {

    private MessageParser() {
    }

    public static Message parse(RawMessage rawMessage) {
        //TODO Moche xd
        switch (rawMessage.typeCode()) {
            case 1, 2, 3, 4:
                return AircraftIdentificationMessage.of(rawMessage);
            case 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 20, 21, 22:
                return AirbornePositionMessage.of(rawMessage);
            case 19:
                return AirborneVelocityMessage.of(rawMessage);
            default:
                return null;
        }
    }


}
