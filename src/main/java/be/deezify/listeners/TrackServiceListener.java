package be.deezify.listeners;

import be.deezify.models.dto.TrackDTO;
import be.deezify.services.TrackService;

/**
 * Listener interface for track-related events.
 * Implementing classes can react to track additions, removals, and metadata changes.
 */
public interface TrackServiceListener {

    default void onTrackAdded(TrackDTO trackDTO) {};

    default void onTrackRemoved(TrackDTO trackDTO) {};

    default void onTrackChanged(TrackDTO trackDTO, TrackService.TrackProperty trackProperty) {};

}
