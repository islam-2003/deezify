package be.deezify.services;

import be.deezify.listeners.PlaybackServiceListener;
import be.deezify.models.dto.TrackDTO;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;


/**
 * PlaybackService handles audio playback using JavaFX {@link MediaPlayer}.
 * It supports features like fade-in/fade-out transitions, playback speed control,
 * volume, balance, seeking, and real-time audio spectrum analysis.
 * It notifies registered {@link PlaybackServiceListener}s of playback events.
 */
@Getter
public class PlaybackService extends Service<PlaybackServiceListener> {

    public enum Status {
        PLAYING,
        PAUSED,
        STOPPED,
        ERROR,
        NULL
    }

    @Setter
    private double transitionDuration = 2.5;
    private double rate = 1;
    private double volume = 0.5;
    private double balance = 0;
    private MediaPlayer fadingInMediaPlayer;
    private MediaPlayer fadingOutMediaPlayer;
    private ChangeListener<Duration> changeListener;

    public void playTrack(TrackDTO track) {
        if (fadingInMediaPlayer != null) {
            fadingOutMediaPlayer = fadingInMediaPlayer;
            fadeOut(fadingOutMediaPlayer);
            startNewTrack(track, true);
        } else {
            startNewTrack(track, false);
        }
    }

    private void startNewTrack(TrackDTO track, boolean fading) {
        Media media = new Media(track.getFilePath().toUri().toString());
        fadingInMediaPlayer = new MediaPlayer(media);
        fadingInMediaPlayer.setRate(rate);
        fadingInMediaPlayer.setBalance(balance);
        fadingInMediaPlayer.setVolume(volume);

        fadingInMediaPlayer.setOnReady(() -> {
            changeListener = (observable, oldValue, newValue) -> {
                double currentSeconds = newValue.toSeconds();
                double totalTime = media.getDuration().toSeconds();
                notifyTimeUpdate(currentSeconds, totalTime);
                // Triggers the end at fadingDuration seconds of the end of track (logical end of the track in this context)
                if (totalTime - currentSeconds <= transitionDuration) {
                    notifyTrackEnd();
                }
            };
            fadingInMediaPlayer.currentTimeProperty().addListener(changeListener);
        });

        notifyTrackUpdate(track);

        setupSpectrum();

        fadingInMediaPlayer.play();
        if (fading) {
            fadeIn(fadingInMediaPlayer, volume);
        }
        notifyStateUpdate();
    }

