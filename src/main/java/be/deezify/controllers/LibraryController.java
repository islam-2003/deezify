package be.deezify.controllers;

import be.deezify.listeners.TagServiceListener;
import be.deezify.listeners.UserServiceListener;
import be.deezify.models.PlayQueue;
import be.deezify.models.User;
import be.deezify.models.dto.PlaylistDTO;
import be.deezify.models.dto.TagDTO;
import be.deezify.models.dto.TrackDTO;
import be.deezify.models.dto.UserDTO;
import be.deezify.services.PlaybackService;
import be.deezify.services.PlaylistService;
import be.deezify.services.TagService;
import be.deezify.services.TrackService;
import be.deezify.utils.AlertUtils;
import be.deezify.views.LibraryView;
import lombok.Getter;
import lombok.NonNull;

import java.io.IOException;
import java.util.Set;

/**
 * Controller for managing the music library.
 * This controller handles interactions between the library view and the library model.
 */
public class LibraryController extends Controller<LibraryView> implements TagServiceListener, UserServiceListener {

    /**
     * The music library model.
     */
    private final PlayQueue playQueue;
    private final PlaybackService playbackService;
    @Getter
    private final TagService tagService;
    private final TrackService trackService;
    private final PlaylistService playlistService;
    private UserDTO activeUser = User.GUEST_USER;

    @Getter
    private final MetaController metaController;

    /**
     * Constructor for the LibraryController class.
     *
     * @param metaController  The associated MetaController.
     * @param playQueue       The playback queue.
     * @param playbackService The playback service.
     * @throws Exception If an error occurs during initialization.
     */
    public LibraryController(MetaController metaController,
                             PlayQueue playQueue,
                             PlaybackService playbackService,
                             TagService tagService,
                             TrackService trackService,
                             PlaylistService playlistService) throws Exception {
        super(metaController, new LibraryView());
        this.playQueue = playQueue;
        this.playbackService = playbackService;
        this.tagService = tagService;
        this.metaController = metaController;
        this.trackService = trackService;
        this.playlistService = playlistService;
        this.view.setController(this);
    }

    /**
     * Method called when the library refresh button is clicked.
     * Reloads the music from the folder to make them visible in the library.
     */
    public void refreshButtonClicked() {
        trackService.reloadTracks();
        view.updateListView();
    }

    /**
     * Plays a track by selecting it from the library.
     *
     * @param track The track to play.
     */
    public void playTrack(TrackDTO track) {
        playQueue.clear(); // Clear the current queue
        playQueue.addTrack(track); // Add the track to the queue
        playQueue.currentTrack().ifPresent(playbackService::playTrack); // Play the current track
    }

    /**
     * Adds a track to the queue without playing it.
     *
     * @param track The track to enqueue.
     */
    public void addTrackToQueue(TrackDTO track) {
        playQueue.addTrack(track);
    }

    public Set<TrackDTO> getTrackList() {
        return trackService.getAllForUser(activeUser);
    }

    public Set<TrackDTO> searchTracks(String searchKey) {
        return trackService.getFilteredAllTracksForUser(activeUser, searchKey);
    }

    public void removeTagFromTrack(TrackDTO track, TagDTO tag) throws IOException {
        trackService.removeTagFromTrack(track, tag);
    }

    public void addTagToTrack(TrackDTO track, TagDTO tag) throws IOException {
        trackService.addTagToTrack(track, tag);
    }

    @Override
    public void onTagRemoved(TagDTO tag) {
        try {
            trackService.removeTagFromAllTrack(tag);
        } catch (IOException e) {
            AlertUtils.showError("error.title.generic", "error.text.generic");
        }
        view.updateListView();
    }

    @Override
    public void onTagChanged(TagDTO tag, TagService.TagProperty tagProperty) {
        view.updateListView();
    }

    public void addTrackToPlaylist(@NonNull TrackDTO track, @NonNull PlaylistDTO playlist) throws IOException {
        playlistService.addTrackToPlaylist(playlist, track);
    }

    public void addTrackToFav(TrackDTO track) throws IOException {
        playlistService.addTrackToFavoriteForUser(activeUser, track);
    }

    public Set<PlaylistDTO> getPlaylists() {
        return playlistService.getAllForUser(activeUser);
    }

    @Override
    public void onActiveUserChanged(UserDTO user) {
        activeUser = user;
        view.updateListView();
    }

    public void updateLanguage() {
        view.updateLanguage();
    }

}
