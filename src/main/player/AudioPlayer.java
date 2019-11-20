package main.player;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer {

    private Track currentTrack;

    private Queue queue;

    private final int SEEK_LEFT = -1;
    private final int SEEK_RIGHT = 1;


    public AudioPlayer() {
        queue = new Queue();
    }


    public void queueTrack(File trackFile) {
        if (isValidAudioFile(trackFile)) {
            queue.addTrackFile(trackFile);
        }

        if (queue.size() == 1) {
            loadCurrentTrack();
        }
    }


    private boolean isValidAudioFile(File file) {
        try {
            AudioSystem.getAudioInputStream(file);
            return true;
        } catch (UnsupportedAudioFileException | IOException e) {
            return false;
        }
    }


    private void loadCurrentTrack() {
        File file = queue.getCurrentTrackFile();

        if (isValidAudioFile(file)) {
            try {
                currentTrack = new Track(file);
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                System.err.format("Unable to set current track");
                e.printStackTrace();
            }
        }
    }



    public void setAndPlay(File file) {
        queue.quickLoadTrackFile(file);
        switchTrack();
        play();
    }

    public void play() {
        if (currentTrack != null) {
            currentTrack.playClip();
        }
    }


    public void pause() {
        if (currentTrack != null) {
            currentTrack.pauseClip();
        }
    }


    public void seekLeft() {
        seek(SEEK_LEFT);
    }


    public void seekRight() {
        seek(SEEK_RIGHT);
    }


    private void seek(int direction) {
        if (currentTrack != null && queue.hasTrack(direction)) {
            // check if current track is playing, to start next song in same state
            boolean playNewTrack = currentTrack.isPlaying();

            queue.seek(direction);
            switchTrack();

            if (playNewTrack) {
                play();
            }
        }
    }


    // Destroy old track object and create new one for new track
    private void switchTrack() {
        stop();
        if (currentTrack != null) {
            currentTrack.close();
        }
        currentTrack = null;
        loadCurrentTrack();
    }


    public String getCurrentTrackName() {
        if (currentTrack != null) {
            return currentTrack.getName();
        }
        return "No Track Selected";
    }


    public void stop() {
        if (currentTrack != null) {
            currentTrack.stop();
        }
    }


    public String getElapsedTime() {
        int[] time =  convertTime(currentTrack.elapsedTime());
        return String.format("%02d:%02d:%02d", time[0], time[1], time[2]);
    }


    /** Returns an int array of elapsed time in {hh, mm, ss} */
    private int[] convertTime(int timeInSeconds) {
        int [] time = new int[3];
        time[0] = timeInSeconds / 360;
        timeInSeconds %= 360;
        time[1] = timeInSeconds / 60;
        timeInSeconds %= 60;
        time[2] = timeInSeconds;
        return time;
    }


    public String getLengthOfTrackInSeconds() {
        int[] time = convertTime(currentTrack.lengthInSeconds());
        return String.format("%02d:%02d:%02d", time[0], time[1], time[2]);
    }


    public void quit() {
        try {
            if (currentTrack != null) {
                currentTrack.close();
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