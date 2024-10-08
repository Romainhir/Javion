package ch.epfl.javions;

/**
 * Java Record to store a geographic position in latitude and in longitude.
 *
 * @param longitudeT32 (int) : the longitude coordinate of the position
 * @param latitudeT32  (int) : the latitude coordinate of the position
 * @author Romain Hirschi (Sciper: 359286)
 * @author Moussab Ibrahim  (Sciper: 363888)
 */
public record GeoPos(int longitudeT32, int latitudeT32) {

    private static final int MAX_ABSOLUTE_LATITUDE = 1 << (Integer.SIZE - 2);

    /**
     * Constructor of the record
     *
     * @param longitudeT32 (int) : the longitude coordinate of the position
     * @param latitudeT32  (int) : the latitude coordinate of the position
     */
    public GeoPos {
        Preconditions.checkArgument(isValidLatitudeT32(latitudeT32));
    }

    /**
     * Check if the latitude is valid. A valid latitude has his value between -2^30 and 2^30 (include).
     *
     * @param latitudeT32 (int) : the latitude to check
     * @return (boolean) : true if the value in the range, false otherwise.
     */
    public static boolean isValidLatitudeT32(int latitudeT32) {
        return (-MAX_ABSOLUTE_LATITUDE <= latitudeT32)
                && (latitudeT32 <= MAX_ABSOLUTE_LATITUDE);
    }

    /**
     * Return the longitude in radian.
     *
     * @return (double) : the longitude in radian
     */
    public double longitude() {
        return Units.convert(longitudeT32, Units.Angle.T32, Units.Angle.RADIAN);
    }

    /**
     * Return the latitude in radian.
     *
     * @return (double) : the latitude in radian
     */
    public double latitude() {
        return Units.convert(latitudeT32, Units.Angle.T32, Units.Angle.RADIAN);
    }

    /**
     * Redefine toString to get the longitude and latitude in degree
     *
     * @return String : the latitude and the longitude in degree
     */
    @Override
    public String toString() {
        return "(" + (Units.convert(longitudeT32, Units.Angle.T32, Units.Angle.DEGREE) +
                "°, " + Units.convert(latitudeT32, Units.Angle.T32, Units.Angle.DEGREE)) + "°)";
    }

}



