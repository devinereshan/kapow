package main.player;

import java.nio.file.Path;
import java.nio.file.Paths;

import main.library.Track;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class AudioPlayer {

    private Track currentTrack;
    private Media currentTrackMedia;
    private MediaPlayer currentTrackPlayer;
    private boolean autoPlay = false;
    private boolean playing;

    private Queue queue;

    private final int SEEK_LEFT = -1;
    private final int SEEK_RIGHT = 1;


    public AudioPlayer() {
        queue = new Queue();
    }


    private void loadTrack(Track track) {
        if (playing) {
            autoPlay = true;
        } else {
            autoPlay = false;
        }

        if (currentTrack != null) {
            stop();
            currentTrackPlayer.dispose();
        }

        currentTrack = track;

        loadCurrentTrack();
    }

    private void loadCurrentTrack() {
        Path filepath = Paths.get(currentTrack.getFilepath());

        currentTrackMedia = new Media(filepath.toUri().toString());
        currentTrackPlayer = new MediaPlayer(currentTrackMedia);

        currentTrackPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
                if (autoPlay) {
                    currentTrackPlayer.play();
                    playing = true;
                }
            }
        });

        currentTrackPlayer.setOnPaused(new Runnable() {
            @Override
            public void run() {
                playing = false;
            }
        });

        currentTrackPlayer.setOnPlaying(new Runnable() {
            @Override
            public void run() {
                playing = true;
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
            currentTrackPlayer.setAutoPlay(true);
        }
    }


    public void pause() {
        if (currentTrackPlayer != null) {
            currentTrackPlayer.pause();
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
        }
    }


    public String getElapsedTime() {
        return String.valueOf(currentTrackPlayer.getCurrentTime());
        // int[] time =  convertTime(currentTrack.elapsedTime());
        // return String.format("%02d:%02d:%02d", time[0], time[1], time[2]);
    }


    public String getLengthOfTrackInSeconds() {
        return String.valueOf(currentTrackPlayer.getCurrentTime().toSeconds());
        // int[] time = convertTime(currentTrack.lengthInSeconds());
        // return String.format("%02d:%02d:%02d", time[0], time[1], time[2]);
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