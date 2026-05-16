package be.deezify.views;

import be.deezify.controllers.MetaController;
import be.deezify.controllers.PlayQueueController;
import be.deezify.models.dto.TrackDTO;
import be.deezify.utils.AlertUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import lombok.Setter;

import java.io.IOException;
import java.util.Collection;
import java.util.ResourceBundle;

public class PlayQueueView extends View {

    @Setter
    private PlayQueueController playQueueController;

    private ResourceBundle bundle;

    @FXML
    private Label playQueueLabel;
    @FXML
    private Button clearButton;
    @FXML
    private ListView<TrackDTO> playQueueListView;

    public PlayQueueView() throws IOException {
        super("/fxml/playqueue/PlayQueue.fxml");

        playQueueListView.setCellFactory(trackListView -> new TrackListCell());
        clearButton.setOnAction(actionEvent -> playQueueController.clearQueue());
    }


    /**
     * Updates the play queue list with a new collection of tracks.
     *
     * @param tracks the collection of tracks to display
     */
    public void updateTrackList(Collection<TrackDTO> tracks) {
        clearTrackList();
        addTrackToList(tracks);
    }

    /**
     * Clears all tracks from the play queue list.
     */
    public void clearTrackList() {
        playQueueListView.getItems().clear();
    }

    /**
     * Adds a collection of tracks to the play queue list view.
     *
     * @param tracks the tracks to be added
     */
    private void addTrackToList(Collection<TrackDTO> tracks) {
        playQueueListView.getItems().addAll(tracks);
    }

    /**
     * Returns the title of the view window.
     *
     * @return the title "Play Queue"
     */
    @Override
    protected String getTitle() {
        return "Play Queue";
    }

    /**
     * Updates all UI texts (labels, buttons) with the correct
     * translations from the current resource bundle.
     */
    private void configureUITexts() {
        playQueueLabel.setText(bundle.getString("queue.title"));
        clearButton.setText(bundle.getString("clear.button"));
    }

    /**
     * Reloads the language bundle using the current locale from the controller,
     * and updates all UI texts accordingly.
     */
    @Override
    public void updateLanguage() {
        this.bundle = MetaController.getResourceBundle();
        configureUITexts();
    }

    /**
     * Custom cell used to display a track in the play queue list.
     * Shows the track's name and artist, along with a remove button.
     */
    private class TrackListCell extends ListCell<TrackDTO> {
        @Setter
        protected HBox hBox;
        @FXML
        protected Label trackLabel;
        @FXML
        protected Button button;

        public TrackListCell() {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/playqueue/PlayQueueTrackListCell.fxml"));
                loader.setController(this);
                hBox = loader.load();

                button.setOnAction(this::onButtonClick);
            } catch (IOException e) {
                AlertUtils.showError("error.title.generic", "error.text.generic");
            }
        }

        /**
         * Handles the remove button click.
         * Removes the track from both the list view and the controller's queue.
         *
         * @param event the action event triggered by the button click
         */
        private void onButtonClick(ActionEvent event) {
            playQueueListView.getItems().remove(getIndex());
            playQueueController.removeTrackFromQueueAtIndex(getIndex());
        }

        /**
         * Updates the content of the cell to show the track’s name and artist.
         * If the cell is empty, no content is displayed.
         *
         * @param track the track to display
         * @param empty whether this cell is empty
         */
        @Override
        protected void updateItem(TrackDTO track, boolean empty) {
            super.updateItem(track, empty);

            if (empty || track == null) {
                setGraphic(null);
            } else {
                trackLabel.setText(track.getName() + " - " + track.getArtist());
                setGraphic(hBox);
            }
        }
    }
}
