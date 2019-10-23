package kapow;

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


    public Track (File trackFile) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (isValidAudioFile(trackFile)) {
            audioListener = new AudioListener();
            this.name = trackFile.getName();
            audioInputStream = AudioSystem.getAudioInputStream(trackFile);
            audioFormat = audioInputStream.getFormat();
            totalFrames = audioInputStream.getFrameLength();
            frameRate = audioFormat.getFrameRate();
            clip = AudioSystem.getClip();
            clip.addLineListener(audioListener);
            clip.open(audioInputStream);
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


    public void playClip() {
        if (clip.isOpen()) {
            clip.start();
            // audioListener.reset();

            System.out.format("playclip(), Audio listener done: %b\tname: %s\nCurrent position: %d\nisPlaying: %b\n\n", audioListener.isDone(), name, clip.getFramePosition(), isPlaying());
        }
    }


    public void pauseClip() {
            clip.stop();

            System.out.format("pauseClip(), Audio listener done: %b\tname: %s\nisPlaying %b\n", audioListener.isDone(), name, isPlaying());
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
        return (int)(clip.getFramePosition() / frameRate);
    }


    public int lengthInSeconds() {
        return (int)(totalFrames / frameRate);
    }


    public void makeReady() {
        resetFramePosition();
        audioListener.reset();
    }


    public void stop() {
        pauseClip();
        resetFramePosition();
    }


    private void resetFramePosition() {
        while(clip.getFramePosition() != 0) {
            clip.setFramePosition(0);
        }

    }
}