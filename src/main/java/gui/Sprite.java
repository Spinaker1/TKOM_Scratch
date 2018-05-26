package gui;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import structures.node.program.Program;

import java.awt.*;

public class Sprite extends ImageView {
    private Stage stage;

    private Program program = null;

    private final int MOVE_STEP = 5; //pixels
    private final int FRAME_DURATION = 40; //ms
    private final int ROTATION_DEGREE = 5; //degrees

    private Timeline timeline;
    private PauseTransition pause;
    private Label label;

    public Sprite(Stage stage) {
        this.stage = stage;
        setImage(ImagesManager.imagesMap.get("chicken.png"));
    }

    public void moveToMouse() {
        Point p = MouseInfo.getPointerInfo().getLocation().getLocation();
        Scene scene = stage.getScene();
        double x = p.getX() - stage.getX() - scene.getX() - getWidth() / 2;
        double y = p.getY() - stage.getY() - scene.getY() - getHeight() / 2;
        this.setX(x);
        this.setY(y);
    }

    public void move(double x, double y) {
        setX(x);
        setY(y);
    }

    public void rotateLeft(double degrees) {
        if (degrees < 0) {
            rotateRight(-degrees);
            return;
        }

        try {
            setRotate(getRotate() - ((int) degrees) % ROTATION_DEGREE);

            Thread.sleep(FRAME_DURATION);

            if (degrees >= ROTATION_DEGREE) {

                final int frames = (int) (degrees / ROTATION_DEGREE);

                timeline = new Timeline(
                        new KeyFrame(
                                Duration.millis(FRAME_DURATION),
                                event -> setRotate(getRotate() - ROTATION_DEGREE)
                        ));
                timeline.setCycleCount(frames);
                timeline.play();

                Thread.sleep(frames * FRAME_DURATION);
            }
        } catch (InterruptedException e) {
        }
    }

    public void rotateRight(double degrees) {
        if (degrees < 0) {
            rotateLeft(-degrees);
            return;
        }

        try {
            setRotate(getRotate() + ((int) degrees) % ROTATION_DEGREE);

            Thread.sleep(FRAME_DURATION);

            if (degrees >= ROTATION_DEGREE) {

                final int frames = (int) (degrees / ROTATION_DEGREE);

                timeline = new Timeline(
                        new KeyFrame(
                                Duration.millis(FRAME_DURATION),
                                event -> setRotate(getRotate() + ROTATION_DEGREE)
                        ));
                timeline.setCycleCount(frames);
                timeline.play();

                Thread.sleep(frames * FRAME_DURATION);
            }
        } catch (InterruptedException e) {
        }
    }

    public void moveLeft(double pixels) {
        if (pixels < 0) {
            moveRight(-pixels);
            return;
        }

        try {
            setX(getX() - ((int) pixels) % MOVE_STEP);

            Thread.sleep(FRAME_DURATION);

            if (pixels >= MOVE_STEP) {
                final int frames = (int) (pixels / MOVE_STEP);

                timeline = new Timeline(
                        new KeyFrame(
                                Duration.millis(FRAME_DURATION),
                                event ->
                                        setX(getX() - MOVE_STEP))
                );
                timeline.setCycleCount(frames);
                timeline.play();

                Thread.sleep(frames * FRAME_DURATION);
            }
        } catch (InterruptedException e) {
        }
    }

    public void moveRight(double pixels) {
        if (pixels < 0) {
            moveLeft(-pixels);
            return;
        }

        try {
            setX(getX() + ((int) pixels) % MOVE_STEP);

            Thread.sleep(FRAME_DURATION);

            if (pixels >= MOVE_STEP) {
                final int frames = (int) (pixels / MOVE_STEP);

                timeline = new Timeline(
                        new KeyFrame(
                                Duration.millis(FRAME_DURATION),
                                event -> setX(getX() + MOVE_STEP)
                        ));
                timeline.setCycleCount(frames);
                timeline.play();


                Thread.sleep(frames * FRAME_DURATION);
            }
        } catch (InterruptedException e) {
        }
    }

