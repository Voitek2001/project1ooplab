package agh.ics.oop.MapElements;

import agh.ics.oop.WorldMapComp.IWorldMap;
import agh.ics.oop.Vector2d;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractWorldElement implements IMapElement {

    protected Vector2d position;
    protected IWorldMap map;
    protected List<IPositionChangeObserver> observers = new LinkedList<>();

    public Vector2d getPosition() {
        return position;
    }
    public void addObserver(IPositionChangeObserver observer) {
        observers.add(observer);
    }
    public void removeObserver(IPositionChangeObserver observer) {
        observers.remove(observer);
    }

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        for (IPositionChangeObserver observer : observers) {
            observer.positionChanged(this, oldPosition, newPosition);
        }
    }

}
