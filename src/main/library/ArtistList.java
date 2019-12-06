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

    public void update(Artist artist) {
        int artistID = artist.getId();
        int indexOfArtist = -1;
        for (int i = 0; i < artists.size(); i++) {
            if (artists.get(i).getId() == artistID) {
                indexOfArtist = i;
                break;
            }
        }

        if (indexOfArtist != -1) {
            artists.remove(artists.get(indexOfArtist));
            artists.add(indexOfArtist, artist);
        } else {
            add(artist);
        }
    }

    public void add(Artist artist) {
        int indexToInsert = -1;
        for (int i = 0; i < artists.size(); i++) {
            if (artists.get(i).getName().compareToIgnoreCase(artist.getName()) > 0) {
                indexToInsert = i;
                break;
            }
        }

        if (indexToInsert > -1) {
            artists.add(indexToInsert, artist);
        } else {
            artists.add(artist);
        }

    }


    public void delete(Artist artist) {
        int albumID = artist.getId();
        int indexToRemove = -1;
        for (int i = 0; i < artists.size(); i++) {
            if (artists.get(i).getId() == albumID) {
                indexToRemove = i;
                break;
            }
        }

        if (indexToRemove > -1) {
            artists.remove(indexToRemove);
        }
    }
}