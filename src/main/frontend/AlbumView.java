package main.frontend;

import java.sql.SQLException;

import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import main.database.DBConnection;
import main.library.Album;
import main.library.AlbumList;
import main.library.Artist;

public class AlbumView {
    private AlbumList albumList;
    private VBox albumViewContents;
    String title = "kapow!";
    final TableView<Album> albumViewTable = new TableView<>();
    TableColumn<Album,String> nameCol = new TableColumn<>("Name");
    TableColumn<Album,String> artistsCol = new TableColumn<>("Artists");
    TableColumn<Album,String> numberOfTracksCol = new TableColumn<>("Tracks");
    TableColumn<Album,String> genresCol = new TableColumn<>("Genres");
    private Artist artist;
    private FilteredList<Album> filteredAlbums;


    public AlbumView() {
        albumList = new AlbumList();
        filteredAlbums = new FilteredList<>(albumList.getAlbums(), p -> true);
        assignColumnValues();
        albumViewTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }


    public AlbumView(Artist artist) {
        albumList = new AlbumList(artist.getId());
        filteredAlbums = new FilteredList<>(albumList.getAlbums(), p -> true);
        assignColumnValues();
        this.artist = artist;
        title = artist.getName();
        albumViewTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }


    private void assignColumnValues() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        artistsCol.setCellValueFactory(new PropertyValueFactory<>("artists"));
        numberOfTracksCol.setCellValueFactory(new PropertyValueFactory<>("numberOfTracks"));
        genresCol.setCellValueFactory(new PropertyValueFactory<>("genres"));
        albumViewTable.getColumns().setAll(nameCol, artistsCol, numberOfTracksCol, genresCol);
        albumViewTable.setItems(filteredAlbums);
    }

    public void filter(TextField searchBox) {
        filteredAlbums.setPredicate(p -> p.getSearchString().toLowerCase().contains(searchBox.getText().toLowerCase().trim()));
    }

    public String getTitle() {
        return title;
    }


    public void updateTitle() {
        if (artist != null) {
            try (DBConnection connection = new DBConnection()) {
                artist = connection.getArtist(artist.getId());
                title = artist.getName();
            } catch (SQLException e) {
                System.err.println("TrackView: Unable to update artist title");
                e.printStackTrace();
            }
        }
    }


    public TableView<Album> getTableView() {
        return albumViewTable;
    }


    public VBox getContents() {
        return (albumViewContents);
    }

    public AlbumList getList() {
        return albumList;
    }
}