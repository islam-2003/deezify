package be.deezify.controllers;

import be.deezify.listeners.PlaybackServiceListener;
import be.deezify.models.Lyrics;
import be.deezify.models.dto.TrackDTO;
import be.deezify.views.KaraokeView;
import lombok.Getter;

import java.io.IOException;
import java.util.Optional;

/**
 * Controller for managing the karaoke menu.
 * This controller handles interactions between the karaoke view and the karaoke model.
 */
public class KaraokeController extends Controller<KaraokeView> implements PlaybackServiceListener {

    @Getter
    private final MetaController metaController;
    private Lyrics currentLyrics;

    /**
     * Constructor for the KaraokeController class.
     *
     * @param metaController The associated MetaController.
     * @throws Exception If an error occurs during initialization.
     */
    public KaraokeController(MetaController metaController) throws Exception {
        super(metaController, new KaraokeView());
        this.metaController = metaController;
        view.setController(this);
        view.initializeBundle();
        view.setStatus(KaraokeView.KaraokeState.WAITING);
    }
    /**
     * Callback method invoked when the track changes.
     * Attempts to load lyrics from the new track and update internal state.
     *
     * @param track The updated track information.
     */
    @Override
    public void onTrackUpdate(TrackDTO track) {
        try {
            Optional<Lyrics> optionalLyrics = track.getLyrics();
            if (optionalLyrics.isPresent()) {
                currentLyrics = optionalLyrics.get();
            } else {
                view.setStatus(KaraokeView.KaraokeState.EMPTY);
                currentLyrics = null;
            }
        } catch (IOException exception) {
            view.setStatus(KaraokeView.KaraokeState.ERROR);
            currentLyrics = null;
        }
    }

    /**
     * Callback method invoked with time updates during playback.
     * Responsible for highlighting the current line and word in the karaoke view.
     *
     * @param newTime   Current playback time in seconds.
     * @param totalTime Total duration of the track in seconds.
     */
    @Override
    public void onPlaybackTimeUpdate(double newTime, double totalTime) {
        if (currentLyrics == null) return;

        // Convert the current playback time from seconds to milliseconds
        long currentTimeMs = (long) (newTime * 1000);
        Lyrics.LyricsLine currentLine = currentLyrics.getCurrentLine(currentTimeMs);

        if (currentLine != null) {
            String fullLineText = currentLine.getText();
            String currentWord = "";

            // Iterate through the words in the line to find the currently sung word
            for (Lyrics.LyricsWord word : currentLine.getWords()) {
                // If the playback time has reached or passed the word's timestamp, update the current word
                if (currentTimeMs >= word.getTimestamp()) {
                    currentWord = word.getWord();
                } else {
                    break;
                }
            }

            view.updateLyrics(fullLineText, currentWord);
        } else {
            view.updateLyrics("", "");
        }
    }

    /**
     * Triggers a language update in the karaoke view.
     * Typically used when changing application language settings.
     */
    public void updateLanguage() {
        view.updateLanguage();
    }
}
