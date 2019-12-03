package main.library;

import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.database.DBConnection;

public class TrackList {
    private ArrayList<Integer> trackIDs;
    public ObservableList<Track> tracks = FXCollections.observableArrayList();

    public TrackList () {
        try (DBConnection connection = new DBConnection()) {
            trackIDs = getTrackIDs();
            for (Integer trackID : trackIDs) {
                tracks.add(connection.getTrack(trackID));
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


    public void addTrack(Track newTrack) {
        tracks.add(newTrack);
        trackIDs.add(newTrack.getId());
    }


    public int size() {
        return trackIDs.size();
    }


    public void deleteTrack(Track trackToDelete) {
        try (DBConnection connection = new DBConnection()) {
            int trackID = trackToDelete.getId();
            connection.removeTrackFromDB(trackID);
            trackIDs.remove(trackIDs.indexOf(trackID));
            tracks.remove(trackToDelete);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}