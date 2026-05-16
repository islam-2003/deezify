package be.deezify.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestStringUtils {

    @Test
    void testFormatTime_ZeroSeconds() {
        assertEquals("00:00", StringUtils.formatTime(0));
    }

    @Test
    void testFormatTime_LessThanOneMinute() {
        assertEquals("00:45", StringUtils.formatTime(45));
    }

    @Test
    void testFormatTime_ExactlyOneMinute() {
        assertEquals("01:00", StringUtils.formatTime(60));
    }

    @Test
    void testFormatTime_MultipleMinutes() {
        assertEquals("03:25", StringUtils.formatTime(205));
    }

    @Test
    void testFormatTime_NonIntegerSeconds() {
        assertEquals("02:30", StringUtils.formatTime(150.9));
    }

    @Test
    void testFormatTime_JustUnderNextMinute() {
        assertEquals("01:59", StringUtils.formatTime(119.9));
    }

    @Test
    void testFormatTime_JustOverMinute() {
        assertEquals("02:00", StringUtils.formatTime(120.1));
    }
}
