package be.deezify.views;

import be.deezify.controllers.MetaController;
import be.deezify.controllers.PlaylistController;
import be.deezify.exceptions.ForbiddenActionException;
import be.deezify.models.dto.PlaylistDTO;
import be.deezify.models.dto.TrackDTO;
import be.deezify.utils.AlertUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Setter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * View for managing user playlists.
 * Handles displaying playlists and user interactions such as creating, editing,
 * and deleting playlists or tracks within them.
 */
public class PlaylistView extends View {

    private PlaylistController controller;

    private ResourceBundle bundle;

    @Setter
    @FXML
    private ListView<PlaylistDTO> playlistNamesListView;

    @FXML
    private Button addPlaylistButton;

    @FXML
    private Button removePlaylistButton;

    @FXML
    private TextField playlistNameField;

    public PlaylistView() throws IOException {
        super("/fxml/Playlist.fxml");
    }

    public void setController(PlaylistController controller) {
        this.controller = controller;
        // Setting the action on button
        addPlaylistButton.setOnAction(event -> onAddPlaylist());
        removePlaylistButton.setOnAction(event -> onRemovePlaylist());
        playlistNamesListView.setCellFactory(playlistDTOListView -> new PlaylistCell());
    }

    /**
     * Updates the playlist
     */
    public void updatePlaylistList() {
        playlistNamesListView.getItems().clear();
        playlistNamesListView.getItems().addAll(controller.getPlaylists());
    }

    /**
     * Add a playlist with the name entered in the "playlistNameField" textbox
     */
    @FXML
    private void onAddPlaylist() {
        String playlistName = playlistNameField.getText().trim();
        if (!playlistName.isEmpty()) {
            try {
                controller.addPlaylist(playlistName);
            } catch (IOException e) {
                AlertUtils.showError("error.title.generic", "error.text.generic");
            }
            playlistNameField.clear();
            updatePlaylistList();
        }
    }

    /**
     * Removes a playlist
     */
    @FXML
    private void onRemovePlaylist() {
        PlaylistDTO selectedPlaylist = playlistNamesListView.getSelectionModel().getSelectedItem();
        if (selectedPlaylist != null) {
            try {
                controller.removePlaylist(selectedPlaylist);
            } catch (IOException | ForbiddenActionException e) {
                AlertUtils.showError("error.title.generic", "error.text.generic");
            }
            updatePlaylistList();
        }
    }

    /**
     * Called when a user's playlists have changed (e.g., switched user).
     *
     * @param username the user's username
     */
    public void onUserPlaylistsChanged(String username) {
        if (controller != null) {
            // TODO controller.changeUserPlaylists(username);
            updatePlaylistList();
        }
    }
    /**
     * Called when a user's playlists are deleted (e.g., logging out).
     */
    public void onUserPlaylistsDeleted() {
        if (controller != null) {
            // TODO controller.goBackToGuestPlaylist();
            updatePlaylistList();
        }
    }

    @Override
    protected String getTitle() {
        return "Playlists";
    }

    /**
     * Opens a dialog to show track recommendations for a playlist.
     * Each suggested track has a button to add it to the playlist.
     *
     * @param recommendations the list of recommended tracks
     * @param playlist the target playlist
     */
    public void showRecommendationsPopup(List<TrackDTO> recommendations, PlaylistDTO playlist) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle(bundle.getString("recommendations.title"));
        dialog.setHeaderText(bundle.getString("suggestions.text") + " : " + playlist.getName());
        VBox content = new VBox(10);
        content.setStyle("-fx-padding: 10;");

        for (TrackDTO track : recommendations) {
            Label trackLabel = new Label(track.getName() + " - " + track.getArtist());

            Button addButton = new Button(bundle.getString("add.button"));
            addButton.setOnAction(e -> {
                try {
                    controller.addTrackToPlaylist(playlist, track);
                } catch (IOException ex) {
                    AlertUtils.showError("error.title.generic", "error.text.generic");
                }
                addButton.setDisable(true); // empêche les ajouts multiples
            });

            HBox hbox = new HBox(10, trackLabel, addButton);
            content.getChildren().add(hbox);
        }

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        dialog.getDialogPane().setContent(scrollPane);

        ButtonType closeButton = new ButtonType(bundle.getString("close.button"), ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(closeButton);

        dialog.showAndWait();
    }

