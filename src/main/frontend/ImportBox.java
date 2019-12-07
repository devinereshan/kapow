package main.frontend;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.database.DBConnection;
import main.library.MediaListHandler;
import main.library.Track;
import main.player.TrackLengthCalculator;

public class ImportBox {
    private final Stage stage = new Stage();
    private final Button importFiles = new Button("Import Files");
    private final Button importDirectory = new Button("Import Directory");
    private final Button submit = new Button("Submit");
    private final Button cancel = new Button("Cancel");
    private MediaListHandler mediaListHandler;
    private VBox root;
    private HBox top;
    private HBox middle;
    private VBox middleLeft;
    private VBox middleRight;
    private HBox bottom;

    private List<File> files;
    private Label filepathLabel;
    private Label importing = new Label();
    private ArrayList<TrackInfo> trackInfos = new ArrayList<>();
    private AlbumInfo albumInfo;


    private class TrackInfo {
        File file;
        TextField nameField;
        Label nameLabel = new Label("Name:");
        TextField indexInAlbumField;
        Label indexLabel = new Label("Track #");
        VBox parent;
        HBox top;
        HBox bottom;
        int lengthInSeconds;
        String duration;

        public TrackInfo(File file) {
            this.file = file;
            nameField = new TextField();
            indexInAlbumField = new TextField();
            indexInAlbumField.setPrefWidth(40);

            top = new HBox(new Label(file.getName().toString()));
            bottom = new HBox(10, indexLabel, indexInAlbumField, nameLabel, nameField);
            parent = new VBox(5, top, bottom);

            indexInAlbumField.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                    if (!newValue.matches("\\d*")) {
                        indexInAlbumField.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                }
            });

            setLengthVariables();
            autoFillFields();
        }


        public String getFilepath() {
            return file.getAbsolutePath();
        }


        public int getIndexInAlbum() {
            return Integer.parseInt(indexInAlbumField.getText());
        }


        public String getName() {
            return nameField.getText();
        }

        public VBox getParent() {
            return parent;
        }

        public String getDuration() {
            return duration;
        }

        public int getLengthInSeconds() {
            return lengthInSeconds;
        }

        private void autoFillFields() {
            String name = file.getName();
            if (!(name.charAt(0) == '.')) {
                name = name.split("\\.", 2)[0];
            }

            String trackIndex = "";
            Pattern p = Pattern.compile("\\d+");
            Matcher m = p.matcher(name);

            while (m.find()) {
                trackIndex = m.group().replaceFirst("^0+", "").strip();
                break;
            }

            name = name.replaceFirst("^\\d+", "").strip();

            nameField.setText(name);
            indexInAlbumField.setText(trackIndex);

        }


        private void setLengthVariables() {
            try (TrackLengthCalculator tempTrack = new TrackLengthCalculator(file)) {
                lengthInSeconds = tempTrack.lengthInSeconds();
                int hours = tempTrack.getHours();
                if (hours > 0) {
                    duration = String.format("%02d:%02d:%02d", hours, tempTrack.getMinutes(), tempTrack.getSeconds());
                }
                duration = String.format("%02d:%02d", tempTrack.getMinutes(), tempTrack.getSeconds());

            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                System.err.println("Unable to determine track length");
            }

            if (duration == null) {
                duration = "--:--:--";
            }
        }

    }


    private class AlbumInfo {
        File file;
        Label albumNameLabel = new Label("Album Name");
        Label artistNameLabel = new Label("Artist Name");
        Label genreLabel = new Label("Genre");
        TextField albumField = new TextField();
        TextField artistField = new TextField();
        TextField genreField = new TextField();

        VBox parent;


        public AlbumInfo (File file) {
            this.file = file;

            parent = new VBox(10, artistNameLabel, artistField, albumNameLabel, albumField, genreLabel, genreField);

            autoFillFields();
        }

        public VBox getParent() {
            return parent;
        }

        private void autoFillFields() {
            artistField.setText(file.getParentFile().getName());
            albumField.setText(file.getName());
        }

        public String getAlbumName() {
            return albumField.getText();
        }

        public String getArtistName() {
            return artistField.getText();
        }

        public String getGenre() {
            return genreField.getText();
        }
    }


    public ImportBox (Stage primaryStage, MediaListHandler mediaListHandler) {
        this.mediaListHandler = mediaListHandler;
        cancel.setOnAction(e -> stage.close());
        submit.setDisable(true);
        submit.setOnAction(e -> submit());

        filepathLabel = new Label("No Audio Files Selected");
        importFiles.setOnAction(e -> getFiles(stage));

        top = new HBox(10, importFiles, filepathLabel);
        middleLeft = new VBox(10);
        middleRight = new VBox(10);
        middle = new HBox(10, middleLeft, middleRight);
        bottom = new HBox(50, submit, importing, cancel);

        root = new VBox();
        root.setPrefSize(600, 500);
        root.getChildren().addAll(top, middle, bottom);

        Scene scene = new Scene(root);
        stage.setTitle("Import Audio Files");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(primaryStage);
        stage.show();
    }


    private void getFiles(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.aiff", "*.au");
        fileChooser.getExtensionFilters().add(extFilter);

        files = fileChooser.showOpenMultipleDialog(stage);

        if (files != null) {
            addImportFields(files);
        }
    }


    private void addImportFields(List<File> files) {
        File parentFile = files.get(0).getParentFile();
        filepathLabel.setText(parentFile.toString());

        for (File file : files) {
            if (isValidAudioFile(file)) {
                TrackInfo temp = new TrackInfo(file);
                // temp.setLengthVariables();
                trackInfos.add(temp);
                middleLeft.getChildren().add(temp.getParent());
            }
        }

        albumInfo = new AlbumInfo(parentFile);
        middleRight.getChildren().add(albumInfo.getParent());

        submit.setDisable(false);
    }

    private boolean isValidAudioFile(File file) {
        try {
            AudioSystem.getAudioInputStream(file);
            return true;
        } catch (UnsupportedAudioFileException | IOException e) {
            return false;
        }
    }




    private void submit() {
        String albumName = albumInfo.getAlbumName().trim();
        String artistName = albumInfo.getArtistName().trim();
        String genre = albumInfo.getGenre().trim();

        ArrayList<Track> tracks = new ArrayList<>();

        for (TrackInfo t : trackInfos) {
            Track temp = new Track(t.getFilepath().trim(), t.getName().trim(), t.getDuration().trim(), artistName, albumName, genre, t.getLengthInSeconds(), t.getIndexInAlbum());
            tracks.add(temp);
        }

        importing.setText("Importing...");

        boolean success = false;
        try (DBConnection connection = new DBConnection()) {
            tracks = connection.importMultiTrack(tracks);
            success = true;
        } catch (SQLException e) {
            System.err.println("ImportBox submit: failed to submit new information to database");
            e.printStackTrace();
        }


        if (success) {
            mediaListHandler.addMultiTrackToLists(tracks);
            importing.setText("Success");
            stage.close();
        } else {
            importing.setText("Failed");
            submit.setDisable(true);
        }


    }
}