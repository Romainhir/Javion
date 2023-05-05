package ch.epfl.javions.gui;

import ch.epfl.javions.adsb.AircraftStateAccumulator;
import ch.epfl.javions.adsb.AircraftStateSetter;
import ch.epfl.javions.adsb.Message;
import ch.epfl.javions.aircraft.AircraftDatabase;
import ch.epfl.javions.aircraft.IcaoAddress;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.ReadOnlySetProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

/**
 * Class used to manage the state of all aircraft depending on the messages received.
 *
 * @author Romain Hirschi
 * @author Moussab Tasnim Ibrahim
 */
public final class AircraftStateManager {

    private Map<IcaoAddress, AircraftStateAccumulator<ObservableAircraftState>> stateAccumulatorMap;
    private ObservableSet<ObservableAircraftState> aircraftStateSet;

    private AircraftDatabase db;
    private long lastMessageTimeStampNs = 0;
    public final static long ONEMIN = 60000000000L;

    public AircraftStateManager(AircraftDatabase db) {
        stateAccumulatorMap = new HashMap<>();
        aircraftStateSet = FXCollections.observableSet();
        this.db = db;
    }

    /**
     * Return the set of all aircraft observable state where the position is defined.
     *
     * @return (Set < ObservableAircraftState >) : the set of the aircraft observable state
     */
    public ObservableSet<ObservableAircraftState> getStates() {
        return FXCollections.unmodifiableObservableSet(aircraftStateSet);
    }

    /**
     * Update the
     *
     * @param message
     */
    public void update(Message message) throws IOException {
        if(stateAccumulatorMap.containsKey(message.icaoAddress())){
            stateAccumulatorMap.get(message.icaoAddress()).update(message);
            if(stateAccumulatorMap.get(message.icaoAddress()).stateSetter().getPosition() != null){

                aircraftStateSet.add(stateAccumulatorMap.get(message.icaoAddress()).stateSetter());
            }
        }else {
            stateAccumulatorMap.put(message.icaoAddress(),
                    new AircraftStateAccumulator<>(new ObservableAircraftState(message.icaoAddress(), db)));
        }
        lastMessageTimeStampNs = message.timeStampNs();

    }

    /**
     * Delete all observable state of aircraft that have not sent a new message in the last minute.
     */
    public void purge() {
        aircraftStateSet.removeIf
                (observableAircraftState -> lastMessageTimeStampNs - observableAircraftState.getLastMessageTimeStampNs() > ONEMIN);
    }
}
