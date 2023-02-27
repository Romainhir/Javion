package ch.epfl.javions.adsb;

import java.util.regex.Pattern;

public record CallSign(String string) {
    private static final Pattern formatOACI = Pattern.compile("[A-Z0-9 ]{0,8}");
    public CallSign {
        if (!(formatOACI.matcher(this.string()).matches())){
            throw new IllegalArgumentException("The string does not respect the right format");
        }
    }
}
