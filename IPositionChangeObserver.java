package agh.ics.oop;

import java.util.Objects;

public interface IPositionChangeObserver {


    /**
     * @param mapElement
     * element on oldPosition
     * @param oldPosition
     * old position of animal
     * @param newPosition
     * new position of animal
     * change position on map in haspmap which contains all positions as keys
     */
    void positionChanged(AbstractWorldElement mapElement, Vector2d oldPosition, Vector2d newPosition);
}
