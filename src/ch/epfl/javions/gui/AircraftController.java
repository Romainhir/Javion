package ch.epfl.javions.gui;

import ch.epfl.javions.GeoPos;
import ch.epfl.javions.Units;
import ch.epfl.javions.WebMercator;
import ch.epfl.javions.aircraft.AircraftData;
import ch.epfl.javions.aircraft.AircraftDescription;
import ch.epfl.javions.aircraft.AircraftTypeDesignator;
import ch.epfl.javions.aircraft.WakeTurbulenceCategory;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Objects;

/**
 * Class used to control the view of the aircraft on the map. It stores and shows also the properties of the selected
 * aircraft.
 *
 * @author Romain Hirschi
 * @author Moussab Ibrahim
 */
public final class AircraftController {

    private Pane aircraftPane;
    private MapParameters parameters;
    private ObjectProperty<ObservableAircraftState> selectedAircraft;
    private ObservableSet<ObservableAircraftState> aircraftStateSet;

    /**
     * Constructor of the aircraft controller. In parameter is given the parameters of the map, the set of
     * observable aircraft state (the set is observable) and the observable aircraft state of the selected aircraft.
     * The map parameters and the aircraft state set must not be null.
     *
     * @param parameters            (MapParameters) : the parameters of the map (non-null)
     * @param aircraftStateSet      (ObservableSet<ObservableAircraftState>) : the observable set of observable aircraft state
     * @param aircraftStateProperty (ObjectProperty<ObservableAircraftState>) :  the observable aircraft state of the selected aircraft
     */
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

    /**
     * Return the pane that contain all the aircraft.
     *
     * @return (Pane) :  the pane with all the aircraft
     */
    public Pane pane() {
        return aircraftPane;
    }

    private void addAircraft(ObservableAircraftState state) {
        //Create the icon and the svg path
        AircraftIcon icon = createIcon(state);
        SVGPath path = new SVGPath();

        //Set up the svg path
        path.setContent(icon.svgPath());
        path.setViewOrder(state.altitudeProperty().negate().get());
        path.viewOrderProperty().bind(state.altitudeProperty().negate());
        path.setLayoutX(setXPosition(state.getPosition()));
        path.setLayoutY(setYPosition(state.getPosition()));
        path.setFill(ColorRamp.PLASMA.at(colorFormula(state.getAltitude())));
        path.setStroke(Color.BLACK);
        path.setOnMouseClicked(event -> selectedAircraft.set(state));
        path.setId(state.getIcaoAddress().string());

        //Set up listener to update the position/orientation of the icon
        state.trackOrHeadingProperty().addListener((observable, oldValue, newValue) ->
                path.setRotate(icon.canRotate() ? Units.convertTo(newValue.doubleValue(), Units.Angle.DEGREE) : 0));
        state.positionProperty().addListener((observable, oldValue, newValue) -> {
            path.setLayoutX(setXPosition(newValue));
            path.setLayoutY(setYPosition(newValue));
        });
        state.altitudeProperty().addListener((observable, oldValue, newValue) ->
                path.setFill(ColorRamp.PLASMA.at(colorFormula(newValue.doubleValue()))));

        //Set up listener to relocate icon if the map is moved
        parameters.minXProperty().addListener((observable, oldValue, newValue) ->
                path.setLayoutX(setXPosition(state.getPosition())));
        parameters.minYProperty().addListener((observable, oldValue, newValue) ->
                path.setLayoutY(setYPosition(state.getPosition())));

        //Create the group to show when the aircraft is selected or when the zoom is very high
        createSelectedAircraft(state);

        //Add the icon on the pane
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
        pane().getChildren().removeIf(node -> node.getId().equals(state.getIcaoAddress().string()));
    }

    private AircraftIcon createIcon(ObservableAircraftState state) {
        AircraftData data = state.getAircraftData();
        AircraftIcon icon = data != null
                ? AircraftIcon.iconFor(data.typeDesignator() != null ? data.typeDesignator() : new AircraftTypeDesignator(""),
                data.description() != null ? data.description() : new AircraftDescription(""),
                state.getCategory(),
                data.wakeTurbulenceCategory() != null ? data.wakeTurbulenceCategory() : WakeTurbulenceCategory.UNKNOWN)
                : AircraftIcon.iconFor(new AircraftTypeDesignator(""), new AircraftDescription(""), state.getCategory(),
                WakeTurbulenceCategory.UNKNOWN);
        return icon;
    }

