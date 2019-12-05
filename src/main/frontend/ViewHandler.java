package main.frontend;

import javafx.scene.control.Tab;
import main.library.Album;
import main.library.AlbumList;
import main.library.Artist;
import main.library.TrackList;

public class ViewHandler {
    private TrackView mainTrackView;
    private AlbumView mainAlbumView;
    private ArtistView mainArtistView;
    private Tab mainTrackViewTab;
    private Tab mainAlbumViewTab;
    private Tab mainArtistViewTab;

    private TrackView nestedTrackView;
    private AlbumView nestedAlbumView;

    private AudioPlayerView audioPlayerView;

    // public ViewHandler(TrackView mainTrackView, AlbumView mainAlbumView, ArtistView mainArtistView, AudioPlayerView audioPlayerView) {
    //     this.mainTrackView = mainTrackView;
    //     this.mainAlbumView = mainAlbumView;
    //     this.mainArtistView = mainArtistView;
    //     this.audioPlayerView = audioPlayerView;

    //     mainTrackViewTab = new Tab("Tracks");
    //     mainTrackViewTab.setContent(mainTrackView.getContents());
    //     mainAlbumViewTab = new Tab("Albums");
    //     mainAlbumViewTab.setContent(mainAlbumView.getContents());
    //     mainArtistViewTab = new Tab("Artists");
    //     mainArtistViewTab.setContent(mainArtistView.getContents());
    // }

    public ViewHandler() {
        mainTrackViewTab = new Tab("Tracks");
        mainAlbumViewTab = new Tab("Albums");
        mainArtistViewTab = new Tab("Artists");
    }

    public void setMainViews(TrackView mainTrackView, AlbumView mainAlbumView, ArtistView mainArtistView, AudioPlayerView audioPlayerView) {
        this.mainTrackView = mainTrackView;
        this.mainAlbumView = mainAlbumView;
        this.mainArtistView = mainArtistView;
        this.audioPlayerView = audioPlayerView;

        mainTrackViewTab.setContent(mainTrackView.getContents());
        mainAlbumViewTab.setContent(mainAlbumView.getContents());
        mainArtistViewTab.setContent(mainArtistView.getContents());

        mainTrackViewTab.setClosable(false);
        mainAlbumViewTab.setClosable(false);
        mainArtistViewTab.setClosable(false);
    }

    public void switchToNestedAlbumView(Artist artist) {
        nestedAlbumView = new AlbumView(new AlbumList(artist.getId()), this, artist.getName());
        mainArtistViewTab.setContent(nestedAlbumView.getContents());
    }

    public void returnToParent(AlbumView nestedAlbum) {
        mainArtistViewTab.setContent(mainArtistView.getContents());
        nestedAlbumView = null;
        nestedAlbum = null;
    }

    public void switchToNestedTrackView(Album album) {
        nestedTrackView = new TrackView(new TrackList(album.getId(), "album"), this, album.getName());

        if (nestedAlbumView == null) {
            mainAlbumViewTab.setContent(nestedTrackView.getContents());
        } else {
            mainArtistViewTab.setContent(nestedTrackView.getContents());
        }
    }

    public void returnToParent(TrackView nestedTracks) {
        if (nestedAlbumView == null) {
            mainAlbumViewTab.setContent(mainAlbumView.getContents());
        } else {
            mainArtistViewTab.setContent(nestedAlbumView.getContents());
        }

        nestedTrackView = null;
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
}