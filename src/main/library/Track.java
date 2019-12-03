package main.library;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Track {
    private int id;
    private int lengthInSeconds;
    private int indexInAlbum = 0;
    private SimpleStringProperty filepath;
    private SimpleStringProperty name;
    private SimpleStringProperty duration;
    private SimpleStringProperty artists;
    private SimpleStringProperty albums;
    private SimpleStringProperty genres;

    public Track(int id, String filepath, String name, String duration, String artists, String albums, String genres, int lengthInSeconds) {
        this.id = id;
        this.filepath = new SimpleStringProperty(filepath);
        this.name = new SimpleStringProperty(name);
        this.duration = new SimpleStringProperty(duration);
        this.artists = new SimpleStringProperty(artists);
        this.albums = new SimpleStringProperty(albums);
        this.genres = new SimpleStringProperty(genres);
        this.lengthInSeconds = lengthInSeconds;
    }

    public Track(int id, String filepath, String name, String duration, String artists, String albums, String genres, int lengthInSeconds, int indexInAlbum) {
        this.id = id;
        this.filepath = new SimpleStringProperty(filepath);
        this.name = new SimpleStringProperty(name);
        this.duration = new SimpleStringProperty(duration);
        this.artists = new SimpleStringProperty(artists);
        this.albums = new SimpleStringProperty(albums);
        this.genres = new SimpleStringProperty(genres);
        this.lengthInSeconds = lengthInSeconds;
        this.indexInAlbum = indexInAlbum;
    }


    public Track(String filepath, String name, String duration, String artists, String albums, String genres, int lengthInSeconds) {
        this.lengthInSeconds = lengthInSeconds;
        this.filepath = new SimpleStringProperty(filepath);
        this.name = new SimpleStringProperty(name);
        this.duration = new SimpleStringProperty(duration);
        this.artists = new SimpleStringProperty(artists);
        this.albums = new SimpleStringProperty(albums);
        this.genres = new SimpleStringProperty(genres);
    }


    // toString for testing
    @Override
    public String toString() {
        return String.format("%d %s %s %s %s %s %s", id, filepath, name, duration, artists, albums, genres);
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
        return indexInAlbum;
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

    public void setPositionInAlbum(int position) {
        this.indexInAlbum = position;
    }

    public StringProperty nameProperty() { return name; }
    public StringProperty albumsProperty() { return albums; }
    public StringProperty artistsProperty() { return artists; }
    public StringProperty genresProperty() { return genres; }


    // implement this later:
    public int getLengthInSeconds() {
        return lengthInSeconds;
    }
}