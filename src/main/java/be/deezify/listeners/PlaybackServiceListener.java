package be.deezify.listeners;

import be.deezify.models.dto.TrackDTO;
import be.deezify.services.PlaybackService;

/**
 * Listener interface for playback service events.
 */
public interface PlaybackServiceListener {

    default void onTrackUpdate(TrackDTO newTrack) {};

    default void onPlaybackStateUpdate(PlaybackService.Status status) {};

    default void onPlaybackTimeUpdate(double newTime, double totalTime) {};

    default void onTrackEnd() {};

    default void onAmplitudeUpdate(double amplitude) {};

    default void onFrequencyUpdate(double frequency) {};

}
