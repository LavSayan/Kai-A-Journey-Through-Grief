package com.example.kaiajourneythroughgrief.dialogues;

import com.example.kaiajourneythroughgrief.DialogueConfig;
import com.example.kaiajourneythroughgrief.DialogueConfig.DialogueLine;
import com.example.kaiajourneythroughgrief.DialogueConfig.DialogueLine.Speaker;

import java.util.Arrays;
import java.util.List;

/**
 * Dialogue4: The City of Shattered Glass - Kai encounters Shard
 * Characters: Kai (Left), Shard (Right)
 * Background: City with shattered glass reflections
 */
public class Dialogue4 {

    public static DialogueConfig getConfig() {
        return new DialogueConfig(
                "Dialogue4",
                "/com/example/kaiajourneythroughgrief/assets/dialogueScreen/dialogue4/background.png",
                "",  // backgroundMusicPath - can be empty
                0.2,  // Background music volume (lowered during dialogue)
                "Kai",
                "/com/example/kaiajourneythroughgrief/assets/dialogueScreen/dialogue4/left.png",
                "Shard",
                "/com/example/kaiajourneythroughgrief/assets/dialogueScreen/dialogue4/right.png",
                getDialogueLines()
        );
    }

    private static List<DialogueLine> getDialogueLines() {
        return Arrays.asList(
                new DialogueLine(Speaker.NARRATOR, "The city opens up around KAI as she walks into the city. It doesn't have a gate. For a moment, she was at the edge. Then, she's inside.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/108.mp3"),

                new DialogueLine(Speaker.NARRATOR, "The streets are narrow, not wide enough to walk through without bumping into the walls. The buildings lean inward toward each other as if trying to close around her. Every window is broken. Every surface is glass. Each surface is shattered, strewn with glass, splintered reflections in everything that all seemed to be her.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/109.mp3"),

                new DialogueLine(Speaker.LEFT, "She stops.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/110.mp3"),

                new DialogueLine(Speaker.NARRATOR, "She stands still in a hall that returns every fragment of her in every wall, every shard, every jagged edge lies her own face… staring right back at her.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/111.mp3"),

                new DialogueLine(Speaker.NARRATOR, "Nothing matches.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/112.mp3"),

                new DialogueLine(Speaker.NARRATOR, "One has her face, but different eyes. One has her body, her face, but no expression. The reflections move with her. Some faster, some slower than others.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/113.mp3"),

                new DialogueLine(Speaker.NARRATOR, "She takes a step backward and steps on a jagged piece on the ground. She looks down.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/114.mp3"),

                new DialogueLine(Speaker.NARRATOR, "Her reflection in the mirror is glaring at her. The reflection stops when KAI notices.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/15.mp3"),

                new DialogueLine(Speaker.NARRATOR, "She proceeded and kept walking. Then, she sees it.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/116.mp3"),

                new DialogueLine(Speaker.NARRATOR, "A person standing in the middle of the street. Same clothes. Same hair. Same face. Another… her?",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/117.mp3"),

                new DialogueLine(Speaker.RIGHT, "You took your time.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/118.mp3"),

                new DialogueLine(Speaker.LEFT, "Who are you?",
                        "119.mp3"),

                new DialogueLine(Speaker.RIGHT, "Oh, don't act dumb. You know who I am.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/120.mp3"),

                new DialogueLine(Speaker.LEFT, "No, I don't.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/121.mp3"),

                new DialogueLine(Speaker.RIGHT, "I am the feeling you bury every morning. Whenever someone asks how you are, when your grandmother looks at you with those sad eyes. Each time you see a family at the grocery store and you want to scream at them for no reason.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/122.mp3"),

                new DialogueLine(Speaker.LEFT, "I don't-",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/123.mp3"),

                new DialogueLine(Speaker.RIGHT, "You do. You just bury it. You don't let it out. You sit at your desk and you read the same paragraph over and over because if you stop moving for one second, it might surface.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/124.mp3"),

                new DialogueLine(Speaker.LEFT, "I'm not-",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/125.mp3"),

                new DialogueLine(Speaker.RIGHT, "Not what? Angry? You have been holding that for months. Months of convincing yourself everything's alright. Months of sitting alone, mulling over in your room, pretending to study so you don't have to feel anything.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/126.mp3"),

                new DialogueLine(Speaker.LEFT, "What do you want from me?",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/127.mp3"),

                new DialogueLine(Speaker.RIGHT, "Nothing. I'm you. I want what you want.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/128.mp3"),

                new DialogueLine(Speaker.LEFT, "I don't want anything!",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/129.mp3"),

                new DialogueLine(Speaker.RIGHT, "Yes, you do. You don't even know how to admit it to yourself- you can't. That's why I look like this. You keep burying me. I've been in here for months, in the dark, in the basement, while you continue to live your life, going around and convincing everyone else- even yourself, that you're 'fine.'",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/130.mp3"),

                new DialogueLine(Speaker.NARRATOR, "The other steps closer. KAI steps back.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/131.mp3"),

                new DialogueLine(Speaker.NARRATOR, "The other her laughs. Quiet. Mean.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/132.mp3"),

                new DialogueLine(Speaker.RIGHT, "You have been angry for months. At your parents, at yourself. Everyone who still has parents, everyone else who survived the crash. To every person who assured you it would get better.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/133.mp3"),

                new DialogueLine(Speaker.NARRATOR, "KAI's chest feels heavy. The more her reflection speaks, the more her suppressed emotions cloud her vision.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/134.mp3"),

                new DialogueLine(Speaker.NARRATOR, "Then, KAI swings.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/135.mp3"),

                new DialogueLine(Speaker.NARRATOR, "Her fist goes through her reflection's chest. There was nothing but air. She stumbles forward, nearly dropping to her knees against jagged pieces that laugh at her.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/136.mp3"),

                new DialogueLine(Speaker.NARRATOR, "Her reflection doesn't move.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/137.mp3"),

                new DialogueLine(Speaker.NARRATOR, "The very person staring back at her laughs at the sight. How pitiful. Is that all you can do?",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/138.mp3")
        );
    }
}