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

    private final ConcurrentLinkedQueue<RawMessage> messageQueue = new ConcurrentLinkedQueue<>();

    private static final int WIDTH_MIN = 800;
    private static final int HEIGHT_MIN = 600;
    private static final int X_COORD = 33_530;
    private static final int Y_COORD = 23_070;
    private static final int ZOOM = 8;



    private List<RawMessage> readAllMessages(String fileName)
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

    public static void main(String[] args) {launch(args); }
    @Override
    public void start(Stage primaryStage) throws Exception {

        TileManager tm =
                new TileManager(Path.of("tile-cache"), "tile.openstreetmap.org");
        MapParameters mp =
                new MapParameters(ZOOM, X_COORD, Y_COORD);
        BaseMapController bmc = new BaseMapController(tm, mp);

        URL u = getClass().getResource("/aircraft.zip");
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
                    RawMessage m;
                    while ((m = demodulator.nextMessage()) != null) {messageQueue.add(m);}

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
                        messageQueue.add(new RawMessage(timeStampNs, message));
                        long delta = timeStampNs - lastTimeStampNs;
                        Thread.sleep(delta / 1_000_000);
                        lastTimeStampNs = timeStampNs;
                    }
                } catch (IOException | InterruptedException e2) {
            }
        }
        });
       t.setDaemon(true);
       t.start();


        new AnimationTimer() {
            @Override
            public void handle(long now) {
                try {
                    if(!(messageQueue.isEmpty())) {
                        RawMessage m = messageQueue.poll();
                        Message message = MessageParser.parse(m);
                        asm.update(message);
                        slc.messageCountProperty().set(slc.messageCountProperty().get() + 1);
                    }
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        }.start();
    }

}
