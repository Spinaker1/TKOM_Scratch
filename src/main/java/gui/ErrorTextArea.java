package gui;

import javafx.scene.control.TextArea;

public class ErrorTextArea extends TextArea {
    private final int height = 150;

    public ErrorTextArea(final double windowWidth, final double windowHeight) {
        super();
        setMinWidth(windowWidth-400);
        setMaxHeight(height);
        setLayoutX(0);
        setLayoutY(windowHeight-height);
        setEditable(false);
    }
}
