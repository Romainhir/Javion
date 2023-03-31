package ch.epfl.javions;

/**
 * Class used to project geographical coordinates based on the WebMercator projection.
 *
 * @author Romain Hirschi
 * @author Moussab Tasnim Ibrahim
 */
public class WebMercator {

    private WebMercator() {
    }

    /**
     * Return the X value of a position, specifically a longitude coordinate and a level of zoom.
     *
     * @param zoomLevel (int) : the zoom level
     * @param longitude (int) : the longitude coordinate
     * @return (double) : the X coordinate
     */
    public static double x(int zoomLevel, double longitude) {
        return Math.scalb(1, 8 + zoomLevel) *
                (Units.convertTo(longitude, Units.Angle.TURN) + 0.5);
    }

    /**
     * Return the Y value of a position, specifically a latitude coordinate and a level of zoom.
     *
     * @param zoomLevel (int) : the zoom level
     * @param latitude  (int) : the longitude coordinate
     * @return (double) : the Y coordinate
     */
    public static double y(int zoomLevel, double latitude) {
        return Math.scalb(1, 8 + zoomLevel) * (-Units.convertTo
                (Math2.asinh(Math.tan(latitude)), Units.Angle.TURN) + 0.5);

    }
}
