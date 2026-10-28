package com.comp2042;

/**
 * Entry point of the packaged (shaded) jar.
 * <p>
 * When the class named in the jar manifest extends {@link javafx.application.Application}
 * and JavaFX sits on the class path rather than the module path, the {@code java} launcher
 * refuses to start with "JavaFX runtime components are missing". Starting through this
 * plain class skips that check, so {@code java -jar target/snake-javafx.jar} works.
 */
public final class Launcher {

    private Launcher() {
    }

    /**
     * Starts the game by delegating to {@link Main#main(String[])}.
     *
     * @param args command-line arguments, passed on unchanged
     */
    public static void main(String[] args) {
        Main.main(args);
    }
}
