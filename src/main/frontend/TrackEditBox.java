package main.frontend;

import java.sql.SQLException;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.database.DBConnection;
import main.library.Track;

public class TrackEditBox {
    private final Stage editBox = new Stage();
    private GridPane root = new GridPane();

    private String filepath;
    private String trackName;
    private String artistName;
    private String albumName;
    private String genreName;

    private Label file = new Label("File:");
    private Label filepathLabel;
    private Label artist = new Label("Artist Name:");
    private Label album = new Label("Album Name:");
    private Label trackLabel = new Label("Track Name:");
    private Label genre = new Label("Genre:");

    private TextField artistField = new TextField();
    private TextField albumField = new TextField();
    private TextField trackField = new TextField();
    private TextField genreField = new TextField();
    private Button submit = new Button("Submit");
    private Button cancel = new Button("Cancel");

    Track track;

    private int row = 0;

    public TrackEditBox(Track track) {
        root.setVgap(10);
        root.setHgap(5);
        root.setPadding(new Insets(10));

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
        genreName = track.getGenres();
        genreField.setText(genreName);

        root.add(file, 0, row);
        root.add(filepathLabel, 2, row++);
        root.add(trackLabel, 0, row);
        root.add(trackField, 2, row++);
        root.add(artist, 0, row);
        root.add(artistField, 2, row++);
        root.add(album, 0, row);
        root.add(albumField, 2, row++);
        root.add(genre, 0, row);
        root.add(genreField, 2, row++);
        root.add(submit, 0, row);
        root.add(cancel, 2, row++);

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

        // TODO: this set of update transactions should be atomic
        try (DBConnection connection = new DBConnection()) {

            int trackID = connection.getID("Track", "filepath", filepath);
            if (trackName != trackField.getText()) {
                connection.updateTrackName(trackField.getText(), trackID);
                track.setName(trackField.getText());
            }

            if (artistName != artistField.getText()) {
                connection.updateTrackArtist(artistName, artistField.getText(), trackID);
                track.setArtists(artistField.getText());
            }

            if (albumName != albumField.getText()) {
                connection.updateTrackAlbum(albumName, albumField.getText(), trackID);
                track.setAlbums(albumField.getText());
            }

            if (genreName != genreField.getText()) {
                connection.updateTrackGenre(genreName, genreField.getText(), trackID);
                track.setGenres(genreField.getText());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        editBox.close();
    }



}