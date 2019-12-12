package main.frontend;

import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.library.Artist;
import main.library.ArtistList;

public class ArtistView {
    final ArtistList artistList;
    final TableView<Artist> artistViewTable = new TableView<>();
    final TableColumn<Artist,String> nameCol = new TableColumn<>("Name");
    final TableColumn<Artist,String> numberOfAlbumsCol = new TableColumn<>("Albums");
    final TableColumn<Artist,String> genresCol = new TableColumn<>("Genres");
    String title = "kapow!";
    private FilteredList<Artist> filteredArtists;


    public ArtistView() {
        artistList = new ArtistList();
        filteredArtists = new FilteredList<>(artistList.getArtists(), p -> true);
        assignColumnValues();
        artistViewTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void assignColumnValues() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        numberOfAlbumsCol.setCellValueFactory(new PropertyValueFactory<>("numberOfAlbums"));
        genresCol.setCellValueFactory(new PropertyValueFactory<>("genres"));
        artistViewTable.getColumns().setAll(nameCol, numberOfAlbumsCol, genresCol);
        artistViewTable.setItems(filteredArtists);
    }

    public void filter(TextField searchBox) {
        filteredArtists.setPredicate(p -> p.getSearchString().toLowerCase().contains(searchBox.getText().toLowerCase().trim()));
    }


    public String getTitle() {
        return title;
    }


    public ArtistList getList() {
        return artistList;
    }
}