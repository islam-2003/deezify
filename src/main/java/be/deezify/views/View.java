package be.deezify.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;
import java.net.URL;

/**
 * Abstract base class for all views.
 * This class handles the loading of FXML files and provides common methods for views.
 */
public abstract class View {
    /**
     * The root Pane of the view.
     */
    @Getter
    protected final Pane root;

    /**
     * The scene associated with the view.
     */
    protected Scene scene;

    /**
     * Constructor of the View class.
     *
     * @param fxmlPath The path to the FXML file.
     * @throws IOException If an error occurs while loading the FXML file.
     */
    public View(String fxmlPath) throws IOException {
        URL url = getClass().getResource(fxmlPath);
        if (url == null) {
            throw new IOException("FXML file not found: " + fxmlPath);
        }

        FXMLLoader fxmlLoader = new FXMLLoader(url);
        fxmlLoader.setController(this);
        this.root = fxmlLoader.load(); // Loads the root Pane from the FXML
        this.scene = new Scene(root);
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

    /**
     * Returns the title of the view.
     *
     * @return The title of the view.
     */
    protected abstract String getTitle();

    public abstract void updateLanguage();

}