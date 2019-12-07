package main.frontend;

import java.io.File;
import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.library.MediaListHandler;

public class ImportBox {
    private final Stage stage = new Stage();
    private final Button importFiles = new Button("Import Files");
    private final Button importDirectory = new Button("Import Directory");
    private MediaListHandler mediaListHandler;
    private HBox root;
    private GridPane gridPane;
    private int row;

    private List<File> files;
    private Label filepathLabel;

    public ImportBox (Stage primaryStage, MediaListHandler mediaListHandler) {
        this.mediaListHandler = mediaListHandler;
        row = 0;


        filepathLabel = new Label("No Audio Files Selected");
        importFiles.setOnAction(e -> getFiles(stage));

        gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(5);
        gridPane.setPadding(new Insets(10));

        gridPane.add(importFiles, 0, row);
        gridPane.add(filepathLabel, 2, row++);

        root = new HBox();
        root.setPrefSize(600, 500);
        root.getChildren().addAll(gridPane);

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
            filepathLabel.setText("");
            addImportFields(files);
        }
    }

    private void addImportFields(List<File> files) {
        gridPane.add(new Label(files.get(0).getParent().toString()), 0, row++);
        for (File file : files) {
            gridPane.add(new Label(file.getName().toString()), 0, row++);
        }
    }
}