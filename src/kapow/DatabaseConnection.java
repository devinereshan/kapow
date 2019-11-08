package kapow;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private Connection getDatabaseConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:sql/music.db");
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


    public void readTrackTable() throws SQLException {
        Connection connection = getDatabaseConnection();

        Statement statement = connection.createStatement();
        ResultSet rs = null;

        rs = statement.executeQuery("SELECT * FROM Track;");

        System.out.format("%-30s %-15s %-1s\n",
        "filepath", "NAME", "DURATION");

        while (rs.next()) {
            System.out.format("%-30s %-15s %-1s\n",
            rs.getString(1), rs.getString(2), rs.getString(3));
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


    public void restoreFromTemplate() throws SQLException {
        Connection connection =  getDatabaseConnection();
        Statement statement = connection.createStatement();

        statement.executeUpdate("restore from sql/template.db");

        connection.close();
    }


    public void insertSampleData() throws SQLException {
        Connection connection =  getDatabaseConnection();

        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);

        try {
            statement.executeUpdate(
                "INSERT INTO Track(filepath, track_name, duration) VALUES" +
                "('/path/to/song.wav', 'song', '3:12'), " +
                "('/path/to/song2.wav', 'song2', '2:32'), " +
                "('/path/to/song3.wav', 'song3', '4:35'), " +
                "('/path/to/jam.wav', 'jam', '1:05'), " +
                "('/path/to/jam2.wav', 'jam2', '2:35'), " +
                "('/path/to/jam3.wav', 'jam3', '7:45');"
            );

            statement.executeUpdate(
                "INSERT INTO Artist(artist_name) VALUES" +
                "('Song Guy')," +
                "('Jam Dude');"
            );

            statement.executeUpdate(
                "INSERT INTO Track_Artist(filepath, artist_id) VALUES" +
                "('/path/to/song.wav', 1), " +
                "('/path/to/song2.wav', 1), " +
                "('/path/to/song2.wav', 2), " +
                "('/path/to/song3.wav', 1), " +
                "('/path/to/jam.wav', 2), " +
                "('/path/to/jam2.wav', 2), " +
                "('/path/to/jam3.wav', 2);"
            );

            statement.executeUpdate(
                "INSERT INTO Genre(genre_name) VALUES" +
                "('Rock')," +
                "('Country')," +
                "('Pop');"
            );
        } catch (SQLException e) {
            // System.out.println("Data already exists");
            e.printStackTrace();
        }

        connection.close();
    }
}