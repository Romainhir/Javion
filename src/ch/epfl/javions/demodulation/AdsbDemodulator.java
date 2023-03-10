package ch.epfl.javions.demodulation;

import java.io.IOException;
import java.io.InputStream;



public final class AdsbDemodulator {

    private final InputStream samplesStream;
    private final PowerWindow powerWindow;
    AdsbDemodulator(InputStream samplesStream) throws IOException {
        this.samplesStream = samplesStream;
        powerWindow = new PowerWindow(samplesStream, 1200);

    }
}
