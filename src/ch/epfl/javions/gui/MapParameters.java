package ch.epfl.javions.gui;


import ch.epfl.javions.Math2;
import ch.epfl.javions.Preconditions;
import ch.epfl.javions.WebMercator;
import javafx.beans.property.*;

import ch.epfl.javions.Preconditions;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;


public final class MapParameters {

    private IntegerProperty zoom;
    private DoubleProperty minX;
    private DoubleProperty minY;

    public MapParameters(int zoom, double minX, double minY) {
        /*Preconditions.checkArgument(TileManager.TileId.isValid(zoom, minX, minY));*/
        this.zoom = new SimpleIntegerProperty(zoom);
        this.minX = new SimpleDoubleProperty(minX);
        this.minY = new SimpleDoubleProperty(minY);
    }

    //TODO Constante
    public void changeZoomLevel(int dZoom) {
        if((6 <= getZoom() && getZoom() <= 19)) {
            if((getZoom() == 6 && dZoom < 0) || (getZoom() == 19 && dZoom > 0)){
                return;
            }
            minX.set(Math.scalb(getMinX(), dZoom));
            minY.set(Math.scalb(getMinY(), dZoom));
        }
        zoom.set(Math2.clamp(6, getZoom() + dZoom, 19));
    }

    public void scroll(double dX, double dY) {
        minX.set(getMinX() + dX);
        minY.set(getMinY() + dY);
    }

    public int getZoom() {
        return zoom.get();
    }

    public ReadOnlyIntegerProperty zoomProperty() {
        return zoom;
    }

    public double getMinX() {
        return minX.get();
    }

    public ReadOnlyDoubleProperty minXProperty() {
        return minX;
    }

    public double getMinY() {
        return minY.get();
    }

    public ReadOnlyDoubleProperty minYProperty() {
        return minY;
    }
}
