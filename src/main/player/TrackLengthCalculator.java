package main.player;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


public class TrackLengthCalculator implements AutoCloseable {

    private Clip clip;

    private AudioInputStream audioInputStream;

    private AudioFormat audioFormat;

    private float frameRate;
    private long totalFrames;

    private int hours;
    private int minutes;
    private int seconds;


    public TrackLengthCalculator (File trackFile) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (isValidAudioFile(trackFile)) {
            audioInputStream = AudioSystem.getAudioInputStream(trackFile);
            audioFormat = audioInputStream.getFormat();
            totalFrames = audioInputStream.getFrameLength();
            frameRate = audioFormat.getFrameRate();
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            calculateLength();
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


    @Override
    public void close() {
        clip.close();
    }
}