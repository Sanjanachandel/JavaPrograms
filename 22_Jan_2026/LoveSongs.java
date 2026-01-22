package ThreadDay22;



public class LoveSongs extends Thread {

    private final Spotify player;

    LoveSongs(Spotify player) {
        this.player = player;
    }

    @Override
    public void run() {

        String[] loveSongs = {
            "Chand Sifarish - Shaan, Kailash Kher",
            "Phir Bhi Tumko Chaahunga - Arijit Singh",
            "Duniyaa - Akhil & Dhvani Bhanushali"
        };

        player.playSongs("Top 3 Hindi Love Songs", loveSongs);
    }
}
