package main.library;

import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.database.DBConnection;

public class TrackRowList {
    private ArrayList<Integer> trackIDs;
    private int currentTrackIndex = 0;
    public ObservableList<TrackRow> trackRows = FXCollections.observableArrayList();

    public TrackRowList () {
        try {
            trackIDs = getTrackIDs();
            while (hasMoreTracks()) {
                trackRows.add(getNextTrackRow());
            }
        } catch (SQLException e) {
            System.err.println("Unable to access track ids");
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
                trackRows.add(getNextTrackRow());
            }
        } catch (SQLException e) {
            System.out.println("Exception while updating trackIDs");
            e.printStackTrace();
        }
    }

    public TrackRow getNextTrackRow() throws SQLException {
        try (DBConnection connection = new DBConnection()) {
            int id = trackIDs.get(currentTrackIndex);
            currentTrackIndex += 1;

            return new TrackRow (
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

    public void deleteTrack(TrackRow trackToDelete) {
        try (DBConnection connection = new DBConnection()) {
            // System.out.println("Trackrows size before removal: " + trackRows.size());
            int trackID = trackToDelete.getId();
            connection.removeTrackFromDB(trackID);
            // System.out.format("size: %d, IDs before removal", trackIDs.size());
            // printAllTrackIDs();
            // trackIDs.remove(trackID);
            trackIDs.remove(trackIDs.indexOf(trackID));
            // System.out.format("size: %d, IDs after removal", trackIDs.size());
            // printAllTrackIDs();
            trackRows.remove(trackToDelete);
            currentTrackIndex -= 1;
            // System.out.println("Trackrows size after removal: " + trackRows.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}