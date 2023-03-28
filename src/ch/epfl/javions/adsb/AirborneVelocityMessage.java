package ch.epfl.javions.adsb;

import ch.epfl.javions.Bits;
import ch.epfl.javions.Preconditions;
import ch.epfl.javions.aircraft.IcaoAddress;

import java.util.Objects;

public record AirborneVelocityMessage(long timeStampNs, IcaoAddress icaoAddress, double speed,
                                      double trackOrHeading) implements Message {

    public AirborneVelocityMessage {
        Objects.requireNonNull(icaoAddress);
        Preconditions.checkArgument((timeStampNs >= 0) && (speed >= 0) && (trackOrHeading >= 0));
    }

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
                double [] speedAndTrackArray = calculateGroundSpeedAndTrack(payload);
                if (speedAndTrackArray == null){
                    return null;
                }
                speed = speedAndTrackArray[0];
                trackOrHeading = speedAndTrackArray[1];
            }
            case 2 -> {
                double [] speedAndTrackArray = calculateGroundSpeedAndTrack(payload);
                if (speedAndTrackArray == null){
                    return null;
                }
                speed = 4 * speedAndTrackArray[0];
                trackOrHeading = speedAndTrackArray[1];
            }
            case 3 -> {
                double [] speedAndHeadingArray = calculateAirSpeedAndHeading(payload);
                if (speedAndHeadingArray == null){
                    return null;
                }
                speed = speedAndHeadingArray[0];
                trackOrHeading = speedAndHeadingArray[1];
            }
            case 4 -> {
                double [] speedAndHeadingArray = calculateAirSpeedAndHeading(payload);
                if (speedAndHeadingArray == null){
                    return null;
                }
                speed = 4 * speedAndHeadingArray[0];
                trackOrHeading = speedAndHeadingArray[1];
            }
        }
        return new AirborneVelocityMessage(rawMessage.timeStampNs(),
                rawMessage.icaoAddress(), speed, trackOrHeading);
    }

    private static double [] calculateGroundSpeedAndTrack(long payload) {
        int xSpeed = Bits.extractUInt(payload, 32, 10) - 1;
        int ySpeed = Bits.extractUInt(payload, 21, 10) - 1;
        if ((xSpeed <= 0) || (ySpeed <= 0)) {
            return null;
        }
        xSpeed = (Bits.extractUInt(payload, 42, 1) == 1) ? -xSpeed : xSpeed;
        ySpeed = (Bits.extractUInt(payload, 31, 1) == 1) ? -ySpeed : ySpeed;
        double track;
        if (xSpeed < 0 && ySpeed > 0) {
            track = 450 - (Math.atan2(ySpeed, xSpeed)) / Math.PI * 180;
        }else {
            track = 90 - (Math.atan2(ySpeed, xSpeed)) / Math.PI * 180;

        }
        return new double [] {Math.hypot(xSpeed, ySpeed), track};
    }

    private static double[] calculateAirSpeedAndHeading(long payload){
        double speed = Bits.extractUInt(payload, 21, 10) - 1;
        double heading = Bits.extractUInt(payload, 32, 10) / Math.scalb(2.d, 10);
        if (speed <= 0 || Bits.extractUInt(payload, 42, 1) == 0){
            return null;
        }
        return new double [] {speed, heading};
    }

    @Override
    public long timeStampNs() {
        return timeStampNs;
    }

    @Override
    public IcaoAddress icaoAddress() {
        return icaoAddress;
    }
}
