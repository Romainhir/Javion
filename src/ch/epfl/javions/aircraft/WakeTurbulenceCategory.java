package ch.epfl.javions.aircraft;

/**
 * Enumeration that represent the different wake turbulence
 *
 * @author Romain Hirschi (Sciper: 359286)
 * @author Moussab Ibrahim  (Sciper: 363888)
 */
public enum WakeTurbulenceCategory {
    LIGHT,
    MEDIUM,
    HEAVY,
    UNKNOWN;

    /**
     * Return the correct wake turbulence category that match the String (s) given in parameter. It returns "LIGHT" if
     * s is "L", "MEDIUM" if s is "M", "Heavy" if s is "H" or "UNKNOWN" otherwise.
     *
     * @param s (String) ; the String to match with the turbulence category
     * @return (WakeTurbulenceCategory) : the correct WakeTurbulenceCategory
     */
    public static WakeTurbulenceCategory of(String s) {
        switch (s) {
            case "L" -> {
                return LIGHT;
            }
            case "M" -> {
                return MEDIUM;
            }
            case "H" -> {
                return HEAVY;
            }
        }
        return UNKNOWN;
    }
}
