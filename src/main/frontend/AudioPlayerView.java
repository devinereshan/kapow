package main.frontend;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import main.library.Track;
import main.player.AudioPlayer;
import main.player.ElapsedTimeListener;

public class AudioPlayerView {
    private AudioPlayer audioPlayer;
    private Label currentTrackName;
    private ElapsedTimeListener elapsedTimeListener;

    private final Button seekLeft = new Button("<<");
    private final Button seekRight = new Button(">>");
    private final Button stopTrack = new Button("Stop");
    private final Button play = new Button("Play");
    private final Button pause = new Button("Pause");

    // Slider elapsedTimeBar = new Slider(0, 1, 0);
    private Label elapsedTime = new Label("--:--");
    private Label totalTime = new Label("--:--");

    private HBox trackName;
    private HBox timeBox;
    private HBox buttonBar;
    VBox mainContainer;

    public AudioPlayerView() {
        // elapsedTimeListener = new ElapsedTimeListener(elapsedTimeBar);
        elapsedTimeListener = new ElapsedTimeListener();
        audioPlayer = new AudioPlayer(elapsedTimeListener);
        elapsedTime.textProperty().bind(elapsedTimeListener.elapsedTimeProperty());
        totalTime.textProperty().bind(elapsedTimeListener.totalTimeProperty());

        currentTrackName = new Label("No Track Selected");
        currentTrackName.setFont(new Font(16));
        mapButtons();

        trackName = new HBox(20, currentTrackName);
        trackName.setAlignment(Pos.CENTER);

        // timeBox = new HBox(elapsedTime, elapsedTimeBar, totalTime);
        timeBox = new HBox(elapsedTime, totalTime);
        timeBox.setAlignment(Pos.CENTER);

        buttonBar = new HBox(20, seekLeft, stopTrack, play, pause, seekRight);
        buttonBar.setAlignment(Pos.CENTER);
        buttonBar.setPadding(new Insets(10));

        mainContainer = new VBox(trackName, timeBox, buttonBar);

    }


    public VBox getMainContainer() {
        return mainContainer;
    }

    private void mapButtons() {
        seekLeft.setOnAction(e -> seekLeft());
        seekRight.setOnAction(e -> seekRight());
        stopTrack.setOnAction(e -> stopTrack());
        pause.setOnAction(e -> pause());
        play.setOnAction(e -> play());
    }


    private void stopTrack() {
        audioPlayer.stop();
    }

    private void play() {
        audioPlayer.play();
    }

    private void pause() {
        audioPlayer.pause();
    }

    private void seekLeft() {
        audioPlayer.seekLeft();
        updatecurrentTrackName(audioPlayer.getCurrentTrack());
    }

    private void seekRight() {
        audioPlayer.seekRight();
        updatecurrentTrackName(audioPlayer.getCurrentTrack());
    }

    public void queueAndPlay(Track track) {
        audioPlayer.queueAndPlay(track);
        currentTrackName.setText(track.getName() + " - " + track.getArtists());
    }

    public void queueAndPlay(ObservableList<Track> tracks) {
        audioPlayer.queueAndPlay(tracks);
        currentTrackName.setText(audioPlayer.getCurrentTrack().getName() + " - " + audioPlayer.getCurrentTrack().getArtists());
    }

    public void queue(Track track) {
        audioPlayer.queue(track);
    }

    public void queue(ObservableList<Track> tracks) {
        audioPlayer.queue(tracks);
    }

    // public void queueNext(Track track) {
    //     audioPlayer.queueNext(track);
    // }

    // public void queueNext(ObservableList<Track> tracks) {
    //     audioPlayer.queueNext(tracks);
    // }

    private void updatecurrentTrackName(Track track) {
        if (track != null) {
            currentTrackName.setText(track.getName() + " - " + track.getArtists());
        }
    }
}