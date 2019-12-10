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
    private Label currentTrackLabel;

    @FXML
    private Label elapsedTimeLabel;

    @FXML
    private Label totalTimeLabel;



    private final ArtistView artistView = new ArtistView();
    private final AlbumView albumView = new AlbumView();
    private final TrackView trackView = new TrackView();
    private ElapsedTimeListener elapsedTimeListener;
    private AudioPlayer audioPlayer;

    @FXML
    void albumsClicked(ActionEvent event) {
        // set current table view to albums table view
        libraryPlayerPane.setCenter(albumView.albumViewTable);
    }

    @FXML
    void artistsClicked(ActionEvent event) {
        libraryPlayerPane.setCenter(artistView.artistViewTable);
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
        setDoubleClick();
        connectSliderToPlayer();

        elapsedTimeLabel.textProperty().bind(elapsedTimeListener.elapsedTimeProperty());
        totalTimeLabel.textProperty().bind(elapsedTimeListener.totalTimeProperty());
        currentTrackLabel.textProperty().bind(audioPlayer.currentTrackNameProperty());
        
    }
    
    private void setDoubleClick() {
        trackView.trackViewTable.setRowFactory(tv -> {
            TableRow<Track> trackRow = new TableRow<>();
            trackRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!trackRow.isEmpty())) {
                    queueAndPlay(trackRow.getItem());
                }
            });
            return trackRow;
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

}
