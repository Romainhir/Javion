package ch.epfl.javions;

public record GeoPos(int longitudeT32, int latitudeT32) {

    public GeoPos{
        if (isValidLatitudeT32(latitudeT32)){ throw new IllegalArgumentException();}
    }
    public static boolean isValidLatitudeT32(int latitudeT32){
        return -Math.scalb(1, 30) <= latitudeT32 && latitudeT32 <= Math.scalb(1, 30);
    }
}
