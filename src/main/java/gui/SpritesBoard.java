package gui;

import javafx.scene.layout.Pane;

public class SpritesBoard extends Pane {
    public SpritesBoard(Sprite sprite) {
        setLayoutX(0);
        setLayoutY(50);
        setMinHeight(400);
        setMinWidth(600);
        setMaxHeight(400);
        setMaxWidth(600);
        this.getChildren().add(sprite);
    }
}
