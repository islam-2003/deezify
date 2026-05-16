package be.deezify;

import be.deezify.controllers.MetaController;
import be.deezify.views.global.MainView;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The main class of the Deezify application.
 * This class extends {@link javafx.application.Application} and serves as the entry point for the JavaFX application.
 * It initializes the main view and the MetaController, then displays the first scene.
 */
public class Main extends Application {

    /**
     * The main method of the application.
     * Launches the JavaFX application.
     *
     * @param args The command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Method called at the start of the JavaFX application.
     * Initializes the main view, the MetaController, and displays the first scene.
     *
     * @param primaryStage The main stage of the application.
     * @throws Exception If an error occurs during initialization.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Create the main view
        MainView mainView = new MainView();

        // Initialize the MetaController with the main view
        MetaController meta = new MetaController(primaryStage, mainView);

        // Pass the MetaController reference to MainView
        mainView.setMetaController(meta);

        // Display the main view
        mainView.show(primaryStage);

        // Load the first scene (e.g., PLAYER)
        meta.switchScene(MetaController.Scenes.LIBRARY);
    }
}
