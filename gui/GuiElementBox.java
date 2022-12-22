package agh.ics.oop.gui;

import agh.ics.oop.IMapElement;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.FileNotFoundException;

public class GuiElementBox {

    private final VBox guiElement = new VBox(4);
    public GuiElementBox(IMapElement mapElement, String parentPath, int imgViewWidth, int imgViewHeight) {
        try {
            Image image = ImageLoader.loadImage(parentPath + "/" + mapElement.getImagePath());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(imgViewWidth);
            imageView.setFitHeight(imgViewHeight);
            guiElement.getChildren().add(imageView);
            Label label = new Label(mapElement.describePosition());
            guiElement.getChildren().add(label);
            guiElement.setAlignment(Pos.CENTER);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace(System.out);
        }
    }

    public VBox getGUIElement() {
        return guiElement;
    }



}
