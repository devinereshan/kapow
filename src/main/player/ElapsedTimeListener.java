package main.player;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.Slider;

public class ElapsedTimeListener {
    private final int HOURS = 0;
    private final int MINUTES = 1;
    private final int SECONDS = 2;
    private int elapsedTimeInSeconds = 0;
    private int totalTimeInSeconds = 0;
    private SimpleStringProperty totalTime = new SimpleStringProperty();
    private SimpleStringProperty elapsedTime = new SimpleStringProperty();
    private Slider elapsedTimeSlider;

    public ElapsedTimeListener(Slider elapsedTimeSlider) {
        this.elapsedTimeSlider = elapsedTimeSlider;
    }

    public void updateElapsedTimeFields(int elapsedTime) {
        elapsedTimeInSeconds = elapsedTime;
        setElapsedTime(elapsedTimeInSeconds);
        elapsedTimeSlider.setValue((int) (elapsedTimeInSeconds / totalTimeInSeconds * 100));

    }

    // public void setTotalTimeInSeconds(double totalTimeInSeconds) {
    //     elapsedTimeSlider.setMax(totalTimeInSeconds);
    // }

    public void setNewTrackDimensions(int totalTimeInSeconds) {
        this.totalTimeInSeconds = totalTimeInSeconds;
        setTotalTime(totalTimeInSeconds);
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