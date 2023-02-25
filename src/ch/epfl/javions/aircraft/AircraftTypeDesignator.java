package ch.epfl.javions.aircraft;

import java.util.regex.Pattern;

public record AircraftTypeDesignator(String string) {
    private static final Pattern formatOACI = Pattern.compile("[A-Z0-9]{2,4}}");
    public AircraftTypeDesignator {
        if (!(formatOACI.matcher(this.string()).matches())){
            throw new IllegalArgumentException("The string does not respect the right format");
        }
    }
}
