package be.deezify.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a full lyrics structure with timestamped lines and optionally timestamped words.
 */
@Data
@NoArgsConstructor
public class Lyrics {
    private List<LyricsLine> lines = new ArrayList<>();

    /**
     * Adds a "line" to the lyrics.
     */
    public void addLine(LyricsLine line) {
        lines.add(line);
    }

    /**
     * Sorts all lyrics lines chronologically.
     */
    public void sortLines() {
        Collections.sort(lines);
    }

    /**
     * Retrieves the current line based on the playback time.
     *
     * @param currentTime Playback time in milliseconds.
     * @return The current lyrics line or null if none match.
     */
    public LyricsLine getCurrentLine(long currentTime) {
        for (int i = lines.size() - 1; i >= 0; i--) {
            if (currentTime >= lines.get(i).getTimestamp()) {
                return lines.get(i);
            }
        }
        return null;
    }

    /**
     * Concatenates all lyrics into a single string.
     *
     * @return Full lyrics as plain text.
     */
    public String getFullLyrics() {
        StringBuilder fullLyrics = new StringBuilder();
        for (LyricsLine line : lines) {
            if (line.getText() != null && !line.getText().isEmpty()) {
                fullLyrics.append(line.getText()).append("\n");
            }
        }
        return fullLyrics.toString().trim();
    }

    /**
     * Represents a line of lyrics with a timestamp and optional words.
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LyricsLine implements Comparable<LyricsLine> {
        private long timestamp; // In milliseconds
        private String text;
        private List<LyricsWord> words = new ArrayList<>();

        @Override
        public int compareTo(LyricsLine other) {
            return Long.compare(this.timestamp, other.timestamp);
        }
    }

    /**
     * Represents a single word in a lyrics line with a timestamp.
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LyricsWord {
        private long timestamp;
        private String word;
    }
}

