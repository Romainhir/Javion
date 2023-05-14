package ch.epfl.javions.gui;

import ch.epfl.javions.GeoPos;
import ch.epfl.javions.Units;
import ch.epfl.javions.WebMercator;
import ch.epfl.javions.aircraft.*;
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
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;

import java.util.*;

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
        path.setFill(ColorRamp.PLASMA.at(colorFormula(state.getAltitude())));
        path.setStroke(Color.BLACK);
        state.trackOrHeadingProperty().addListener((observable, oldValue, newValue) -> {
            path.setRotate(icon.canRotate() ? Units.convertTo(newValue.doubleValue(), Units.Angle.DEGREE) : 0);
        });
        path.setOnMouseClicked(event -> {
            selectedAircraft.set(state);
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
        state.trackOrHeadingProperty().addListener((observable, oldValue, newValue) -> {
            state.setPosition(state.getPosition());
        });
        state.altitudeProperty().addListener((observable, oldValue, newValue) ->
                path.setFill(ColorRamp.PLASMA.at(colorFormula(newValue.doubleValue()))));
        createSelectedAircraft(state);
        pane().getChildren().add(path);
    }

    private double colorFormula(double value) {
        return Math.cbrt(value / 12000);
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
    }

    private AircraftIcon createIcon(ObservableAircraftState state) {
        AircraftData data = state.getAircraftData();
        AircraftIcon icon = data != null ?
                AircraftIcon.iconFor(data.typeDesignator() != null ? data.typeDesignator() : new AircraftTypeDesignator(""),
                        data.description() != null ? data.description() : new AircraftDescription(""),
                        state.getCategory(), data.wakeTurbulenceCategory() != null ? data.wakeTurbulenceCategory() : WakeTurbulenceCategory.UNKNOWN)
                : AircraftIcon.iconFor(new AircraftTypeDesignator(""), new AircraftDescription(""), state.getCategory(),
                WakeTurbulenceCategory.UNKNOWN);
        return icon;
    }

    private void createSelectedAircraft(ObservableAircraftState state) {
        Group icaoGroup = new Group();
        Group mid = new Group();
        Group label = new Group();
        Rectangle rectangle = new Rectangle();
        Text text = new Text();
        Group trajectoryGroup = new Group();
        label.getChildren().addAll(rectangle, text);
        mid.getChildren().add(label);
        icaoGroup.getChildren().addAll(mid, trajectoryGroup);
        icaoGroup.setId("adr.\u2002OACI");
        label.getStyleClass().add("label");
        state.airbornePosProperty().addListener((ListChangeListener<? super ObservableAircraftState.AirbornePos>) c -> {
            if (trajectoryGroup.isVisible()) {
                createTrajectoryGroup(trajectoryGroup, (List<ObservableAircraftState.AirbornePos>) c.getList());
            }
        });
        trajectoryGroup.getStyleClass().add("trajectory");
        trajectoryGroup.visibleProperty().bind(Bindings.createBooleanBinding(() -> (selectedAircraft.get() == state), selectedAircraft));
        trajectoryGroup.layoutXProperty().bind(Bindings.createDoubleBinding(() -> -parameters.getMinX() - icaoGroup.getLayoutX(),
                parameters.minXProperty(), icaoGroup.layoutXProperty()));
        trajectoryGroup.layoutYProperty().bind(Bindings.createDoubleBinding(() -> -parameters.getMinY() - icaoGroup.getLayoutY(),
                parameters.minYProperty(), icaoGroup.layoutYProperty()));
        trajectoryGroup.visibleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                createTrajectoryGroup(trajectoryGroup, state.getAirbornePos());
            }
        });
        parameters.zoomProperty().addListener((observable, oldValue, newValue) -> {
            createTrajectoryGroup(trajectoryGroup, state.getAirbornePos());
        });
        icaoGroup.layoutXProperty().bind(Bindings.createDoubleBinding(() ->
                        WebMercator.x(parameters.getZoom(),
                                state.getPosition().longitude()) - parameters.getMinX()
                , parameters.zoomProperty(), state.positionProperty(), parameters.minXProperty()));
        icaoGroup.layoutYProperty().bind(Bindings.createDoubleBinding(() ->
                        WebMercator.y(parameters.getZoom(), state.getPosition().latitude()) - parameters.getMinY()
                , parameters.zoomProperty(), state.positionProperty(), parameters.minYProperty()));
        rectangle.widthProperty().bind(text.layoutBoundsProperty().map(bounds -> bounds.getWidth() + 4));
        rectangle.heightProperty().bind(text.layoutBoundsProperty().map(bounds -> bounds.getHeight() + 4));
        text.setText(state.getIcaoAddress().string() + "\n" +
                String.format("%f km/h", Units.convertFrom(state.getVelocity(), Units.Speed.KNOT)) + " " +
                String.format("%f.0 m", state.getAltitude()));
        text.textProperty().bind(Bindings.createStringBinding(() -> state.getIcaoAddress().string() + "\n" +
                        String.format("%.1f km/h", state.getVelocity()) + " " +
                        String.format("%.1f m", state.getAltitude()), state.altitudeProperty(),
                state.velocityProperty()));
        icaoGroup.visibleProperty().bind(Bindings.createBooleanBinding(() -> (parameters.getZoom() >= 11) ||
                (selectedAircraft.get() == state), parameters.zoomProperty(), selectedAircraft));
        pane().getChildren().add(icaoGroup);
        rectangle.setOpacity(1d);
    }

    private void createTrajectoryGroup(Group trajectoryGroup, List<ObservableAircraftState.AirbornePos> list) {
        trajectoryGroup.getChildren().clear();
        ObservableAircraftState.AirbornePos last;
        ObservableAircraftState.AirbornePos current;
        for (int i = 1; i < list.size(); i++) {
            last = list.get(i - 1);
            current = list.get(i);
            Line line = new Line(WebMercator.x(parameters.getZoom(), last.pos().longitude()),
                    WebMercator.y(parameters.getZoom(), last.pos().latitude()),
                    WebMercator.x(parameters.getZoom(), current.pos().longitude()),
                    WebMercator.y(parameters.getZoom(), current.pos().latitude()));
            if (Math.floor(last.altitude()) == Math.floor(current.altitude())) {
                line.setStroke(ColorRamp.PLASMA.at(colorFormula(current.altitude())));
            } else {
                LinearGradient linearGradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                        new Stop(0, ColorRamp.PLASMA.at(colorFormula(last.altitude()))), new Stop(1, ColorRamp.PLASMA.at(colorFormula(current.altitude()))));
                line.setStroke(linearGradient);
            }
            trajectoryGroup.getChildren().add(line);
        }
    }

}
