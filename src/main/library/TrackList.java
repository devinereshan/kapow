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
            trackIDs = connection.getIDs();
            for (Integer trackID : trackIDs) {
                tracks.add(connection.getTrack(trackID));
            }
        } catch (SQLException e) {
            System.err.println("Unable to access tracks ids");
            e.printStackTrace();
        }
    }

    // constructor for single album or artist track list
    public TrackList(int parentID, String parentName) {
        try (DBConnection connection = new DBConnection()) {
            if (parentName == "album") {
                trackIDs = connection.getTrackAlbumIDs(parentID);
            } else if (parentName == "artist") {
                // TODO
                // trackIDs = connection.getTrackArtistIDs(parentID);
            } else {
                // not valid parent
                System.err.println("Unrecognized parent: " + parentName);
            }
            for (Integer trackID : trackIDs) {
                tracks.add(connection.getTrack(trackID));
            }
        } catch (SQLException e) {
            System.err.println("Unable to access tracks ids");
            e.printStackTrace();
        }
    }


    public void addTrack(Track newTrack) {
        tracks.add(newTrack);
        trackIDs.add(newTrack.getId());
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


    public ObservableList<Track> getTracks() {
        return tracks;
    }
}