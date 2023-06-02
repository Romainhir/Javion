package ch.epfl.javions.aircraft;

import ch.epfl.javions.Preconditions;

import java.util.regex.Pattern;

/**
 * Record that store a valid ICAO address.
 *
 * @param string (String) : the ICAO address
 * @author Romain Hirschi (Sciper: 359286)
 * @author Moussab Ibrahim  (Sciper: 363888)
 */
public record IcaoAddress(String string) {
    private static final Pattern FORMAT_ICAO = Pattern.compile("[0-9A-F]{6}");

    /**
     * Constructor of the record.
     *
     * @param string (String) : the ICAO address
     */
    public IcaoAddress {
        Preconditions.checkArgument(FORMAT_ICAO.matcher(string).matches());
    }
}
