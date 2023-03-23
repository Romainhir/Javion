
package ch.epfl.javions.demodulation;

import ch.epfl.javions.ByteString;
import ch.epfl.javions.adsb.RawMessage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;


/**
 * Class that represent an ADSB message demodulator
 *
 * @author Romain Hirschi
 * @author Moussab Tasnim Ibrahim
 */
public final class AdsbDemodulator {

    private final PowerWindow powerWindow;
    private final byte[] messageBytes = new byte[RawMessage.LENGTH];
    private final static int MESSAGE_SIZE = 112;

    /**
     * The size of a window of power sample
     */
    public final static int WINDOWSIZE = 1200;

    /**
     * Constructor of the ADSB demodulator. Take in parameter the input stream of the data to be decoded, to give it
     * at the PowerWindow  attribute. Throw an Exception if something went wrong during the creation of the power window
     *
     * @param samplesStream (InputStream) : The input stream of the data
     * @throws IOException if something went wrong during the creation of the power window
     */
    public AdsbDemodulator(InputStream samplesStream) throws IOException {
        powerWindow = new PowerWindow(samplesStream, WINDOWSIZE);
    }

    private int peakSample(int i) {
        return powerWindow.get(i) + powerWindow.get(10 + i) +powerWindow.get(35 + i)
                + powerWindow.get(45 + i);
    }
    private void addLastPeak(int[] peaksSample) {
        peaksSample[0] = peaksSample[1];
        peaksSample[1] = peaksSample[2];
        peaksSample[2] = peakSample(1);
    }
    private boolean preambleFound(int[] peaksSample) {
       int valleySample = powerWindow.get(5) + powerWindow.get(15) + powerWindow.get(20)
               + powerWindow.get(25) + powerWindow.get(30) + powerWindow.get(40);

       return (peaksSample[1] >= 2 * valleySample) && (peaksSample[1] > peaksSample[0])
               && (peaksSample[1] > peaksSample[2]);
    }

    private void decodeMessage(byte[] messageBytes) {
        for (int i = 0; i < MESSAGE_SIZE; i++){
            if(powerWindow.get(80 + 10*i) < powerWindow.get(85 + 10*i)){
                messageBytes[i/Byte.SIZE] = (byte) (messageBytes[i/Byte.SIZE] << 1);
            }else {
                messageBytes[i/Byte.SIZE] = (byte)((messageBytes[i/Byte.SIZE] << 1) | 1);
            }
        }
    }

    /**
     * Return the next ADSB message (in RawMessage) in the input stream given in constructor or null if there is no more message.
     * Throw an Exception if something went wrong during the reading of the power samples.
     *
     * @return (RawMessage) : the next ADSB message in the input stream of null if there is no more message
     * @throws IOException if something went wrong during the reading of the power samples
     */
    public RawMessage nextMessage() throws IOException{
        int[] peaksSample = {0, peakSample(0), peakSample(1)};
        while (powerWindow.isFull()) {
            if (preambleFound(peaksSample)) {
                Arrays.fill(messageBytes, (byte) 0);
                decodeMessage(messageBytes);
                if ((RawMessage.size(messageBytes[0]) == RawMessage.LENGTH) &&
                RawMessage.of(powerWindow.position(), messageBytes) != null) {
                    powerWindow.advanceBy(WINDOWSIZE);
                    return new RawMessage((powerWindow.position() - WINDOWSIZE)*100, new ByteString(messageBytes));
                }
            }
            powerWindow.advance();
            addLastPeak(peaksSample);
        }
        return null;
    }
}
