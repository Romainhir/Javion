package ch.epfl.javions;

public record GeoPos(int longitudeT32, int latitudeT32) {

    public GeoPos{
        if (!(isValidLatitudeT32(latitudeT32))){ throw new IllegalArgumentException();}
    }
    public static boolean isValidLatitudeT32(int latitudeT32){
        return -Math.scalb(1, 30) <= latitudeT32 & latitudeT32 <= Math.scalb(1, 30);
    }

    public double longitude(){return Units.convert(longitudeT32, Units.Angle.T32, Units.Angle.RADIAN);}
    public double latitude(){return Units.convert(latitudeT32, Units.Angle.T32, Units.Angle.RADIAN);}
    @Override
    public String toString() {
        return "("+(Units.convertTo(longitude(), Units.Angle.DEGREE)+
                ","+Units.convertTo(latitude(), Units.Angle.DEGREE))+")";
    }

}



