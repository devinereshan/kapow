package main.frontend;

// import javafx.application.Application;

// import javafx.scene.Scene;
// import javafx.scene.layout.BorderPane;
// import javafx.stage.Stage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.library.Track;


public class GUI extends Application {
    private static Stage pStage;

    public static void main(String[] args) {
        launch(args);
    }



    public void start(Stage primaryStage) throws Exception{
        pStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("main_view.fxml"));
        primaryStage.setTitle("Kapow!");
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.show();
    }

    public static void editTrack(Track track) {
        TrackEditBox trackEditBox = new TrackEditBox(track);
        trackEditBox.open(pStage);
    }
}