package com.example.kaiajourneythroughgrief;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;

/**
 * Singleton music player shared across all screens.
 *
 * Usage:
 *   MusicPlayer.getInstance().play("bgmusic.mp3");   // start / resume
 *   MusicPlayer.getInstance().stop();                // stop and release
 *   MusicPlayer.getInstance().isPlaying();           // check state
 *
 * Calling play() with the same filename while already playing does nothing,
 * so the music continues seamlessly when navigating between menu and level screen.
 *
 * Place bgmusic.mp3 in:
 *   src/main/resources/com/example/kaiajourneythroughgrief/assets
 */
public class MusicPlayer {

    // ── Singleton ─────────────────────────────────────────────────────────
    private static MusicPlayer instance;

    public static MusicPlayer getInstance() {
        if (instance == null) instance = new MusicPlayer();
        return instance;
    }

    private MusicPlayer() {}

    // ── State ─────────────────────────────────────────────────────────────
    private MediaPlayer player;
    private String      currentFile;

    // ── Volume (0.0 – 1.0) ────────────────────────────────────────────────
    private static final double VOLUME = 0.5;

    /**
     * Starts playing the given file on an infinite loop.
     * If the same file is already playing, this is a no-op — music continues
     * without interruption across scene switches.
     *
     * @param filename e.g. "bgmusic.mp3" — must be in the resources folder
     *                 next to this class.
     */
    public void play(String filename) {
        // Already playing the right track — do nothing
        if (filename.equals(currentFile) && player != null
                && player.getStatus() == MediaPlayer.Status.PLAYING) {
            return;
        }

        // Stop any existing player first
        stopQuietly();

        URL resource = getClass().getResource(filename);
        if (resource == null) {
            System.err.println("[MusicPlayer] File not found: " + filename
                    + " — place it in resources/com/example/kaiajourneythroughgrief/");
            return;
        }

        try {
            Media media = new Media(resource.toExternalForm());
            player = new MediaPlayer(media);
            player.setVolume(VOLUME);
            player.setCycleCount(MediaPlayer.INDEFINITE);  // loop forever
            player.setOnError(() ->
                    System.err.println("[MusicPlayer] Playback error: " + player.getError()));
            player.play();
            currentFile = filename;
        } catch (Exception e) {
            System.err.println("[MusicPlayer] Could not start playback: " + e.getMessage());
        }
    }

    /**
     * Stops playback and releases the MediaPlayer.
     * Call this when entering a battle stage or quitting.
     */
    public void stop() {
        stopQuietly();
        currentFile = null;
    }

    /**
     * Fades the music out over {@code millis} milliseconds, then stops.
     * Use this for smoother transitions into battle scenes.
     */
    public void fadeOutAndStop(long millis) {
        if (player == null) return;
        MediaPlayer p = player;  // capture reference before nulling

        // Step volume down from current to 0 over the given duration
        double startVol = p.getVolume();
        int    steps    = 30;
        long   stepMs   = millis / steps;

        javafx.animation.Timeline fade = new javafx.animation.Timeline();
        for (int i = 1; i <= steps; i++) {
            double vol = startVol * (1.0 - i / (double) steps);
            long   t   = stepMs * i;
            fade.getKeyFrames().add(new javafx.animation.KeyFrame(
                    javafx.util.Duration.millis(t),
                    e -> p.setVolume(vol)
            ));
        }
        fade.setOnFinished(e -> { p.stop(); p.dispose(); });
        fade.play();

        player      = null;
        currentFile = null;
    }

    /** Returns true if music is currently playing. */
    public boolean isPlaying() {
        return player != null && player.getStatus() == MediaPlayer.Status.PLAYING;
    }

    // =========================================================================
    // PRIVATE
    // =========================================================================

    private void stopQuietly() {
        if (player != null) {
            player.stop();
            player.dispose();
            player = null;
        }
    }
}