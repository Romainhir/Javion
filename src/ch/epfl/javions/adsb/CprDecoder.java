package ch.epfl.javions.adsb;

import ch.epfl.javions.GeoPos;
import ch.epfl.javions.Preconditions;
import ch.epfl.javions.Units;


/**
 * Class that decode the CPR encoded position
 */
public class CprDecoder {

    private final static int Z_PHI_ZERO = 60;

    private CprDecoder() {

    }

    /**
     * Decode the position of an aircraft
     *
     * @param x0
     * @param y0
     * @param x1
     * @param y1
     * @param mostRecent (int) : 0 if the position is the most recent, 1 if the position is the second most recent
     * @return (GeoPos) : the position of the aircraft
     */
    public static GeoPos decodePosition(double x0, double y0, double x1, double y1, int mostRecent) {
        Preconditions.checkArgument(mostRecent == 0 || mostRecent == 1);
        double z_phi_zero = Math.rint(((Z_PHI_ZERO - 1) * y0 - Z_PHI_ZERO * y1));
        double z_phi_one;
        if (z_phi_zero < 0) {
            z_phi_one = z_phi_zero + Z_PHI_ZERO - 1;
            z_phi_zero += Z_PHI_ZERO;
        } else {
            z_phi_one = z_phi_zero;
        }
        double phi_zero = (z_phi_zero + y0) / Z_PHI_ZERO;
        double phi_one = (z_phi_one + y1) / (Z_PHI_ZERO - 1);

        if (phi_zero >= 0.5) {
            phi_zero -= 1;
        }

        if (phi_one >= 0.5) {
            phi_one -= 1;
        }


        double longitudeNbzero = findLongitudeZone(phi_zero);
        double longitudeNbone = findLongitudeZone(phi_one);
        if ((int) longitudeNbzero != (int) longitudeNbone) {
            return null;
        }

        double Z_lambda_one = longitudeNbzero - 1;

        double z_lambda_zero = Math.rint((Z_lambda_one * x0 - longitudeNbzero * x1));
        double z_lambda_one;
        if (z_lambda_zero < 0) {
            z_lambda_one = z_lambda_zero + Z_lambda_one;
            z_lambda_zero += longitudeNbzero;
        } else {
            z_lambda_one = z_lambda_zero;
        }

        double lambda_zero = (z_lambda_zero + x0) / longitudeNbzero;
        double lambda_one;
        if (Z_lambda_one == 0) {
            lambda_one = lambda_zero;
        } else {
            lambda_one = (z_lambda_one + x1) / Z_lambda_one;
        }

        if (lambda_zero >= 0.5) {
            lambda_zero -= 1;
        }
        if (lambda_one >= 0.5) {
            lambda_one -= 1;
        }

        if (mostRecent == 0) {
            if (checkLatitude(phi_zero)) {
                return null;
            }

            return new GeoPos((int) Math.rint(Units.convert(lambda_zero, Units.Angle.TURN, Units.Angle.T32)),
                    (int) Math.rint(Units.convert(phi_zero, Units.Angle.TURN, Units.Angle.T32)));
        } else {
            if (checkLatitude(phi_one)) {
                return null;
            }

            return new GeoPos((int) Math.rint(Units.convert(lambda_one, Units.Angle.TURN, Units.Angle.T32)),
                    (int) Math.rint(Units.convert(phi_one, Units.Angle.TURN, Units.Angle.T32)));
        }

    }

    private static boolean checkLatitude(double phi) {
        double k = Units.convert(phi, Units.Angle.TURN, Units.Angle.DEGREE);//TODO Inutile ??
        return 90 <= Units.convert(phi, Units.Angle.TURN, Units.Angle.DEGREE) ||
                Units.convert(phi, Units.Angle.TURN, Units.Angle.DEGREE) <= -90;
    }

    private static double findLongitudeZone(double phi) {
        double A = Math.acos(1 - ((1 - Math.cos(2 * Math.PI / Z_PHI_ZERO)) /
                (Math.cos(Units.convertFrom(phi, Units.Angle.TURN)) *
                        Math.cos(Units.convertFrom(phi, Units.Angle.TURN)))));
        if (Double.isNaN(A))
            return 1;

        return Math.floor(2 * Math.PI / A);
    }

}