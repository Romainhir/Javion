package ch.epfl.javions.gui;

import ch.epfl.javions.Preconditions;
import javafx.scene.paint.Color;

/**
 * Class that represent a color ramp.
 *
 * @author Romain Hirschi (Sciper: 359286)
 * @author Moussab Ibrahim  (Sciper: 363888)
 */
public final class ColorRamp {

    /**
     * Specific color ramp that represent a plasma ramp.
     */
    public static final ColorRamp PLASMA = new ColorRamp(
            Color.valueOf("0x0d0887ff"), Color.valueOf("0x220690ff"),
            Color.valueOf("0x320597ff"), Color.valueOf("0x40049dff"),
            Color.valueOf("0x4e02a2ff"), Color.valueOf("0x5b01a5ff"),
            Color.valueOf("0x6800a8ff"), Color.valueOf("0x7501a8ff"),
            Color.valueOf("0x8104a7ff"), Color.valueOf("0x8d0ba5ff"),
            Color.valueOf("0x9814a0ff"), Color.valueOf("0xa31d9aff"),
            Color.valueOf("0xad2693ff"), Color.valueOf("0xb6308bff"),
            Color.valueOf("0xbf3984ff"), Color.valueOf("0xc7427cff"),
            Color.valueOf("0xcf4c74ff"), Color.valueOf("0xd6556dff"),
            Color.valueOf("0xdd5e66ff"), Color.valueOf("0xe3685fff"),
            Color.valueOf("0xe97258ff"), Color.valueOf("0xee7c51ff"),
            Color.valueOf("0xf3874aff"), Color.valueOf("0xf79243ff"),
            Color.valueOf("0xfa9d3bff"), Color.valueOf("0xfca935ff"),
            Color.valueOf("0xfdb52eff"), Color.valueOf("0xfdc229ff"),
            Color.valueOf("0xfccf25ff"), Color.valueOf("0xf9dd24ff"),
            Color.valueOf("0xf5eb27ff"), Color.valueOf("0xf0f921ff"));

    private final Color[] colors;

    /**
     * Constructor of the color ramp. In parameter is given the colors of the ramp.
     *
     * @param colors (Color...) : the colors of the ramp
     */
    public ColorRamp(Color... colors) {
        Preconditions.checkArgument(colors.length >= 2);
        this.colors = colors;
    }

    /**
     * Return the color in the ramp at a specific position. The position must be between 0 and 1 (included).
     *
     * @param value (double) : the color position in the ramp (between 0 and 1)
     * @return (Color) : the color at the given position
     */
    public Color at(double value) {
        if (value <= 0d) {
            return colors[0];
        }
        if (value >= 1) {
            return colors[colors.length - 1];
        }
        double pos = value * (colors.length - 1);
        double ratio = pos - (pos / (colors.length - 1));
        return colors[(int) Math.floor(pos)].interpolate(colors[(int) Math.ceil(pos)], ratio);
    }


}
