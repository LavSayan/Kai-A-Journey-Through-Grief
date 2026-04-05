package com.example.kaiajourneythroughgrief.dialogues;

import com.example.kaiajourneythroughgrief.DialogueConfig;
import com.example.kaiajourneythroughgrief.DialogueConfig.DialogueLine;
import com.example.kaiajourneythroughgrief.DialogueConfig.DialogueLine.Speaker;

import java.util.Arrays;
import java.util.List;

/**
 * Dialogue7: The Garden at Dawn - Kai encounters her Future Self
 * Characters: Kai (Left), Future Kai (Right)
 * Background: Garden with golden light
 */
public class Dialogue7 {

    public static DialogueConfig getConfig() {
        return new DialogueConfig(
                "Dialogue7",
                "/com/example/kaiajourneythroughgrief/assets/dialogueScreen/dialogue7/background.png",
                "",  // backgroundMusicPath - can be empty
                0.2,  // Background music volume (lowered during dialogue)
                "Kai",
                "/com/example/kaiajourneythroughgrief/assets/dialogueScreen/dialogue7/stage7/left.png",
                "The Eclipse",
                "/com/example/kaiajourneythroughgrief/assets/dialogueScreen/dialogue7/right.png",
                getDialogueLines()
        );
    }

    private static List<DialogueLine> getDialogueLines() {
        return Arrays.asList(
                new DialogueLine(Speaker.NARRATOR, "The air is still. The walls shimmer faintly. KAI stood alone, in the center of the garden, the weight of grief and the chaos that's supposedly over now quiet in the void.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/220.mp3"),

                new DialogueLine(Speaker.NARRATOR, "A voice cuts through.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/221.mp3"),

                new DialogueLine(Speaker.RIGHT, "You took longer than I expected.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/222.mp3"),

                new DialogueLine(Speaker.LEFT, "Who... are you?",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/223.mp3"),

                new DialogueLine(Speaker.RIGHT, "KAI.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/224.mp3"),

                new DialogueLine(Speaker.NARRATOR, "KAI turns, looking around the space as she searches the light. The figure emerges slowly, shimmering first, then settling.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/225.mp3"),

                new DialogueLine(Speaker.NARRATOR, "The figure solidifies. It takes her form. It's her.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/226.mp3"),

                new DialogueLine(Speaker.NARRATOR, "Her height, her face, her clothes. All identical, as if it's a memory of the reflection she saw a few stages past- but without the anger it harbors. It's a version of herself she's only heard of but never met yet.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/227.mp3"),

                new DialogueLine(Speaker.RIGHT, "Do not be afraid. I am you, before you, and after.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/228.mp3"),

                new DialogueLine(Speaker.LEFT, "W-wait… you're… me? How… how can this be?",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/229.mp3"),

                new DialogueLine(Speaker.RIGHT, "I am you, I am the one who has endured what you only are finishing now.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/230.mp3"),

                new DialogueLine(Speaker.RIGHT, "To speak plainly, KAI. You have walked every path, faced every challenge- overcame all shadows. You endured more than you realize. I am here to show you what it all meant.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/231.mp3"),

                new DialogueLine(Speaker.LEFT, "… So, all the voices… the lights, the wind… the enemies, figures… that was me all along?",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/232.mp3"),

                new DialogueLine(Speaker.RIGHT, "Exactly. Every guide, every obstacle, every hesitation you felt. It has always been a part of you, waiting for you to see it.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/233.mp3"),

                new DialogueLine(Speaker.LEFT, "Were any of the stages- the choices I made, real?",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/234.mp3"),

                new DialogueLine(Speaker.RIGHT, "It was sufficient enough to teach you. Real enough to shape who you are now.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/235.mp3"),

                new DialogueLine(Speaker.NARRATOR, "A gust of wind sweeps from the still forest covered in blue light.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/236.mp3"),

                new DialogueLine(Speaker.RIGHT, "Denial. A refusal to believe something is gone and would never come back. It makes people cling onto the memories of what remains. You clung to the memories, to what has been, afraid to let everything go.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/237.mp3"),

                new DialogueLine(Speaker.NARRATOR, "The shattered city behind her blinks in a soft hue of red.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/238.mp3"),

                new DialogueLine(Speaker.RIGHT, "Anger. Raging at the world, at yourself, at everyone else. It burned brightly in red, as you abhorred your own helplessness. You hated what you could no longer change, what was taken away from you.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/239.mp3"),

                new DialogueLine(Speaker.NARRATOR, "In the distance, a temple with endless doors hovers, yellow light luminous through the cracks.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/240.mp3"),

                new DialogueLine(Speaker.RIGHT, "Bargaining. The dilemma of endless questions, seeking answers, second chances, and anything to bring back what was lost and reverse time itself.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/241.mp3"),

                new DialogueLine(Speaker.NARRATOR, "The ocean remains still as it stretches infinitely.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/242.mp3"),

                new DialogueLine(Speaker.RIGHT, "Depression. One sinks into it. The feeling of being swallowed. Overwhelmed. Alone. But even there, you withstood it. You endured. You kept moving- for as long as you could- even when you ran on autopilot; even when the world seemed absent.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/243.mp3"),

                new DialogueLine(Speaker.NARRATOR, "The garden, bathed in a luminous light glows softly around them.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/244.mp3"),

                new DialogueLine(Speaker.RIGHT, "Acceptance. You have faced loss. You have arrived at where you were supposed to have been. You faced inevitability, survived in the process- not by actively overlooking it, but by going through it.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/245.mp3"),

                new DialogueLine(Speaker.NARRATOR, "KAI exhales.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/246.mp3"),

                new DialogueLine(Speaker.RIGHT, "I am you who has carried everything forward. I am you who learned to accept, despite it being rigorous, what is gone and what remains of it. I am you, proof that grief is not about forgetting.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/247.mp3"),

                new DialogueLine(Speaker.NARRATOR, "KAI looks down to her hands and sees how it trembles. She didn't even realize the tremor. She looks back up to the golden light.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/248.mp3"),

                new DialogueLine(Speaker.LEFT, "… but what now?",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/249.mp3"),

                new DialogueLine(Speaker.RIGHT, "Now, you carry on all that you have learned. You must bring it with you on your journey forward. It is not meant to be a burden on your own, but rather as a proof of your strength and resilience.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/250.mp3"),

                new DialogueLine(Speaker.NARRATOR, "KAI exhales slowly.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/251.mp3"),

                new DialogueLine(Speaker.RIGHT, "This is not the end, KAI. It is only the beginning. It is time.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/252.mp3"),

                new DialogueLine(Speaker.LEFT, "Time for what?",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/253.mp3"),

                new DialogueLine(Speaker.RIGHT, "Time for me to leave. Time for you to live.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/254.mp3"),

                new DialogueLine(Speaker.LEFT, "I… I will…",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/255.mp3"),

                new DialogueLine(Speaker.RIGHT, "I shall now leave this moment to you, KAI. May those who follow learn courage in the path we've walked together.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/256.mp3")
        );
    }
}