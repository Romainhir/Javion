package ch.epfl.javions.gui;

import ch.epfl.javions.Units;
import ch.epfl.javions.adsb.CallSign;
import ch.epfl.javions.aircraft.AircraftData;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Class used to control the aircraft table, under the map.
 *
 * @author Romain Hirschi (Sciper: 359286)
 * @author Moussab Ibrahim  (Sciper: 363888)
 */
public final class AircraftTableController {

    private static final int POSITION_DIGITS = 4;
    private static final int WIDTH_ONE = 60;
    private static final int WIDTH_TWO = 70;
    private static final int WIDTH_THREE = 90;
    private static final int WIDTH_FOUR = 230;
    private static final int WIDTH_FIVE = 50;
    private static final int NUMERIC_WIDTH = 85;
    private static final String NUMERIC_SYTLE = "numeric";
    private static final String TABLE_CSS = "table.css";
    private static final String COL1_NAME = "ICAO";
    private static final String COL2_NAME = "CallSign";
    private static final String COL3_NAME = "Immatriculation";
    private static final String COL4_NAME = "Modèle";
    private static final String COL5_NAME = "Type";
    private static final String COL6_NAME = "Description";
    private static final String COL7_NAME = "Longitude(°)";
    private static final String COL8_NAME = "Latitude(°)";
    private static final String COL9_NAME = "Altitude(m)";
    private static final String COL10_NAME = "Vitesse(km/h)";
    private ObservableSet<ObservableAircraftState> aircraftStateSet;
    private ObjectProperty<ObservableAircraftState> observedAircraft;
    private TableView<ObservableAircraftState> table;

    /**
     * Constructor of the aircraft table controller. In parameter is given the aircraft state set and the selected aircraft.
     * These two arguments are observable.
     *
     * @param aircraftStateSet (ObservableSet<ObservableAircraftState>) : the observable set of observable aircraft state
     * @param observedAircraft (ObjectProperty<ObservableAircraftState>) : the observed aircraft state
     */
    public AircraftTableController(ObservableSet<ObservableAircraftState> aircraftStateSet,
                                   ObjectProperty<ObservableAircraftState> observedAircraft) {
        table = new TableView<>();
        this.aircraftStateSet = aircraftStateSet;
        this.observedAircraft = observedAircraft;

        table.getStylesheets().add(TABLE_CSS);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_SUBSEQUENT_COLUMNS);
        table.setTableMenuButtonVisible(true);

        TableColumn<ObservableAircraftState, String> ICAOCol = DataColumn(f ->
                new ReadOnlyObjectWrapper<>(f.getValue().getIcaoAddress().string()), COL1_NAME);

        TableColumn<ObservableAircraftState, String> CallSignCol = DataColumn(f ->
                f.getValue().callSignProperty().map(CallSign::string), COL2_NAME);


        TableColumn<ObservableAircraftState, String> ImmatriculationCol =
                DataColumn(f -> new ReadOnlyObjectWrapper<>(f.getValue().getAircraftData())
                        .map(s -> s.registration().string()), COL3_NAME);

        TableColumn<ObservableAircraftState, String> ModelCol =
                DataColumn(f -> new ReadOnlyObjectWrapper<>(f.getValue().getAircraftData())
                        .map(AircraftData::model), COL4_NAME);

        TableColumn<ObservableAircraftState, String> TypeCol =
                DataColumn(f -> new ReadOnlyObjectWrapper<>(f.getValue().getAircraftData())
                                .map(s -> s.typeDesignator().string())
                        , COL5_NAME);

        TableColumn<ObservableAircraftState, String> DescriptionCol =
                DataColumn(f -> new ReadOnlyObjectWrapper<>(f.getValue().getAircraftData())
                                .map(s -> s.description().string()),
                        COL6_NAME);

        TableColumn<ObservableAircraftState, String> LongitudeCol =
                NumberColumn(POSITION_DIGITS, COL7_NAME, f ->
                        Units.convertTo(f.getValue().getPosition().longitude(), Units.Angle.DEGREE));

        TableColumn<ObservableAircraftState, String> LatitudeCol =
                NumberColumn(POSITION_DIGITS, COL8_NAME, f ->
                        Units.convertTo(f.getValue().getPosition().latitude(), Units.Angle.DEGREE));

        TableColumn<ObservableAircraftState, String> AltitudeCol =
                NumberColumn(0, COL9_NAME, f ->
                        Math.rint(f.getValue().getAltitude()));

