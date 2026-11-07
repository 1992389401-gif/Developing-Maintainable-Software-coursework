# Snake – COMP2042 Developing Maintainable Software Coursework

[![Build and test](https://github.com/1992389401-gif/Developing-Maintainable-Software-coursework/actions/workflows/build.yml/badge.svg?branch=develop)](https://github.com/1992389401-gif/Developing-Maintainable-Software-coursework/actions/workflows/build.yml)

Maintenance and extension of the JavaFX Snake game from [kooitt/CW2026](https://github.com/kooitt/CW2026).

> Work in progress: this README is updated as the project goes on.

## GitHub

https://github.com/1992389401-gif/Developing-Maintainable-Software-coursework

- `develop` – the latest work (default branch)
- `master` – the original code; the final version is merged into it at the end
- `fix/*`, `refactor/*`, `build/*`, `chore/*` – one branch per piece of work, merged into `develop` with `--no-ff`

## Compilation Instructions

**Requirements**
- JDK 25 (JavaFX 25 needs JDK 23 or later). Set `JAVA_HOME` to it, or choose it as the project SDK in IntelliJ IDEA.
- Nothing else: the Maven Wrapper (`mvnw` / `mvnw.cmd`) downloads Maven 3.9.16 and every dependency, including JavaFX 25.0.1.

**Command line** (Windows: `mvnw.cmd`, macOS/Linux: `./mvnw`)

| Task | Command |
|---|---|
| Run the game | `mvnw.cmd javafx:run` |
| Run the unit tests | `mvnw.cmd test` |
| Build everything and run the tests | `mvnw.cmd clean verify` |
| Run the packaged game (after `verify`) | `java -jar target/snake-javafx.jar` |

The packaged jar contains the JavaFX libraries of the system it was built on, so build it on the system that will run it.

**IntelliJ IDEA**
1. Open the project folder; IntelliJ imports it as a Maven project.
2. File → Project Structure → Project → SDK: choose a JDK 25.
3. Run `com.comp2042.app.Launcher`, or in the Maven tool window run Plugins → javafx → `javafx:run`. Running `Main` directly fails with "JavaFX runtime components are missing"; `Launcher` avoids that.
4. Run the tests: right-click `src/test/java` → Run 'All Tests'.

## Implemented and Working Properly

So far the work has been maintenance and refactoring; new features come next.
- **Bug fixes**: 16 bugs of the original game are fixed (B1–B16), each with a regression test or a before-and-after check. Examples: pressing DOWN while moving UP reversed the snake; the direction leaked from one game into the next through static fields; the rainbow coin could never appear; obstacles could be placed off the board or right in front of the snake; the game froze when the board was full; the "+" key for full screen did not work on most keyboards; the coin sound never played.
- **Structure**: classes are organised into `app`, `model`, `view` and `controller` packages (Model-View-Controller). The model does not depend on JavaFX, and an architecture test checks this on every build.
- **Build and tests**: the project builds with the Maven Wrapper on Java 25, the packaged jar starts, and 116 JUnit 5 tests run automatically on GitHub Actions for every push.

## Implemented but Not Working Properly

Nothing known at the moment.

## Features Not Implemented

To be completed when the feature work is finished.

## New Java Classes

| Class | Package | Purpose |
|---|---|---|
| `Launcher` | `app` | Plain entry point of the packaged jar, so that `java -jar` works |
| `Position` | `model` | Immutable board cell; replaces `java.awt.Point` |
| `Direction` | `model` | The four directions with their x/y step and their opposite |
| `Difficulty` | `model` | Speed and wall rule of each difficulty level |
| `CoinType` | `model` | The kinds of coin; the view chooses the sprite by type |
| `CoinFactory` | `model` | Chooses the next coin type (testable with a seeded `Random`) and creates the coin |
| `KeyBindings` | `controller` | Maps keys to directions and to the full-screen commands |

## Modified Java Classes

All classes moved from `com.comp2042` into the `app`, `model`, `view` and `controller` packages.

| Class | Changes and reasons |
|---|---|
| `Snake` | Direction enum instead of characters; keeps its heading and refuses 180° turns (B1); self-collision for every length (B10); dies when wrapping onto an obstacle (B15); uses `Position`; no images, so it can be unit tested |
| `Coin` and subclasses | Images moved to the view; `getType()`; placed on a random free cell instead of looping forever on a full board (B7) |
| `Obstacle` | Placed on free cells inside the board, never twice, with a safe zone in front of the snake (B6); colour moved to the view |
| `Game` | Correct win target with obstacles (B7) and after a restart (B16); `Difficulty` enum instead of a char; colour moved to the view |
| `GameController` | Direction per game instead of static fields (B3); game loop in `tick()`, stopped on a win (B8) and on ESC (B4); coin sound (B12); coin choice through `CoinFactory` (B2); keys through `KeyBindings` (B13) |
| `GameView` | Owns all sprites and reuses rotated ones; correct sprite size in full screen (B5); corner markers sized to the board and cleared on restart (B9); board and obstacle colours |
| `Main`, `ViewManager` | Uses the window JavaFX provides and reports menu loading errors (B11); loads the fonts from the class path (B14) |
| `MainMenuController` | Uses the `Difficulty` enum |

## Unexpected Problems

- **The Maven Wrapper could not download Maven**: its URL returned HTTP 404. It now points to Maven 3.9.16.
- **JavaFX 25 needs JDK 23 or later**, but the project was compiled for Java 21. It now targets Java 25 (LTS).
- **The packaged jar did not start** ("JavaFX runtime components are missing"). The jar now starts through the plain `Launcher` class.
- **The fonts failed to load when the project folder name contains a space**, because of the `%20` in the CSS `@font-face` URL. The fonts are now loaded from the class path at startup.
- **The model classes could not be unit tested**, because they created JavaFX images. The images moved to the view.
