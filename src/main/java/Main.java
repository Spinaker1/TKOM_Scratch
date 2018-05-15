import gui.*;
import input.InputManager;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import lexer.Lexer;
import node.Assignment;
import node.Program;
import parser.Parser;
import semantic.SemanticParser;

import javax.swing.*;


public class Main extends Application {
    private final int WIDTH = 1000;
    private final int HEIGHT = 600;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World!");
        ImagesManager.loadImages();

        Pane root = new Pane();
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        Sprite sprite = new Sprite(primaryStage);
        SpritesBoard spritesBoard = new SpritesBoard(sprite);
        root.getChildren().add(spritesBoard);

        TextArea consoleTextArea = new ConsoleTextArea(WIDTH, HEIGHT);
        root.getChildren().add(consoleTextArea);

        TextArea errorTextArea = new ErrorTextArea(WIDTH,HEIGHT);
        root.getChildren().add(errorTextArea);

        Button button = new Button("Kompiluj");
        root.getChildren().add(button);

        InputManager source = new InputManager();
        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        SemanticParser semanticParser = new SemanticParser();
        Executor executor = new Executor();

        button.setOnAction(e -> {
            try {
                errorTextArea.setText("");
                source.setInputStream(consoleTextArea.getText());
                Program program = parser.parse();
                semanticParser.check(program);
                executor.execute(sprite, program);
                errorTextArea.setText("Program skompilował się pomyślnie.");
            } catch (Exception exc) {
                exc.printStackTrace();
                String errorText = "Błąd:\n" + exc.getMessage() + "\nLinia: "
                        + source.getLineNumber() + " Znak: " + source.getPositionInLine();
                errorTextArea.setText(errorText);
                consoleTextArea.requestFocus();
                consoleTextArea.positionCaret(source.getPositionInFile());
            }
        });

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}