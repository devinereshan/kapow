package main.library;

public class TrackRow {
    private int id;
    private String filepath;
    private String name;
    private String duration;
    private String artists;
    private String albums;
    private String genres;

    public TrackRow(int id, String filepath, String name, String duration, String artists, String albums, String genres) {
        this.id = id;
        this.filepath = filepath;
        this.name = name;
        this.duration = duration;
        this.artists = artists;
        this.albums = albums;
        this. genres = genres;
    }


    // toString for testing
    @Override
    public String toString() {
        return String.format("%d %s %s %s %s %s %s", id, filepath, name, duration, artists, albums, genres);
    }

    public String getAlbums() {
        return albums;
    }

    public String getArtists() {
        return artists;
    }

    public String getDuration() {
        return duration;
    }

    public String getFilepath() {
        return filepath;
    }


    public String getGenres() {
        return genres;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}