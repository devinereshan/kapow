package main.library;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TrackListing {
    private int id;
    private StringProperty filepath;
    private StringProperty name;
    private StringProperty duration;
    private StringProperty artists;
    private StringProperty albums;
    private StringProperty genres;


    public void setID(int value) {
        id = value;
    }

    public int getID() {
        return id;
    }


    public void setFilepath(String value) {
        filepathProperty().set(value);
    }

    public String getFilepath() {
        return filepathProperty().get();
    }

    public StringProperty filepathProperty() {
        if (filepath == null) filepath = new SimpleStringProperty(this, "");
        return filepath;
    }


    public void setName(String value) {
        nameProperty().set(value);
    }

    public String getName() {
        return nameProperty().get();
    }

    public StringProperty nameProperty() {
        if (name == null) name = new SimpleStringProperty(this, "");
        return name;
    }


    public void setDuration(String value) {
        durationProperty().set(value);
    }

    public String getDuration() {
        return durationProperty().get();
    }

    public StringProperty durationProperty() {
        if (duration == null) duration = new SimpleStringProperty(this, "");
        return duration;
    }


    public void setArtists(String value) {
        artistsProperty().set(value);
    }

    public String getArtists() {
        return artistsProperty().get();
    }

    public StringProperty artistsProperty() {
        if (artists == null) artists = new SimpleStringProperty(this, "");
        return artists;
    }


    public void setAlbums(String value) {
        albumsProperty().set(value);
    }

    public String getAlbums() {
        return albumsProperty().get();
    }

    public StringProperty albumsProperty() {
        if (albums == null) albums = new SimpleStringProperty(this, "");
        return albums;
    }


    public void setGenres(String value) {
        genresProperty().set(value);
    }

    public String getGenres() {
        return genresProperty().get();
    }

    public StringProperty genresProperty() {
        if (genres == null) genres = new SimpleStringProperty(this, "");
        return genres;
    }


}
