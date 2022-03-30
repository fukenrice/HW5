package com.example.hw5;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class HelloController {
    @FXML
    private Text timerView;

    @FXML
    private GridPane figureTemplate;

    @FXML
    public GridPane field;

    private Timer timer = new Timer();

    private boolean timerOn = false;

    int[][] currentFigure;

    private double mouseAnchorX;

    private double mouseAnchorY;



    void startTimer() {
        final int[] time = {-1};
        if (timerOn) {
            timer.purge();
            timer.cancel();
            timer = new Timer();
        }

        TimerTask myTimerTask = new TimerTask() {
            @Override
            public void run() {
                time[0]++;
                timerView.setText(String.format("%02d:%02d", time[0] / 60, time[0] % 60));
            }
        };
        timer.scheduleAtFixedRate(myTimerTask, 0, 1000);
        timerOn = true;
    }

    void clearTemplate() {
        for (int i = 0; i < 9; i++) {
            StackPane nd = (StackPane) (figureTemplate.getChildren().get(i));
            nd.setStyle("-fx-background-color: transparent");
        }
    }

    int[][] getFigure() {
        Random r = new Random();
        int index = r.nextInt(FigureModels.figures.length);
        return FigureModels.figures[index];
    }



    void drawFigure() {
        clearTemplate();
        currentFigure = getFigure();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                StackPane nd = (StackPane) (figureTemplate.getChildren().get(i * 3 + j));
                if (currentFigure[i][j] == 1) {
                    nd.setStyle("-fx-background-color:GREEN");
                }
            }
        }
    }



    private Pair<Integer, Integer> getCellIndexByCoordinates(double xCord, double yCord) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                double fx = field.getLayoutX();
                double fy = field.getLayoutY();
                if (field.getCellBounds(i, j).getMinX() + fx <= xCord && field.getCellBounds(i, j).getMaxX() + fx > xCord &&
                        field.getCellBounds(i, j).getMinY() + fy <= yCord && field.getCellBounds(i, j).getMaxY() + fy > yCord) {
                    return new Pair<>(i, j);
                }
            }
        }
        return null;
    }

    void clearField() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                FigureModels.fieldModel[i][j] = 0;
            }
        }
    }

    void drawField() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {

                StackPane nd = (StackPane) (field.getChildren().get(i * 9 + j));
                if (FigureModels.fieldModel[i][j] == 1) {
                    nd.setStyle("-fx-background-color:GREEN");
                } else {
                    nd.setStyle("-fx-background-color: transparent");
                }
            }
        }
    }

    boolean rowContains(int[][] arr, int value, int row) {
        for (int i = 0; i < arr[row].length; i++) {
            if (arr[row][i] == value) {
                return true;
            }
        }
        return false;
    }

    boolean colContains(int[][] arr, int value, int col) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i][col] == value) {
                return true;
            }
        }
        return false;
    }

    private final EventHandler<MouseEvent> mousePressed = mouseEvent -> {
        mouseAnchorX = mouseEvent.getX();
        mouseAnchorY = mouseEvent.getY();
    };

    private final EventHandler<MouseEvent> mouseDragged = mouseEvent -> {
        figureTemplate.setLayoutX(mouseEvent.getSceneX() - mouseAnchorX);
        figureTemplate.setLayoutY(mouseEvent.getSceneY() - mouseAnchorY);
    };

    private final EventHandler<MouseEvent> mouseReleased = mouseDragEvent -> {
        // В этом методе определяем координаты закрашенных кусков, если каждый закрашенный кусок в таблице и каждый кусок таблицы под ним не закрашен, тогда перекрашиваем соответствующие куски таблицы.
        double xCord = figureTemplate.getLayoutX() + figureTemplate.getWidth() / 2;
        double yCord = figureTemplate.getLayoutY() + figureTemplate.getHeight() / 2;
        // Получаем координаты ценентра рабочей фигуры.
        Pair<Integer, Integer> coordsPlaced = getCellIndexByCoordinates(xCord, yCord);
        boolean flag = true;
        boolean changed = false;
        // Проверяем, поставили ли фигуру на поле.
        if (coordsPlaced != null) {
            // Проверяем, не заходит ли что-то за поля.
            if (coordsPlaced.getValue() == 0 && rowContains(currentFigure, 1, 0)) {
                flag = false;
            }
            if (coordsPlaced.getKey() == 0 && colContains(currentFigure, 1, 0)) {
                flag = false;
            }
            if (flag) {
                // Проверяем, помещается ли фигура вообще.
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (currentFigure[i][j] == 1 && coordsPlaced.getValue() + i - 1 > 8 ||
                                currentFigure[i][j] == 1 && coordsPlaced.getKey() + j - 1 > 8 ||
                                currentFigure[i][j] == 1 && FigureModels.fieldModel[i + coordsPlaced.getValue() - 1][j + coordsPlaced.getKey() - 1] == 1) {
                            flag = false;
                            break;
                        }
                    }
                }
                // Если все ок, вносим изменения в модель.
                if (flag) {
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            if (currentFigure[i][j] == 1) {
                                FigureModels.fieldModel[i + coordsPlaced.getValue() - 1][j + coordsPlaced.getKey() - 1] = 1;
                            }
                        }
                    }
                    changed = true;
                }
            }
        }
        figureTemplate.setLayoutX(431);
        figureTemplate.setLayoutY(260);
        if (changed) {
            drawField();
            drawFigure();
        }
    };


    @FXML
    protected void onStop() {
        if (timerOn) {
            timer.purge();
            timer.cancel();
        }
        figureTemplate.setOnMousePressed(null);
        figureTemplate.setOnMouseDragged(null);
        figureTemplate.setOnMouseReleased(null);
    }

    @FXML
    protected void onStart() {
        startTimer();
        clearField();
        drawField();
        drawFigure();
        figureTemplate.setOnMousePressed(mousePressed);
        figureTemplate.setOnMouseDragged(mouseDragged);
        figureTemplate.setOnMouseReleased(mouseReleased);
    }
}
