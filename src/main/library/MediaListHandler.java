package main.library;

import java.sql.SQLException;
import java.util.ArrayList;

import main.database.DBConnection;

public class MediaListHandler {
    private static TrackList mainTrackList;
    private static AlbumList mainAlbumList;
    private static ArtistList mainArtistList;
    private static TrackList nestedTrackList;
    private static AlbumList nestedAlbumList;


    public static void setMainAlbumList(AlbumList albumList) {
        mainAlbumList = albumList;
    }

    public static void setMainTrackList(TrackList trackList) {
        mainTrackList = trackList;
    }

    public static void setNestedAlbumList(AlbumList albumList) {
        MediaListHandler.nestedAlbumList = albumList;
    }

    public static void setNestedTrackList(TrackList trackList) {
        nestedTrackList = trackList;
    }
    

    public static void setMainArtistList(ArtistList artistList) {
        mainArtistList = artistList;
    }

    public static void nullifyNestedTrackList() {
        nestedTrackList = null;
    }

    public static void nullifyNestedAlbumList() {
        nestedAlbumList = null;
    }

    public static void addMultiTrackToLists(ArrayList<Track> tracks) {
        for (Track track : tracks) {
            addTrackToLists(track);
        }
    }

    public static void hardRefresh() {
        mainTrackList.hardRefresh();
        mainAlbumList.hardRefresh();
        mainArtistList.hardRefresh();

        if (nestedAlbumList != null) {
            nestedAlbumList.hardRefresh();
        }
        
        if (nestedTrackList != null) {
            nestedTrackList.hardRefresh();
        }
    }

    public static void addTrackToLists(Track track) {
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
        if (nestedTrackList != null) {
            if (nestedTrackList.getAlbumID() == albumID) {
                nestedTrackList.addTrack(track);
            }
        }

        if (album != null) {
            mainAlbumList.update(album);

            if (nestedAlbumList != null) {
                if (nestedAlbumList.getArtistID() == artistID) {
                    nestedAlbumList.update(album);
                }
            }                    
        } else {
            System.err.println("MediaListHandler AddTrackToLists: Unable to locate album ID in Database");
        }

        if (nestedAlbumList != null) {
            if (nestedAlbumList.getArtistID() == artistID) {
                nestedAlbumList.update(album);
            }
        }

        if (artist != null) {
            mainArtistList.update(artist);
        } else {
            System.err.println("MediaListHandler AddTrackToLists: Unable to locate artist in database");
        }
    }


    public static void deleteTrack(Track track) {
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
        if (nestedTrackList != null) {
            if (nestedTrackList.getAlbumID() == albumID) {
                nestedTrackList.deleteTrack(track);
            }
        }


        if (album == null) {
            System.err.println("MediaListHandler: Unable to find album in database");
        } else {
            if (album.getNumberOfTracks() == 0) {
                mainAlbumList.delete(album);
                if (nestedAlbumList != null) {
                    if (nestedAlbumList.getArtistID() == artistID) {
                        nestedAlbumList.delete(album);
                    }
                }
            } else {
                mainAlbumList.update(album);
                if (nestedAlbumList != null) {
                    if (nestedAlbumList.getArtistID() == artistID) {
                        nestedAlbumList.update(album);
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


    public static void updateLists(Track track, Album newAlbum, Album oldAlbum, Artist newArtist, Artist oldArtist) {
        mainTrackList.updateTrack(track);
        if (nestedTrackList != null) {
            nestedTrackList.updateTrack(track);
        }

        int oldAlbumID = oldAlbum.getId();
        int oldArtistID = oldArtist.getId();
        try (DBConnection connection = new DBConnection()) {
            oldAlbum = connection.getAlbum(oldAlbumID);
            oldArtist = connection.getArtist(oldArtistID);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // if the album was changed, handle the old album
        if (oldAlbumID != newAlbum.getId() && oldAlbum.getNumberOfTracks() == 0) {
            mainAlbumList.delete(oldAlbum);
            if (nestedAlbumList != null) {
                nestedAlbumList.delete(oldAlbum);
            }
        } else if (oldAlbumID != newAlbum.getId()) {
            mainAlbumList.update(oldAlbum);
            if (nestedAlbumList != null) {
                nestedAlbumList.update(oldAlbum);
            }
        }

        // in any case, update or add the new album
        mainAlbumList.update(newAlbum);
        if (nestedAlbumList != null) {
            nestedAlbumList.update(newAlbum);
        }

        // if the artist was changed, handle the old album
        if (oldArtistID != newArtist.getId() && oldArtist.getNumberOfAlbums() == 0) {
            mainArtistList.delete(oldArtist);
        } else if (oldArtistID != newArtist.getId()) {
            mainArtistList.update(oldArtist);
        }

        // in any case, update or add the new artist
        mainArtistList.update(newArtist);
    }
}