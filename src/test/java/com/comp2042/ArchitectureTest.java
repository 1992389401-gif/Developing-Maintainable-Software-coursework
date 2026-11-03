package com.comp2042;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Checks the layer rules of the Model-View-Controller packages by reading the import statements
 * of the production code. The model must not use JavaFX, AWT or the other packages, so it can be
 * tested without a user interface, and the view must not depend on the controllers or the app.
 */
class ArchitectureTest {

    private static final Path SOURCES = Path.of("src", "main", "java", "com", "comp2042");

    @ParameterizedTest(name = "{0} must not import {1}")
    @CsvSource({
            "model, javafx.",
            "model, java.awt.",
            "model, com.comp2042.view.",
            "model, com.comp2042.controller.",
            "model, com.comp2042.app.",
            "view, com.comp2042.controller.",
            "view, com.comp2042.app."
    })
    void packageDoesNotDependOn(String layer, String forbidden) throws IOException {
        List<Path> files;
        try (Stream<Path> walk = Files.walk(SOURCES.resolve(layer))) {
            files = walk.filter(file -> file.toString().endsWith(".java")).toList();
        }
        assertFalse(files.isEmpty(), "no source files found in " + layer);

        List<String> offending = new ArrayList<>();
        for (Path file : files) {
            for (String line : Files.readAllLines(file)) {
                String trimmed = line.trim();
                if (trimmed.startsWith("import " + forbidden) || trimmed.startsWith("import static " + forbidden)) {
                    offending.add(file.getFileName() + ": " + trimmed);
                }
            }
        }
        assertEquals(List.of(), offending);
    }
}
