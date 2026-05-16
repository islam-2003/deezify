package be.deezify.models;

import be.deezify.models.dto.TagDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestMetaData {

    private Metadata metadata;
    private TagDTO tag1;
    private TagDTO tag2;

    @BeforeEach
    void setUp() {
        metadata = new Metadata("Track Title", "Artist Name");
    }

    @Test
    void testInitialValues() {
        assertEquals("Track Title", metadata.getName());
        assertEquals("Artist Name", metadata.getArtist());
        assertTrue(metadata.getAlbum().isEmpty());
        assertTrue(metadata.getTags().isEmpty());
    }



    @Test
    void testOptionalPaths() {
        assertTrue(metadata.getCoverImagePath().isEmpty());
        assertTrue(metadata.getLyricsPath().isEmpty());

        Path coverPath = Path.of("cover.png");
        Path lyricsPath = Path.of("lyrics.lrc");
        metadata.setCoverImagePath(coverPath);
        metadata.setLyricsPath(lyricsPath);

        assertEquals(Optional.of(coverPath), metadata.getCoverImagePath());
        assertEquals(Optional.of(lyricsPath), metadata.getLyricsPath());
    }

    @Test
    void testGetLyricsWhenNullPath() throws IOException {
        assertTrue(metadata.getLyrics().isEmpty());
    }
}
