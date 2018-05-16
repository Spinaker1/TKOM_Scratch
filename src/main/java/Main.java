import executor.Executor;
import gui.*;
import input.InputManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import lexer.Lexer;
import node.Program;
import parser.Parser;
import semantic.SemanticParser;
import token.EventType;

import java.util.HashMap;
import java.util.Map;


public class Main extends Application {
    private final int WIDTH = 1000;
    private final int HEIGHT = 600;

    private Map<Sprite, SpriteThread> spriteThreadsHashMap = new HashMap<>();

    private Sprite sprite;

    private InputManager source = new InputManager();
    private Lexer lexer = new Lexer(source);
    private Parser parser = new Parser(lexer);
    private SemanticParser semanticParser = new SemanticParser();

    private TextArea consoleTextArea = new ConsoleTextArea(WIDTH, HEIGHT);
    private TextArea errorTextArea = new ErrorTextArea(WIDTH, HEIGHT);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World!");
        ImagesManager.loadImages();

        Pane root = new Pane();
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        sprite = new Sprite(primaryStage);
        SpritesBoard spritesBoard = new SpritesBoard(sprite);
        root.getChildren().add(spritesBoard);

        root.getChildren().add(consoleTextArea);
        root.getChildren().add(errorTextArea);

        HBox hBox = new HBox();

        Button button = new Button("Kompiluj");
        setupCompileButton(button);

        Button button1 = new Button("Wykonaj");
        setupExecuteButton(button1);

        hBox.getChildren().addAll(button, button1);
        root.getChildren().add(hBox);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void setupCompileButton(Button button) {
        button.setOnAction(e -> {
            try {
                errorTextArea.setText("");

                source.setInputStream(consoleTextArea.getText());
                Program program = parser.parse();
                semanticParser.check(program);

                sprite.setProgram(program);

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
    }

    private void setupExecuteButton(Button button) {
        button.setOnAction(e -> {
            SpriteThread spriteThread = new SpriteThread(sprite, EventType.START);
            spriteThreadsHashMap.put(sprite,spriteThread);
            spriteThread.start();

            sprite.addEventFilter(MouseEvent.MOUSE_PRESSED, e1 -> {
                sprite.stopTimeLine();
                
                if (spriteThreadsHashMap.containsKey(sprite)) {
                    SpriteThread spriteThreadToDelete = spriteThreadsHashMap.get(sprite);
                    spriteThreadsHashMap.remove(sprite);
                    spriteThreadToDelete.stop();
                }

                SpriteThread mouseSpriteThread = new SpriteThread(sprite, EventType.MOUSE);
                spriteThreadsHashMap.put(sprite,mouseSpriteThread);
                mouseSpriteThread.start();
            });

        });
    }

    private class SpriteThread extends Thread {
        private Sprite sprite;
        private Executor executor;
        private EventType eventType;

        SpriteThread(Sprite sprite, EventType eventType) {
            super();
            this.eventType = eventType;
            this.sprite = sprite;
            this.executor = new Executor();
        }

        @Override
        public void run() {
            executor.execute(sprite, eventType);
        }
    }
}