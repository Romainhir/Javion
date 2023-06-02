package ch.epfl.javions.aircraft;

import ch.epfl.javions.Preconditions;

import java.util.regex.Pattern;

/**
 * Record that store a valid aircraft description
 *
 * @param string (String) : the aircraft description
 * @author Romain Hirschi (Sciper: 359286)
 * @author Moussab Ibrahim  (Sciper: 363888)
 */
public record AircraftDescription(String string) {
    private static final Pattern FORMAT_ICAO = Pattern.compile("[ABDGHLPRSTV-][0123468][EJPT-]");

    /**
     * Constructor of the record
     *
     * @param string (String) : the aircraft description
     */
    public AircraftDescription {
        Preconditions.checkArgument(FORMAT_ICAO.matcher(string).matches() || (string.equals("")));
    }
}
