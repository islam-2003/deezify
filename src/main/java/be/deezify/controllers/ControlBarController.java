package be.deezify.controllers;

import be.deezify.listeners.PlaybackServiceListener;
import be.deezify.listeners.UserServiceListener;
import be.deezify.models.User;
import be.deezify.models.dto.TrackDTO;
import be.deezify.models.dto.UserDTO;
import be.deezify.services.PlaybackService;
import be.deezify.services.TrackService;
import be.deezify.utils.AlertUtils;
import be.deezify.utils.StringUtils;
import be.deezify.views.global.ControlBarView;
import javafx.scene.image.ImageView;
import lombok.Getter;

import java.nio.file.Path;
import java.util.Objects;

public class ControlBarController extends Controller<ControlBarView> implements PlaybackServiceListener, UserServiceListener {

    private static final Path DEFAULT_COVER_PATH = Path.of("assets", "covers", "default.png");

    private final ControlBarView controlBarView;
    private final PlaybackService playbackService;
    private final PlayQueueController playQueueController;
    private final LyricsController lyricsController;
    private final UserDTO activeUser = User.GUEST_USER;
    private final TrackService trackService;

    @Getter
    private final MetaController metaController;

    public ControlBarController(MetaController metaController,
                                ControlBarView controlBarView,
                                PlaybackService playbackService,
                                PlayQueueController playQueueController,
                                LyricsController lyricsController,
                                TrackService trackService) {
        super(metaController, controlBarView);
        this.metaController = metaController;
        this.controlBarView = controlBarView;
        this.playbackService = playbackService;
        this.playQueueController = playQueueController;
        this.lyricsController = lyricsController;
        this.trackService = trackService;
        view.setControlBarController(this);
    }

    /**
     * Updates the control bar when a new track is loaded.
     *
     * @param newTrack The new track to display.
     */
    @Override
    public void onTrackUpdate(TrackDTO newTrack) {
        controlBarView.setArtist(newTrack.getArtist());
        controlBarView.setSongTitle(newTrack.getName());
        newTrack.getCoverImagePath().ifPresentOrElse(path -> {
            controlBarView.setAlbumCover(new ImageView(path.toUri().toString()));
        }, () -> controlBarView.setAlbumCover(new ImageView(DEFAULT_COVER_PATH.toUri().toString())));
    }

    /**
     * Updates the play/pause icon based on the playback status.
     *
     * @param status The current playback state.
     */
    @Override
    public void onPlaybackStateUpdate(PlaybackService.Status status) {
        if (Objects.requireNonNull(status) == PlaybackService.Status.PLAYING) {
            view.updatePlayButtonText("▶");
        } else {
            view.updatePlayButtonText("⏸");
        }
    }

    /**
     * Updates the progress bar, current time, and total time of the track.
     *
     * @param newTime   The current playback time in seconds.
     * @param totalTime The total duration of the track in seconds.
     */
    @Override
    public void onPlaybackTimeUpdate(double newTime, double totalTime) {
        controlBarView.setCurrentTime(StringUtils.formatTime(newTime));
        controlBarView.setTotalTime(StringUtils.formatTime(totalTime));
        controlBarView.setProgress(newTime / totalTime * 100);
    }

    /**
     * Sets the playback position based on slider value.
     *
     * @param value A percentage value from 0 to 100 representing track position.
     */
    public void setProgress(double value) {
        playbackService.seek(value);
    }

    public void setVolume(double value) {
        playbackService.setVolume(value);
    }

    public void pause() {
        switch (playbackService.getStatus()) {
            case PLAYING -> playbackService.pause();
            case PAUSED -> playbackService.resume();
            case STOPPED -> playQueueController.nextTrack();
            case NULL -> playQueueController.playCurrentTrack();
            case ERROR -> AlertUtils.showError("error.title.generic", "error.text.generic");
        }
    }

    public void next() {
        playQueueController.nextTrack();
    }

    public void previous() {
        playQueueController.previousTrack();
    }

    public void speedUp() {
        playbackService.increasePlaybackSpeed();
    }

    public void slowDown() {
        playbackService.decreasePlaybackSpeed();
    }

    public void shuffle() {
        playRandomTrack();
    }

    public void setBalance(double value) {
        playbackService.setBalance(value);
    }

    public void showLyrics() {
        lyricsController.showLyricsInRightPane();
    }

    public void setTransitionDuration(double value) {
        playbackService.setTransitionDuration(value);
    }

    public void playRandomTrack() {
        try {
            TrackDTO randomTrack = trackService.getRandomForUser(activeUser);
            playQueueController.clearQueue(); // Clear the current queue
            playQueueController.addTrackToQueue(randomTrack); // Add the random track to the queue
            playQueueController.playCurrentTrack(); // Play the current track
        } catch (Exception e) {
            AlertUtils.showError("error.title.generic", "error.text.generic");
        }

    }

    public void updateLanguage() {
        view.updateLanguage();
    }
}
