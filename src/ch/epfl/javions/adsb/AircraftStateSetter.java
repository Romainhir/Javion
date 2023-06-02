package ch.epfl.javions.adsb;

import ch.epfl.javions.GeoPos;

/**
 * Interface that represent all state setter of an aircraft
 *
 * @author Romain Hirschi (Sciper: 359286)
 * @author Moussab Ibrahim  (Sciper: 363888)
 */
public interface AircraftStateSetter {

    /**
     * Set the timestamp of the last message received
     *
     * @param timeStampNs (long) : the timestamp to update
     */
    void setLastMessageTimeStampNs(long timeStampNs);

    /**
     * Set the category of the aircraft
     *
     * @param category (int) : the category to update
     */
    void setCategory(int category);

    /**
     * Set the call sign of the aircraft
     *
     * @param callSign (CallSign) : the call sign to update
     */
    void setCallSign(CallSign callSign);

    /**
     * Set the position of the aircraft
     *
     * @param position (GeoPos) : the position to update
     */
    void setPosition(GeoPos position);

    /**
     * Set the altitude of the aircraft
     *
     * @param altitude (double) : the altitude to update
     */
    void setAltitude(double altitude);

    /**
     * Set the velocity of the aircraft
     *
     * @param velocity (double) : the velocity to update
     */
    void setVelocity(double velocity);

    /**
     * Set the track or heading of the aircraft
     *
     * @param trackOrHeading (double) : the track or heading to update
     */
    void setTrackOrHeading(double trackOrHeading);


}
