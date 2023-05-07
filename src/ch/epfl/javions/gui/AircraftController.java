package ch.epfl.javions.gui;

import ch.epfl.javions.aircraft.AircraftData;
import com.sun.javafx.collections.UnmodifiableObservableMap;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.SVGPath;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public final class AircraftController {

    private Pane aircraftPane;
    private MapParameters parameters;
    private ObjectProperty<ObservableAircraftState> selectedAircraft;
    private ObservableSet<ObservableAircraftState> aircraftStateSet;

    public AircraftController(MapParameters parameters, ObservableSet<ObservableAircraftState> aircraftStateSet,
                              ObjectProperty<ObservableAircraftState> aircraftStateProperty) {
        Objects.requireNonNull(parameters);
        Objects.requireNonNull(aircraftStateSet);
        this.parameters = parameters;
        this.aircraftStateSet = new SimpleSetProperty<>(aircraftStateSet);
        selectedAircraft = aircraftStateProperty;
        aircraftPane = new Pane();
        aircraftPane.setPickOnBounds(false);
        aircraftPane.getStylesheets().add("aircraft.css");
        for (ObservableAircraftState observableAircraftState : aircraftStateSet) {
            addAircraft(observableAircraftState);
        }
        selectedAircraft.addListener((observable, oldValue, newValue) -> changeSelectedAircraft());
    }

    public Pane pane() {
        return aircraftPane;
    }

    private void addAircraft(ObservableAircraftState state) {
        AircraftData data = state.getAircraftData();
        /*AircraftIcon icon = AircraftIcon.iconFor(data.typeDesignator(), data.description(),
                state.getCategory(), data.wakeTurbulenceCategory());
        SVGPath path = new SVGPath();
        path.setContent(icon.svgPath());
        path.setOnMouseClicked(event -> {
            selectedAircraft = new SimpleObjectProperty<>(state);
        });
        Node n = path;
        this.aircraftStateSet.addListener((SetChangeListener<? super ObservableAircraftState>) change -> {
            if (!aircraftStateSet.contains(state)) {
                pane().getChildren().remove(n);
            }
        });
        pane().getChildren().add(n);*/
    }

    private void changeSelectedAircraft() {
        throw new UnsupportedOperationException("TODO");
    }

}
