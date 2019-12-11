package main.frontend;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import main.library.Track;

public class ConfirmDeleteController {

    ObservableList<Track> tracksToDelete;

    @FXML
    private VBox trackListBox;
    
    @FXML
    private Label trackLabel;

    @FXML
    private Label topLabel;

    @FXML
    void cancelClicked(ActionEvent event) {

    }

    @FXML
    void confirmClicked(ActionEvent event) {

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

        Track firstTrack = tracksToDelete.get(0);
        trackLabel.setText(String.format("%s - %s - %s", firstTrack.getName(), firstTrack.getAlbums(), firstTrack.getArtists()));
    }

}
