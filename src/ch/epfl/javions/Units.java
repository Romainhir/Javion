package ch.epfl.javions;

/**
 * Class that represent the different units used.
 *
 * @author Romain Hirschi
 * @author Moussab Tasnim Ibrahim
 */
public final class Units {

    /**
     * Constant that represent the prefix "centi".
     */
    public static final double CENTI = 1e-2;

    /**
     * Constant that represent the prefix "centi".
     */
    public static final double KILO = 1e3;

    private Units() {

    }

    /**
     * Convert a given value (double) from a unit given in parameter to another value (double) in a unit
     * also given in parameter.
     *
     * @param value    (double) : the initial value
     * @param fromUnit (double) : the starting unit
     * @param toUnit   (double) : the unit of the final value
     * @return (double) : the final value
     */
    public static double convert(double value, double fromUnit, double toUnit) {
        return value * (fromUnit / toUnit);
    }

    /**
     * Convert a given value (double) from a unit given in parameter to another value (double) in the base unit (SI).
     *
     * @param value    (double) : the initial value
     * @param fromUnit (double) : the starting unit
     * @return (double) : the final value
     */
    public static double convertFrom(double value, double fromUnit) {
        return convert(value, fromUnit, 1);
    }

    /**
     * Convert a given value (double) from the base unit (SI) to another value (double) in a unit
     * given in parameter.
     *
     * @param value  (double) : the initial value
     * @param toUnit (double) : the unit of the final value
     * @return (double) : the final value
     */
    public static double convertTo(double value, double toUnit) {
        return convert(value, 1, toUnit);
    }

    /**
     * Nested class that represent the angle units
     *
     * @author Romain Hirschi
     * @author Moussab Tasnim Ibrahim
     */
    public static class Angle {

        /**
         * Constant of the base angle unit "Radian"
         */
        public static final double RADIAN = 1;

        /**
         * Constant of the angle unit "Turn"
         */
        public static final double TURN = 2 * Math.PI * RADIAN;

        /**
         * Constant of the angle unit "degree"
         */
        public static final double DEGREE = TURN / 360;

        /**
         * Constant of the angle unit "T32"
         */
        public static final double T32 = TURN / Math.scalb(1, 32);

        private Angle() {

        }

    }

    /**
     * Nested class that represent the length units
     *
     * @author Romain Hirschi
     * @author Moussab Tasnim Ibrahim
     */
    public static class Length {

        /**
         * Constant of the base length unit "meter"
         */
        public static final double METER = 1;

        /**
         * Constant of the length unit "centimeter"
         */
        public static final double CENTIMETER = CENTI * METER;

        /**
         * Constant of the length unit "kilometer"
         */
        public static final double KILOMETER = KILO * METER;

        /**
         * Constant of the length unit "inch"
         */
        public static final double INCH = CENTIMETER * 2.54;

        /**
         * Constant of the length unit "foot"
         */
        public static final double FOOT = 12 * INCH;

        /**
         * Constant of the length unit "nautical mile"
         */
        public static final double NAUTICAL_MILE = 1852 * METER;

        private Length() {
        }
    }

    /**
     * Nested class that represent the time units
     *
     * @author Romain Hirschi
     * @author Moussab Tasnim Ibrahim
     */
    public static class Time {

        /**
         * Constant of the base time unit "second"
         */
        public static final double SECOND = 1;

        /**
         * Constant of the time unit "minute"
         */
        public static final double MINUTE = 60 * SECOND;

        /**
         * Constant of the time unit "hour"
         */
        public static final double HOUR = 60 * MINUTE;

        private Time() {
        }
    }

    /**
     * Nested class that represent the speed units
     *
     * @author Romain Hirschi
     * @author Moussab Tasnim Ibrahim
     */
    public static class Speed {

        /**
         * Constant of the base speed unit "meter per second"
         */
        public static final double METER_PER_SECOND = Length.METER / Time.SECOND;

        /**
         * Constant of the speed unit "knot"
         */
        public static final double KNOT = Length.NAUTICAL_MILE / Time.HOUR;

        /**
         * Constant of the speed unit "kilometer per hour"
         */
        public static final double KILOMETER_PER_HOUR = Length.KILOMETER / Time.HOUR;

        private Speed() {
        }
    }

}
