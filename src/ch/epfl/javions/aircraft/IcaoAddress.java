package ch.epfl.javions.aircraft;

import java.util.regex.Pattern;

public record IcaoAddress(String string) {
    private static final Pattern formatOACI = Pattern.compile("[0-9A-F]{6}");
    public IcaoAddress{
        if (!(formatOACI.matcher(this.string()).matches())){
            throw new IllegalArgumentException("The string does not respect OACI format");
        }
    }
}
