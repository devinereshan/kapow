package main.frontend;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextField;
// import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
// import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import main.database.DBConnection;
import main.library.Album;
import main.library.Artist;
import main.library.MediaListHandler;
import main.library.Track;
import main.player.AudioPlayer;
import main.player.ElapsedTimeListener;

public class FxmlController implements Initializable {

    @FXML
    private BorderPane libraryPlayerPane;

    @FXML
    private Slider elapsedTimeBar;

    @FXML
    private Slider volumeBar;

    @FXML
    private TextField searchBox;

    @FXML
    private Button backButton;

    @FXML
    private Label title;

    @FXML
    private Label currentTrackLabel;

    @FXML
    private Label elapsedTimeLabel;

    @FXML
    private Label totalTimeLabel;

    private final ArtistView artistView = new ArtistView();
    private final AlbumView albumView = new AlbumView();
    private TrackView trackView;
    private AlbumView nestedAlbumView;
    private TrackView nestedTrackView;
    // private TrackView currentTrackView;
    // private AlbumView currentAlbumView;
    private ElapsedTimeListener elapsedTimeListener;
    private AudioPlayer audioPlayer;
    // private final MediaListHandler mediaListHandler = new MediaListHandler();
    // private ContextMenu artistContextMenu = new ContextMenu();
    // private ContextMenu albumContextMenu = new ContextMenu();
    // private ContextMenu trackContextMenu;

    @FXML
    void albumsClicked(ActionEvent event) {
        libraryPlayerPane.setCenter(albumView.albumViewTable);
        clearNestedViews();
        disableBackButton();
        // currentAlbumView = albumView;
        title.setText(albumView.getTitle());
    }

    @FXML
    void artistsClicked(ActionEvent event) {
        libraryPlayerPane.setCenter(artistView.artistViewTable);
        clearNestedViews();
        disableBackButton();
        title.setText(albumView.getTitle());
    }

    @FXML
    void tracksClicked(ActionEvent event) {
        libraryPlayerPane.setCenter(trackView.trackViewTable);
        clearNestedViews();
        disableBackButton();
        // currentTrackView = trackView;
        title.setText(albumView.getTitle());
        // trackView.trackViewTable.setContextMenu(trackContextMenu);
    }


    @FXML
    void backClicked(ActionEvent event) {
        if (nestedTrackView != null && nestedAlbumView != null) {
            libraryPlayerPane.setCenter(nestedAlbumView.albumViewTable);
            title.setText(nestedAlbumView.getTitle());
            nestedTrackView = null;
            MediaListHandler.nullifyNestedTrackList();
        } else if (nestedTrackView != null && nestedAlbumView == null) {
            libraryPlayerPane.setCenter(albumView.albumViewTable);
            title.setText(albumView.getTitle());
            nestedTrackView = null;
            MediaListHandler.nullifyNestedTrackList();
            disableBackButton();
        } else if (nestedTrackView == null && nestedAlbumView != null) {
            libraryPlayerPane.setCenter(artistView.artistViewTable);
            title.setText(artistView.getTitle());
            nestedAlbumView = null;
            MediaListHandler.nullifyNestedAlbumList();
            disableBackButton();
        }
    }

    @FXML
    void importClicked(ActionEvent event) {

    }

    @FXML
    void pauseClicked(ActionEvent event) {
        audioPlayer.pause();
    }

    @FXML
    void playClicked(ActionEvent event) {
        audioPlayer.play();
    }

    @FXML
    void searchTermEntered(KeyEvent event) {

    }

    @FXML
    void seekLeftClicked(ActionEvent event) {
        audioPlayer.seekLeft();
    }

    @FXML
    void seekRightClicked(ActionEvent event) {
        audioPlayer.seekRight();
    }

    @FXML
    void stopClicked(ActionEvent event) {
        audioPlayer.stop();
    }

    @FXML
    void volumeScrolled(ScrollEvent event) {

    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        System.out.println("Loading user data");
        elapsedTimeListener = new ElapsedTimeListener(elapsedTimeBar);
        audioPlayer = new AudioPlayer(elapsedTimeListener);
        trackView = new TrackView(audioPlayer);

        // currentTrackView = trackView;
        // currentAlbumView = albumView;
        MediaListHandler.setMainTrackList(trackView.getList());
        MediaListHandler.setMainAlbumList(albumView.getList());
        MediaListHandler.setMainArtistList(artistView.getList());

        libraryPlayerPane.setCenter(artistView.artistViewTable);
        setDoubleClicks();
        connectSliderToPlayer();

        elapsedTimeLabel.textProperty().bind(elapsedTimeListener.elapsedTimeProperty());
        totalTimeLabel.textProperty().bind(elapsedTimeListener.totalTimeProperty());
        currentTrackLabel.textProperty().bind(audioPlayer.currentTrackNameProperty());

        System.out.println("Made is here");
        // setContextMenus();
    }

    private void setDoubleClicks() {
        setTrackViewDoubleClick(trackView);
        setAlbumViewDoubleClick(albumView);
        setArtistViewDoubleClick(artistView);
    }

