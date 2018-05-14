package gui;

import javafx.scene.image.Image;

import java.util.HashMap;

public class ImagesManager {
    static HashMap<String, Image> imagesMap = new HashMap<>();

    public static void loadImages() {
        loadImage("chicken.png");
    }

    static private void loadImage(String fileName) {
         imagesMap.put(fileName, new Image("file:src/images/"+fileName));
    }
}
