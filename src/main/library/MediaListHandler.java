package main.library;

import java.sql.SQLException;

import main.database.DBConnection;

public class MediaListHandler {
    TrackList mainTrackList;
    AlbumList mainAlbumList;
    ArtistList mainArtistList;


    public MediaListHandler() {
        mainTrackList = new TrackList();
        mainAlbumList = new AlbumList();
        mainArtistList = new ArtistList();
    }


    public AlbumList getMainAlbumList() {
        return mainAlbumList;
    }


    public TrackList getMainTrackList() {
        return mainTrackList;
    }

    public ArtistList getMainArtistList() {
        return mainArtistList;
    }


    public void addTrackToLists(Track track) {
        int albumID = 0;
        try (DBConnection connection = new DBConnection()) {
            albumID = connection.getAlbumID(track.getAlbums(), track.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        mainTrackList.addTrack(track);
        if (albumID == 0) {
            System.err.println("MediaListHandler: Unable to locate album ID in Database");
        } else {
            mainAlbumList.update(albumID);
        }
    }


    public void deleteTrack(Track track) {
        int albumID = 0;
        try (DBConnection connection = new DBConnection()) {
            albumID = connection.getAlbumID(track.getAlbums(), track.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        mainTrackList.deleteTrack(track);

        if (albumID == 0) {
            System.err.println("Unable to access database to update album view");
        } else {
            mainAlbumList.update(albumID);
        }
    }
}