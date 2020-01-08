package main.frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.database.DBBuilder;


public class GUI extends Application {
    private static Stage pStage;
    public static final String databaseDir = ".database";
    public static final String databasePath = databaseDir + "/music.db";

    public static void main(String[] args) {
        boolean success = DBBuilder.dbExists();

        if (!success) {
            success = DBBuilder.buildDatabase();
        }

        if (success) {
            launch(args);
        } else {
            System.err.println("Something went wrong while launching kapow. Unable to access database");
        }
    }

    public void start(Stage primaryStage) throws Exception{
        pStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("main_view.fxml"));
        primaryStage.setTitle("Kapow!");
        Scene scene = new Scene(root, 1200, 800);
        scene.getStylesheets().add("main/frontend/style.css");
        // primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static Stage getpStage() {
        return pStage;
    }
}