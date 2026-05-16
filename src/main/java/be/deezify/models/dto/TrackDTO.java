package be.deezify.models.dto;

import be.deezify.models.Lyrics;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

/**
 * Interface representing a track with metadata and content.
 */
public interface TrackDTO extends IndexableDTO {

    String getName();
    String getArtist();
    Optional<String> getAlbum();
    Set<TagDTO> getTags();
    Path getFilePath();
    Optional<Lyrics> getLyrics() throws IOException;
    Optional<Path> getCoverImagePath();

}
