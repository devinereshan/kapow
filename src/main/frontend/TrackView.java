package main.frontend;

import java.io.IOException;
import java.sql.SQLException;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import javafx.stage.Stage;
import main.database.DBConnection;
import main.library.Album;
import main.library.Track;
import main.library.TrackList;
import main.player.AudioPlayer;

public class TrackView {
    private final TrackList trackList;
    // private Tab trackViewTab = new Tab("Tracks");
    final TableView<Track> trackViewTable = new TableView<>();
    private VBox trackViewContents;
    // private Button returnToParent;
    // private Label currentAlbumLabel = new Label("kapow!");
    private String title = "kapow!";
    private AlbumView parent;
    private ContextMenu contextMenu;
    private AudioPlayer audioPlayer;
    // private ViewHandler viewHandler;
    private static Track trackToEdit;
    private Album album;

    TableColumn<Track,String> indexCol = new TableColumn<>("#");
    TableColumn<Track,String> nameCol = new TableColumn<>("Name");
    TableColumn<Track,String> durationCol = new TableColumn<>("Duration");
    TableColumn<Track,String> artistsCol = new TableColumn<>("Artists");
    TableColumn<Track,String> albumsCol = new TableColumn<>("Albums");
    TableColumn<Track,String> genresCol = new TableColumn<>("Genres");


    public TrackView(TrackList trackList, ViewHandler viewHandler) {
        this.trackList = trackList;
        assignColumnValues();

        trackViewContents = new VBox(trackViewTable);

        trackViewTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        // buildContextMenu();
    }

    public TrackView(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        trackList = new TrackList();
        
        assignColumnValues();
        buildContextMenu();
        trackViewTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        trackViewTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }


    public TrackView(Album album, AudioPlayer audioPlayer) {
        trackList = new TrackList(album.getId(), "album");
        this.audioPlayer = audioPlayer;

        assignAlbumColumnValues();

        this.album = album;
        title = String.format("%s - %s", album.getArtists(), album.getName());
        buildContextMenu();
        trackViewTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        trackViewTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public String getTitle() {
        return title;
    }

    // private void setDoubleClick() {
    //     trackViewTable.setRowFactory(tv -> {
    //         TableRow<Track> trackRow = new TableRow<>();
    //         trackRow.setOnMouseClicked(event -> {
    //             if (event.getClickCount() == 2 && (!trackRow.isEmpty())) {
    //                 play(trackRow.getItem());
    //             }
    //         });
    //         return trackRow;
    //     });
    // }

    private void buildContextMenu() {
        contextMenu = new ContextMenu();

        MenuItem play = new MenuItem("Play");
        play.setOnAction(e -> play(trackViewTable.getSelectionModel().getSelectedItems()));

        MenuItem queue = new MenuItem("Queue");
        queue.setOnAction(e -> addToQueue(trackViewTable.getSelectionModel().getSelectedItems()));

        MenuItem queueNext = new MenuItem("Queue Next");
        queueNext.setOnAction(e -> queueNext(trackViewTable.getSelectionModel().getSelectedItems()));

        // MenuItem importAudio = new MenuItem("Import");
        // importAudio.setOnAction(e -> viewHandler.importAudio());

        MenuItem editTrack = new MenuItem("Edit Selection");
        editTrack.setOnAction(e -> editTrack(trackViewTable.getSelectionModel().getSelectedItem()));

        MenuItem delete = new MenuItem("Remove");
        delete.setOnAction(e -> deleteTrack(trackViewTable.getSelectionModel().getSelectedItems()));


        contextMenu.getItems().add(play);
        contextMenu.getItems().add(queue);
        contextMenu.getItems().add(queueNext);
        // contextMenu.getItems().add(importAudio);
        contextMenu.getItems().add(editTrack);
        contextMenu.getItems().add(delete);
        trackViewTable.setContextMenu(contextMenu);
    }


    private void editTrack(Track track) {
        if (track != null) {
            try {
                trackToEdit = track;
                Parent root = FXMLLoader.load(getClass().getResource("track_edit_box.fxml"));
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

    public static void cleanTrackToEdit() {
        trackToEdit = null;
    }

    public static Track getTrackToEdit() {
        return trackToEdit;
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


    // public Tab getTab() {
    //     return trackViewTab;
    // }

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