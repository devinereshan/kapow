package main.frontend;


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
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import main.library.Track;
import main.library.TrackList;
import main.player.AudioPlayer;
import main.player.ElapsedTimeListener;

public class GUI extends Application {
    private final Button seekLeft = new Button("<<");
    private final Button seekRight = new Button(">>");
    private final Button stopTrack = new Button("Stop");
    private final Button play = new Button("Play");
    private final Button pause = new Button("Pause");
    private final Button quit = new Button("Quit");

    private Label currentTrackName;
    private AudioPlayer audioPlayer;


    // Table view additions
    private TableView<Track> table = new TableView<>();
    private final TrackList trackList = new TrackList();

    TableColumn<Track,String> nameCol = new TableColumn<>("Name");
    TableColumn<Track,String> durationCol = new TableColumn<>("Duration");
    TableColumn<Track,String> artistsCol = new TableColumn<>("Artists");
    TableColumn<Track,String> albumsCol = new TableColumn<>("Albums");
    TableColumn<Track,String> genresCol = new TableColumn<>("Genres");
    TrackImportBox trackImportBox;
    TrackEditBox trackEditBox;

    private ElapsedTimeListener elapsedTimeListener;
    Slider elapsedTimeBar = new Slider(0, 1, 0);
    private Label elapsedTime = new Label("--:--");
    private Label totalTime = new Label("--:--");


    private final ContextMenu contextMenu = new ContextMenu();


    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {

        elapsedTimeListener = new ElapsedTimeListener(elapsedTimeBar);
        elapsedTime.textProperty().bind(elapsedTimeListener.elapsedTimeProperty());
        totalTime.textProperty().bind(elapsedTimeListener.totalTimeProperty());

        audioPlayer = new AudioPlayer(elapsedTimeListener);

        currentTrackName = new Label("No Track Selected");

        currentTrackName.setFont(new Font(16));


        mapButtons(primaryStage);
        assignColumnValues();


        buildContextMenu(primaryStage);


        table.setContextMenu(contextMenu);


        HBox trackName = new HBox(20, currentTrackName);
        trackName.setAlignment(Pos.CENTER);

        HBox timeBox = new HBox( elapsedTime, elapsedTimeBar, totalTime);
        HBox.setHgrow(elapsedTimeBar, Priority.ALWAYS);
        timeBox.setAlignment(Pos.CENTER);

        HBox buttonBar = new HBox(20, seekLeft, stopTrack, play, pause, seekRight, quit);
        buttonBar.setAlignment(Pos.CENTER);
        buttonBar.setPadding(new Insets(10));

        VBox player = new VBox(trackName, timeBox, buttonBar);

        TabPane views = new TabPane();
        Tab trackView = new Tab("Tracks");
        Tab albumView = new Tab("Albums");
        Tab artistView = new Tab("Artists");

        trackView.setContent(table);
        views.getTabs().add(trackView);
        views.getTabs().add(albumView);
        views.getTabs().add(artistView);

        trackView.setClosable(false);
        albumView.setClosable(false);
        artistView.setClosable(false);
        // VBox library = new VBox(table);


        BorderPane root = new BorderPane();

        root.setTop(player);
        // root.setCenter(library);
        root.setCenter(views);

        Scene scene = new Scene(root, 500, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Kapow! - Kool Audio Player, or whatever...");
        // primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
        //     @Override
        //     public void handle(WindowEvent t) {
        //         Platform.exit();
        //         // System.exit(0);
        //     }
        // });
        primaryStage.show();


    }


    private void buildContextMenu(Stage primaryStage) {
        MenuItem menuPlay = new MenuItem("play");
        menuPlay.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (table.getSelectionModel().getSelectedItem() != null) {
                    loadTrackFromTable(table.getSelectionModel().getSelectedItem());
                }
            }
        });

        MenuItem importTrack = new MenuItem("import track");
        importTrack.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                trackImportBox = new TrackImportBox(trackList);
                trackImportBox.open(primaryStage);
            }
        });

        MenuItem editTrack = new MenuItem("edit track");
        editTrack.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                trackEditBox = new TrackEditBox(table.getSelectionModel().getSelectedItem());
                trackEditBox.open(primaryStage);
            }
        });

        MenuItem delete = new MenuItem("delete");
        delete.setOnAction(e -> deleteTrack(table.getSelectionModel().getSelectedItem()));

        contextMenu.getItems().add(menuPlay);
        contextMenu.getItems().add(importTrack);
        contextMenu.getItems().add(delete);
        contextMenu.getItems().add(editTrack);

        table.setContextMenu(contextMenu);
    }


    public void deleteTrack(Track trackToDelete) {
        trackList.deleteTrack(trackToDelete);
    }

    private void mapButtons(Stage stage) {
        seekLeft.setOnAction(e -> seekLeft());
        seekRight.setOnAction(e -> seekRight());
        stopTrack.setOnAction(e -> stopTrack());
        pause.setOnAction(e -> pause());
        play.setOnAction(e -> play());
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
        table.setItems(trackList.tracks);
    }

    private void loadTrackFromTable(Track track) {
        audioPlayer.setAndPlay(track);
        currentTrackName.setText(track.getName() + " - " + track.getArtists());
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
        updatecurrentTrackName(audioPlayer.getCurrentTrack());
    }

    private void seekRight() {
        audioPlayer.seekRight();
        updatecurrentTrackName(audioPlayer.getCurrentTrack());
    }


    private void updatecurrentTrackName(Track track) {
        if (track != null) {
            currentTrackName.setText(track.getName() + " - " + track.getArtists());
        }
    }

}