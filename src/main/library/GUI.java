package main.library;

import java.sql.SQLException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        TableView<TrackListing> table = new TableView<>();


        try {
            TrackList trackList = new TrackList();
            trackList.buildTrackList();
        } catch (SQLException e) {
            System.err.println("Unable to build Tracklist");
            e.printStackTrace();
        }
        // ObservableList<TrackListing> tracks = trackList.getTracks();

        table.setItems(TrackList.getTracks());

        TableColumn<TrackListing,String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<TrackListing,String> durationCol = new TableColumn<>("Duration");
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));

        TableColumn<TrackListing,String> artistsCol = new TableColumn<>("Artists");
        artistsCol.setCellValueFactory(new PropertyValueFactory<>("artists"));

        TableColumn<TrackListing,String> albumsCol = new TableColumn<>("Albums");
        albumsCol.setCellValueFactory(new PropertyValueFactory<>("albums"));

        TableColumn<TrackListing,String> genresCol = new TableColumn<>("Genres");
        genresCol.setCellValueFactory(new PropertyValueFactory<>("genres"));

        table.getColumns().setAll(nameCol, durationCol, artistsCol, albumsCol, genresCol);

        VBox vbox = new VBox(table);

        Scene scene = new Scene(vbox);

        primaryStage.setScene(scene);

        primaryStage.show();
    }

}