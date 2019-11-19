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
        ResultSet rs = connection.createStatement().executeQuery("SELECT id FROM Track ORDER BY name");
        ArrayList<Integer> ids = new ArrayList<>();

        while (rs.next()) {
            ids.add(rs.getInt(1));
        }

        return ids;
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

        // PreparedStatement getTrackID = connection.prepareStatement("SELECT id FROM Track WHERE filepath = ?");
        // getTrackID.setString(1, filepath);

        // ResultSet rs = getTrackID.executeQuery();

        // rs.next();
        // int trackID = rs.getInt(1);
        int trackID = getID("Track", "filepath", filepath);

        System.out.println("trackID: " + trackID);



        int artistID;
        if (valueExists("Artist", "name", artistName)) {
            artistID = getID("Artist", "name", artistName);
        } else {
            PreparedStatement insertArtist = connection.prepareStatement("INSERT INTO Artist (name) VALUES (?)");
            insertArtist.setString(1, artistName);
            insertArtist.executeUpdate();
            artistID = getID("Artist", "name", artistName);
        }

        System.out.println("artist ID: " + artistID);


        connection.rollback();


        // start transaction


        // add filepath, trackName, and duration to Track Table (get autogenerated track ID when this happens if possible)

        // if artist not in artist Table, add artistName and get ID, else just get ID
        // if album not in album Table, add albumName and get ID, else just get ID
        // if genre not in genre Table, add genreName and get ID, else just get ID

        // add all ID pairs in track_artist, track_album, and track_genre tables

        //
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