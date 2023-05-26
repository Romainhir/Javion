package ch.epfl.javions.aircraft;

import ch.epfl.javions.Preconditions;

import java.util.regex.Pattern;

/**
 * Record that store a valid aircraft registration.
 *
 * @param string (String) : the aircraft registration
 * @author Romain Hirschi
 * @author Moussab Tasnim Ibrahim
 */
public record AircraftRegistration(String string) {
    private static final Pattern FORMAT_ICAO = Pattern.compile("[A-Z0-9 .?/_+-]+");

    /**
     * Constructor of the record
     *
     * @param string (String) : the aircraft registration
     */
    public AircraftRegistration {
        Preconditions.checkArgument(FORMAT_ICAO.matcher(string).matches());
    }
}
