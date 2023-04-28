package ch.epfl.javions.p7;

import ch.epfl.javions.ByteString;
import ch.epfl.javions.adsb.MessageParser;
import ch.epfl.javions.adsb.RawMessage;
import ch.epfl.javions.aircraft.AircraftDatabase;
import ch.epfl.javions.gui.AircraftStateManager;
import ch.epfl.javions.gui.ObservableAircraftState;

import javax.imageio.IIOException;
import java.io.*;

public class testonsp7 {
    public static void main(String[] args) {
        try (DataInputStream s = new DataInputStream(
                new BufferedInputStream(
                        new FileInputStream("resources/messages_20230318_0915.bin")))){
            byte[] bytes = new byte[RawMessage.LENGTH];
            AircraftStateManager asm = new AircraftStateManager(new AircraftDatabase("/aircraft.zip"));
            int i = 0;
            while (i < 100) {
                i++;
                long timeStampNs = s.readLong();
                int bytesRead = s.readNBytes(bytes, 0, bytes.length);
                assert bytesRead == RawMessage.LENGTH;
                ByteString message = new ByteString(bytes);

                asm.update(MessageParser.parse(new RawMessage(timeStampNs, message)));
                asm.purge();

                /*System.out.printf("%13d: %s\n", timeStampNs, message);*/
                System.out.println("-----------------------------Tableau de bord-----------------------------");
                for (ObservableAircraftState state : asm.getStates()) {
                    System.out.println(state.getIcaoAddress().string() + " " +state.getCallSign().string() + "  "
                            + state.getPosition() + "  " + (3.6*Math.rint(state.getVelocity()))
                            + "  " + Math.rint(state.getAltitude()));
                }
            }
        } catch (IIOException e) { /* nothing to do */ } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
