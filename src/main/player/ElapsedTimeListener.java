package main.player;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Slider;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class ElapsedTimeListener {
    private final int HOURS = 0;
    private final int MINUTES = 1;
    private final int SECONDS = 2;
    private double elapsedTimeInSeconds = 0;
    private SimpleStringProperty totalTime = new SimpleStringProperty("--:--");
    private SimpleStringProperty elapsedTime = new SimpleStringProperty("--:--");
    private Slider elapsedTimeSlider;

    public ElapsedTimeListener(Slider elapsedTimeSlider) {
        this.elapsedTimeSlider = elapsedTimeSlider;

    }


    public void connectSliderToPlayer(MediaPlayer mediaPlayer) {
        elapsedTimeSlider.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable observable) {
                if (elapsedTimeSlider.isPressed()) {
                    mediaPlayer.seek(Duration.seconds(elapsedTimeSlider.getValue()));
                }
            }
        });

    }


    public void updateElapsedTimeFields(double elapsedTime) {
        System.out.println("Updating elapsed time: " + elapsedTime);
        elapsedTimeInSeconds = elapsedTime;
        setElapsedTime((int) elapsedTimeInSeconds);

        elapsedTimeSlider.setValue(elapsedTime);
    }


    public void setNewTrackDimensions(double totalTimeInSeconds) {
        setTotalTime((int) totalTimeInSeconds);
        setElapsedTime(0);
        elapsedTimeSlider.setMin(0);
        elapsedTimeSlider.setMax(totalTimeInSeconds);
        elapsedTimeSlider.setValue(0);
    }


    public void setElapsedTime(String time) {
        this.elapsedTime.set(time);
    }


    public void setTotalTime(String time) {
        this.totalTime.set(time);
    }


    public String getElapsedTime() {
        return elapsedTime.get();
    }


    public String getTotalTime() {
        return totalTime.get();
    }


    public StringProperty elapsedTimeProperty() {
        return elapsedTime;
    }

    public StringProperty totalTimeProperty() {
        return totalTime;
    }


    private void setTotalTime(int timeInSeconds) {
        int[] time = convertTime(timeInSeconds);

        if (time[HOURS] > 0) {
            System.out.println("has hours: " + time[HOURS] + time[MINUTES]+ time[SECONDS]);
            setTotalTime(String.format("%02d:%02d:%02d", time[HOURS], time[MINUTES], time[SECONDS]));
        } else {
            System.out.println("No hours: " + time[HOURS] + time[MINUTES]+ time[SECONDS]);
            setTotalTime(String.format("%02d:%02d", time[MINUTES], time[SECONDS]));
        }
    }

    private void setElapsedTime(int timeInSeconds) {
        int[] time = convertTime(timeInSeconds);

        if (time[HOURS] > 0) {
            setElapsedTime(String.format("%02d:%02d:%02d", time[HOURS], time[MINUTES], time[SECONDS]));
        } else {
            setElapsedTime(String.format("%02d:%02d", time[MINUTES], time[SECONDS]));
        }
    }

    private int[] convertTime(int timeInSeconds) {
        System.out.println("In convert Time: " + timeInSeconds);
        int hours = timeInSeconds / 3600;
        timeInSeconds %= 3600;
        int minutes = timeInSeconds / 60;
        timeInSeconds %= 60;
        int seconds = timeInSeconds;

        return new int[] {hours, minutes, seconds};
    }
}