package main.frontend;

import java.io.File;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.library.TrackRow;
import main.library.TrackRowList;
import main.player.AudioPlayer;

public class GUI extends Application {
    private final Button seekLeft = new Button("<<");
    private final Button seekRight = new Button(">>");
    private final Button stopTrack = new Button("Stop");
    private final Button play = new Button("Play");
    private final Button pause = new Button("Pause");
    private final Button load = new Button("Load");
    private final Button quit = new Button("Quit");

    private Label currentTrackName;
    private AudioPlayer audioPlayer;


    // Table view additions
    private TableView<TrackRow> table = new TableView<>();
    private final TrackRowList trackRowList = new TrackRowList();

    TableColumn<TrackRow,String> nameCol = new TableColumn<>("Name");
    TableColumn<TrackRow,String> durationCol = new TableColumn<>("Duration");
    TableColumn<TrackRow,String> artistsCol = new TableColumn<>("Artists");
    TableColumn<TrackRow,String> albumsCol = new TableColumn<>("Albums");
    TableColumn<TrackRow,String> genresCol = new TableColumn<>("Genres");
    TrackImportBox trackImportBox;


    private final ContextMenu contextMenu = new ContextMenu();


    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {

        audioPlayer = new AudioPlayer();

        currentTrackName = new Label("No Track Selected");

        currentTrackName.setFont(new Font(16));


        mapButtons(primaryStage);
        assignColumnValues();


        buildContextMenu(primaryStage);


        table.setContextMenu(contextMenu);


        HBox trackName = new HBox(20, currentTrackName);
        trackName.setAlignment(Pos.CENTER);

        HBox buttonBar = new HBox(20, load, seekLeft, stopTrack, play, pause, seekRight, quit);
        buttonBar.setAlignment(Pos.CENTER);
        buttonBar.setPadding(new Insets(10));

        VBox player = new VBox(trackName, buttonBar);

        VBox library = new VBox(table);

        BorderPane root = new BorderPane();

        root.setTop(player);
        root.setCenter(library);

        Scene scene = new Scene(root, 500, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Kapow! - Kool Audio Player, or whatever...");
        primaryStage.show();


    }



    private void buildContextMenu(Stage primaryStage) {
        MenuItem menuPlay = new MenuItem("play");
        menuPlay.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                loadTrackFromTable(table.getSelectionModel().getSelectedItem());
            }
        });

        MenuItem importTrack = new MenuItem("import track");
        importTrack.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                trackImportBox = new TrackImportBox(trackRowList);
                trackImportBox.open(primaryStage);
            }
        });

        MenuItem delete = new MenuItem("delete");
        delete.setOnAction(e -> deleteTrack(table.getSelectionModel().getSelectedItem()));

        contextMenu.getItems().add(menuPlay);
        contextMenu.getItems().add(importTrack);
        contextMenu.getItems().add(delete);

        table.setContextMenu(contextMenu);
    }


    public void deleteTrack(TrackRow trackToDelete) {
        trackRowList.deleteTrack(trackToDelete);
    }

    private void mapButtons(Stage stage) {
        seekLeft.setOnAction(e -> seekLeft());
        seekRight.setOnAction(e -> seekRight());
        stopTrack.setOnAction(e -> stopTrack());
        pause.setOnAction(e -> pause());
        play.setOnAction(e -> play());
        load.setOnAction(e -> loadAudioFile(stage));
        quit.setOnAction(e -> {
            audioPlayer.quit();
            Platform.exit();
        });
    }


    private void assignColumnValues() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        artistsCol.setCellValueFactory(new PropertyValueFactory<>("artists"));
        albumsCol.setCellValueFactory(new PropertyValueFactory<>("albums"));
        genresCol.setCellValueFactory(new PropertyValueFactory<>("genres"));
        table.getColumns().setAll(nameCol, durationCol, artistsCol, albumsCol, genresCol);
        table.setItems(trackRowList.trackRows);
    }

    private void loadTrackFromTable(TrackRow track) {
        File audioFile = new File(track.getFilepath());
        if (audioFile != null) {
            audioPlayer.setAndPlay(audioFile);
            currentTrackName.setText(track.getName() + " - " + track.getArtists());
        }
    }

    private File getAudioFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.aiff", "*.au");
        fileChooser.getExtensionFilters().add(extFilter);
        return fileChooser.showOpenDialog(stage);
    }

    private void loadAudioFile(Stage stage) {
        File audioFile = getAudioFile(stage);
        if (audioFile != null) {
            audioPlayer.queueTrack(audioFile);
            audioPlayer.printQueue(); // test
            currentTrackName.setText(audioPlayer.getCurrentTrackName());
        }
    }

    private void stopTrack() {
        audioPlayer.stop();
    }

    private void play() {
        audioPlayer.play();
    }

    private void pause() {
        audioPlayer.pause();
    }

    private void seekLeft() {
        audioPlayer.seekLeft();
        currentTrackName.setText(audioPlayer.getCurrentTrackName());
    }

    private void seekRight() {
        audioPlayer.seekRight();
        currentTrackName.setText(audioPlayer.getCurrentTrackName());
    }



}