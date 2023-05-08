package ch.epfl.javions.gui;

import ch.epfl.javions.Units;
import ch.epfl.javions.adsb.CallSign;
import ch.epfl.javions.aircraft.AircraftData;
import ch.epfl.javions.aircraft.IcaoAddress;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableSetValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;

import javax.swing.text.TabableView;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public final class AircraftTableController {

    private ObservableSet<ObservableAircraftState> aircraftStateSet;
    private ObservableObjectValue<ObservableAircraftState> observedAircraft;
    private TableView<ObservableAircraftState> table = new TableView<>();

    public AircraftTableController(ObservableSet<ObservableAircraftState> aircraftStateSet,
                                   ObservableObjectValue<ObservableAircraftState>  observedAircraft){
        this.aircraftStateSet = aircraftStateSet;
        this.observedAircraft = observedAircraft;
    }


    public TableView<ObservableAircraftState> pane(){
        table.getStylesheets().add("table.css");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_SUBSEQUENT_COLUMNS);
        table.setTableMenuButtonVisible(true);

        TableColumn<ObservableAircraftState, String> ICAOCol = ICAOColumn();
        TableColumn<ObservableAircraftState, String> CallSignCol = CallSignColumn();

        TableColumn<ObservableAircraftState, String> ImmatriculationCol =
                DataColumn(f -> f.registration().string(), "Immatriculation");

        TableColumn<ObservableAircraftState, String> ModelCol =
                DataColumn(AircraftData::model, "Modèle");

        TableColumn<ObservableAircraftState, String> TypeCol =
                DataColumn(f -> f.typeDesignator().string(), "Type");

        TableColumn<ObservableAircraftState, String> DescriptionCol =
                DataColumn(f -> f.description().string(), "Description");

        TableColumn<ObservableAircraftState, String> LongitudeCol =
                NumberColumn(4, "Longitude(°)", f ->
                        Units.convertTo(f.getValue().getPosition().longitude(), Units.Angle.DEGREE));

        TableColumn<ObservableAircraftState, String> LatitudeCol =
                NumberColumn(4, "Latitude(°)", f ->
                        Units.convertTo(f.getValue().getPosition().latitude(), Units.Angle.DEGREE));

        TableColumn<ObservableAircraftState, String> AltitudeCol =
                NumberColumn(0, "Altitude(m)", f ->
                       Math.rint(f.getValue().getAltitude()));

        TableColumn<ObservableAircraftState, String> VelocityCol =
                NumberColumn(0, "Vitesse(km/h)", f ->
                        Math.rint(Units.convertTo(f.getValue().getVelocity(), Units.Speed.KILOMETER_PER_HOUR)));

        ICAOCol.setPrefWidth(60);
        CallSignCol.setPrefWidth(70);
        ImmatriculationCol.setPrefWidth(90);
        ModelCol.setPrefWidth(230);
        TypeCol.setPrefWidth(50);
        DescriptionCol.setPrefWidth(70);

        LatitudeCol.getStyleClass().add("numeric");
        LongitudeCol.getStyleClass().add("numeric");
        AltitudeCol.getStyleClass().add("numeric");
        VelocityCol.getStyleClass().add("numeric");

        table.getColumns().addAll(ICAOCol, CallSignCol, ImmatriculationCol, ModelCol,
                TypeCol, DescriptionCol, LongitudeCol, LatitudeCol, AltitudeCol, VelocityCol);

        aircraftStateSet.addListener((SetChangeListener<? super ObservableAircraftState>) change -> {
            if(change.wasAdded()){
                table.getItems().add(change.getElementAdded());
                table.sort();

                change.getElementAdded().positionProperty().addListener( posChange -> {
                    table.refresh();
                });

                change.getElementAdded().altitudeProperty().addListener( altChange -> {
                    table.refresh();
                });
                change.getElementAdded().velocityProperty().addListener( velChange -> {
                    table.refresh();
                });
            }
            if(change.wasRemoved()){
                table.getItems().remove(change.getElementRemoved());
            }
        });

        /*observedAircraft.addListener( current -> {
            table.getSelectionModel().select();
        });*/


        return table;
    }

    private TableColumn<ObservableAircraftState, String> ICAOColumn(){
        TableColumn<ObservableAircraftState, String> column = new TableColumn<>("OACI");
        column.setCellValueFactory(f ->
                       new ReadOnlyObjectWrapper<>(f.getValue().getIcaoAddress().string()));

        return column;
    }

    private TableColumn<ObservableAircraftState, String> CallSignColumn(){
        TableColumn<ObservableAircraftState, String> column = new TableColumn<>("Indicatif");
        column.setCellValueFactory(f ->
                f.getValue().callSignProperty().map(CallSign::string));
        return column;
    }

    private TableColumn<ObservableAircraftState, String>
    DataColumn(Function<AircraftData, String> operator, String name){
        TableColumn<ObservableAircraftState, String> column = new TableColumn<>(name);
        column.setCellValueFactory(f -> {
            AircraftData ad = f.getValue().getAircraftData();
            return new ReadOnlyObjectWrapper<>(ad).map(operator::apply);
        });
        return column;
    }

    private TableColumn<ObservableAircraftState, String> NumberColumn
            (int nbOfDigits, String name,
             Function<TableColumn.CellDataFeatures<ObservableAircraftState, String>,
                     Double> operator){
        TableColumn<ObservableAircraftState, String> column = new TableColumn<>(name);
        column.setCellValueFactory(f -> {
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMinimumFractionDigits(0);
            nf.setMaximumFractionDigits(nbOfDigits);
            double value = operator.apply(f);
            return new ReadOnlyObjectWrapper<>(nf.format(value));

        });
        return column;
    }

    public void setOnDoubleClick(Consumer<ObservableAircraftState> consumer){
        consumer.accept(observedAircraft.get());
    }

}
