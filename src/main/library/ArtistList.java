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
        try (DBConnection connection = new DBConnection()) {
            for (Artist artist : artists) {
                if (artist.getId() == artistID) {
                    artists.remove(artist);
                    artists.add(connection.getArtist(artistID));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}