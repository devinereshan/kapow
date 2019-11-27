package main.frontend;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.database.DBConnection;
import main.library.TrackRow;
import main.library.TrackRowList;
import main.player.Track;

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
    private Label track = new Label("Track Name:");
    private Label genre = new Label("Genre:");

    private TextField artistField = new TextField();
    private TextField albumField = new TextField();
    private TextField trackField = new TextField();
    private TextField genreField = new TextField();
    private Button submit = new Button("Submit");
    private Button cancel = new Button("Cancel");

    TrackRowList trackRowList;
    TrackRow trackRow;

    private int row = 0;

    public TrackEditBox(TrackRow trackRow) {
        root.setVgap(10);
        root.setHgap(5);
        root.setPadding(new Insets(10));

        cancel.setOnAction(e -> editBox.close());
        submit.setOnAction(e -> submitInfo());

        this.trackRow = trackRow;
        filepath = trackRow.getFilepath();
        filepathLabel = new Label(filepath);
        artistName = trackRow.getArtists();
        artistField.setText(artistName);
        albumName = trackRow.getAlbums();
        albumField.setText(albumName);
        trackName = trackRow.getName();
        trackField.setText(trackName);
        genreName = trackRow.getGenres();
        genreField.setText(genreName);

        root.add(file, 0, row);
        root.add(filepathLabel, 2, row++);
        root.add(track, 0, row);
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

        // this.trackRowList = trackRowList;
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


        // trackName = trackField.getText();
        // artistName = artistField.getText();
        // albumName = albumField.getText();
        // genreName = genreField.getText();
        // System.out.format("Filepath: %s\ntrack: %s\nartist: %s\nalbum: %s\ngenre: %s\n", filepath, trackName, artistName, albumName, genreName);

        try (DBConnection connection = new DBConnection()) {
            // connection.updateTrackInfo(filepath, trackName, artistName, albumName, genreName);

            int trackID = connection.getID("Track", "filepath", filepath);
            if (trackName != track.getText()) {
                connection.updateTrackName(track.getText(), trackID);
                trackRow.setName(track.getText());
            }

            if (artistName != artist.getText()) {
                //TODO: remove old Track_Artist ID pair
                //add new ID pair
                connection.updateTrackArtist(artistName, artist.getText(), trackID);
                trackRow.setArtists(artist.getText());
            }

            if (albumName != album.getText()) {
                connection.updateTrackAlbum(albumName, album.getText(), trackID);
                trackRow.setAlbums(album.getText());
            }

            if (genreName != genre.getText()) {
                connection.updateTrackGenre(genreName, genre.getText(), trackID);
                trackRow.setGenres(genre.getText());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        // trackRow.refresh();

        editBox.close();
    }



}