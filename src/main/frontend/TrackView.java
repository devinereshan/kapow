package main.frontend;

import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
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
    private Button returnToParent;
    private Label currentAlbumLabel;
    private ContextMenu contextMenu;
    private ViewHandler viewHandler;

    TableColumn<Track,String> indexCol = new TableColumn<>("#");
    TableColumn<Track,String> nameCol = new TableColumn<>("Name");
    TableColumn<Track,String> durationCol = new TableColumn<>("Duration");
    TableColumn<Track,String> artistsCol = new TableColumn<>("Artists");
    TableColumn<Track,String> albumsCol = new TableColumn<>("Albums");
    TableColumn<Track,String> genresCol = new TableColumn<>("Genres");


    public TrackView(TrackList trackList, ViewHandler viewHandler) {
        this.trackList = trackList;
        this.viewHandler = viewHandler;
        assignColumnValues();

        trackViewContents = new VBox(trackViewTable);
        trackViewTab.setContent(trackViewContents);

        trackViewTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setDoubleClick();
        buildContextMenu();
    }


    // Constructor for nested TrackView
    public TrackView(TrackList trackList, ViewHandler viewHandler, String parentName) {
        this.trackList = trackList;
        this.viewHandler = viewHandler;
        assignAlbumColumnValues();

        returnToParent = new Button("Back To Album");
        currentAlbumLabel = new Label(parentName);
        trackViewContents = new VBox(returnToParent, currentAlbumLabel, trackViewTable);
        trackViewTab.setContent(trackViewContents);

        returnToParent.setOnAction(e -> viewHandler.returnToParent(this));
        setDoubleClick();
        buildContextMenu();
    }

    private void setDoubleClick() {
        trackViewTable.setRowFactory(tv -> {
            TableRow<Track> trackRow = new TableRow<>();
            trackRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!trackRow.isEmpty())) {
                    play(trackRow.getItem());
                }
            });
            return trackRow;
        });
    }

    private void buildContextMenu() {
        contextMenu = new ContextMenu();

        MenuItem play = new MenuItem("Play");
        play.setOnAction(e -> play(trackViewTable.getSelectionModel().getSelectedItems()));

        MenuItem queue = new MenuItem("Add to queue");
        queue.setOnAction(e -> addToQueue(trackViewTable.getSelectionModel().getSelectedItems()));

        MenuItem importAudio = new MenuItem("Import");
        importAudio.setOnAction(e -> viewHandler.importAudio());

        MenuItem editTrack = new MenuItem("Edit track");
        editTrack.setOnAction(e -> viewHandler.editTrack(trackViewTable.getSelectionModel().getSelectedItem()));

        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(e -> viewHandler.deleteTrack(trackViewTable.getSelectionModel().getSelectedItem()));


        contextMenu.getItems().add(play);
        contextMenu.getItems().add(queue);
        contextMenu.getItems().add(importAudio);
        contextMenu.getItems().add(editTrack);
        contextMenu.getItems().add(delete);
        trackViewTable.setContextMenu(contextMenu);
    }

    private void play(Track track) {
        if (track != null) {
            viewHandler.queueAndPlay(track);
        }
    }

    private void play(ObservableList<Track> tracks) {
        if (tracks != null) {
            viewHandler.queueAndPlay(tracks);
        }
    }

    private void addToQueue(ObservableList<Track> tracks) {
        if (tracks != null) {
            viewHandler.queue(tracks);
        }
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


    private void assignAlbumColumnValues() {
        indexCol.setCellValueFactory(new PropertyValueFactory<>("indexInAlbum"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        artistsCol.setCellValueFactory(new PropertyValueFactory<>("artists"));
        albumsCol.setCellValueFactory(new PropertyValueFactory<>("albums"));
        genresCol.setCellValueFactory(new PropertyValueFactory<>("genres"));
        trackViewTable.getColumns().setAll(indexCol, nameCol, durationCol, artistsCol, albumsCol, genresCol);
        trackViewTable.setItems(trackList.getTracks());
    }


    public TableView<Track> getTrackViewTable() {
        return trackViewTable;
    }

    public TrackList getList() {
        return trackList;
    }
}