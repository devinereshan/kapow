package audioplayer;

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

    public String getCurrentTrackName() {
        return currentTrack.getName();
    }

    public void pauseTrack() {
        if (currentTrack != null) {
            currentTrack.pauseClip();
        }

    }
   
    public void queueTrack(Track track) {
        trackQueue.add(track);
        // if (trackQueue.size() == 1) {
        //     currentTrack = trackQueue.elementAt(0);
        // }
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

    private void getTotalTime() {

    }


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

}