package ch.epfl.javions.demodulation;

import ch.epfl.javions.adsb.RawMessage;

import java.io.IOException;
import java.io.InputStream;



public final class AdsbDemodulator {

    private final InputStream samplesStream;
    private final PowerWindow powerWindow;

    public final static int WINDOWSIZE = 1200;
    public AdsbDemodulator(InputStream samplesStream) throws IOException {
        this.samplesStream = samplesStream;
        powerWindow = new PowerWindow(samplesStream, WINDOWSIZE);

    }

    private boolean preambleFound() {
       int peakSample = powerWindow.get(0) + powerWindow.get(10) +powerWindow.get(35)
               + powerWindow.get(45);

       int valleySample = powerWindow.get(5) + powerWindow.get(15) + powerWindow.get(20)
               + powerWindow.get(25) + powerWindow.get(30) + powerWindow.get(40);

       return (peakSample > 2 * valleySample);
    }
    public RawMessage nextMessage() throws IOException{
        while (powerWindow.isFull()) {
            if (preambleFound()) {
                return null;
            }else {
                powerWindow.advance();
                    }

        }
        return null;
    }
}
