package ThreadDay22;

public class Spotify {

    synchronized void playSongs(String type, String[] songs) {

        System.out.println("\n=== " + type + " Songs Playlist ===");

        for (int i = 0; i < songs.length; i++) {
            System.out.println((i + 1) + ". " + songs[i]);

            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("=== " + type + " Playlist Ended ===\n");
    }
}
