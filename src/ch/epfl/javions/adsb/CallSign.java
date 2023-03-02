package ch.epfl.javions.adsb;

import ch.epfl.javions.Preconditions;

import java.util.regex.Pattern;

/**
 * Record that store a valid aircraft call sign
 *
 * @param string (String) : the aircraft call sign
 */
public record CallSign(String string) {
    private static final Pattern formatOACI = Pattern.compile("[A-Z0-9 ]{0,8}");

    /**
     * Constructor of the record
     *
     * @param string (String) : the aircraft call sign
     */
    public CallSign {
        Preconditions.checkArgument(formatOACI.matcher(string).matches());
    }
}
