package com.example.hw5;

import javafx.scene.Node;

public class DraggableMaker {
    private double mouseAnchorX;
    private double mouseAnchorY;



    public void makeDraggable(Node node){
        node.setOnMousePressed(mouseEvent -> {
            mouseAnchorX = mouseEvent.getX();
            mouseAnchorY = mouseEvent.getY();
        });

        node.setOnMouseDragged(mouseEvent -> {
            node.setLayoutX(mouseEvent.getSceneX() - mouseAnchorX);
            node.setLayoutY(mouseEvent.getSceneY() - mouseAnchorY);
        });

        node.setOnMouseReleased(mouseDragEvent -> {
            // В этом методе определяем координаты закрашенных кусков, если каждый закрашенный кусок в таблице и каждый кусок таблицы под ним не закрашен, тогда перекрашиваем соответствующие куски таблицы.
            double xCord = node.getLayoutX();
            double yCord = node.getLayoutY();

            node.setLayoutX(431);
            node.setLayoutY(260);
        });
    }
}
