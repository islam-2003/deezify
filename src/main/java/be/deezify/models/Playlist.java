package be.deezify.models;

import be.deezify.models.dto.PlaylistDTO;
import be.deezify.models.dto.TrackDTO;
import be.deezify.models.dto.UserDTO;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Object that represents a music playlist.
 * A playlist is composed of a name and a list of tracks.
 */
@Getter
@Setter
@RequiredArgsConstructor
public class Playlist implements PlaylistDTO, Indexable {

    private int id = 0;
    @NonNull
    private String name;
    private UserDTO owner;
    private boolean favorite = false;

    private final List<TrackDTO> tracks = new ArrayList<>();

    public void addTrack(@NonNull TrackDTO track) {
        if(!tracks.contains(track)) {
            tracks.add(track);
        }
    }

    public void removeTrack(@NonNull TrackDTO track) {
        tracks.remove(track);
    }

    public void removeTrackAtIndex(int index) {
        tracks.remove(index);
    }

    public void clear() {
        tracks.clear();
    }

    public Optional<UserDTO> getOwner() {
        return Optional.ofNullable(owner);
    }
}