package be.deezify.models;

import be.deezify.models.dto.TagDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TestTrack {

    private Metadata metadata;
    private Path filePath;
    private Track track;

    @BeforeEach
    void setUp() {
        filePath = Path.of("/music/song.mp3");
        metadata = mock(Metadata.class);
        track = new Track(filePath, metadata);
    }

    @Test
    void testConstructorInitializesFields() {
        assertEquals(0, track.getId());
        assertEquals(filePath, track.getFilePath());
    }

    @Test
    void testSetAndGetName() {
        when(metadata.getName()).thenReturn("Song Name");

        assertEquals("Song Name", track.getName());

        track.setName("New Name");
        verify(metadata).setName("New Name");
    }

    @Test
    void testSetAndGetArtist() {
        when(metadata.getArtist()).thenReturn("Artist Name");

        assertEquals("Artist Name", track.getArtist());

        track.setArtist("New Artist");
        verify(metadata).setArtist("New Artist");
    }

    @Test
    void testSetAndGetAlbum() {
        Optional<String> album = Optional.of("Album Title");
        when(metadata.getAlbum()).thenReturn(album);

        assertEquals(album, track.getAlbum());

        track.setAlbum("New Album");
        verify(metadata).setAlbum("New Album");
    }

    @Test
    void testAddAndRemoveTag() {
        TagDTO tag = mock(TagDTO.class);

        track.addTag(tag);
        verify(metadata).addTag(tag);

        track.removeTag(tag);
        verify(metadata).removeTag(tag);
    }

    @Test
    void testRemoveAllTags() {
        track.removeAllTags();
        verify(metadata).removeAllTags();
    }

    @Test
    void testGetTags() {
        Set<TagDTO> mockTags = Set.of(mock(TagDTO.class));
        when(metadata.getTags()).thenReturn(mockTags);

        assertEquals(mockTags, track.getTags());
    }

    @Test
    void testGetCoverImagePath() {
        Optional<Path> mockPath = Optional.of(Path.of("/images/cover.png"));
        when(metadata.getCoverImagePath()).thenReturn(mockPath);

        assertEquals(mockPath, track.getCoverImagePath());
    }

    @Test
    void testSetAndGetId() {
        track.setId(99);
        assertEquals(99, track.getId());
    }
}
