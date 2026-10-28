package com.comp2042;

import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class Main extends Application {

    /** The pixel fonts that styling.css refers to by family name. */
    private static final String[] FONTS = {
            "/resources/views/res/SFPixelate.ttf",
            "/resources/views/res/SFPixelateShaded-Bold.ttf"
    };

    private static ViewManager viewManager;

    @Override
    public void start(Stage primaryStage) throws IOException {
        loadFonts();
        viewManager = new ViewManager(primaryStage);
        viewManager.initializeMainMenu();
    }

    /**
     * Registers the pixel fonts before any styled control is shown. They are read from the
     * class path as streams, which works from any folder and from the jar; the CSS
     * {@code @font-face} rule used before failed whenever the folder name contained a space.
     * A font that cannot be loaded only produces a warning, and the default font is used.
     */
    private static void loadFonts() {
        for (String path : FONTS) {
            try (InputStream in = Main.class.getResourceAsStream(path)) {
                if (in == null || Font.loadFont(in, 12) == null) {
                    System.err.println("Could not load font " + path);
                }
            } catch (IOException e) {
                System.err.println("Could not load font " + path + ": " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static ViewManager getViewManager() {
        return viewManager;
    }
}
