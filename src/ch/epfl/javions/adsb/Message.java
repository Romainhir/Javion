package ch.epfl.javions.adsb;

import ch.epfl.javions.aircraft.IcaoAddress;

/**
 * Interface that represent any type of analysed ADSB message.
 *
 * @author Romain Hirschi (Sciper: 359286)
 * @author Moussab Ibrahim  (Sciper: 363888)
 */
public interface Message {

    /**
     * Return the timestamp of the message
     *
     * @return (long) : the timestamp of the message
     */
    long timeStampNs();

    /**
     * Return the ICAO address of the message
     *
     * @return (IcaoAddress) : the ICAO address of the message
     */
    IcaoAddress icaoAddress();
}
