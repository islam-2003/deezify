package be.deezify.controllers;

import be.deezify.models.dto.TrackDTO;
import be.deezify.services.TrackService;
import be.deezify.services.UserService;
import be.deezify.views.MetadataView;
import lombok.Getter;

import java.io.IOException;
import java.util.Set;

/**
 * Controller for managing the metadata of tracks.
 * This controller handles interactions between the metadata view and the track data.
 */
public class MetadataController extends Controller<MetadataView> {

    @Getter
    private final MetaController metaController;
    private final TrackService trackService;
    private final UserService userService;

    /**
     * Constructs a MetadataController with given MetaController and TrackService.
     *
     * @param metaController MetaController for broader context.
     * @param trackService   TrackService to interact with track data.
     * @throws Exception If the view fails to initialize.
     */
    public MetadataController(MetaController metaController, TrackService trackService, UserService userService) throws Exception {
        super(metaController, new MetadataView());
        this.view.setController(this);
        this.metaController = metaController;
        this.trackService = trackService;
        this.userService = userService;
        view.updateTrackList(trackService.getAllForUser(userService.getActiveUser()));
    }

    /**
     * Retrieves all available tracks.
     *
     * @return A set of TrackDTOs.
     */
    public Set<TrackDTO> getTracks() {
        return trackService.getAllForUser(userService.getActiveUser());
    }

    /**
     * Saves the updated metadata for a given track. Calls the TrackService which will
     * notify the listeners that the metadata of the track has changed.
     *
     * @param track  The target track.
     * @param name   New track name.
     * @param artist New artist name.
     * @param album  New album name.
     */
    public void saveMetadata(TrackDTO track, String name, String artist, String album) throws IOException {
        trackService.changeName(track, name);
        trackService.changeArtist(track, artist);
        trackService.changeAlbum(track, album);
    }

    public void updateLanguage() {
        view.updateLanguage();
    }
}