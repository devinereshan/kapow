package main.library;

import java.util.ArrayList;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.database.DBConnection;
import main.library.Album;

public class AlbumList {
    private ArrayList<Integer> albumIDs;
    // private int currentAlbumIndex;
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

    }

    public ObservableList<Album> getAlbums() {
        return albums;
    }
}