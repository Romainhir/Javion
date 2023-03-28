package ch.epfl.javions.adsb;

import java.util.Objects;

public class AircraftStateAccumulator<T extends AircraftStateSetter> {

    private T stateSetter;
    private AirbornePositionMessage even;
    private AirbornePositionMessage odd;

    public AircraftStateAccumulator(T stateSetter) {
        Objects.requireNonNull(stateSetter);
        this.stateSetter = stateSetter;
    }

    public T stateSetter() {
        return stateSetter;
    }

    public void update(Message message) {
        //TODO Nécessaire ??
        Objects.requireNonNull(message);

        boolean isEven = (message.timeStampNs() % 2 == 0);
        stateSetter.setLastMessageTimeStampNs(message.timeStampNs());
        switch (message) {
            case AircraftIdentificationMessage aim -> {
                stateSetter.setCategory(aim.category());
                stateSetter.setCallSign(aim.callSign());
            }
            case AirbornePositionMessage apm -> {
                stateSetter.setAltitude(apm.altitude());
                if (isEven) {
                    if ((message.timeStampNs() - odd.timeStampNs() <= 1e9) && (even != null)) {
                        stateSetter.setPosition(CprDecoder.decodePosition(apm.x(), apm.y(), odd.x(), odd.y(), 0));
                    }
                    even = apm;
                } else {
                    if ((message.timeStampNs() - even.timeStampNs() <= 10) && (odd != null)) {
                        stateSetter.setPosition(CprDecoder.decodePosition(even.x(), even.y(), apm.x(), apm.y(), 1));
                    }
                    odd = apm;
                }
            }
            case AirborneVelocityMessage avm -> {
                stateSetter.setVelocity(avm.speed());
                stateSetter.setTrackOrHeading(avm.trackOrHeading());
            }
            //TODO Throw une exception ?
            default -> System.out.println("OPPEENNN");
        }
    }
}
