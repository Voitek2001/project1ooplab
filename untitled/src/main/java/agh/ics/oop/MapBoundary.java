package agh.ics.oop;

import java.util.SortedSet;
import java.util.TreeSet;

class MapBoundary implements IPositionChangeObserver {

    SortedSet<ElementOfBoundarySet> xSortedElements = new TreeSet<>(new PrimaryXComparator());
    SortedSet<ElementOfBoundarySet> ySortedElements = new TreeSet<>(new PrimaryYComparator());

    public void addWorldElement(AbstractWorldElement mapElement) {
        mapElement.addObserver(this);
        ElementOfBoundarySet newElement = new ElementOfBoundarySet(mapElement.getPosition(), mapElement);
        xSortedElements.add(newElement);
        ySortedElements.add(newElement);

    }
    public void positionChanged(AbstractWorldElement mapElement, Vector2d oldPosition, Vector2d newPosition) {
        ElementOfBoundarySet oldElement = new ElementOfBoundarySet(oldPosition, mapElement);
        ElementOfBoundarySet newElement = new ElementOfBoundarySet(newPosition, mapElement);
        xSortedElements.remove(oldElement);
        ySortedElements.remove(oldElement);
        xSortedElements.add(newElement);
        ySortedElements.add(newElement);

    }

    public Vector2d getLowerLeft() {
        return xSortedElements.first().position().lowerLeft(ySortedElements.first().position());
    }
    public Vector2d getUpperRight() {
        return xSortedElements.last().position().upperRight(ySortedElements.last().position());
    }
}
