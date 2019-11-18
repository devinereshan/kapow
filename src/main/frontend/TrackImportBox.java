package main.frontend;

import java.io.File;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TrackImportBox {
    final Stage importBox = new Stage();
    GridPane root = new GridPane();

    String filepath = "No File Selected.";

    Label file = new Label("File:");
    Label filepathLabel = new Label(filepath);
    Label artist = new Label("Artist Name:");
    Label album = new Label("Album Name:");
    Label track = new Label("Track Name:");
    Label genre = new Label("Genre:");

    TextField artistField = new TextField();
    TextField albumField = new TextField();
    TextField trackField = new TextField();
    TextField genreField = new TextField();
    Button browseFile = new Button("Browse");
    Button submit = new Button("Submit");
    Button cancel = new Button("Cancel");

    int row = 0;



    public TrackImportBox(Stage primaryStage) {
        importBox.initModality(Modality.APPLICATION_MODAL);
        importBox.initOwner(primaryStage);
        root.setVgap(10);
        root.setHgap(5);
        root.setPadding(new Insets(10));

        browseFile.setOnAction(e -> loadAudioFile(importBox));

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
        importBox.show();

    }


    private void loadAudioFile(Stage stage) {
        File audioFile = getAudioFile(stage);
        if (audioFile != null) {
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
}