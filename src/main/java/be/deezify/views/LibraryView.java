package be.deezify.views;

import be.deezify.controllers.LibraryController;
import be.deezify.controllers.MetaController;
import be.deezify.models.dto.PlaylistDTO;
import be.deezify.models.dto.TagDTO;
import be.deezify.models.dto.TrackDTO;
import be.deezify.utils.AlertUtils;
import be.deezify.views.tags.TagElementView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * View for managing the music library.
 * This class handles the display of audio tracks and user interactions with the library.
 */
public class LibraryView extends View {


    /**
     * The controller associated with this view.
     */
    @Getter
    private LibraryController controller;

    private ResourceBundle bundle;

    /**
     * Label for the library title.
     */
    @FXML
    private Label libraryLabel;

    /**
     * ListView to display audio tracks with their artists.
     */
    @FXML
    private ListView<TrackDTO> libraryListView;
    @FXML
    private TextField filterField;
    @FXML
    private Button refreshButton;
    private final ContextMenu contextMenu = new ContextMenu();


    public LibraryView() throws Exception {
        super("/fxml/library/Library.fxml");
    }

    @FXML
    private void initialize() {
        configureRefreshButton();
        configureListView();
        configureSearchField();
    }

    public void setLibraryLabelText(String text) {
        libraryLabel.setText(text);
    }


    /**
     * Configures the refresh button action
     */
    private void configureRefreshButton() {
        refreshButton.setOnAction(event -> onRefreshButtonClicked());
    }

    /**
     * Configures the ListView for the translations
     */
    private void configureListView() {
        libraryListView.setCellFactory(trackListView -> new TrackListCell(bundle));
    }


    /**
     * Configures the search field in the track list
     */
    private void configureSearchField() {
        filterField.textProperty().addListener((observable, oldValue, newValue) -> updateListView(newValue));
    }

    public void setController(@NonNull LibraryController controller) {
        this.controller = controller;
        loadTracksIntoListView();
    }

    /**
     * Loads audio tracks into the ListView.
     */
    public void loadTracksIntoListView() {
        if (controller != null) {
            libraryListView.getItems().setAll(controller.getTrackList());
        }
    }

    public void updateListView() {
        updateListView(filterField.getText());
    }

    /**
     * Updates the list depending on the search key.
     *
     * @param searchKey the search key.
     */
    private void updateListView(String searchKey) {
        if (controller != null) {
            System.out.println("test");
            libraryListView.getItems().clear();
            List<TrackDTO> tracks = controller.searchTracks(searchKey).stream().sorted(Comparator.comparing(TrackDTO::getName)).toList();

            libraryListView.getItems().addAll(tracks);
        }
    }


    /**
     * Refresh button which reloads every track in the library
     */
    public void onRefreshButtonClicked() {
        if (controller != null) {
            controller.refreshButtonClicked();
            loadTracksIntoListView();
        }
    }

    /**
     * Returns the title of the view.
     *
     * @return The title of the view.
     */
    @Override
    protected String getTitle() {
        return "Playlist";
    }

    /**
     * Configure the text for translation
     */
    private void configureUITexts() {
        libraryLabel.setText(bundle.getString("library.title"));
        refreshButton.setText(bundle.getString("refresh.button"));
        filterField.setPromptText(bundle.getString("search.prompt"));
    }

    /**
     * Updates the application's displayed language by reloading the resource bundle
     * with the current locale from the meta controller. This method also updates
     * all UI texts and reconfigures the cell factory for the library list view
     * to use the updated localized strings.
     */
    public void updateLanguage() {
        this.bundle = MetaController.getResourceBundle();
        configureUITexts();
        libraryListView.setCellFactory(trackListView -> new TrackListCell(bundle));
    }

    /**
     * Classe interne pour gérer l'affichage et les interactions des pistes.
     */
    private class TrackListCell extends ListCell<TrackDTO> {

        @Setter
        protected HBox hBox;
        @FXML
        protected Label trackLabel;
        @FXML
        protected Button button;
        @FXML
        private FlowPane tagContainer;
        @FXML
        private Button addTagButton;
        @FXML
        private ComboBox<PlaylistDTO> playlistComboBox;
        @FXML
        private Button addToPlaylistButton;
        @FXML
        private Button addFavButton;

        private final ResourceBundle bundle;

