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
import main.player.Track;

public class TrackImportBox {
    private final Stage importBox = new Stage();
    private GridPane root = new GridPane();

    private String filepath = "No File Selected.";
    private String duration;
    private String trackName;
    private String artistName;
    private String albumName;
    private String genreName;

    private Label file = new Label("File:");
    private Label filepathLabel = new Label(filepath);
    private Label artist = new Label("Artist Name:");
    private Label album = new Label("Album Name:");
    private Label track = new Label("Track Name:");
    private Label genre = new Label("Genre:");

    private TextField artistField = new TextField();
    private TextField albumField = new TextField();
    private TextField trackField = new TextField();
    private TextField genreField = new TextField();
    private Button browseFile = new Button("Browse");
    private Button submit = new Button("Submit");
    private Button cancel = new Button("Cancel");

    private int row = 0;

    public TrackImportBox() {
        // importBox.initModality(Modality.APPLICATION_MODAL);
        // importBox.initOwner(primaryStage);
        root.setVgap(10);
        root.setHgap(5);
        root.setPadding(new Insets(10));

        browseFile.setOnAction(e -> loadAudioFile(importBox));
        cancel.setOnAction(e -> importBox.close());
        submit.setOnAction(e -> submitInfo());

        root.add(browseFile, 0, row++);
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

        importBox.setTitle("Import Track");
        importBox.setScene(scene);
        // importBox.show();

    }


    public void open(Stage primaryStage) {
        importBox.initModality(Modality.APPLICATION_MODAL);
        importBox.initOwner(primaryStage);
        importBox.show();

    }


    public void submitInfo() {
        if (filepath == null) {
            System.out.println("Must provide a valid filepath");
            return;
        }

        trackName = trackField.getText();
        artistName = artistField.getText();
        albumName = albumField.getText();
        genreName = genreField.getText();
        System.out.format("Filepath: %s\ntrack: %s\nartist: %s\nalbum: %s\ngenre: %s\n", filepath, trackName, artistName, albumName, genreName);

        try (DBConnection connection = new DBConnection()) {
            connection.addTrackToDB(filepath, duration, trackName, artistName, albumName, genreName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void loadAudioFile(Stage stage) {
        File audioFile = getAudioFile(stage);
        if (audioFile != null) {
            if (!isValidAudioFile(audioFile)) {
                System.err.println("Not a valid audio file");
                return;
            }

            if (existsInDB(audioFile.toString())) {
                System.err.println("Track already exists");
                // show details about the track so user can find it
                return;
            }

            duration = getTrackLength(audioFile);

            filepath = audioFile.toString();
            filepathLabel.setText(filepath);
        }
    }

    private File getAudioFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.aiff", "*.au");
        fileChooser.getExtensionFilters().add(extFilter);
        return fileChooser.showOpenDialog(stage);
    }

    private boolean isValidAudioFile(File file) {
        try {
            AudioSystem.getAudioInputStream(file);
            return true;
        } catch (UnsupportedAudioFileException | IOException e) {
            return false;
        }
    }

    private boolean existsInDB(String filepath) {
        try (DBConnection connection = new DBConnection()) {
            if (connection.filepathExists(filepath)) {
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Unable to connect to database");
            e.printStackTrace();
        }

        return false;
    }

    private String getTrackLength(File audioFile) {
        try (Track tempTrack = new Track(audioFile)) {
            int hours = tempTrack.getHours();
            if (hours > 0) {
                return String.format("%02d:%02d:%02d", hours, tempTrack.getMinutes(), tempTrack.getSeconds());
            }
            return String.format("%02d:%02d", tempTrack.getMinutes(), tempTrack.getSeconds());

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Unable to determine track length");
        }
        return "--:--:--";
    }
}