    /**
     * Smoothly fades out a given player and disposes it.
     */
    private void fadeOut(MediaPlayer player) {
        Timeline fadeOut = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(player.volumeProperty(), player.getVolume())),
                new KeyFrame(Duration.seconds(transitionDuration), new KeyValue(player.volumeProperty(), 0))
        );
        player.currentTimeProperty().removeListener(changeListener);
        fadeOut.setOnFinished(e -> {
            player.stop();
            player.dispose();
        });
        fadeOut.play();
    }

    /**
     * Smoothly fades in the given player to a target volume.
     */
    private void fadeIn(MediaPlayer player, double targetVolume) {
        player.setVolume(0);
        Timeline fadeIn = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(player.volumeProperty(), 0)),
                new KeyFrame(Duration.seconds(transitionDuration), new KeyValue(player.volumeProperty(), targetVolume))
        );
        fadeIn.play();
    }

    /**
     * Configures the audio spectrum listener for real-time amplitude and frequency feedback.
     */
    private void setupSpectrum() {
        // Configure spectrum analysis parameters
        fadingInMediaPlayer.setAudioSpectrumInterval(0.05); // update every 50ms
        fadingInMediaPlayer.setAudioSpectrumNumBands(64); // number of frequency bands
        fadingInMediaPlayer.setAudioSpectrumThreshold(-80); // silence threshold in decibels

        // Set the listener to receive amplitude and frequency info
        fadingInMediaPlayer.setAudioSpectrumListener((timestamp, duration, magnitudes, phases) -> {

            // Average amplitude (simplified from frequency magnitudes)
            double avgAmplitude = 0;
            for (double mag : magnitudes) {
                avgAmplitude += (80 + mag); // magnitudes are negative, -80 to 0
            }
            avgAmplitude /= magnitudes.length;
            avgAmplitude /= 80; // Normalize to 0.0 - 1.0

            notifyAmplitudeUpdate(avgAmplitude);


            int strongestBand = 0;
            for (int i = 1; i < magnitudes.length; i++) {
                if (magnitudes[i] > magnitudes[strongestBand]) {
                    strongestBand = i;
                }
            }
            double frequency = strongestBand * (22050.0 / magnitudes.length); // estimate frequency in Hz
            notifyFrequencyUpdate(frequency);
        });
    }

    public Status getStatus() {
        if (fadingInMediaPlayer == null) {
            return Status.NULL;
        }

        return switch (fadingInMediaPlayer.getStatus()) {
            case PLAYING -> Status.PLAYING;
            case PAUSED -> Status.PAUSED;
            case STOPPED -> Status.STOPPED;
            default -> Status.ERROR;
        };
    }

    public void pause() {
        if (fadingInMediaPlayer != null) {
            fadingInMediaPlayer.pause();
            notifyStateUpdate();
        }
    }

    public void resume() {
        if (fadingInMediaPlayer != null) {
            fadingInMediaPlayer.play();
            notifyStateUpdate();
        }
    }

    public void stop() {
        if (fadingInMediaPlayer != null) {
            fadingInMediaPlayer.stop();
        }
    }

    public void increasePlaybackSpeed() {
        double newSpeed = Math.min(fadingInMediaPlayer.getRate() + 0.25, 2.0); // Max x2
        rate = newSpeed;
        if (fadingInMediaPlayer != null) {
            fadingInMediaPlayer.setRate(newSpeed);
        }
    }

    public void decreasePlaybackSpeed() {
        double newSpeed = Math.max(fadingInMediaPlayer.getRate() - 0.25, 0.5); // Min x0.5
        rate = newSpeed;
        if (fadingInMediaPlayer != null) {
            fadingInMediaPlayer.setRate(newSpeed);
        }
    }

    public void setVolume(double value) {
        volume = value;
        if (fadingInMediaPlayer != null) {
            fadingInMediaPlayer.setVolume(value);
        }
    }

    public void seek(double percentage) {
        if (fadingInMediaPlayer != null) {
            double totalDuration = fadingInMediaPlayer.getTotalDuration().toSeconds();
            double newTime = Math.round((percentage / 100) * totalDuration);
            fadingInMediaPlayer.seek(Duration.seconds(newTime));
        }
    }

    private void notifyTrackUpdate(TrackDTO track) {
        for (PlaybackServiceListener listener : listeners) {
            listener.onTrackUpdate(track);
        }
    }

    private void notifyTimeUpdate(double time, double totalTime) {
        for (PlaybackServiceListener listener : listeners) {
            listener.onPlaybackTimeUpdate(time, totalTime);
        }
    }

    private void notifyStateUpdate() {
        for (PlaybackServiceListener listener : listeners) {
            listener.onPlaybackStateUpdate(getStatus());
        }
    }

    private void notifyTrackEnd() {
        for (PlaybackServiceListener listener : listeners) {
            listener.onTrackEnd();
        }
    }

    private void notifyAmplitudeUpdate(double amplitude) {
        for (PlaybackServiceListener listener : listeners) {
            listener.onAmplitudeUpdate(amplitude);
        }
    }

    private void notifyFrequencyUpdate(double frequency) {
        for (PlaybackServiceListener listener : listeners) {
            listener.onFrequencyUpdate(frequency);
        }
    }

    public void setBalance(double value) {
        this.balance = value;
        if (fadingInMediaPlayer != null) {
            fadingInMediaPlayer.setBalance(value);
        }
    }

}
