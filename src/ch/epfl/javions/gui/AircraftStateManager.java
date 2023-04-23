package ch.epfl.javions.gui;

import ch.epfl.javions.adsb.AircraftStateAccumulator;
import ch.epfl.javions.adsb.Message;
import ch.epfl.javions.aircraft.AircraftDatabase;
import ch.epfl.javions.aircraft.IcaoAddress;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.SimpleLongProperty;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final class AircraftStateManager {

    private Map<IcaoAddress, AircraftStateAccumulator> stateAccumulatorMap;
    private Set<ObservableAircraftState> aircraftStateSet;

    public AircraftStateManager(AircraftDatabase db) {
        //TODO
    }

    public Set<ObservableAircraftState> getStates() {
        return Collections.unmodifiableSet(aircraftStateSet);
    }

    public void update(Message message) {
        stateAccumulatorMap.get(message.icaoAddress()).update(message);
    }

    public void purge() {
        //constante ?
        long min = 60000000000L;
        for (ObservableAircraftState observ : aircraftStateSet) {
            ReadOnlyLongProperty longProperty = observ.lastMessageTimeStampNsProperty();
            //Autre manière de get la value ?
            if (longProperty.asObject().getValue() < min) {
                aircraftStateSet.remove(observ);
            }
        }
    }
}
