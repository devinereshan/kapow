PRAGMA foreign_keys = ON;
CREATE TABLE IF NOT EXISTS Track (
    id          INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    filepath    TEXT NOT NULL UNIQUE,
    name        TEXT NOT NULL,
    length_in_seconds   INTEGER,
    duration    TEXT
);
CREATE INDEX Track_Filepath_Index ON Track (filepath);
CREATE TABLE IF NOT EXISTS Album (
    id          INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    name        TEXT NOT NULL
);
CREATE TABLE IF NOT EXISTS Artist (
    id          INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    name        TEXT NOT NULL
);
CREATE TABLE IF NOT EXISTS Genre (
    id          INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    name        TEXT NOT NULL
);
CREATE TABLE IF NOT EXISTS Track_Album (
    track_id    INTEGER,
    album_id    INTEGER,
    -- index_in_album  INTEGER,
    PRIMARY KEY(track_id, album_id),
    FOREIGN KEY(album_id) REFERENCES Album(id),
    FOREIGN KEY(track_id) REFERENCES Track(id)
);
CREATE TABLE IF NOT EXISTS Track_Artist (
    track_id    INTEGER,
    artist_id   INTEGER,
    PRIMARY KEY(track_id, artist_id),
    FOREIGN KEY(track_id) REFERENCES Track(id),
    FOREIGN KEY(artist_id) REFERENCES Artist(id)
);
CREATE TABLE IF NOT EXISTS Track_Genre (
    track_id    INTEGER,
    genre_id    INTEGER,
    PRIMARY KEY(track_id, genre_id),
    FOREIGN KEY(track_id) REFERENCES Track(id),
    FOREIGN KEY(genre_id) REFERENCES Genre(id)
);