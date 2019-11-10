package main.library;

import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.database.DBConnection;
import main.database.DatabaseConnection;

// Track List should be initialized in it's own thread ()
public class TrackList {
    // DatabaseConnection databaseConnection;
    public static ObservableList<TrackListing> tracks;

    public TrackList() throws SQLException {
        buildTrackList();
    }

    public void buildTrackList() throws SQLException {
        tracks = FXCollections.observableArrayList();

        // String filepath, name, duration, artists, albums, genres;

        try (DBConnection connection = new DBConnection()) {
            for (Integer track_id : connection.getAllTrackID()) {
                TrackListing track = new TrackListing();
                track.setID(track_id);
                track.setFilepath(connection.getFilepath(track_id));
                track.setName(connection.getName(track_id));
                track.setDuration(connection.getDuration(track_id));
                track.setArtists(connection.getArtists(track_id));
                track.setAlbums(connection.getAlbums(track_id));
                track.setGenres(connection.getGenres(track_id));
                System.out.format("%-20s %-10s %-20s %-20s %-20s %-1s\n", track.getName(), track.getDuration(), track.getArtists(), track.getAlbums(), track.getGenres(), track.getFilepath());

                tracks.add(track);
            }
        }

        // for (String filepath: databaseConnection.getTrackFilePaths()) {
        //     tracks.add(buildTrackListing(filepath));
        // }
        // for (String[] track: databaseConnection.getTrackLibrary()) {
        //     TrackListing trackInfo = new TrackListing();
        //     trackInfo.setName(track[0]);
        //     trackInfo.setDuration(track[1]);
            // trackInfo.setArtists(track[2]);

            // tracks.add()
        // }
    }


    private TrackListing buildTrackListing(String filepath) {

        TrackListing trackListing = new TrackListing();
        // String name = databaseConnection.getTrackName(filepath);
        // String duration = data
        // String artists = databaseConnection.getArtists(filepath);
        // String albums = databaseConnection.getAlbums(filepath);
        // String
        trackListing.setFilepath(filepath);
        // trackListing.setName(databaseConnection.getName(filepath));
        // trackListing.setDuration(databaseConnection.getDuration(filepath));
        // trackListing.setArtists(databaseConnection.getArtists(filepath));
        // trackListing.setAlbums(databaseConnection.getAlbums(filepath));
        // trackListing.setGenres(databaseConnection.getGenres(filepath));

        return trackListing;
    }

    public static ObservableList<TrackListing> getTracks() {
        // ObservableList<TrackListing> tracks = FXCollections.observableArrayList();

        // TrackListing track1 = new TrackListing();
        // track1.setName("Track1");
        // track1.setDuration("1:23");

        // TrackListing track2 = new TrackListing();
        // track2.setName("track2");
        // track2.setDuration("2:34");
        // tracks.addAll(track1, track2);

        return tracks;
    }

}