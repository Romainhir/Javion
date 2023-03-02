package ch.epfl.javions.aircraft;

import ch.epfl.javions.Preconditions;

import java.util.regex.Pattern;

/**
 * Record that store a valid aircraft type designator
 *
 * @param string (String) : the aircraft type designator
 */
public record AircraftTypeDesignator(String string) {
    private static final Pattern formatOACI = Pattern.compile("[A-Z0-9]{2,4}");

    /**
     * COnstructor of the record
     *
     * @param string (String) : the aircraft type designator
     */
    public AircraftTypeDesignator {
        Preconditions.checkArgument(formatOACI.matcher(string).matches());
    }
}
