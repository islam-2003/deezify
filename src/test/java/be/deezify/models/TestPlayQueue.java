package be.deezify.models;

import be.deezify.models.dto.TrackDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class TestPlayQueue {

    private TrackDTO track1;
    private TrackDTO track2;

    @BeforeEach
    void setUp() {
        track1 = mock(Track.class);
        track2 = mock(Track.class);
    }

    @Test
    void testAddTrack() {
        PlayQueue playQueue = new PlayQueue();
        playQueue.addTrack(track1);

        assertEquals(1, playQueue.getTracks().size());
        assertTrue(playQueue.getTracks().contains(track1));
    }

    @Test
    void testNextTrack() {
        PlayQueue playQueue = new PlayQueue();
        playQueue.addTrack(track1);
        playQueue.addTrack(track2);

        Optional<TrackDTO> nextTrack = playQueue.nextTrack();

        assertTrue(nextTrack.isPresent());
        assertEquals(track2, nextTrack.get());
    }

    @Test
    void testNextTrackNoMoreTracks() {
        PlayQueue playQueue = new PlayQueue();
        playQueue.addTrack(track1);

        Optional<TrackDTO> nextTrack = playQueue.nextTrack();

        assertFalse(nextTrack.isPresent());
    }

    @Test
    void testPreviousTrack() {
        PlayQueue playQueue = new PlayQueue();
        playQueue.addTrack(track1);
        playQueue.addTrack(track2);
        playQueue.nextTrack(); // move to track2

        Optional<TrackDTO> previousTrack = playQueue.previousTrack();

        assertTrue(previousTrack.isPresent());
        assertEquals(track1, previousTrack.get());
    }

    @Test
    void testPreviousTrackNoPrevious() {
        PlayQueue playQueue = new PlayQueue();
        playQueue.addTrack(track1);

        Optional<TrackDTO> previousTrack = playQueue.previousTrack();

        assertFalse(previousTrack.isPresent());
    }

    @Test
    void testCurrentTrack() {
        PlayQueue playQueue = new PlayQueue();
        playQueue.addTrack(track1);

        Optional<TrackDTO> currentTrack = playQueue.currentTrack();

        assertTrue(currentTrack.isPresent());
        assertEquals(track1, currentTrack.get());
    }

    @Test
    void testInsertTrackInQueueAtIndex() {
        PlayQueue playQueue = new PlayQueue();
        playQueue.addTrack(track1);
        playQueue.insertTrackInQueueAtIndex(1, track2);

        assertEquals(2, playQueue.getTracks().size());
        assertEquals(track2, playQueue.getTracks().get(1));
    }

    @Test
    void testInsertTrackInQueueAtInvalidIndex() {
        PlayQueue playQueue = new PlayQueue();

        assertThrows(IndexOutOfBoundsException.class, () -> {
            playQueue.insertTrackInQueueAtIndex(10, track1);
        });
    }

    @Test
    void testRemoveTrackFromQueue() {
        PlayQueue playQueue = new PlayQueue();
        playQueue.addTrack(track1);
        playQueue.addTrack(track2);

        playQueue.removeTrackFromQueue(track1);

        assertEquals(1, playQueue.getTracks().size());
        assertFalse(playQueue.getTracks().contains(track1));
    }

    @Test
    void testRemoveTrackFromQueueNotFound() {
        PlayQueue playQueue = new PlayQueue();

        assertThrows(IllegalArgumentException.class, () -> {
            playQueue.removeTrackFromQueue(track1);
        });
    }

    @Test
    void testRemoveTrackFromQueueAtIndex() {
        PlayQueue playQueue = new PlayQueue();
        playQueue.addTrack(track1);

        playQueue.removeTrackFromQueueAtIndex(0);

        assertTrue(playQueue.getTracks().isEmpty());
    }

    @Test
    void testClear() {
        PlayQueue playQueue = new PlayQueue();
        playQueue.addTrack(track1);

        playQueue.clear();

        assertTrue(playQueue.getTracks().isEmpty());
    }
}
