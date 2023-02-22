package ch.epfl.javions;

public class WebMercator {

    private WebMercator(){}

    public double x(int zoomLevel, double longitude){
        return Math.scalb(1, 8+zoomLevel) *
                (Units.convertTo(longitude, Units.Angle.TURN) + 0.5);
    }

    public double y(int zoomLevel, double latitude){
        return Math.scalb(1, 8+zoomLevel) * (-Units.convertTo
                (Math2.asinh(Math.tan(latitude)), Units.Angle.TURN) + 0.5);

    }
}
