package ch.epfl.javions.gui;

import ch.epfl.javions.Preconditions;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;

public final class MapParameters {

    private IntegerProperty zoom;
    private DoubleProperty minX;
    private DoubleProperty minY;

    public MapParameters(int zoom, double minX, double minY) {
        Preconditions.checkArgument(TileManager.TileId.isValid(zoom, minX, minY));
    }
}
