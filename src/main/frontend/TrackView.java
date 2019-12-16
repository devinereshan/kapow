package main.frontend;

import java.io.IOException;
import java.sql.SQLException;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.database.DBConnection;
import main.library.Album;
import main.library.Track;
import main.library.TrackList;
import main.player.AudioPlayer;

public class TrackView {
    private final TrackList trackList;
    final TableView<Track> trackViewTable = new TableView<>();
    private VBox trackViewContents;
    private String title = "kapow!";
    private AlbumView parent;
    private ContextMenu contextMenu;
    private AudioPlayer audioPlayer;
    private Album album;

    private FilteredList<Track> filteredTracks;

    TableColumn<Track,String> indexCol = new TableColumn<>("#");
    TableColumn<Track,String> nameCol = new TableColumn<>("Track Name");
    TableColumn<Track,String> durationCol = new TableColumn<>("Duration");
    TableColumn<Track,String> artistsCol = new TableColumn<>("Artist Name");
    TableColumn<Track,String> albumsCol = new TableColumn<>("Album");
    TableColumn<Track,String> genresCol = new TableColumn<>("Genre");


    public TrackView(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        trackList = new TrackList();
        filteredTracks = new FilteredList(trackList.getTracks(), p -> true);

        assignColumnValues();
        buildContextMenu();
        trackViewTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        trackViewTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }


    public TrackView(Album album, AudioPlayer audioPlayer) {
        trackList = new TrackList(album.getId(), "album");
        this.audioPlayer = audioPlayer;

        filteredTracks = new FilteredList(trackList.getTracks(), p -> true);
        assignAlbumColumnValues();

        this.album = album;
        title = String.format("%s - %s", album.getArtists(), album.getName());
        buildContextMenu();
        trackViewTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        indexCol.setMinWidth(20);
        indexCol.setMaxWidth(50);
        trackViewTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public String getTitle() {
        return title;
    }

    private void buildContextMenu() {
        contextMenu = new ContextMenu();

        MenuItem play = new MenuItem("Play");
        play.setOnAction(e -> play(trackViewTable.getSelectionModel().getSelectedItems()));

        MenuItem queue = new MenuItem("Queue");
        queue.setOnAction(e -> addToQueue(trackViewTable.getSelectionModel().getSelectedItems()));

        MenuItem queueNext = new MenuItem("Queue Next");
        queueNext.setOnAction(e -> queueNext(trackViewTable.getSelectionModel().getSelectedItems()));

        MenuItem importAudio = new MenuItem("Import");
        importAudio.setOnAction(e -> importAudio());

        MenuItem editTrack = new MenuItem("Edit Selection");
        editTrack.setOnAction(e -> editTrack(trackViewTable.getSelectionModel().getSelectedItem()));

        MenuItem delete = new MenuItem("Remove");
        delete.setOnAction(e -> deleteTrack(trackViewTable.getSelectionModel().getSelectedItems()));


        contextMenu.getItems().add(play);
        contextMenu.getItems().add(queue);
        contextMenu.getItems().add(queueNext);
        contextMenu.getItems().add(importAudio);
        contextMenu.getItems().add(editTrack);
        contextMenu.getItems().add(delete);
        trackViewTable.setContextMenu(contextMenu);
    }

    private void importAudio() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("import_box.fxml"));
            Parent root = (Parent)fxmlLoader.load();
            ImportController controller = fxmlLoader.<ImportController>getController();

            Stage popup = new Stage();
            popup.setTitle("Edit Track Information");
            popup.setScene(new Scene(root));
            popup.show();
        } catch (IOException e) {
            System.err.println("TrackView: Unable to open track edit box");
            e.printStackTrace();
        }
    }


    private void editTrack(Track track) {
        if (track != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("track_edit_box.fxml"));
                Parent root = (Parent)fxmlLoader.load();
                TrackEditController controller = fxmlLoader.<TrackEditController>getController();
                controller.setTrackToEdit(track);
                controller.setFields();

                Stage popup = new Stage();
                popup.setTitle("Edit Track Information");
                popup.setScene(new Scene(root));
                popup.show();
            } catch (IOException e) {
                System.err.println("TrackView: Unable to open track edit box");
                e.printStackTrace();
            }
        }
    }

    private void deleteTrack(ObservableList<Track> tracksToDelete) {
        if (tracksToDelete != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("confirm_delete.fxml"));     

                Parent root = (Parent)fxmlLoader.load();          
                ConfirmDeleteController controller = fxmlLoader.<ConfirmDeleteController>getController();
                controller.setTracksToDelete(tracksToDelete);
                controller.setFields();

                Stage popup = new Stage();
                popup.setTitle("Remove Track Confirmation");
                popup.setScene(new Scene(root));
                popup.show();
            } catch (IOException e) {
                System.err.println("TrackView: Unable to open track confirm delete box");
                e.printStackTrace();
            }
        }
    }

    public void updateTitle() {
        if (album != null) {
            try (DBConnection connection = new DBConnection()) {
                album = connection.getAlbum(album.getId());
                title = String.format("%s - %s", album.getArtists(), album.getName());
            } catch (SQLException e) {
                System.err.println("TrackView: Unable to update album title");
                e.printStackTrace();
            }
        }
    }

    public Album getAlbum() {
        return album;
    }

    private void play(ObservableList<Track> tracks) {
        if (tracks != null) {
            audioPlayer.queueAndPlay(tracks);
        }
    }

    private void addToQueue(ObservableList<Track> tracks) {
        if (tracks != null) {
            audioPlayer.queue(tracks);
        }
    }

    private void queueNext(ObservableList<Track> tracks) {
        if (tracks != null) {
            audioPlayer.queueNext(tracks);
        }
    }

    public VBox getContents() {
        return trackViewContents;
    }


    private void assignColumnValues() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        artistsCol.setCellValueFactory(new PropertyValueFactory<>("artists"));
        albumsCol.setCellValueFactory(new PropertyValueFactory<>("albums"));
        genresCol.setCellValueFactory(new PropertyValueFactory<>("genres"));
        trackViewTable.getColumns().setAll(nameCol, durationCol, artistsCol, albumsCol, genresCol);
        trackViewTable.setItems(filteredTracks);
    }


    private void assignAlbumColumnValues() {
        indexCol.setCellValueFactory(new PropertyValueFactory<>("indexInAlbum"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        artistsCol.setCellValueFactory(new PropertyValueFactory<>("artists"));
        albumsCol.setCellValueFactory(new PropertyValueFactory<>("albums"));
        genresCol.setCellValueFactory(new PropertyValueFactory<>("genres"));
        trackViewTable.getColumns().setAll(indexCol, nameCol, durationCol, artistsCol, albumsCol, genresCol);
        trackViewTable.setItems(filteredTracks);
    }


    public void filter(TextField searchBox) {
        filteredTracks.setPredicate(p -> p.getSearchString().toLowerCase().contains(searchBox.getText().toLowerCase().trim()));
    }

    public AlbumView getParent() {
        return parent;
    }

    public TableView<Track> getTrackViewTable() {
        return trackViewTable;
    }

    public TrackList getList() {
        return trackList;
    }
}