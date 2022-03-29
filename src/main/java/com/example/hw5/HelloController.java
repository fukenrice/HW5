package com.example.hw5;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private Text timerView;

    @FXML
    private GridPane figureTemplate;

    @FXML
    public GridPane field;

    private Timer timer = new Timer();
    private boolean timerOn = false;

    @FXML
    protected void onStop() {
        if (timerOn) {
            timer.purge();
            timer.cancel();
        }
    }

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

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
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

    int[][] currentFigure;

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

    private double mouseAnchorX;
    private double mouseAnchorY;


    private Pair<Integer, Integer> getCellIndexByCoordinates(double xCord, double yCord) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (field.getCellBounds(i, j).contains(xCord, yCord)) {
                    return new Pair<>(i, j);
                }
            }
        }
        return null;
    }


    int fieldModel[][] = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
    };

    void clearField() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                fieldModel[i][j] = 0;
            }
        }
    }

    void drawField() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {

                StackPane nd = (StackPane) (field.getChildren().get(i * 9 + j));
                if (fieldModel[i][j] == 1) {
                    nd.setStyle("-fx-background-color:GREEN");
                } else {
                    nd.setStyle("-fx-background-color: transparent");
                }
            }
        }
    }

    @FXML
    protected void onStart() {
    clearField();
    drawField();
        figureTemplate.setOnMousePressed(mouseEvent -> {
            mouseAnchorX = mouseEvent.getX();
            mouseAnchorY = mouseEvent.getY();
        });

        figureTemplate.setOnMouseDragged(mouseEvent -> {
            figureTemplate.setLayoutX(mouseEvent.getSceneX() - mouseAnchorX);
            figureTemplate.setLayoutY(mouseEvent.getSceneY() - mouseAnchorY);
        });

        figureTemplate.setOnMouseReleased(mouseDragEvent -> {
            // В этом методе определяем координаты закрашенных кусков, если каждый закрашенный кусок в таблице и каждый кусок таблицы под ним не закрашен, тогда перекрашиваем соответствующие куски таблицы.
            double xCord = figureTemplate.getLayoutX();
            double yCord = figureTemplate.getLayoutY();
            //System.out.println("123");
            Pair<Integer, Integer> coordsPlaced = getCellIndexByCoordinates(xCord, yCord);

            boolean flag = true;

            // Проверяем, поставили ли фигуру на поле.
            if (coordsPlaced != null) {
                // Проверяем, помещается ли фигура вообще.
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (currentFigure[i][j] == 1 && coordsPlaced.getKey() + i > 8 ||
                                currentFigure[i][j] == 1 && coordsPlaced.getValue() + j > 8 ||
                                currentFigure[i][j] == 1 &&
                                        fieldModel[i + coordsPlaced.getValue()][j + coordsPlaced.getKey()] == 1) {
                            flag = false;
                            break;
                        }
                    }
                }
                if (flag) {
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            if (currentFigure[i][j] == 1) {
                                fieldModel[i + coordsPlaced.getValue()][j + coordsPlaced.getKey()] = 1;
                            }
                        }
                    }
                }
            }
            figureTemplate.setLayoutX(431);
            figureTemplate.setLayoutY(260);
            if (flag) {
                drawField();
                drawFigure();
            }
        });

//        ((StackPane) (field.getChildren().get(1 * 9 + 1))).setStyle("-fx-background-color:GREEN");
//        ((StackPane) (field.getChildren().get(1 * 9 + 2))).setStyle("-fx-background-color:GREEN");
//        ((StackPane) (field.getChildren().get(2 * 9 + 1))).setStyle("-fx-background-color:GREEN");
//        ((StackPane) (field.getChildren().get(3 * 9 + 1))).setStyle("-fx-background-color:GREEN");
        System.out.println(field.getChildren().size());
        startTimer();
        clearTemplate();
        drawFigure();
        // new DraggableMaker().makeDraggable(figureTemplate);
//        System.out.println(field.getCellBounds(0,0));
//        System.out.println(field.getCellBounds(0,1));
//        System.out.println(field.getCellBounds(0,2));
//        ((StackPane)field.getChildren().get(0 + 2)).setStyle("-fx-background-color:GREEN");
    }
}
