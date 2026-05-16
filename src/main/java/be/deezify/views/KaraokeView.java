package be.deezify.views;

import be.deezify.controllers.KaraokeController;
import be.deezify.controllers.MetaController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import lombok.Setter;

import java.util.ResourceBundle;

/**
 * View for managing the karaoke window.
 * This class handles the display of the karaoke lyrics.
 */
public class KaraokeView extends View {

    public enum KaraokeState {
        WAITING,
        EMPTY,
        INCOMPATIBLE,
        ERROR
    }
    /**
     * FXML elements for the view.
     */
    @FXML
    private Label titleLabel;
    @FXML
    private Label karaokeLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private TextFlow lyricsTextFlow;

    /**
     * The controller associated with this view.
     */
    @Setter
    private KaraokeController controller;

    private ResourceBundle bundle;

    /**
     * Constructor for the KaraokeView class.
     *
     * @throws Exception If an error occurs during initialization.
     */
    public KaraokeView() throws Exception {
        super("/fxml/Karaoke.fxml");
    }

    /**
     * Initializes the resource bundle based on the controller's current locale.
     * This allows dynamic internationalization of the view.
     */
    public void initializeBundle() {
        this.bundle = MetaController.getResourceBundle();
    }

    /**
     * Sets the status message based on a predefined numeric code.
     * Also clears the displayed lyrics.
     *
     * @param state An integer code representing the current karaoke state.
     */
    public void setStatus(KaraokeState state) {
        String message = "";
        switch (state) {
            case WAITING:
                message = bundle.getString("karaoke.waiting");
                break;
            case EMPTY:
                message = bundle.getString("karaoke.empty");
                break;
            case INCOMPATIBLE:
                message = bundle.getString("karaoke.incompatible");
                break;
            case ERROR:
                message = bundle.getString("karaoke.error");
                break;
        }
        lyricsTextFlow.getChildren().clear();
        statusLabel.setText(message);
    }

    /**
     * Sets the status message based on a filename.
     * Used when lyrics are successfully loaded.
     *
     * @param fileName The name of the lyrics file currently displayed.
     */
    public void setStatus(String fileName) {
        lyricsTextFlow.getChildren().clear();
        statusLabel.setText(bundle.getString("karaoke.title") + " : " + fileName);
    }

    /**
     * Updates the displayed lyrics with the given line and highlights the current word.
     *
     * @param fullLine    The full line of lyrics to display.
     * @param currentWord The word to highlight in the line. If null or empty, the whole line is highlighted.
     */
    public void updateLyrics(String fullLine, String currentWord) {
        lyricsTextFlow.getChildren().clear();

        if (fullLine == null || fullLine.isEmpty()) return;

        // Split the full line into individual words using one or more whitespace characters (space, tab, newline, etc.)
        // The regex "\\s+" ensures it works even if there are multiple or mixed whitespace characters between words
        String[] words = fullLine.split("\\s+");
        boolean highlightEntireLine = currentWord == null || currentWord.isEmpty();

        for (String w : words) {
            Text text = new Text(w + " ");

            // If the entire line should be highlighted
            if (highlightEntireLine) {
                text.setStyle("-fx-fill: gold; -fx-font-weight: bold;");
                // Check if the current word in the line matches the currently sung word,
                // This allows matching even if the lyrics text and the timestamped word differ in capitalization
            } else if (w.equalsIgnoreCase(currentWord)) {
                text.setStyle("-fx-fill: gold; -fx-font-weight: bold;");
            } else {
                text.setStyle("-fx-fill: gray;");
            }

            lyricsTextFlow.getChildren().add(text);
        }
    }


    @Override
    protected String getTitle() {
        return "Karaoke";
    }

    /**
     * Updates the UI labels based on the current locale bundle.
     * Called when the application language is changed.
     */
    private void configureUITexts() {
        karaokeLabel.setText(bundle.getString("karaoke.title"));
    }

    /**
     * Updates all language-dependent texts in the view.
     * Should be called after changing the application locale.
     */
    public void updateLanguage() {
        configureUITexts();
    }
}
