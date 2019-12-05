package main.frontend;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import main.library.MediaListHandler;
import main.library.Track;

public class GUI extends Application {
    private TableView<Track> table;
    private final ContextMenu contextMenu = new ContextMenu();
    TrackImportBox trackImportBox;
    TrackEditBox trackEditBox;
    TabPane views = new TabPane();
    TrackView mainTrackView;
    ArtistView mainArtistView;
    AlbumView mainAlbumView;
    MediaListHandler mediaListHandler = new MediaListHandler();
    AudioPlayerView audioPlayerView;
    ViewHandler viewHandler;


    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {
        viewHandler = new ViewHandler();
        audioPlayerView = new AudioPlayerView();

        mainTrackView = new TrackView(mediaListHandler.getMainTrackList(), audioPlayerView);
        mainArtistView = new ArtistView(mediaListHandler.getMainArtistList(), viewHandler);
        mainAlbumView = new AlbumView(mediaListHandler.getMainAlbumList(), viewHandler);

        viewHandler.setMainViews(mainTrackView, mainAlbumView, mainArtistView, audioPlayerView);

        buildContextMenu(primaryStage);

        views.getTabs().add(viewHandler.getMainTrackViewTab());
        views.getTabs().add(viewHandler.getMainAlbumViewTab());
        views.getTabs().add(viewHandler.getMainArtistViewTab());


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