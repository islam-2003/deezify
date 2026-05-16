package be.deezify.utils;

import javafx.scene.paint.Color;

/**
 * Utility class for color-related operations.
 * Provides helper methods for formatting JavaFX {@link Color} objects.
 */
public class ColorUtils {

    public static String formatColorToHex(Color color) {
        return String.format("%02X%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255),
                (int) (color.getOpacity() * 255));
    }

}
