package main.library;

import java.util.ArrayList;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.database.DBConnection;

public class ArtistList {
    private ArrayList<Integer> artistIDs;
    private ObservableList<Artist> artists = FXCollections.observableArrayList();


    public ArtistList() {
        try (DBConnection connection = new DBConnection()) {
            artistIDs = connection.getArtistIDs();

            for (Integer artistID : artistIDs) {
                artists.add(connection.getArtist(artistID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // construct an albumList instance for a single artist
    public ArtistList(int artistID) {

    }

    public ObservableList<Artist> getArtists() {
        return artists;
    }

    public void update(int artistID) {
        int indexOfArtist = -1;
        try (DBConnection connection = new DBConnection()) {
            for (int i = 0; i < artists.size(); i++) {
                if (artists.get(i).getId() == artistID) {
                    indexOfArtist = i;
                    break;
                }
            }

            if (indexOfArtist != -1) {
                artists.remove(artists.get(indexOfArtist));
                Artist artist = connection.getArtist(artistID);
                if (artist != null) {
                    artists.add(indexOfArtist, artist);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}