package com.example.kaiajourneythroughgrief.dialogues;

import com.example.kaiajourneythroughgrief.DialogueConfig;
import com.example.kaiajourneythroughgrief.DialogueConfig.DialogueLine;
import com.example.kaiajourneythroughgrief.DialogueConfig.DialogueLine.Speaker;

import java.util.Arrays;
import java.util.List;

/**
 * Dialogue5: The Temple of Endless Doors - Kai encounters The Door
 * Characters: Kai (Left), The Door (Right)
 * Background: Temple with glowing doors
 */
public class Dialogue5 {

    public static DialogueConfig getConfig() {
        return new DialogueConfig(
                "Dialogue5",
                "/com/example/kaiajourneythroughgrief/assets/dialogueScreen/dialogue5/background.png",
                "",  // backgroundMusicPath - can be empty
                0.2,  // Background music volume (lowered during dialogue)
                "Kai",
                "/com/example/kaiajourneythroughgrief/assets/dialogueScreen/dialogue5/left.png",
                "The Door",
                "/com/example/kaiajourneythroughgrief/assets/dialogueScreen/dialogue5/right.png",
                getDialogueLines()
        );
    }

    private static List<DialogueLine> getDialogueLines() {
        return Arrays.asList(
                new DialogueLine(Speaker.NARRATOR, "Inside the temple, there are doors everywhere. Floor, ceiling, walls. Thousands of them, all glowing in yellow.",
                        "139.mp3"),

                new DialogueLine(Speaker.NARRATOR, "She walks toward the nearest door and opens it.",
                        "140.mp3"),

                new DialogueLine(Speaker.NARRATOR, "Another.",
                        "141.mp3"),

                new DialogueLine(Speaker.NARRATOR, "Another.",
                        "142.mp3"),

                new DialogueLine(Speaker.NARRATOR, "In every door she opened, every room she entered, showed every version of the conversation she had with her parents the night before they died. She attempts screaming, pleading, begging, threatening, bargaining.",
                        "143.mp3"),

                new DialogueLine(Speaker.NARRATOR, "In some of the rooms, they listen. Sometimes they don't.",
                        "144.mp3"),

                new DialogueLine(Speaker.NARRATOR, "Every time, the door closes and she finds herself back in the temple.",
                        "145.mp3"),

                new DialogueLine(Speaker.LEFT, "KAI opens another door.",
                        "146.mp3"),

                new DialogueLine(Speaker.NARRATOR, "As KAI stepped out of the temple, the yellow light dies upon leaving it behind.",
                        "147.mp3"),

                new DialogueLine(Speaker.NARRATOR, "She walks forward, on and on, until she can no longer see doors. Then, she stops.",
                        "148.mp3"),

                new DialogueLine(Speaker.NARRATOR, "Like what she had seen earlier, the ground is gray- the sky remained empty. The only thing noticeable was the absence of life.",
                        "149.mp3"),

                new DialogueLine(Speaker.RIGHT, "Did you find out what you were looking for?",
                        "150.mp3"),

                new DialogueLine(Speaker.LEFT, "No.",
                        "151.mp3"),

                new DialogueLine(Speaker.RIGHT, "Then, why did you keep opening the other doors?",
                        "152.mp3"),

                new DialogueLine(Speaker.LEFT, "Perhaps it's some wishful thinking, that maybe the next one-",
                        "153.mp3"),

                new DialogueLine(Speaker.RIGHT, "Would have a different outcome. That, indeed, is the nature of bargaining. You convince yourself you are yet to attempt the correct 'actions.' The right words. The right decision.",
                        "154.mp3"),

                new DialogueLine(Speaker.NARRATOR, "KAI remained quiet.",
                        "155.mp3"),

                new DialogueLine(Speaker.RIGHT, "Say, what did you offer them? In the rooms? What did you offer- sacrifice, in exchange for their lives?",
                        "156.mp3"),

                new DialogueLine(Speaker.LEFT, "Myself. My happiness. I promised I would be better. I would never complain. I would never ask anything from anyone.",
                        "157.mp3"),

                new DialogueLine(Speaker.RIGHT, "Did they accept those offers?",
                        "158.mp3"),

                new DialogueLine(Speaker.LEFT, "Not every time, that's for sure. They would agree to stay. They would promise not to leave. And then the door would close anyway.",
                        "159.mp3"),

                new DialogueLine(Speaker.RIGHT, "Because a door is not a contract. You seem to have failed to understand that. A door is a question you keep asking. 'What if.' 'What if.' 'What if.'",
                        "160.mp3"),

                new DialogueLine(Speaker.LEFT, "I know.",
                        "161.mp3"),

                new DialogueLine(Speaker.RIGHT, "Do you? You don't know. You still believe that somewhere, in some door, there is a version of events where they live.",
                        "162.mp3"),

                new DialogueLine(Speaker.LEFT, "...Is there any?",
                        "163.mp3"),

                new DialogueLine(Speaker.RIGHT, "No.",
                        "1/com/example/kaiajourneythroughgrief/assets/dialogue/164.mp3"),

                new DialogueLine(Speaker.NARRATOR, "The voice says it bluntly.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/165.mp3"),

                new DialogueLine(Speaker.NARRATOR, "KAI feels something tug inside her chest.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/166.mp3"),

                new DialogueLine(Speaker.RIGHT, "There is no door where they survive. There is no version of that night where the car swerves differently. There is no timeline where your mother finishes her sentence. Where your father turns the wheel one second earlier. Where the other driver is not on that road.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/167.mp3"),

                new DialogueLine(Speaker.LEFT, "You don't know that.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/168.mp3"),

                new DialogueLine(Speaker.RIGHT, "You already know it. You always have. That is why you keep opening each door. Not because you believe you will find one that will change anything, but because closing the last door would mean accepting there are no more questions left to ask.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/169.mp3"),

                new DialogueLine(Speaker.LEFT, "Is there anything wrong with asking questions?",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/170.mp3"),

                new DialogueLine(Speaker.RIGHT, "None at all. Nothing… until the questions themselves begin to bind you. Until you spend nights sitting at your desk in odd hours not for academic purposes, but in attempting to outrun the answer.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/171.mp3"),

                new DialogueLine(Speaker.LEFT, "What's the answer?",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/172.mp3"),

                new DialogueLine(Speaker.RIGHT, "That your parents are dead. Nothing you do will ever bring them back. No amount of bargaining will undo what has already been done. There's no escaping the inevitable.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/173.mp3"),

                new DialogueLine(Speaker.NARRATOR, "KAI stands very still.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/174.mp3"),

                new DialogueLine(Speaker.RIGHT, "The temple is not a domain of countless possibilities. You waltz through its doors not for the sake of searching for answers, but to avoid the moment when there are no more doors left to open.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/175.mp3"),

                new DialogueLine(Speaker.LEFT, "What exactly are you telling me? To just accept it?",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/176.mp3"),

                new DialogueLine(Speaker.RIGHT, "Acceptance does not equate to giving up. Acceptance is the acknowledgment that the battle has concluded, where something lost cannot be brought back. And you must continue living anyway.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/177.mp3"),

                new DialogueLine(Speaker.LEFT, "That sounds like giving up, don't you think?",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/178.mp3"),

                new DialogueLine(Speaker.RIGHT, "That is because you have spent months confusing stillness with progress. You have been moving, but you have not gone anywhere.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/179.mp3"),

                new DialogueLine(Speaker.NARRATOR, "KAI wants to argue. She opens her mouth. Nothing comes out.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/180.mp3"),

                new DialogueLine(Speaker.RIGHT, "The forest froze you. The city woke your anger. The temple showed you the sound of your hopes. Each of these has been necessary. But none of them is the end.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/181.mp3"),

                new DialogueLine(Speaker.LEFT, "The end?",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/182.mp3"),

                new DialogueLine(Speaker.NARRATOR, "She looks up.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/183.mp3"),

                new DialogueLine(Speaker.NARRATOR, "The ocean is ahead. Still. No waves. No shore on the other side. Just water stretching into nothing.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/184.mp3"),

                new DialogueLine(Speaker.RIGHT, "That is where the bargaining ends. That is where you stop asking 'what if' and start dealing with what it is.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/185.mp3"),

                new DialogueLine(Speaker.LEFT, "The ocean.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/186.mp3"),

                new DialogueLine(Speaker.RIGHT, "Grief. Not the idea of grief. Not the performance of grief. Not the grief you shove down or bargain with or hide from. Just grief, heavy and unmoving.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/187.mp3"),

                new DialogueLine(Speaker.NARRATOR, "KAI stares at the water.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/188.mp3"),

                new DialogueLine(Speaker.LEFT, "It doesn't look like anything.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/189.mp3"),

                new DialogueLine(Speaker.RIGHT, "That is because you are still standing on the shore.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/190.mp3"),

                new DialogueLine(Speaker.NARRATOR, "The voice pauses.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/191.mp3"),

                new DialogueLine(Speaker.RIGHT, "You do not have to swim. You only have to go in.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/192.mp3"),

                new DialogueLine(Speaker.RIGHT, "And then you do not drown. That is the fear. That if you allow yourself to feel it, you will never come back up to the surface. But you will not drown. You will simply... be in it. Eventually, you will learn to exist in that vast space.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/193.mp3"),

                new DialogueLine(Speaker.NARRATOR, "KAI looks at the ocean. Then back at the temple. The yellow light is almost gone now. Just a faint glow in the distance.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/194.mp3"),

                new DialogueLine(Speaker.LEFT, "What happens if I don't go in?",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/195.mp3"),

                new DialogueLine(Speaker.RIGHT, "Then you stay here. On the gray ground. Between doors. Between places. Never moving forward. Never moving back. Just standing.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/196.mp3"),

                new DialogueLine(Speaker.LEFT, "For how long?",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/197.mp3"),

                new DialogueLine(Speaker.RIGHT, "Until you are ready.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/198.mp3"),

                new DialogueLine(Speaker.LEFT, "What about my grandmother?",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/199.mp3"),

                new DialogueLine(Speaker.RIGHT, "She would still be waiting for you to wake up.",
                        "/com/example/kaiajourneythroughgrief/assets/dialogue/200.mp3")
        );
    }
}