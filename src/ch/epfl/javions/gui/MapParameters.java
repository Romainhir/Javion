package ch.epfl.javions.gui;


import ch.epfl.javions.Math2;
import ch.epfl.javions.Preconditions;
import ch.epfl.javions.WebMercator;
import javafx.beans.property.*;

import ch.epfl.javions.Preconditions;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;

/**
 * Class that represent the map parameters. The parameters are the zoom level, the minimum x and the minimum y.
 * These attributes are JavaFX properties.
 *
 * @author Romain Hirschi
 * @author Moussab Tasnim Ibrahim
 */
public final class MapParameters {

    public static final int MAX_ZOOM = 19;
    public static final int MIN_ZOOM = 6;
    private IntegerProperty zoom;
    private DoubleProperty minX;
    private DoubleProperty minY;

    /**
     * Constructor of the map parameters. The zoom, the minimum x and the minimum y are given in parameter.
     *
     * @param zoom (int) : the zoom level
     * @param minX (double) : the minimum x
     * @param minY (double) : the minimum y
     */
    public MapParameters(int zoom, double minX, double minY) {
        /*Preconditions.checkArgument(TileManager.TileId.isValid(zoom, minX, minY));*/
        this.zoom = new SimpleIntegerProperty(zoom);
        this.minX = new SimpleDoubleProperty(minX);
        this.minY = new SimpleDoubleProperty(minY);
    }

    /**
     * Change the current level of zoom, depending on the difference between the current zoom
     * and the next zoom given in parameter.
     *
     * @param dZoom (int) : the difference between the current zoom and the next zoom.
     */
    public void changeZoomLevel(int dZoom) {
        if ((MIN_ZOOM <= getZoom() && getZoom() <= MAX_ZOOM)) {
            if ((getZoom() == MIN_ZOOM && dZoom < 0) || (getZoom() == MAX_ZOOM && dZoom > 0)) {
                return;
            }
            minX.set(Math.scalb(getMinX(), dZoom));
            minY.set(Math.scalb(getMinY(), dZoom));
        }
        zoom.set(Math2.clamp(MIN_ZOOM, getZoom() + dZoom, MAX_ZOOM));
    }

    /**
     * Scroll the map, depending on the difference between the current x/y coordinate and the next x/y coordinate.
     *
     * @param dX (double) :  the difference between the current and the next x coordinate.
     * @param dY (double) :  the difference between the current and the next y coordinate.
     */
    public void scroll(double dX, double dY) {
        minX.set(getMinX() + dX);
        minY.set(getMinY() + dY);
    }

    /**
     * Get the current zoom level
     *
     * @return (int) : the current zoom level
     */
    public int getZoom() {
        return zoom.get();
    }

    /**
     * Return the JavaFX zoom property of the zoom level.
     *
     * @return (ReadOnlyIntegerProperty) : the zoom level property
     */
    public ReadOnlyIntegerProperty zoomProperty() {
        return zoom;
    }

    /**
     * Get the minimum x.
     *
     * @return (double) : the minimum x
     */
    public double getMinX() {
        return minX.get();
    }

    /**
     * Return the JavaFX minimum x property of the minimum x level.
     *
     * @return (ReadOnlyIntegerProperty) : the minimum x property
     */
    public ReadOnlyDoubleProperty minXProperty() {
        return minX;
    }

    /**
     * Get the minimum y.
     *
     * @return (double) : the minimum y
     */
    public double getMinY() {
        return minY.get();
    }

    /**
     * Return the JavaFX minimum y property of the minimum y level.
     *
     * @return (ReadOnlyIntegerProperty) : the minimum y property
     */
    public ReadOnlyDoubleProperty minYProperty() {
        return minY;
    }
}
