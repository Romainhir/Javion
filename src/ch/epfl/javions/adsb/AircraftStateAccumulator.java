package ch.epfl.javions.adsb;

import java.util.Objects;

public class AircraftStateAccumulator<T extends AircraftStateSetter> {

    private T stateSetter;
    private Message even;
    private Message odd;

    public AircraftStateAccumulator(T stateSetter) {
        Objects.requireNonNull(stateSetter);
        this.stateSetter = stateSetter;
    }

    public T stateSetter() {
        return stateSetter;
    }

    public void update(Message message) {
        //TODO store les messages et comparer pour position
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
                    if (message.timeStampNs() - odd.timeStampNs() <= 10) {
                      //  stateSetter.setPosition();
                    }
                } else {
                    if (message.timeStampNs() - even.timeStampNs() <= 10) {
                        //stateSetter.setPosition();
                    }
                }
            }
            case AirborneVelocityMessage avm -> {
                stateSetter.setVelocity(avm.speed());
                stateSetter.setTrackOrHeading(avm.trackOrHeading());
            }
            default -> System.out.println("OPPEENNN");
        }
    }
}
