package agh.ics.oop;

import java.util.Comparator;

public class PrimaryYComparator extends AbstractComparator {

    @Override
    public int compare(ElementOfBoundarySet o1, ElementOfBoundarySet o2) {

        int yDiff = calcCompareForY(o1.position(), o2.position());
        if (yDiff != 0) {
            return yDiff;
        }
        int xDiff = calcCompareForX(o1.position(), o2.position());
        if (xDiff != 0) {
            return xDiff;
        }
        // it's impossible to have animal and grass on the same place
        if (o1.mapElement() instanceof Animal && !(o2.mapElement() instanceof Animal)) {
            return 1;
        }
        return 0;
    }

}

