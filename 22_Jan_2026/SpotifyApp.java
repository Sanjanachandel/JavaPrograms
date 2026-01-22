package ThreadDay22;

public class SpotifyApp {

    public static void main(String[] args) {

        Spotify spotify = new Spotify();

        LoveSongs loveThread = new LoveSongs(spotify);
        BreakUpSongs breakupThread = new BreakUpSongs(spotify);

        loveThread.start();
        breakupThread.start();
    }
}