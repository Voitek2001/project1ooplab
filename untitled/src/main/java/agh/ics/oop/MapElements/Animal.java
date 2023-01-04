package agh.ics.oop.MapElements;

import agh.ics.oop.*;
import agh.ics.oop.AnimalTracker.IGrassObserver;
import agh.ics.oop.WorldMapComp.AbstractWorldMap;
import agh.ics.oop.WorldMapComp.AnimalContainer;

import java.util.*;

public class Animal extends AbstractWorldElement {
    private MapDirection orientation = MapDirection.NORTH;
    private int noOfEatenGrass = 0;
    private int bornDate;

    private int age = 0;
    private int deathDate;
    private int numberOfChildren = 0;
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
        /**
         * Z genu otrzymujemy kolejny ruch
         * jeśli nowa pozycja mieści się w granicach mapy to ruch jest po prostu wykonywany
         * jeśli jest poza granicami to z zależności od wariantu mapy podejmowana jest decyzja o nowym polu
         * możnaby to rozbić na dwa warianty mapy i uniknąć paru ifów
         */
        MoveDirection direction = this.genotype.getCurrentMove();
        Vector2d possiblePosition = this.position.add(MapDirection.getByValue((direction.value + this.orientation.value)%8).get().toUnitVector());
        Random rand = new Random();
        this.age ++;
        int consumedEnergy = this.worldMap.getSimulationConfig().energyNecessary();
        if (!this.worldMap.isOutOfBound(possiblePosition)) {
            if (this.worldMap.getSimulationConfig().mapType().equals(MapType.HELLPORTAL)) {
                possiblePosition = new Vector2d(rand.nextInt(this.worldMap.getSimulationConfig().width()), rand.nextInt(this.worldMap.getSimulationConfig().height()));
                consumedEnergy = this.worldMap.getSimulationConfig().energyToCopulation();
            } else if (possiblePosition.x() >= this.worldMap.getSimulationConfig().width() || possiblePosition.x() < 0) {
                possiblePosition = new Vector2d((possiblePosition.x() + this.worldMap.getSimulationConfig().width()) % this.worldMap.getSimulationConfig().width(), possiblePosition.y());
            } else {
                this.reverseOrientation();
                this.energyChanged(this.getEnergy(), this.getEnergy() - consumedEnergy);
                updateStatus();
                return;
            }
        }
        this.positionChanged(this.position, possiblePosition);
        this.position = possiblePosition;
        this.energyChanged(this.getEnergy(), this.getEnergy() - consumedEnergy);
        updateStatus();

    }

    public void updateStatus() {
        if (energy < this.worldMap.getSimulationConfig().energyNecessary()) {
            this.status = AnimalStatus.DEAD;
            this.setDeathDate(age);
            return;
        }
        this.status = AnimalStatus.ALIVE;
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
        } + this.status + "energy: %d, pos:(%d, %d)".formatted(this.getEnergy(), this.position.x(), this.position.y());
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

    public int getAge() {
        return age;
    }
    public Genotype getGenotype() {
        return genotype;
    }
    public void setStatus(AnimalStatus newStatus) {

    }
    public void reverseOrientation() {
        MapDirection.getByValue((this.orientation.value + 4) % 8).ifPresent(val -> this.orientation = val);
    }

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        for (IPositionChangeObserver observer : observers) {
            observer.positionChanged(this, oldPosition, newPosition);
        }
    }

    public void energyChanged(int oldEnergy, int newEnergy) {
        this.setEnergy(newEnergy);
        for (IChangeEnergyObserver observer : this.energyObservers) {
            observer.energyChanged(this, oldEnergy, newEnergy);
        }
    }

    public void born() {
        for (ILifeObserver observer : this.lifeObservers) {
            observer.animalBorn(this);
        }
    }
    public void died() {
        for (ILifeObserver observer : this.lifeObservers) {
            observer.animalDied(this);
        }
    }

    public void eat() {
        for(IGrassObserver observer : this.grassObservers) {
            observer.grassEaten();
        }
    }
    public int getNoOfEatenGrass() {
        return noOfEatenGrass;
    }

    public void setNoOfEatenGrass() {
        this.noOfEatenGrass++;
    }

    public int getBornDate() {
        return bornDate;
    }

    public void setBornDate(int bornDate) {
        this.bornDate = bornDate;
    }

    public int getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(int deathDate) {
        this.deathDate = deathDate;
    }

    public void addChildren() {
        this.numberOfChildren++;
    }

    public int getChildren() {
        return this.numberOfChildren;
    }

}