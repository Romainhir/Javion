package ch.epfl.javions.aircraft;

import ch.epfl.javions.Preconditions;

import java.util.regex.Pattern;

/**
 * Recrod that store a valid aircraft description
 *
 * @param string (String) : the aircraft description
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
