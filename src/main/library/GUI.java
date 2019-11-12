package main.library;

import java.sql.SQLException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GUI extends Application {
    private TableView<TrackRow> table = new TableView<>();
    private final ObservableList<TrackRow> trackRows = FXCollections.observableArrayList();
    private final TrackRowList trackRowList = new TrackRowList();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        TableColumn<TrackRow,String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<TrackRow,String> durationCol = new TableColumn<>("Duration");
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));

        TableColumn<TrackRow,String> artistsCol = new TableColumn<>("Artists");
        artistsCol.setCellValueFactory(new PropertyValueFactory<>("artists"));

        TableColumn<TrackRow,String> albumsCol = new TableColumn<>("Albums");
        albumsCol.setCellValueFactory(new PropertyValueFactory<>("albums"));

        TableColumn<TrackRow,String> genresCol = new TableColumn<>("Genres");
        genresCol.setCellValueFactory(new PropertyValueFactory<>("genres"));

        table.getColumns().setAll(nameCol, durationCol, artistsCol, albumsCol, genresCol);
        table.setItems(trackRows);


        VBox vbox = new VBox(table);

        Scene scene = new Scene(vbox);

        primaryStage.setScene(scene);

        primaryStage.show();

        try {
            while (trackRowList.hasMoreTracks()) {
                trackRows.add(trackRowList.getNextTrackRow());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}