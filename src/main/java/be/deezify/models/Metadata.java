package be.deezify.models;

import be.deezify.models.dto.TagDTO;
import be.deezify.utils.LyricsLoader;
import lombok.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Represents the metadata associated with a track,
 * including its name, artist, album, tags, lyrics, and cover image path.
 */
@Getter
@Setter
@RequiredArgsConstructor
public class Metadata {

    @NonNull
    private String name;
    @NonNull
    private String artist;
    private String album;
    private Path coverImagePath;
    private final Set<TagDTO> tags = new HashSet<>();
    private Path lyricsPath;
    @Setter(AccessLevel.NONE)
    private transient Lyrics lyrics;

    public Optional<String> getAlbum() {
        return Optional.ofNullable(album);
    }

    public void addTag(TagDTO tag) {
        tags.add(tag);
    }

    public void removeTag(TagDTO tag) {
        tags.remove(tag);
    }

    public void removeAllTags() {
        tags.clear();
    }

    public Set<TagDTO> getTags() {
        return new HashSet<>(tags);
    }

    public Optional<Path> getCoverImagePath() {
        return Optional.ofNullable(coverImagePath);
    }

    public Optional<Path> getLyricsPath() {
        return Optional.ofNullable(lyricsPath);
    }

    /**
     * Loads and returns the lyrics object from file if not already loaded.
     *
     * @return Optional containing the lyrics object.
     * @throws IOException If reading the lyrics file fails.
     */
    public Optional<Lyrics> getLyrics() throws IOException {
        if (lyrics == null && lyricsPath != null) {
            lyrics = LyricsLoader.loadFromFile(lyricsPath);
        }
        return Optional.ofNullable(lyrics);
    }
}
