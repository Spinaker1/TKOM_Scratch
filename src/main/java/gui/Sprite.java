package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.Point;
import java.awt.MouseInfo;

public class Sprite extends ImageView{
    private Stage stage;

    public Sprite(Stage stage) {
        this.stage = stage;
        setImage(ImagesManager.imagesMap.get("chicken.png"));
    }

    public void moveToMouse() {
        Image image = getImage();
        Point p = MouseInfo.getPointerInfo().getLocation().getLocation();
        Scene scene = stage.getScene();
        double x = p.getX()-stage.getX()-scene.getX()-image.getWidth()/2;
        double y = p.getY()-stage.getY()-scene.getY()-image.getHeight()/2;
        this.setX(x);
        this.setY(y);
    }

    public void move(double x, double y) {
        setX(x);
        setY(y);
    }

    public void rotateLeft(double degrees) {
        setRotate(getRotate()-degrees);
    }

    public void rotateRight(double degrees) {
        setRotate(getRotate()+degrees);
    }

    public void moveLeft(double pixels) {
        setX(getX()-pixels);
    }

    public void moveRight(double pixels) {
        setX(getX()+pixels);
    }

    public void moveDown(double pixels) {
        setY(getY()+pixels);
    }

    public void moveUp(double pixels) {
        setY(getY()-pixels);
    }

    public void changeSize(int a) {
        double b = ((double)a)/100;
        setScaleX(b);
        setScaleY(b);
    }

    public void changeColor(int r, int g, int b) {
        Lighting lighting = new Lighting();
        lighting.setDiffuseConstant(1.0);
        lighting.setSpecularConstant(0.0);
        lighting.setSpecularExponent(0.0);
        lighting.setSurfaceScale(0.0);

        double rx = ((double)r)/255;
        double gx = ((double)g)/255;
        double bx = ((double)b)/255;

        System.out.println(rx);

        lighting.setLight(new Light.Distant(45, 45, new Color(rx,gx,bx,1.00)));

        setEffect(lighting);
    }
}
