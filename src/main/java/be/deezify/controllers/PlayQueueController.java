package be.deezify.controllers;

import be.deezify.listeners.PlayQueueListener;
import be.deezify.listeners.PlaybackServiceListener;
import be.deezify.models.PlayQueue;
import be.deezify.models.dto.TrackDTO;
import be.deezify.services.PlaybackService;
import be.deezify.views.PlayQueueView;
import lombok.Getter;

import java.io.IOException;

/**
 * Controller for managing the play queue.
 * Handles interactions between the play queue model, the view, and playback service.
 * Listens for changes in the queue and playback events to update the UI.
 */

public class PlayQueueController extends Controller<PlayQueueView> implements PlayQueueListener, PlaybackServiceListener {

    private final PlayQueue playQueue;
    private final PlaybackService playbackService;
    @Getter
    private final MetaController metaController;


    /**
     * Constructs a PlayQueueController and sets it as the controller for the play queue view.
     *
     * @param metaController    The global meta controller.
     * @param playQueue         The playback queue.
     * @param playbackService   The service for controlling playback.
     * @throws IOException If the view fails to load.
     */
    public PlayQueueController(MetaController metaController, PlayQueue playQueue, PlaybackService playbackService) throws IOException {
        super(metaController, new PlayQueueView());
        this.view.setPlayQueueController(this);
        this.metaController = metaController;
        this.playQueue = playQueue;
        this.playbackService = playbackService;
    }

    public void addTrackToQueue(TrackDTO track) {
        playQueue.addTrack(track);
    }

    public void removeTrackFromQueueAtIndex(int index) {
        playQueue.removeTrackFromQueueAtIndex(index);
    }

    public void nextTrack() {
        playQueue.nextTrack().ifPresent(playbackService::playTrack);
    }

    public void previousTrack() {
        playQueue.previousTrack().ifPresent(playbackService::playTrack);
    }

    public void playCurrentTrack() {
        playQueue.currentTrack().ifPresent(playbackService::playTrack);
    }

    @Override
    public void onTrackAdded(TrackDTO track, int position) {
        view.updateTrackList(playQueue.getTracks());
    }

    @Override
    public void onTrackRemoved(TrackDTO track, int position) {
        view.updateTrackList(playQueue.getTracks());
    }

    @Override
    public void onQueueCleared() {
        view.clearTrackList();
    }

    @Override
    public void onTrackEnd() {
        playQueue.nextTrack().ifPresent(playbackService::playTrack);
    }

    public void clearQueue() {
        playQueue.clear();
        playbackService.stop();
    }

    public void updateLanguage() {
        view.updateLanguage();
    }
}