    public void moveDown(double pixels) {
        if (pixels < 0) {
            moveUp(-pixels);
            return;
        }

        try {
            setY(getY() + ((int) pixels) % MOVE_STEP);

            Thread.sleep(FRAME_DURATION);

            if (pixels >= MOVE_STEP) {
                Thread.sleep(FRAME_DURATION);

                final int frames = (int) (pixels / MOVE_STEP);

                timeline = new Timeline(
                        new KeyFrame(
                                Duration.millis(FRAME_DURATION),
                                event -> setY(getY() + MOVE_STEP))
                );
                timeline.setCycleCount(frames);
                timeline.play();

                Thread.sleep(frames * FRAME_DURATION);
            }
        } catch (InterruptedException e) {
        }
    }

    public void moveUp(double pixels) {
        if (pixels < 0) {
            moveDown(-pixels);
            return;
        }

        try {
            setY(getY() - ((int) pixels) % MOVE_STEP);

            Thread.sleep(FRAME_DURATION);

            if (pixels >= MOVE_STEP) {
                Thread.sleep(FRAME_DURATION);

                final int frames = (int) (pixels / MOVE_STEP);

                timeline = new Timeline(
                        new KeyFrame(
                                Duration.millis(FRAME_DURATION),
                                event -> setY(getY() - MOVE_STEP))
                );
                timeline.setCycleCount(frames);
                timeline.play();

                Thread.sleep(frames * FRAME_DURATION);
            }
        } catch (InterruptedException e) {
        }
    }

    public void changeSize(int a) {
        double b = ((double) a) / 100;
        setScaleX(b);
        setScaleY(b);
    }

    public void changeColor(int r, int g, int b) {
        Lighting lighting = new Lighting();
        lighting.setDiffuseConstant(1.0);
        lighting.setSpecularConstant(0.0);
        lighting.setSpecularExponent(0.0);
        lighting.setSurfaceScale(0.0);

        double rx = ((double) r) / 255;
        double gx = ((double) g) / 255;
        double bx = ((double) b) / 255;

        lighting.setLight(new Light.Distant(45, 45, new Color(rx, gx, bx, 1.00)));

        setEffect(lighting);
    }

    public void talk(String text) {
        String css = "    -fx-shape: \"M 45.673,0 C 67.781,0 85.703,12.475 85.703,27.862 C 85.703,43.249 67.781,55.724 45.673,55.724 C 38.742,55.724 32.224,54.497 26.539,52.34 C 15.319,56.564 0,64.542 0,64.542 C 0,64.542 9.989,58.887 14.107,52.021 C 15.159,50.266 15.775,48.426 16.128,46.659 C 9.618,41.704 5.643,35.106 5.643,27.862 C 5.643,12.475 23.565,0 45.673,0 M 45.673,2.22 C 24.824,2.22 7.862,13.723 7.862,27.863 C 7.862,34.129 11.275,40.177 17.472,44.893 L 18.576,45.734 L 18.305,47.094 C 17.86,49.324 17.088,51.366 16.011,53.163 C 15.67,53.73 15.294,54.29 14.891,54.837 C 18.516,53.191 22.312,51.561 25.757,50.264 L 26.542,49.968 L 27.327,50.266 C 32.911,52.385 39.255,53.505 45.673,53.505 C 66.522,53.505 83.484,42.002 83.484,27.862 C 83.484,13.722 66.522,2.22 45.673,2.22 L 45.673,2.22 z \";\n" +
                "    -fx-background-color: black, white;\n" +
                "    -fx-background-insets: 0,1;\n" +
                "    -fx-padding: 50;";

        label = new Label(text);
        label.setLayoutX(this.getX() + getWidth());
        label.setLayoutY(this.getY() - getHeight() * 0.);
        label.setStyle(css);

        Pane pane = (Pane) getParent().getParent();
        Platform.runLater(() -> pane.getChildren().add(label));

        pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> pane.getChildren().remove(label));
        pause.play();
        sleep(4);
    }

    public void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
        }
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public void stopTimeLine() {
        if (timeline != null) {
            timeline.stop();
        }

        if (pause != null) {
            pause.stop();
        }
        Pane pane = (Pane) getParent().getParent();
        pane.getChildren().remove(label);
    }

    public double getWidth() {
        return getImage().getWidth();
    }

    public double getHeight() {
        return getImage().getHeight();
    }
}
