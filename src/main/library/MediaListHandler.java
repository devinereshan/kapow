package main.library;

import java.sql.SQLException;

import main.database.DBConnection;

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


    public void addTrackToLists(Track track) {
        try (DBConnection connection = new DBConnection()) {
            int albumID = connection.getAlbumID(track.getAlbums(), track.getId());
            mainTrackList.addTrack(track);
            mainAlbumList.update(albumID);
        } catch (SQLException e) {
            e.printStackTrace();
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