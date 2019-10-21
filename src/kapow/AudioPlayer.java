package kapow;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;


public class AudioPlayer {

    private Track currentTrack;
    // private int currentTrackIndex;
    private Vector<Track> trackQueue;

    private final int LEFT = -1;
    private final int RIGHT = 1;


    public AudioPlayer () {
        trackQueue = new Vector<Track>();
    }


    public void play() {
        if (currentTrack != null) {
            currentTrack.playClip();
        }
    }


    private void setCurrentTrack(int index) {
        if (index < 0 || index > trackQueue.size() - 1) {
            return;
        }

        if (!queueIsEmpty()) {
            currentTrack = trackQueue.elementAt(index);
        }
    }


    public String getCurrentTrackName() {
        if (currentTrack != null) {
            return currentTrack.getName();
        }
        return "No Track Selected";
    }


    public void pause() {
        if (currentTrack != null) {
            currentTrack.pauseClip();
        }
    }


    private void seek(int direction) {
        if (currentTrack != null) {
            boolean playing = currentTrack.isPlaying();
            int currentIndex = trackQueue.indexOf(currentTrack);
            if (indexHasTrack(currentIndex + direction)) {
                resetTrack(currentTrack);
                setCurrentTrack(trackQueue.indexOf(currentTrack) + direction);
                if (playing) {
                    play();
                }
            }
        }
    }


    public void seekLeft() {
        seek(LEFT);
    }


    public void seekRight() {
        seek(RIGHT);
    }


    private void resetTrack(Track track) {
        track.pauseClip();
        track.seek(0);
        track.reset();
    }


    private boolean isValidAudioFile(File file) {
        try {
            AudioSystem.getAudioInputStream(file);
            return true;
        } catch (UnsupportedAudioFileException | IOException e) {
            return false;
        }
    }


    private boolean indexHasTrack(int queueIndex) {
        try {
            trackQueue.elementAt(queueIndex);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    private Track createNewTrack(File trackFile) {
            return new Track(trackFile);
    }


    public void queueTrack(File trackFile) {
        if (isValidAudioFile(trackFile)) {
            trackQueue.add(createNewTrack(trackFile));

            if (trackQueue.size() == 1) {
                setCurrentTrack(0);
            }
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


    private boolean queueIsEmpty() {
        return trackQueue.size() == 0;
    }


    public void quit() {
        for (Track track : trackQueue) {
            try {
                track.closeClip();
            } catch (NullPointerException e) {
                System.err.println("Invalid clip found while closing application");
                e.printStackTrace();
            }
        }

    }


      // testing
    public void printQueue() {
        System.out.format("Tracks queued: %d\n", trackQueue.size());
    }
}