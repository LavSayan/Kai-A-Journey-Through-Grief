# Kai — A Journey Through Grief

A five-stage sorting-battle game built in JavaFX. Kai walks the five stages of
grief — Denial, Anger, Bargaining, Depression, Acceptance — and each one is a
different twist on the same puzzle: order a row of tiles by a value you cannot
always see, while your vitality drains away second by second.

> *Five stages. Five truths. One way through.*

---

## Requirements

- **JDK 21 or newer** (the project targets Java 21; JDK 24 is known to work).
  Check with `java -version`.
- A **desktop display**. This is a graphical JavaFX application — it will not
  run over a plain SSH session or on a headless server.
- Nothing else. Maven is **not** required to be installed; the project ships the
  Maven Wrapper (`mvnw` / `mvnw.cmd`), which downloads the right version itself.

The first build downloads roughly 85 MB of Maven, JavaFX and Jackson into your
`~/.m2/repository`. That happens once; later builds are fast and work offline.

---

## Install and run

Pick whichever suits you. Both paths were verified working on Windows with
JDK 24.

### Option A — build once, then just play (recommended)

```bash
./mvnw package
java -jar target/Kai-A-Journey-Through-Grief-1.0-SNAPSHOT.jar
```

On Windows use `mvnw.cmd` in place of `./mvnw`:

```bat
mvnw.cmd package
java -jar target\Kai-A-Journey-Through-Grief-1.0-SNAPSHOT.jar
```

`package` produces a single self-contained ~81 MB jar with JavaFX bundled in, so
you can copy that one file anywhere with a JRE installed and play. There is no
`install` step — the jar *is* the game.

`package` runs the test suite as part of the build. To build without testing
(slightly faster), use `./mvnw package -DskipTests`, or run the tests on their
own with `./mvnw test`.

### Option B — run straight from source

```bash
./mvnw javafx:run
```

This compiles and launches without producing a jar. It is the better loop while
you are changing things, because it is faster to iterate.

---

## How to play

### Getting around

| Screen | What to do |
|---|---|
| **Menu** | `BEGIN JOURNEY` starts a new game (it warns you first if you already have progress). `CONTINUE` is dimmed until you have something to continue. `QUIT` exits. The speaker button in the bottom-right opens the master-volume dialog. |
| **Level map** | Click a stage node. Locked stages open a sealed card; unlocked ones open their intro card; completed ones open a completion card you can replay. |
| **Dialogue** | Click the dialogue box at the bottom to advance. Each line plays its own audio and fades the text in. |
| **Battle** | **Drag a tile onto another tile to swap them.** `↺ Restart`, `← Back` and `✕ Leave` sit in the title bar. |

### The battle loop

Each stage asks you to sort **two arrays** to win. For each one:

- Your **vitality starts at 50 %** and **drains continuously at 4 % per second**.
  Standing still is losing.
- Drag a tile onto another to swap them. A swap counts as **good only if it
  strictly reduces the number of inversions** in the array — that is, it must
  move the row measurably closer to sorted order. Equal-value swaps never count.
- A **good swap pays +8 % vitality per tile that lands in its correct final
  position** — 0 %, 8 % or 16 % depending on how cleanly you placed it.
- A **wrong swap costs 5 %** of vitality. In **Stage 2 it costs 10 %**.
- Hit **0 %** and the enemy wins.
- Tiles that already sit in their correct position turn green with a tick, so you
  can see your progress at a glance. The objective line in the top-left of the
  board tells you the direction to sort, and the top-right shows which array you
  are on.

### The five stages

| # | Stage | Enemy | Tiles | The twist |
|---|---|---|---|---|
| I | The Forest Frozen in Fog | Ouroboro | 8 numbers | Sort ascending. The gentlest stage — this is the tutorial. |
| II | The City of Shattered Glass | Shard | 8 numbers | The sort direction is **randomised every array**, so read the objective line each time. Wrong swaps cost double. |
| III | A Temple of Endless Doors | The Door | 8 expressions | Tiles show maths like `5+3`, `12−4`, `3×6`. **Evaluate each one yourself** and sort by the hidden result. The answer is revealed when a tile lands correctly. |
| IV | An Ocean of Still Water | The Mermaid | 8 equations | Tiles show equations like `x + 6 = 11`. **Solve for x** and sort by the hidden solution; `x = ?` becomes `x = 5 ✓` once it is placed. |
| V | A Garden at Dawn | The Eclipse | 10 numbers | Ten tiles, random sort direction, and **4 tiles start hidden behind `?`**. A hidden tile reveals itself when it lands in the right place — or you can bank **3 correct placements** to force one open. |

