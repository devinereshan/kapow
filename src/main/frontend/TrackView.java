package main.frontend;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.library.Track;
import main.library.TrackList;

public class TrackView {
    private final TrackList trackList;
    private TableView<Track> tableView = new TableView<>();
    // public ObservableList<Track> tracks = FXCollections.observableArrayList();



    TableColumn<Track,String> nameCol = new TableColumn<>("Name");
    TableColumn<Track,String> durationCol = new TableColumn<>("Duration");
    TableColumn<Track,String> artistsCol = new TableColumn<>("Artists");
    TableColumn<Track,String> albumsCol = new TableColumn<>("Albums");
    TableColumn<Track,String> genresCol = new TableColumn<>("Genres");


    public TrackView(TrackList trackList) {
        this.trackList = trackList;
        // tracks = trackList.tracks;
        assignColumnValues();
    }


    private void assignColumnValues() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        artistsCol.setCellValueFactory(new PropertyValueFactory<>("artists"));
        albumsCol.setCellValueFactory(new PropertyValueFactory<>("albums"));
        genresCol.setCellValueFactory(new PropertyValueFactory<>("genres"));
        tableView.getColumns().setAll(nameCol, durationCol, artistsCol, albumsCol, genresCol);
        tableView.setItems(trackList.getTracks());
    }

    public TableView<Track> getTableView() {
        return tableView;
    }

    public void deleteTrack(Track track) {
        trackList.deleteTrack(track);
    }


    // private void buildContextMenu(Stage primaryStage) {
    //     MenuItem menuPlay = new MenuItem("play");
        // menuPlay.setOnAction(new EventHandler<ActionEvent>() {

        //     @Override
        //     public void handle(ActionEvent event) {
        //         if (table.getSelectionModel().getSelectedItem() != null) {
        //             loadTrackFromTable(table.getSelectionModel().getSelectedItem());
        //         }
        //     }
        // });
    //     menuPlay.setOnAction(e -> playSelectedTrack());

    //     MenuItem importTrack = new MenuItem("import track");
    //     importTrack.setOnAction(new EventHandler<ActionEvent>() {

    //         @Override
    //         public void handle(ActionEvent event) {
    //             trackImportBox = new TrackImportBox();
    //             trackImportBox.open(primaryStage);
    //         }
    //     });

    //     MenuItem editTrack = new MenuItem("edit track");
    //     editTrack.setOnAction(new EventHandler<ActionEvent>() {

    //         @Override
    //         public void handle(ActionEvent event) {
    //             trackEditBox = new TrackEditBox(table.getSelectionModel().getSelectedItem());
    //             trackEditBox.open(primaryStage);
    //         }
    //     });

    //     MenuItem delete = new MenuItem("delete");
    //     delete.setOnAction(e -> deleteTrack(table.getSelectionModel().getSelectedItem()));

    //     contextMenu.getItems().add(menuPlay);
    //     contextMenu.getItems().add(importTrack);
    //     contextMenu.getItems().add(delete);
    //     contextMenu.getItems().add(editTrack);

    //     table.setContextMenu(contextMenu);
    // }


    // public void playSelectedTrack() {
    //     if (tableView.getSelectionModel().getSelectedItem() != null) {
    //         loadTrackFromTable(tableView.getSelectionModel().getSelectedItem());
    //     }
    // }
    // public void deleteTrack(Track trackToDelete) {
    //     trackList.deleteTrack(trackToDelete);
    // }
}