package be.deezify.views.global;

import be.deezify.controllers.ControlBarController;
import be.deezify.controllers.MetaController;
import be.deezify.utils.NumberUtils;
import be.deezify.views.View;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.ResourceBundle;

public class ControlBarView extends View {

    @Setter
    private ControlBarController controlBarController;

    private ResourceBundle bundle;

    private boolean updateInProgress = false;
    @FXML
    private Label songTitleLabel; // Song title
    @FXML
    private Label artistLabel; // Artist name
    @Getter
    @FXML
    private ImageView albumCoverView; // Album cover
    @FXML
    private Slider progressSlider; // Progress bar
    @FXML
    private Slider volumeSlider;
    @FXML
    private Label currentTimeLabel; // Current time
    @FXML
    private Label totalTimeLabel; // Total time
    @FXML
    private Button pauseButton;
    @FXML
    private Button nextButton;
    @FXML
    private Button previousButton;
    @FXML
    private Button speedUpButton;
    @FXML
    private Button slowDownButton;
    @FXML
    private Button shuffleButton;
    @FXML
    private Slider balanceSlider;
    @FXML
    private Button toggleVolumeButton;
    @FXML
    private Button toggleBalanceButton;
    @FXML
    private Button lyricsButton;
    @FXML
    private Slider transitionSlider;
    @FXML
    private Label transitionLabel;

    public ControlBarView() throws IOException {
        super("/fxml/ControlBar.fxml");
    }

    @FXML
    private void initialize() {
        progressSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!updateInProgress) {
                handleProgressSliderChange(newValue.doubleValue());
            }
        });
        balanceSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            handleBalanceSliderChange(newValue.doubleValue());
        });
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            handleVolumeSliderChange(newValue.doubleValue());
        });
        transitionSlider.valueProperty().addListener(((observableValue, oldValue, newValue) -> {
            handleTransitionSliderChange(newValue.doubleValue());
            transitionLabel.setText("Transition: " + NumberUtils.formatDouble(newValue.doubleValue()) + "s");
        }));
        pauseButton.setOnAction(event -> handlePause());
        nextButton.setOnAction(event -> handleNext());
        previousButton.setOnAction(event -> handlePrevious());
        speedUpButton.setOnAction(event -> handleSpeedUp());
        slowDownButton.setOnAction(event -> handleSlowDown());
        shuffleButton.setOnAction(event -> handleShuffle());

        // Handlers to show/hide sliders
        toggleVolumeButton.setOnAction(event -> toggleVolumeSlider());
        toggleBalanceButton.setOnAction(event -> toggleBalanceSlider());

        // Handler for lyrics button
        lyricsButton.setOnAction(event -> handleLyricsButton());
    }

    @Override
    protected String getTitle() {
        return "";
    }

    public void setSongTitle(String title) {
        songTitleLabel.setText(title);
    }

    public void setArtist(String artist) {
        artistLabel.setText(artist);
    }

    public void setAlbumCover(ImageView albumCover) {
        this.albumCoverView.setImage(albumCover.getImage());
    }

    public void setProgress(double progress) {
        updateInProgress = true;
        progressSlider.setValue(progress);
        updateInProgress = false;
    }

    public void setCurrentTime(String time) {
        currentTimeLabel.setText(time);
    }

    public void setTotalTime(String time) {
        totalTimeLabel.setText(time);
    }

    public void updatePlayButtonText(String text) {
        pauseButton.setText(text);
    }

    private void handleProgressSliderChange(double value) {
        controlBarController.setProgress(value);
    }

    private void handleVolumeSliderChange(double value) {
        controlBarController.setVolume(value);
    }

    private void handlePause() {
        controlBarController.pause();
    }

    private void handleNext() {
        controlBarController.next();
    }

    private void handlePrevious() {
        controlBarController.previous();
    }

    private void handleSpeedUp() {
        controlBarController.speedUp();
    }

    private void handleSlowDown() {
        controlBarController.slowDown();
    }

    private void handleShuffle() {
        controlBarController.shuffle();
    }

    private void handleBalanceSliderChange(double value) {
        controlBarController.setBalance(value);
    }

    private void toggleVolumeSlider() {
        boolean isVolumeVisible = volumeSlider.isVisible();
        volumeSlider.setVisible(!isVolumeVisible);
        balanceSlider.setVisible(isVolumeVisible); // Hide balanceSlider if volumeSlider is visible
    }

    private void toggleBalanceSlider() {
        boolean isBalanceVisible = balanceSlider.isVisible();
        balanceSlider.setVisible(!isBalanceVisible);
        volumeSlider.setVisible(isBalanceVisible); // Hide volumeSlider if balanceSlider is visible
    }

    private void handleLyricsButton() {
        controlBarController.showLyrics();
    }

    private void handleTransitionSliderChange(double value) {
        controlBarController.setTransitionDuration(value);
    }

    private void configureUITexts() {
        slowDownButton.setText(bundle.getString("slow.button"));
        speedUpButton.setText(bundle.getString("speed.button"));
        songTitleLabel.setText(bundle.getString("music.title"));
        artistLabel.setText(bundle.getString("artist.name"));
    }

    public void updateLanguage() {
        this.bundle = MetaController.getResourceBundle();
        configureUITexts();
    }
}
