package com.comp2042;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class ViewManager {

    private static final String MAIN_MENU_FXML = "/resources/views/MainMenu.fxml";

    private final Stage primaryStage;
    private AnchorPane root;

    private Stage gameStage;

    private Image snakeLogo = new Image("resources/views/res/SnakeLogoWhite.png");

    /**
     * Creates the view manager for the window that JavaFX opened for the application.
     *
     * @param primaryStage the stage passed to {@link Main#start(Stage)}
     */
    public ViewManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Loads the main menu from its FXML file and shows it in the primary stage.
     *
     * @throws IOException if the FXML file is missing or cannot be read
     */
    public void initializeMainMenu() throws IOException {
        URL location = Main.class.getResource(MAIN_MENU_FXML);
        if (location == null) {
            throw new IOException("Main menu layout not found on the class path: " + MAIN_MENU_FXML);
        }
        root = new FXMLLoader(location).load();

        Scene mainScene = new Scene(root);

        primaryStage.setScene(mainScene);
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(snakeLogo);
        primaryStage.setTitle("Snake Game");
        primaryStage.show();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void initGameStage() {
        gameStage = new Stage();
        gameStage.setTitle("Snake Game");
        gameStage.getIcons().add(snakeLogo);
        gameStage.setResizable(false);
    }

    public Stage getGameStage() {
        return gameStage;
    }
}
