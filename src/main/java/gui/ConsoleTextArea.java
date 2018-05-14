package gui;

import javafx.scene.control.TextArea;

public class ConsoleTextArea extends TextArea {
    private final double width = 400;

    public ConsoleTextArea(final double windowWidth, final double windowHeight) {
        super();
        setMaxWidth(width);
        setMinHeight(windowHeight);
        setLayoutX(windowWidth-width);
        setLayoutY(0);
    }
}
