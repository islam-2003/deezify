package be.deezify.models.dto;

import java.util.List;
import java.util.Optional;

/**
 * Interface representing a playlist of tracks.
 */
public interface PlaylistDTO extends IndexableDTO {

    String getName();
    List<TrackDTO> getTracks();
    void addTrack(TrackDTO trackDTO);
    void removeTrack(TrackDTO trackDTO);
    void removeTrackAtIndex(int index);
    void clear();
    boolean isFavorite();
    Optional<UserDTO> getOwner();
}
