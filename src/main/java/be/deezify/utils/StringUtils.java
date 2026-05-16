package be.deezify.utils;

/**
 * Utility class for string-related operations.
 * Provides helpers for formatting time and other string representations.
 */
public class StringUtils {

    public static String formatTime(double seconds) {
        int minutes = (int) seconds / 60;
        int sec = (int) seconds % 60;
        return String.format("%02d:%02d", minutes, sec);
    }

}
