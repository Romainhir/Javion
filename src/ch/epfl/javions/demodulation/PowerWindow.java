
package ch.epfl.javions.demodulation;

import ch.epfl.javions.Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public final class PowerWindow {

    private final int TAB_LENGTH = (1 << 16);

    private int batchSize;
    private int batchSize2;
    private int windowsSize;
    private long index;

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
        return windowsSize;
    }

    public long position() {
        return index;
    }

    public boolean isFull() {
        if (batchSize + batchSize2 < index % TAB_LENGTH + windowsSize) {
            return false;
        }
        return true;
    }

    public int get(int i) throws IndexOutOfBoundsException {
        Objects.checkIndex(i, windowsSize);
        if (((int) ((index + i) / (TAB_LENGTH)) == (index / TAB_LENGTH))) {
            return windowMain[(int) ((index % TAB_LENGTH) + i)];
        } else {
            return windowHelper[(int) ((index + i) % TAB_LENGTH)];
        }
    }

    private void switchTab() {
        int[] tab = windowMain;
        windowMain = windowHelper;
        windowHelper = tab;
        batchSize = batchSize2;
        batchSize2 = 0;
    }

    public void advance() throws IOException {
        index++;
        if (index % TAB_LENGTH + windowsSize - 1 == TAB_LENGTH) {
            batchSize2 = computer.readBatch(windowHelper);
        } else if (index % TAB_LENGTH == 0) {
            switchTab();
        }
    }

    public void advanceBy(int offset) throws IOException {
        Preconditions.checkArgument(offset >= 0);
        for (int i = 0; i < offset; i++) {
            advance();
        }
    }
    
}