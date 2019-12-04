package main.frontend;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import main.library.MediaListHandler;
import main.library.Track;

public class GUI extends Application {
    private TableView<Track> table;
    TrackImportBox trackImportBox;
    TrackEditBox trackEditBox;
    TabPane views = new TabPane();
    TrackView trackView;
    private final ContextMenu contextMenu = new ContextMenu();
    MediaListHandler mediaListHandler = new MediaListHandler();
    AudioPlayerView audioPlayerView;


    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {
        audioPlayerView = new AudioPlayerView();

        trackView = new TrackView(mediaListHandler.getMainTrackList(), audioPlayerView);
        table = trackView.getTableView();
        buildContextMenu(primaryStage);

        AlbumView albumView = new AlbumView(mediaListHandler.getMainAlbumList(), audioPlayerView);
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

        root.setTop(audioPlayerView.getMainContainer());
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

        views.setContextMenu(contextMenu);
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
            audioPlayerView.loadTrackFromTable(table.getSelectionModel().getSelectedItem());
        }
    }
    public void deleteTrack(Track track) {
        mediaListHandler.deleteTrack(track);
    }
}