    /**
     * Opens a dialog displaying the selected playlist's tracks and options to edit.
     *
     * @param playlist the playlist to display
     */
    public void openPlaylistDialog(PlaylistDTO playlist) {
        //TODO UPDATE PLAYLIST LISTS VIEW HERE

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle(bundle.getString("playlist.button") + " : " + playlist.getName());

        VBox content = new VBox(10);
        content.setStyle("-fx-padding: 10;");

        HBox header = new HBox(10);
        Label playlistLabel = new Label(bundle.getString("playlist.button") + " : " + playlist.getName());
        header.getChildren().addAll(playlistLabel);

        Button editButton = new Button(bundle.getString("edit.button"));
        editButton.setOnAction(e -> editPlaylistDialog(dialog, playlist)); // Attach the action here
        header.getChildren().add(editButton);

        ListView<TrackDTO> trackListView = new ListView<>();
        trackListView.setCellFactory(trackList -> new TrackListCell(playlist));
        trackListView.getItems().addAll(playlist.getTracks());

        ButtonType closeButton = new ButtonType(bundle.getString("close.button"), ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(closeButton);
        content.getChildren().addAll(header, trackListView);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        dialog.getDialogPane().setContent(scrollPane);

        dialog.showAndWait();
    }

    /**
     * Opens a dialog allowing the user to rename the playlist.
     *
     * @param parentDialog the parent dialog to close after rename
     * @param playlist the playlist being edited
     */
    public void editPlaylistDialog(Dialog<Void> parentDialog, PlaylistDTO playlist) {
        Dialog<String> editDialog = new Dialog<>();
        editDialog.setTitle(bundle.getString("edit.playlist.title"));

        TextField newNameField = new TextField();

        VBox editContent = new VBox(10);
        editContent.getChildren().addAll(new Label(bundle.getString("edit.playlist.label")), newNameField);

        editDialog.getDialogPane().setContent(editContent);

        ButtonType saveButton = new ButtonType(bundle.getString("confirm.button"), ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType(bundle.getString("cancel.button"), ButtonBar.ButtonData.CANCEL_CLOSE);
        editDialog.getDialogPane().getButtonTypes().addAll(saveButton, cancelButton);

        editDialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButton) {
                return newNameField.getText();
            }
            return null;
        });

        Optional<String> result = editDialog.showAndWait();
        result.ifPresent(newName -> {
            if (newName != null && !newName.trim().isEmpty()) {
                editPlaylistName(playlist, newName);
                parentDialog.close();
                updatePlaylistList();
                Platform.runLater(() -> openPlaylistDialog(playlist));
            }
        });
    }

    /**
     * Renames the specified playlist via the controller.
     *
     * @param playlist the playlist to rename
     * @param playlistNewName the new name
     */
    private void editPlaylistName(PlaylistDTO playlist, String playlistNewName) {
        try {
            controller.editPlaylistName(playlist, playlistNewName);
        } catch (IOException e) {
            AlertUtils.showError("error.title.generic", "error.text.generic");
        }
    }

    /**
     * Sets localized UI texts for the current language.
     */
    private void configureUITexts() {
        addPlaylistButton.setText(bundle.getString("add.button"));
        removePlaylistButton.setText(bundle.getString("delete.button"));
        playlistNameField.setPromptText(bundle.getString("playlist.prompt"));
    }

    /**
     * Updates the UI language and refreshes components.
     */
    public void updateLanguage() {
        this.bundle = MetaController.getResourceBundle();
        configureUITexts();
        playlistNamesListView.refresh();
    }

    /**
     * Custom cell renderer for playlist items.
     * Includes a label, a view button (eye icon), and a recommendation button.
     */
    private class PlaylistCell extends ListCell<PlaylistDTO> {

        @Override
        protected void updateItem(PlaylistDTO playlist, boolean empty) {
            super.updateItem(playlist, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                Label playlistLabel = new Label();
                playlistLabel.setText(playlist.getName());
                playlistLabel.getStyleClass().add("track-label");

                Image image = new Image(getClass().getResource("/icons/eye.png").toExternalForm());
                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(10);
                imageView.setPreserveRatio(true);

                Button openButton = new Button();
                openButton.setGraphic(imageView);
                openButton.getStyleClass().add("add-button");
                openButton.setOnAction(event -> openPlaylistDialog(playlist));

                Button recommendationButton = new Button(bundle.getString("recommendation.button"));
                recommendationButton.setOnAction(e -> controller.showRecommendationsForPlaylist(playlist));
                recommendationButton.getStyleClass().add("add-button");

                HBox hbox = new HBox(10, playlistLabel, openButton, recommendationButton);
                setGraphic(hbox);
            }
        }
    }

    /**
     * Custom cell renderer for tracks inside a playlist dialog.
     * Supports click-to-play functionality.
     */
    private class TrackListCell extends ListCell<TrackDTO> {
        public TrackListCell(PlaylistDTO playlist) {
            setOnMouseClicked(event -> {
                if (!isEmpty() && getItem() != null) {
                    controller.playPlaylistFromTrack(playlist, getItem());
                }
            });
        }

        @Override
        protected void updateItem(TrackDTO track, boolean empty) {
            super.updateItem(track, empty);
            if (empty || track == null) {
                setText(null);
            } else {
                setText(track.getName() + " - " + track.getArtist());
            }
        }
    }
}
