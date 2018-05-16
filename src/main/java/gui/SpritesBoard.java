package gui;

import javafx.scene.layout.Pane;

public class SpritesBoard extends Pane {
    private final int width = 600;
    private final int height = 400;

    public SpritesBoard(Sprite sprite) {
        setLayoutX(0);
        setLayoutY(0);
        setMinHeight(height);
        setMinWidth(width);
        setMaxHeight(height);
        setMaxWidth(width);

        this.getChildren().add(sprite);
    }
}
