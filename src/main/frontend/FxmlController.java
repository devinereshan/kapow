package main.frontend;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
// import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
// import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import main.library.Artist;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import main.library.Track;
import main.library.Album;
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
    private Label currentArtistAlbumLabel;

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
    private final TrackView trackView = new TrackView();
    private AlbumView nestedAlbumView;
    private TrackView nestedTrackView;
    private ElapsedTimeListener elapsedTimeListener;
    private AudioPlayer audioPlayer;
    private AlbumView parentAlbumView;
    private ArtistView parentArtistView;

    @FXML
    void albumsClicked(ActionEvent event) {
        // set current table view to albums table view
        libraryPlayerPane.setCenter(albumView.albumViewTable);
        // libraryPlayerPane.getCenter().
        clearNestedViews();
    }

    @FXML
    void artistsClicked(ActionEvent event) {
        libraryPlayerPane.setCenter(artistView.artistViewTable);
        clearNestedViews();
    }

    @FXML
    void elapsedTimeScrolled(ScrollEvent event) {
        // audioPlayer.fineSeek(Duration.seconds(elapsedTimeBar.getValue() * elapsedTimeListener.getMaxElapsedTime() / 100));
    }

    @FXML
    void tableViewClicked(MouseEvent event) {
        System.out.println("table view clicked");
    }

    @FXML
    void backClicked(ActionEvent event) {
        if (parentAlbumView != null) {
            libraryPlayerPane.setCenter(parentAlbumView.albumViewTable);
            title.setText(parentAlbumView.getTitle());
            nestedTrackView = null;
        } else if (parentArtistView != null) {
            libraryPlayerPane.setCenter(parentArtistView.artistViewTable);
            title.setText(parentArtistView.getTitle());
            clearNestedViews();
        }

        if (nestedAlbumView == null) {
            backButton.setText(null);
            backButton.setDisable(true);
            // title.setText("kapow!");
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
    void tracksClicked(ActionEvent event) {
        libraryPlayerPane.setCenter(trackView.trackViewTable);
        clearNestedViews();
    }

    @FXML
    void volumeScrolled(ScrollEvent event) {

    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        System.out.println("Loading user data");
        elapsedTimeListener = new ElapsedTimeListener(elapsedTimeBar);
        audioPlayer = new AudioPlayer(elapsedTimeListener);

        libraryPlayerPane.setCenter(artistView.artistViewTable);
        setDoubleClicks();
        connectSliderToPlayer();

        elapsedTimeLabel.textProperty().bind(elapsedTimeListener.elapsedTimeProperty());
        totalTimeLabel.textProperty().bind(elapsedTimeListener.totalTimeProperty());
        currentTrackLabel.textProperty().bind(audioPlayer.currentTrackNameProperty());
        
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
                    if (nestedAlbumView == null) {
                        parentAlbumView = albumView;
                        switchToNestedTrackView(albumRow.getItem());
                    } else {
                        parentAlbumView = nestedAlbumView;
                        switchToNestedTrackView(albumRow.getItem());
                    }
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
                    // viewHandler.switchToNestedAlbumView(artistRow.getItem());
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
                    audioPlayer.fineSeek(Duration.seconds(elapsedTimeBar.getValue() * elapsedTimeListener.getMaxElapsedTime() / 100));
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
        nestedTrackView = new TrackView(album);
        libraryPlayerPane.setCenter(nestedTrackView.trackViewTable);
        setTrackViewDoubleClick(nestedTrackView);
        title.setText(nestedTrackView.getTitle());
        backButton.setText("Back");
        backButton.setDisable(false);
    }

    private void switchToNestedAlbumView(Artist artist) {
        nestedAlbumView = new AlbumView(artist);
        libraryPlayerPane.setCenter(nestedAlbumView.albumViewTable);
        setAlbumViewDoubleClick(nestedAlbumView);
        title.setText(nestedAlbumView.getTitle());
        backButton.setText("Back");
        backButton.setDisable(false);
    }

    private void clearNestedViews() {
        nestedAlbumView = null;
        nestedTrackView = null;
    }
}
