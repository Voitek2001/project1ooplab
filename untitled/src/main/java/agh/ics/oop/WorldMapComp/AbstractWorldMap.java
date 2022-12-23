package agh.ics.oop.WorldMapComp;

import agh.ics.oop.*;
import agh.ics.oop.MapElements.AbstractWorldElement;
import agh.ics.oop.MapElements.Animal;
import agh.ics.oop.MapElements.IMapElement;
import agh.ics.oop.MapElements.IPositionChangeObserver;

import java.util.HashMap;
import java.util.Optional;


public abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver {
    private final HashMap<Vector2d, IMapElement> elementsOnMap = new HashMap<>();


    private final HashMap<Vector2d, AnimalContainer> animalContainers = new HashMap<>();

    protected final MapBoundary mapBoundary = new MapBoundary();


    protected HashMap<Vector2d, IMapElement> getElementsOnMap() {
        return elementsOnMap;
    }

    public Optional<IMapElement> objectAt(Vector2d position) {
        IMapElement possibleMapEl = elementsOnMap.get(position);
        return Optional.ofNullable(possibleMapEl);
    }

    public void place(Animal animal) throws IllegalArgumentException {
        //zakładam że place umieszcza tylko klase Animal na mapie

        Vector2d elePos = animal.getPosition();
        Optional<IMapElement> elementOnCurrPos = objectAt(elePos);
        elementOnCurrPos.ifPresentOrElse(
                (value) -> {if (!(value instanceof Animal)) {
                    this.elementsOnMap.remove(elePos);
                    this.elementsOnMap.put(elePos, animal);} else {
                    throw new IllegalArgumentException("Pole (%d, %d) jest zajete\n".formatted(elePos.x(), elePos.y()));}
                },
                () -> this.elementsOnMap.put(elePos, animal)
        );
        mapBoundary.addWorldElement(animal);

        if (!this.animalContainers.containsKey(elePos)) {
            this.animalContainers.put(elePos, new AnimalContainer());
        }
        this.animalContainers.get(elePos).addNewAnimal(animal);
        animal.addObserver(this);
    }

    public boolean isOccupied(Vector2d position) {
        return elementsOnMap.containsKey(position);
    }


    public void positionChanged(AbstractWorldElement animal, Vector2d oldPosition, Vector2d newPosition) {
        HashMap<Vector2d, IMapElement> elementsOnMap = getElementsOnMap();
        elementsOnMap.remove(oldPosition);
        elementsOnMap.put(newPosition, animal);
    }

    public abstract Bounds getBounds();

    public void feedAnimals() {
        this.animalContainers.forEach((currPos, currAnimalContainer) -> {

        });

    }

    public HashMap<Vector2d, AnimalContainer> getAnimalContainers() {
        return animalContainers;
    }


}
