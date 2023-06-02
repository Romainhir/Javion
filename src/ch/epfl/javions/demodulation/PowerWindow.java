
package ch.epfl.javions.demodulation;

import ch.epfl.javions.Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * Class that represent a window of all the power sample decoded by the power computer.
 *
 * @author Romain Hirschi (Sciper: 359286)
 * @author Moussab Ibrahim  (Sciper: 363888)
 */
public final class PowerWindow {

    private final int TAB_LENGTH = (1 << 16);

    private int batchSize;
    private int batchSize2;
    private int windowsSize;
    private long index;

    private int[] windowHelper;
    private int[] windowMain;
    private PowerComputer computer;

    /**
     * Constructor of the power window. In parameter is given the input stream for the power computer
     * and the size of a window. This size must be positive and (strictly) larger than 0 and smaller than the
     * length of the arrays of batches stored in attribute. Throw an Exception if the call of readBatch from power
     * computer went wrong.
     *
     * @param stream     (InputStream) : the input stream
     * @param windowSize (int) : the size of a window
     * @throws IOException if the call of readBatch from power computer went wrong
     */
    public PowerWindow(InputStream stream, int windowSize) throws IOException {
        Preconditions.checkArgument((windowSize > 0) && (windowSize <= TAB_LENGTH));
        computer = new PowerComputer(stream, TAB_LENGTH);
        windowMain = new int[TAB_LENGTH];
        windowHelper = new int[TAB_LENGTH];
        batchSize = computer.readBatch(windowMain);
        this.windowsSize = windowSize;
        batchSize2 = 0;
    }

    /**
     * Return the size of the window.
     *
     * @return (int) : the size of the window
     */
    public int size() {
        return windowsSize;
    }

    /**
     * Return the position of the window in the array of batches.
     *
     * @return (long) : the position of the window
     */
    public long position() {
        return index;
    }

    /**
     * Return true if the window is full of power sample, false otherwise.
     *
     * @return (boolean) : true if the window is full of power sample, false otherwise
     */
    public boolean isFull() {
        if (batchSize + batchSize2 < index % TAB_LENGTH + windowsSize) {
            return false;
        }
        return true;
    }

    /**
     * Get the power sample of the given index. Throw an Exception if the index is not in the window.
     *
     * @param i (int) : the index
     * @return (int) : the value of the power sample
     * @throws IndexOutOfBoundsException if the index is not in the window
     */
    public int get(int i) throws IndexOutOfBoundsException {
        Objects.checkIndex(i, windowsSize);
        if (index % TAB_LENGTH + i < TAB_LENGTH) {
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

    /**
     * Advance the window of one sample. Throw an Exception if it needs to read a new batch of power sample and
     * something went wrong during the reading of the batch.
     *
     * @throws IOException if it needs to read a new batch of power sample and something went wrong during
     *                     the reading of the batch
     */
    public void advance() throws IOException {
        index++;
        if (index % TAB_LENGTH + windowsSize - 1 == TAB_LENGTH) {
            batchSize2 = computer.readBatch(windowHelper);
        } else if (index % TAB_LENGTH == 0) {
            switchTab();
        }
    }

    /**
     * Advance the window of a given position. Throw an Exception if it needs to read a new batch of power sample and
     * something went wrong during the reading of the batch.
     *
     * @param offset (int) : the number of position to advance the window
     * @throws IOException if it needs to read a new batch of power sample and something went wrong during
     *                     the reading of the batch
     */
    public void advanceBy(int offset) throws IOException {
        Preconditions.checkArgument(offset >= 0);
        for (int i = 0; i < offset; i++) {
            advance();
        }
    }

}