package main.frontend;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.database.DBConnection;
import main.library.Album;
import main.library.Artist;
import main.library.Track;

public class TrackEditController implements Initializable {

    private Track trackToEdit;

    @FXML
    private Label trackFilepath;
    
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
        Stage stage = (Stage) trackFilepath.getScene().getWindow();
        stage.close();
    }

    @FXML
    void submitClicked(ActionEvent event) {
        boolean success = false;
        Album oldAlbum = null;
        Album updatedAlbum = null;
        Artist oldArtist = null;
        Artist updatedArtist = null;
        try (DBConnection connection = new DBConnection()) {
            oldAlbum = connection.getAlbum(connection.getAlbumID(trackToEdit.getAlbums(), trackToEdit.getId()));
            oldArtist = connection.getArtist(connection.getArtistID(trackToEdit.getArtists(), trackToEdit.getId()));

            trackToEdit.setName(trackNameField.getText());
            trackToEdit.setArtists(artistNameField.getText());
            trackToEdit.setAlbums(albumNameField.getText());
            trackToEdit.setGenres(genreField.getText());
            trackToEdit.setIndexInAlbum(trackToEdit.getIndexInAlbum());
            success = connection.updateTrack(trackToEdit);

            int updatedAlbumID = connection.getAlbumID(trackToEdit.getAlbums(), trackToEdit.getId());
            updatedAlbum = connection.getAlbum(updatedAlbumID);

            int updatedArtistID = connection.getArtistID(trackToEdit.getArtists(), trackToEdit.getId());
            updatedArtist = connection.getArtist(updatedArtistID);
        } catch (SQLException e) {
            System.err.println("TrackEditController: Failed to update track in database");
            e.printStackTrace();
        }

        if (success) {
            FxmlController mainController = FxmlController.getSelf();
            mainController.updateViews(trackToEdit, updatedAlbum, oldAlbum, updatedArtist, oldArtist);
        }

        Stage stage = (Stage) trackFilepath.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

    }

    public void setTrackToEdit(Track track) {
        trackToEdit = track;
    }

    public void setFields() {
        trackFilepath.setText(trackToEdit.getFilepath());
        trackNameField.setText(trackToEdit.getName());
        albumNameField.setText(trackToEdit.getAlbums());
        artistNameField.setText(trackToEdit.getArtists());
        trackNumberField.setText(String.valueOf(trackToEdit.getIndexInAlbum()));
        genreField.setText(trackToEdit.getGenres());
    }

}
