package main.frontend;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
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

public class ImportController implements Initializable {

    private class TrackInfo {
        final int ROW_HEIGHT = 24;
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
        TextField newArtistField;
        Button addArtist = new Button("Add Artist");
        ListView<String> artistsListView;
        ObservableList<String> artistsList = FXCollections.observableArrayList();
        ContextMenu listViewMenu;

        public TrackInfo(File file) {
            this.file = file;
            // Insets padding = new Insets(5, 5, 5, 40);
            nameField = new TextField();
            nameField.setPrefWidth(50);
            indexInAlbumField = new TextField();
            nameField.setPrefWidth(250);
            indexInAlbumField.setPrefWidth(40);

            artistsListView = new ListView<>(artistsList);
            VBox listViewBox = new VBox(artistsListView);
            listViewBox.setPadding(new Insets(5, 5, 5, 20));
            artistsListView.setPrefHeight(100);
            artistsListView.setMaxWidth(400);
            
            artistsListView.setMinHeight(0);
            
            listViewMenu = new ContextMenu();
            
            MenuItem remove = new MenuItem("Remove");
            remove.setOnAction(e -> removeArtist(artistsListView.getSelectionModel().getSelectedItem()));

            listViewMenu.getItems().add(remove);
            artistsListView.setContextMenu(listViewMenu);

            

            top = new HBox(indexLabel, indexInAlbumField, nameLabel, nameField);
            top.setPadding(new Insets(0, 0, 5, 0));

            artistsLabel.setPadding(new Insets(5, 5, 5, 10));
            sameAsAlbumCheckbox.setSelected(true);
            middle = new HBox(artistsLabel, sameAsAlbumCheckbox);
            middle.setAlignment(Pos.CENTER_LEFT);
            
            HBox spacer = new HBox();
            spacer.setMinWidth(5);
            
            newArtistField = new TextField();
            addArtist.setPadding(new Insets(5, 5, 5, 10));
            bottom = new HBox(addArtist, spacer, newArtistField);
            bottom.setPadding(new Insets(5, 5, 5, 15));

            indexLabel.setPadding(new Insets(5, 5, 5, 10));
            nameLabel.setPadding(new Insets(5, 5, 5, 20));
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
                artistsListView.setMinHeight((artistsList.size() * ROW_HEIGHT) + 2);
            }
        }

        private void removeArtist(String artist) {
            artistsList.remove(artist);
            if (artistsList.size() == 0) {
                artistsListView.setMinHeight(0);
            } else {
                artistsListView.setMinHeight((artistsList.size() * ROW_HEIGHT) + 2);
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
    private ObservableList<String> artistNames = FXCollections.observableArrayList();
    private ObservableList<String> genreNames = FXCollections.observableArrayList();

    @FXML
    private ListView<String> albumArtistsListView;

    @FXML
    private ListView<String> albumGenreListView;

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
    void addAlbumArtistClicked(ActionEvent event) {
        String name = artistField.getText().trim();
        artistField.clear();

        if(name.length() > 0) {
            artistNames.add(name);
            albumArtistsListView.setMinHeight((artistNames.size() * 24) + 2);
        }

    }

    @FXML
    void addAlbumGenreClicked(ActionEvent event) {
        String name = genreField.getText().trim();
        genreField.clear();

        if(name.length() > 0) {
            genreNames.add(name);
            albumGenreListView.setMinHeight((genreNames.size() * 24) + 2);
        }

    }
    
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

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        albumArtistsListView.setItems(artistNames);
        buildArtistContextMenu();
        albumGenreListView.setItems(genreNames);
        buildGenreContextMenu();
    }

    private void buildArtistContextMenu() {
        ContextMenu artistMenu = new ContextMenu();
        MenuItem remove = new MenuItem("Remove");
        remove.setOnAction(e -> removeArtist(albumArtistsListView.getSelectionModel().getSelectedItem()));
        artistMenu.getItems().add(remove);
        albumArtistsListView.setContextMenu(artistMenu);
    }

    private void buildGenreContextMenu() {
        ContextMenu genreMenu = new ContextMenu();
        MenuItem remove = new MenuItem("Remove");
        remove.setOnAction(e -> removeGenre(albumGenreListView.getSelectionModel().getSelectedItem()));
        genreMenu.getItems().add(remove);
        albumGenreListView.setContextMenu(genreMenu);
    }

    private void removeArtist(String artist) {
        if (artist != null) {
            artistNames.remove(artist);
            if (artistNames.size() == 0) {
                albumArtistsListView.setMinHeight(0);
            } else {
                albumArtistsListView.setMinHeight((artistNames.size() * 24) + 2);
            }
        }
    }

    private void removeGenre(String genre) {
        if (genre != null) {
            genreNames.remove(genre);
            if (genreNames.size() == 0) {
                albumGenreListView.setMinHeight(0);
            } else {
                albumGenreListView.setMinHeight((genreNames.size() * 24) + 2);
            }
        }
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