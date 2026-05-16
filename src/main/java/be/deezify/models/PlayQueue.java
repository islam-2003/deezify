package be.deezify.models;

import be.deezify.listeners.Listenable;
import be.deezify.listeners.PlayQueueListener;
import be.deezify.models.dto.TrackDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * Represents the queue of tracks to be played, with current position tracking.
 */
public class PlayQueue implements Listenable<PlayQueueListener> {

    private final Set<PlayQueueListener> listeners = new HashSet<>();

    @Getter
    private final List<TrackDTO> tracks = new ArrayList<>();
    @Getter
    @Setter
    private int currentIndex = 0;

    /**
     * Adds a track to the end of the queue.
     */
    public void addTrack(TrackDTO track) {
        tracks.add(track);
        notifyListenersTrackAdded(track, tracks.size() - 1);
    }

    /**
     * Advances to the next track, if available.
     */
    public Optional<TrackDTO> nextTrack() {
        if (currentIndex + 1 < tracks.size()) {
            currentIndex++;
            return Optional.of(tracks.get(currentIndex));  // Return the next track
        }
        return Optional.empty();  // No next track
    }

    /**
     * Moves to the previous track, if available.
     */
    public Optional<TrackDTO> previousTrack() {
        if (currentIndex - 1 >= 0) {
            currentIndex--;
            return Optional.of(tracks.get(currentIndex));  // Return the previous track
        }
        return Optional.empty();  // No previous track
    }

    /**
     * Returns the current track in the queue.
     */
    public Optional<TrackDTO> currentTrack() {
        if (currentIndex >= 0 && currentIndex < tracks.size()) {
            return Optional.of(tracks.get(currentIndex));  // Return the current track
        }
        return Optional.empty();
    }

    /**
     * Inserts a track at a specific position in the queue.
     */
    public void insertTrackInQueueAtIndex(int index, TrackDTO track) {
        if (index >= 0 && index <= tracks.size()) {
            tracks.add(index, track);  // Insert track at the given index
            notifyListenersTrackAdded(track, index);
        } else {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
    }

    /**
     * Removes the first occurrence of a track from the queue.
     */
    public void removeTrackFromQueue(TrackDTO track) {
        int index = tracks.indexOf(track);
        if (index == -1) {
            throw new IllegalArgumentException("Track not found in the queue.");
        }
        removeTrackFromQueueAtIndex(index);
    }

    /**
     * Removes a track from a specific position in the queue.
     */
    public void removeTrackFromQueueAtIndex(int index) {
        if (index >= 0 && index < tracks.size()) {
            TrackDTO track = tracks.remove(index);
            if (currentIndex >= tracks.size()) {
                currentIndex = tracks.size() - 1;
            }
            notifyListenersTrackRemoved(track, index);
        } else {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
    }

    /**
     * Clears the entire queue.
     */
    public void clear() {
        tracks.clear();
        currentIndex = 0;
        notifyListenersQueueCleared();
    }

    public void addListener(PlayQueueListener listener) {
        listeners.add(listener);
    }

    public void removeListener(PlayQueueListener listener) {
        listeners.remove(listener);
    }

    private void notifyListenersTrackAdded(TrackDTO track, int position) {
        for (PlayQueueListener listener : listeners) {
            listener.onTrackAdded(track, position);
        }
    }

    private void notifyListenersTrackRemoved(TrackDTO track, int position) {
        for (PlayQueueListener listener : listeners) {
            listener.onTrackRemoved(track, position);
        }
    }

    private void notifyListenersQueueCleared() {
        for (PlayQueueListener listener : listeners) {
            listener.onQueueCleared();
        }
    }
}