package tests;

import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import ru.hse.homework.fifth.modeles.*;
import ru.hse.homework.fifth.controller.*;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerTests {

    boolean contains(int[][][] arr, int[][] arr2) {
        for (int[][] ints : arr) {
            if (ints == arr2) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void getFigureTest() {
        assertTrue(contains(FiguresModel.figures, new AppController().getFigure()));
    }

    @Test
    public void colContainsTest() {
        assertTrue(new AppController().colContains(
                new int[][] {
                        {1,2,3},
                        {4,5,6},
                        {7,8,9}
                }, 4, 0
        ));
    }

    @Test
    public void rowContainsTest() {
        assertTrue(new AppController().rowContains(
                new int[][] {
                        {1,2,3},
                        {4,5,6},
                        {7,8,9}
                }, 5, 1
        ));
    }

    @Test
    public void canPlaceTest() {
        int[][] field = {
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
        int[][] figure = {
                {0,1,0},
                {0,1,0},
                {0,1,0},
        };
        assertTrue(new AppController().canPlaceFigure(new Pair<>(1,1), figure, field));
        assertFalse(new AppController().canPlaceFigure(new Pair<>(0,0), figure, field));
    }

    @Test
    public void placeFigureTest() {
        int[][] field = {
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
        int[][] figure = {
                {0,1,0},
                {0,1,0},
                {0,1,0},
        };
        new AppController().placeFigure(new Pair<>(1,1), figure, field);
        assertArrayEquals(field, new int[][]{
                {0, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
        });
    }
}
