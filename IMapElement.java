package agh.ics.oop;

public interface IMapElement {

    /**
     *
     * @return current position of object (x, y coordinate)
     */
    Vector2d getPosition();

    /**
     *
     * @return string representation of object
     */
    String toString();

    /**
     *
     * @return string with path to image.
     */

    String getImagePath();


    String describePosition();
}
