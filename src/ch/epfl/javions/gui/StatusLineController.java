package ch.epfl.javions.gui;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public final class StatusLineController {

    private IntegerProperty numberOfAircrafts = new SimpleIntegerProperty(0);
    private LongProperty messageCount = new SimpleLongProperty(0);
    private BorderPane pane = new BorderPane();
    private AircraftStateManager asm;

    public StatusLineController(AircraftStateManager asm){
        this.asm = asm;
    }


    public Pane pane(){
        pane.getStylesheets().add("status.css");

        Text nbOfAircraft = new Text();
        numberOfAircrafts.bind(Bindings.size(asm.getStates()));
        nbOfAircraft.textProperty().bind(Bindings.format("Aéronefs visibles : %d", numberOfAircrafts.get()));
        pane.leftProperty().set(nbOfAircraft);

        Text nbOfMessages = new Text("Messages reçus : " + messageCount.get());
        pane.rightProperty().set(nbOfMessages);
        return pane;

    }

    public IntegerProperty aircraftCountProperty(){return numberOfAircrafts;}
    public LongProperty messageCountProperty(){return messageCount;}
}
