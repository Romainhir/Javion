package ch.epfl.javions.aircraft;

import java.util.regex.Pattern;

public record AircraftDescription(String string) {
    private static final Pattern formatOACI = Pattern.compile("[ABDGHLPRSTV-][0123468][EJPT-]");
    public AircraftDescription {
        if (!(formatOACI.matcher(this.string()).matches())){
            throw new IllegalArgumentException("The string does not respect the right format");
        }
    }
}
