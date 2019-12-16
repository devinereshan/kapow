package main.player;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Slider;

public class ElapsedTimeListener {
    private int elapsedHours;
    private int elapsedMinutes;
    private int elapsedSeconds;
    private int totalHours;
    private int totalMinutes;
    private int totalSeconds;
    private double maxElapsedTime;
    private int previousElapsedTime = 0;
    private SimpleStringProperty totalTime = new SimpleStringProperty("00:00");
    private SimpleStringProperty elapsedTime = new SimpleStringProperty("00:00");
    private Slider elapsedTimeSlider;

    public ElapsedTimeListener(Slider elapsedTimeSlider) {
        this.elapsedTimeSlider = elapsedTimeSlider;
    }

    public ElapsedTimeListener() {}

    public void resetElapsedTime() {
        setElapsedTime(0);
        previousElapsedTime = 0;
    }

    public void updateElapsedTimeFields(double elapsedTime) {
        elapsedTimeSlider.setValue(elapsedTime / maxElapsedTime * 100);

        if ((int) elapsedTime > previousElapsedTime) {
            setElapsedTime((int) elapsedTime);
            previousElapsedTime = (int) elapsedTime;
        }

    }


    public void setNewTrackDimensions(double totalTimeInSeconds) {
        maxElapsedTime = totalTimeInSeconds;
        setTotalTime((int) totalTimeInSeconds);
        resetElapsedTime();
        elapsedTimeSlider.setValue(0);
    }


    public void setElapsedTime(String time) {
        this.elapsedTime.set(time);
    }


    public void setTotalTime(String time) {
        this.totalTime.set(time);
    }

    public double getMaxElapsedTime() {
        return maxElapsedTime;
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
        totalHours = timeInSeconds / 3600;
        timeInSeconds %= 3600;
        totalMinutes = timeInSeconds / 60;
        timeInSeconds %= 60;
        totalSeconds = timeInSeconds;

        if (totalHours > 0) {
            setTotalTime(String.format("%02d:%02d:%02d", totalHours, totalMinutes, totalSeconds));
        } else {
            setTotalTime(String.format("%02d:%02d", totalMinutes, totalSeconds));
        }
    }

    private void setElapsedTime(int timeInSeconds) {
        elapsedHours = timeInSeconds / 3600;
        timeInSeconds %= 3600;
        elapsedMinutes = timeInSeconds / 60;
        timeInSeconds %= 60;
        elapsedSeconds = timeInSeconds;

        if (elapsedHours > 0) {
            setElapsedTime(String.format("%02d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds));
        } else {
            setElapsedTime(String.format("%02d:%02d", elapsedMinutes, elapsedSeconds));
        }
    }
}