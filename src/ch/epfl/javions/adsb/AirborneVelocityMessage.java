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
        switch (subType) {
            case 1 -> {
                int xSpeed = Bits.extractUInt(payload, 11, 10) - 1;
                int ySpeed = Bits.extractUInt(payload, 0, 10) - 1;
                if ((xSpeed <= 0) || (ySpeed <= 0)) {
                    return null;
                }
                xSpeed = (Bits.extractUInt(payload, 21, 1) == 1) ? -xSpeed : xSpeed;
                ySpeed = (Bits.extractUInt(payload, 21, 1) == 1) ? -ySpeed : ySpeed;
                double speed = Math.hypot(xSpeed, ySpeed);
                //TODO
                // double track = Math.atan2(ySpeed, xSpeed);
            }
        }
        return null;
    }

//    private static double calculateSpeed(long payload) {
//        int xSpeed = Bits.extractUInt(payload, 11, 10) - 1;
//        int ySpeed = Bits.extractUInt(payload, 0, 10) - 1;
//    }

    @Override
    public long timeStampNs() {
        return timeStampNs;
    }

    @Override
    public IcaoAddress icaoAddress() {
        return icaoAddress;
    }
}
