package ch.epfl.javions.gui;

import ch.epfl.javions.GeoPos;
import ch.epfl.javions.Units;
import ch.epfl.javions.WebMercator;
import ch.epfl.javions.aircraft.AircraftData;
import ch.epfl.javions.aircraft.AircraftDatabase;
import ch.epfl.javions.aircraft.IcaoAddress;
import com.sun.javafx.collections.UnmodifiableObservableMap;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
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
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;

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
        this.aircraftStateSet.addListener((SetChangeListener<? super ObservableAircraftState>) change -> {
            if (change.wasAdded()) {
                addAircraft(change.getElementAdded());
            } else if (change.wasRemoved()) {
                removeAircraft(change.getElementRemoved());
            }
        });
        for (ObservableAircraftState observableAircraftState : aircraftStateSet) {
            addAircraft(observableAircraftState);
        }
    }

    public Pane pane() {
        return aircraftPane;
    }

    private void addAircraft(ObservableAircraftState state) {
        AircraftIcon icon = createIcon(state);
        SVGPath path = new SVGPath();
        path.setContent(icon.svgPath());
        path.setViewOrder(state.altitudeProperty().negate().get());
        path.viewOrderProperty().bind(state.altitudeProperty().negate());
        path.setLayoutX(setXPosition(state.getPosition()));
        path.setLayoutY(setYPosition(state.getPosition()));
        path.setRotate(Units.convertTo(state.getTrackOrHeading(), Units.Angle.DEGREE));
        state.trackOrHeadingProperty().addListener((observable, oldValue, newValue) -> {
            path.setRotate(newValue.doubleValue());
        });
        path.setOnMouseClicked(event -> {
            selectedAircraft = new SimpleObjectProperty<>(state);
            changeSelectedAircraft(icon);
        });
        state.positionProperty().addListener((observable, oldValue, newValue) -> {
            path.setLayoutX(setXPosition(newValue));
            path.setLayoutY(setYPosition(newValue));
        });
        parameters.minXProperty().addListener((observable, oldValue, newValue) -> {
            path.setLayoutX(setXPosition(state.getPosition()));
        });
        parameters.minYProperty().addListener((observable, oldValue, newValue) -> {
            path.setLayoutY(setYPosition(state.getPosition()));
        });
        pane().getChildren().add(path);
        iconDisplayed.add(icon);
    }

    private double setXPosition(GeoPos g) {
        return WebMercator.x(parameters.getZoom(),
                g.longitude()) - parameters.getMinX();
    }

    private double setYPosition(GeoPos g) {
        return WebMercator.y(parameters.getZoom(),
                g.latitude()) - parameters.getMinY();
    }

    private void removeAircraft(ObservableAircraftState state) {
        AircraftIcon icon = createIcon(state);
        SVGPath path = new SVGPath();
        path.setContent(icon.svgPath());
        pane().getChildren().remove(path);
        iconDisplayed.remove(icon);
    }

    private AircraftIcon createIcon(ObservableAircraftState state) {
        AircraftData data = state.getAircraftData();
        return AircraftIcon.iconFor(data.typeDesignator(), data.description(),
                state.getCategory(), data.wakeTurbulenceCategory());
    }

    private void changeSelectedAircraft(AircraftIcon icon) {
        Group icaoGroup = new Group();
        Group trajectoryGroup = new Group();
        Group mid = new Group();
        Group label = new Group();
        SVGPath path = new SVGPath();
        Rectangle rectangle = new Rectangle();
        Text text = new Text();

        label.getChildren().addAll(rectangle, text);
        mid.getChildren().addAll(path, label);
        icaoGroup.getChildren().addAll(mid, trajectoryGroup);
        icaoGroup.setId("adr.\u2002OACI");
        trajectoryGroup.getStyleClass().add("trajectory");
        label.getStyleClass().add("label");
        path.setContent(icon.svgPath());
        path.getStyleClass().add("aircraft");
        icaoGroup.layoutXProperty().bind(Bindings.createDoubleBinding(() ->
                        WebMercator.x(parameters.getZoom(),
                                selectedAircraft.get().getPosition().longitude()) - parameters.getMinX()
                , parameters.zoomProperty(), selectedAircraft.get().positionProperty(), parameters.minXProperty()));
        icaoGroup.layoutYProperty().bind(Bindings.createDoubleBinding(() ->
                        WebMercator.y(parameters.getZoom(), selectedAircraft.get().getPosition().latitude()) - parameters.getMinY()
                , parameters.zoomProperty(), selectedAircraft.get().positionProperty(), parameters.minYProperty()));
        text.setText(selectedAircraft.get().getIcaoAddress().string());
        pane().getChildren().add(icaoGroup);
        System.out.println("YEEEEEEEEEEETTTTT");
    }

}
