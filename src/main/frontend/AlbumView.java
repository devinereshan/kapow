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
import main.library.Track;
import main.library.TrackList;

public class AlbumView {
    private AlbumList albumList;
    private Tab albumViewTab = new Tab("Albums");
    private VBox albumViewContents;
    private Button allAlbumsButton = new Button("All Albums");
    private Label currentAlbumLabel = new Label("All Albums");
    private TableView<Album> albumViewTable = new TableView<>();
    TableColumn<Album,String> nameCol = new TableColumn<>("Name");
    TableColumn<Album,String> artistsCol = new TableColumn<>("Artists");
    TableColumn<Album,String> numberOfTracksCol = new TableColumn<>("Tracks");
    TableColumn<Album,String> genresCol = new TableColumn<>("Genres");
    private TrackView localTrackView;
    private TableView<Track> localTrackViewTable;


    public AlbumView(AlbumList albumList) {
        this.albumList = albumList;
        assignColumnValues();
        setupVisualComponents();
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

    private void setupVisualComponents() {
        albumViewContents = new VBox(allAlbumsButton, currentAlbumLabel, albumViewTable);
        albumViewTab.setContent(albumViewContents);
        allAlbumsButton.setVisible(false);

    }

    public void makeInteractive() {
        allAlbumsButton.setOnAction(e -> restoreMainView());
        albumViewTable.setRowFactory(tv -> {
            TableRow<Album> albumRow = new TableRow<>();
            albumRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!albumRow.isEmpty())) {
                    switchToLocalTrackView(albumRow.getItem());
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

    public void restoreMainView() {

        if (albumViewContents.getChildren().contains(localTrackViewTable)) {
            albumViewContents.getChildren().set(2, albumViewTable);
            allAlbumsButton.setVisible(false);
            localTrackViewTable = null;
            localTrackView = null;
        }
    }
    public void switchToLocalTrackView(Album album) {
        System.out.println("Local Track View");
        localTrackView = new TrackView(new TrackList(album.getId(), "album"));
        localTrackViewTable = localTrackView.getTableView();
        currentAlbumLabel.setText(album.getName() + " - " + album.getArtists());
        allAlbumsButton.setVisible(true);
        albumViewContents.getChildren().set(2, localTrackViewTable);

    }
}