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
    private VBox currentContents;
    private Button returnToParent;
    private Label currentArtistLabel;
    private TableView<Album> albumViewTable = new TableView<>();
    TableColumn<Album,String> nameCol = new TableColumn<>("Name");
    TableColumn<Album,String> artistsCol = new TableColumn<>("Artists");
    TableColumn<Album,String> numberOfTracksCol = new TableColumn<>("Tracks");
    TableColumn<Album,String> genresCol = new TableColumn<>("Genres");
    private TrackView localTrackView;
    private ViewHandler viewHandler;
    // private TableView<Track> localTrackViewTable;
    // private AudioPlayerView audioPlayerView;



    public AlbumView(AlbumList albumList, ViewHandler viewHandler) {
        this.albumList = albumList;
        this.viewHandler = viewHandler;
        // this.audioPlayerView = audioPlayerView;
        // returnToParent = new Button("Albums");
        assignColumnValues();
        albumViewContents = new VBox(albumViewTable);
        albumViewTab.setContent(albumViewContents);
        // setupVisualComponents();
        makeInteractive();
    }


    // public AlbumView(AlbumList albumList, AudioPlayerView audioPlayerView, ArtistView parent, String parentName) {
    //     this.albumList = albumList;
    //     this.audioPlayerView = audioPlayerView;
    //     returnToParent = new Button("Back To Artists");
    //     currentArtistLabel = new Label(parentName);
    //     assignColumnValues();
    //     albumViewContents = new VBox(returnToParent, currentArtistLabel, albumViewTable);
    //     albumViewTab.setContent(albumViewContents);

    //     // setupVisualComponents();
    //     returnToParent.setOnAction(e -> restoreMainView());

    //     makeInteractive();
    // }


    public AlbumView(AlbumList albumList, ViewHandler viewHandler, String parentName) {
        this.albumList = albumList;
        this.viewHandler = viewHandler;
        // this.audioPlayerView = audioPlayerView;
        returnToParent = new Button("Back To Artists");
        currentArtistLabel = new Label(parentName);
        assignColumnValues();
        albumViewContents = new VBox(returnToParent, currentArtistLabel, albumViewTable);
        albumViewTab.setContent(albumViewContents);

        // setupVisualComponents();
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

    // private void setupVisualComponents() {
    //     // System.out.println("Setting up albumview visual components: ");
    //     // System.out.println(albumViewTable == null);
    //     albumViewContents = new VBox(returnToParent, currentArtistLabel, albumViewTable);
    //     // currentContents = albumViewContents;
    //     albumViewTab.setContent(albumViewContents);
    //     // returnToParent.setVisible(false);

    // }

    public void makeInteractive() {
        // returnToParent.setOnAction(e -> restoreMainView());
        albumViewTable.setRowFactory(tv -> {
            TableRow<Album> albumRow = new TableRow<>();
            albumRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!albumRow.isEmpty())) {
                    // switchToLocalTrackView(albumRow.getItem());
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

    // public void restoreMainView() {

    //     if (localTrackView != null) {
    //         // currentContents = albumViewContents;
    //         // albumViewTab.setContent(currentContents);
    //         albumViewTab.setContent(albumViewContents);
    //         localTrackView = null;
    //     }
    //     // if (albumViewContents.getChildren().contains(localTrackViewTable)) {
    //     //     albumViewContents.getChildren().set(2, albumViewTable);
    //     //     returnToParent.setVisible(false);
    //     //     currentAlbumLabel.setText("Albums");
    //     //     localTrackViewTable = null;
    //     //     localTrackView = null;
    //     // }
    // }


    // public void switchToLocalTrackView(Album album) {
    //     // System.out.println("Local Track View");
    //     localTrackView = new TrackView(new TrackList(album.getId(), "album"), audioPlayerView, this, album.getName());
    //     // currentContents = localTrackView.getContents();
    //     // albumViewTab.setContent(currentContents);
    //     albumViewTab.setContent(localTrackView.getContents());


    //     // localTrackViewTable = localTrackView.getTableView();
    //     // currentAlbumLabel.setText(album.getName() + " - " + album.getArtists());
    //     // returnToParent.setVisible(true);
    //     // System.out.println("Album view: " + albumViewContents.getChildren());
    //     // albumViewContents.getChildren().set(2, localTrackViewTable);

    // }
}