package ch.epfl.javions.gui;

import ch.epfl.javions.ByteString;
import ch.epfl.javions.adsb.Message;
import ch.epfl.javions.adsb.MessageParser;
import ch.epfl.javions.adsb.RawMessage;
import ch.epfl.javions.aircraft.AircraftDatabase;
import ch.epfl.javions.demodulation.AdsbDemodulator;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
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
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class Main extends Application {

    public static final int ONE_MILLION = 1_000_000;
    public static final String IMAGE_STORAGE = "tile-cache";
    private static final int WIDTH_MIN = 800;
    private static final int HEIGHT_MIN = 600;
    private static final int X_COORD = 33_530;
    private static final int Y_COORD = 23_070;
    private static final int ZOOM = 8;
    public static final String SERVER_NAME = "tile.openstreetmap.org";
    public static final String AIRCRAFT_DATABASE = "/aircraft.zip";
    private final ConcurrentLinkedQueue<Message> messageQueue = new ConcurrentLinkedQueue<>();



    public static void main(String[] args) {launch(args); }
    @Override
    public void start(Stage primaryStage) throws Exception {

        TileManager tm =
                new TileManager(Path.of(IMAGE_STORAGE), SERVER_NAME);
        MapParameters mp =
                new MapParameters(ZOOM, X_COORD, Y_COORD);
        BaseMapController bmc = new BaseMapController(tm, mp);

        URL u = getClass().getResource(AIRCRAFT_DATABASE);
        assert u != null;
        Path p = Path.of(u.toURI());
        AircraftDatabase db = new AircraftDatabase(p.toString());

       AircraftStateManager asm = new AircraftStateManager(db);
        ObjectProperty<ObservableAircraftState> observedAircraft = new SimpleObjectProperty<>();
       AircraftController ac = new AircraftController(mp, asm.getStates(), observedAircraft);
       AircraftTableController atc = new AircraftTableController(asm.getStates(), observedAircraft);
       StatusLineController slc = new StatusLineController();

       SplitPane sp = new SplitPane();
       StackPane stkPane = new StackPane();
       BorderPane bPane = new BorderPane();

       slc.aircraftCountProperty().bind(Bindings.size(asm.getStates()));

       stkPane.getChildren().add(bmc.pane());
       stkPane.getChildren().add(ac.pane());
       bPane.centerProperty().set(atc.pane());
       bPane.topProperty().set(slc.pane());
       sp.getItems().addAll(stkPane, bPane);
       sp.setMinWidth(WIDTH_MIN);
       sp.setMinHeight(HEIGHT_MIN);
       sp.setOrientation(javafx.geometry.Orientation.VERTICAL);
       atc.setOnDoubleClick(selected -> bmc.centerOn(selected.getPosition()));

       primaryStage.setScene(new Scene(sp));
       primaryStage.show();



       Thread t = new Thread(() -> {
            List<String> args = getParameters().getRaw();
            if (args.isEmpty()) {
                try(InputStream in = System.in) {
                    AdsbDemodulator demodulator = new AdsbDemodulator(in);
                    RawMessage rm;
                    while((rm = demodulator.nextMessage()) != null) {
                        Message m = MessageParser.parse(rm);
                        if (m != null) {messageQueue.add(m);}
                    }

                } catch (IOException e) {}
            } else {
                URL file = getClass().getResource(args.get(0));
                assert file != null;
                try (DataInputStream s = new DataInputStream(
                        new BufferedInputStream(
                                new FileInputStream(file.getFile())))) {
                    byte[] bytes = new byte[RawMessage.LENGTH];
                    int i = 0;
                    long lastTimeStampNs = 0;
                    while (i < s.available()) {
                        i++;
                        long timeStampNs = s.readLong();
                        int bytesRead = s.readNBytes(bytes, 0, bytes.length);
                        assert bytesRead == RawMessage.LENGTH;
                        ByteString message = new ByteString(bytes);
                        Message m = MessageParser.parse(new RawMessage(timeStampNs, message));
                        if (m != null) {
                            messageQueue.add(m);
                            long delta = timeStampNs - lastTimeStampNs;
                            Thread.sleep(delta / ONE_MILLION);
                            lastTimeStampNs = timeStampNs;
                        }
                    }
                } catch (IOException | InterruptedException e2)
                {
            }
        }
        });
       t.setDaemon(true);
       t.start();

        new AnimationTimer() {
            long lastPurgeCall = 0;
            @Override
            public void handle(long now) {
                if(now - lastPurgeCall > 1_000_000_000) {
                    lastPurgeCall = now;
                    asm.purge();
                }
                try {
                    if(!(messageQueue.isEmpty())) {
                        Message m = messageQueue.poll();
                        asm.update(m);
                        slc.messageCountProperty().set(slc.messageCountProperty().get() + 1);
                    }
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        }.start();
    }

}
