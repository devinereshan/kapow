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

    }

    public ObservableList<Album> getAlbums() {
        return albums;
    }

    public void update(int albumID) {
        try (DBConnection connection = new DBConnection()) {
            for (Album album : albums) {
                if (album.getId() == albumID) {
                    albums.remove(album);
                    albums.add(connection.getAlbum(albumID));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}