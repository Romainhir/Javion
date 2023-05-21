package ch.epfl.javions.gui;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.SetChangeListener;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public final class StatusLineController {

    private IntegerProperty numberOfAircrafts = new SimpleIntegerProperty(0);
    private LongProperty messageCount = new SimpleLongProperty(0);
    private BorderPane pane = new BorderPane();

    public StatusLineController(){
        pane.getStylesheets().add("status.css");

        Text nbOfAircraft = new Text();
        nbOfAircraft.textProperty().bind(Bindings.format("Aéronefs visibles : %d", numberOfAircrafts));
        pane.leftProperty().set(nbOfAircraft);

        Text nbOfMessages = new Text();
        nbOfMessages.textProperty().bind(Bindings.format("Messages reçus : %d", messageCount));
        pane.rightProperty().set(nbOfMessages);
    }

    public Pane pane(){return pane;}

    public IntegerProperty aircraftCountProperty(){return numberOfAircrafts;}
    public LongProperty messageCountProperty(){return messageCount;}
}
