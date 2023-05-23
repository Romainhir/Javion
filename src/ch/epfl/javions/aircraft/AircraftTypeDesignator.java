package ch.epfl.javions.aircraft;

import ch.epfl.javions.Preconditions;

import java.util.regex.Pattern;

/**
 * Record that store a valid aircraft type designator
 *
 * @param string (String) : the aircraft type designator
 * @author Romain Hirschi
 * @author Moussab Tasnim Ibrahim
 */
public record AircraftTypeDesignator(String string) {
    private static final Pattern FORMAT_ICAO = Pattern.compile("[A-Z0-9]{2,4}");

    /**
     * Constructor of the record
     *
     * @param string (String) : the aircraft type designator
     */
    public AircraftTypeDesignator {
        Preconditions.checkArgument(FORMAT_ICAO.matcher(string).matches() || (string.equals("")));
    }
}
