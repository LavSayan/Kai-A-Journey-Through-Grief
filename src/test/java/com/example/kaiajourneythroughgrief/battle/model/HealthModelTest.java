package com.example.kaiajourneythroughgrief.battle.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HealthModelTest {

    @Test
    void drainsOverTimeAndClampsAtZero() {
        HealthModel health = new HealthModel(0.5);
        health.drain(0.04, 1.0);
        assertEquals(0.46, health.value(), 1e-9);
        health.drain(1.0, 10.0);
        assertEquals(0.0, health.value());
        assertTrue(health.isDepleted());
    }

    @Test
    void clampsAtOne() {
        HealthModel health = new HealthModel(0.9);
        health.gain(0.08, 5);
        assertEquals(1.0, health.value());
    }

    @Test
    void gainIsProportionalToCorrectPlacements() {
        HealthModel health = new HealthModel(0.5);
        health.gain(0.08, 2);
        assertEquals(0.66, health.value(), 1e-9);
    }

    @Test
    void resetRestoresTheStartingValue() {
        HealthModel health = new HealthModel(0.5);
        health.drain(1.0, 1.0);
        health.reset();
        assertEquals(0.5, health.value());
    }
}
