package audioplayer;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Track {

    /**
     * The audio clip handled by this Track instance.
     */
    private Clip clip;

    private AudioInputStream audioInputStream;

    private AudioFormat audioFormat;

    private AudioListener audioListener;

    private float frameRate;
    private long totalFrames;

    // Track objects should be instantiated with a valid audio file and input stream.
    // Track clips should be opened at the end of the instantiation.
    // if any part of the track construction fails, the whole creation should fail and not be made available to the system.
    // Look into throwing custom exceptions.
    
    /**
     * 
     * @param trackFile
     */
    public Track (File trackFile) {
        assert trackFile.exists();

        try {
            audioListener = new AudioListener();

            try {
                audioInputStream = AudioSystem.getAudioInputStream(trackFile);
            } catch (UnsupportedAudioFileException e) {
                System.err.println("Not a valid audio file format");
                e.printStackTrace();
            } catch (IOException e) {
                System.err.println("Unable to open file");
                e.printStackTrace();
            }

            audioFormat = audioInputStream.getFormat();
            totalFrames = audioInputStream.getFrameLength();
            frameRate = audioFormat.getFrameRate();

            try {
                clip = AudioSystem.getClip();
            } catch (LineUnavailableException e) {
                System.err.println("Line Unavailable. Unable to acquire clip from Audio System.");
                e.printStackTrace();
            }

            clip.addLineListener(audioListener);

            try {
                clip.open(audioInputStream);
            } catch (LineUnavailableException e) {
                System.err.println("Line Unavailable. Unable to open clip");
                e.printStackTrace();
            } catch (IOException e) {
                System.err.println("IOException. Unable to open clip.");
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.err.println("Unknown exception. Failed to instantiate Track object.");
            e.printStackTrace();
        }
    }


    public void playClip() {
        if (clip.isOpen()) {
            seek(totalFrames - 400_000);
            clip.start();
        }
    }

    public boolean isPlaying() {
        return !audioListener.isDone();
    }

    public void closeClip() {
        clip.close();
    }


    public void seek(long framePosition) {
        // Revisit this - avoid potential loss of information with explicit conversion.
        // Annoyingly, there doesn't seem to be a setLongFramePosition(), even though
        // there is a getLongFramePosition(), which the documentation suggests using
        // over the int version: getFramePosition... because of potential loss of
        // information...
        clip.setFramePosition((int)framePosition);
    }
}