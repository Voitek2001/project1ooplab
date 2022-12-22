package agh.ics.oop;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Animal extends AbstractWorldElement {
    private MapDirection orientation = MapDirection.NORTH;
    private Vector2d position;



    private final Genotype genotype;
    private final AbstractWorldMap worldMap;

    private int energy;

    private final int id = IDGenerator.currId.getAndIncrement();

    private AnimalStatus status;

    public Animal(AbstractWorldMap map) {
        this(map, new Vector2d(2, 2));
    }
    public Animal(AbstractWorldMap map, Vector2d initialPosition) {
        this(map, initialPosition, 10);
    }

    public Animal(AbstractWorldMap map, Vector2d initialPosition, int initEnergy) {
        this(map, initialPosition, initEnergy, new Genotype(new ArrayList<>()));
    }
    public Animal(AbstractWorldMap map, Vector2d initialPosition, int initEnergy, Genotype genotype) {
        this.position = initialPosition;
        this.worldMap = map;
        this.energy = initEnergy;
        this.status = AnimalStatus.ALIVE;
        this.genotype = genotype;
    }

    public MapDirection getOrientation() {
        return orientation;
    }


    public void makeMove() {
        // if nieco szaleństwa

    }
    public void move(MoveDirection direction) {
        Vector2d possiblePosition = this.position;
        switch (direction) {
            case LEFT -> this.orientation = this.orientation.previous();
            case RIGHT -> this.orientation = this.orientation.next();
            case FORWARD -> possiblePosition = this.position.add(this.orientation.toUnitVector());
            case BACKWARD -> possiblePosition = this.position.add(this.orientation.toUnitVector().opposite());
        }
//        System.out.println(worldMap.canMoveTo(possiblePosition));
        if (worldMap.canMoveTo(possiblePosition)) {
            this.positionChanged(this.position, possiblePosition);
            this.position = possiblePosition;
        }

        // position = position.lowerLeft(4, 4)
        // position = position.upperRight(0, 0)
    }


    public Vector2d getPosition() {
        return position;
    }

    public boolean isAt(Vector2d position) {
        return Objects.equals(this.position, position); // use object to avoid nullpointerexception
    }

    public String toString() {
//        return "Animal position: (%d, %d) and orientation: %s".formatted(position.x(), position.y(), orientation);
        return switch (this.orientation) {
            case WEST -> "<";
            case SOUTH -> "v";
            case NORTH -> "^";
            case EAST -> ">";
            case NORTHEAST -> "^>";
            case SOUTHEAST -> "v>";
            case SOUTHWEST -> "<v";
            case NORTHWEST -> "<^";
        };
    }

    public String getImagePath() {
        return switch (this.orientation) {
            case WEST -> "left.png";
            case SOUTH -> "down.png";
            case NORTH -> "up.png";
            case EAST -> "right.png";
            case NORTHEAST -> "up_right.png";
            case SOUTHEAST -> "down_right.png";
            case SOUTHWEST -> "down_left.png";
            case NORTHWEST -> "up_left.png";
        };
    }

    public String describePosition() {
        return switch (this.orientation) {
            case WEST -> "W";
            case SOUTH -> "S";
            case NORTH -> "N";
            case EAST -> "E";
            case NORTHEAST -> "NE";
            case SOUTHEAST -> "SE";
            case SOUTHWEST -> "SW";
            case NORTHWEST -> "NW";
        } + " (%d , %d)".formatted(this.position.x(), this.position.y()) + "id:%d".formatted(this.id);
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getEnergy() {
        return energy;
    }

    public AnimalStatus getStatus() {
        return status;
    }
    public int getId() {
        return this.id;
    }
    public Genotype getGenotype() {
        return genotype;
    }
}
