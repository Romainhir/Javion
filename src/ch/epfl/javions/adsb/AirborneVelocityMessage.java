package ch.epfl.javions.adsb;

import ch.epfl.javions.Bits;
import ch.epfl.javions.Preconditions;
import ch.epfl.javions.Units;
import ch.epfl.javions.aircraft.IcaoAddress;

import java.util.Objects;

/**
 * Record that represent an airborne velocity message. We store in the record the timestamp of the message,
 * the ICAO address, the speed and the heading/track of the aircraft.
 *
 * @param timeStampNs    (long) : the timestamp of the message
 * @param icaoAddress    (IcaoAddress) : the ICAO address of the aircraft
 * @param speed          (double) : the speed of the aircraft
 * @param trackOrHeading (double) : the heading/track of the aircraft
 * @author Romain Hirschi (Sciper: 359286)
 * @author Moussab Ibrahim  (Sciper: 363888)
 */
public record AirborneVelocityMessage(long timeStampNs, IcaoAddress icaoAddress, double speed,
                                      double trackOrHeading) implements Message {

    private static final int START_SUBTYPE = 48;
    private static final int SIZE_SUBTYPE = 3;
    private static final int START_XSPEED = 32;
    private static final int START_SPEED = 21;
    private static final int SIZE_SPEED = 10;
    private static final int START_SIGNX = 42;
    private static final int START_SIGNY = 31;

    /**
     * Constructor of the record. In parameter is given the record the timestamp of the message,
     * the ICAO address, the speed and the heading/track of the aircraft
     *
     * @param timeStampNs    (long) : the timestamp of the message
     * @param icaoAddress    (IcaoAddress) : the ICAO address of the aircraft
     * @param speed          (double) : the speed of the aircraft
     * @param trackOrHeading (double) : the heading/track of the aircraft
     */
    public AirborneVelocityMessage {
        Objects.requireNonNull(icaoAddress);
        Preconditions.checkArgument((timeStampNs >= 0) && (speed >= 0) && (trackOrHeading >= 0));
    }

    /**
     * Decode the given raw message and create an airborne velocity message corresponding to the decoded value.
     *
     * @param rawMessage (RawMessage) : the raw message to decode
     * @return (AirborneVelocityMessage) : the airborne velocity message
     */
    public static AirborneVelocityMessage of(RawMessage rawMessage) {
        long payload = rawMessage.payload();
        int subType = Bits.extractUInt(payload, START_SUBTYPE, SIZE_SUBTYPE);
        double speed = 0;
        double trackOrHeading = 0;
        switch (subType) {
            case 1 -> {
                double[] speedAndTrackArray = calculateGroundSpeedAndTrack(payload);
                if (speedAndTrackArray == null) {
                    return null;
                }
                speed = speedAndTrackArray[0];
                trackOrHeading = speedAndTrackArray[1];
            }
            case 2 -> {
                double[] speedAndTrackArray = calculateGroundSpeedAndTrack(payload);
                if (speedAndTrackArray == null) {
                    return null;
                }
                speed = 4 * speedAndTrackArray[0];
                trackOrHeading = speedAndTrackArray[1];
            }
            case 3 -> {
                double[] speedAndHeadingArray = calculateAirSpeedAndHeading(payload);
                if (speedAndHeadingArray == null) {
                    return null;
                }
                speed = speedAndHeadingArray[0];
                trackOrHeading = speedAndHeadingArray[1];
            }
            case 4 -> {
                double[] speedAndHeadingArray = calculateAirSpeedAndHeading(payload);
                if (speedAndHeadingArray == null) {
                    return null;
                }
                speed = 4 * speedAndHeadingArray[0];
                trackOrHeading = speedAndHeadingArray[1];
            }
            default -> {
                return null;
            }
        }
        return new AirborneVelocityMessage(rawMessage.timeStampNs(),
                rawMessage.icaoAddress(), Units.convertFrom(speed, Units.Speed.KNOT), trackOrHeading);
    }

    private static double[] calculateGroundSpeedAndTrack(long payload) {
        int xSpeed = Bits.extractUInt(payload, START_XSPEED, SIZE_SPEED);
        int ySpeed = Bits.extractUInt(payload, START_SPEED, SIZE_SPEED);
        if (xSpeed == 0 || ySpeed == 0) {
            return null;
        }
        xSpeed -= 1;
        ySpeed -= 1;


        xSpeed = (Bits.extractUInt(payload, START_SIGNX, 1) == 1) ? -xSpeed : xSpeed;
        ySpeed = (Bits.extractUInt(payload, START_SIGNY, 1) == 1) ? -ySpeed : ySpeed;
        double track;
        if (xSpeed < 0 && ySpeed >= 0) {
            track = (Math.PI * 450 / 180) - (Math.atan2(ySpeed, xSpeed));
        } else {
            track = (Math.PI * 90 / 180) - (Math.atan2(ySpeed, xSpeed));

        }
        double speed = Math.hypot(xSpeed, ySpeed);
        if (!(Double.isFinite(speed)) || !(Double.isFinite(track))) {

            return null;
        }
        return new double[]{speed, track};
    }

    private static double[] calculateAirSpeedAndHeading(long payload) {
        double speed = Bits.extractUInt(payload, START_SPEED, SIZE_SPEED);

        double heading = Units.convertFrom(
                Bits.extractUInt(payload, START_XSPEED, SIZE_SPEED) / Math.scalb(1, 10),
                Units.Angle.TURN);

        if (speed == 0 || Bits.extractUInt(payload, START_SIGNX, 1) == 0) {
            return null;
        }
        speed -= 1;
        return new double[]{speed, heading};

    }

    /**
     * Return the timestamp of the current message.
     *
     * @return (long) : the timestamp of the message
     */
    @Override
    public long timeStampNs() {
        return timeStampNs;
    }

    /**
     * Return the ICAO address of the aircraft
     *
     * @return (IcaoAddress) : the ICAO address of the aircraft
     */
    @Override
    public IcaoAddress icaoAddress() {
        return icaoAddress;
    }
}
