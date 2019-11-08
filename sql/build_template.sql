PRAGMA foreign_keys = ON;
CREATE TABLE IF NOT EXISTS Track (
    filepath    TEXT PRIMARY KEY,
    track_name  TEXT NOT NULL,
    duration    TEXT
);
CREATE TABLE IF NOT EXISTS Album (
    album_id    INTEGER PRIMARY KEY AUTOINCREMENT,
    album_name  TEXT NOT NULL
);
CREATE TABLE IF NOT EXISTS Artist (
    artist_id   INTEGER PRIMARY KEY AUTOINCREMENT,
    artist_name TEXT NOT NULL
);
CREATE TABLE IF NOT EXISTS Genre (
    genre_id    INTEGER PRIMARY KEY AUTOINCREMENT,
    genre_name  TEXT NOT NULL
);
CREATE TABLE IF NOT EXISTS Track_Album (
    filepath    TEXT,
    album_id    INTEGER,
    PRIMARY KEY(filepath, album_id),
    FOREIGN KEY(album_id) REFERENCES Album(album_id),
    FOREIGN KEY(filepath) REFERENCES Track(filepath)
);
CREATE TABLE IF NOT EXISTS Track_Artist (
    filepath    TEXT,
    artist_id   INTEGER,
    PRIMARY KEY(filepath, artist_id),
    FOREIGN KEY(filepath) REFERENCES Track(filepath),
    FOREIGN KEY(artist_id) REFERENCES Artist(artist_id)
);
CREATE TABLE IF NOT EXISTS Track_Genre (
    filepath    TEXT,
    genre_id    INTEGER,
    PRIMARY KEY(filepath, genre_id),
    FOREIGN KEY(filepath) REFERENCES Track(filepath),
    FOREIGN KEY(genre_id) REFERENCES Genre(genre_id)
);
CREATE TABLE IF NOT EXISTS Album_Artist (
    artist_id   INTEGER,
    album_id    INTEGER,
    PRIMARY KEY(artist_id, album_id),
    FOREIGN KEY(artist_id) REFERENCES Artist(artist_id),
    FOREIGN KEY(album_id) REFERENCES Album(album_id)
);