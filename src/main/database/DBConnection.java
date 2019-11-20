package main.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DBConnection implements AutoCloseable {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private Statement statement;
    private ResultSet resultSet;

    public DBConnection () throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:rick_astley.db");
    }


    private String queryForString(String field, int track_id) throws SQLException {
        preparedStatement = connection.prepareStatement(
            "SELECT " + field + " FROM Track WHERE id = ?"
        );

        preparedStatement.setInt(1, track_id);
        resultSet = preparedStatement.executeQuery();
        resultSet.next();

        String value = resultSet.getString(1);

        return value;
    }


    public String getFilepath(int track_id) throws SQLException {
        return queryForString("filepath", track_id);

    }


    public String getName(int track_id) throws SQLException {
        return queryForString("name", track_id);
    }


    public String getDuration(int track_id) throws SQLException {
        return queryForString("duration", track_id);
    }


    private String getMultiValueString(String query, int track_id) throws SQLException {
        preparedStatement = connection.prepareStatement(query);

        preparedStatement.setInt(1, track_id);
        resultSet = preparedStatement.executeQuery();

        resultSet.next();
        String values = resultSet.getString(1);

        while (resultSet.next()) {
            values += ", " + resultSet.getString(1);
        }

        return values;
    }


    public String getArtists(int track_id) throws SQLException {
        String query = "SELECT Artist.name FROM Artist JOIN Track_Artist ON Artist.id = Track_Artist.artist_id WHERE track_id = ?";

        return getMultiValueString(query, track_id);
    }


    public String getAlbums(int track_id) throws SQLException {
        String query = "SELECT Album.name FROM Album JOIN Track_Album ON Album.id = Track_Album.album_id WHERE track_id = ?";

        return getMultiValueString(query, track_id);
    }


    public String getGenres(int track_id) throws SQLException {
        String query = "SELECT Genre.name FROM Genre JOIN Track_Genre ON Genre.id = Track_Genre.genre_id WHERE track_id = ?";

        return getMultiValueString(query, track_id);
    }


    public ArrayList<Integer> getIDs() throws SQLException {
        ResultSet rs = connection.createStatement().executeQuery("SELECT id FROM Track ORDER BY id");
        ArrayList<Integer> ids = new ArrayList<>();

        while (rs.next()) {
            ids.add(rs.getInt(1));
        }

        return ids;
    }


    public ArrayList<Integer> getNewIDs(int previousID) throws SQLException {
        resultSet = connection.createStatement().executeQuery("SELECT id FROM TRACK WHERE id > " + previousID + " ORDER BY id");

        ArrayList<Integer> newIDs = new ArrayList<>();

        while(resultSet.next()) {
            newIDs.add(resultSet.getInt(1));
        }

        return newIDs;

    }


    public boolean valueExists(String table, String field, String value) throws SQLException {
        preparedStatement = connection.prepareStatement("SELECT * FROM " + table + " WHERE " + field + " = ? LIMIT 1");
        preparedStatement.setString(1, value);

        resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            // not empty, filepath exists
            return true;
        }

        // empty, filepath doesn't exist
        return false;
    }


    private int getID(String table, String field, String value) throws SQLException {
        preparedStatement = connection.prepareStatement("SELECT id FROM " + table + " WHERE " + field + " = ?");
        preparedStatement.setString(1, value);

        resultSet = preparedStatement.executeQuery();

        resultSet.next();
        return resultSet.getInt(1);

    }


    private void addUniqueValue(String table, String field, String value) throws SQLException {
        if (valueExists(table, field, value)) {
            System.out.println("Value already exists in database");
            return;
        }
        preparedStatement = connection.prepareStatement("INSERT INTO " + table + " (" + field + ") VALUES (?)");
        preparedStatement.setString(1, value);

        preparedStatement.executeUpdate();
    }


    private void addIDPair(String table, int trackID, int pairID) throws SQLException {
        preparedStatement = connection.prepareStatement("INSERT INTO " + table + " VALUES (?, ?)");
        preparedStatement.setInt(1, trackID);
        preparedStatement.setInt(2, pairID);

        preparedStatement.executeUpdate();
    }

    public void addTrackToDB(String filepath, String duration, String trackName, String artistName, String albumName, String genreName)
            throws SQLException {
        // TODO: support multiple artists, albums, and genres in an add (possibly with String[] or by accepting a track object)

        if (valueExists("Track", "filepath", filepath)) {
            // track already exists. Notify user somehow, then return
            System.out.println("Track already Exists");
            return;
        }

        connection.setAutoCommit(false);

        PreparedStatement insertTrack = connection.prepareStatement("INSERT INTO Track(filepath, name, duration) VALUES(?, ?, ?)");
        insertTrack.setString(1, filepath);
        insertTrack.setString(2, trackName);
        insertTrack.setString(3, duration);

        insertTrack.executeUpdate();

        int trackID = getID("Track", "filepath", filepath);
        System.out.println("trackID: " + trackID);


        addUniqueValue("Artist", "name", artistName);
        int artistID = getID("Artist", "name", artistName);

        System.out.println("artist ID: " + artistID);


        addUniqueValue("Album", "name", albumName);
        int albumID = getID("Album", "name", albumName);

        System.out.println("album ID: " + albumID);


        addUniqueValue("Genre", "name", genreName);
        int genreID = getID("Genre", "name", genreName);

        System.out.println("genre ID: " + genreID);

        // add ID pairs
        System.out.format("Adding to track_artist: %d %d\n", trackID, artistID);
        addIDPair("Track_Artist", trackID, artistID);
        System.out.format("Adding to track_album: %d %d\n", trackID, albumID);
        addIDPair("Track_Album", trackID, albumID);
        System.out.format("Adding to track_genre: %d %d\n", trackID, genreID);
        addIDPair("Track_Genre", trackID, genreID);

        connection.createStatement().executeQuery("Select * from Track_Artist");
        connection.createStatement().executeQuery("Select * from Track_Album");
        connection.createStatement().executeQuery("Select * from Track_Genre");


        // connection.rollback();
        connection.commit();
    }

    @Override
    public void close() throws SQLException {
        try {
            if (statement != null) {
                statement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        } finally {
            connection.close();
        }
    }
}