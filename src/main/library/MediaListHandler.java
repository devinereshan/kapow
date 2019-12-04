package main.library;

public class MediaListHandler {
    TrackList mainTrackList;
    AlbumList mainAlbumList;


    public MediaListHandler() {
        mainTrackList = new TrackList();
        mainAlbumList = new AlbumList();
    }


    public AlbumList getMainAlbumList() {
        return mainAlbumList;
    }


    public TrackList getMainTrackList() {
        return mainTrackList;
    }

    public void updateTrackList(Track track) {
        mainTrackList.addTrack(track);
    }

    public void updateAlbumList(int albumID) {
        mainAlbumList.update(albumID);
    }

    public void updateLists() {
        // TODO
    }
}