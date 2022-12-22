package agh.ics.oop;

import java.util.Comparator;

abstract class AbstractComparator implements Comparator<ElementOfBoundarySet> {
    protected int calcCompareForX(Vector2d o1, Vector2d o2) {
        return o1.x() - o2.x();
    }

    protected int calcCompareForY(Vector2d o1, Vector2d o2) {
        return o1.y() - o2.y();
    }
}
