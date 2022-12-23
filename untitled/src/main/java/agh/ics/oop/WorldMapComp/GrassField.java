package agh.ics.oop;

import agh.ics.oop.MapElements.AbstractWorldElement;
import agh.ics.oop.MapElements.Animal;
import agh.ics.oop.MapElements.Grass;
import agh.ics.oop.MapElements.IMapElement;

import java.util.*;

public class GrassField extends AbstractWorldMap {

    private final int n;



    public GrassField(int n) {
        this.visualizer = new MapVisualizer(this);
        this.n = n;
        Integer[] randomCoordinates = generateDistRandomNumbers(n, (int) Math.sqrt(10 * n) * (int) Math.sqrt(10 * n));
        HashMap<Vector2d, IMapElement> elementsOnMap = getElementsOnMap();
        for (int i = 0; i < n; i++) {
            Vector2d pos = new Vector2d(randomCoordinates[i] / (int) Math.sqrt(10 * n), randomCoordinates[i] % (int) Math.sqrt(10 * n));
            AbstractWorldElement newGrass = new Grass(pos);
            elementsOnMap.put(pos, newGrass);
            mapBoundary.addWorldElement(newGrass);
        }

    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        if (super.isOccupied(position) && getElementsOnMap().get(position) instanceof Grass) {
            return true;
        }
        return !super.isOccupied(position);
    }

    @Override
    public void place(Animal animal) {
        super.place(animal);

    }

    @Override
    public void positionChanged(AbstractWorldElement mapElement, Vector2d oldPosition, Vector2d newPosition) {
        // zapytać czy zapisać wywołanie getElementsOnMap do zmiennej
        HashMap<Vector2d, IMapElement> elementsOnMap = getElementsOnMap();
        elementsOnMap.remove(oldPosition);
        if (getElementsOnMap().get(newPosition) instanceof Grass) {
            findNewPositionForGrass(newPosition, (AbstractWorldElement) getElementsOnMap().get(newPosition));
        }
        elementsOnMap.put(newPosition, mapElement);
    }

    @Override
    public String toString() {
        Bounds newBounds = getBounds();
        return super.toString(newBounds.lowerLeft(), newBounds.upperRight());
    }

    public Bounds getBounds() {
        Vector2d lowerLeft;
        Vector2d upperRight;

        if (getElementsOnMap().isEmpty()) {
            lowerLeft = new Vector2d(Integer.MAX_VALUE, Integer.MAX_VALUE);
            upperRight = new Vector2d(Integer.MIN_VALUE, Integer.MIN_VALUE);
        } else {
            lowerLeft = this.mapBoundary.getLowerLeft();
            upperRight = this.mapBoundary.getUpperRight();
        }

        return new Bounds(lowerLeft, upperRight);

    }

    @Override
    public void feedAnimals() {

    }

//    @Override
//    public void reproductAnimals() {
//
//    }

    private Integer[] generateDistRandomNumbers(int count, int maxValue) {
        // if count > maxValue doesn't have sense to generate distinct number, raise error or return null
        Integer[] randomNum = new Integer[maxValue];
        Random rand = new Random();

        for (int i = 0; i < maxValue; i++) {
            randomNum[i] = i;
        }

        for (int i = 0; i < maxValue; i++) {
            int randomIndex = rand.nextInt(maxValue);
            int temp = randomNum[randomIndex];
            randomNum[randomIndex] = randomNum[i];
            randomNum[i] = temp;
        }
        return Arrays.copyOfRange(randomNum, 0, count);

    }

    private void findNewPositionForGrass(Vector2d oldPosition, AbstractWorldElement grass) {
        Optional<Vector2d> newGrassPos = generateNewNotOccupiedPosition(oldPosition);
        newGrassPos.ifPresent(// check if it is even possible to place grass on new place
                (newPos) -> {
                    getElementsOnMap().put(newPos, grass);
                    mapBoundary.positionChanged(grass, oldPosition, newPos);
                }
        );
    }

//    (Dla zaawansowanych).
//    Zmodyfikuj implementację tak, żeby po spotkaniu zwierzęcia i trawy, trawa znikała.
//    Nowe kępki trawy powinny pojawiać się losowo w obszarze z punktu 1, po zjedzeniu trawy przez zwierzę,
//    przy założeniu, że nowe położenie kępki trawy nie pokrywa się z istniejącą kępką trawy, ani z żadnym zwierzęciem.
//
//    Po dłuższym zastanowieniu, nie za bardzo wiem jak mógłbym usunąć atrybut n
//    i nadal móc realizować losowanie trawy dla powyższego punktu.
    private Optional<Vector2d> generateNewNotOccupiedPosition(Vector2d pos) {
        LinkedList<Vector2d> tmpList = new LinkedList<>();
        for (int i = 0; i < (int) Math.sqrt(10 * n); i++) {
            for (int j = 0; j < (int) Math.sqrt(10 * n); j++) {
                Vector2d newPos = new Vector2d(i, j);
                if (!isOccupied(newPos) && !newPos.equals(pos)) {
                    tmpList.add(newPos);
                }
            }
        }

        return Optional.ofNullable(tmpList.get(new Random().nextInt(tmpList.size()-1)));

    }

}