Beating Stage V plays the ending and returns you to the menu.

---

## Where your progress is saved

Progress is stored in the operating system's preference store, **not** in a file
next to the game:

- **Windows** — registry key
  `HKEY_CURRENT_USER\Software\JavaSoft\Prefs\com\example\kaiajourneythroughgrief`
- **macOS / Linux** — `~/.java/.userPrefs/com/example/kaiajourneythroughgrief`

Each stage is recorded as `locked`, `unlocked` or `complete`; clearing a stage
unlocks the next one. The game has no save-file to delete and nothing to carry
between machines.

To wipe your progress, use **BEGIN JOURNEY → START FRESH** in the menu.

---

## Troubleshooting

**`Error: JavaFX runtime components are missing, and are required to run this application`**
You are running the project classes directly with `java -cp`, which does not put
JavaFX on the module path. Use one of the two supported commands above instead —
`java -jar` (Option A) or `./mvnw javafx:run` (Option B).

**`invalid target release: 21` or `release version 21 not supported`**
Your `JAVA_HOME` points at a JDK older than 21. Install JDK 21+ and either set
`JAVA_HOME` to it or put its `bin` directory first on your `PATH`. Verify with
`java -version`.

**The first build hangs or fails to download**
The first run fetches JavaFX and Jackson from Maven Central. Check your
network/proxy. Maven Wrapper also downloads Maven itself on first use. Once that
has succeeded you can build offline with `./mvnw -o package`.

**A wall of `WARNING:` lines on startup**
On JDK 24 these come from JavaFX 17 and the Marlin renderer and are harmless —
the game starts normally. Running the packaged jar you will see three kinds of
warning: `A restricted method in java.lang.System has been called`,
`A terminally deprecated method in sun.misc.Unsafe has been called`, and
`Unsupported JavaFX configuration`. The first is silenced by:

```bash
java --enable-native-access=ALL-UNNAMED -jar target/Kai-A-Journey-Through-Grief-1.0-SNAPSHOT.jar
```

That flag does not remove the other two — they come from JavaFX 17 running on the
classpath and from its internal renderer. They are cosmetic and go away by
upgrading JavaFX. None of them appear when you launch with `./mvnw javafx:run`.

**No sound**
Check the volume slider in the menu's settings dialog. `CONTINUE` skips the
intro dialogue by design, so if you want to hear the opening audio, start a new
journey instead.

**My progress disappeared**
Reinstalling the JRE, switching between a `java -jar` run and an IDE run, or
switching Java versions can change which preference store is used. Clear it by
choosing **BEGIN JOURNEY → START FRESH**.

**The window is the wrong size or clips**
The game fixes its window at 1000×740 and disables resizing. On a display
scaled above 100 % or below the window's minimum, shrink your desktop scaling or
run on a larger monitor.

---

## Project layout

```
src/main/java/com/example/kaiajourneythroughgrief/
├── Main.java              JavaFX Application, opens the menu
├── Launcher.java          jar entry point (see Troubleshooting note above)
├── Menu.java              title screen
├── LevelScreen.java       the stage map
├── DialogueScreen.java    cutscene player
├── SaveManager.java       registry-backed progress
├── MusicPlayer.java       singleton background music
├── BattleScene.java       builds a battle scene for any stage
├── battle/                the sorting-battle engine
│   ├── BattleController   one controller, shared by all five stages
│   ├── StageConfig        everything that differs between stages
│   ├── model/             Board, BoardLayout, HealthModel, LockedTiles (no JavaFX)
│   ├── gen/               tile generators: numbers, expressions, equations
│   ├── view/              painters and scene chrome
│   └── nav/               scene transitions
├── stages/StageCatalog    the five stage definitions
└── dialogues/             Dialogue1–8 scripts
```

All five stages share one controller and one `battle.fxml`; a stage supplies only
its data. See `stages/StageCatalog.java` to change anything about a stage, and
`battle/Theme.java` for colours, fonts and balance numbers.