    private void setTrackViewDoubleClick(TrackView view) {
        view.trackViewTable.setRowFactory(tv -> {
            TableRow<Track> trackRow = new TableRow<>();
            trackRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!trackRow.isEmpty())) {
                    queueAndPlay(trackRow.getItem());
                }
            });
            return trackRow;
        });
    }

    private void setAlbumViewDoubleClick(AlbumView view) {
        view.albumViewTable.setRowFactory(tv -> {
            TableRow<Album> albumRow = new TableRow<>();
            albumRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!albumRow.isEmpty())) {
                    switchToNestedTrackView(albumRow.getItem());
                }
            });
            return albumRow;
        });
    }

    private void setArtistViewDoubleClick(ArtistView view) {
        view.artistViewTable.setRowFactory(tv -> {
            TableRow<Artist> artistRow = new TableRow<>();
            artistRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!artistRow.isEmpty())) {
                    switchToNestedAlbumView(artistRow.getItem());
                }
            });
            return artistRow;
        });
    }

    public void connectSliderToPlayer() {
        elapsedTimeBar.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable observable) {
                if (elapsedTimeBar.isPressed()) {
                    audioPlayer.fineSeek(Duration
                            .seconds(elapsedTimeBar.getValue() * elapsedTimeListener.getMaxElapsedTime() / 100));
                }
            }
        });

    }

    private void queueAndPlay(Track track) {
        if (track != null) {
            audioPlayer.queueAndPlay(track);
        }
    }

    private void switchToNestedTrackView(Album album) {
        nestedTrackView = new TrackView(album, audioPlayer);
        libraryPlayerPane.setCenter(nestedTrackView.trackViewTable);
        setTrackViewDoubleClick(nestedTrackView);
        // currentTrackView = nestedTrackView;
        MediaListHandler.setNestedTrackList(nestedTrackView.getList());
        title.setText(nestedTrackView.getTitle());
        backButton.setText("Back");
        backButton.setDisable(false);
    }

    private void switchToNestedAlbumView(Artist artist) {
        nestedAlbumView = new AlbumView(artist);
        libraryPlayerPane.setCenter(nestedAlbumView.albumViewTable);
        setAlbumViewDoubleClick(nestedAlbumView);
        // currentAlbumView = nestedAlbumView;
        MediaListHandler.setNestedAlbumList(nestedAlbumView.getList());
        title.setText(nestedAlbumView.getTitle());
        backButton.setText("Back");
        backButton.setDisable(false);
    }

    private void clearNestedViews() {
        nestedAlbumView = null;
        nestedTrackView = null;
    }

    private void disableBackButton() {
        backButton.setText(null);
        backButton.setDisable(true);
    }

    public static void updateViews(Track editedTrack, Album updatedAlbum, Album oldAlbum, Artist updatedArtist, Artist oldArtist) {
        // TODO
        // mediaListHandler.updateLists();
        // if old Album was removed from database, switch view to main Artist view
        try (DBConnection connection = new DBConnection()) {
            // boolean albumExists = connection.albumExists(oldAlbum.getId());
            // if (!albumExists) {
                // switch to main artist view and nullify views
            // }

            // boolean artistExists = connection
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // same for removal of old artist

        // then update lists
        MediaListHandler.hardRefresh();
    }
    // private void setContextMenus() {
    //     System.out.println("In set context Menu");
    //     buildTrackViewMenu();
    //     // setTrackViewMenu(trackView);
    //     // trackView.trackViewTable.setContextMenu(trackContextMenu);
    //     // System.out.println("current track view null:");
    //     // System.out.println(currentTrackView == null);


    // }

    // private void buildTrackViewMenu() {
    //     trackContextMenu = new ContextMenu();
    //     MenuItem play = new MenuItem("Play");
    //     play.setOnAction(e -> {
    //         System.out.println("Play selected");
    //         queueAndPlay(trackView.trackViewTable.getSelectionModel().getSelectedItems());
    //     });

    //     MenuItem queue = new MenuItem("Queue");
    //     queue.setOnAction(e -> {
    //         System.out.println("queue selected");
    //         queue(trackView.trackViewTable.getSelectionModel().getSelectedItems()); 
    //     });

    //     MenuItem queueNext = new MenuItem("Queue Next");
    //     queue.setOnAction(e -> queueNext(trackView.trackViewTable.getSelectionModel().getSelectedItems()));

    //     MenuItem edit = new MenuItem("Edit Selection");
    //     queue.setOnAction(e -> editTrack(trackView.trackViewTable.getSelectionModel().getSelectedItem()));

    //     trackContextMenu.getItems().addAll(play, queue, queueNext, edit);
    //     // trackContextMenu.getItems().add(queue);
    //     // trackContextMenu.getItems().add(queueNext);
    //     // trackContextMenu.getItems().add(edit);

    //     trackView.trackViewTable.setContextMenu(trackContextMenu);

    //     System.out.println("Done building track context menu");
    // }

    // private void queueAndPlay(ObservableList<Track> tracks) {
    //     if (tracks != null) {
    //         if (tracks.size() > 0) {
    //             audioPlayer.queueAndPlay(tracks);
    //         }
    //     }
    // }

    // private void queue(ObservableList<Track> tracks) {
    //     System.out.println("Tracks is null: " + tracks == null);
    //     if (tracks != null) {
    //         if (tracks.size() > 0) {
    //             System.out.println("queueing tracks");
    //             audioPlayer.queue(tracks);
    //         }
    //     }
    // }

    // private void queueNext(ObservableList<Track> tracks) {
    //     if (tracks != null) {
    //         if (tracks.size() > 0) {
    //             audioPlayer.queueNext(tracks);
    //         }
    //     }
    // }

    // private void editTrack(Track track) {
    //     if (track != null) {
    //         // Do some editing
    //     }
    // }

    // private void setTrackViewMenu(TrackView view) {
    //     view.trackViewTable.setContextMenu(trackContextMenu);
    // }
}
