package be.deezify.models;

import be.deezify.json.PathAdapter;
import be.deezify.models.dto.TagDTO;
import be.deezify.models.dto.TrackDTO;
import be.deezify.models.dto.UserDTO;
import com.google.gson.annotations.JsonAdapter;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

/**
 * Represents an audio track.
 * This class contains the metadata of an audio track, such as title, artist, album, etc.
 */
@Getter
public class Track implements TrackDTO, Indexable {

    @Setter
    private int id = 0;
    @JsonAdapter(PathAdapter.class)
    private final Path filePath;
    private final Metadata metadata;
    @Setter
    private UserDTO owner;

    public Track(Path filePath, Metadata metadata) {
        this.filePath = filePath;
        this.metadata = metadata;
    }

    public void setName(String newName) {
        metadata.setName(newName);
    }

    @Override
    public String getName() {
        return metadata.getName();
    }

    public void setArtist(String artistName) {
        metadata.setArtist(artistName);
    }

    @Override
    public String getArtist() {
        return metadata.getArtist();
    }

    public void setAlbum(String albumName) {
        metadata.setAlbum(albumName);
    }

    @Override
    public Optional<String> getAlbum() {
        return metadata.getAlbum();
    }

    public void addTag(TagDTO tag) {
        metadata.addTag(tag);
    }

    public void removeTag(TagDTO tag) {
        metadata.removeTag(tag);
    }

    public void removeAllTags() {
        metadata.removeAllTags();
    }

    @Override
    public Set<TagDTO> getTags() {
        return metadata.getTags();
    }

    @Override
    public Optional<Path> getCoverImagePath() {
        return metadata.getCoverImagePath();
    }

    @Override
    public Optional<Lyrics> getLyrics() throws IOException {
        return metadata.getLyrics();
    }

    public Optional<UserDTO> getOwner() {
        return Optional.ofNullable(owner);
    }
}