package ch.epfl.javions.aircraft;

import ch.epfl.javions.Preconditions;

import java.util.regex.Pattern;

public record AircraftRegistration(String string) {
    private static final Pattern formatOACI = Pattern.compile("[A-Z0-9 .?/_+-]+");
    public AircraftRegistration {
        Preconditions.checkArgument(formatOACI.matcher(string).matches() || (string.equals("")));
    }
}
