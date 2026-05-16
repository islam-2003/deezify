package be.deezify.controllers;

import be.deezify.json.MetadataAdapter;
import be.deezify.json.PathAdapter;
import be.deezify.json.PlaylistAdapter;
import be.deezify.json.TrackAdapter;
import be.deezify.models.PlayQueue;
import be.deezify.models.Playlist;
import be.deezify.models.Track;
import be.deezify.repositories.impl.JsonPlaylistRepository;
import be.deezify.repositories.impl.JsonTagRepository;
import be.deezify.repositories.impl.JsonTrackRepository;
import be.deezify.repositories.impl.JsonUserRepository;
import be.deezify.services.*;
import be.deezify.utils.AlertUtils;
import be.deezify.views.LibraryView;
import be.deezify.views.PlaylistView;
import be.deezify.views.View;
import be.deezify.views.global.ControlBarView;
import be.deezify.views.global.LanguageMenuView;
import be.deezify.views.global.MainView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.Getter;

import java.nio.file.Path;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * MetaController manages the navigation and handling of the different views of the application.
 * It acts as a central controller to switch scenes and coordinate other controllers.
 */
@Getter
public class MetaController {

    @Getter
    static MetaController instance;

    public static ResourceBundle getResourceBundle() {
        return ResourceBundle.getBundle("i18n.messages", instance.currentLocale);
    }

    /**
     * Enumeration defining the different scenes of the application.
     */
    public enum Scenes {
        LIBRARY,
        PLAY_QUEUE,
        PLAYLIST,
        METADATA,
        TAG,
        LYRICS,
        KARAOKE,
        VISUALIZER,
        SIGNUP
    }

    /**
     * The main stage of the application.
     */
    private final Stage stage;

    /**
     * The main view of the application.
     */
    @Getter
    private final MainView mainView;

    /**
     * The controller for the audio player.
     */
    private final MetadataController metadataController;

    /**
     * The controller for the music library.
     */
    private final LibraryController libraryController;
    private final PlaylistController playlistController;
    private final PlayQueueController playQueueController;
    private final ControlBarController controlBarController;
    private final LyricsController lyricsController;
    private final TagController tagController;
    private final KaraokeController karaokeController;
    private final UserController userController;
    private final LanguageMenuController languageMenuController;
    private final VisualizerController visualizerController;

    /**
     * The playback service
     */
    private final PlaybackService playbackService;

    private Locale currentLocale = Locale.ENGLISH; // default locale

    /**
     * Initializes the MetaController.
     *
     * @param stage    The main JavaFX stage.
     * @param mainView The main view of the application.
     * @throws Exception If an error occurs during initialization.
     */
    public MetaController(Stage stage, MainView mainView) throws Exception {
        instance = this;
        // Assign instance variables
        this.stage = stage;
        this.mainView = mainView;

        // Initialize services
        playbackService = new PlaybackService();
        Gson tagGson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        UserService userService = new UserService(new JsonUserRepository(Path.of("assets/json/users.json"), tagGson));
        TagService tagService = new TagService(new JsonTagRepository(Path.of("assets/json/tags.json"), tagGson));
        Gson trackGson = new GsonBuilder()
                .registerTypeAdapter(Track.class, new TrackAdapter(new MetadataAdapter(tagService), new PathAdapter(), userService))
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        TrackService trackService = new TrackService(new JsonTrackRepository(Path.of("assets/json/tracks.json"), trackGson));
        Gson playlistGson = new GsonBuilder()
                .registerTypeAdapter(Playlist.class, new PlaylistAdapter(trackService, userService))
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        PlaylistService playlistService = new PlaylistService(new JsonPlaylistRepository(Path.of("assets/json/playlists.json"), playlistGson));
        // Initialize models
        PlayQueue playQueue = new PlayQueue();

        // Initialize controllers
        metadataController = new MetadataController(this, trackService, userService);
        playlistController = new PlaylistController(this, playQueue, playbackService, new PlaylistView(), playlistService, trackService);
        libraryController = new LibraryController(this, playQueue, playbackService, tagService, trackService, playlistService);
        playQueueController = new PlayQueueController(this, playQueue, playbackService);
        lyricsController = new LyricsController(this, playbackService);
        tagController = new TagController(this, tagService);
        karaokeController = new KaraokeController(this);
        userController = new UserController(this, userService);

        visualizerController = new VisualizerController(this);

        // Initialize control bar and set UI elements
        controlBarController = new ControlBarController(this, new ControlBarView(), playbackService, playQueueController, lyricsController, trackService);
        mainView.setBottom(controlBarController.getView());

        // Initialize language menu and set UI elements
        languageMenuController = new LanguageMenuController(this, new LanguageMenuView());
        mainView.setTop(languageMenuController.getView());

        // Set up listeners
        playbackService.addListener(controlBarController);
        playbackService.addListener(playQueueController);
        playbackService.addListener(karaokeController);
        playbackService.addListener(lyricsController);
        playbackService.addListener(visualizerController);
        playQueue.addListener(playQueueController);
        userService.addListener(libraryController);
        userService.addListener(playlistController);
        playlistService.addListener(playlistController);
        tagService.addListener(libraryController);
    }


    /**
     * Switches the scene displayed in the central area of MainView.
     *
     * @param scene The scene to display.
     */
    public void switchScene(Scenes scene) {
        try {
            View view = null;

            switch (scene) {
                case LIBRARY:
                    view = libraryController.getView();
                    break;
                case PLAY_QUEUE:
                    view = playQueueController.getView();
                    break;
                case PLAYLIST:
                    view = playlistController.getView();
                    //playlistController.updatePlaylistView();
                    break;
                case METADATA:
                    view = metadataController.getView();
                    break;
                case LYRICS:
                    view = lyricsController.getView();
                    break;
                case TAG:
                    view = tagController.getView();
                    break;
                case KARAOKE:
                    view = karaokeController.getView();
                    break;
                case SIGNUP:
                    view = userController.getView();
                    break;
                case VISUALIZER:
                    view = visualizerController.getView();
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized scene: " + scene);
            }

            if (view != null) {
                mainView.setCenter(view.getRoot()); // Ensures correct UI placement
                view.updateLanguage();
                if (view instanceof LibraryView) {
                    System.out.println("update");
                    ((LibraryView) view).updateListView();
                }
            } else {
                AlertUtils.showError("error.title.generic", "error.text.generic");
            }

        } catch (Exception e) {
            AlertUtils.showError("error.title.generic", "error.text.generic");
        }
    }

    public void setLocale(Locale locale) {
        this.currentLocale = locale;
        updateAllViews();
    }

    private void updateAllViews() {
        libraryController.updateLanguage();
        controlBarController.updateLanguage();
        playlistController.updateLanguage();
        playQueueController.updateLanguage();
        metadataController.updateLanguage();
        tagController.updateLanguage();
        karaokeController.updateLanguage();
        mainView.updateLanguage();
    }
}