        public TrackListCell(ResourceBundle bundle) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/library/LibraryTrackListCell.fxml"));
                loader.setController(this);
                hBox = loader.load();
                button.setOnAction(this::onButtonClick);
                hBox.setOnMouseClicked(this::onCellClick);

            } catch (IOException e) {
                e.printStackTrace();
            }
            this.bundle = bundle;
            addToPlaylistButton.setOnAction(actionEvent -> onAddToPlaylistButtonClick());
            configureUITexts();
        }

        /**
         * Handles the button click event for adding the current item (track)
         * to the playback queue.
         *
         * @param event the action event triggered by the button click
         */
        protected void onButtonClick(ActionEvent event) {
            if (controller != null && getItem() != null) {
                controller.addTrackToQueue(getItem());
            }
        }

        /**
         * Plays the track
         *
         * @param event the action event triggered by the cell click
         */
        protected void onCellClick(MouseEvent event) {
            if (controller != null && getItem() != null) {
                controller.playTrack(getItem());
            }
        }

        /**
         * Action when the user adds a track to the playlist
         */
        protected void onAddToPlaylistButtonClick() {
            if (getItem() != null && playlistComboBox.getValue() != null) {
                PlaylistDTO selectedPlaylist = playlistComboBox.getValue();
                try {
                    controller.addTrackToPlaylist(getItem(), selectedPlaylist);
                } catch (IOException e) {
                    AlertUtils.showError("error.title.generic", "error.text.generic");
                }
            }
        }

        protected void onFavButtonClick() {
            if (getItem() != null) {
                try {
                    controller.addTrackToFav(getItem());
                } catch (IOException e) {
                    AlertUtils.showError("error.title.generic", "error.text.generic");
                }
            } else {
                AlertUtils.showError("error.title.generic", "error.text.generic");
            }
        }

        @Override
        protected void updateItem(TrackDTO track, boolean empty) {
            super.updateItem(track, empty);

            if (empty || track == null) {
                setText(null);
                setGraphic(null); // <--- This is crucial
                return;
            }

            if (!empty && track != null) {
                trackLabel.setText(track.getName() + " - " + track.getArtist());
                setGraphic(hBox);
                updateTags(track);

                addFavButton.setOnAction(e -> onFavButtonClick());

                addTagButton.setOnAction(actionEvent -> {
                    contextMenu.getItems().clear();
                    contextMenu.getStyleClass().add("context-menu");
                    Set<TagDTO> tags = track.getTags();

                    for (TagDTO tag : controller.getTagService().getAll()) {
                        CheckMenuItem menuItem = new CheckMenuItem(tag.getName());
                        menuItem.getStyleClass().add("menu-item");

                        contextMenu.getItems().add(menuItem);
                        if (tags.contains(tag)) {
                            menuItem.setSelected(true);
                        }
                        menuItem.setOnAction(itemActionEvent -> {
                            try {
                                controller.addTagToTrack(track, tag);
                            } catch (IOException e) {
                                AlertUtils.showError("error.title.generic", "error.text.generic");
                            }
                            updateItem(track, false);
                        });
                    }

                    addTagButton.setOnMouseClicked(event -> {
                        if (event.getButton().toString().equals("PRIMARY")) {
                            contextMenu.show(addTagButton, event.getScreenX(), event.getScreenY());
                        }
                    });
                });

                playlistComboBox.getItems().setAll(controller.getPlaylists());

                playlistComboBox.setCellFactory(comboBox -> new ListCell<>() {
                    @Override
                    protected void updateItem(PlaylistDTO playlist, boolean empty) {
                        super.updateItem(playlist, empty);
                        if (!empty && playlist != null) {
                            setText(playlist.getName());
                        } else {
                            setText(null);
                        }
                    }
                });

                playlistComboBox.setButtonCell(new ListCell<>() {
                    @Override
                    protected void updateItem(PlaylistDTO playlist, boolean empty) {
                        super.updateItem(playlist, empty);
                        if (!empty && playlist != null) {
                            setText(playlist.getName());  // Display playlist name in the dropdown button
                        } else {
                            setText(null);
                        }
                    }
                });
            }
        }

        /**
         * Updates tag for the given track.
         */
        private void updateTags(TrackDTO track) {
            tagContainer.getChildren().clear();
            Set<TagDTO> tags = track.getTags();

            for (TagDTO tag : tags) {
                tagContainer.getChildren().add(new TagElementView(tag, () -> {
                    try {
                        controller.removeTagFromTrack(track, tag);
                    } catch (IOException e) {
                        AlertUtils.showError("error.title.generic", "error.text.generic");
                    }
                    updateItem(track, false);
                }).getRoot());
            }
        }

        private void configureUITexts() {
            playlistComboBox.setPromptText(bundle.getString("playlist.dropdown"));
        }
    }
}
