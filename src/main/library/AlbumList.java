package main.library;

import java.util.ArrayList;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.database.DBConnection;
import main.library.Album;

public class AlbumList {
    private ArrayList<Integer> albumIDs;
    private ObservableList<Album> albums = FXCollections.observableArrayList();


    public AlbumList() {
        try (DBConnection connection = new DBConnection()) {
            albumIDs = connection.getAlbumIDs();

            for (Integer albumID : albumIDs) {
                albums.add(connection.getAlbum(albumID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // construct an albumList instance for a single artist
    public AlbumList(int artistID) {
        try (DBConnection connection = new DBConnection()) {
            albumIDs = connection.getAlbumIDs(artistID);

            for (Integer albumID : albumIDs) {
                albums.add(connection.getAlbum(albumID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Album> getAlbums() {
        return albums;
    }

    public void update(Album album) {
        int albumID = album.getId();
        int indexOfAlbum = -1;
        for (int i = 0; i < albums.size(); i++) {
            if (albums.get(i).getId() == albumID) {
                indexOfAlbum = i;
                break;
            }
        }

        if (indexOfAlbum != -1) {
            // found album in list. Update it.
            albums.remove(albums.get(indexOfAlbum));
            albums.add(indexOfAlbum, album);
        } else {
            // album not in list. Add it
            add(album);
        }
    }

    public void add(Album album) {
        // insert into list based on album ordering
        int indexToInsert = -1;
        for (int i = 0; i < albums.size(); i++) {
            if (albums.get(i).getName().compareToIgnoreCase(album.getName()) > 0) {
                // the album name at i is alphabetically greater than the album name to insert
                indexToInsert = i;
                break;
            }
        }

        if (indexToInsert > -1) {
            albums.add(indexToInsert, album);
        } else {
            albums.add(album);
        }
    }
}