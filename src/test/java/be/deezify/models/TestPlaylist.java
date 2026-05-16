package be.deezify.models;

import be.deezify.models.dto.TrackDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class TestPlaylist {

    private Playlist playlist;
    private TrackDTO track1;
    private TrackDTO track2;

    @BeforeEach
    void setUp() {
        playlist = new Playlist("My Playlist");
        track1 = mock(TrackDTO.class);
        track2 = mock(TrackDTO.class);
    }

    @Test
    void testInitialState() {
        assertEquals("My Playlist", playlist.getName());
        assertTrue(playlist.getTracks().isEmpty());
        assertEquals(0, playlist.getId());
    }

    @Test
    void testAddTrack() {
        playlist.addTrack(track1);
        assertEquals(1, playlist.getTracks().size());
        assertTrue(playlist.getTracks().contains(track1));
    }

    @Test
    void testRemoveTrack() {
        playlist.addTrack(track1);
        playlist.addTrack(track2);

        playlist.removeTrack(track1);

        List<TrackDTO> remaining = playlist.getTracks();
        assertEquals(1, remaining.size());
        assertFalse(remaining.contains(track1));
        assertTrue(remaining.contains(track2));
    }

    @Test
    void testRemoveTrackAtIndex() {
        playlist.addTrack(track1);
        playlist.addTrack(track2);

        playlist.removeTrackAtIndex(0);

        assertEquals(1, playlist.getTracks().size());
        assertEquals(track2, playlist.getTracks().get(0));
    }

    @Test
    void testClearTracks() {
        playlist.addTrack(track1);
        playlist.addTrack(track2);

        playlist.clear();

        assertTrue(playlist.getTracks().isEmpty());
    }

    @Test
    void testSetAndGetId() {
        playlist.setId(42);
        assertEquals(42, playlist.getId());
    }

    @Test
    void testSetAndGetName() {
        playlist.setName("New Name");
        assertEquals("New Name", playlist.getName());
    }
}
