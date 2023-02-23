package ch.epfl.javions;

public record GeoPos(int longitudeT32, int latitudeT32) {

    /**
     * Constructor
     * @param longitudeT32 longitude
     * @param latitudeT32 latitude
     */
    public GeoPos{
        if (!(isValidLatitudeT32(latitudeT32))){ throw new IllegalArgumentException();}
    }

    /**
     * Check si la latitude est valide
     * @param latitudeT32 latitude
     * @return boolean
     */
    public static boolean isValidLatitudeT32(int latitudeT32){
        return -Math.scalb(1, 30) <= latitudeT32 & latitudeT32 <= Math.scalb(1, 30);
    }

    /**
     * Retourne la longitude en radian
     * @return longitude
     */
    public double longitude(){return Units.convert(longitudeT32, Units.Angle.T32, Units.Angle.RADIAN);}

    /**
     * Retourne la latitude en radian
     * @return latitude
     */
    public double latitude(){return Units.convert(latitudeT32, Units.Angle.T32, Units.Angle.RADIAN);}

    /**
     * Redefinit toString pour renvoyer la longitude et latitude en degrés
     * @return String
     */
    @Override
    public String toString() {
        return "("+(Units.convertTo(longitude(), Units.Angle.DEGREE)+
                "°,"+Units.convertTo(latitude(), Units.Angle.DEGREE))+"°)";
    }

}



