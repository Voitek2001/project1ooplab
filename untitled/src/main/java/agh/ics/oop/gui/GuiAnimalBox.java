
package agh.ics.oop.gui;

import agh.ics.oop.MapElements.Animal;
import agh.ics.oop.MapElements.AnimalStatus;
import agh.ics.oop.MoveDirection;
import javafx.geometry.Insets;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.FileNotFoundException;

public class GuiAnimalBox {

    private VBox box;
    public GuiAnimalBox(Animal animal, String parentPath) {
        String img = animal.getImagePath();
        if (animal.getStatus().equals(AnimalStatus.DEAD)){
            img = "rip.png";
        }
        try {
            Image image = ImageLoader.loadImage(parentPath + "/" + img);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);
            ProgressBar energybar = new ProgressBar();
            energybar.setPrefSize(60, 15);
            energybar.setProgress((double) animal.getEnergy() / 30);
            box = new VBox(imageView, energybar);
            VBox.setMargin(imageView, new Insets(0, 0, 0, 0));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace(System.out);
        }
    }

    public VBox getBox() {
        return box;
    }
}

