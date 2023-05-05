package ch.epfl.javions.p8;

import ch.epfl.javions.WebMercator;
import ch.epfl.javions.gui.TileManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;


import java.nio.file.Path;

import static javafx.application.Application.launch;

public final class TestTileManager extends Application {
    public static void main(String[] args) {launch(args); }

    @Override
    public void start(Stage primaryStage) throws Exception {
        new TileManager(Path.of("tile-cache"),
                "tile.openstreetmap.org")
                .getTileImageAt(new TileManager.TileId(18, (int)(3.4778654E7 / 256), (int)(2.373486E7/256)));
        Platform.exit();
    }
}
