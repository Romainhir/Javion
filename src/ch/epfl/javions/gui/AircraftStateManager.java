package ch.epfl.javions.gui;

import ch.epfl.javions.adsb.AircraftStateAccumulator;
import ch.epfl.javions.adsb.AircraftStateSetter;
import ch.epfl.javions.adsb.Message;
import ch.epfl.javions.aircraft.AircraftDatabase;
import ch.epfl.javions.aircraft.IcaoAddress;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.SimpleLongProperty;

import java.util.*;

/**
 * Class used to manage the state of all aircraft depending on the messages received.
 *
 * @author Romain Hirschi
 * @author Moussab Tasnim Ibrahim
 */
public final class AircraftStateManager {

    private Map<IcaoAddress, AircraftStateAccumulator> stateAccumulatorMap;
    private Set<ObservableAircraftState> aircraftStateSet;

    private AircraftDatabase db;

    public AircraftStateManager(AircraftDatabase db) {
        //TODO Wtf
        this.db = db;
    }

    /**
     * Return the set of all aircraft observable state where the position is defined.
     *
     * @return (Set < ObservableAircraftState >) : the set of the aircraft observable state
     */
    public Set<ObservableAircraftState> getStates() {
        Set<ObservableAircraftState> posState = new HashSet<>();
        for (ObservableAircraftState obs : aircraftStateSet) {
            if (obs.getPosition() != null) {
                posState.add(obs);
            }
        }
        return posState;
    }

    /**
     * Update the
     *
     * @param message
     */
    public void update(Message message) {
        AircraftStateAccumulator asa = stateAccumulatorMap.get(message.icaoAddress());
        if (asa == null) {
            ObservableAircraftState ass = new ObservableAircraftState(message.icaoAddress());
            asa = new AircraftStateAccumulator<>(ass);
            aircraftStateSet.add(ass);
            stateAccumulatorMap.put(message.icaoAddress(), asa);
        }
        asa.update(message);
    }

    /**
     * Delete all observable state of aircraft that have not sent a new message in the last minute.
     */
    public void purge() {
        //constante ?
        long min = 60000000000L;
        for (ObservableAircraftState observ : aircraftStateSet) {
            if (observ.getLastMessageTimeStampNs() > min) {
                aircraftStateSet.remove(observ);
            }
        }
    }
}
