package be.deezify.listeners;

import be.deezify.models.dto.PlaylistDTO;
import be.deezify.models.dto.TrackDTO;
import be.deezify.services.PlaylistService;

/**
 * Listener interface for playlist-related events.
 */
public interface PlaylistServiceListener {

    default void onTrackAdded(PlaylistDTO playlistDTO, TrackDTO trackDTO) {};
    default void onTrackRemoved(PlaylistDTO playlistDTO, TrackDTO trackDTO) {};
    default void onPlaylistChanged(PlaylistDTO playlistDTO, PlaylistService.PlaylistProperty playlistProperty) {};
    default void onPlaylistAdded(PlaylistDTO playlistDTO) {};

}
