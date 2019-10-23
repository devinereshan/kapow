package kapow;

import java.io.File;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FrontEnd extends Application {

    private Label currentTrackName;
    private AudioPlayer audioPlayer;

    @Override
    public void start(Stage stage) {

        audioPlayer = new AudioPlayer();

        currentTrackName = new Label("No Track Selected");

        currentTrackName.setFont(new Font(16));

        Button seekLeft = new Button("<<");
        seekLeft.setOnAction(e -> seekLeft());

        Button seekRight = new Button(">>");
        seekRight.setOnAction(e -> seekRight());

        Button play = new Button("Play");
        play.setOnAction(e -> play());

        Button pause = new Button("Pause");
        pause.setOnAction(e -> pause());

        Button load = new Button("Load");
        load.setOnAction(e -> loadAudioFile(stage));

        Button quit = new Button("Quit");
        quit.setOnAction(e -> {
            audioPlayer.quit();
            Platform.exit();
        });

        HBox buttonBar = new HBox(20, load, seekLeft, play, pause, seekRight, quit);
        buttonBar.setAlignment(Pos.CENTER);
        BorderPane root = new BorderPane();
        root.setCenter(currentTrackName);
        root.setBottom(buttonBar);

        Scene scene = new Scene(root, 500, 200);
        stage.setScene(scene);
        stage.setTitle("Kapow! - Kool Audio Player, or whatever...");
        stage.show();
    }


    private File getAudioFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.aiff", "*.au");
        fileChooser.getExtensionFilters().add(extFilter);
        return fileChooser.showOpenDialog(stage);
    }

    private void loadAudioFile(Stage stage) {
        File audioFile = getAudioFile(stage);
        if (audioFile != null) {
            audioPlayer.queueTrack(audioFile);
            audioPlayer.printQueue(); // test
            currentTrackName.setText(audioPlayer.getCurrentTrackName());
        }
    }

    private void play() {
        audioPlayer.play();
    }

    private void pause() {
        audioPlayer.pause();
    }

    private void seekLeft() {
        audioPlayer.seekLeft();
        currentTrackName.setText(audioPlayer.getCurrentTrackName());
    }

    private void seekRight() {
        audioPlayer.seekRight();
        currentTrackName.setText(audioPlayer.getCurrentTrackName());
    }


    public static void main(String[] args) {
        launch(args);
    }
}