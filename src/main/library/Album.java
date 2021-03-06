package main.library;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Album {
    private int id;
    private SimpleStringProperty name;
    private SimpleStringProperty artists;
    private SimpleIntegerProperty numberOfTracks;
    private SimpleStringProperty genres;
    private String searchString;


    // Constructor for albums that don't yet exist in database (no id)
    public Album(String name, String artists, int numberOfTracks, String genres) {
        this.name = new SimpleStringProperty(name);
        this.artists = new SimpleStringProperty(artists);
        this.numberOfTracks = new SimpleIntegerProperty(numberOfTracks);
        this.genres = new SimpleStringProperty(genres);
        buildSearchString();
    }

    public Album(int id, String name, String artists, int numberOfTracks, String genres) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.artists = new SimpleStringProperty(artists);
        this.numberOfTracks = new SimpleIntegerProperty(numberOfTracks);
        this.genres = new SimpleStringProperty(genres);
        buildSearchString();
    }

    private void buildSearchString() {
        searchString = name.get() + artists.get() + genres.get();
    }

    public String getSearchString() {
        return searchString;
    }

    public int getId() {
        return id;
    }


    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getArtists() {
        return artists.get();
    }

    public void setArtists(String artists) {
        this.artists.set(artists);
    }

    public SimpleStringProperty artistsProperty() {
        return artists;
    }


    public int getNumberOfTracks() {
        return numberOfTracks.get();
    }

    public void setNumberOfTracks(int numberOfTracks) {
        this.numberOfTracks.set(numberOfTracks);
    }

    public SimpleIntegerProperty numberOfTracksProperty() {
        return numberOfTracks;
    }


    public String getGenres() {
        return genres.get();
    }

    public void setGenres(String genres) {
        this.genres.set(genres);
    }

    public SimpleStringProperty genresProperty() {
        return genres;
    }
}