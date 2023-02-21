package ch.epfl.javions;

public final class Units {

    public static final double CENTI = 0.01;
    public static final double KILO = 1000;

    private Units() {

    }

    public static class Angle {

        private static final double RADIAN = 1;
        private static final double TURN = 2 * Math.PI * RADIAN;
        private static final double DEGREE = TURN/360;
        private static final double T32 = TURN/Math.pow(2, 32);

        private Angle() {

        }

    }

    public static class Length {

        private static final double METER = 1;
        

        private Length() {
        }
    }

    public static class Time {

        private static final double SECOND = 1;
        private static final double MINUTE = 60*SECOND;
        private static final double HOUR = 60*MINUTE;

        private Time() {
        }
    }

    public static class Speed {

        private Speed() {
        }
    }

}
