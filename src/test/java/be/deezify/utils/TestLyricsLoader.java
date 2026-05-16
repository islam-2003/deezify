package be.deezify.utils;

import be.deezify.models.Lyrics;
import be.deezify.models.Lyrics.LyricsLine;
import be.deezify.models.Lyrics.LyricsWord;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestLyricsLoader {

    @Test
    void testLineLevelTimestamps() throws IOException {
        String content = "[01:23.456]This is a line of lyrics\n";
        Path tempFile = Files.createTempFile("lyrics_test_", ".lrc");
        Files.write(tempFile, content.getBytes());

        Lyrics lyrics = LyricsLoader.loadFromFile(tempFile);
        assertEquals(1, lyrics.getLines().size());

        LyricsLine line = lyrics.getLines().get(0);
        assertEquals("This is a line of lyrics", line.getText());
        assertEquals(83456, line.getTimestamp());
        assertTrue(line.getWords().isEmpty());

        Files.deleteIfExists(tempFile);
    }

    @Test
    void testWordLevelTimestamps() throws IOException {
        String content = "[00:10.000]<00:10.000>Hello <00:10.500>world\n";
        Path tempFile = Files.createTempFile("lyrics_test_", ".lrc");
        Files.write(tempFile, content.getBytes());

        Lyrics lyrics = LyricsLoader.loadFromFile(tempFile);
        assertEquals(1, lyrics.getLines().size());

        LyricsLine line = lyrics.getLines().get(0);
        assertEquals("Hello world", line.getText());
        assertEquals(10000, line.getTimestamp());

        List<LyricsWord> words = line.getWords();
        assertEquals(2, words.size());
        assertEquals(new LyricsWord(10000, "Hello"), words.get(0));
        assertEquals(new LyricsWord(10500, "world"), words.get(1));

        Files.deleteIfExists(tempFile);
    }

    @Test
    void testMalformedLinesIgnored() throws IOException {
        String content = "This line has no timestamp\n[00:10.000]Valid line\n";
        Path tempFile = Files.createTempFile("lyrics_test_", ".lrc");
        Files.write(tempFile, content.getBytes());

        Lyrics lyrics = LyricsLoader.loadFromFile(tempFile);
        assertEquals(1, lyrics.getLines().size());

        LyricsLine line = lyrics.getLines().get(0);
        assertEquals("Valid line", line.getText());
        assertEquals(10000, line.getTimestamp());

        Files.deleteIfExists(tempFile);
    }

    @Test
    void testEmptyFile() throws IOException {
        Path tempFile = Files.createTempFile("lyrics_test_", ".lrc");
        Lyrics lyrics = LyricsLoader.loadFromFile(tempFile);
        assertTrue(lyrics.getLines().isEmpty());
        Files.deleteIfExists(tempFile);
    }
}
