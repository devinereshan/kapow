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

    private String name;

    private AudioInputStream audioInputStream;

    private AudioFormat audioFormat;

    private AudioListener audioListener;

    private float frameRate;
    private long totalFrames;

    // Track objects should be instantiated with a valid audio file and input stream.
    // Track clips should be opened at the end of the instantiation.
    // if any part of the track construction fails, the whole creation should fail and not be made available to the system.
    // Look into throwing custom exceptions.
    public Track (File trackFile) {
        if (isValidAudioFile(trackFile)) {    
            try {
                setAudioListener();
                setName(trackFile.getName());
                setAudioInputStream(trackFile);
                setAudioFormat();
                setTotalFrames();
                setFrameRate();
                setClip();
                connectClipToAudioListener();
                openClip();
            } catch (Exception e) {
                System.err.println("Failed to instantiate Track object.");
                e.printStackTrace();
            }
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

    private void setAudioListener() {
        audioListener = new AudioListener();
    }

    private void setName(String name) {
        this.name = name;
    }

    private void setAudioInputStream(File trackFile) throws UnsupportedAudioFileException, IOException {
        audioInputStream = AudioSystem.getAudioInputStream(trackFile);
    }

    private void setAudioFormat() {
        audioFormat = audioInputStream.getFormat();
    }

    private void setTotalFrames() {
        totalFrames = audioInputStream.getFrameLength();
    }

    private void setFrameRate() {
        frameRate = audioFormat.getFrameRate();
    }

    private void setClip() throws LineUnavailableException {
        clip = AudioSystem.getClip();
    }

    private void connectClipToAudioListener() {
        clip.addLineListener(audioListener);
    }

    private void openClip() throws LineUnavailableException, IOException {
        clip.open(audioInputStream);
    }


    public void playClip() {
        if (clip.isOpen()) {
            // seek(totalFrames - 400_000);
            clip.start();
        }
    }

    public void pauseClip() {
            clip.stop();
    }

    public boolean isPlaying() {
        return !audioListener.isDone();
    }

    public void closeClip() {
        clip.close();
    }

    public String getName() {
        return name;
    }


    public void seek(long framePosition) {
        // Revisit this - avoid potential loss of information with explicit conversion.
        // Annoyingly, there doesn't seem to be a setLongFramePosition(), even though
        // there is a getLongFramePosition(), which the documentation suggests using
        // over the int version: getFramePosition... because of potential loss of
        // information...
        clip.setFramePosition((int)framePosition);
    }

    public int elapsedTime() {
        return (int) (clip.getFramePosition() / frameRate);
    }
}