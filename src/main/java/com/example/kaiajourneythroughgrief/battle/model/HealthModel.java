package com.example.kaiajourneythroughgrief.battle.model;

/**
 * The player's vitality meter: drains continuously, pays out for good moves.
 * Pure state — no JavaFX, so it can be tested headless.
 */
public final class HealthModel {

    private final double startValue;
    private double value;

    public HealthModel(double startValue) {
        this.startValue = startValue;
        this.value = startValue;
    }

    public double value() {
        return value;
    }

    public void set(double newValue) {
        this.value = clamp(newValue);
    }

    /** Applies passive drain for one frame. */
    public void drain(double perSecond, double deltaSeconds) {
        set(value - perSecond * deltaSeconds);
    }

    /** Pays out health for {@code correctCount} tiles that landed correctly. */
    public void gain(double perCorrect, int correctCount) {
        set(value + perCorrect * correctCount);
    }

    public void reset() {
        this.value = startValue;
    }

    public boolean isDepleted() {
        return value <= 0.0;
    }

    private static double clamp(double v) {
        return Math.max(0.0, Math.min(1.0, v));
    }
}
