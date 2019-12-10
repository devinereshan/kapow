package main.frontend;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
// import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
// import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import main.library.Artist;
import javafx.scene.control.Slider;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class FxmlController implements Initializable {

    @FXML
    private TableView currentTableView = new TableView();

    @FXML
    private Slider elapsedTimeBar;

    @FXML
    private Slider volumeBar;

    @FXML
    private TextField serchBox;

    private final ArtistView artistView = new ArtistView();
    private final TableView<Artist> artistTable = new TableView<>();

    @FXML
    void albumsClicked(ActionEvent event) {
        // set current table view to albums table view
    }

    @FXML
    void artistsClicked(ActionEvent event) {

    }

    @FXML
    void elapsedTimeScrolled(ScrollEvent event) {

    }

    @FXML
    void importClicked(ActionEvent event) {

    }

    @FXML
    void pauseClicked(ActionEvent event) {

    }

    @FXML
    void playClicked(ActionEvent event) {

    }

    @FXML
    void searchTermEntered(KeyEvent event) {

    }

    @FXML
    void seekLeftClicked(ActionEvent event) {

    }

    @FXML
    void seekRightClicked(ActionEvent event) {

    }

    @FXML
    void stopClicked(ActionEvent event) {

    }

    @FXML
    void tracksClicked(ActionEvent event) {

    }

    @FXML
    void volumeScrolled(ScrollEvent event) {

    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO Auto-generated method stub
        System.out.println("Loading user data");
        currentTableView.getColumns().setAll(artistView.nameCol, artistView.numberOfAlbumsCol, artistView.genresCol);
        currentTableView.setItems(artistView.artistList.getArtists());


    }

}
