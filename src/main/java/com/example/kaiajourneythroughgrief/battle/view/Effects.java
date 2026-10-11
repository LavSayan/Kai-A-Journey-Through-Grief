package com.example.kaiajourneythroughgrief.battle.view;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Small shared animations: the hit reactions on the character sprites and the
 * fade-in on the result card.
 */
public final class Effects {

    private Effects() {}

    /** Sine oscillation with a linear decay, ending exactly back at zero. */
    public static void shake(Node node, double amplitude, int cycles, int millis) {
        if (node == null) return;

        Timeline shake = new Timeline();
        int steps = cycles * 4;
        double stepTime = millis / (double) steps;

        for (int i = 0; i <= steps; i++) {
            double progress = i / (double) steps;
            double offset = Math.sin(progress * cycles * 2 * Math.PI) * amplitude * (1 - progress);
            shake.getKeyFrames().add(new KeyFrame(
                    Duration.millis(i * stepTime), e -> node.setTranslateX(offset)));
        }
        // Guarantee we end at 0 so the sprite does not drift.
        shake.getKeyFrames().add(new KeyFrame(
                Duration.millis(millis), e -> node.setTranslateX(0)));
        shake.play();
    }

    public static void fadeIn(Node node, long millis) {
        FadeTransition ft = new FadeTransition(Duration.millis(millis), node);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }

    /** Standard ease used by the swap animation. */
    public static double easeInOutQuad(double t) {
        return t < 0.5 ? 2 * t * t : 1 - Math.pow(-2 * t + 2, 2) / 2;
    }
}
