package ch.epfl.javions.gui;

import ch.epfl.javions.aircraft.AircraftData;
import ch.epfl.javions.aircraft.AircraftDatabase;
import ch.epfl.javions.aircraft.IcaoAddress;
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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class AircraftController {

    private Pane aircraftPane;
    private MapParameters parameters;
    private ObjectProperty<ObservableAircraftState> selectedAircraft;
    private ObservableSet<ObservableAircraftState> aircraftStateSet;
    private Set<AircraftIcon> iconDisplayed;

    public AircraftController(MapParameters parameters, ObservableSet<ObservableAircraftState> aircraftStateSet,
                              ObjectProperty<ObservableAircraftState> aircraftStateProperty) {
        Objects.requireNonNull(parameters);
        Objects.requireNonNull(aircraftStateSet);
        this.parameters = parameters;
        this.aircraftStateSet = new SimpleSetProperty<>(aircraftStateSet);
        iconDisplayed = new HashSet<>();
        selectedAircraft = aircraftStateProperty;
        aircraftPane = new Pane();
        aircraftPane.setPickOnBounds(false);
        aircraftPane.getStylesheets().add("aircraft.css");
        for (ObservableAircraftState observableAircraftState : aircraftStateSet) {
            addAircraft(observableAircraftState);
        }
        try {
            addAircraft(new ObservableAircraftState(new IcaoAddress("344645"), new AircraftDatabase("resources/aircraft.zip")));
        } catch (Exception e) {
        }
        this.aircraftStateSet.addListener((SetChangeListener<? super ObservableAircraftState>) change -> {
            if (change.wasAdded()) {
                addAircraft(change.getElementAdded());
            } else if (change.wasRemoved()) {
                removeAircraft(change.getElementRemoved());
            }
        });
    }

    public Pane pane() {
        return aircraftPane;
    }

    private void addAircraft(ObservableAircraftState state) {
        AircraftData data = state.getAircraftData();
        AircraftIcon icon = AircraftIcon.iconFor(data.typeDesignator(), data.description(),
                state.getCategory(), data.wakeTurbulenceCategory());
        SVGPath path = new SVGPath();
        path.setContent(icon.svgPath());
        path.setLayoutX(Math.random() * 50 + 1);
        path.setLayoutY(Math.random() * 50 + 1);
        path.setOnMouseClicked(event -> {
            changeSelectedAircraft(state);
        });
        pane().getChildren().add(path);
        iconDisplayed.add(icon);
    }

    private void removeAircraft(ObservableAircraftState state) {
        AircraftData data = state.getAircraftData();
        AircraftIcon icon = AircraftIcon.iconFor(data.typeDesignator(), data.description(),
                state.getCategory(), data.wakeTurbulenceCategory());
        SVGPath path = new SVGPath();
        path.setContent(icon.svgPath());
        pane().getChildren().remove(path);
        iconDisplayed.remove(icon);
    }

    private void changeSelectedAircraft(ObservableAircraftState state) {
        selectedAircraft = new SimpleObjectProperty<>(state);
        Group icaoGroup = new Group();
        Group trajectoryGroup = new Group();
        Group temp = new Group();
        Group label = new Group();
        SVGPath path = new SVGPath();
        System.out.println("YEEEEEEEEEEETTTTT");
    }

}
