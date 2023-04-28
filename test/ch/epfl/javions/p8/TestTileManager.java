package ch.epfl.javions.p8;

import ch.epfl.javions.gui.TileManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;


import java.nio.file.Path;

import static javafx.application.Application.launch;

public final class TestTileManager extends Application {
    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) throws Exception {
        new TileManager(Path.of("tile-cache"),
                "tile.openstreetmap.org")
                .getTileImageAt(new TileManager.TileId(17, 67927, 46357));
        Platform.exit();
    }
}
