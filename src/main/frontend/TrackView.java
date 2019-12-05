package main.frontend;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import main.library.Track;
import main.library.TrackList;

public class TrackView {
    private final TrackList trackList;
    private Tab trackViewTab = new Tab("Tracks");
    private TableView<Track> trackViewTable = new TableView<>();
    private VBox trackViewContents;
    // private AlbumView parent;
    private Button returnToParent;
    private Label currentAlbumLabel;

    TableColumn<Track,String> nameCol = new TableColumn<>("Name");
    TableColumn<Track,String> durationCol = new TableColumn<>("Duration");
    TableColumn<Track,String> artistsCol = new TableColumn<>("Artists");
    TableColumn<Track,String> albumsCol = new TableColumn<>("Albums");
    TableColumn<Track,String> genresCol = new TableColumn<>("Genres");


    public TrackView(TrackList trackList, AudioPlayerView audioPlayerView) {
        this.trackList = trackList;
        assignColumnValues();

        trackViewContents = new VBox(trackViewTable);
        trackViewTab.setContent(trackViewContents);

        trackViewTable.setRowFactory(tv -> {
            TableRow<Track> trackRow = new TableRow<>();
            trackRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!trackRow.isEmpty())) {
                    audioPlayerView.loadTrackFromTable(trackRow.getItem());
                }
            });
            return trackRow;
        });
    }

    // public TrackView(TrackList trackList, AudioPlayerView audioPlayerView, AlbumView parent, String parentName) {
    //     this.trackList = trackList;
    //     assignColumnValues();
    //     this.parent = parent;

    //     returnToParent = new Button("Back To Album");
    //     currentAlbumLabel = new Label(parentName);
    //     trackViewContents = new VBox(returnToParent, currentAlbumLabel, trackViewTable);
    //     trackViewTab.setContent(trackViewContents);

    //     returnToParent.setOnAction(e -> parent.restoreMainView());
    //     trackViewTable.setRowFactory(tv -> {
    //         TableRow<Track> trackRow = new TableRow<>();
    //         trackRow.setOnMouseClicked(event -> {
    //             if (event.getClickCount() == 2 && (!trackRow.isEmpty())) {
    //                 audioPlayerView.loadTrackFromTable(trackRow.getItem());
    //             }
    //         });
    //         return trackRow;
    //     });
    // }

    // Constructor for nested TrackView
    public TrackView(TrackList trackList, ViewHandler viewHandler, String parentName) {
        this.trackList = trackList;
        assignColumnValues();
        // this.parent = parent;

        returnToParent = new Button("Back To Album");
        currentAlbumLabel = new Label(parentName);
        trackViewContents = new VBox(returnToParent, currentAlbumLabel, trackViewTable);
        trackViewTab.setContent(trackViewContents);

        returnToParent.setOnAction(e -> viewHandler.returnToParent(this));
        trackViewTable.setRowFactory(tv -> {
            TableRow<Track> trackRow = new TableRow<>();
            trackRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!trackRow.isEmpty())) {
                    // audioPlayerView.loadTrackFromTable(trackRow.getItem());
                }
            });
            return trackRow;
        });
    }


    public VBox getContents() {
        return trackViewContents;
    }


    public Tab getTab() {
        return trackViewTab;
    }

    private void assignColumnValues() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        artistsCol.setCellValueFactory(new PropertyValueFactory<>("artists"));
        albumsCol.setCellValueFactory(new PropertyValueFactory<>("albums"));
        genresCol.setCellValueFactory(new PropertyValueFactory<>("genres"));
        trackViewTable.getColumns().setAll(nameCol, durationCol, artistsCol, albumsCol, genresCol);
        trackViewTable.setItems(trackList.getTracks());
    }

    public TableView<Track> getTrackViewTable() {
        return trackViewTable;
    }

    public void deleteTrack(Track trackRow) {
        trackList.deleteTrack(trackRow);
    }
}