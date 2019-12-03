package main.frontend;


import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.library.Album;
import main.library.AlbumList;

public class AlbumView {
    private AlbumList albumList;
    private TableView<Album> tableView = new TableView<>();
    TableColumn<Album,String> nameCol = new TableColumn<>("Name");
    TableColumn<Album,String> artistsCol = new TableColumn<>("Artists");
    TableColumn<Album,String> numberOfTracksCol = new TableColumn<>("Tracks");
    TableColumn<Album,String> genresCol = new TableColumn<>("Genres");


    public AlbumView() {
        albumList = new AlbumList();
        assignColumnValues();
    }

    private void assignColumnValues() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        artistsCol.setCellValueFactory(new PropertyValueFactory<>("artists"));
        numberOfTracksCol.setCellValueFactory(new PropertyValueFactory<>("numberOfTracks"));
        genresCol.setCellValueFactory(new PropertyValueFactory<>("genres"));
        tableView.getColumns().setAll(nameCol, artistsCol, numberOfTracksCol, genresCol);
        tableView.setItems(albumList.albums);
    }

    public TableView<Album> getTableView() {
        return tableView;
    }
}