package be.deezify.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestNumberUtils {

    @Test
    void testFormatDouble_withInteger() {
        double input = 5.0;
        String expected = "5,0";
        String actual = NumberUtils.formatDouble(input);
        assertEquals(expected, actual);
    }

    @Test
    void testFormatDouble_withOneDecimal() {
        double input = 3.14;
        String expected = "3,1"; // arrondi à une décimale
        String actual = NumberUtils.formatDouble(input);
        assertEquals(expected, actual);
    }

    @Test
    void testFormatDouble_withRounding() {
        double input = 2.96;
        String expected = "3,0"; // arrondi
        String actual = NumberUtils.formatDouble(input);
        assertEquals(expected, actual);
    }

    @Test
    void testFormatDouble_withZero() {
        double input = 0.0;
        String expected = "0,0";
        String actual = NumberUtils.formatDouble(input);
        assertEquals(expected, actual);
    }

    @Test
    void testFormatDouble_withNegativeNumber() {
        double input = -1.234;
        String expected = "-1,2";
        String actual = NumberUtils.formatDouble(input);
        assertEquals(expected, actual);
    }

}
