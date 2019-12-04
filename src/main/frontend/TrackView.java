package main.frontend;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.library.Track;
import main.library.TrackList;

public class TrackView {
    private final TrackList trackList;
    private TableView<Track> tableView = new TableView<>();

    TableColumn<Track,String> nameCol = new TableColumn<>("Name");
    TableColumn<Track,String> durationCol = new TableColumn<>("Duration");
    TableColumn<Track,String> artistsCol = new TableColumn<>("Artists");
    TableColumn<Track,String> albumsCol = new TableColumn<>("Albums");
    TableColumn<Track,String> genresCol = new TableColumn<>("Genres");


    public TrackView(TrackList trackList, AudioPlayerView audioPlayerView) {
        this.trackList = trackList;
        assignColumnValues();

        tableView.setRowFactory(tv -> {
            TableRow<Track> track = new TableRow<>();
            track.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!track.isEmpty())) {
                    audioPlayerView.loadTrackFromTable(track.getItem());
                }
            });
            return track;
        });
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
}