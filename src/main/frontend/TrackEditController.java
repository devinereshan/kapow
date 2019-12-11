package main.frontend;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import main.library.Track;

public class TrackEditController implements Initializable {

    private Track trackToEdit;
    private static String filepath;
    private static String trackName;
    private static String artistName;
    private static String albumName;
    private static String trackNumber;
    private static String genreName;

    @FXML
    private TextField trackNameField;

    @FXML
    private TextField albumNameField;

    @FXML
    private TextField artistNameField;

    @FXML
    private TextField trackNumberField;

    @FXML
    private TextField genreField;

    @FXML
    void cancelClicked(ActionEvent event) {

    }

    @FXML
    void submitClicked(ActionEvent event) {

    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        trackToEdit = TrackView.getTrackToEdit();

    }

}
