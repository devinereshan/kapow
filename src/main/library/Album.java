package main.library;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Album {
    private int id;
    private SimpleStringProperty name;
    private SimpleStringProperty artists;
    private SimpleIntegerProperty numberOfTracks;
    private SimpleStringProperty genres;
    private SimpleStringProperty info;


    public Album(int id, String name, String artists, int numberOfTracks, String genres) {
        // System.out.println(id);
        this.id = id;
        // System.out.println(name);
        this.name = new SimpleStringProperty(name);
        // System.out.println(artists);
        this.artists = new SimpleStringProperty(artists);
        // System.out.println(numberOfTracks);
        this.numberOfTracks = new SimpleIntegerProperty(numberOfTracks);
        // System.out.println(genres);
        this.genres = new SimpleStringProperty(genres);
        createInfoString();
    }

    public int getId() {
        return id;
    }

    private void createInfoString() {
        // System.out.println("getName() " + getName());
        // System.out.println("getArtists() " + getArtists());
        // System.out.println("getNumberOfTracks() " + getNumberOfTracks());
        // System.out.println("getGenres() " + getGenres());

        String infoString = String.format("Name: %s, Artists: %s, Tracks: %d, Genres: %s", getName(), getArtists(), getNumberOfTracks(), getGenres());
        this.info = new SimpleStringProperty(infoString);
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



    public String getInfo() {
        return info.get();
    }

    public void setInfo(String info) {
        this.info.set(info);
    }

    public SimpleStringProperty infoProperty() {
        return info;
    }
}