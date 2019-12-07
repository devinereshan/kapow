package main.frontend;

import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
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
    private ContextMenu contextMenu;


    public AlbumView(AlbumList albumList, ViewHandler viewHandler) {
        this.albumList = albumList;
        this.viewHandler = viewHandler;
        assignColumnValues();
        albumViewContents = new VBox(albumViewTable);
        albumViewTab.setContent(albumViewContents);
        buildContextMenu();

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
        buildContextMenu();

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


    private void buildContextMenu() {
        contextMenu = new ContextMenu();

        // MenuItem play = new MenuItem("play");
        // play.setOnAction(e -> play(trackViewTable.getSelectionModel().getSelectedItem()));

        MenuItem importAudio = new MenuItem("Import");
        importAudio.setOnAction(e -> viewHandler.importAudio());

        // MenuItem editTrack = new MenuItem("edit track");
        // editTrack.setOnAction(e -> viewHandler.editTrack(trackViewTable.getSelectionModel().getSelectedItem()));

        // MenuItem delete = new MenuItem("delete");
        // delete.setOnAction(e -> viewHandler.deleteTrack(trackViewTable.getSelectionModel().getSelectedItem()));


        // contextMenu.getItems().add(play);
        contextMenu.getItems().add(importAudio);
        // contextMenu.getItems().add(editTrack);
        // contextMenu.getItems().add(delete);
        albumViewTable.setContextMenu(contextMenu);
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

    public AlbumList getList() {
        return albumList;
    }
}