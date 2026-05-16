package be.deezify.views;

import be.deezify.controllers.MetaController;
import be.deezify.controllers.MetadataController;
import be.deezify.models.dto.TrackDTO;
import be.deezify.utils.AlertUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import lombok.Setter;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.ResourceBundle;

/**
 * View for managing the metadata menu.
 * This class updates the controller when user input is provided.
 */
public class MetadataView extends View {

    private ResourceBundle bundle;

    @FXML
    private Button confirm;
    @FXML
    public ComboBox<TrackDTO> fileNameComboBox;
    @FXML
    private TextField name;
    @FXML
    private TextField artist;
    @FXML
    private TextField album;

    @Setter
    private MetadataController controller;

    /**
     * Constructor for the metadata view.
     * Initializes the UI and binds auto-completion fields.
     *
     * @throws Exception If the FXML file can't be loaded.
     */
    public MetadataView() throws Exception {
        super("/fxml/Metadata.fxml");

        TextFields.bindAutoCompletion(this.artist, this.getAuthorsList());
        TextFields.bindAutoCompletion(this.album, this.getAlbumsList());

        this.confirm.setOnAction((e) -> this.onConfirmClicked());
        setupComboBox();
    }

    /**
     * Configures how track entries appear in the combo box.
     */
    private void setupComboBox() {
        fileNameComboBox.setCellFactory(trackDTOListView -> getTrackCell());
        fileNameComboBox.setButtonCell(getTrackCell());
    }

    /**
     * Updates the list of tracks shown in the combo box.
     *
     * @param tracks The list of tracks to show.
     */
    public void updateTrackList(Collection<TrackDTO> tracks) {
        clearTrackList();
        addTracksToList(tracks);
    }

    private void clearTrackList() {
        fileNameComboBox.getItems().clear();
    }

    private void addTracksToList(Collection<TrackDTO> tracks) {
        fileNameComboBox.getItems().addAll(tracks);
    }

    /**
     * Called when the confirm button is clicked.
     * Sends updated metadata to the controller and clears input fields.
     */
    public void onConfirmClicked() {
        if (controller != null) {
            try {
                controller.saveMetadata(
                        fileNameComboBox.getValue(),
                        name.getText(),
                        artist.getText(),
                        album.getText()
                );
            } catch (IOException e) {
                AlertUtils.showError("error.title.generic", "error.text.generic");
            }

            name.clear();
            artist.clear();
            album.clear();
        }
    }

    /**
     * Provides a list of known authors (for auto-completion).
     * TODO: Move to controller logic.
     *
     * @return Set of authors.
     */
    private HashSet<String> getAuthorsList() {
        HashSet<String> artistSet = new HashSet<>();
        return artistSet;
    }

    /**
     * Provides a list of known albums (for auto-completion).
     * TODO: Move to controller logic.
     *
     * @return Set of albums.
     */
    private HashSet<String> getAlbumsList() {
        HashSet<String> albumSet = new HashSet<>();
        // TODO
        return albumSet;
    }

    /**
     * Sets localized UI text elements based on the active bundle.
     */
    private void configureUITexts() {
        confirm.setText(bundle.getString("confirm.button"));
        name.setPromptText(bundle.getString("name.prompt"));
        artist.setPromptText(bundle.getString("artist.name"));
    }

    @Override
    public void updateLanguage() {
        this.bundle = MetaController.getResourceBundle();
        configureUITexts();
    }

    /**
     * Creates a custom cell for rendering TrackDTOs in the combo box.
     *
     * @return The configured list cell.
     */
    private ListCell<TrackDTO> getTrackCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(TrackDTO trackDTO, boolean empty) {
                super.updateItem(trackDTO, empty);
                if (empty || trackDTO == null) {
                    setText(null);
                } else {
                    setText(trackDTO.getName() + " - " + trackDTO.getArtist());
                }
            }
        };
    }

    @Override
    protected String getTitle() {
        return "";
    }
}