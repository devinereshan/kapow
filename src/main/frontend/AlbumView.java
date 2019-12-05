package main.frontend;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import main.library.Album;
import main.library.AlbumList;

public class AlbumView {
    private AlbumList albumList;
    private Tab albumViewTab = new Tab("Albums");
    private VBox albumViewContents;
    private Button returnToParent;
    private Label currentArtistLabel;
    private TableView<Album> albumViewTable = new TableView<>();
    TableColumn<Album,String> nameCol = new TableColumn<>("Name");
    TableColumn<Album,String> artistsCol = new TableColumn<>("Artists");
    TableColumn<Album,String> numberOfTracksCol = new TableColumn<>("Tracks");
    TableColumn<Album,String> genresCol = new TableColumn<>("Genres");
    private ViewHandler viewHandler;


    public AlbumView(AlbumList albumList, ViewHandler viewHandler) {
        this.albumList = albumList;
        this.viewHandler = viewHandler;
        assignColumnValues();
        albumViewContents = new VBox(albumViewTable);
        albumViewTab.setContent(albumViewContents);
        makeInteractive();
    }


    public AlbumView(AlbumList albumList, ViewHandler viewHandler, String parentName) {
        this.albumList = albumList;
        this.viewHandler = viewHandler;
        returnToParent = new Button("Back To Artists");
        currentArtistLabel = new Label(parentName);
        assignColumnValues();
        albumViewContents = new VBox(returnToParent, currentArtistLabel, albumViewTable);
        albumViewTab.setContent(albumViewContents);

        returnToParent.setOnAction(e -> viewHandler.returnToParent(this));

        makeInteractive();
    }


    private void assignColumnValues() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        artistsCol.setCellValueFactory(new PropertyValueFactory<>("artists"));
        numberOfTracksCol.setCellValueFactory(new PropertyValueFactory<>("numberOfTracks"));
        genresCol.setCellValueFactory(new PropertyValueFactory<>("genres"));
        albumViewTable.getColumns().setAll(nameCol, artistsCol, numberOfTracksCol, genresCol);
        albumViewTable.setItems(albumList.getAlbums());
    }


    public void makeInteractive() {
        albumViewTable.setRowFactory(tv -> {
            TableRow<Album> albumRow = new TableRow<>();
            albumRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!albumRow.isEmpty())) {
                    viewHandler.switchToNestedTrackView(albumRow.getItem());
                }
            });
            return albumRow;
        });
    }

    public TableView<Album> getTableView() {
        return albumViewTable;
    }

    public Tab getTab() {
        return albumViewTab;
    }

    public VBox getContents() {
        return (albumViewContents);
    }
}