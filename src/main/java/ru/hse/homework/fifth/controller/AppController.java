// Методы контроллера сделал публичными для того, что бы можно было провести тесты.

package ru.hse.homework.fifth.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Pair;
import ru.hse.homework.fifth.modeles.*;
import ru.hse.homework.fifth.modeles.FiguresModel;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class AppController {
    @FXML
    private Text timerView;

    @FXML
    private GridPane figureTemplate;

    @FXML
    private GridPane field;

    @FXML
    private Text steps;

    private Timer timer = new Timer();

    private boolean timerOn = false;

    private int[][] currentFigure;

    private double mouseAnchorX;

    private double mouseAnchorY;

    private FieldModel fieldModel;

    private int numOfSteps;

    private final EventHandler<MouseEvent> mousePressed = mouseEvent -> {
        mouseAnchorX = mouseEvent.getX();
        mouseAnchorY = mouseEvent.getY();
    };

    private final EventHandler<MouseEvent> mouseDragged = mouseEvent -> {
        figureTemplate.setLayoutX(mouseEvent.getSceneX() - mouseAnchorX);
        figureTemplate.setLayoutY(mouseEvent.getSceneY() - mouseAnchorY);
    };

    private final EventHandler<MouseEvent> mouseReleased = mouseDragEvent -> {
        // Получаем координаты ценентра рабочей фигуры.
        double xCord = figureTemplate.getLayoutX() + figureTemplate.getWidth() / 2;
        double yCord = figureTemplate.getLayoutY() + figureTemplate.getHeight() / 2;
        Pair<Integer, Integer> coordsPlaced = getCellIndexByCoordinates(xCord, yCord);

        // Проверяем, поставили ли фигуру на поле.
        boolean canPlace = canPlaceFigure(coordsPlaced, currentFigure, fieldModel.field);

        // Если все ок, вносим изменения в модель.
        if (canPlace) {
            placeFigure(coordsPlaced, currentFigure, fieldModel.field);
        }
        figureTemplate.setLayoutX(431);
        figureTemplate.setLayoutY(260);
        if (canPlace) {
            drawField();
            drawFigure();
            incrementSteps();
        }
    };

    /**
     * Метод для отслеживания времени игры, запускающий таймер.
     */
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

    /**
     * Метод для очистки поля с новой фигурой.
     */
    void clearTemplate() {
        for (int i = 0; i < FieldModel.FIELD_SIZE; i++) {
            StackPane nd = (StackPane) (figureTemplate.getChildren().get(i));
            nd.setStyle("-fx-background-color: transparent");
        }
    }

    /**
     * Метод возвращающий модель случайноый фигуры.
     * @return Модель случайной фигуры.
     */
    public int[][] getFigure() {
        Random r = new Random();
        int index = r.nextInt(FiguresModel.figures.length);
        return FiguresModel.figures[index];
    }

    /**
     * Метод для отрисовки фигуры во вью.
     */
    void drawFigure() {
        clearTemplate();
        currentFigure = getFigure();
        for (int i = 0; i < FieldModel.TEMPLATE_SIZE; i++) {
            for (int j = 0; j < FieldModel.TEMPLATE_SIZE; j++) {
                StackPane nd = (StackPane) (figureTemplate.getChildren().get(i * 3 + j));
                if (currentFigure[i][j] == 1) {
                    nd.setStyle("-fx-background-color:GREEN");
                }
            }
        }
    }

    /**
     * Метод, возвращающий индекс ячейки по родительским координатам во вью.
     * @param xCord x-координата.
     * @param yCord y-координата.
     * @return Индекс ячейки.
     */
    private Pair<Integer, Integer> getCellIndexByCoordinates(double xCord, double yCord) {
        for (int i = 0; i < FieldModel.FIELD_SIZE; i++) {
            for (int j = 0; j < FieldModel.FIELD_SIZE; j++) {
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

    /**
     * Метод, отрисовывающий поле по модели.
     */
    void drawField() {
        for (int i = 0; i < FieldModel.FIELD_SIZE; i++) {
            for (int j = 0; j < FieldModel.FIELD_SIZE; j++) {
                StackPane nd = (StackPane) (field.getChildren().get(i * 9 + j));
                if (fieldModel.field[i][j] == 1) {
                    nd.setStyle("-fx-background-color:GREEN");
                } else {
                    nd.setStyle("-fx-background-color: transparent");
                }
            }
        }
    }

    /**
     * Метод, проверяющий, содержит ли определенный ряд двумерного массива заданный элемент.
     * @param arr двумерный моссив.
     * @param value элемент.
     * @param row ряд.
     * @return содержит ли ряд элемент.
     */
    public boolean rowContains(int[][] arr, int value, int row) {
        for (int i = 0; i < arr[row].length; i++) {
            if (arr[row][i] == value) {
                return true;
            }
        }
        return false;
    }

    /**
     * Метод, проверяющий, содержит ли определенный столбец двумерного массива заданный элемент.
     * @param arr двумерный массив.
     * @param value элемент.
     * @param col столбец.
     * @return содержит ли столбец элемент.
     */
    public boolean colContains(int[][] arr, int value, int col) {
        for (int[] ints : arr) {
            if (ints[col] == value) {
                return true;
            }
        }
        return false;
    }

    /**
     * Метод, проверяющий, можно ли выставить фигуру на поле на заданную позицию(задается центром).
     * @param coordsPlaced координаты, куда будет поставлен центр фигуры.
     * @param figureTemplate модель фигры.
     * @param field модель поля.
     * @return можно ли поставить фигуру.
     */
    public boolean canPlaceFigure(Pair<Integer, Integer> coordsPlaced, int[][] figureTemplate, int[][] field) {
        // Если точка передана верно.
        if (coordsPlaced == null) {
            return false;
        }
        // Проверяем, не заходит ли что-то за поля.
        if (coordsPlaced.getValue() == 0 && rowContains(figureTemplate, 1, 0)) {
            return false;
        }
        if (coordsPlaced.getKey() == 0 && colContains(figureTemplate, 1, 0)) {
            return false;
        }
        // Проверяем, помещается ли фигура вообще.
        for (int i = 0; i < FieldModel.TEMPLATE_SIZE; i++) {
            for (int j = 0; j < FieldModel.TEMPLATE_SIZE; j++) {
                if (figureTemplate[i][j] == 1 && coordsPlaced.getValue() + i - 1 > 8 ||
                        figureTemplate[i][j] == 1 && coordsPlaced.getKey() + j - 1 > 8 ||
                        figureTemplate[i][j] == 1 && field[i + coordsPlaced.getValue() - 1][j + coordsPlaced.getKey() - 1] == 1) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Метод, выставляющий фигуру на поле.
     * @param coordsPlaced координаты центра поставленной фигуры.
     * @param figureTemplate модель фигурыю
     * @param field модель поля.
     */
    public void placeFigure(Pair<Integer, Integer> coordsPlaced, int[][] figureTemplate, int[][] field) {
        for (int i = 0; i < FieldModel.TEMPLATE_SIZE; i++) {
            for (int j = 0; j < FieldModel.TEMPLATE_SIZE; j++) {
                if (figureTemplate[i][j] == 1) {
                    field[i + coordsPlaced.getValue() - 1][j + coordsPlaced.getKey() - 1] = 1;
                }
            }
        }
    }

    /**
     * Увелицить количество ходов и отрисовать.
     */
    void incrementSteps() {
        numOfSteps++;
        steps.setText("Ходов: " + numOfSteps);
    }

    /**
     * Обработчик нажатия на кнопку "Стоп".
     */
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

    /**
     * Обработчик нажатия на кнопку "Начать новую игру".
     */
    @FXML
    protected void onStart() {
        numOfSteps = 0;
        steps.setText("Ходов: " + numOfSteps);
        fieldModel = new FieldModel();
        startTimer();
        drawField();
        drawFigure();
        figureTemplate.setOnMousePressed(mousePressed);
        figureTemplate.setOnMouseDragged(mouseDragged);
        figureTemplate.setOnMouseReleased(mouseReleased);
    }

    /**
     * Обработчик нажатия на кнопку выхода.
     */
    @FXML
    protected void onExit() {
        System.exit(0);
    }
}
