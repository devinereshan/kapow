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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.database.DBConnection;
import main.library.MediaListHandler;
import main.library.Track;
import main.player.TrackLengthCalculator;

public class ImportController {

    private class TrackInfo {
        File file;
        TitledPane titledPane;
        VBox infoBox;
        TextField nameField;
        Label nameLabel = new Label("Name:");
        TextField indexInAlbumField;
        Label indexLabel = new Label("Track #");
        HBox top;
        HBox middle;
        HBox bottom;
        int lengthInSeconds;
        String duration;
        Label artistsLabel = new Label("Artists:\t");
        CheckBox sameAsAlbumCheckbox = new CheckBox("Same as album");
        // ArrayList<String> artists = new ArrayList<>();
        // ArrayList<Label> artistNameLabels = new ArrayList<>();
        // ArrayList<Button> removeArtistButtons = new ArrayList<>();
        // ArrayList<HBox> nameButtonBoxes = new ArrayList<>();
        TextField newArtistField;
        Button addArtist = new Button("Add Artist");
        ListView<String> artistsListView;
        ObservableList<String> artistsList = FXCollections.observableArrayList();

        public TrackInfo(File file) {
            this.file = file;
            // Insets padding = new Insets(5, 5, 5, 40);
            nameField = new TextField();
            nameField.setPrefWidth(50);
            indexInAlbumField = new TextField();
            nameField.setPrefWidth(250);
            indexInAlbumField.setPrefWidth(40);

            artistsListView = new ListView<>(artistsList);
            // artistsListView.setItems(artistsList);
            VBox listViewBox = new VBox(artistsListView);
            artistsListView.setPrefHeight(100);
            // artistsListView.setMinHeight(0);
            
            artistsListView.setMinHeight(0);
            // VBox.setVgrow(artistsListView);

            top = new HBox(indexLabel, indexInAlbumField, nameLabel, nameField);
            top.setPadding(new Insets(0, 0, 5, 0));

            artistsLabel.setPadding(new Insets(5, 5, 5, 10));
            sameAsAlbumCheckbox.setSelected(true);
            middle = new HBox(artistsLabel, sameAsAlbumCheckbox);
            // middle.setPadding(new Insets(5, 5, 5, 10));
            middle.setAlignment(Pos.CENTER_LEFT);
            // artists.add("Same as album");
            // artistNameLabels.add(new Label(artists.get(0)));
            // artistNameLabels.get(0).setPadding(new Insets(5, 5, 5, 10));
            
            // removeArtistButtons.add(new Button("Remove"));
            // removeArtistButtons.get(0).setPadding(new Insets(5, 5, 5, 10));
            
            HBox spacer = new HBox();
            spacer.setMinWidth(5);
            
            // nameButtonBoxes.add(new HBox(removeArtistButtons.get(0), artistNameLabels.get(0)));
            // nameButtonBoxes.get(0).setPadding(new Insets(5, 5, 5, 15));

            newArtistField = new TextField();
            addArtist.setPadding(new Insets(5, 5, 5, 10));
            // newArtistField.setPadding(new Insets(5, 5, 5, 10));
            bottom = new HBox(addArtist, spacer, newArtistField);
            bottom.setPadding(new Insets(5, 5, 5, 15));


            // Label filePathLabel = new Label(file.getName().toString());
            // filePathLabel.setPadding(new Insets(10, 10, 5, 10));
            indexLabel.setPadding(new Insets(5, 5, 5, 10));
            nameLabel.setPadding(new Insets(5, 5, 5, 20));
            // Separator separator = new Separator();
            // trackListBox.getChildren().addAll(filePathLabel, top, artistsLabel, nameButtonBoxes.get(0), bottom, separator);
            infoBox = new VBox(top, middle, listViewBox, bottom);
            titledPane = new TitledPane(file.getName().toString(), infoBox);
            trackListBox.getPanes().add(titledPane);

            addArtist.setOnAction(e -> addArtist());

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

        private void addArtist() {
            String name = newArtistField.getText().trim();
            newArtistField.clear();
            if (name.length() > 0) {
                artistsList.add(name);
                System.out.println("Name is: " + name);
                artistsListView.setMinHeight((artistsList.size() * 24) + 2);
                // artistsListView.setMinHeight(artistsList.size() * 10);
                // int index = artists.size();
                // artists.add(name);
                // artistNameLabels.add(new Label(name));
                // removeArtistButtons.add(new Button("Remove"));
                // nameButtonBoxes.add(new HBox(removeArtistButtons.get(index), artistNameLabels.get(index)));
                // re
            }
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



    private File parentFile;
    private List<File> files;
    private ArrayList<TrackInfo> trackInfos = new ArrayList<>();

    
    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Accordion trackListBox;

    @FXML
    private Button browseButton;

    @FXML
    private Label filepathLabel;

    @FXML
    private TextField artistField;

    @FXML
    private TextField albumField;

    @FXML
    private TextField genreField;

    @FXML
    void browseClicked(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        getFiles(stage);
        confirmButton.setDisable(false);
    }

    @FXML
    void cancelClicked(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void confirmClicked(ActionEvent event) {
        submit();
    }

    private void getFiles(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.aiff",
                "*.au");
        fileChooser.getExtensionFilters().add(extFilter);

        files = fileChooser.showOpenMultipleDialog(stage);

        if (files != null) {
            addImportFields();
        }
    }

    private void addImportFields() {
        parentFile = files.get(0).getParentFile();
        filepathLabel.setText(parentFile.toString());

        for (File file : files) {
            if (isValidAudioFile(file)) {
                TrackInfo temp = new TrackInfo(file);
                trackInfos.add(temp);
            }
        }

        artistField.setText(parentFile.getParentFile().getName());
        albumField.setText(parentFile.getName());

        confirmButton.setDisable(false);
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
        String albumName = albumField.getText().trim();
        String artistName = artistField.getText().trim();
        String genre = genreField.getText().trim();

        ArrayList<Track> tracks = new ArrayList<>();

        for (TrackInfo t : trackInfos) {
            Track temp = new Track(t.getFilepath().trim(), t.getName().trim(), t.getDuration().trim(), artistName, albumName, genre, t.getLengthInSeconds(), t.getIndexInAlbum());
            tracks.add(temp);
        }

        boolean success = false;

        try (DBConnection connection = new DBConnection()) {
            connection.importMultiTrack(tracks);
            success = true;
        } catch (SQLException e) {
            System.err.println("ImportBox submit: failed to submit new information to database");
            e.printStackTrace();
        }


        if (success) {
            MediaListHandler.hardRefresh();
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        } else {
            // trackListBox.getChildren().clear();
            Label failed = new Label("Failed to import tracks");
            failed.setFont(new Font(30));
            failed.setPadding(new Insets(10, 10, 10, 10));
            // trackListBox.getChildren().add(failed);
            confirmButton.setDisable(true);
        }
    }
}