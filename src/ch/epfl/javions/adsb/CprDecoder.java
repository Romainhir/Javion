package ch.epfl.javions.adsb;

import ch.epfl.javions.GeoPos;
import ch.epfl.javions.Preconditions;
import ch.epfl.javions.Units;

public class CprDecoder {

    private final static int Z_PHI_ZERO = 60;
    private CprDecoder() {

    }

    public static GeoPos decodePosition(double x0, double y0, double x1, double y1, int mostRecent) {
        Preconditions.checkArgument(mostRecent == 0 || mostRecent == 1);
        int z_phi_zero = (int)Math.rint(((Z_PHI_ZERO-1) * y0 - Z_PHI_ZERO * y1));
        int z_phi_one;
        if (z_phi_zero < 0) {
            z_phi_one = z_phi_zero + Z_PHI_ZERO;
        }else{
            z_phi_one = z_phi_zero;
        }
        double phi_zero = ( z_phi_zero + y0) / Z_PHI_ZERO;
        double phi_one = ( z_phi_one + y1) / (Z_PHI_ZERO - 1);

        if(phi_zero >= 0.5){
            phi_zero -= 1;
        }

        if(phi_one >= 0.5){
            phi_one -= 1;
        }
        double A = Math.acos(1 - ((1 - Math.cos(2 * Math.PI / Z_PHI_ZERO)) /
                (Math.cos(Units.convertFrom(phi_zero, Units.Angle.TURN)) *
                        Math.cos(Units.convertFrom(phi_zero, Units.Angle.TURN)))));

        if (Double.isNaN(A)){
            A = 1;
        }

        int Z_lambda_zero = (int) Math.floor(2 * Math.PI / A);
        int Z_lambda_one = Z_lambda_zero - 1;

        int z_lambda_zero =  (int)Math.rint((Z_lambda_one * x0 - Z_lambda_zero * x1));
        int z_lambda_one;
        if (z_lambda_zero < 0) {
            z_lambda_one = z_lambda_zero + Z_lambda_one;
        }else{
            z_lambda_one = z_lambda_zero;
        }

        double lambda_zero = (z_lambda_zero + x0) / Z_lambda_zero;
        double lambda_one = (z_lambda_one + x1) / Z_lambda_one;
        if (lambda_zero >= 0.5) {
            lambda_zero -= 1;
        }
        if (lambda_one >= 0.5) {
            lambda_one -= 1;
        }

        return new GeoPos( (int) Units.convert(lambda_zero, Units.Angle.TURN, Units.Angle.T32),
                (int) Units.convert(phi_zero, Units.Angle.TURN, Units.Angle.T32));
    }

}