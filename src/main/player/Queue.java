package main.player;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.library.Track;


public class Queue {
    public final int SEEK_LEFT = -1;
    public final int SEEK_RIGHT = 1;

    private ObservableList<Track> tracks;
    private ObservableList<String> trackNames;
    private int currentIndex;


    public Queue() {
        currentIndex = 0;
        tracks = FXCollections.observableArrayList();
        trackNames = FXCollections.observableArrayList();
    }


    public void addNext(Track track) {
        if (tracks.size() == 0) {
            tracks.add(track);
            trackNames.add(track.getName());
        } else {
            tracks.add(currentIndex + 1, track);
            trackNames.add(1, track.getName());
        }
    }

    public void addNext(ObservableList<Track> newTracks) {
        if (tracks.size() == 0) {
            tracks.addAll(newTracks);
            for (int i = 0; i < newTracks.size(); i++) {
                trackNames.add(newTracks.get(i).getName());
            }
        } else {
            for (int j = 0, i = currentIndex + 1; j < newTracks.size(); i++, j++) {
                tracks.add(i, newTracks.get(j));
                trackNames.add(j + 1, newTracks.get(j).getName());
            }
        }
    }

    public void add(Track track) {
        tracks.add(track);
        trackNames.add(track.getName());
    }

    public void add(ObservableList<Track> newTracks) {
        for (Track track : newTracks) {
            tracks.add(track);
            trackNames.add(track.getName());
        }
    }

    public Track getCurrentTrack() {
        return tracks.get(currentIndex);
    }

    public Track quickLoadTrack(Track track) {
        addNext(track);

        if (tracks.size() > 1) {
            currentIndex += 1;
        }

        if (trackNames.size() > 1) {
            trackNames.remove(0);
        }

        return tracks.get(currentIndex);
    }


    public Track next() {
        if (tracks.size() > 1) {
            currentIndex += 1;
            trackNames.remove(0);
        }

        return tracks.get(currentIndex);
    }


    public Track previous() {
        if (currentIndex > 0) {
            currentIndex -= 1;
            trackNames.add(0, tracks.get(currentIndex).getName());
        }

        return tracks.get(currentIndex);
    }


    public boolean hasTrack(int direction) {
        int index = currentIndex + direction;

        if (index < 0 || index > tracks.size() - 1) {
            return false;
        }

        if (tracks.get(index) != null) {
            return true;
        }

        return false;
    }

    public int size() {
        return tracks.size();
    }

    public ObservableList<Track> getTracks() {
        return tracks;
    }

    public ObservableList<String> getTrackNames() {
        return trackNames;
    }
}