package main.library;

import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.database.DBConnection;

public class TrackList {
    private ArrayList<Integer> trackIDs;
    public ObservableList<Track> tracks = FXCollections.observableArrayList();
    int albumID = 0;

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
                this.albumID = parentID;
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
        tracks.remove(trackToDelete);
    }

    public void updateTrack(Track trackToUpdate) {
        int trackID = trackToUpdate.getId();
        int indexToUpdate = -1;

        for (int i = 0; i < tracks.size(); i++) {
            if (tracks.get(i).getId() == trackID) {
                indexToUpdate = i;
                break;
            }
        }

        if (indexToUpdate != -1) {
            tracks.remove(tracks.get(indexToUpdate));
            tracks.add(indexToUpdate, trackToUpdate);
        }
    }


    public ObservableList<Track> getTracks() {
        return tracks;
    }

    public int getAlbumID() {
        return albumID;
    }

    public void hardRefresh() {
        trackIDs.clear();
        tracks.clear();
        try (DBConnection connection = new DBConnection()) {
            if (albumID > 0) {
                trackIDs = connection.getTrackAlbumIDs(albumID);
            } else {
                trackIDs = connection.getIDs();
            }
            
            for (Integer trackID : trackIDs) {
                tracks.add(connection.getTrack(trackID));
            }
        } catch (SQLException e) {
            System.err.println("TrackList: Hard refresh failed");
            e.printStackTrace();
        }
    }
}