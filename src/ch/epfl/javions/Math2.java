package ch.epfl.javions;

/**
 * Class used to perform some mathematical operation
 *
 * @author Romain Hirschi (Sciper: 359286)
 * @author Moussab Ibrahim  (Sciper: 363888)
 */
public final class Math2 {

    /**
     * Private constructor
     */
    private Math2() {
    }

    /**
     * Compare a given value ("v") with other numbers (one "min" value and one "max" value).
     * These numbers are provided in parameter. Return the "min" value if "v" is smaller than "min", and
     * the "max" value if "v" is bigger than "max". Return "v" if this value is between the "min" and the "max".
     * Throw an Exception if the "min" value is (strictly) bigger than the "max" value.
     *
     * @param min (int) : the "min" number
     * @param v   (int) : the number to compare
     * @param max (int) : the "max" number
     * @return (int) : "min" if "v" is smaller than "min", "max" if "v" is bigger than "max", "v" otherwise
     * @throws IllegalArgumentException if "min" is (strictly) bigger than "max"
     */
    public static int clamp(int min, int v, int max) throws IllegalArgumentException {
        Preconditions.checkArgument(max >= min);
        if (Math.min(v, min) == v) {
            return min;
        } else if (Math.max(v, max) == v) {
            return max;
        } else return v;
    }

    /**
     * Return the inverse hyperbolic sine of the value (double) given in parameter.
     *
     * @param x (double) : the number to transform
     * @return (double) : the inverse hyperbolic sine
     */
    public static double asinh(double x) {
        return Math.log(x + Math.sqrt(1 + (x * x)));
    }
}
