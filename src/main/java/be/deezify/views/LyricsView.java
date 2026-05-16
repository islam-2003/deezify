package be.deezify.views;

import be.deezify.controllers.LyricsController;
import be.deezify.controllers.MetaController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import lombok.Setter;

/**
 * View for displaying and managing lyrics.
 */
public class LyricsView extends View {

    /**
     * TextArea for displaying lyrics.
     */
    @FXML
    private TextArea lyricsTextArea;

    /**
     * Button to close the lyrics view.
     */
    @FXML
    private Button closeButton;

    /**
     * The controller associated with this view.
     */
    @Setter
    private LyricsController controller;

    /**
     * Constructor for LyricsView.
     *
     * @throws Exception If an error occurs while loading the FXML file.
     */
    public LyricsView() throws Exception {
        super("/fxml/Lyrics.fxml");
    }

    /**
     * Initializes the view and sets up event handlers.
     */
    @FXML
    public void initialize() {
        closeButton.setOnAction(event -> handleCloseLyrics());
    }

    /**
     * Handles the action of closing the lyrics view.
     */
    private void handleCloseLyrics() {
        if (controller != null) {
            controller.handleClose();
        }
    }

    /**
     * Sets the lyrics to display in the TextArea.
     *
     * @param lyrics The lyrics to display.
     */
    public void setLyrics(String lyrics) {
        if (lyricsTextArea != null) {
            lyricsTextArea.setText(lyrics);
        }
    }

    /**
     * Returns the title of the view.
     *
     * @return The title of the view.
     */
    @Override
    protected String getTitle() {
        return MetaController.getResourceBundle().getString("lyrics.lyrics");
    }

    @Override
    public void updateLanguage() {}
}