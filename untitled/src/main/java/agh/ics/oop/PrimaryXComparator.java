package agh.ics.oop;

import java.util.Comparator;

public class PrimaryXComparator implements Comparator<ElementOfBoundarySet> {

    @Override
    public int compare(ElementOfBoundarySet o1, ElementOfBoundarySet o2) {

        int xDiff = calcCompareForX(o1.position(), o2.position());
        if (xDiff != 0) {
            return xDiff;
        }
        int yDiff = calcCompareForY(o1.position(), o2.position());
        if (yDiff != 0) {
            return yDiff;
        }
        // it's impossible to have animal and grass on the same place
        if (o1.mapElement() instanceof Animal && !(o2.mapElement() instanceof Animal)) {
            return 1;
        }
        return 0;
    }

    protected int calcCompareForX(Vector2d o1, Vector2d o2) {
        return o1.x() - o2.x();
    }

    protected int calcCompareForY(Vector2d o1, Vector2d o2) {
        return o1.y() - o2.y();
    }

}
