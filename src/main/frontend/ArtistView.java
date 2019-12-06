package main.frontend;

import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import main.library.Artist;
import main.library.ArtistList;

public class ArtistView {
    private ArtistList artistList;
    private Tab artistViewTab = new Tab("Artists");
    private VBox artistViewContents;
    private Label currentArtistLabel = new Label("Artists");
    private TableView<Artist> artistViewTable = new TableView<>();
    TableColumn<Artist,String> nameCol = new TableColumn<>("Name");
    TableColumn<Artist,String> numberOfAlbumsCol = new TableColumn<>("Albums");
    TableColumn<Artist,String> genresCol = new TableColumn<>("Genres");
    ViewHandler viewHandler;


    public ArtistView(ArtistList artistList, ViewHandler viewHandler) {
        this.artistList = artistList;
        this.viewHandler = viewHandler;
        assignColumnValues();
        artistViewContents = new VBox(currentArtistLabel, artistViewTable);
        artistViewTab.setContent(artistViewContents);

        makeInteractive();
    }

    private void assignColumnValues() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        numberOfAlbumsCol.setCellValueFactory(new PropertyValueFactory<>("numberOfAlbums"));
        genresCol.setCellValueFactory(new PropertyValueFactory<>("genres"));
        artistViewTable.getColumns().setAll(nameCol, numberOfAlbumsCol, genresCol);
        artistViewTable.setItems(artistList.getArtists());
    }


    public void makeInteractive() {
        artistViewTable.setRowFactory(tv -> {
            TableRow<Artist> artistRow = new TableRow<>();
            artistRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!artistRow.isEmpty())) {
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

    public ArtistList getList() {
        return artistList;
    }
}