package main.frontend;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
// import main.library.Album;
import main.library.AlbumList;
import main.library.Artist;
import main.library.ArtistList;
// import main.library.Track;
// import main.library.TrackList;

public class ArtistView {
    private ArtistList artistList;
    private Tab artistViewTab = new Tab("Artists");
    private VBox artistViewContents;
    // private VBox currentContents;
    // private Button returnToParentButton;
    private Label currentArtistLabel = new Label("Artists");
    private TableView<Artist> artistViewTable = new TableView<>();
    TableColumn<Artist,String> nameCol = new TableColumn<>("Name");
    TableColumn<Artist,String> numberOfAlbumsCol = new TableColumn<>("Albums");
    TableColumn<Artist,String> genresCol = new TableColumn<>("Genres");
    // private AlbumView localAlbumView;
    // private TableView<Album> localAlbumViewTable;
    // private AudioPlayerView audioPlayerView;
    ViewHandler viewHandler;

    // public ArtistView(ArtistList artistList) {
    //     this.artistList = artistList;

    // }

    public ArtistView(ArtistList artistList, ViewHandler viewHandler) {
        this.artistList = artistList;
        this.viewHandler = viewHandler;
        // this.audioPlayerView = audioPlayerView;
        assignColumnValues();
        artistViewContents = new VBox(currentArtistLabel, artistViewTable);
        artistViewTab.setContent(artistViewContents);

        // setupVisualComponents();
        makeInteractive();
    }

    private void assignColumnValues() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        numberOfAlbumsCol.setCellValueFactory(new PropertyValueFactory<>("numberOfAlbums"));
        genresCol.setCellValueFactory(new PropertyValueFactory<>("genres"));
        artistViewTable.getColumns().setAll(nameCol, numberOfAlbumsCol, genresCol);
        artistViewTable.setItems(artistList.getArtists());
    }

    // private void setupVisualComponents() {
    //     artistViewContents = new VBox(currentArtistLabel, artistViewTable);
    //     // currentContents
    //     artistViewTab.setContent(artistViewContents);
    //     // allArtistsButton.setVisible(false);

    // }

    public void makeInteractive() {
        // allArtistsButton.setOnAction(e -> restoreMainView());
        artistViewTable.setRowFactory(tv -> {
            TableRow<Artist> artistRow = new TableRow<>();
            artistRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!artistRow.isEmpty())) {
                    // switchToLocalAlbumView(artistRow.getItem());
                    viewHandler.switchToNestedAlbumView(artistRow.getItem());
                }
            });
            return artistRow;
        });
    }

    public TableView<Artist> getTableView() {
        return artistViewTable;
    }

    public Tab getTab() {
        return artistViewTab;
    }

    public VBox getContents() {
        return artistViewContents;
    }

    // public void restoreMainView() {

    //     if (localAlbumView != null) {
    //         // currentContents = artistViewContents;
    //         artistViewTab.setContent(artistViewContents);
    //         localAlbumView = null;
    //     }
    //     // if (artistViewContents.getChildren().contains(localAlbumViewTable)) {
    //     //     artistViewContents.getChildren().set(2, artistViewTable);
    //     //     allArtistsButton.setVisible(false);
    //     //     currentArtistLabel.setText("All Albums");
    //     //     localAlbumViewTable = null;
    //     //     localAlbumView = null;
    //     // }
    // }


    // public void switchToLocalAlbumView(Artist artist) {
    //     // System.out.println("Local Track View");
    //     localAlbumView = new AlbumView(new AlbumList(artist.getId()), audioPlayerView, this, artist.getName());
    //     artistViewTab.setContent(localAlbumView.getContents());


        // localAlbumViewTable = localAlbumView.getTableView();
        // currentArtistLabel.setText(artist.getName());
        // allArtistsButton.setVisible(true);
        // System.out.println("Artist view: " + artistViewContents.getChildren());
        // artistViewContents.getChildren().set(2, localAlbumViewTable);

    // }
}