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
    private int previousElapsedTime = 0;
    private SimpleStringProperty totalTime = new SimpleStringProperty("--:--");
    private SimpleStringProperty elapsedTime = new SimpleStringProperty("--:--");
    private Slider elapsedTimeSlider;

    public ElapsedTimeListener(Slider elapsedTimeSlider) {
        this.elapsedTimeSlider = elapsedTimeSlider;
    }



    // public void connectSliderToPlayer(MediaPlayer mediaPlayer) {
    //     elapsedTimeSlider.valueProperty().addListener(new InvalidationListener() {
    //         public void invalidated(Observable observable) {
    //             if (elapsedTimeSlider.isPressed()) {
    //                 mediaPlayer.seek(Duration.seconds(elapsedTimeSlider.getValue()));
    //             }
    //         }
    //     });

    // }


    public void updateElapsedTimeFields(double elapsedTime) {
        if ((int) elapsedTime > previousElapsedTime) {
            setElapsedTime((int) elapsedTime);
            previousElapsedTime = (int) elapsedTime;
        }

        // This is where the memory leak is. Updating the slider value.
        // elapsedTimeSlider.setValue(elapsedTime);
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