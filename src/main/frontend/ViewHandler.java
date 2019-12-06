package main.frontend;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import main.library.Album;
import main.library.AlbumList;
import main.library.Artist;
import main.library.MediaListHandler;
import main.library.Track;
import main.library.TrackList;

public class ViewHandler {
    TabPane views = new TabPane();
    private TrackView mainTrackView;
    private AlbumView mainAlbumView;
    private ArtistView mainArtistView;
    private Tab mainTrackViewTab;
    private Tab mainAlbumViewTab;
    private Tab mainArtistViewTab;
    private Tab currentTab;

    private TrackView nestedAlbumTrackView;
    private TrackView nestedArtistAlbumTrackView;
    private AlbumView nestedAlbumView;

    private AudioPlayerView audioPlayerView;
    private Stage primaryStage;
    private MediaListHandler mediaListHandler;

    public ViewHandler(Stage primaryStage) {
        this.primaryStage = primaryStage;
        mediaListHandler = new MediaListHandler();
        audioPlayerView = new AudioPlayerView();

        mainTrackViewTab = new Tab("Tracks");
        mainAlbumViewTab = new Tab("Albums");
        mainArtistViewTab = new Tab("Artists");

        mainArtistView = new ArtistView(mediaListHandler.getMainArtistList(), this);
        mainAlbumView = new AlbumView(mediaListHandler.getMainAlbumList(), this);
        mainTrackView = new TrackView(mediaListHandler.getMainTrackList(), this);

        mainTrackViewTab.setContent(mainTrackView.getContents());
        mainAlbumViewTab.setContent(mainAlbumView.getContents());
        mainArtistViewTab.setContent(mainArtistView.getContents());

        mainTrackViewTab.setClosable(false);
        mainAlbumViewTab.setClosable(false);
        mainArtistViewTab.setClosable(false);

        views.getTabs().add(mainArtistViewTab);
        views.getTabs().add(mainAlbumViewTab);
        views.getTabs().add(mainTrackViewTab);
    }


    public void switchToNestedAlbumView(Artist artist) {
        nestedAlbumView = new AlbumView(new AlbumList(artist.getId()), this, artist.getName());
        currentTab = views.getSelectionModel().getSelectedItem();
        if (currentTab.equals(mainArtistViewTab)) {
            mainArtistViewTab.setContent(nestedAlbumView.getContents());
        }
    }

    public void returnToParent(AlbumView nestedAlbum) {
        mainArtistViewTab.setContent(mainArtistView.getContents());
        nestedAlbumView = null;
        nestedAlbum = null;
    }

    public void switchToNestedTrackView(Album album) {
        currentTab = views.getSelectionModel().getSelectedItem();
        if (currentTab.equals(mainAlbumViewTab)) {
            nestedAlbumTrackView = new TrackView(new TrackList(album.getId(), "album"), this, album.getName());
            mainAlbumViewTab.setContent(nestedAlbumTrackView.getContents());
        } else if (currentTab.equals(mainArtistViewTab)) {
            nestedArtistAlbumTrackView = new TrackView(new TrackList(album.getId(), "album"), this, album.getName());
            mainArtistViewTab.setContent(nestedArtistAlbumTrackView.getContents());
        }
    }

    public void returnToParent(TrackView nestedTracks) {
        currentTab = views.getSelectionModel().getSelectedItem();

        if (currentTab.equals(mainAlbumViewTab)) {
            mainAlbumViewTab.setContent(mainAlbumView.getContents());
        } else if (currentTab.equals(mainArtistViewTab)) {
            mainArtistViewTab.setContent(nestedAlbumView.getContents());
        }
        nestedAlbumTrackView = null;
        nestedTracks = null;
    }


    public Tab getMainAlbumViewTab() {
        return mainAlbumViewTab;
    }


    public Tab getMainArtistViewTab() {
        return mainArtistViewTab;
    }


    public Tab getMainTrackViewTab() {
        return mainTrackViewTab;
    }

    public void play(Track track) {
        if (track != null) {
            audioPlayerView.loadTrackFromTable(track);
        }
    }

    public void importTrack() {
        TrackImportBox trackImportBox = new TrackImportBox(mediaListHandler);
        trackImportBox.open(primaryStage);
    }

    public AudioPlayerView getAudioPlayerView() {
        return audioPlayerView;
    }

	public void editTrack(Track track) {
        TrackEditBox trackEditBox = new TrackEditBox(track, mediaListHandler);
        trackEditBox.open(primaryStage);
    }

    public void deleteTrack(Track track) {
        mediaListHandler.deleteTrack(track);
    }
}