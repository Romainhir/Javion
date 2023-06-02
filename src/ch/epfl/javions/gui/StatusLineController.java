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

/**
 * Class that represent the status line controller, the line that shows how many aircraft are visible and how many messages
 * has been received.
 *
 * @author Romain Hirschi
 * @author Moussab Ibrahim
 */
public final class StatusLineController {

    private static final String STATUS_STYLE = "status.css";
    private IntegerProperty numberOfAircrafts;
    private LongProperty messageCount;
    private BorderPane pane;

    /**
     * Constructor of the status line controller
     */
    public StatusLineController() {
        numberOfAircrafts = new SimpleIntegerProperty(0);
        messageCount = new SimpleLongProperty(0);
        pane = new BorderPane();

        pane.getStylesheets().add(STATUS_STYLE);

        Text nbOfAircraft = new Text();
        nbOfAircraft.textProperty().bind(Bindings.format("Aéronefs visibles : %d", numberOfAircrafts));
        pane.leftProperty().set(nbOfAircraft);

        Text nbOfMessages = new Text();
        nbOfMessages.textProperty().bind(Bindings.format("Messages reçus : %d", messageCount));
        pane.rightProperty().set(nbOfMessages);
    }

    /**
     * Return the pane with the number of aircraft and messages.
     *
     * @return (Pane) : the pane with the number of aircraft and messages
     */
    public Pane pane() {
        return pane;
    }


    /**
     * Return the aircraft count property
     *
     * @return (IntegerProperty) : the aircraft count property
     */
    public IntegerProperty aircraftCountProperty() {
        return numberOfAircrafts;
    }

    /**
     * Return the message count property
     *
     * @return (LongProperty) : the message count property
     */
    public LongProperty messageCountProperty() {
        return messageCount;
    }

}
