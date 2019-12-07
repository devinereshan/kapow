package main.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import main.library.Album;
import main.library.Artist;
import main.library.Track;

public class DBConnection implements AutoCloseable {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private Statement statement;
    private ResultSet resultSet;

    public DBConnection () throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:sql/music.db");
        connection.createStatement().executeUpdate("PRAGMA foreign_keys = ON;");
    }


    private String queryForString(String field, int track_id) throws SQLException {
        preparedStatement = connection.prepareStatement(
            "SELECT " + field + " FROM Track WHERE id = ?"
        );

        preparedStatement.setInt(1, track_id);
        resultSet = preparedStatement.executeQuery();
        String value = null;
        while (resultSet.next()) {
            value = resultSet.getString(1);
            break;
        }

        return value;
    }


    private String queryForString(String desiredField, String tableToQuery, int id) throws SQLException {
        preparedStatement = connection.prepareStatement("SELECT " + desiredField + " FROM " + tableToQuery + " WHERE id = ?");

        preparedStatement.setInt(1, id);
        resultSet = preparedStatement.executeQuery();
        String value = null;
        while (resultSet.next()) {
            value = resultSet.getString(1);
            break;
        }

        return value;
    }


    public String getFilepath(int track_id) throws SQLException {
        return queryForString("filepath", track_id);

    }


    public String getName(int track_id) throws SQLException {
        return queryForString("name", track_id);
    }


    public int getLengthInSeconds(int track_id) throws SQLException {
        preparedStatement = connection.prepareStatement("SELECT length_in_seconds FROM Track WHERE id = ?");
        preparedStatement.setInt(1, track_id);
        resultSet = preparedStatement.executeQuery();
        resultSet.next();

        return resultSet.getInt(1);
    }

    public String getName(String filepath) throws SQLException {
        preparedStatement = connection.prepareStatement("SELECT name FROM Track WHERE filepath = ?");
        preparedStatement.setString(1, filepath);
        resultSet = preparedStatement.executeQuery();
        resultSet.next();

        return resultSet.getString(1);
    }


    public String getDuration(int track_id) throws SQLException {
        return queryForString("duration", track_id);
    }


    private String getMultiValueString(String query, int id) throws SQLException {
        preparedStatement = connection.prepareStatement(query);

        preparedStatement.setInt(1, id);
        resultSet = preparedStatement.executeQuery();

        String values = "";

        while (resultSet.next()) {
            values += ", " + resultSet.getString(1);
        }

        if (values.length() > 2) {
            return values.substring(2, values.length());
        } else {
            return values;
        }

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


    public ArrayList<Integer> getTrackAlbumIDs(int albumID) throws SQLException {
        preparedStatement = connection.prepareStatement("SELECT track_id FROM Track_Album WHERE album_id = ? ORDER BY index_in_album");
        preparedStatement.setInt(1, albumID);
        resultSet = preparedStatement.executeQuery();

        ArrayList<Integer> ids = new ArrayList<>();

        while (resultSet.next()) {
            ids.add(resultSet.getInt(1));
        }

        return ids;
    }


    public String getAlbumName(int albumID) throws SQLException {
        preparedStatement = connection.prepareStatement("SELECT name FROM Album WHERE id = ?");
        preparedStatement.setInt(1, albumID);
        resultSet = preparedStatement.executeQuery();

        String name = "";
        while (resultSet.next()) {
            name = resultSet.getString(1);
        }

        return name;
    }



    public ArrayList<Integer> getAlbumIDs() throws SQLException {
        resultSet = connection.createStatement().executeQuery("SELECT id FROM Album ORDER BY name");
        ArrayList<Integer> ids = new ArrayList<>();

        while (resultSet.next()) {
            ids.add(resultSet.getInt(1));
        }

        return ids;
    }

    public ArrayList<Integer> getAlbumIDs(int artistID) throws SQLException {
        preparedStatement = connection.prepareStatement("SELECT Album.id FROM Album WHERE Album.id IN (SELECT album_id FROM Track_Album WHERE track_id IN (SELECT track_id FROM Track_Artist WHERE artist_id = ?))");
        preparedStatement.setInt(1, artistID);
        resultSet = preparedStatement.executeQuery();
        ArrayList<Integer> ids = new ArrayList<>();

        while (resultSet.next()) {
            ids.add(resultSet.getInt(1));
        }

        return ids;
    }



    public ArrayList<Integer> getArtistIDs() throws SQLException {
        resultSet = connection.createStatement().executeQuery("SELECT id FROM Artist ORDER BY name");
        ArrayList<Integer> ids = new ArrayList<>();

        while (resultSet.next()) {
            ids.add(resultSet.getInt(1));
        }

        return ids;
    }




    public Track getTrack(int id) throws SQLException {
        String filepath = getFilepath(id);
        String name = getName(id);
        String duration = getDuration(id);
        String artists = getArtists(id);
        String albums = getAlbums(id);
        String genres = getGenres(id);
        int lengthInSeconds = getLengthInSeconds(id);
        int indexInAlbum = getIndexInAlbum(id, albums);

        return new Track(id, filepath, name, duration, artists, albums, genres, lengthInSeconds, indexInAlbum);
    }


    public Track getAlbumTrack(int trackID, int albumID) throws SQLException {
        String filepath = getFilepath(trackID);
        String name = getName(trackID);
        String duration = getDuration(trackID);
        String artists = getArtists(trackID);
        String albums = getAlbums(trackID);
        String genres = getGenres(trackID);
        int lengthInSeconds = getLengthInSeconds(trackID);
        int indexInAlbum = getIndexInAlbum(trackID, albumID);

        return new Track(trackID, filepath, name, duration, artists, albums, genres, lengthInSeconds, indexInAlbum);
    }

    private int getIndexInAlbum(int trackID, int albumID) throws SQLException {
        preparedStatement = connection.prepareStatement("SELECT index_in_album FROM Track_Album WHERE track_id = ? AND album_id = ?");
        preparedStatement.setInt(1, trackID);
        preparedStatement.setInt(2, albumID);
        resultSet = preparedStatement.executeQuery();

        resultSet.next();

        return resultSet.getInt(1);
    }


    private int getIndexInAlbum(int trackID, String albumName) throws SQLException {
        preparedStatement = connection.prepareStatement("SELECT index_in_album FROM Track_Album WHERE track_id = ? AND album_id IN (SELECT Album.id FROM Album WHERE Album.name = ?)");
        preparedStatement.setInt(1, trackID);
        preparedStatement.setString(2, albumName);
        resultSet = preparedStatement.executeQuery();

        resultSet.next();

        return resultSet.getInt(1);
    }


    public Album getAlbum(int id) throws SQLException {
        String name;
        String artists;
        int numberOfTracks;
        String genres;

        name = queryForString("name", "Album", id);
        artists = getAlbumArtists(id);
        numberOfTracks = getNumberOfTracks(id);
        genres = getAlbumGenres(id);

        return new Album(id, name, artists, numberOfTracks, genres);

    }

    public Artist getArtist(int id) throws SQLException {
        String name;
        String albums;
        int numberOfAlbums;
        String genres;

        name = queryForString("name", "Artist", id);
        albums = getArtistAlbums(id);
        numberOfAlbums = getNumberOfAlbums(id);
        genres = getArtistGenres(id);

        return new Artist(id, name, albums, genres, numberOfAlbums);
    }

    private String getAlbumArtists(int albumID) throws SQLException {
        String query = "SELECT Artist.name from Artist WHERE Artist.id IN (SELECT artist_id from Track_Artist WHERE track_id IN (SELECT track_id from Track_Album WHERE album_id = ?))";

        return getMultiValueString(query, albumID);
    }

    private String getArtistAlbums(int artistID) throws SQLException {
        String query = "SELECT Album.name from Album WHERE Album.id IN (SELECT album_id from Track_Album WHERE track_id IN (SELECT track_id from Track_Artist WHERE artist_id = ?))";

        return getMultiValueString(query, artistID);
    }




    private String getAlbumGenres(int album_id) throws SQLException {
        String query = "SELECT Genre.name from Genre WHERE Genre.id IN (SELECT genre_id from Track_Genre WHERE track_id IN (SELECT track_id from Track_Album WHERE album_id = ?))";

        return getMultiValueString(query, album_id);
    }


    private String getArtistGenres(int artistID) throws SQLException {
        String query = "SELECT Genre.name from Genre WHERE Genre.id IN (SELECT genre_id from Track_Genre WHERE track_id IN (SELECT track_id from Track_Artist WHERE artist_id = ?))";

        return getMultiValueString(query, artistID);
    }


    private int getNumberOfTracks(int id) throws SQLException {
        preparedStatement = connection.prepareStatement("SELECT Count(*) FROM Track_Album WHERE album_id = ?");

        preparedStatement.setInt(1, id);
        resultSet = preparedStatement.executeQuery();
        resultSet.next();

        return resultSet.getInt(1);
    }


    private int getNumberOfAlbums(int artistID) throws SQLException {
        preparedStatement = connection.prepareStatement("SELECT Count(*) FROM Album WHERE Album.id IN (SELECT DISTINCT album_id FROM Track_Album WHERE track_id IN (SELECT track_id FROM Track_Artist WHERE artist_id = ?))");

        preparedStatement.setInt(1, artistID);
        resultSet = preparedStatement.executeQuery();
        resultSet.next();

        return resultSet.getInt(1);
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


    public int getID(String table, String field, String value) throws SQLException {
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

    private void addTrackAlbumPair(int trackID, int albumID, int indexInAlbum) throws SQLException {
        preparedStatement = connection.prepareStatement("INSERT INTO Track_Album (track_id, album_id, index_in_album) VALUES (?, ?, ?)");
        preparedStatement.setInt(1, trackID);
        preparedStatement.setInt(2, albumID);
        preparedStatement.setInt(3, indexInAlbum);

        preparedStatement.executeUpdate();
    }


    private void removeIDPair(String table, int trackID, String pairName, int pairID) throws SQLException {
        preparedStatement = connection.prepareStatement("DELETE FROM " + table + " WHERE track_id = ? AND " + pairName + " = ?");
        preparedStatement.setInt(1, trackID);
        preparedStatement.setInt(2, pairID);

        preparedStatement.executeUpdate();
    }


    public int getAlbumID(String albumName, int trackID) throws SQLException{
        preparedStatement = connection.prepareStatement("SELECT Album.id FROM Album WHERE Album.name = ? AND Album.id IN (SELECT album_id FROM Track_Album WHERE track_id = ?)");
        preparedStatement.setString(1, albumName);
        preparedStatement.setInt(2, trackID);
        resultSet = preparedStatement.executeQuery();
        resultSet.next();

        return resultSet.getInt(1);
    }


    public int getArtistID(String artistName, int trackID) throws SQLException {
        preparedStatement = connection.prepareStatement("SELECT Artist.id FROM Artist WHERE Artist.name = ? AND Artist.id IN (SELECT artist_id FROM Track_Artist WHERE track_id = ?)");
        preparedStatement.setString(1, artistName);
        preparedStatement.setInt(2, trackID);
        resultSet = preparedStatement.executeQuery();
        resultSet.next();

        return resultSet.getInt(1);

    }

    public void importTrack(Track track) throws SQLException {
        connection.setAutoCommit(false);
        addTrackToDB(track);
        connection.commit();
        connection.setAutoCommit(true);
    }

    public ArrayList<Track> importMultiTrack(ArrayList<Track> tracks) throws SQLException {
        connection.setAutoCommit(false);
        for (Track track : tracks) {
            addTrackToDB(track);
            track.setID(getID("Track", "filepath", track.getFilepath()));
            System.out.println(track.getId());
        }

        connection.commit();
        connection.setAutoCommit(true);
        return tracks;
    }

    private void addTrackToDB(Track newTrack) throws SQLException {
        // TODO: support multiple artists, albums, and genres in an add (possibly with String[] or by accepting a track object)

        String filepath = newTrack.getFilepath();
        String trackName = newTrack.getName();
        String duration = newTrack.getDuration();
        String artistName = newTrack.getArtists();
        String albumName = newTrack.getAlbums();
        String genreName = newTrack.getGenres();
        int indexInAlbum = newTrack.getIndexInAlbum();
        int lengthInSeconds = newTrack.getLengthInSeconds();

        if (valueExists("Track", "filepath", filepath)) {
            // track already exists. Notify user somehow, then return
            System.out.println("Track already Exists");
            return;
        }

        PreparedStatement insertTrack = connection.prepareStatement("INSERT INTO Track(filepath, name, duration, length_in_seconds) VALUES(?, ?, ?, ?)");
        insertTrack.setString(1, filepath);
        insertTrack.setString(2, trackName);
        insertTrack.setString(3, duration);
        insertTrack.setInt(4, lengthInSeconds);

        insertTrack.executeUpdate();

        int trackID = getID("Track", "filepath", filepath);

        addUniqueValue("Artist", "name", artistName);
        int artistID = getID("Artist", "name", artistName);

        addUniqueValue("Album", "name", albumName);
        int albumID = getID("Album", "name", albumName);

        addUniqueValue("Genre", "name", genreName);
        int genreID = getID("Genre", "name", genreName);

        // add ID pairs
        addIDPair("Track_Artist", trackID, artistID);

        addTrackAlbumPair(trackID, albumID, indexInAlbum);

        addIDPair("Track_Genre", trackID, genreID);
    }


    public void removeArtist(Artist artist) throws SQLException {
        connection.setAutoCommit(false);
        int artistID = artist.getId();

        // TODO: If albums still exist, remove them first

        deleteFromTable(artistID, "artist_id", "Track_Artist");
        deleteFromTable(artistID, "id", "Artist");

        connection.commit();

        connection.setAutoCommit(true);
    }


    public void removeAlbum(Album album) throws SQLException {
        connection.setAutoCommit(false);
        int albumID = album.getId();

        // TODO: If tracks still exist, remove them first

        deleteFromTable(albumID, "album_id", "Track_Album");
        deleteFromTable(albumID, "id", "Album");

        connection.commit();

        connection.setAutoCommit(true);
    }

    private boolean albumIsEmpty(int albumID) throws SQLException {
        preparedStatement = connection.prepareStatement("SELECT Count(*) from Track_Album WHERE album_id = ?");
        preparedStatement.setInt(1, albumID);

        resultSet = preparedStatement.executeQuery();
        int trackCount = 0;
        while (resultSet.next()) {
            trackCount = resultSet.getInt(1);
        }

        if (trackCount > 0) {
            return false;
        } else {
            return true;
        }
    }


    private boolean artistIsEmpty(int artistID) throws SQLException {
        preparedStatement = connection.prepareStatement("SELECT Count(*) from Track_Artist WHERE artist_id = ?");
        preparedStatement.setInt(1, artistID);

        resultSet = preparedStatement.executeQuery();
        int trackCount = 0;
        while (resultSet.next()) {
            trackCount = resultSet.getInt(1);
        }

        if (trackCount > 0) {
            return false;
        } else {
            return true;
        }
    }


    public void removeTrackFromDB(int trackID, int artistID, int albumID) throws SQLException {
        connection.setAutoCommit(false);

        // int albumID = getAlbumID(albumName, trackID);
        deleteFromTable(trackID, "track_id", "Track_Genre");
        deleteFromTable(trackID, "track_id", "Track_Album");
        deleteFromTable(trackID, "track_id", "Track_Artist");
        deleteFromTable(trackID, "id", "Track");

        // if this empties an album, remove that album
        if (albumIsEmpty(albumID)) {
            // removeAlbum(getAlbum(albumID));
            deleteFromTable(albumID, "album_id", "Track_Album");
            deleteFromTable(albumID, "id", "Album");
        }

        // if this empties an artist, remove that artist
        if (artistIsEmpty(artistID)) {
            // removeArtist(getArtist(artistID));
            deleteFromTable(artistID, "artist_id", "Track_Artist");
            deleteFromTable(artistID, "id", "Artist");

        }

        connection.commit();

        connection.setAutoCommit(true);
    }


    private void deleteFromTable(int id, String idName, String tableName) throws SQLException {
        preparedStatement = connection.prepareStatement("DELETE FROM " + tableName + " WHERE " + idName + " = ?");
        preparedStatement.setInt(1, id);

        preparedStatement.executeUpdate();
    }


    public boolean updateTrack(Track newTrack) {
        try {
            int trackID = newTrack.getId();
            Track oldTrack = getTrack(trackID);
            connection.setAutoCommit(false);

            String newName = newTrack.getName();
            String newArtist = newTrack.getArtists();
            String newAlbums = newTrack.getAlbums();
            int newIndex = newTrack.getIndexInAlbum();
            String newGenres = newTrack.getGenres();

            if (!newName.equals(oldTrack.getName())) {
                updateTrackName(newName, trackID);
            }

            if (!newArtist.equals(oldTrack.getArtists())) {
                updateTrackArtist(oldTrack.getArtists(), newArtist, trackID);
            }

            if (!newAlbums.equals(oldTrack.getAlbums())) {
                updateTrackAlbum(oldTrack.getAlbums(), newAlbums, trackID, newIndex);
            }

            if (newIndex != oldTrack.getIndexInAlbum()) {
                updateTrackAlbumIndex(newAlbums, trackID, newIndex);
            }

            if (!newGenres.equals(oldTrack.getGenres())) {
                updateTrackGenre(oldTrack.getGenres(), newGenres, trackID);
            }

            connection.commit();
            connection.setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            System.err.println("DBConnection updateTrack: failed to commit changes to database");
            e.printStackTrace();
            return false;
        }
    }


    private void updateTrackAlbumIndex(String albumName, int trackID, int index) throws SQLException {
        int albumID = getAlbumID(albumName, trackID);
        preparedStatement = connection.prepareStatement("UPDATE Track_Album SET index_in_album = ? WHERE track_id = ? and album_id = ?");
        preparedStatement.setInt(1, index);
        preparedStatement.setInt(2, trackID);
        preparedStatement.setInt(3, albumID);
        preparedStatement.executeUpdate();
    }


    public void updateTrackName(String name, int trackID) throws SQLException {
        preparedStatement = connection.prepareStatement("UPDATE Track SET name = ? WHERE id = ?");
        preparedStatement.setString(1, name);
        preparedStatement.setInt(2, trackID);

        preparedStatement.executeUpdate();
    }

    public void updateTrackArtist(String oldArtistName, String newArtistName, int trackID) throws SQLException {
        // remove old track_artist id pair
        int oldArtistID = getID("Artist", "name", oldArtistName);
        removeIDPair("Track_Artist", trackID, "artist_id", oldArtistID);

        // add artistName and get ID;
        addUniqueValue("Artist", "name", newArtistName);
        int newArtistID = getID("Artist", "name", newArtistName);

        addIDPair("Track_Artist", trackID, newArtistID);
    }


    public void updateTrackAlbum(String oldAlbumName, String newAlbumName, int trackID, int indexInAlbum) throws SQLException {
        int oldAlbumID = getID("Album", "name", oldAlbumName);
        removeIDPair("Track_Album", trackID, "album_id", oldAlbumID);

        addUniqueValue("Album", "name", newAlbumName);
        int newAlbumID = getID("Album", "name", newAlbumName);

        addTrackAlbumPair(trackID, newAlbumID, indexInAlbum);
    }


    public void updateTrackGenre(String oldGenreName, String newGenreName, int trackID) throws SQLException {
        int oldGenreID = getID("Genre", "name", oldGenreName);
        removeIDPair("Track_Genre", trackID, "genre_id", oldGenreID);

        addUniqueValue("Genre", "name", newGenreName);
        int newGenreID = getID("Genre", "name", newGenreName);

        addIDPair("Track_Genre", trackID, newGenreID);
    }


    @Override
    public void close() throws SQLException {
        try {
            if (statement != null) {
                statement.close();
            }

            if (preparedStatement != null) {
                preparedStatement.close();
            }

            if (resultSet != null) {
                resultSet.close();
            }
        } finally {
            connection.close();
        }
    }
}