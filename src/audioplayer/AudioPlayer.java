package audioplayer;

import java.io.File;
import java.io.IOException;
import java.util.Vector;


public class AudioPlayer {

    private Track currentTrack;
    private Vector<Track> trackQueue;

 

    public AudioPlayer () {
        trackQueue = new Vector<Track>();
    }

    

    private void playTrack() {
        currentTrack.playClip();
    }

    public void play() {
        if (isPlaying()) {
            return;
        }
        
        if (setCurrentTrack()) {
            playTrack();
        }
        
    }

    /** Returns true if currentTrack is set successfully */
    private boolean setCurrentTrack() {
        if (queueIsEmpty()) {
            return false;
        }

        currentTrack = trackQueue.elementAt(0);
        return true;
    }

    public void pauseTrack() {

    }
   
    public void queueTrack(Track track) {
        trackQueue.add(track);
    }

    private void getElapsedTime() {

    }

    private void getTotalTime() {

    }

    /**
     * If currentTrack is currently set, return true if it is playing.
     * If currentTrack has not been set, it is not playing. 
     * @return
     */
    private boolean isPlaying() {
        try {
            return currentTrack.isPlaying();
        } catch (Exception e) {
            return false;
        }
    }

    private boolean queueIsEmpty() {
        return trackQueue.size() == 0;
    }

}