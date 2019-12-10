package main.frontend;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import main.library.Artist;
import main.library.ArtistList;

public class ArtistView {
    final ArtistList artistList;
    // private Tab artistViewTab = new Tab("Artists");
    // private VBox artistViewContents;
    // private Label currentArtistLabel = new Label("Artists");
    final TableView<Artist> artistViewTable = new TableView<>();
    final TableColumn<Artist,String> nameCol = new TableColumn<>("Name");
    final TableColumn<Artist,String> numberOfAlbumsCol = new TableColumn<>("Albums");
    final TableColumn<Artist,String> genresCol = new TableColumn<>("Genres");
    // ViewHandler viewHandler;
    // ContextMenu contextMenu;


    public ArtistView(ArtistList artistList, ViewHandler viewHandler) {
        this.artistList = artistList;
        // this.viewHandler = viewHandler;
        assignColumnValues();
        // artistViewContents = new VBox(currentArtistLabel, artistViewTable);
        // artistViewTab.setContent(artistViewContents);

        // buildContextMenu();

        // makeInteractive();
    }


    public ArtistView() {
        artistList = new ArtistList();
        // this.artistList = artistList;
        assignColumnValues();
        // artistViewContents = new VBox(currentArtistLabel, artistViewTable);
        // artistViewTab.setContent(artistViewContents);

        // buildContextMenu();

        // makeInteractive();
    }

    private void assignColumnValues() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        numberOfAlbumsCol.setCellValueFactory(new PropertyValueFactory<>("numberOfAlbums"));
        genresCol.setCellValueFactory(new PropertyValueFactory<>("genres"));
        artistViewTable.getColumns().setAll(nameCol, numberOfAlbumsCol, genresCol);
        artistViewTable.setItems(artistList.getArtists());
    }


    // public void makeInteractive() {
    //     artistViewTable.setRowFactory(tv -> {
    //         TableRow<Artist> artistRow = new TableRow<>();
    //         artistRow.setOnMouseClicked(event -> {
    //             if (event.getClickCount() == 2 && (!artistRow.isEmpty())) {
    //                 viewHandler.switchToNestedAlbumView(artistRow.getItem());
    //             }
    //         });
    //         return artistRow;
    //     });
    // }

    // public TableView<Artist> getTableView() {
    //     return artistViewTable;
    // }

    // public Tab getTab() {
    //     return artistViewTab;
    // }

    // public VBox getContents() {
    //     return artistViewContents;
    // }

    public ArtistList getList() {
        return artistList;
    }

    // private void buildContextMenu() {
    //     contextMenu = new ContextMenu();

    //     MenuItem importAudio = new MenuItem("Import");
    //     importAudio.setOnAction(e -> viewHandler.importAudio());

    //     contextMenu.getItems().add(importAudio);
    //     artistViewTable.setContextMenu(contextMenu);
    // }

}