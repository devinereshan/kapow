package main.frontend;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.library.MediaListHandler;

public class ImportBox {
    private final Stage stage = new Stage();
    private final Button importFiles = new Button("Import Files");
    private final Button importDirectory = new Button("Import Directory");
    private MediaListHandler mediaListHandler;
    private VBox root;
    private HBox top;
    private HBox middle;
    private VBox middleLeft;
    private VBox middleRight;

    private List<File> files;
    private Label filepathLabel;
    private ArrayList<TrackInfo> trackInfos = new ArrayList<>();


    private class TrackInfo {
        File file;
        TextField nameField;
        Label nameLabel = new Label("Name:");
        TextField indexInAlbumField;
        Label indexLabel = new Label("Track #");
        VBox parent;
        HBox top;
        HBox bottom;

        public TrackInfo(File file) {
            this.file = file;
            nameField = new TextField();
            indexInAlbumField = new TextField();
            indexInAlbumField.setPrefWidth(40);

            top = new HBox(new Label(file.getName().toString()));
            bottom = new HBox(10, indexLabel, indexInAlbumField, nameLabel, nameField);
            parent = new VBox(5, top, bottom);

            // TODO auto fill text fields
        }


        public File getFile() {
            return file;
        }


        public TextField getIndexInAlbumField() {
            return indexInAlbumField;
        }


        public TextField getNameField() {
            return nameField;
        }

        public VBox getParent() {
            return parent;
        }
    }


    private class AlbumInfo {
        File file;
        TextField nameField;
        TextField artistField;
        TextField genreField;

        public AlbumInfo (File file) {
            this.file = file;
        }
    }


    public ImportBox (Stage primaryStage, MediaListHandler mediaListHandler) {
        this.mediaListHandler = mediaListHandler;

        filepathLabel = new Label("No Audio Files Selected");
        importFiles.setOnAction(e -> getFiles(stage));

        top = new HBox(10, importFiles, filepathLabel);
        middleLeft = new VBox(10);
        middleRight = new VBox(10);
        middle = new HBox(10, middleLeft, middleRight);

        root = new VBox();
        root.setPrefSize(600, 500);
        root.getChildren().addAll(top, middle);

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
            TrackInfo temp = new TrackInfo(file);
            trackInfos.add(temp);
            middleLeft.getChildren().add(temp.getParent());
        }

        addAlbumDetailsFields(parentFile);
    }


    private void addAlbumDetailsFields(File parentFile) {

    }
}