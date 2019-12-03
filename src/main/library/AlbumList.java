package main.library;

import java.util.ArrayList;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.database.DBConnection;
import main.library.Album;

public class AlbumList {
    private ArrayList<Integer> albumIDs;
    private int currentAlbumIndex;
    public ObservableList<Album> albums = FXCollections.observableArrayList();


    public AlbumList() {
        try (DBConnection connection = new DBConnection()) {
            albumIDs = connection.getAlbumIDs();

            // start a new thread here so the player doesn't have to wait on loading the enitre library
            while (albumIDs.size() > albums.size()) {
                int id = albumIDs.get(currentAlbumIndex);
                albums.add(connection.getAlbum(id));
                currentAlbumIndex += 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}