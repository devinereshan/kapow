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
import javafx.application.Platform;

public class AudioPlayer {

    private Track currentTrack;
    private Media currentTrackMedia;
    private MediaPlayer currentTrackPlayer;
    private boolean autoPlay = false;
    private boolean playing;

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

        if (elapsedTimeListener != null) {
            // Update database so each track has an int record of length in seconds so this isn't necessary
            try (OldTrack tempTimeFix = new OldTrack(new File(currentTrack.getFilepath()))) {
                int timeInSeconds = tempTimeFix.lengthInSeconds();
                System.out.println("Time in seconds: " + timeInSeconds);
                elapsedTimeListener.setNewTrackDimensions(timeInSeconds);
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }

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

        currentTrackPlayer.setOnStopped(new Runnable() {
            @Override
            public void run() {
                playing = false;
            }
        });

        currentTrackPlayer.currentTimeProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                Platform.runLater(new Runnable() {
                    public void run() {
                        if (elapsedTimeListener == null) {
                            return;
                        }

                        System.out.println("Duration test: " + currentTrackPlayer.getMedia().getDuration());
                        elapsedTimeListener.updateElapsedTimeFields( currentTrackPlayer.getCurrentTime().toSeconds());
                    }
                });
            }
        });


        if (elapsedTimeListener != null) {
            elapsedTimeListener.connectSliderToPlayer(currentTrackPlayer);
        }
        // elapsedTimeSlider.valueProperty().addListener(new InvalidationListener() {
        //     public void invalidated(Observable ov) {
        //         if (elapsedTimeSlider.isPressed()) { // It would set the elapsedTimeSlider
        //             // as specified by user by pressing
        //             mediaPlayer.seek(mediaPlayer.getMedia().getDuration().multiply(elapsedTimeSlider.getValue() / 100));
        //         }
        //     }
        // });
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


    public String getElapsedTimeInSeconds() {
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