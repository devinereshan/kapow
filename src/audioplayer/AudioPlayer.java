package audioplayer;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer {

    private Clip currentSongClip;
    private Vector<File> songQueue;

    private AudioInputStream audioInputStream;
    private AudioFormat currentSongFormat;
    private AudioListener audioListener;

    private float currentSongFrameRate;
    private long currentSongFrames;


    public AudioPlayer (AudioListener al) {
        audioListener = al;
    }

    public void playSong() {
        try {
            currentSongClip.open(audioInputStream);
        } catch (LineUnavailableException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        currentSongClip.setFramePosition(currentSongClip.getFrameLength() - 400_000);
        // currentSongClip.loop(0);
        currentSongClip.start();

        

        
    }


    public boolean isPlaying() {
        return !audioListener.isDone();
    }
    /**
     * Create a clip instance from a file and set it as the current song.
     * 
     * @param songFile
     */
    public void setCurrentSong(File songFile) {
        // Create inputStream
        try {
            audioInputStream = AudioSystem.getAudioInputStream(songFile);
        } catch (UnsupportedAudioFileException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // getFormat
        currentSongFormat = audioInputStream.getFormat();

        // get frameLength;
        currentSongFrames = audioInputStream.getFrameLength();

        // getFrameRate
        currentSongFrameRate = currentSongFormat.getFrameRate();

        // create clip
        try {
            currentSongClip = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // add audio listener
        currentSongClip.addLineListener(audioListener);

        // open clip

        // set clip.loop

    }

    private void queueSong(File songFile) {
        songQueue.add(songFile);
    }

    private void getElapsedTime() {

    }

    private void getTotalTime() {

    }

    

    public void closeCurrentClip() {
        currentSongClip.close();
    }

}