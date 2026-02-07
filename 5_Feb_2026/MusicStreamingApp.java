import java.util.HashSet;
import java.util.Set;

public class MusicStreamingApp {

    public static void main(String[] args) {

        Playlist myPlaylist = new Playlist("My Favorites");

        myPlaylist.addSong("Shape of You");
        myPlaylist.addSong("Blinding Lights");
        myPlaylist.addSong("Shape of You");

        myPlaylist.displayPlaylist();

        myPlaylist.removeSong("Blinding Lights");

        myPlaylist.displayPlaylist();
    }
}

class Playlist {

    private String playlistName;
    private Set<String> songs;

    public Playlist(String playlistName) {
        this.playlistName = playlistName;
        this.songs = new HashSet<>();
    }

    public void addSong(String song) {
        if (songs.add(song)) {
            System.out.println(song + " added to " + playlistName + " playlist.");
        } else {
            System.out.println(song + " is already in the playlist!");
        }
    }

    public void removeSong(String song) {
        if (songs.remove(song)) {
            System.out.println(song + " removed from " + playlistName + " playlist.");
        }
    }

    public void displayPlaylist() {
        System.out.println("Playlist: " + playlistName);
        for (String song : songs) {
            System.out.println(" - " + song);
        }
    }
}