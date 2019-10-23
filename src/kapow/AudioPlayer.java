package kapow;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer {

    private Track currentTrack;
    private Vector<Track> trackQueue;

    private final int SEEK_LEFT = -1;
    private final int SEEK_RIGHT = 1;

    public AudioPlayer() {
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

        if (indexHasTrack(index)) {
            currentTrack = trackQueue.elementAt(index);
            currentTrack.makeReady();
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
            // check if current track is playing, to start next song in same state
            boolean playNewTrack = currentTrack.isPlaying();

            // Find index of current track so verify seek direction
            int currentIndex = trackQueue.indexOf(currentTrack);

            // verify that a track exists in sought index
            if (indexHasTrack(currentIndex + direction)) {
                switchTrack(direction, playNewTrack);
            }
        }
    }


    private void switchTrack(int direction, boolean playNewTrack) {
        stop();
        setCurrentTrack(trackQueue.indexOf(currentTrack) + direction);
        if (playNewTrack) {
            play();
        }

    }

    public void seekLeft() {
        seek(SEEK_LEFT);
    }

    public void seekRight() {
        seek(SEEK_RIGHT);
    }


    public void stop() {
        currentTrack.stop();
    }


    private boolean isValidAudioFile(File file) {
        try {
            AudioSystem.getAudioInputStream(file);
            return true;
        } catch (UnsupportedAudioFileException | IOException e) {
            return false;
        }
    }

    private boolean indexHasTrack(int index) {
        if (index < 0 || index > trackQueue.size() - 1) {
            return false;
        }

        if (trackQueue.elementAt(index) != null) {
            return true;
        }

        return false;
    }

    private Track createNewTrack(File trackFile)
            throws UnsupportedAudioFileException, IOException, LineUnavailableException {
            return new Track(trackFile);
    }


    public void queueTrack(File trackFile) {
        if (isValidAudioFile(trackFile)) {
            try {
                trackQueue.add(createNewTrack(trackFile));

                if (trackQueue.size() == 1) {
                    setCurrentTrack(0);
                }
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                System.err.format("Unable to queue track");
                e.printStackTrace();
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
        for (Track track: trackQueue) {
            System.out.println(track.getName());
        }
    }
}