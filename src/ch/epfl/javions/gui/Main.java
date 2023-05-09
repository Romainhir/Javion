package ch.epfl.javions.gui;

import ch.epfl.javions.ByteString;
import ch.epfl.javions.adsb.Message;
import ch.epfl.javions.adsb.MessageParser;
import ch.epfl.javions.adsb.RawMessage;
import ch.epfl.javions.aircraft.AircraftDatabase;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class Main extends Application {

    static List<RawMessage> readAllMessages(String fileName)
            throws IOException {
        List<RawMessage> list = new ArrayList<>();
        try (DataInputStream s = new DataInputStream(
                new BufferedInputStream(
                        new FileInputStream("resources/messages_20230318_0915.bin")))) {
            byte[] bytes = new byte[RawMessage.LENGTH];
            AircraftStateManager asm = new AircraftStateManager(new AircraftDatabase("resources/aircraft.zip"));
            int i = 0;
            while (i < s.available()) {
                i++;
                long timeStampNs = s.readLong();
                int bytesRead = s.readNBytes(bytes, 0, bytes.length);
                assert bytesRead == RawMessage.LENGTH;
                ByteString message = new ByteString(bytes);
                list.add(new RawMessage(timeStampNs, message));
            }
        }
        return list;
    }

    public static void main(String[] args) {
        launch(args); }
    @Override
    public void start(Stage primaryStage) throws Exception {

        TileManager tm =
                new TileManager(Path.of("tile-cache"), "tile.openstreetmap.org");
        MapParameters mp =
                new MapParameters(8, 33_530, 23_070);
        BaseMapController bmc = new BaseMapController(tm, mp);

        URL u = getClass().getResource("/aircraft.zip");
        assert u != null;
        Path p = Path.of(u.toURI());
        AircraftDatabase db = new AircraftDatabase(p.toString());

       AircraftStateManager asm = new AircraftStateManager(db);

       AircraftController ac = new AircraftController(mp, asm.getStates(), new  SimpleObjectProperty<>());
       AircraftTableController atc = new AircraftTableController(asm.getStates(), new  SimpleObjectProperty<>());
       StatusLineController slc = new StatusLineController(asm);

       SplitPane sp = new SplitPane();
       StackPane stkPane = new StackPane();
       BorderPane bPane = new BorderPane();

       stkPane.getChildren().add(bmc.pane());
       stkPane.getChildren().add(ac.pane());
       bPane.centerProperty().set(atc.pane());
       bPane.topProperty().set(slc.pane());
       sp.getItems().addAll(stkPane, bPane);
       sp.setMinWidth(800);
       sp.setMinHeight(600);
       sp.setOrientation(javafx.geometry.Orientation.VERTICAL);

        primaryStage.setScene(new Scene(sp));
        primaryStage.show();

        List<RawMessage> list = readAllMessages("resources/messages_20230318_0915.bin");
        var mi = list.iterator();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                try {
                    for (int i = 0; i < 2; i += 1) {
                        Message m = MessageParser.parse(mi.next());
                        if (m != null) asm.update(m);
                    }
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        }.start();
    }

}
