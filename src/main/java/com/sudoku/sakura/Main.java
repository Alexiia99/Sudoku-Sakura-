package com.sudoku.sakura;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.sudoku.sakura.utils.AudioManager;

public class Main extends Application {

    private static Stage primaryStage;
    private static final String TITLE = "🌸 Sudoku Sakura 🌸";
    private static final int WIDTH = 600;
    private static final int HEIGHT = 700;

    @Override
    public void start(Stage stage) {
        try {
            primaryStage = stage;
            loadScene("views/menu.fxml", WIDTH, HEIGHT);

            primaryStage.setTitle(TITLE);
            primaryStage.setResizable(false);
            primaryStage.show();

            AudioManager.getInstance().playBackgroundMusic();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadScene(String fxmlPath, int width, int height) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/" + fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root, width, height);

            String css = Main.class.getResource("/css/sakura-theme.css").toExternalForm();
            scene.getStylesheets().add(css);

            primaryStage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void stop() {
        AudioManager.getInstance().stopBackgroundMusic();
    }

    public static void main(String[] args) {
        launch(args);
    }
}