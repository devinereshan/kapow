package main.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import main.frontend.GUI;

public class DBBuilder {

    public static boolean dbExists() {
        return new File(GUI.databasePath).exists();
    }

    public static boolean buildDatabase() {
        boolean success = false;
        File dbDirectory = new File(GUI.databaseDir);

        if(!dbDirectory.exists()) {
            dbDirectory.mkdir();
        }

        String url = "jdbc:sqlite:" + GUI.databasePath;
 

        try (Connection connection = DriverManager.getConnection(url)) {
            if (connection != null) {
                System.out.println("Connection success");
                connection.createStatement().executeUpdate("PRAGMA foreign_keys = ON;");
                buildTables(connection);
                success = true;
            } else {
                System.err.println("DBBuilder: Failed to build tables. Connection is null");
            }
 
        } catch (SQLException e) {
            System.err.println("DBBuilder: Failed to connect to database");
            System.out.println(e.getStackTrace());
        }

        return success;
    }

    private static void buildTables(Connection connection) throws SQLException{
        connection.createStatement().executeUpdate(String.format(
            "CREATE TABLE IF NOT EXISTS Track (\n" +
            "\tid          INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            "\tfilepath    TEXT NOT NULL UNIQUE,\n" +
            "\tname        TEXT NOT NULL,\n" +
            "\tlength_in_seconds   INTEGER,\n" +
            "\tduration    TEXT\n" +
            ");"
        ));

        connection.createStatement().executeUpdate(
            "CREATE INDEX Track_Filepath_Index ON Track (filepath);"
        );
        
        connection.createStatement().executeUpdate(String.format(
            "CREATE TABLE IF NOT EXISTS Album (\n" +
            "\tid          INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            "\tname        TEXT NOT NULL\n" +
            ");"
        ));

        connection.createStatement().executeUpdate(String.format(
            "CREATE TABLE IF NOT EXISTS Artist (\n" +
            "\tid          INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            "\tname        TEXT NOT NULL\n" +
            ");"
        ));

        connection.createStatement().executeUpdate(String.format(
            "CREATE TABLE IF NOT EXISTS Genre (\n" +
            "\tid          INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            "\tname        TEXT NOT NULL\n" +
            ");"
        ));

        connection.createStatement().executeUpdate(String.format(
            "CREATE TABLE IF NOT EXISTS Track_Album (\n" +
            "\ttrack_id    INTEGER,\n" +
            "\talbum_id    INTEGER,\n" +
            "\tindex_in_album  INTEGER,\n" +
            "\tPRIMARY KEY(track_id, album_id),\n" +
            "\tFOREIGN KEY(album_id) REFERENCES Album(id),\n" +
            "\tFOREIGN KEY(track_id) REFERENCES Track(id)\n" +
            ");"
        ));

        connection.createStatement().executeUpdate(String.format(
            "CREATE TABLE IF NOT EXISTS Track_Artist (\n" +
            "\ttrack_id    INTEGER,\n" + 
            "\tartist_id   INTEGER,\n" +
            "\tPRIMARY KEY(track_id, artist_id),\n" +
            "\tFOREIGN KEY(track_id) REFERENCES Track(id),\n" +
            "\tFOREIGN KEY(artist_id) REFERENCES Artist(id)\n" +
            ");"
        ));

        connection.createStatement().executeUpdate(String.format(
            "CREATE TABLE IF NOT EXISTS Track_Genre (\n" +
            "\ttrack_id    INTEGER,\n" +
            "\tgenre_id    INTEGER,\n" +
            "\tPRIMARY KEY(track_id, genre_id),\n" +
            "\tFOREIGN KEY(track_id) REFERENCES Track(id),\n" +
            "\tFOREIGN KEY(genre_id) REFERENCES Genre(id)\n" +
            ");"
        ));
    }
}