package com.example.kaiajourneythroughgrief.dialogues;

import com.example.kaiajourneythroughgrief.DialogueConfig;
import com.example.kaiajourneythroughgrief.DialogueConfig.DialogueLine;
import com.example.kaiajourneythroughgrief.DialogueConfig.DialogueLine.Speaker;

import java.util.Arrays;
import java.util.List;

/**
 * Dialogue6: The Ocean of Still Water - Kai encounters The Mermaid
 * Characters: Kai (Left), The Mermaid (Right)
 * Background: Ocean with still water
 */
public class Dialogue6 {

    public static DialogueConfig getConfig() {
        return new DialogueConfig(
                "Dialogue6",
                "/com/example/kaiajourneythroughgrief/assets/dialogueScreen/dialogue6/background.png",
                "/com/example/kaiajourneythroughgrief/assets/stage4/bgmusic.mp3",  // backgroundMusicPath - can be empty
                0.05,  // Background music volume (lowered during dialogue)
                "Kai",
                "/com/example/kaiajourneythroughgrief/assets/dialogueScreen/dialogue6/left.png",
                "The Mermaid",
                "/com/example/kaiajourneythroughgrief/assets/dialogueScreen/dialogue6/right.png",
                getDialogueLines()
        );
    }

    private static List<DialogueLine> getDialogueLines() {
        return Arrays.asList(
                new DialogueLine(Speaker.NARRATOR, "KAI steps onto the water. She doesn't sink underneath. The surface holds her weight as she walks forward. The shore disappears behind her.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/201.mp3"),

                new DialogueLine(Speaker.NARRATOR, "She couldn't see anything beyond the horizon. The background- or the remnants of it- the sand, city, temple and forest vanished like a mist behind her.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/202.mp3"),

                new DialogueLine(Speaker.NARRATOR, "All that remains is water. Unending, deep water.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/203.mp3"),

                new DialogueLine(Speaker.NARRATOR, "From a distance, she sees a boat. She walks closer to it and gets in.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/204.mp3"),

                new DialogueLine(Speaker.NARRATOR, "The boat moves on its own, deeper into the ocean. The water doesn't change. The sky doesn't change. Everything is bathed in indigo.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/205.mp3"),

                new DialogueLine(Speaker.NARRATOR, "Everything remains still.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/206.mp3"),

                new DialogueLine(Speaker.NARRATOR, "She then hears a faint, low voice. Singing.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/207.mp3"),

                new DialogueLine(Speaker.NARRATOR, "KAI looks around. She sees nothing. Just water.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/208.mp3"),

                new DialogueLine(Speaker.NARRATOR, "The singing gets louder.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/209.mp3"),

                new DialogueLine(Speaker.NARRATOR, "A figure emerges from the water. A few feet ahead. Half-submerged. Long hair floating on the surface. Skin the color of deep water. Eyes that reflect nothing.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/210.mp3"),

                new DialogueLine(Speaker.NARRATOR, "A mermaid.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/211.mp3"),

                new DialogueLine(Speaker.NARRATOR, "It looks wrong in many ways. Not beautiful. Not seductive. Just sad. Her mouth moves. The song comes out, without words. Just a hum that pulls something in KAI's chest.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/212.mp3"),

                new DialogueLine(Speaker.NARRATOR, "The boat stops.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/213.mp3"),

                new DialogueLine(Speaker.NARRATOR, "The mermaid swims, a lot closer now, circling the boat. She never fully breaks the surface, as only her head, shoulders and her hands gripping the edge of the boat are the only ones that can be seen.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/214.mp3"),

                new DialogueLine(Speaker.NARRATOR, "Her eyes meet KAI's.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/215.mp3"),

                new DialogueLine(Speaker.NARRATOR, "The song gets louder.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/216.mp3"),

                new DialogueLine(Speaker.NARRATOR, "KAI feels it tug in her bones. It was a force that wanted her to stop moving. It wants her to lie down. To let the water take her.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/217.mp3"),

                new DialogueLine(Speaker.NARRATOR, "The mermaid sinks deeper into the water. Her eyes, however, remain above the surface.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/218.mp3"),

                new DialogueLine(Speaker.LEFT, "Everyone eventually leaves. That's what this is. The ocean. The unending water. The feeling of being alone and no one is coming.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/219.mp3")
        );
    }
}