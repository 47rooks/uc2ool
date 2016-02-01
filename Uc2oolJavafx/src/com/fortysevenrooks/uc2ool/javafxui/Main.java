package com.fortysevenrooks.uc2ool.javafxui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Main application launch class. Does nothing but boot the JavaFX environment
 * and hence the application.
 * 
 * @author  Daniel Semler
 * @version %I%, %G%
 * @since   1.0
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            VBox page = (VBox) FXMLLoader.load(
                    Main.class.getResource("Uc2ool.fxml"));
            Scene scene = new Scene(page);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Unicode 2ool");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Application.launch(Main.class, (java.lang.String[])null);
    }
}
