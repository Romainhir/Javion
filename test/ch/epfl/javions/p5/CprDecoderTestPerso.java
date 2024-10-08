package ch.epfl.javions.adsb;

import ch.epfl.javions.GeoPos;
import ch.epfl.javions.Units;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CprDecoderTestPerso {
    @Test
    void basicTest() {
        double y0 = Math.scalb(94445, -17);
        double y1 = Math.scalb(77558, -17);
        double x0 = Math.scalb(111600, -17);
        double x1 = Math.scalb(108865, -17);

        GeoPos pos0 = CprDecoder.decodePosition(x0, y0, x1, y1, 0);
        System.out.println(pos0);

        assertEquals(7.476062, Units.convertTo(pos0.longitude(),Units.Angle.DEGREE), 1e-6);
        assertEquals(46.323349, Units.convertTo(pos0.latitude(),Units.Angle.DEGREE), 1e-6);
    }

    @Test
    void edStem() {
        GeoPos pos = CprDecoder.decodePosition(0.62,0.42,0.6200000000000000001,0.4200000000000000001,0);
        System.out.println(pos);
    }

    @Test
    void edStemSchinz() {
        double x0 = Math.scalb(111600d, -17);
        double y0 = Math.scalb(94445d, -17);
        double x1 = Math.scalb(108865d, -17);
        double y1 = Math.scalb(77558d, -17);

        GeoPos p = CprDecoder.decodePosition(x0, y0, x1, y1, 0);
        assertEquals("(7.476062346249819°, 46.323349038138986°)", p.toString());
    }
}
