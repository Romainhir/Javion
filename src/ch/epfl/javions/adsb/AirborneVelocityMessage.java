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
 * @author Romain Hirschi
 * @author Moussab Tasnim Ibrahim
 */
public record AirborneVelocityMessage(long timeStampNs, IcaoAddress icaoAddress, double speed,
                                      double trackOrHeading) implements Message {

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
        int subType = Bits.extractUInt(payload, 48, 3);
        double speed = 0;
        double trackOrHeading = 0;
        switch (subType) {
            case 0 -> {
                return null;
            }
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
        }
        return new AirborneVelocityMessage(rawMessage.timeStampNs(),
                rawMessage.icaoAddress(), Units.convertFrom(speed, Units.Speed.KNOT), trackOrHeading);
    }

    private static double [] calculateGroundSpeedAndTrack(long payload) {
        int xSpeed = Bits.extractUInt(payload, 32, 10) ;
        int ySpeed = Bits.extractUInt(payload, 21, 10);
        if (xSpeed == 0 || ySpeed == 0){
            return null;
        }
        xSpeed -= 1;
        ySpeed -= 1;


        xSpeed = (Bits.extractUInt(payload, 42, 1) == 1) ? -xSpeed : xSpeed;
        ySpeed = (Bits.extractUInt(payload, 31, 1) == 1) ? -ySpeed : ySpeed;
        double track;
        if (xSpeed < 0 && ySpeed > 0) {
            track = (Math.PI * 450 / 180) - (Math.atan2(ySpeed, xSpeed));
        } else {
            track = (Math.PI * 90 / 180) - (Math.atan2(ySpeed, xSpeed));

        }
        double speed = Math.hypot(xSpeed, ySpeed);
        if (!(Double.isFinite(speed)) || !(Double.isFinite(track))){

            return null;
        }
        return new double[]{speed, track};
    }

    private static double[] calculateAirSpeedAndHeading(long payload){
        double speed = Bits.extractUInt(payload, 21, 10);

        double heading = Units.convertFrom(
                Bits.extractUInt(payload, 32, 10) / Math.scalb(1, 10),
                Units.Angle.TURN);

        if (speed == 0 || Bits.extractUInt(payload, 42, 1) == 0){
            return null;
        }
        speed -= 1;
        return new double [] {speed, heading};

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
