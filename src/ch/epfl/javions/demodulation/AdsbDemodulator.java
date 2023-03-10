package ch.epfl.javions.demodulation;

import ch.epfl.javions.adsb.RawMessage;

import java.io.IOException;
import java.io.InputStream;



public final class AdsbDemodulator {

    private final InputStream samplesStream;
    private final PowerWindow powerWindow;

    public final static int WINDOWSIZE = 1200;
    AdsbDemodulator(InputStream samplesStream) throws IOException {
        this.samplesStream = samplesStream;
        powerWindow = new PowerWindow(samplesStream, WINDOWSIZE);

    }

    public RawMessage nextMessage() throws IOException{
        return null;
    }
}
