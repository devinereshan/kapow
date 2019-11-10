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
        connection = DriverManager.getConnection("jdbc:sqlite:sql/music.db");
    }


    public ArrayList<Integer> getAllTrackID() throws SQLException {
        statement = connection.createStatement();

        resultSet = statement.executeQuery("SELECT id FROM Track");

        ArrayList<Integer> trackIDs = new ArrayList<>();
        while (resultSet.next()) {
            trackIDs.add(resultSet.getInt(1));
        }

        return trackIDs;
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