package ru.hse.homework.fifth;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class TetrisApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(TetrisApplication.class.getResource("tetris-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 620, 400);
        stage.setResizable(false);
        stage.setTitle("HW5");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}