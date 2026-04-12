package com.example.kaiajourneythroughgrief.dialogues;

import com.example.kaiajourneythroughgrief.DialogueConfig;
import com.example.kaiajourneythroughgrief.DialogueConfig.DialogueLine;
import com.example.kaiajourneythroughgrief.DialogueConfig.DialogueLine.Speaker;

import java.util.Arrays;
import java.util.List;

/**
 * Dialogue8: Waking Up - Kai returns to reality with a new perspective
 * Characters: Kai (Left), The Mermaid (Right)
 * Background: Bedroom returning to reality
 */
public class Dialogue8 {

    public static DialogueConfig getConfig() {
        return new DialogueConfig(
                "Dialogue8",
                "/com/example/kaiajourneythroughgrief/assets/dialogueScreen/dialogue8/background.png",
                "/com/example/kaiajourneythroughgrief/assets/stage5/bgmusic.mp3",  // backgroundMusicPath - can be empty
                0.05,  // Background music volume (lowered during dialogue)
                "Kai",
                "/com/example/kaiajourneythroughgrief/assets/dialogueScreen/dialogue8/left.png",
                "Grandma",
                "/com/example/kaiajourneythroughgrief/assets/dialogueScreen/dialogue8/right.png",
                getDialogueLines()
        );
    }

    private static List<DialogueLine> getDialogueLines() {
        return Arrays.asList(
                new DialogueLine(Speaker.NARRATOR, "As the garden's sunlight grows overwhelming, Kai feels her body growing heavy, but it's different from before. Before, she felt weighed down. Now, she feels... grounded. Solid. Present.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/257.mp3"),

                new DialogueLine(Speaker.NARRATOR, "She wakes at her desk, 3:28 AM, only seven minutes have passed. The textbook is still open to the same page. But everything feels different. The grief isn't gone - she knows it never will be - but it's no longer crushing her. It's part of her now, integrated, carried.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/258.mp3"),

                new DialogueLine(Speaker.NARRATOR, "A soft knock at her door. Gentle. Hesitant.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/259.mp3"),

                new DialogueLine(Speaker.RIGHT, "Don't study too late, sweetheart. Love you.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/260.mp3"),

                new DialogueLine(Speaker.NARRATOR, "For the first time in three months, KAI answers..",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/261.mp3"),

                new DialogueLine(Speaker.LEFT, "Love you too, Grandma.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/262.mp3"),

                new DialogueLine(Speaker.NARRATOR, "A pause. Then, softly, her grandmother's footsteps retreating down the hall.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/263.mp3"),

                new DialogueLine(Speaker.NARRATOR, "Kai closes the textbook, climbs into bed, and sleeps, truly sleeps, for the first time since the accident.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/264.mp3")
        );
    }
}