package main.library;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Track {
    private int id;
    private int lengthInSeconds;
    private SimpleStringProperty filepath;
    private SimpleStringProperty name;
    private SimpleStringProperty duration;
    private SimpleStringProperty artists;
    private SimpleStringProperty albums;
    private SimpleStringProperty genres;
    private SimpleIntegerProperty indexInAlbum;
    private String searchString;


    public Track(int id, String filepath, String name, String duration, String artists, String albums, String genres, int lengthInSeconds, int indexInAlbum) {
        this.id = id;
        this.filepath = new SimpleStringProperty(filepath);
        this.name = new SimpleStringProperty(name);
        this.duration = new SimpleStringProperty(duration);
        this.artists = new SimpleStringProperty(artists);
        this.albums = new SimpleStringProperty(albums);
        this.genres = new SimpleStringProperty(genres);
        this.lengthInSeconds = lengthInSeconds;
        this.indexInAlbum = new SimpleIntegerProperty(indexInAlbum);
        buildSearchString();
    }


    public Track(String filepath, String name, String duration, String artists, String albums, String genres, int lengthInSeconds, int indexInAlbum) {
        this.lengthInSeconds = lengthInSeconds;
        this.filepath = new SimpleStringProperty(filepath);
        this.name = new SimpleStringProperty(name);
        this.duration = new SimpleStringProperty(duration);
        this.artists = new SimpleStringProperty(artists);
        this.albums = new SimpleStringProperty(albums);
        this.genres = new SimpleStringProperty(genres);
        this.indexInAlbum = new SimpleIntegerProperty(indexInAlbum);
        buildSearchString();
    }


    private void buildSearchString() {
        searchString = name.get() + albums.get() + artists.get() + genres.get();
    }


    public String getSearchString() {
        return searchString;
    }

    // toString for testing
    @Override
    public String toString() {
        return String.format("%s %s %s %s %s %s %d %d", filepath.get(), name.get(), duration.get(), artists.get(), albums.get(), genres.get(), lengthInSeconds, indexInAlbum.get());
    }

    public String getAlbums() {
        return albums.get();
    }

    public String getArtists() {
        return artists.get();
    }

    public String getDuration() {
        return duration.get();
    }

    public String getFilepath() {
        return filepath.get();
    }


    public String getGenres() {
        return genres.get();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name.get();
    }


    public int getIndexInAlbum() {
        return indexInAlbum.get();
    }


    public void setID(int id) {
        this.id = id;
    }


    public void setName(String name) {
        this.name.set(name);
    }

    public void setAlbums(String albums) {
        this.albums.set(albums);
    }

    public void setArtists(String artists) {
        this.artists.set(artists);
    }

    public void setGenres(String genres) {
        this.genres.set(genres);
    }

    public void setIndexInAlbum(int index) {
        this.indexInAlbum.set(index);
    }

    public StringProperty nameProperty() { return name; }
    public StringProperty albumsProperty() { return albums; }
    public StringProperty artistsProperty() { return artists; }
    public StringProperty genresProperty() { return genres; }
    public SimpleIntegerProperty indexInAlbumProperty() { return indexInAlbum; }


    public int getLengthInSeconds() {
        return lengthInSeconds;
    }
}