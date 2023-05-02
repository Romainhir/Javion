package ch.epfl.javions.gui;

import javafx.beans.property.ObjectProperty;
import javafx.scene.layout.Pane;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public final class AircraftController {

    private Pane aircraftPane;
    private MapParameters parameters;
    private ObjectProperty<ObservableAircraftState> selectedAircraft;
    private Set<ObservableAircraftState> aircraftStateSet;

    public AircraftController(MapParameters parameters, Set<ObservableAircraftState> aircraftStateSet,
                              ObjectProperty<ObservableAircraftState> aircraftStateProperty) {
        Objects.requireNonNull(parameters);
        Objects.requireNonNull(aircraftStateSet);
        this.parameters = parameters;
        this.aircraftStateSet = Collections.unmodifiableSet(aircraftStateSet);
        selectedAircraft = aircraftStateProperty;
        aircraftPane = new Pane();
        aircraftPane.setPickOnBounds(false);
        for (ObservableAircraftState observableAircraftState : aircraftStateSet) {
            //TODO
        }
    }

    public Pane pane() {
        return aircraftPane;
    }

    private void addAircraft() {
        throw new UnsupportedOperationException("TODO");
    }
}
