package com.example.kaiajourneythroughgrief.dialogues;

import com.example.kaiajourneythroughgrief.DialogueConfig;
import com.example.kaiajourneythroughgrief.DialogueConfig.DialogueLine;
import com.example.kaiajourneythroughgrief.DialogueConfig.DialogueLine.Speaker;

import java.util.Arrays;
import java.util.List;

/**
 * Dialogue1: Opening scene - Kai's bedroom at night
 * Characters: Kai (Left), Mom (Right)
 * Background: Bedroom - Night
 */
public class Dialogue1 {

    public static DialogueConfig getConfig() {
        return new DialogueConfig(
                "Dialogue1",
                "/com/example/kaiajourneythroughgrief/assets/dialogueScreen/dialogue1/background.png",
                "/com/example/kaiajourneythroughgrief/assets/bgmusic.mp3",  // backgroundMusicPath - can be empty
                0.05,  // Background music volume (50% during dialogue)
                "Kai",
                "/com/example/kaiajourneythroughgrief/assets/dialogueScreen/dialogue1/left.png",
                null,
                null,
                getDialogueLines()
        );
    }

    private static List<DialogueLine> getDialogueLines() {
        return Arrays.asList(
                new DialogueLine(Speaker.NARRATOR, "For KAI, the night is too quiet for the hour. It's the kind of quietude that nests within the small confines of her bedroom, settling into the walls, stretching thin across the ceiling.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/1.mp3"),
                new DialogueLine(Speaker.NARRATOR, "The refrigerator hums from the kitchen. She hears the air-conditioning unit make a concerning sound, but she's never been bothered to check it out herself or call maintenance.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/2.mp3"),
                new DialogueLine(Speaker.NARRATOR, "KAI's gaze drops to the open textbook laid in front of her.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/3.mp3"),
                new DialogueLine(Speaker.LEFT, "Nutrient cycling.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/4.mp3"),
                new DialogueLine(Speaker.NARRATOR, "She reads the line once. Then, again.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/5.mp3"),
                new DialogueLine(Speaker.LEFT, "Focus.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/6.mp3"),
                new DialogueLine(Speaker.NARRATOR, "She heaves a breath, pressing her tongue against the inside of her cheek as she briefly closes her eyes—opening them again to drag her attention back to the beginning of the paragraph.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/7.mp3"),
                new DialogueLine(Speaker.NARRATOR, "At times like this, she can only hope for her mind to refrain from relentlessly wandering into irrelevant thoughts unrelated to her studies.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/8.mp3"),
                new DialogueLine(Speaker.LEFT, "Just finish this chapter.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/9.mp3"),
                new DialogueLine(Speaker.NARRATOR, "Despite all her attempts at tricking her brain into studying, it's to no avail. The words blur halfway through as she scans the paragraph, and when she tries to recall the text, her mind returns nothing but a blank output.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/10.mp3"),
                new DialogueLine(Speaker.NARRATOR, "She exhales sharply through her nose.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/11.mp3"),
                new DialogueLine(Speaker.LEFT, "Okay.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/12.mp3"),
                new DialogueLine(Speaker.NARRATOR, "She mutters under her breath, barely audible even to herself.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/13.mp3"),
                new DialogueLine(Speaker.LEFT, "Again.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/14.mp3"),
                new DialogueLine(Speaker.NARRATOR, "Her pencil hovers over the start of the same paragraph she's gone through multiple times, unmoving.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/15.mp3"),
                new DialogueLine(Speaker.NARRATOR, "The letters etched on the page don't look right; the words barely make sense to KAI's worn-out brain, which apparently refuses to absorb any more information.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/16.mp3"),
                new DialogueLine(Speaker.NARRATOR, "The lines blur, the ink bleeds into itself, until every sentence becomes nothing more than a dark smear across the paper.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/17.mp3"),
                new DialogueLine(Speaker.NARRATOR, "The floor lamp beside her desk flickers—too bright and too yellow.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/18.mp3"),
                new DialogueLine(Speaker.NARRATOR, "For a moment, KAI wonders if her focus would snap back into place had she only stood up, walked around, or if it's just the light.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/19.mp3"),
                new DialogueLine(Speaker.LEFT, "No.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/20.mp3"),
                new DialogueLine(Speaker.LEFT, "You've done harder than this.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/21.mp3"),
                new DialogueLine(Speaker.NARRATOR, "The thought doesn't seep within her to settle. She grabs her phone, left untouched on the desk, and turns it on. The clock reads 3:15 A.M.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/22.mp3"),
                new DialogueLine(Speaker.LEFT, "It's just late, that's all.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/23.mp3"),
                new DialogueLine(Speaker.NARRATOR, "KAI yawns, rubs at her eyes, then straightens slightly in her chair.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/24.mp3"),
                new DialogueLine(Speaker.LEFT, "Energy and matter are transferred...",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/25.mp3"),
                new DialogueLine(Speaker.NARRATOR, "She stops. KAI exhales sharply, dragging her hand down her face and covering it with both hands, murmuring something inaudible like she did earlier.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/26.mp3"),
                new DialogueLine(Speaker.LEFT, "I should ask mom…",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/27.mp3"),
                new DialogueLine(Speaker.NARRATOR, "The train of thought halts before it concludes, as if snapping her back to her own senses. She sneers at the incredulity of it.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/28.mp3"),
                new DialogueLine(Speaker.LEFT, "What am I thinking? They're gone. Gone, Kai. Get that into your head.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/29.mp3"),
                new DialogueLine(Speaker.NARRATOR, "She tears away the hands caging her face, as if she were hiding from the textbook that might swallow her whole if she continued reading.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/30.mp3"),
                new DialogueLine(Speaker.LEFT, "Whatever.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/31.mp3"),
                new DialogueLine(Speaker.NARRATOR, "She flips through the pages of her Biology textbook, skipping to an entirely different chapter.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/32.mp3"),
                new DialogueLine(Speaker.NARRATOR, "KAI goes still once she hears a soft creak emerge from the other room.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/33.mp3"),
                new DialogueLine(Speaker.NARRATOR, "It must be her grandmother, already awake.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/34.mp3"),
                new DialogueLine(Speaker.NARRATOR, "However, the realization only dawns on her that she's been awake longer than necessary.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/35.mp3"),
                new DialogueLine(Speaker.LEFT, "Of course, she's already awake.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/36.mp3"),
                new DialogueLine(Speaker.NARRATOR, "Or maybe she's just… She just sighs to herself. KAI leans back slightly, her eyes fixating on the edge of her desk rather than the textbook in front of her.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/37.mp3"),
                new DialogueLine(Speaker.LEFT, "It's fine. You just need to pass. It's all over after that.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/38.mp3"),
                new DialogueLine(Speaker.NARRATOR, "Over what, exactly?",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/39.mp3"),
                new DialogueLine(Speaker.NARRATOR, "She looks back down, her hand once again finding the edge of the book as she flips through page after page in a haphazard manner, the words not once registering in her head.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/40.mp3"),
                new DialogueLine(Speaker.NARRATOR, "The clock ticks to 3:21. She exhales—slower, less rushed, more exhausted. She takes off her glasses and dips her head forward to lean against the desk, the surface reeking of ink and paper. Her vision blurs. She closes her eyes, just for a second.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/41.mp3"),
                new DialogueLine(Speaker.LEFT, "Just a few minutes.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/42.mp3")
        );
    }
}