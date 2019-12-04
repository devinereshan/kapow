package main.library;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Artist {
    private int id;
    private SimpleStringProperty name;
    private SimpleStringProperty albums;
    private SimpleStringProperty genres;
    private SimpleIntegerProperty numberOfAlbums;

    public Artist(int id, String name, String albums, String genres, int numberOfAlbums) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.albums = new SimpleStringProperty(albums);
        this.genres = new SimpleStringProperty(genres);
        this.numberOfAlbums = new SimpleIntegerProperty(numberOfAlbums);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public String getAlbums() {
        return albums.get();
    }

    public String getGenres() {
        return genres.get();
    }

    public int getNumberOfAlbums() {
        return numberOfAlbums.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setAlbums(String albums) {
        this.albums.set(albums);
    }

    public void setGenres(String genres) {
        this.genres.set(genres);
    }

    public void setNumberOfAlbums(int numberOfAlbums) {
        this.numberOfAlbums.set(numberOfAlbums);
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleStringProperty albumsProperty() {
        return albums;
    }

    public SimpleStringProperty genresProperty() {
        return genres;
    }

    public SimpleIntegerProperty numberOfAlbumsProperty() {
        return numberOfAlbums;
    }
}