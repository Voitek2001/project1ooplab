package agh.ics.oop.MapElements;

import agh.ics.oop.MapElements.AbstractWorldElement;
import agh.ics.oop.Vector2d;

public class Grass extends AbstractWorldElement {
    private final Vector2d position;
    public Grass(Vector2d position) {
        this.position = position;
    }

    public Vector2d getPosition() {
        return this.position;
    }

    @Override
    public String toString() {
        return "*";
    }

    @Override
    public String getImagePath() {
        return "Grass.png";
    }

    public String describePosition() {
        return "Grass";
    }


}
