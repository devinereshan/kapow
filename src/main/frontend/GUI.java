package main.frontend;


import javafx.application.Application;
import javafx.application.Platform;
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
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import main.library.MediaListHandler;
import main.library.Track;
import main.player.AudioPlayer;
import main.player.ElapsedTimeListener;
import javafx.scene.control.TableRow;

public class GUI extends Application {
    private final Button seekLeft = new Button("<<");
    private final Button seekRight = new Button(">>");
    private final Button stopTrack = new Button("Stop");
    private final Button play = new Button("Play");
    private final Button pause = new Button("Pause");
    private final Button quit = new Button("Quit");

    private Label currentTrackName;
    private AudioPlayer audioPlayer;


    private TableView<Track> table;
    TrackImportBox trackImportBox;
    TrackEditBox trackEditBox;

    private ElapsedTimeListener elapsedTimeListener;
    Slider elapsedTimeBar = new Slider(0, 1, 0);
    private Label elapsedTime = new Label("--:--");
    private Label totalTime = new Label("--:--");


    TrackView trackView;

    private final ContextMenu contextMenu = new ContextMenu();


    MediaListHandler mediaListHandler = new MediaListHandler();


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


        trackView = new TrackView(mediaListHandler.getMainTrackList());
        table = trackView.getTableView();
        buildContextMenu(primaryStage);

        table.setRowFactory(tv -> {
            TableRow<Track> track = new TableRow<>();
            track.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!track.isEmpty())) {
                    loadTrackFromTable(track.getItem());
                }
            });
            return track;
        });


        HBox trackName = new HBox(20, currentTrackName);
        trackName.setAlignment(Pos.CENTER);

        HBox timeBox = new HBox( elapsedTime, elapsedTimeBar, totalTime);
        HBox.setHgrow(elapsedTimeBar, Priority.ALWAYS);
        timeBox.setAlignment(Pos.CENTER);

        HBox buttonBar = new HBox(20, seekLeft, stopTrack, play, pause, seekRight, quit);
        buttonBar.setAlignment(Pos.CENTER);
        buttonBar.setPadding(new Insets(10));

        VBox player = new VBox(trackName, timeBox, buttonBar);

        AlbumView albumView = new AlbumView(mediaListHandler.getMainAlbumList());
        TabPane views = new TabPane();
        Tab trackViewTab = new Tab("Tracks");
        Tab albumViewTab = albumView.getTab();
        Tab artistViewTab = new Tab("Artists");

        trackViewTab.setContent(trackView.getTableView());

        views.getTabs().add(trackViewTab);
        views.getTabs().add(albumViewTab);
        views.getTabs().add(artistViewTab);

        trackViewTab.setClosable(false);
        albumViewTab.setClosable(false);
        artistViewTab.setClosable(false);


        BorderPane root = new BorderPane();

        root.setTop(player);
        root.setCenter(views);

        Scene scene = new Scene(root, 500, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Kapow! - Kool Audio Player, or whatever...");
        primaryStage.show();
    }


    private void buildContextMenu(Stage primaryStage) {
        MenuItem menuPlay = new MenuItem("play");
        menuPlay.setOnAction(e -> playSelectedTrack());

        MenuItem importTrack = new MenuItem("import track");
        importTrack.setOnAction(e -> importNewTrack(primaryStage));

        MenuItem editTrack = new MenuItem("edit track");
        editTrack.setOnAction(e -> editTrack(primaryStage));

        MenuItem delete = new MenuItem("delete");
        delete.setOnAction(e -> deleteTrack(table.getSelectionModel().getSelectedItem()));

        contextMenu.getItems().add(menuPlay);
        contextMenu.getItems().add(importTrack);
        contextMenu.getItems().add(delete);
        contextMenu.getItems().add(editTrack);

        table.setContextMenu(contextMenu);
    }

    public void editTrack(Stage primaryStage) {
        trackEditBox = new TrackEditBox(table.getSelectionModel().getSelectedItem());
        trackEditBox.open(primaryStage);
    }

    public void importNewTrack(Stage primaryStage) {
        trackImportBox = new TrackImportBox(mediaListHandler);
        trackImportBox.open(primaryStage);
    }

    public void playSelectedTrack() {
        if (table.getSelectionModel().getSelectedItem() != null) {
            loadTrackFromTable(table.getSelectionModel().getSelectedItem());
        }
    }
    public void deleteTrack(Track track) {
        // trackView.deleteTrack(trackToDelete);
        mediaListHandler.deleteTrack(track);
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