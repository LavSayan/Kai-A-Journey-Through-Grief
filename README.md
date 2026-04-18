# Kai - A Journey Through Grief

A contemplative interactive narrative experience built with Java and JavaFX. Guide Kai through five profound stages of the grief journey with immersive dialogue, atmospheric music, and thoughtful storytelling.

## Overview

**Kai - A Journey Through Grief** is an interactive game that explores the emotional landscape of loss through a structured narrative. Players progress through multiple stages, each representing different aspects of the grieving process, enhanced by dialogue interactions, environmental storytelling, and an musical score.

### Key Features

- **5 Distinctive Stages**: Each stage represents a different phase of the emotional journey
- **8 Interactive Dialogues**: Deeply written conversations that advance the narrative
- **Music**: Full background score that complements each stage
- **Save/Load System**: Resume your journey at any time
- **UI**: JavaFX interface with animations and visual effects

### System Architecture

```
Core Components:
├── Main.java              → Application entry point
├── Menu.java              → Main menu interface
├── LevelScreen.java       → Level/stage selection screen
├── DialogueScreen.java    → Interactive dialogue system
├── SaveManager.java       → Game progress persistence
├── MusicPlayer.java       → Audio playback management
│
├── Stages:
│   ├── StageOne.java      → First stage of journey
│   ├── StageTwo.java      → Second stage of journey
│   ├── StageThree.java    → Third stage of journey
│   ├── StageFour.java     → Fourth stage of journey
│   └── StageFive.java     → Fifth stage of journey
│
├── Dialogues:
│   ├── Dialogue1.java
│   ├── Dialogue2.java
│   ├── ... through Dialogue8.java
│
└── Configuration:
    ├── DialogueConfig.java → Dialogue system configuration
    └── Assets             → Images, music, and layouts
```

### Technologies

- **Language**: Java 21
- **GUI Framework**: JavaFX 21.0.6
- **Build Tool**: Maven
- **Data Format**: JSON (Jackson library for parsing)
- **Audio**: JavaFX Media API

## Running the Application

### Quick Start

**Double-click `Launcher.bat`** in the project folder to start the game immediately.

### Alternative Methods

1. **Using PowerShell**:
   ```powershell
   .\RUN.ps1
   ```

2. **Using Maven** (for development):
   ```bash
   mvn javafx:run
   ```

3. **Manual Java Command** (after building):
   ```bash
   java --module-path "path\to\javafx\modules" --add-modules javafx.controls,javafx.fxml,javafx.media -jar target/Kai-A-Journey-Through-Grief-1.0-SNAPSHOT.jar
   ```

## Requirements

- **Java 21** (JDK or Runtime Environment)
- **JavaFX 21.0.6** (included via Maven dependencies)
- **Windows** (Launcher.bat is Windows-specific; use Maven for cross-platform)

The application is configured to automatically handle all dependencies during build time.

## Building from Source

### Prerequisites

- Java 21 installed
- Maven installed (or use the included Maven wrapper `mvnw.cmd`)

### Build Steps

1. Open PowerShell and navigate to the project directory:
   ```powershell
   cd "C:\Users\brizu\IdeaProjects\Kai-A-Journey-Through-Grief-copy"
   ```

2. Build the project with Maven:
   ```bash
   $env:JAVA_HOME="C:\Users\brizu\.jdk\jdk-21.0.8"
   .\mvnw.cmd clean package
   ```

3. The JAR file will be created in the `target/` folder:
   ```
   target/Kai-A-Journey-Through-Grief-1.0-SNAPSHOT.jar
   ```

## Game Structure

### Menu System
- **Main Menu**: Start new game, continue existing game, or exit
- **Level Selection**: Choose which stage to experience
- **Settings**: Music volume control

### Gameplay Flow

1. **Menu** → Choose to continue or start new game
2. **Level Screen** → Select which of 5 stages to play
3. **Stage Screen** → Experience environmental storytelling and puzzles
4. **Dialogue Screen** → Engage in meaningful conversations
5. **Save/Load** → Progress is automatically saved

### Save System

The game includes a persistent save system that tracks:
- Current stage progress
- Dialogue choices made
- Time spent in each stage
- Music preferences

Save data is stored locally and can be resumed at any time.

## Project Statistics

- **Source Files**: 15+ Java classes
- **Lines of Code**: ~3,000+
- **Build Artifacts**: 85 MB packaged JAR (with all dependencies)
- **Audio Assets**: Full background music soundtrack
- **Visual Assets**: Stage-specific imagery and dialogue screens

## Development

### Directory Structure

```
src/
├── main/
│   ├── java/
│   │   ├── module-info.java          (Module configuration for Java 21)
│   │   └── com/example/kaiajourneythroughgrief/
│   │       ├── *.java                (20+ Java classes)
│   │       └── dialogues/            (8 dialogue classes)
│   └── resources/
│       ├── *.fxml                    (7 UI layout files)
│       └── assets/
│           ├── bgmusic.mp3           (Background music)
│           ├── dialogue/             (Dialogue audio)
│           ├── dialogueScreen/       (UI assets for 8 dialogues)
│           └── stage[1-5]/           (Stage-specific imagery)
```

## Troubleshooting

### "Cannot find java command"
Ensure Java 21 is installed. The application looks for Java 21 at:
```
C:\Users\brizu\.jdk\jdk-21.0.8
```

### "JavaFX modules not found"
The Maven build automatically downloads JavaFX dependencies. Make sure:
1. You have an internet connection during first build
2. Maven cache (`~/.m2/repository`) has sufficient space
3. Run `mvn clean package` to rebuild

### "No sound/music playing"
Check your system volume settings. The game uses JavaFX MediaPlayer for audio playback.

## Credits

- **Created with**: Java 21, JavaFX 21.0.6, Maven
- **Story & Design**: Brizuela, Zyann Miguel; Dela Calzada, Shaine Kate; Lee, Jerom
- **Audio**: Dela Calzada, Shaine Kate; Lee, Jerom
- **UI Framework**: JavaFX (Oracle)

## Support

For issues or questions, please check the following:
1. Ensure Java 21 is installed: `java -version`
2. Verify JavaFX is properly configured (should work automatically with Maven)
3. Check that all files exist in the `src/resources/assets/` directory

---

**Enjoy Kai's journey and take time to reflect on the emotions it evokes.** 
