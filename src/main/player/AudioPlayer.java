package main.player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import main.library.Track;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

public class AudioPlayer {

    private Track currentTrack;
    private Media currentTrackMedia;
    private MediaPlayer currentTrackPlayer;
    private boolean autoPlay = false;

    private ElapsedTimeListener elapsedTimeListener;

    private Queue queue;

    private final int SEEK_LEFT = -1;
    private final int SEEK_RIGHT = 1;

    public AudioPlayer() {
        queue = new Queue();
    }

    public AudioPlayer(ElapsedTimeListener elapsedTimeListener) {
        this.elapsedTimeListener = elapsedTimeListener;
        queue = new Queue();
    }

    private void loadTrack(Track track) {

        if (currentTrackPlayer != null) {
            currentTrackPlayer.dispose();
        }

        currentTrack = track;

        loadCurrentTrack();
    }

    private void loadCurrentTrack() {
        Path filepath = Paths.get(currentTrack.getFilepath());

        currentTrackMedia = new Media(filepath.toUri().toString());
        currentTrackPlayer = new MediaPlayer(currentTrackMedia);

        if (elapsedTimeListener != null) {
            // Update database so each track has an int record of length in seconds so this isn't necessary
            System.out.println("Length in seconds test: " + currentTrack.getLengthInSeconds());
            try (TrackLengthCalculator tempTimeFix = new TrackLengthCalculator(new File(currentTrack.getFilepath()))) {
                elapsedTimeListener.setNewTrackDimensions(tempTimeFix.lengthInSeconds());
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }

        currentTrackPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
                if (autoPlay) {
                    play();
                }
            }
        });

        currentTrackPlayer.currentTimeProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                if (elapsedTimeListener == null) {
                    return;
                }

                elapsedTimeListener.updateElapsedTimeFields( currentTrackPlayer.getCurrentTime().toSeconds());
            }
        });

    }


    public void setAndPlay(Track track) {
        loadTrack(queue.quickLoadTrack(track));
        play();
    }


    private void nextTrack() {
        if (queue.hasTrack(SEEK_RIGHT)) {
            loadTrack(queue.next());
        }
    }


    private void previousTrack() {
        if (queue.hasTrack(SEEK_LEFT)) {
            loadTrack(queue.previous());
        }
    }


    public void play() {
        if (currentTrackPlayer != null) {
            currentTrackPlayer.play();
            autoPlay = true;
        }
    }


    public void pause() {
        if (currentTrackPlayer != null) {
            currentTrackPlayer.pause();
            autoPlay = false;
        }
    }


    public void seekLeft() {
        if (queue.hasTrack(SEEK_LEFT)) {
            previousTrack();
        }
    }


    public void seekRight() {
        if (queue.hasTrack(SEEK_RIGHT)) {
            nextTrack();
        }
    }


    public Track getCurrentTrack() {
        return currentTrack;
    }

    public void stop() {
        if (currentTrackPlayer != null) {
            currentTrackPlayer.stop();
            autoPlay = false;
        }
    }


    public void quit() {
        try {
            if (currentTrack != null) {
                currentTrackPlayer.dispose();
            }
        } catch (NullPointerException e) {
            System.err.println("Invalid clip found while closing application");
            e.printStackTrace();
        }
    }


      // testing
    public void printQueue() {
        System.out.println(queue.toString());
    }
}