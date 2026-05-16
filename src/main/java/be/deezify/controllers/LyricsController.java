package be.deezify.controllers;

import be.deezify.listeners.PlaybackServiceListener;
import be.deezify.models.dto.TrackDTO;
import be.deezify.services.PlaybackService;
import be.deezify.utils.AlertUtils;
import be.deezify.views.LyricsView;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import java.io.IOException;

/**
 * Controller for managing lyrics display and import functionality.
 */
public class LyricsController extends Controller<LyricsView> implements PlaybackServiceListener {

    private final PlaybackService playbackService;

    /**
     * Constructor for LyricsController.
     *
     * @param metaController The main application controller.
     * @throws IOException If an error occurs while loading the view.
     */
    public LyricsController(MetaController metaController, PlaybackService playbackService) throws Exception {
        super(metaController, new LyricsView());
        this.playbackService = playbackService;
        this.view.setController(this); // Link the controller to the view
    }

    /**
     * Displays the lyrics in the right pane of the main view.
     */
    public void showLyricsInRightPane() {
        if (playbackService.getStatus() != PlaybackService.Status.NULL) {
            metaController.getMainView().setRight(this.getView());
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Aucune musique n'est actuellement en lecture.");
            alert.showAndWait();
        }
    }

    /**
     * Handles the close button action.
     */
    @FXML
    public void handleClose() {
        metaController.getMainView().clearRight();
    }

    @Override
    public void onTrackUpdate(TrackDTO newTrack) {
        try {
            newTrack.getLyrics().ifPresentOrElse(lyrics -> view.setLyrics(lyrics.getFullLyrics()), () -> {
                view.setLyrics("Aucune parole n'est disponible pour ce morceau.");
            });
        } catch (IOException exception) {
            AlertUtils.showError("Error", "Error while loading Lyrics");
        }
    }

}