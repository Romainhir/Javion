package ch.epfl.javions.demodulation;

import ch.epfl.javions.Preconditions;

import java.io.IOException;
import java.io.InputStream;

public final class PowerWindow {

    private final int TAB_LENGTH = 8;

    private int batchSize;
    private int batchSize2;
    private int windowsSize;
    private int index;

    private int[] windowHelper;
    private int[] windowMain;
    private PowerComputer computer;

    public PowerWindow(InputStream stream, int windowSize) throws IOException {
        Preconditions.checkArgument((windowSize > 0) && (windowSize <= TAB_LENGTH));
        computer = new PowerComputer(stream, TAB_LENGTH);
        windowMain = new int[TAB_LENGTH];
        windowHelper = new int[TAB_LENGTH];
        batchSize = computer.readBatch(windowMain);
        this.windowsSize = windowSize;
        batchSize2 = 0;
    }

    public int size() {
        return batchSize;
    }

    public long position() {
        return index;
    }

    public boolean isFull() {
        if (batchSize + batchSize2 < index + windowsSize) {
            return false;
        }
        return true;
    }

    public int get(int i) throws IndexOutOfBoundsException {
        if ((i < 0) || (i >= windowsSize)) {
            throw new IndexOutOfBoundsException();
        }
        if (index + i >= TAB_LENGTH) {
            return windowHelper[index + i - TAB_LENGTH];
        } else {
            return windowMain[index + i];
        }
    }

    private void switchTab() {
        int[] tab = windowMain;
        windowMain = windowHelper;
        windowHelper = tab;
        index = 0;
        batchSize = batchSize2;
        batchSize2 = 0;
    }

    public void advance() throws IOException {
        index++;
        if (index + windowsSize - 1 == TAB_LENGTH) {
            batchSize2 = computer.readBatch(windowHelper);
        } else if (index > TAB_LENGTH) {
            switchTab();
        }
    }

    public void advanceBy(int offset) throws IOException {
        for (int i = 0; i < offset; i++) {
            advance();
        }
    }

}