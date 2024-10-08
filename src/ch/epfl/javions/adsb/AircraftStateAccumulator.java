package ch.epfl.javions.adsb;

import ch.epfl.javions.GeoPos;

import java.util.Objects;

/**
 * Class that represent the aircraft state accumulator. Precisely, it stacks messages from an aircraft to update
 * his state, with an aircraft state setter (generic, given in parameter).
 *
 * @param <T> (AircraftStateSetter) : the state setter of the aircraft
 * @author Romain Hirschi (Sciper: 359286)
 * @author Moussab Ibrahim  (Sciper: 363888)
 */
public class AircraftStateAccumulator<T extends AircraftStateSetter> {

    private T stateSetter;
    private AirbornePositionMessage even;

    private AirbornePositionMessage odd;

    private final static long MAX_TIME_DIFF = 10_000_000_000L;

    /**
     * Constructor of the aircraft state accumulator. In parameter is given the aircraft state setter.
     *
     * @param stateSetter (<T extends AircraftStateSetter>) : the aircraft state setter
     */

    public AircraftStateAccumulator(T stateSetter) {
        Objects.requireNonNull(stateSetter);
        this.stateSetter = stateSetter;
    }

    /**
     * Return the aircraft state setter given in the constructor.
     *
     * @return (<T extends AircraftStateSetter>) : the aircraft state setter
     */
    public T stateSetter() {
        return stateSetter;
    }

    /**
     * Update the state of the aircraft with the information provided in the message in parameter.
     *
     * @param message (Message) : the message containing the information to update
     */
    public void update(Message message) {

        Objects.requireNonNull(message);
        stateSetter.setLastMessageTimeStampNs(message.timeStampNs());
        switch (message) {
            case AircraftIdentificationMessage aim -> {
                stateSetter.setCategory(aim.category());
                stateSetter.setCallSign(aim.callSign());
            }
            case AirbornePositionMessage apm -> {
                if (apm.parity() == 0) {
                    even = apm;
                    if ((odd != null) && (message.timeStampNs() - odd.timeStampNs() <= MAX_TIME_DIFF)) {
                        GeoPos position = CprDecoder.decodePosition(apm.x(), apm.y(), odd.x(), odd.y(), apm.parity());
                        if (position != null){
                            stateSetter.setPosition(position);
                        }
                    }
                }
                if (apm.parity() == 1){
                    odd = apm;
                    if ((even != null) && (message.timeStampNs() - even.timeStampNs() <= MAX_TIME_DIFF)) {
                        GeoPos position = CprDecoder.decodePosition(even.x(), even.y(), apm.x(), apm.y(), apm.parity());
                        if (position != null){
                            stateSetter.setPosition(position);
                        }
                    }
                }
                stateSetter.setAltitude(apm.altitude());
            }
            case AirborneVelocityMessage avm -> {
                stateSetter.setVelocity(avm.speed());
                stateSetter.setTrackOrHeading(avm.trackOrHeading());
            }
            default -> throw new IllegalArgumentException("Unexpected case");
        }
    }
}
