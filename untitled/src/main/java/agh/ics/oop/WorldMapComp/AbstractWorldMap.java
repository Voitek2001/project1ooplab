package agh.ics.oop.WorldMapComp;

import agh.ics.oop.*;
import agh.ics.oop.MapElements.*;
import agh.ics.oop.Simulation.SimulationConfig;

import java.util.*;


public abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver {
    private final HashMap<Vector2d, AnimalContainer> animalContainers = new HashMap<>();
    private final SimulationConfig simulationConfig;
    protected HashMap<Vector2d, Grass> grassMap = new HashMap<>();
    protected List<ToxicCorpsesElement> howManyDied = new ArrayList<>();


    public AbstractWorldMap(SimulationConfig simulationConfig) {
        this.simulationConfig = simulationConfig;
        for(int i = 0; i < simulationConfig.width(); i++) {
            for (int j = 0; j < simulationConfig.height(); j++) {
                this.howManyDied.add(new ToxicCorpsesElement(new Vector2d(i, j), 0));
            }
        }
    }
    public Optional<IMapElement> objectAt(Vector2d position) {
        IMapElement possibleMapEl = grassMap.get(position);
        return Optional.ofNullable(possibleMapEl);
    }

    public void place(Animal animal) throws IllegalArgumentException {
        //zakładam że place umieszcza tylko klase Animal na mapie

        Vector2d elePos = animal.getPosition();

        if (!this.animalContainers.containsKey(elePos)) {
            this.animalContainers.put(elePos, new AnimalContainer());
        }
        this.animalContainers.get(elePos).addNewAnimal(animal);
        animal.addObserver(this);
    }

    private Bounds getForestedEquatorBounds() {
        int h1 = (int) (this.simulationConfig.height() / 2 + this.simulationConfig.height() * 0.1);
        int h2 = (int) (this.simulationConfig.height() / 2 - this.simulationConfig.height() * 0.1);
        return new Bounds(new Vector2d(0, h2), new Vector2d(this.simulationConfig.width(), h1));
    }

    private List<Vector2d> findFreePositions(Vector2d leftSide, Vector2d rightSide) {
        List<Vector2d> freePos = new LinkedList<>();
        for (int i = leftSide.x(); i < rightSide.x(); i++) {
            for(int j = leftSide.y(); j < rightSide.y(); j++) {
                Vector2d currPos = new Vector2d(i, j);
                if (!this.grassMap.containsKey(currPos)) {
                    freePos.add(currPos);
                }
            }
        }
        return freePos;
    }

    private Optional<Vector2d> findNewPositionForGrassIfAfforestation() {
        Random rand = new Random();
        List<Vector2d> freePositions;
        Bounds forestBound = getForestedEquatorBounds();

        if (rand.nextInt(5) < 4) {
            // na równiku
            freePositions = findFreePositions(forestBound.lowerLeft(), forestBound.upperRight());
            return freePositions.size() > 0 ? Optional.of(freePositions.get(rand.nextInt(freePositions.size()))) : Optional.empty();
        }

        // nie na równiku
        if (rand.nextInt(2) == 0) {
            // górna czesc równika
            freePositions = findFreePositions(new Vector2d(0, forestBound.upperRight().y()), new Vector2d(this.simulationConfig.width(), this.simulationConfig.height()));
            return freePositions.size() > 0 ? Optional.of(freePositions.get(rand.nextInt(freePositions.size()))) : Optional.empty();

        }
        freePositions = findFreePositions(new Vector2d(0, 0), new Vector2d(forestBound.upperRight().x(), forestBound.lowerLeft().y()));
        return freePositions.size() > 0 ? Optional.of(freePositions.get(rand.nextInt(freePositions.size()))) : Optional.empty();

    }

    public void addDeadAtPosition(Vector2d pos) {
        int ind = pos.x() + pos.y() * this.simulationConfig.width();
        this.howManyDied.get(ind).increaseCorpsesByOne();
        this.howManyDied
                .stream()
                .filter(element -> element.getPosition().equals(pos))
                .forEach(ToxicCorpsesElement::increaseCorpsesByOne);
        Collections.sort(this.howManyDied);
    }
    private Optional<Vector2d> findNewPositionForGrassIfDeadCorpses() {
        Random rand = new Random();
        List<Vector2d> freePositions = new LinkedList<>();
        if (rand.nextInt(5) < 4) {
            List<ToxicCorpsesElement> currHowManyDied = this.howManyDied.subList(0, (int) (0.8 * howManyDied.size()));
            for(ToxicCorpsesElement toxicCropEle : currHowManyDied) {
                if (!this.grassMap.containsKey(toxicCropEle.getPosition())) {
                    freePositions.add(toxicCropEle.getPosition());
                }
            }
            return freePositions.size() > 0 ? Optional.of(freePositions.get(rand.nextInt(freePositions.size()))) : Optional.empty();
        }
        List<ToxicCorpsesElement> currHowManyDied = this.howManyDied.subList((int) (0.8 * howManyDied.size()), howManyDied.size());
        for(ToxicCorpsesElement toxicCropEle : currHowManyDied) {
            if (!this.grassMap.containsKey(toxicCropEle.getPosition())) {
                freePositions.add(toxicCropEle.getPosition());
            }
        }
        return freePositions.size() > 0 ? Optional.of(freePositions.get(rand.nextInt(freePositions.size()))) : Optional.empty();

    }

    public void generateNewGrasses() {
        if (this.simulationConfig.AfforestationType().equals(AfforestationType.FORESTEDEQUATORS)) {

            for (int i = 0; i < this.simulationConfig.everydayPlantCount(); i++) {
                findNewPositionForGrassIfAfforestation().ifPresent((newPos) -> this.grassMap.put(newPos, new Grass(newPos)));
            }
            return;
        }
        for (int i = 0; i < this.simulationConfig.everydayPlantCount(); i++) {
            findNewPositionForGrassIfDeadCorpses().ifPresent((newPos) -> this.grassMap.put(newPos, new Grass(newPos)));
        }

    }

    public boolean isOccupied(Vector2d position) {
        return grassMap.containsKey(position);
    }

    public boolean isOutOfBound(Vector2d pos) {

        return (pos.precedes(new Vector2d(this.simulationConfig.width(), this.simulationConfig.height())) && pos.follows(new Vector2d(0, 0)));
    }

    public void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition) {

        AnimalContainer oldPosAnimalContainer = this.animalContainers.get(oldPosition);
        if (!this.animalContainers.containsKey(newPosition)) {
            this.animalContainers.put(newPosition, new AnimalContainer());
        }
        AnimalContainer newPosAnimalContainer = this.animalContainers.get(newPosition);

//        System.out.println(newPosition);
        oldPosAnimalContainer.removeAnimal(animal);
        newPosAnimalContainer.addNewAnimal(animal);

    }

    public abstract Bounds getBounds();

    public void feedAnimals() {
        List<Vector2d> keysToDel = new LinkedList<>();
        for (Map.Entry<Vector2d, Grass> entry : this.grassMap.entrySet()) {
            this.animalContainers.get(entry.getKey()).getGreatestEnergyAnimal().ifPresent((animal -> animal.setEnergy(animal.getEnergy() + this.simulationConfig.plantEnergyProfit())));
            keysToDel.add(entry.getKey());
        }
        keysToDel.forEach((currKey) -> this.grassMap.remove(currKey));
    }

    public HashMap<Vector2d, AnimalContainer> getAnimalContainers() {
        return animalContainers;
    }
    public SimulationConfig getSimulationConfig() {
        return simulationConfig;
    }
    public HashMap<Vector2d, Grass> getGrassMap() {
        return grassMap;
    }

}
