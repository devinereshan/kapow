package main.frontend;

import java.sql.SQLException;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.database.DBConnection;
import main.library.Album;
import main.library.Artist;
import main.library.MediaListHandler;
import main.library.Track;

public class TrackEditBox {
    private final Stage editBox = new Stage();
    private VBox root = new VBox();
    private GridPane gridPane = new GridPane();

    private String filepath;
    private String trackName;
    private String artistName;
    private String albumName;
    private int trackNumber;
    private String genreName;

    private Label file = new Label("File:");
    private Label filepathLabel;
    private Label artist = new Label("Artist Name:");
    private Label album = new Label("Album Name:");
    private Label trackLabel = new Label("Track Name:");
    private Label trackNumberLabel = new Label("Track Number");
    private Label genre = new Label("Genre:");

    private TextField artistField = new TextField();
    private TextField albumField = new TextField();
    private TextField trackField = new TextField();
    private TextField trackNumberField = new TextField();
    private TextField genreField = new TextField();
    private Button submit = new Button("Submit");
    private Button cancel = new Button("Cancel");

    private Track track;
    // private MediaListHandler mediaListHandler;

    private int row = 0;

    public TrackEditBox(Track track) {
        // this.mediaListHandler = mediaListHandler;
        gridPane.setVgap(10);
        gridPane.setHgap(5);
        gridPane.setPadding(new Insets(10));
        root.setPrefSize(500, 500);
        root.setAlignment(Pos.CENTER);
        gridPane.setPrefSize(400, 400);
        

        cancel.setOnAction(e -> editBox.close());
        submit.setOnAction(e -> submitInfo());

        this.track = track;
        filepath = track.getFilepath();
        filepathLabel = new Label(filepath);
        artistName = track.getArtists();
        artistField.setText(artistName);
        albumName = track.getAlbums();
        albumField.setText(albumName);
        trackName = track.getName();
        trackField.setText(trackName);
        trackNumber = track.getIndexInAlbum();
        trackNumberField.setText(String.valueOf(trackNumber));
        genreName = track.getGenres();
        genreField.setText(genreName);

        gridPane.add(file, 0, row);
        gridPane.add(filepathLabel, 2, row++);
        gridPane.add(trackLabel, 0, row);
        gridPane.add(trackField, 2, row++);
        gridPane.add(artist, 0, row);
        gridPane.add(artistField, 2, row++);
        gridPane.add(album, 0, row);
        gridPane.add(albumField, 2, row++);
        gridPane.add(trackNumberLabel, 0, row);
        gridPane.add(trackNumberField, 2, row++);
        gridPane.add(genre, 0, row);
        gridPane.add(genreField, 2, row++);
        gridPane.add(submit, 0, row);
        gridPane.add(cancel, 2, row++);

        root.getChildren().add(gridPane);
        Scene scene = new Scene(root);

        editBox.setTitle("Edit Track");
        editBox.setScene(scene);

    }


    public void open(Stage primaryStage) {
        editBox.initModality(Modality.APPLICATION_MODAL);
        editBox.initOwner(primaryStage);
        editBox.show();

    }


    public void submitInfo() {
        if (filepath == null) {
            System.out.println("Must provide a valid filepath");
            return;
        }

        boolean success = false;
        Track oldTrack = new Track(track.getId(), track.getFilepath(), track.getName(), track.getDuration(), track.getArtists(), track.getAlbums(), track.getGenres(), track.getLengthInSeconds(), track.getIndexInAlbum());
        Album oldAlbum = null;
        Album updatedAlbum = null;
        Artist oldArtist = null;
        Artist updatedArtist = null;
        try (DBConnection connection = new DBConnection()) {

            oldAlbum = connection.getAlbum(connection.getAlbumID(oldTrack.getAlbums(), oldTrack.getId()));
            oldArtist = connection.getArtist(connection.getArtistID(oldTrack.getArtists(), oldTrack.getId()));

            track.setName(trackField.getText());
            track.setArtists(artistField.getText());
            track.setAlbums(albumField.getText());
            track.setGenres(genreField.getText());
            track.setIndexInAlbum(track.getIndexInAlbum());
            success = connection.updateTrack(track);

            updatedAlbum = connection.getAlbum(connection.getAlbumID(track.getAlbums(), track.getId()));
            updatedArtist = connection.getArtist(connection.getArtistID(track.getArtists(), track.getId()));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (success) {
            // mediaListHandler.updateLists(track, updatedAlbum, oldAlbum, updatedArtist, oldArtist);
            Platform.runLater(() -> FxmlController.updateViews());
        }

        editBox.close();
    }



}