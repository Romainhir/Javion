package ch.epfl.javions.gui;

import ch.epfl.javions.adsb.CallSign;
import ch.epfl.javions.aircraft.AircraftData;
import ch.epfl.javions.aircraft.IcaoAddress;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableSet;
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
    private ObservableAircraftState observedAircraft;
    private TableView<ObservableAircraftState> table = new TableView<>();

    public AircraftTableController(ObservableSet<ObservableAircraftState> aircraftStateSet,
                                   ObservableAircraftState observedAircraft){
        this.aircraftStateSet = aircraftStateSet;
        this.observedAircraft = observedAircraft;
    }


    public TableView<ObservableAircraftState> pane(){
        table.getStylesheets().add("table.css");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_SUBSEQUENT_COLUMNS);
        table.setTableMenuButtonVisible(true);

        TableColumn<ObservableAircraftState, String> ICAOCol = ICAOColumn();
        TableColumn<ObservableAircraftState, String> CallSignCol = CallSignColumn();
        TableColumn<ObservableAircraftState, String> ImmatriculationCol = DataColumn(f -> f.typeDesignator().string());
        TableColumn<ObservableAircraftState, String> ModelCol = DataColumn(AircraftData::model);
        TableColumn<ObservableAircraftState, String> TypeCol = DataColumn(f -> f.typeDesignator().string());
        TableColumn<ObservableAircraftState, String> DescriptionCol = DataColumn(f -> f.description().string());

        ICAOCol.setPrefWidth(60);
        CallSignCol.setPrefWidth(70);
        ImmatriculationCol.setPrefWidth(90);
        ModelCol.setPrefWidth(230);
        TypeCol.setPrefWidth(50);
        DescriptionCol.setPrefWidth(70);


        return table;
    }

    private TableColumn<ObservableAircraftState, String> ICAOColumn(){
        TableColumn<ObservableAircraftState, String> column = new TableColumn<>();
        column.setCellValueFactory(f ->
                       new ReadOnlyObjectWrapper<>(f.getValue().getIcaoAddress().string()));

        return column;
    }

    private TableColumn<ObservableAircraftState, String> CallSignColumn(){
        TableColumn<ObservableAircraftState, String> column = new TableColumn<>();
        column.setCellValueFactory(f ->
                f.getValue().callSignProperty().map(CallSign::string));
        return column;
    }

    private TableColumn<ObservableAircraftState, String> DataColumn(Function<AircraftData, String> operator){
        TableColumn<ObservableAircraftState, String> column = new TableColumn<>();
        column.setCellValueFactory(f -> {
            AircraftData ad = f.getValue().getAircraftData();
            return new ReadOnlyObjectWrapper<>(ad).map(operator::apply);
        });
        return column;
    }

    /*private TableColumn<ObservableAircraftState, Number> NumberColumn
            (int nbOfDigits, Function<ObservableAircraftState, Double> operator){
        TableColumn<ObservableAircraftState, Number> column = new TableColumn<>();
        column.setCellValueFactory(f -> {
            NumberFormat nf = NumberFormat.getInstance();
            if(nbOfDigits != 0){
                nf.setMinimumFractionDigits(0);
                nf.setMaximumFractionDigits(nbOfDigits);
                String formatted = nf.format(operator.apply(f.getValue()));
                try {
                    return nf.parse(nf.format((double)operator.apply(f.getValue())));
                } catch (ParseException e) {
                    throw new Error(e);
                }
            } else {
                try {
                    return  nf.parse(nf.format(Math.rint(operator.apply(f.getValue()))));
                } catch (ParseException e) {
                    throw new Error(e);
                }
            }

        });
        return column;
    }*/

    public void setOnDoubleClick(Consumer<ObservableAircraftState> consumer){
        consumer.accept(observedAircraft);
    }

}
