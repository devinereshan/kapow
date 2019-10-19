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
import javafx.stage.Stage;

public class FrontEnd extends Application {



    public void start(Stage stage) {

        File songFile = new File("/home/bo/Music/Rick Astley/never_gonna_give_you_up.wav");
        

        Track track = new Track(songFile);

        AudioPlayer audioPlayer = new AudioPlayer();

        audioPlayer.queueTrack(track);


        
        Label message = new Label("Audio Player");

        message.setFont(new Font(40));

        Button play = new Button("Play");
        play.setOnAction(e -> audioPlayer.play());

        Button pause = new Button("Pause");
        pause.setOnAction(e -> audioPlayer.pauseTrack());

        Button quit = new Button("Quit");
        quit.setOnAction(e -> {
            audioPlayer.quit(); 
            Platform.exit();
        });

        HBox buttonBar = new HBox(20, play, pause, quit);
        buttonBar.setAlignment(Pos.CENTER);
        BorderPane root = new BorderPane();
        root.setCenter(message);
        root.setBottom(buttonBar);

        Scene scene = new Scene(root, 450, 200);
        stage.setScene(scene);
        stage.setTitle("Audio Player");
        stage.show();
    }

  

    public static void main(String[] args) {
        launch(args);
    }
}