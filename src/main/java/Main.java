import executor.Executor;
import gui.*;
import input.InputManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lexer.Lexer;
import parser.Parser;
import semantic.SemanticParser;
import structures.node.event.EventType;
import structures.node.program.Program;

public class Main extends Application {
    private final int WIDTH = 1000;
    private final int HEIGHT = 550;

    private SpriteThread spriteThread;

    private CollisionThread collisionThread = new CollisionThread();

    private Sprite sprite;

    private InputManager source = new InputManager();
    private Lexer lexer = new Lexer(source);
    private Parser parser = new Parser(lexer);
    private SemanticParser semanticParser = new SemanticParser();

    private TextArea consoleTextArea = new ConsoleTextArea(WIDTH, HEIGHT);
    private TextArea errorTextArea = new ErrorTextArea(WIDTH, HEIGHT);
    private SpritesBoard spritesBoard;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Scratch");
        ImagesManager.loadImages();

        Pane root = new Pane();
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        sprite = new Sprite(primaryStage);
        spritesBoard = new SpritesBoard(sprite);
        root.getChildren().add(consoleTextArea);
        root.getChildren().add(errorTextArea);
        root.getChildren().add(spritesBoard);

        HBox hBox = new HBox();
        hBox.setLayoutX(600);

        Button button = new Button("Kompiluj");
        setupCompileButton(button);

        Button button1 = new Button("Wykonaj");
        setupExecuteButton(button1);

        hBox.getChildren().addAll(button, button1);
        root.getChildren().add(hBox);

        collisionThread.start();

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(e -> collisionThread.stop());
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
            startNewSpriteThread(EventType.START);

            sprite.addEventFilter(MouseEvent.MOUSE_PRESSED, e1 -> {
                startNewSpriteThread(EventType.MOUSE);
            });
        });
    }

    private void startNewSpriteThread(EventType eventType) {
        stopSpriteThread();

        spriteThread = new SpriteThread(sprite, eventType);
        spriteThread.start();
    }

    private void stopSpriteThread() {
        if (spriteThread != null ) {
            sprite.stopTimeLine();
            spriteThread.stop();
        }
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
            try {
                executor.execute(sprite, eventType);
            }
            catch (Exception e) {
                String errorText = "Błąd! Zatrzymano wykonanie programu.\n" + e.getMessage();

                e.printStackTrace();

                errorTextArea.setText(errorText);
                stopSpriteThread();
            }
        }
    }

    private class CollisionThread extends Thread {
        @Override
        public void run() {
            while (true) {
                boolean isCollision = false;

                if (sprite.getX() < 0) {
                    stopSpriteThread();
                    sprite.setX(0.01);
                    isCollision = true;
                }
                if (sprite.getX() > spritesBoard.getWidth() - sprite.getWidth()) {
                    stopSpriteThread();
                    sprite.setX(spritesBoard.getWidth() - sprite.getWidth()-0.01);
                    isCollision = true;
                }
                if (sprite.getY() < spritesBoard.getLayoutY()) {
                    stopSpriteThread();
                    sprite.setY(spritesBoard.getLayoutY()+0.01);
                    isCollision = true;
                }
                if (sprite.getY() > spritesBoard.getLayoutY() + spritesBoard.getHeight() - sprite.getHeight()) {
                    stopSpriteThread();
                    sprite.setY(spritesBoard.getLayoutY() + spritesBoard.getHeight() - sprite.getHeight()-0.01);
                    isCollision = true;
                }

                if(isCollision) {
                    startNewSpriteThread(EventType.WALL);
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
        }
    }
}