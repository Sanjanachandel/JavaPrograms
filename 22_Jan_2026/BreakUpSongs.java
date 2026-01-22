package ThreadDay22;



public class BreakUpSongs extends Thread {

    private final Spotify player;

    BreakUpSongs(Spotify player) {
        this.player = player;
    }

    @Override
    public void run() {

        String[] breakupSongs = {
            "Agar Tum Saath Ho - Arijit Singh & Alka Yagnik",
            "Pachtaoge - Arijit Singh",
            "Saiyaara - Faheem Abdullah"
        };

        player.playSongs("Top 3 Hindi Breakup", breakupSongs);
    }
}
