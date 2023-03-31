package ch.epfl.javions.adsb;

import java.util.Objects;

public class AircraftStateAccumulator<T extends AircraftStateSetter> {

    private T stateSetter;
    private AirbornePositionMessage even;
    private AirbornePositionMessage odd;

    private final static long MAX_TIME_DIFF = 10_000_000_000L;

    public AircraftStateAccumulator(T stateSetter) {
        Objects.requireNonNull(stateSetter);
        this.stateSetter = stateSetter;
    }

    public T stateSetter() {
        return stateSetter;
    }

    public void update(Message message) {

        Objects.requireNonNull(message);
        stateSetter.setLastMessageTimeStampNs(message.timeStampNs());
        switch (message) {
            case AircraftIdentificationMessage aim -> {
                stateSetter.setCategory(aim.category());
                stateSetter.setCallSign(aim.callSign());
            }
            case AirbornePositionMessage apm -> {
                stateSetter.setAltitude(apm.altitude());
                if (apm.parity() == 0) {
                    if(even == null){
                        even = apm;
                    }
                    if ((odd != null) && (message.timeStampNs() - odd.timeStampNs() <= MAX_TIME_DIFF)) {
                        stateSetter.setPosition(CprDecoder.decodePosition(apm.x(), apm.y(), odd.x(), odd.y(), apm.parity()));
                    }
                    even = apm;
                }
                if (apm.parity() == 1){
                    if(odd == null){
                        odd = apm;
                    }
                    if ((even != null) && (message.timeStampNs() - even.timeStampNs() <= MAX_TIME_DIFF)) {
                        stateSetter.setPosition(CprDecoder.decodePosition(even.x(), even.y(), apm.x(), apm.y(), apm.parity()));
                    }
                    odd = apm;
                }
            }
            case AirborneVelocityMessage avm -> {
                stateSetter.setVelocity(avm.speed());
                stateSetter.setTrackOrHeading(avm.trackOrHeading());
            }
            default -> throw new IllegalArgumentException("Unexpected case" );
        }
    }
}
