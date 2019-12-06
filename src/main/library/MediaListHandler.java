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

        trackLists = new ArrayList<>();
        albumLists = new ArrayList<>();
        artistLists = new ArrayList<>();
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

    public void addTrackList(TrackList trackList) {
        trackLists.add(trackList);
    }

    public void removeTrackList(TrackList trackList) {
        trackLists.remove(trackList);
    }

    public void addAlbumList(AlbumList albumList) {
        albumLists.add(albumList);
    }

    public void removeAlbumList(AlbumList albumList) {
        albumLists.remove(albumList);
    }


    public void addTrackToLists(Track track) {
        Album album = null;
        Artist artist = null;
        int albumID = 0;
        int artistID = 0;
        try (DBConnection connection = new DBConnection()) {
            albumID = connection.getAlbumID(track.getAlbums(), track.getId());
            album = connection.getAlbum(albumID);
            artistID = connection.getArtistID(track.getArtists(), track.getId());
            artist = connection.getArtist(artistID);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        mainTrackList.addTrack(track);
        for (TrackList trackList : trackLists) {
            if (trackList.getAlbumID() == albumID) {
                trackList.addTrack(track);
            }
        }

        if (album != null) {
            mainAlbumList.update(album);
            for (AlbumList albumList : albumLists) {
                if (albumList.getArtistID() == artistID) {
                    albumList.update(album);
                }
            }
        } else {
            System.err.println("MediaListHandler AddTrackToLists: Unable to locate album ID in Database");
        }

        if (artist != null) {
            mainArtistList.update(artist);
        } else {
            System.err.println("MediaListHandler AddTrackToLists: Unable to locate artist in database");
        }
    }


    public void deleteTrack(Track track) {
        int albumID = 0;
        Album album = null;
        Artist artist = null;
        int artistID = 0;

        try (DBConnection connection = new DBConnection()) {
            albumID = connection.getAlbumID(track.getAlbums(), track.getId());
            artistID = connection.getArtistID(track.getArtists(), track.getId());

            connection.removeTrackFromDB(track.getId(), artistID, albumID);

            album = connection.getAlbum(albumID);
            artist = connection.getArtist(artistID);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        mainTrackList.deleteTrack(track);
        for (TrackList trackList : trackLists) {
            if (trackList.getAlbumID() == albumID) {
                trackList.deleteTrack(track);
            }
        }


        if (album == null) {
            System.err.println("MediaListHandler: Unable to find album in database");
        } else {
            if (album.getNumberOfTracks() == 0) {
                mainAlbumList.delete(album);
                for (AlbumList albumList : albumLists) {
                    if (albumList.getArtistID() == artistID) {
                        albumList.delete(album);
                    }
                }
            } else {
                mainAlbumList.update(album);
                for (AlbumList albumList : albumLists) {
                    if (albumList.getArtistID() == artistID) {
                        albumList.update(album);
                    }
                }
            }
        }

        if (artist == null) {
            System.err.println("MediaListHandler: Unable to find artist in database");
        } else {
            if (artist.getNumberOfAlbums() == 0) {
                mainArtistList.delete(artist);
            } else {
                mainArtistList.update(artist);
            }
        }
    }


    public void updateLists(Track newTrack, Track oldTrack) {
        //TODO
        // if (new)

    }
}