package com.example.kaiajourneythroughgrief;

/**
 * Entry point for the packaged jar.
 *
 * <p>The {@code java} launcher refuses to start a shaded JavaFX application when
 * the main class directly extends {@link javafx.application.Application}: it sees
 * the subclass and demands the JavaFX modules on the module path, even though
 * they are already bundled in the jar. Starting through a plain class that simply
 * delegates to {@link Main#main} sidesteps that check, so the fat jar runs with a
 * bare {@code java -jar}.
 */
public final class Launcher {

    private Launcher() {}

    public static void main(String[] args) {
        Main.main(args);
    }
}
