package be.deezify.utils;

import be.deezify.models.Lyrics;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for loading lyrics from a LRC timestamped file format.
 */
public class LyricsLoader {

    // Regex pattern for lines with timestamps: [min:sec.decimal]text
    private static final Pattern LINE_PATTERN = Pattern.compile("\\[(\\d+):(\\d+\\.\\d+)](.*)");
    // Regex pattern for words with individual timestamps: <min:sec.decimal>word
    private static final Pattern WORD_PATTERN = Pattern.compile("\\s*<(\\d+):(\\d+\\.\\d+)>\\s*(\\S+)");

    /**
     * Loads a lyrics file and parses it into a Lyrics object with timestamped lines and words.
     *
     * @param path Path to the lyrics file.
     * @return Parsed Lyrics object.
     * @throws IOException If reading the file fails.
     */
    public static Lyrics loadFromFile(Path path) throws IOException {
        Lyrics lyric = new Lyrics();
        List<String> lines = Files.readAllLines(path);

        for (String rawLine : lines) {
            // Try to match a full line from the file with a timestamp
            Matcher lineMatcher = LINE_PATTERN.matcher(rawLine);
            if (!lineMatcher.find()) continue;

            // Parse line timestamp from [min:sec.dec] to milliseconds
            long lineTimestamp = parseTimestamp(lineMatcher.group(1), lineMatcher.group(2));
            String content = lineMatcher.group(3).trim(); // Retrieves the content associated to the timestamp

            // Create a new LyricsLine and assign its timestamp
            Lyrics.LyricsLine lyricLine = new Lyrics.LyricsLine();
            lyricLine.setTimestamp(lineTimestamp);

            // Check for per-word timestamps in the content
            Matcher wordMatcher = WORD_PATTERN.matcher(content);
            if (wordMatcher.find()) {
                // If words have timestamps, build the full line from those
                StringBuilder fullLine = new StringBuilder();
                wordMatcher.reset(); // Reset matcher to re-iterate
                while (wordMatcher.find()) {
                    long wordTimestamp = parseTimestamp(wordMatcher.group(1), wordMatcher.group(2));
                    String wordText = wordMatcher.group(3);

                    // Append to the line and add word with timestamp
                    fullLine.append(wordText).append(" ");
                    lyricLine.getWords().add(new Lyrics.LyricsWord(wordTimestamp, wordText));
                }
                // Set the full line text composed from individual words
                lyricLine.setText(fullLine.toString().trim());
            } else {
                // Fallback: No word-level timestamps, we use the raw text
                lyricLine.setText(content);
            }

            lyric.addLine(lyricLine);
        }

        lyric.sortLines();
        return lyric;
    }

    /**
     * Converts timestamp from minute:second format to milliseconds.
     */
    private static long parseTimestamp(String minutes, String seconds) {
        int min = Integer.parseInt(minutes);
        float sec = Float.parseFloat(seconds);
        return (long) ((min * 60L + sec) * 1000);
    }
}

