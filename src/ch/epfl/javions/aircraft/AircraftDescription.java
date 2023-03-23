package ch.epfl.javions.aircraft;

import ch.epfl.javions.Preconditions;

import java.util.regex.Pattern;

/**
 * Record that store a valid aircraft description
 *
 * @param string (String) : the aircraft description
 * @author Romain Hirschi
 * @author Moussab Tasnim Ibrahim
 */
public record AircraftDescription(String string) {
    private static final Pattern formatOACI = Pattern.compile("[ABDGHLPRSTV-][0123468][EJPT-]");

    /**
     * Constructor of the record
     *
     * @param string (String) : the aircraft description
     */
    public AircraftDescription {
        Preconditions.checkArgument(formatOACI.matcher(string).matches() || (string.equals("")));
    }
}
