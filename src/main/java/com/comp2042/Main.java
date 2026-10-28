package com.comp2042;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static ViewManager viewManager;

    @Override
    public void start(Stage primaryStage) throws IOException {
        viewManager = new ViewManager(primaryStage);
        viewManager.initializeMainMenu();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static ViewManager getViewManager() {
        return viewManager;
    }
}
