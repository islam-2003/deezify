package be.deezify.listeners;

import be.deezify.models.dto.TrackDTO;

/**
 * Listener interface for changes in the play queue.
 */
public interface PlayQueueListener {

    default void onTrackAdded(TrackDTO track, int position) {};

    default void onTrackRemoved(TrackDTO track, int position) {};

    default void onQueueCleared() {};

}