    private void createSelectedAircraft(ObservableAircraftState state) {
        //Create all JFX Object
        Group icaoGroup = new Group();
        Group mid = new Group();
        Group label = new Group();
        Rectangle rectangle = new Rectangle();
        Text text = new Text();
        Group trajectoryGroup = new Group();

        //Linking object together
        label.getChildren().addAll(rectangle, text);
        mid.getChildren().add(label);
        icaoGroup.getChildren().addAll(mid, trajectoryGroup);
        icaoGroup.setId("adr.\u2002OACI");
        label.getStyleClass().add("label");
        trajectoryGroup.getStyleClass().add("trajectory");

        //Set up listener/binding to the trajectory group
        trajectoryGroup.visibleProperty().bind(
                Bindings.createBooleanBinding(() -> (selectedAircraft.get() == state), selectedAircraft));
        trajectoryGroup.layoutXProperty().bind(
                Bindings.createDoubleBinding(() -> -parameters.getMinX() - icaoGroup.getLayoutX(),
                        parameters.minXProperty(), icaoGroup.layoutXProperty()));
        trajectoryGroup.layoutYProperty().bind(
                Bindings.createDoubleBinding(() -> -parameters.getMinY() - icaoGroup.getLayoutY(),
                        parameters.minYProperty(), icaoGroup.layoutYProperty()));
        trajectoryGroup.visibleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                createTrajectoryGroup(trajectoryGroup, state.getAirbornePos());
            }
        });

        //Listener used to draw the trajectory if needed
        state.airbornePosProperty().addListener((ListChangeListener<? super ObservableAircraftState.AirbornePos>) c -> {
            if (trajectoryGroup.isVisible()) {
                createTrajectoryGroup(trajectoryGroup, (List<ObservableAircraftState.AirbornePos>) c.getList());
            }
        });

        //Listener used to draw the trajectory when changing the zoom value
        parameters.zoomProperty().addListener((observable, oldValue, newValue) -> {
            if (trajectoryGroup.isVisible()) {
                createTrajectoryGroup(trajectoryGroup, state.getAirbornePos());
            }
        });

        //Set up binding to update the position of the whole group according to the aircraft position
        //and to know if it should be visible or not
        icaoGroup.layoutXProperty().bind(
                Bindings.createDoubleBinding(() -> WebMercator.x(parameters.getZoom(),
                                state.getPosition().longitude()) - parameters.getMinX()
                        , parameters.zoomProperty(), state.positionProperty(), parameters.minXProperty()));
        icaoGroup.layoutYProperty().bind(
                Bindings.createDoubleBinding(() -> WebMercator.y(parameters.getZoom(),
                                state.getPosition().latitude()) - parameters.getMinY(),
                        parameters.zoomProperty(), state.positionProperty(), parameters.minYProperty()));
        icaoGroup.visibleProperty().bind(
                Bindings.createBooleanBinding(() -> (parameters.getZoom() >= 11) || (selectedAircraft.get() == state),
                        parameters.zoomProperty(), selectedAircraft));

        //Bind the rectangle's dimension according to the text's dimension
        rectangle.widthProperty().bind(text.layoutBoundsProperty().map(bounds -> bounds.getWidth() + 4));
        rectangle.heightProperty().bind(text.layoutBoundsProperty().map(bounds -> bounds.getHeight() + 4));

        //Set and update the text with the correct values
        text.setText(state.getIcaoAddress().string()
                + "\n"
                + (state.getVelocity() > 0d ? String.format("%f km/h", Units.convertFrom(state.getVelocity(), Units.Speed.KNOT)) : "?")
                + " "
                + (state.getAltitude() > 0 ? String.format("%f.0 m", state.getAltitude()) : "?"));
        text.textProperty().bind(
                Bindings.createStringBinding(() -> state.getIcaoAddress().string()
                                + "\n"
                                + String.format("%.0f km/h", Math.rint(Units.convertTo
                                (state.getVelocity(), Units.Speed.KILOMETER_PER_HOUR)))
                                + " "
                                + String.format("%.0f m", Math.rint(state.getAltitude())), state.altitudeProperty(),
                        state.velocityProperty()));

        //Add the group to the pane
        pane().getChildren().add(icaoGroup);
    }

    private void createTrajectoryGroup(Group trajectoryGroup, List<ObservableAircraftState.AirbornePos> list) {
        trajectoryGroup.getChildren().clear();
        ObservableAircraftState.AirbornePos last;
        ObservableAircraftState.AirbornePos current;
        for (int i = 1; i < list.size(); i++) {
            last = list.get(i - 1);
            current = list.get(i);
            if (last.pos() != null) {
                Line line = new Line(WebMercator.x(parameters.getZoom(), last.pos().longitude()),
                        WebMercator.y(parameters.getZoom(), last.pos().latitude()),
                        WebMercator.x(parameters.getZoom(), current.pos().longitude()),
                        WebMercator.y(parameters.getZoom(), current.pos().latitude()));
                if (Math.floor(last.altitude()) == Math.floor(current.altitude())) {
                    line.setStroke(ColorRamp.PLASMA.at(colorFormula(current.altitude())));
                } else {
                    LinearGradient linearGradient = new LinearGradient(
                            0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                            new Stop(0, ColorRamp.PLASMA.at(colorFormula(last.altitude()))),
                            new Stop(1, ColorRamp.PLASMA.at(colorFormula(current.altitude()))));
                    line.setStroke(linearGradient);
                }
                trajectoryGroup.getChildren().add(line);
            }
        }
    }

}
