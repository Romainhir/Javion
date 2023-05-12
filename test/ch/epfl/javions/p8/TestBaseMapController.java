package ch.epfl.javions.p8;

import ch.epfl.javions.ByteString;
import ch.epfl.javions.adsb.RawMessage;
import ch.epfl.javions.aircraft.AircraftDatabase;
import ch.epfl.javions.gui.AircraftStateManager;
import ch.epfl.javions.gui.BaseMapController;
import ch.epfl.javions.gui.MapParameters;
import ch.epfl.javions.gui.TileManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static javafx.application.Application.launch;



public final class TestBaseMapController extends Application {
    public static void main(String[] args) {
        launch(args); }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Path tileCache = Path.of("tile-cache");
        TileManager tm =
                new TileManager(tileCache, "tile.openstreetmap.org");
        MapParameters mp =
                new MapParameters(17, 17_389_327, 11_867_430);
        BaseMapController bmc = new BaseMapController(tm, mp);
        primaryStage.setScene(new Scene(bmc.pane()));
        primaryStage.show();

    }
}

