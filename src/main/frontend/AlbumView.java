package main.frontend;

import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import main.database.DBConnection;
import main.library.Album;
import main.library.AlbumList;
import main.library.Artist;
import main.library.Track;
import main.library.TrackList;
import main.player.AudioPlayer;

public class AlbumView {
    private AlbumList albumList;
    private VBox albumViewContents;
    String title = "kapow!";
    final TableView<Album> albumViewTable = new TableView<>();
    TableColumn<Album,String> nameCol = new TableColumn<>("Name");
    TableColumn<Album,String> artistsCol = new TableColumn<>("Artists");
    TableColumn<Album,String> numberOfTracksCol = new TableColumn<>("Tracks");
    TableColumn<Album,String> genresCol = new TableColumn<>("Genres");
    private Artist artist;
    private FilteredList<Album> filteredAlbums;
    private ContextMenu contextMenu;
    private AudioPlayer audioPlayer;


    public AlbumView(AudioPlayer audioPlayer) {
        albumList = new AlbumList();
        filteredAlbums = new FilteredList<>(albumList.getAlbums(), p -> true);
        this.audioPlayer = audioPlayer;
        assignColumnValues();
        buildContextMenu();
        albumViewTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        albumViewTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }


    public AlbumView(Artist artist, AudioPlayer audioPlayer) {
        albumList = new AlbumList(artist.getId());
        filteredAlbums = new FilteredList<>(albumList.getAlbums(), p -> true);
        this.audioPlayer = audioPlayer;
        assignColumnValues();
        buildContextMenu();
        this.artist = artist;
        title = artist.getName();
        albumViewTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        albumViewTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }


    private void assignColumnValues() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        artistsCol.setCellValueFactory(new PropertyValueFactory<>("artists"));
        numberOfTracksCol.setCellValueFactory(new PropertyValueFactory<>("numberOfTracks"));
        genresCol.setCellValueFactory(new PropertyValueFactory<>("genres"));
        albumViewTable.getColumns().setAll(nameCol, artistsCol, numberOfTracksCol, genresCol);
        albumViewTable.setItems(filteredAlbums);
    }

    private void buildContextMenu() {
        contextMenu = new ContextMenu();

        MenuItem play = new MenuItem("Play");
        play.setOnAction(e -> play(albumViewTable.getSelectionModel().getSelectedItems()));

        MenuItem queue = new MenuItem("Queue");
        queue.setOnAction(e -> addToQueue(albumViewTable.getSelectionModel().getSelectedItems()));

        MenuItem queueNext = new MenuItem("Queue Next");
        queueNext.setOnAction(e -> queueNext(albumViewTable.getSelectionModel().getSelectedItems()));

        contextMenu.getItems().add(play);
        contextMenu.getItems().add(queue);
        contextMenu.getItems().add(queueNext);
        albumViewTable.setContextMenu(contextMenu);
    }

    private void play(ObservableList<Album> albums) {
        // TODO:
        if (albums != null) {
            ObservableList<Track> tracks = FXCollections.observableArrayList();
            for (Album album : albums) {
                TrackList partialTrackList = new TrackList(album.getId(), "album");
                tracks.addAll(partialTrackList.getTracks());
            }
            
            if (tracks != null) {
                audioPlayer.queueAndPlay(tracks);
            }
        }
    }

    private void addToQueue(ObservableList<Album> albums) {
        if (albums != null) {
            ObservableList<Track> tracks = FXCollections.observableArrayList();
            for (Album album : albums) {
                TrackList partialTrackList = new TrackList(album.getId(), "album");
                tracks.addAll(partialTrackList.getTracks());
            }

            if (tracks != null) {
                audioPlayer.queue(tracks);
            }
        }
    }

    private void queueNext(ObservableList<Album> albums) {
        if (albums != null) {
            ObservableList<Track> tracks = FXCollections.observableArrayList();
            for (Album album : albums) {
                TrackList partialTrackList = new TrackList(album.getId(), "album");
                tracks.addAll(partialTrackList.getTracks());
            }

            if (tracks != null) {
                audioPlayer.queueNext(tracks);
            }
        }
    }


    public void filter(TextField searchBox) {
        filteredAlbums.setPredicate(p -> p.getSearchString().toLowerCase().contains(searchBox.getText().toLowerCase().trim()));
    }

    public String getTitle() {
        return title;
    }


    public void updateTitle() {
        if (artist != null) {
            try (DBConnection connection = new DBConnection()) {
                artist = connection.getArtist(artist.getId());
                title = artist.getName();
            } catch (SQLException e) {
                System.err.println("TrackView: Unable to update artist title");
                e.printStackTrace();
            }
        }
    }


    public TableView<Album> getTableView() {
        return albumViewTable;
    }


    public VBox getContents() {
        return (albumViewContents);
    }

    public AlbumList getList() {
        return albumList;
    }
}