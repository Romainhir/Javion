package ch.epfl.javions.aircraft;

import java.util.regex.Pattern;

public record AircraftRegistration(String string) {
    private static final Pattern formatOACI = Pattern.compile("[A-Z0-9 .?/_+-]+");
    public AircraftRegistration {
        if (!(formatOACI.matcher(this.string()).matches())){
            throw new IllegalArgumentException("The string does not respect the right format");
        }
    }
}
