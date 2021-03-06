package main.player;

import java.nio.file.Path;
import java.nio.file.Paths;

import main.library.Track;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

public class AudioPlayer {

    private Track currentTrack;
    private Media currentTrackMedia;
    private MediaPlayer currentTrackPlayer;
    private boolean autoPlay = false;
    private SimpleStringProperty currentTrackName = new SimpleStringProperty("No Track Selected");

    private ElapsedTimeListener elapsedTimeListener;

    private Queue queue;

    private double volume = 0.8;

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

        setVolume(volume);

        currentTrackName.set(currentTrack.getName());
        
        if (elapsedTimeListener != null) {
            elapsedTimeListener.setNewTrackDimensions(currentTrack.getLengthInSeconds());
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
                if (currentTrackPlayer.getCurrentTime().toSeconds() > elapsedTimeListener.getMaxElapsedTime()) {
                    if (queue.hasTrack(SEEK_RIGHT)) {
                        loadTrack(queue.next());
                    } else {
                        stop();
                        loadCurrentTrack();
                    }
                }

                if (elapsedTimeListener == null) {
                    return;
                }

                elapsedTimeListener.updateElapsedTimeFields(currentTrackPlayer.getCurrentTime().toSeconds());
            }
        });

    }

    public void setVolume(double volume) {
        if (currentTrackPlayer != null) {
            this.volume = volume;
            currentTrackPlayer.setVolume(volume);
        }
    }

    public void queueAndPlay(Track track) {
        loadTrack(queue.quickLoadTrack(track));
        play();
    }

    public void queueAndPlay(ObservableList<Track> tracks) {
        boolean wasEmpty = queue.size() == 0;
        queueNext(tracks);
        if (wasEmpty) {
            loadTrack(queue.getCurrentTrack());
        } else {
            seekRight();
        }

        play();
    }

    public ObservableList<String> getTrackNameList() {
        return queue.getTrackNames();
    }

    private void nextTrack() {
        if (queue.hasTrack(SEEK_RIGHT)) {
            loadTrack(queue.next());
        }
    }


    public void seekToQueueIndex(int index) {
        if (queue.hasIndex(index)) {
            loadTrack(queue.seekStep(index));
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
            elapsedTimeListener.resetElapsedTime();
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


    public void queue(Track track) {
        queue.add(track);
        if (currentTrack == null) {
            seekRight();
        }
    }

    public void queue(ObservableList<Track> tracks) {
        queue.add(tracks);
        if (currentTrack == null) {
            seekRight();
        }
    }

    public void queueNext(Track track) {
        queue.addNext(track);
        if (currentTrack == null) {
            seekRight();
        }
    }

    public void queueNext(ObservableList<Track> tracks) {
        queue.addNext(tracks);
        if (currentTrack == null) {
            seekRight();
        }
    }


    public ObservableList<Track> getQueue() {
        return queue.getTracks();
    }

    public void fineSeek(Duration duration) {
        if (currentTrackPlayer != null) {
            currentTrackPlayer.seek(duration);
        }
    }

    public String getCurrentTrackName() {
        return currentTrackName.get();
    }

    public void setCurrentTrackName(String name) {
        currentTrackName.set(name);
    }

    public SimpleStringProperty currentTrackNameProperty() {
        return currentTrackName;
    }
}