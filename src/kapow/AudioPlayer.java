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

    private final int SEEK_LEFT = -1;
    private final int SEEK_RIGHT = 1;


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

        if (indexHasTrack(index)) {
            currentTrack = trackQueue.elementAt(index);
            currentTrack.reset(); // make sure track starts from the beginning. Temporary fix because of race conditions. Learn about threads. If I call currentTrack.reset before switching to a new track, the setFramePosition doesn't complete. This is verified by calling getFramePosition() on the previous track. Apparently setFramePosition() finds the current frame position by seeking for it. Some condition somewhere is interrupting the call to setFramePosition(). Right now I'm assuming it's something to do with setting currentTrack to point to the new track before the call to setFramePosition() finishes, which is odd because reference to the previous track is not actually lost. Only the reference in currentTrack is lost. In any case, I feel the most responsible way to handle this is to make the program wait for the previous track's reset() method to finish before assigning the next track to currentTrack. But maybe that's not necessary. In the meantime resetting each new track as it is assigned to current track seems to fix the issue, because threads do wait for themselves (If my assumption about this being a race condition is correct). It might be just as simple to set a startFrame member variable and make sure startFrame == getFramePosition() before doing anything with a track, but I haven't thought much on that yet. Interestingly, the tracks reset fine if they are paused before switching.
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
        resetTrack(currentTrack);
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


    private void resetTrack(Track track) {
        // track.pauseClip();
        // track.seek(0);
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