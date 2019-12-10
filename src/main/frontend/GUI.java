package main.frontend;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class GUI extends Application {
    ViewHandler viewHandler;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {
        viewHandler = new ViewHandler(primaryStage);

        BorderPane root = new BorderPane();

        root.setTop(viewHandler.getAudioPlayerView().getMainContainer());
        root.setCenter(viewHandler.views);

        Scene scene = new Scene(root, 600, 700);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Kapow! - Kool Audio Player, or whatever...");
        primaryStage.show();
    }
}