package ch.epfl.javions.demodulation;

import ch.epfl.javions.Preconditions;

import java.io.IOException;
import java.io.InputStream;

public final class PowerWindow {

    private final int TAB_LENGTH = (1 << 16);

    private int batchSize;
    private int windowsSize;
    private int index;

    private int[] windowHelper;
    private int[] windowMain;
    private PowerComputer computer;

    //TODO Taille tableau ??
    public PowerWindow(InputStream stream, int windowSize) throws IOException {
        Preconditions.checkArgument((windowSize > 0) && (windowSize <= TAB_LENGTH));
        computer = new PowerComputer(stream, TAB_LENGTH);
        windowMain = new int[TAB_LENGTH];
        windowHelper = new int[TAB_LENGTH];
        batchSize = computer.readBatch(windowMain);
        this.windowsSize = windowSize;
    }

    public int size() {
        return batchSize;
    }

    public long position() {
        return index;
    }

    public boolean isFull() {
        if (batchSize < windowsSize) {
            return false;
        }
        return true;
    }

    public int get(int i) throws IndexOutOfBoundsException {
        if ((i <= 0) || (i > batchSize - 1)) {
            throw new IndexOutOfBoundsException();
        }
        if (index >= TAB_LENGTH) {
            return windowHelper[index + 1 - TAB_LENGTH];
        } else {
            return windowMain[index + i];
        }
    }

    private void switchTab() throws IOException {
        windowMain = windowHelper;
        index = 0;
    }

    public void advance() throws IOException {
        index++;
        if (index + batchSize >= TAB_LENGTH) {
            computer.readBatch(windowHelper);
        }
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