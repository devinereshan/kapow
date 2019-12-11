package main.frontend;

import java.sql.SQLException;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.database.DBConnection;
import main.library.MediaListHandler;
import main.library.Track;

public class ConfirmDeleteController {

    ObservableList<Track> tracksToDelete;

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    @FXML
    private VBox trackListBox;
    
    @FXML
    private Label trackLabel;

    @FXML
    private Label topLabel;

    @FXML
    void cancelClicked(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void confirmClicked(ActionEvent event) {
        removeTracks();
    }

    public void setTracksToDelete(ObservableList<Track> tracksToDelete) {
        this.tracksToDelete = tracksToDelete;
    }

    public void setFields() {
        if (tracksToDelete == null) {
            System.out.println("No tracks to delete");
            return;
        }
        
        if (tracksToDelete.size() > 1) {
            topLabel.setText("Remove Tracks?");
        }

        for (Track track : tracksToDelete) {
            Label l = new Label(String.format("%s - %s - %s", track.getName(), track.getAlbums(), track.getArtists()));
            l.setPadding(new Insets(10, 20, 10, 20));
            trackListBox.getChildren().add(l);
        }
    }
    
    private void removeTracks() {
        int albumID = 0;
        // Album album = null;
        // Artist artist = null;
        int artistID = 0;

        boolean success = false;

        try (DBConnection connection = new DBConnection()) {
            for (Track track : tracksToDelete) {
                albumID = connection.getAlbumID(track.getAlbums(), track.getId());
                artistID = connection.getArtistID(track.getArtists(), track.getId());
                connection.removeTrackFromDB(track.getId(), artistID, albumID);
            }

            success = true;

            // album = connection.getAlbum(albumID);
            // artist = connection.getArtist(artistID);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (success) {
            MediaListHandler.hardRefresh();
            Stage stage = (Stage) confirmButton.getScene().getWindow();
            stage.close();
        } else {
            topLabel.setText("Failed to remove tracks");
            confirmButton.setDisable(true);
        }
    }
}
