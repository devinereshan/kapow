package main.frontend;

import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.database.DBConnection;
import main.library.Album;

public class AlbumView {
    private ArrayList<Integer> albumIDs;
    private int currentAlbumIndex;
    public ObservableList<Album> albums = FXCollections.observableArrayList();
    private TableView<Album> tableView = new TableView<>();
    TableColumn<Album,String> nameCol = new TableColumn<>("Name");
    TableColumn<Album,String> artistsCol = new TableColumn<>("Artists");
    TableColumn<Album,String> numberOfTracksCol = new TableColumn<>("Tracks");
    TableColumn<Album,String> genresCol = new TableColumn<>("Genres");


    public AlbumView() {
        try (DBConnection connection = new DBConnection()) {
            albumIDs = connection.getAlbumIDs();

            // start a new thread here so the player doesn't have to wait on loading the enitre library
            while (albumIDs.size() > albums.size()) {
                int id = albumIDs.get(currentAlbumIndex);
                albums.add(connection.getAlbum(id));
                currentAlbumIndex += 1;
            }
            assignColumnValues();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void assignColumnValues() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        artistsCol.setCellValueFactory(new PropertyValueFactory<>("artists"));
        numberOfTracksCol.setCellValueFactory(new PropertyValueFactory<>("numberOfTracks"));
        genresCol.setCellValueFactory(new PropertyValueFactory<>("genres"));
        tableView.getColumns().setAll(nameCol, artistsCol, numberOfTracksCol, genresCol);
        tableView.setItems(albums);
    }

    public TableView<Album> getTableView() {
        return tableView;
    }

}