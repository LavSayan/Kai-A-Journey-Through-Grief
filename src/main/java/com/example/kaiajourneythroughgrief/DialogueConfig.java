package com.example.kaiajourneythroughgrief;

import java.util.List;

public class DialogueConfig {

    private final String sceneName;
    private final String backgroundImagePath;
    private final String backgroundMusicPath;
    private final double backgroundMusicVolume;
    private final String leftCharacterName;
    private final String leftCharacterImagePath;
    private final String rightCharacterName;
    private final String rightCharacterImagePath;
    private final List<DialogueLine> dialogueLines;

    public DialogueConfig(
            String sceneName,
            String backgroundImagePath,
            String backgroundMusicPath,
            double backgroundMusicVolume,
            String leftCharacterName,
            String leftCharacterImagePath,
            String rightCharacterName,
            String rightCharacterImagePath,
            List<DialogueLine> dialogueLines) {
        this.sceneName = sceneName;
        this.backgroundImagePath = backgroundImagePath;
        this.backgroundMusicPath = backgroundMusicPath;
        this.backgroundMusicVolume = backgroundMusicVolume;
        this.leftCharacterName = leftCharacterName;
        this.leftCharacterImagePath = leftCharacterImagePath;
        this.rightCharacterName = rightCharacterName;
        this.rightCharacterImagePath = rightCharacterImagePath;
        this.dialogueLines = dialogueLines;
    }

    // Getters
    public String getSceneName() {
        return sceneName;
    }

    public String getBackgroundImagePath() {
        return backgroundImagePath;
    }

    public String getBackgroundMusicPath() {
        return backgroundMusicPath;
    }

    public double getBackgroundMusicVolume() {
        return backgroundMusicVolume;
    }

    public String getLeftCharacterName() {
        return leftCharacterName;
    }

    public String getLeftCharacterImagePath() {
        return leftCharacterImagePath;
    }

    public String getRightCharacterName() {
        return rightCharacterName;
    }

    public String getRightCharacterImagePath() {
        return rightCharacterImagePath;
    }

    public List<DialogueLine> getDialogueLines() {
        return dialogueLines;
    }

    /**
     * Inner class for individual dialogue lines.
     * Can be used across all DialogueConfig instances.
     */
    public static class DialogueLine {
        public enum Speaker {
            LEFT, RIGHT, NARRATOR, NONE
        }

        private final Speaker speaker;
        private final String text;
        private final String audioPath;

        public DialogueLine(Speaker speaker, String text) {
            this(speaker, text, null);
        }

        public DialogueLine(Speaker speaker, String text, String audioPath) {
            this.speaker = speaker;
            this.text = text;
            this.audioPath = audioPath;
        }

        public Speaker getSpeaker() {
            return speaker;
        }

        public String getText() {
            return text;
        }

        public String getAudioPath() {
            return audioPath;
        }
    }
}