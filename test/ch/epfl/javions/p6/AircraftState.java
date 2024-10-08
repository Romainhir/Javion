package ch.epfl.javions.p6;

import ch.epfl.javions.GeoPos;
import ch.epfl.javions.adsb.AircraftStateSetter;
import ch.epfl.javions.adsb.CallSign;

class AircraftState implements AircraftStateSetter {
    @Override
    public void setLastMessageTimeStampNs(long timeStampNs) {
        /*System.out.println("timeStampNs : " + timeStampNs);*/
    }

    @Override
    public void setCategory(int category) {
        /*System.out.println("category : " + category);*/
    }

    @Override
    public void setCallSign(CallSign callSign) {
        System.out.println("indicatif : " + callSign);
    }

    @Override
    public void setAltitude(double altitude) {
        /*System.out.println("altitude : " + altitude);*/
    }

    @Override
    public void setVelocity(double velocity) {

        
    }

    @Override
    public void setTrackOrHeading(double trackOrHeading) {
        /*System.out.println("trackOrHeading : " + trackOrHeading);*/
    }

    @Override
    public void setPosition(GeoPos position) {
        System.out.println("position : " + position);
    }

}
