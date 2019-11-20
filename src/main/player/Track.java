package main.player;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Track implements AutoCloseable {

    private Clip clip;

    private String name;

    private AudioInputStream audioInputStream;

    private AudioFormat audioFormat;

    private float frameRate;
    private long totalFrames;

    private int hours;
    private int minutes;
    private int seconds;


    public Track (File trackFile) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (isValidAudioFile(trackFile)) {
            this.name = trackFile.getName();
            audioInputStream = AudioSystem.getAudioInputStream(trackFile);
            audioFormat = audioInputStream.getFormat();
            totalFrames = audioInputStream.getFrameLength();
            frameRate = audioFormat.getFrameRate();
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            calculateLength();
            makeReady();
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
            System.out.format("start playClip() position: %d\n", clip.getFramePosition());

            clip.start();

            // trying to guarantee that methods called after this one do not
            // acquire the state of the clip before it has been updated
            while (!clip.isActive()) {
                System.out.println("----------");
            }

            System.out.format("playclip(),\tname: %s\nCurrent position: %d\nisPlaying: %b\nisActive %b\n\n", name, clip.getFramePosition(), isPlaying(), clip.isActive());
        }
    }


    public void pauseClip() {
            clip.stop();

            // trying to gurantee that methods called after this one do not
            // acquire the state of the clip before is has been updated
            while (clip.isActive()) {
                System.out.println("---------------");
            }

            System.out.format("pauseClip()\tname: %s\nisPlaying %b\nisActive() %b\n", name, isPlaying(), clip.isActive());
    }


    public boolean isPlaying() {
        return clip.isActive();
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


    private void calculateLength() {
        int lengthInSeconds = lengthInSeconds();
        hours = lengthInSeconds / 3600;
        lengthInSeconds %= 3600;
        minutes = lengthInSeconds / 60;
        lengthInSeconds %= 60;
        seconds = lengthInSeconds;
    }


    public int getHours() {
        return hours;
    }


    public int getMinutes() {
        return minutes;
    }


    public int getSeconds() {
        return seconds;
    }


    public void makeReady() {
        resetFramePosition();
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

    @Override
    public void close() {
        clip.close();
    }
}