package ch.epfl.javions;

public final class Units {

    public static final double CENTI = 1e-2;
    public static final double KILO = 1e3;

    private Units() {

    }

    public static double convert(double value, double fromUnit, double toUnit) {
        return value * (fromUnit / toUnit);
    }

    public static double convertFrom(double value, double fromUnit){
        return convert(value, fromUnit, 1);
    }

    public static double convertTo(double value, double toUnit){
        return convert(value, 1, toUnit);
    }

    public static class Angle {

        public static final double RADIAN = 1;
        public static final double TURN = 2 * Math.PI * RADIAN;
        public static final double DEGREE = TURN / 360;
        public static final double T32 = TURN / Math.scalb(1, 32);

        private Angle() {

        }

    }

    public static class Length {

        public static final double METER = 1;
        public static final double CENTIMETER = CENTI * METER;
        public static final double KILOMETER = KILO * METER;
        public static final double INCH = CENTIMETER * 2.54;
        public static final double FOOT = 12 * INCH;
        public static final double NAUTICAL_MILE = 1852 * METER;

        private Length() {
        }
    }

    public static class Time {

        public static final double SECOND = 1;
        public static final double MINUTE = 60 * SECOND;
        public static final double HOUR = 60 * MINUTE;

        private Time() {
        }
    }

    public static class Speed {

        public static final double METER_PER_SECOND = Length.METER / Time.SECOND;
        public static final double KNOT = Length.NAUTICAL_MILE / Time.HOUR;
        public static final double KILOMETER_PER_HOUR = Length.KILOMETER / Time.HOUR;

        private Speed() {
        }
    }

}
