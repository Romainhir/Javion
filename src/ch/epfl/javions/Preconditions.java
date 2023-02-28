package ch.epfl.javions;

/**
 * Class used to check if arguments in methods are valid or not.
 *
 * @author Romain Hirschi
 * @author Moussab Tasnim Ibrahim
 */
public final class Preconditions {

    private Preconditions() {

    }

    /**
     * Throw an Exception if the boolean value given in parameter is false. Do nothing otherwise.
     *
     * @param shouldBeTrue (boolean) the value to be tested
     * @throws IllegalArgumentException if the value is false
     */
    public static void checkArgument(boolean shouldBeTrue) throws IllegalArgumentException {
        if (!shouldBeTrue) {
            throw new IllegalArgumentException();
        }
    }
}
