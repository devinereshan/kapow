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

    public void update(int albumID) {
        int indexOfAlbum = -1;
        try (DBConnection connection = new DBConnection()) {
            for (int i = 0; i < albums.size(); i++) {
                if (albums.get(i).getId() == albumID) {
                    indexOfAlbum = i;
                    break;
                }
            }

            if (indexOfAlbum != -1) {
                albums.remove(albums.get(indexOfAlbum));
                Album album = connection.getAlbum(albumID);
                if (album != null) {
                    albums.add(indexOfAlbum, album);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}