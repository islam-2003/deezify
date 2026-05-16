package be.deezify.models;

import be.deezify.listeners.UserServiceListener;
import be.deezify.models.dto.TagDTO;
import be.deezify.models.dto.TrackDTO;
import be.deezify.models.dto.UserDTO;
import be.deezify.services.TrackService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.management.relation.Role;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class responsible for generating track recommendations based on similarity.
 */
@RequiredArgsConstructor
public class TrackRecommender implements UserServiceListener {

    // Weights assigned to different types of similarity
    private static final int TAG_MATCH_WEIGHT = 3;
    private static final int ARTIST_MATCH_WEIGHT = 2;
    private static final int ALBUM_MATCH_WEIGHT = 1;

    private final TrackService trackService;
    private UserDTO activeUser = User.GUEST_USER;

    @Override
    public void onActiveUserChanged(UserDTO user) {
        activeUser = user;
    }

    /**
     * Returns a list of suggested tracks ranked by similarity to the reference track.
     *
     * @param referenceTrack The track used as a reference for similarity.
     * @return Sorted list of similar tracks.
     */
    public List<TrackDTO> getSuggestions(TrackDTO referenceTrack) {
        Map<TrackDTO, Integer> scores = new HashMap<>();

        List<TrackDTO> candidates = trackService.getAllForUser(activeUser).stream()
                .filter(candidate -> !candidate.equals(referenceTrack))
                .toList();

        for (TrackDTO candidate : candidates) { // loop through each track to calculate the similarities between the reference track and the candidate track
            if (candidate.equals(referenceTrack)) continue;

            int score = 0;

            /**
             * Increment the score based on the similarities between the referenced track and the current candidate.
             * Then puts the score per candidate in a hashmap.
             */

            score += intersectionSize(referenceTrack.getTags(), candidate.getTags()) * TAG_MATCH_WEIGHT;

            if (referenceTrack.getAlbum().isPresent() && candidate.getAlbum().isPresent()) {
                if (same(referenceTrack.getAlbum().get(), candidate.getAlbum().get())) {
                    score += ALBUM_MATCH_WEIGHT;
                }
            }

            if (same(referenceTrack.getArtist(), candidate.getArtist())) score += ARTIST_MATCH_WEIGHT;

            if (score > 0) {
                scores.put(candidate, score);
            }
        }

        return scores.entrySet().stream().sorted(Map.Entry.<TrackDTO, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey).collect(Collectors.toList());
    }

    private int intersectionSize(Set<TagDTO> a, Set<TagDTO> b) { // return the number of tags similar between a and b
        Set<TagDTO> intersection = new HashSet<>(a);
        intersection.retainAll(b);
        return intersection.size();
    }

    private boolean same(String a, String b) { // return true if the two strings are the same
        if (a == null || b == null) return false;
        return a.trim().equalsIgnoreCase(b.trim());
    }
}
