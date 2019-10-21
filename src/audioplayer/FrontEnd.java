package audioplayer;

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


    Label message;
    


    @Override
    public void start(Stage stage) {

        // File songFile = new File("/home/bo/Music/Rick Astley/never_gonna_give_you_up.wav");
        

        // Track track = new Track(songFile);

        AudioPlayer audioPlayer = new AudioPlayer();

        // audioPlayer.queueTrack(track);


        
        message = new Label("No Track Selected");

        message.setFont(new Font(16));

        Button play = new Button("Play");
        play.setOnAction(e -> audioPlayer.play());

        Button pause = new Button("Pause");
        pause.setOnAction(e -> audioPlayer.pauseTrack());

        Button load = new Button("Load");
        load.setOnAction(e -> loadAudioFile(stage, audioPlayer));

        Button quit = new Button("Quit");
        quit.setOnAction(e -> {
            audioPlayer.quit(); 
            Platform.exit();
        });

        HBox buttonBar = new HBox(20, load, play, pause, quit);
        buttonBar.setAlignment(Pos.CENTER);
        BorderPane root = new BorderPane();
        root.setCenter(message);
        root.setBottom(buttonBar);

        Scene scene = new Scene(root, 450, 200);
        stage.setScene(scene);
        stage.setTitle("Audio Player");
        stage.show();
    }


    File getAudioFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*aiff");
        fileChooser.getExtensionFilters().add(extFilter);
        return fileChooser.showOpenDialog(stage);
    }

    void loadAudioFile(Stage stage, AudioPlayer audioPlayer) {
        File audioFile = getAudioFile(stage);
        if (audioFile != null) {
            audioPlayer.queueTrack(new Track(audioFile));
            // message.setText(audioFile.getName());
            // message.setText(audioPlayer.getCurrentTrackName());
        }
    }
  

    public static void main(String[] args) {
        launch(args);
    }
}