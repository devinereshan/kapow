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
        Album album = null;
        // TODO: Artist artist = null;
        try (DBConnection connection = new DBConnection()) {
            int albumID = connection.getAlbumID(track.getAlbums(), track.getId());
            album = connection.getAlbum(albumID);
            System.out.println("got album " + album.getName());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        mainTrackList.addTrack(track);

        if (album != null) {
            mainAlbumList.update(album);
        } else {
            System.err.println("MediaListHandler: Unable to locate album ID in Database");
        }
    }


    public void deleteTrack(Track track) {
        int albumID = 0;
        Album album = null;
        Album updatedAlbum = null;
        int artistID = 0;
        try (DBConnection connection = new DBConnection()) {
            albumID = connection.getAlbumID(track.getAlbums(), track.getId());
            album = connection.getAlbum(albumID);
            artistID = connection.getArtistID(track.getArtists(), track.getId());
            connection.removeTrackFromDB(track.getId(), artistID, albumID);
            updatedAlbum = connection.getAlbum(albumID);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        mainTrackList.deleteTrack(track);

        if (album == null) {
            System.err.println("MediaListHandler: Unable to find album in database");
        } else {
            if (updatedAlbum == null) {
                mainAlbumList.delete(album);
            } else {
                mainAlbumList.update(updatedAlbum);
            }
        }

        if (artistID == 0) {
            System.err.println("MediaListHandler: Unable to find artist ID in database");
        } else {
            // TODO
            // mainArtistList.update(artistID);
        }

    }


    public void updateLists(Track newTrack, Track oldTrack) {
        //TODO
        // if (new)

    }
}