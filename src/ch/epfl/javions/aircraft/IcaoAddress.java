package ch.epfl.javions.aircraft;

import ch.epfl.javions.Preconditions;

import java.util.Objects;
import java.util.regex.Pattern;

public record IcaoAddress(String string) {
    private static final Pattern formatOACI = Pattern.compile("[0-9A-F]{6}");
    public IcaoAddress{
      //  Objects.requireNonNull(string);
        Preconditions.checkArgument(formatOACI.matcher(string).matches());
    }
}
