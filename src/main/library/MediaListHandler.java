package main.library;

import java.sql.SQLException;
import java.util.ArrayList;

import main.database.DBConnection;

public class MediaListHandler {
    TrackList mainTrackList;
    AlbumList mainAlbumList;
    ArtistList mainArtistList;

    ArrayList<TrackList> trackLists;
    ArrayList<AlbumList> albumLists;
    ArrayList<ArtistList> artistLists;


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
        int artistID = 0;
        try (DBConnection connection = new DBConnection()) {
            albumID = connection.getAlbumID(track.getAlbums(), track.getId());
            artistID = connection.getArtistID(track.getArtists(), track.getId());
            connection.removeTrackFromDB(track.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        mainTrackList.deleteTrack(track);

        if (albumID == 0) {
            System.err.println("MediaListHandler: Unable to find album ID in database");
        } else {
            mainAlbumList.update(albumID);
        }

        if (artistID == 0) {
            System.err.println("MediaListHandler: Unable to find artist ID in database");
        } else {
            mainArtistList.update(artistID);
        }

    }


    public void updateLists(Track newTrack, Track oldTrack) {
        // if (new)
    }
}