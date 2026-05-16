package be.deezify.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Utility class for number-related formatting operations.
 * Provides helper methods to convert numeric values to readable strings.
 */
public class NumberUtils {

    public static String formatDouble(double value) {
        NumberFormat numberFormat = new DecimalFormat("#0.0");
        return numberFormat.format(value).replace('.', ',');
    }

}
