package be.deezify.views.global;

import be.deezify.controllers.MetaController;
import be.deezify.views.View;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Main view of the application.
 * This class manages the display of the main view and navigation between different scenes.
 */
public class MainView extends View {

    @FXML
    private BorderPane mainContainer;
    @FXML
    private VBox topContainer;
    @FXML
    private Button libraryButton;
    @FXML
    private Button playlistButton;
    @FXML
    private Button playQueueButton;
    @FXML
    private Button metadataButton;
    @FXML
    private Button tagButton;
    @FXML
    private Button karaokeButton;
    @FXML
    private Button visualizerButton;
    @FXML
    private Button signUpButton;


    /**
     * The MetaController associated with this view.
     */
    private MetaController metaController;

    private ResourceBundle bundle;

    /**
     * Constructor of the MainView class.
     *
     * @throws IOException If an error occurs while loading the FXML file.
     */
    public MainView() throws IOException {
        super("/fxml/MainView.fxml");
    }

    /**
     * Sets the MetaController associated with this view.
     *
     * @param metaController The MetaController to associate.
     */
    public void setMetaController(MetaController metaController) {
        this.metaController = metaController;

        if (libraryButton != null)
            libraryButton.setOnAction(actionEvent -> switchToScene(MetaController.Scenes.LIBRARY));
        if (playlistButton != null)
            playlistButton.setOnAction(actionEvent -> switchToScene(MetaController.Scenes.PLAYLIST));
        if (playQueueButton != null)
            playQueueButton.setOnAction(actionEvent -> switchToScene(MetaController.Scenes.PLAY_QUEUE));
        if (metadataButton != null)
            metadataButton.setOnAction(actionEvent -> switchToScene(MetaController.Scenes.METADATA));
        if (tagButton != null)
            tagButton.setOnAction(actionEvent -> switchToScene(MetaController.Scenes.TAG));
        if (karaokeButton != null)
            karaokeButton.setOnAction(actionEvent -> switchToScene(MetaController.Scenes.KARAOKE));
        if (visualizerButton != null)
            visualizerButton.setOnAction(actionEvent -> switchToScene(MetaController.Scenes.VISUALIZER));
        if (signUpButton != null)
            signUpButton.setOnAction(actionEvent -> switchToScene(MetaController.Scenes.SIGNUP));

    }

    /**
     * Returns the title of the view.
     *
     * @return The title of the view.
     */
    @Override
    protected String getTitle() {
        return "Deezify - Music Player";
    }

    /**
     * Displays the view in a window.
     *
     * @param stage The window in which to display the view.
     */
    public void show(Stage stage) {
        stage.setScene(this.scene);
        stage.setTitle(this.getTitle());
        stage.show();
    }

    private void switchToScene(MetaController.Scenes scene) {
        if (metaController != null) {
            metaController.switchScene(scene);
        }
    }

    /**
     * Sets the central content of the view.
     *
     * @param pane The content to display in the center.
     */
    public void setCenter(Pane pane) {
        if (mainContainer != null) {
            mainContainer.setCenter(pane);
        }
    }

    /**
     * Used to set the bottom of the main view (i.e. the Control Bar)
     *
     * @param view
     */
    public void setBottom(View view) {
        mainContainer.setBottom(view.getRoot());
    }

    /**
     * Sets the content to display on the top of the main view.
     *
     * @param node The content to display at the top.
     */
    public void setTop(Node node) {
        if (mainContainer != null) {
            mainContainer.setTop(node);
        }
    }

    /**
     * Sets the content to display at the top of the main view. (i.e. the Language Menu)
     *
     * @param view The content to display at the top.
     */
    public void setTop(View view) {
        topContainer.getChildren().add(0, view.getRoot());
    }

    /**
     * Sets the content to display on the right side of the main view.
     *
     * @param view The content to display on the right.
     */
    public void setRight(View view) {
        if (mainContainer != null) {
            mainContainer.setRight(view.getRoot());
        }
    }


    /**
     * Clears the content on the right side of the main view.
     */
    public void clearRight() {
        if (mainContainer != null) {
            mainContainer.setRight(null);
        }
    }

    private void configureUITexts() {
        libraryButton.setText(bundle.getString("library.title"));
        playlistButton.setText(bundle.getString("playlist.button"));
        playQueueButton.setText(bundle.getString("queue.title"));
        karaokeButton.setText(bundle.getString("karaoke.title"));
        signUpButton.setText(bundle.getString("signup.title"));
    }

    public void updateLanguage() {
        this.bundle = MetaController.getResourceBundle();
        configureUITexts();
    }
}
