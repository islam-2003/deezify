package be.deezify.models;

import be.deezify.models.Lyrics.LyricsLine;
import be.deezify.models.Lyrics.LyricsWord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestLyrics {

    private Lyrics lyrics;
    private LyricsLine line1;
    private LyricsLine line2;

    @BeforeEach
    void setUp() {
        lyrics = new Lyrics();

        line1 = new LyricsLine(1000, "First line",
                List.of(new LyricsWord(1000, "First"), new LyricsWord(1200, "line")));

        line2 = new LyricsLine(2000, "Second line",
                List.of(new LyricsWord(2000, "Second"), new LyricsWord(2200, "line")));

        lyrics.addLine(line2);
        lyrics.addLine(line1);
    }

    @Test
    void testAddLine() {
        assertEquals(2, lyrics.getLines().size());
        assertTrue(lyrics.getLines().contains(line1));
    }

    @Test
    void testSortLines() {
        lyrics.sortLines();
        assertEquals(line1, lyrics.getLines().get(0));
        assertEquals(line2, lyrics.getLines().get(1));
    }

    @Test
    void testGetCurrentLine() {
        lyrics.sortLines();
        assertNull(lyrics.getCurrentLine(500));
        assertEquals(line1, lyrics.getCurrentLine(1000));
        assertEquals(line1, lyrics.getCurrentLine(1500));
        assertEquals(line2, lyrics.getCurrentLine(2000));
    }

    @Test
    void testGetFullLyrics() {
        lyrics.sortLines();
        String expected = "First line\nSecond line";
        assertEquals(expected, lyrics.getFullLyrics());
    }
}