        TableColumn<ObservableAircraftState, String> VelocityCol =
                NumberColumn(0, COL10_NAME, f ->
                        Math.rint(Units.convertTo(f.getValue().getVelocity(), Units.Speed.KILOMETER_PER_HOUR)));

        ICAOCol.setPrefWidth(WIDTH_ONE);
        CallSignCol.setPrefWidth(WIDTH_TWO);
        ImmatriculationCol.setPrefWidth(WIDTH_THREE);
        ModelCol.setPrefWidth(WIDTH_FOUR);
        TypeCol.setPrefWidth(WIDTH_FIVE);
        DescriptionCol.setPrefWidth(WIDTH_TWO);

        LongitudeCol.setPrefWidth(NUMERIC_WIDTH);
        LatitudeCol.setPrefWidth(NUMERIC_WIDTH);
        AltitudeCol.setPrefWidth(NUMERIC_WIDTH);
        VelocityCol.setPrefWidth(NUMERIC_WIDTH);


        table.getColumns().addAll(ICAOCol, CallSignCol, ImmatriculationCol, ModelCol,
                TypeCol, DescriptionCol, LongitudeCol, LatitudeCol, AltitudeCol, VelocityCol);

        aircraftStateSet.addListener((SetChangeListener<? super ObservableAircraftState>) change -> {
            if (change.wasAdded()) {
                table.getItems().add(change.getElementAdded());
                table.sort();

                change.getElementAdded().positionProperty().addListener(posChange -> {
                    table.refresh();
                });

                change.getElementAdded().altitudeProperty().addListener(altChange -> {
                    table.refresh();
                });
                change.getElementAdded().velocityProperty().addListener(velChange -> {
                    table.refresh();
                });
            }
            if (change.wasRemoved()) {
                table.getItems().remove(change.getElementRemoved());
            }
        });

        observedAircraft.addListener(current -> {
            if (!(observedAircraft.equals(table.getSelectionModel().selectedItemProperty()))) {
                table.scrollTo(observedAircraft.get());
                table.getSelectionModel().select(observedAircraft.get());
            }
        });

        table.getSelectionModel().selectedItemProperty().addListener(selected -> {
            if (!(observedAircraft.equals(table.getSelectionModel().selectedItemProperty()))) {
                observedAircraft.set(table.getSelectionModel().getSelectedItem());
            }
        });
    }


    /**
     * Return the pane (accurately the table view) that contain all information about the aircraft drawn in the map.
     *
     * @return (TableView<ObservableAircraftState>) : the table view with all information about the aircraft
     */
    public TableView<ObservableAircraftState> pane() {
        return table;
    }


    private TableColumn<ObservableAircraftState, String>
    DataColumn(Function<TableColumn.CellDataFeatures<ObservableAircraftState, String>,
            ObservableValue> operator, String name) {
        TableColumn<ObservableAircraftState, String> column = new TableColumn<>(name);
        column.setCellValueFactory(operator::apply);
        return column;
    }

    private TableColumn<ObservableAircraftState, String> NumberColumn
            (int nbOfDigits, String name,
             Function<TableColumn.CellDataFeatures<ObservableAircraftState, String>,
                     Double> operator) {
        TableColumn<ObservableAircraftState, String> column = new TableColumn<>(name);
        column.getStyleClass().add(NUMERIC_SYTLE);
        //One comparator for all numeric columns is sufficient because we only compare double.
        column.setComparator((n1, n2) -> {
            try {
                NumberFormat nf = NumberFormat.getInstance();
                return Double.compare(nf.parse(n1).doubleValue(), nf.parse(n2).doubleValue());
            } catch (ParseException e) {
                throw new Error(e);
            }
        });
        column.setCellValueFactory(f -> {
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMinimumFractionDigits(0);
            nf.setMaximumFractionDigits(nbOfDigits);
            // We also wrap altitude and velocity eventhough they are already in an observable object because we don't
            // want to modify the original value itself, so it is a new formatted value that is wrapped and observed.
            return new ReadOnlyObjectWrapper<>(nf.format(operator.apply(f)));

        });
        return column;
    }

    /**
     * Define what happened if we double-clock on an aircraft in the table.
     *
     * @param consumer (Comsumer<ObservableAircraftState>) : the consumer of the action
     */
    public void setOnDoubleClick(Consumer<ObservableAircraftState> consumer) {
        table.setOnMouseClicked((event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2
                    && observedAircraft.get() != null) {
                consumer.accept(observedAircraft.get());
            }
        });


    }

}
