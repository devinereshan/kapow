package main.frontend;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
// import javafx.scene.control.ToggleButton;
// import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
// import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.database.DBConnection;
import main.library.Album;
import main.library.Artist;
import main.library.MediaListHandler;
import main.library.Track;
import main.player.AudioPlayer;
import main.player.ElapsedTimeListener;

public class FxmlController implements Initializable {

    private static FxmlController self;

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

    @FXML
    private ToggleButton artistsToggleButton;

    @FXML
    private ToggleButton albumsToggleButton;

    @FXML
    private ToggleButton tracksToggleButton;

    @FXML
    private ListView<String> queueListView;

    @FXML
    private BorderPane listViewBorderPane;

    @FXML
    void queueListViewClicked(MouseEvent event) {
        int index = queueListView.getSelectionModel().getSelectedIndex();
        if (event.getClickCount() == 2 && (queueListView.getSelectionModel().getSelectedItem() != null)) {
            audioPlayer.seekToQueueIndex(index);            
        }
        // boolean suucess = audioPlayer.seekToIndex(index);
        // if (success) {
        //     queueListView.
        // }
    }

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
    void albumsToggleClicked(ActionEvent event) {
        switchToMainAlbumsView();
    }

    private void switchToMainAlbumsView() {
        libraryPlayerPane.setCenter(albumView.albumViewTable);
        clearNestedViews();
        disableBackButton();
        title.setText(albumView.getTitle());
        artistsToggleButton.setSelected(false);
        tracksToggleButton.setSelected(false);
        albumsToggleButton.setSelected(true);
    }

    @FXML
    void artistsToggleClicked(ActionEvent event) {
        switchToMainArtistsView();
    }

    private void switchToMainArtistsView() {
        libraryPlayerPane.setCenter(artistView.artistViewTable);
        clearNestedViews();
        disableBackButton();
        title.setText(artistView.getTitle());
        albumsToggleButton.setSelected(false);
        tracksToggleButton.setSelected(false);
        artistsToggleButton.setSelected(true);
    }

    @FXML
    void tracksToggleClicked(ActionEvent event) {
        switchToMainTracksView();
    }

    private void switchToMainTracksView() {
        libraryPlayerPane.setCenter(trackView.trackViewTable);
        clearNestedViews();
        disableBackButton();
        title.setText(trackView.getTitle());
        artistsToggleButton.setSelected(false);
        albumsToggleButton.setSelected(false);
        tracksToggleButton.setSelected(true);
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
        launchImportBox();
    }

    @FXML
    void pauseClicked(ActionEvent event) {
        audioPlayer.pause();
    }

    @FXML
    void playClicked(ActionEvent event) {
        audioPlayer.play();
        queueListView.getSelectionModel().select(0);
    }

    @FXML
    void searchTermEntered(KeyEvent event) {
        if (tracksToggleButton.isSelected()) {
            trackView.filter(searchBox);
        } else if (albumsToggleButton.isSelected()) {
            if (nestedTrackView != null) {
                nestedTrackView.filter(searchBox);
            } else {
                albumView.filter(searchBox);
            }
        }
    }

    @FXML
    void seekLeftClicked(ActionEvent event) {
        audioPlayer.seekLeft();
        queueListView.getSelectionModel().select(0);
    }

    @FXML
    void seekRightClicked(ActionEvent event) {
        audioPlayer.seekRight();
        queueListView.getSelectionModel().select(0);
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

        // queueListView = new ListView<String>(audioPlayer.getTrackNameList());
        queueListView.setItems(audioPlayer.getTrackNameList());
        listViewBorderPane.setCenter(queueListView);
        queueListView.getItems().addListener(new ListChangeListener() {
            @Override
            public void onChanged(ListChangeListener.Change change) {
                queueListView.getSelectionModel().select(0);
            }
        });

        volumeBar.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                audioPlayer.setVolume(volumeBar.getValue() / 100);
            }
        });
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
        self = this;
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
        MediaListHandler.nullifyNestedAlbumList();
        MediaListHandler.nullifyNestedTrackList();
    }

    private void disableBackButton() {
        backButton.setText(null);
        backButton.setDisable(true);
    }

    public static FxmlController getSelf() {
        return self;
    }
    public void updateViews(Track editedTrack, Album updatedAlbum, Album oldAlbum, Artist updatedArtist, Artist oldArtist) {
        try (DBConnection connection = new DBConnection()) {
            if (nestedTrackView != null) {
                boolean albumExists = connection.albumExists(nestedTrackView.getAlbum().getId());
                if (!albumExists) {
                    System.out.println("FXML controller Album no longer exists");
                    // libraryPlayerPane.setCenter(artistView.artistViewTable);
                    // clearNestedViews();
                    // disableBackButton();
                    // title.setText(artistView.getTitle());
                    switchToMainArtistsView();
                }
            }

            boolean artistExists = connection.artistExists(oldArtist.getId());
            if (!artistExists) {
                // libraryPlayerPane.setCenter(artistView.artistViewTable);
                // clearNestedViews();
                // disableBackButton();
                // title.setText(artistView.getTitle());
                switchToMainArtistsView();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (nestedTrackView != null) {
            nestedTrackView.updateTitle();
        }

        if (nestedAlbumView != null) {
            nestedAlbumView.updateTitle();
        }

        // then update lists
        MediaListHandler.hardRefresh();
    }


    private void launchImportBox() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("import_box.fxml"));
            Parent root = (Parent)fxmlLoader.load();
            ImportController controller = fxmlLoader.<ImportController>getController();
            // controller.setTrackToEdit(track);
            // controller.setFields();

            Stage popup = new Stage();
            popup.setTitle("Edit Track Information");
            popup.setScene(new Scene(root));
            popup.show();
        } catch (IOException e) {
            System.err.println("TrackView: Unable to open track edit box");
            e.printStackTrace();
        }
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
