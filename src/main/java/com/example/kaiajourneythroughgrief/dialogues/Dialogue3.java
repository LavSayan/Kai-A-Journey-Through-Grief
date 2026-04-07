package com.example.kaiajourneythroughgrief.dialogues;

import com.example.kaiajourneythroughgrief.DialogueConfig;
import com.example.kaiajourneythroughgrief.DialogueConfig.DialogueLine;
import com.example.kaiajourneythroughgrief.DialogueConfig.DialogueLine.Speaker;

import java.util.Arrays;
import java.util.List;

/**
 * Dialogue3: The Forest Frozen in Fog - Kai encounters Ouroboro
 * Characters: Kai (Left), Ouroboro (Right)
 * Background: Frozen forest with suspended time
 */
public class Dialogue3 {

    public static DialogueConfig getConfig() {
        return new DialogueConfig(
                "Dialogue3",
                "/com/example/kaiajourneythroughgrief/assets/dialogueScreen/dialogue3/background.png",
                "/com/example/kaiajourneythroughgrief/assets/stage1/bgmusic.mp3",  // backgroundMusicPath - can be empty
                0.05,  // Background music volume (lowered during dialogue)
                "Kai",
                "/com/example/kaiajourneythroughgrief/assets/dialogueScreen/dialogue3/left.png",
                "Ouroboro",
                "/com/example/kaiajourneythroughgrief/assets/dialogueScreen/dialogue3/right.png",
                getDialogueLines()
        );
    }

    private static List<DialogueLine> getDialogueLines() {
        return Arrays.asList(
                new DialogueLine(Speaker.NARRATOR, "KAI enters the forest. The trees are frozen mid-sway. Leaves hang in the air. A bird is suspended with wings half-open. Time has stopped here.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/101.mp3"),

                new DialogueLine(Speaker.NARRATOR, "She finds OUROBORO, a massive serpent coiled in an endless circle, tail in mouth. Its single pale eye watches her.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/102.mp3"),

                new DialogueLine(Speaker.RIGHT, "You know me. You have been carrying me for three months. The same hour. The same sentence. The same chair. You wrapped yourself around the moment before everything ended and called it survival.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/r.mp3"),

                new DialogueLine(Speaker.NARRATOR, "KAI tells herself she isn't frozen. She goes to class. She studies. She functions. But the argument sounds hollow even in her own head.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/104.mp3"),

                new DialogueLine(Speaker.RIGHT, "You perform. You do not live. I am the belief that if you do not move forward, nothing else can be taken from you.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/r.mp3"),

                new DialogueLine(Speaker.NARRATOR, "The serpent tightens. The fog coils in. The air becomes suffocating.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/106.mp3"),

                new DialogueLine(Speaker.RIGHT, "If you wish to leave this forest, you must go through me. Prove you are ready to move.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/r.mp3")
        );
    }
}