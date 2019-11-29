package main.library;

import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.database.DBConnection;

public class TrackList {
    private ArrayList<Integer> trackIDs;
    private int currentTrackIndex = 0;
    public ObservableList<Track> tracks = FXCollections.observableArrayList();

    public TrackList () {
        try {
            trackIDs = getTrackIDs();
            while (hasMoreTracks()) {
                tracks.add(getNextTrackRow());
            }
        } catch (SQLException e) {
            System.err.println("Unable to access tracks ids");
            e.printStackTrace();
        }
    }

    private ArrayList<Integer> getTrackIDs() throws SQLException {
        try (DBConnection connection = new DBConnection()) {
            return connection.getIDs();
        }
    }

    public void refresh() {
        // System.out.println(trackIDs.get(currentTrackIndex - 1));
        try (DBConnection connection = new DBConnection()) {
            trackIDs.addAll(connection.getNewIDs(trackIDs.get(currentTrackIndex - 1)));

            // temp for verification
            printAllTrackIDs();
            while (hasMoreTracks()) {
                tracks.add(getNextTrackRow());
            }
        } catch (SQLException e) {
            System.out.println("Exception while updating trackIDs");
            e.printStackTrace();
        }
    }

    public Track getNextTrackRow() throws SQLException {
        try (DBConnection connection = new DBConnection()) {
            int id = trackIDs.get(currentTrackIndex);
            currentTrackIndex += 1;

            return new Track (
                id,
                connection.getFilepath(id),
                connection.getName(id),
                connection.getDuration(id),
                connection.getArtists(id),
                connection.getAlbums(id),
                connection.getGenres(id)
            );
        }
    }

    public boolean hasMoreTracks() {
        return trackIDs.size() > currentTrackIndex;
    }

    public int size() {
        return trackIDs.size();
    }

    private void printAllTrackIDs() {
        for (Integer id: trackIDs) {
            System.out.println(id);
        }
    }

    public void deleteTrack(Track trackToDelete) {
        try (DBConnection connection = new DBConnection()) {
            // System.out.println("Trackrows size before removal: " + tracks.size());
            int trackID = trackToDelete.getId();
            connection.removeTrackFromDB(trackID);
            // System.out.format("size: %d, IDs before removal", trackIDs.size());
            // printAllTrackIDs();
            // trackIDs.remove(trackID);
            trackIDs.remove(trackIDs.indexOf(trackID));
            // System.out.format("size: %d, IDs after removal", trackIDs.size());
            // printAllTrackIDs();
            tracks.remove(trackToDelete);
            currentTrackIndex -= 1;
            // System.out.println("Trackrows size after removal: " + tracks.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}