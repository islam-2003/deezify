package be.deezify.utils;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestColorUtil {

    @Test
    void testFormatColorToHex_whiteOpaque() {
        Color color = Color.WHITE; // (1.0, 1.0, 1.0, 1.0)
        String expected = "FFFFFFFF";
        String actual = ColorUtils.formatColorToHex(color);
        assertEquals(expected, actual);
    }

    @Test
    void testFormatColorToHex_blackTransparent() {
        Color color = new Color(0, 0, 0, 0); // transparent black
        String expected = "00000000";
        String actual = ColorUtils.formatColorToHex(color);
        assertEquals(expected, actual);
    }

    @Test
    void testFormatColorToHex_customColor() {
        Color color = new Color(0.2, 0.4, 0.6, 0.8);
        String expected = "336699CC"; // 0.2*255=51, 0.4*255=102, 0.6*255=153, 0.8*255=204
        String actual = ColorUtils.formatColorToHex(color);
        assertEquals(expected, actual);
    }

}
