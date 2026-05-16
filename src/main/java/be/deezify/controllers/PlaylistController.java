package be.deezify.controllers;

import be.deezify.exceptions.ForbiddenActionException;
import be.deezify.listeners.PlaylistServiceListener;
import be.deezify.listeners.UserServiceListener;
import be.deezify.models.PlayQueue;
import be.deezify.models.Playlist;
import be.deezify.models.TrackRecommender;
import be.deezify.models.User;
import be.deezify.models.dto.PlaylistDTO;
import be.deezify.models.dto.TrackDTO;
import be.deezify.models.dto.UserDTO;
import be.deezify.services.PlaybackService;
import be.deezify.services.PlaylistService;
import be.deezify.services.TrackService;
import be.deezify.utils.AlertUtils;
import be.deezify.views.PlaylistView;
import lombok.Getter;
import lombok.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Contrôleur pour la gestion des playlists.
 * Ce contrôleur gère les interactions entre la vue des playlists et le modèle.
 */
public class PlaylistController extends Controller<PlaylistView> implements UserServiceListener, PlaylistServiceListener {

    /**
     * Le gestionnaire des playlists.
     */
    private PlaylistService playlistService;
    private final PlayQueue playQueue;
    private final PlaybackService playbackService;
    private TrackRecommender trackRecommender;
    private UserDTO activeUser = User.GUEST_USER;

    @Getter
    private final MetaController metaController;

    /**
     * Constructeur de la classe PlaylistController.
     *
     * @param metaController Le MetaController associé.
     * @param view           La vue associée à ce contrôleur.
     */
    public PlaylistController(MetaController metaController,
                              PlayQueue playQueue,
                              PlaybackService playbackService,
                              PlaylistView view,
                              PlaylistService playlistManager,
                              TrackService trackService) {
        super(metaController, view);
        this.playlistService = playlistManager;
        this.playQueue = playQueue;
        this.playbackService = playbackService;
        this.view.setController(this);
        this.metaController = metaController;
        this.trackRecommender = new TrackRecommender(trackService);
        updatePlaylistView();
    }

    @Override
    public void onActiveUserChanged(UserDTO user) {
        activeUser = user;
        updatePlaylistView();
    }

    /**
     * Ajoute une nouvelle playlist.
     *
     * @param playlistName Le nom de la playlist à ajouter.
     */
    public void addPlaylist(@NonNull String playlistName) throws IOException {
        if (playlistName.trim().isEmpty()) {
            AlertUtils.showError("error.title.generic", "error.text.generic");
            return;
        }

        playlistService.save(new Playlist(playlistName));
    }

    public void removePlaylist(@NonNull PlaylistDTO playlist) throws IOException, ForbiddenActionException {
        playlistService.delete(playlist);
        updatePlaylistView();
    }

    public void addTrackToPlaylist(PlaylistDTO playlistDTO, TrackDTO trackDTO) throws IOException {
        playlistService.addTrackToPlaylist(playlistDTO, trackDTO);
    }


    public void updatePlaylistView() {
        view.updatePlaylistList();
    }

    /*
     * Loops through each track of the library and finds the best recommendations based on all the track in the given
     * playlist.
     */
    public void showRecommendationsForPlaylist(@NonNull PlaylistDTO playlist) {
        List<TrackDTO> tracks = playlist.getTracks();
        Set<TrackDTO> recommendations = new HashSet<>();

        for (TrackDTO track : tracks) {
            List<TrackDTO> suggested = trackRecommender.getSuggestions(track);
            for (TrackDTO t : suggested) {
                if (!tracks.contains(t)) {
                    recommendations.add(t);
                }
            }
        }

        view.showRecommendationsPopup(new ArrayList<>(recommendations), playlist);
    }

    public Set<PlaylistDTO> getPlaylists() {
        return playlistService.getAllForUser(activeUser);
    }

    public void updateLanguage() {
        view.updateLanguage();
    }

    public void editPlaylistName(PlaylistDTO playlist, String playlistNewName) throws IOException {
        playlistService.changeName(playlist, playlistNewName);
    }

    /**
     * Starts playing a playlist beginning from a selected track.
     * The tracks are reordered to start from the clicked one.
     *
     * @param playlist   The playlist to play.
     * @param startTrack The track to start playback from.
     */
    public void playPlaylistFromTrack(PlaylistDTO playlist, TrackDTO startTrack) {
        playQueue.clear();

        List<TrackDTO> tracks = playlist.getTracks();
        int startIndex = tracks.indexOf(startTrack);
        if (startIndex == -1) return;

        List<TrackDTO> reordered = new ArrayList<>();
        reordered.addAll(tracks.subList(startIndex, tracks.size())); // from clicked track to end
        reordered.addAll(tracks.subList(0, startIndex)); // from beginning to just before clicked track

        reordered.forEach(playQueue::addTrack);
        playQueue.currentTrack().ifPresent(playbackService::playTrack);
    }

    @Override
    public void onPlaylistAdded(PlaylistDTO playlistDTO) {
        updatePlaylistView();
    }
}
