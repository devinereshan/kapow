package main.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private Connection getDatabaseConnection() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:sql/music.db");

        connection.createStatement().executeUpdate("PRAGMA foreign_keys = ON;");

        return connection;
    }


    public void addArtist(String name) throws SQLException {
        Connection connection = getDatabaseConnection();

        PreparedStatement addArtist = connection.prepareStatement(
            "INSERT INTO Artist(artist_name) VALUES(?);"
        );

        addArtist.setString(1, name);
        addArtist.executeUpdate();

        connection.close();
    }


    public void addTrack(String filepath, String name, String duration) throws SQLException {
        Connection connection = getDatabaseConnection();

        PreparedStatement addTrack = connection.prepareStatement(
            "INSERT INTO Track(filepath, track_name, duration) VALUES(?, ?, ?);"
        );

        addTrack.setString(1, filepath);
        addTrack.setString(2, name);
        addTrack.setString(3, duration);
        addTrack.executeUpdate();

        connection.close();
    }


    public void readAllTrackInfo() throws SQLException {
        Connection connection = getDatabaseConnection();

        Statement statement = connection.createStatement();
        ResultSet rs = null;

        rs = statement.executeQuery(
            "SELECT Track.name, Track.duration, Artist.name, Album.name, Genre.name FROM Track JOIN Track_Artist JOIN Artist JOIN Album JOIN Track_Album JOIN Genre JOIN Track_Genre ON Track.id = Track_Artist.track_id AND Track_Artist.artist_id = Artist.id AND Track.id = Track_Album.track_id AND Track_Album.album_id = Album.id AND Track.id = Track_Genre.track_id AND Track_Genre.genre_id = Genre.id;"
        );

        String format = "%-15s %-10s %-20s %-20s %-15s\n";
        System.out.format(format, "Track Name", "Duration", "Artist", "Album", "Genre");

        while (rs.next()) {
            System.out.format(format,
            rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
        }

        connection.close();
    }


    public void createBackup() throws SQLException {
        Connection connection = getDatabaseConnection();
        Statement statement = connection.createStatement();

        statement.executeUpdate("backup to sql/backup_music.db");

        connection.close();
    }


    public void restoreFromBackup() throws SQLException {
        Connection connection = getDatabaseConnection();
        Statement statement = connection.createStatement();

        statement.executeUpdate("restore from sql/backup_music.db");

        connection.close();
    }

}