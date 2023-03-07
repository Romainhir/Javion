package ch.epfl.javions.demodulation;

import ch.epfl.javions.Preconditions;

import java.io.IOException;
import java.io.InputStream;

public final class PowerWindow {

    private final int TAB_LENGTH = (1 << 16);

    private int batchSize;
    private int[] window;
    private int index;

    private int[] windowHelper;
    private int[] windowMain;
    private PowerComputer computer;

    public PowerWindow(InputStream stream, int windowSize) throws Exception {
        Preconditions.checkArgument((windowSize > 0) && (windowSize <= TAB_LENGTH));
        computer = new PowerComputer(stream, windowSize);
        windowMain = new int[TAB_LENGTH];
        windowHelper = new int[TAB_LENGTH];
        batchSize = computer.readBatch(windowMain);
        if (isFull()) {
            batchSize = computer.readBatch(windowHelper);
        }
        window = new int[windowSize];
        for (index = 0; index < windowSize; index++) {
            if (index >= windowSize) {
                switchTab();
            }
            window[index] = windowMain[index];
        }
    }

    public int size() {
        return window.length;
    }

    public long position() {
        return index;
    }

    public boolean isFull() {
        if (batchSize < TAB_LENGTH) {
            return false;
        }
        return true;
    }

    //TODO
    public int get(int i) {
        throw new UnsupportedOperationException("TODO");
    }

    private void switchTab() throws IOException {
        windowMain = windowHelper;
        computer.readBatch(windowHelper);
        index = 0;
    }

    public void advance() throws IOException {
        index++;
        if (index >= TAB_LENGTH) {
            switchTab();
        }
    }

    public void advanceBy(int offset) throws IOException {
        for (int i = 0; i < offset; i++) {
            advance();
        }
    }

}
