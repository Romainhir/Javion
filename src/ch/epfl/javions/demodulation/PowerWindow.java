package ch.epfl.javions.demodulation;

import ch.epfl.javions.Preconditions;

import java.io.IOException;
import java.io.InputStream;

public final class PowerWindow {

    private int windowSize;

    private PowerComputer computer;

    //Exception ??? création de computer ?? TODO
    public PowerWindow(InputStream stream, int windowSize) throws IOException, Exception {
        Preconditions.checkArgument((windowSize > 0) && (windowSize <= (int) Math.scalb(1, 16)));
        computer = new PowerComputer(stream, windowSize);
    }

    public int size() {
        return windowSize;
    }

    //TODO
    public long position() {
        throw new UnsupportedOperationException("Not Implemented yet");
    }

    //TODO
    public boolean isFull() {
        throw new UnsupportedOperationException("Not Implemented yet");
    }

    //TODO
    public int get(int i) {
        throw new UnsupportedOperationException("Not Implemented yet");
    }

    //TODO
    public void advance() throws IOException {
        throw new UnsupportedOperationException("Not Implemented yet");
    }

    //TODO
    public void advanceBy(int offset) throws IOException {
        throw new UnsupportedOperationException("Not Implemented yet");
    }

}
