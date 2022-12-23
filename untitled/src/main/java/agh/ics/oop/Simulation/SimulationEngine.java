package agh.ics.oop.Simulation;

import agh.ics.oop.*;
import agh.ics.oop.MapElements.Animal;
import agh.ics.oop.MapElements.AnimalStatus;
import agh.ics.oop.MapElements.Genotype;
import agh.ics.oop.WorldMapComp.AbstractWorldMap;
import agh.ics.oop.WorldMapComp.Bounds;
import agh.ics.oop.gui.IRenderGridObserver;
import javafx.util.Pair;

import java.util.*;


public class SimulationEngine implements IEngine {

    private final AbstractWorldMap map;
    private final List<Animal> animalsList = new ArrayList<>();
    private final List<IRenderGridObserver> renderGridobservers = new ArrayList<>();
    private final int moveDelay;
    private final SimulationConfig simulationConfig;

    public SimulationEngine(AbstractWorldMap map, SimulationConfig simulationConfig, int numberOfInitAnimals, int moveDelay) throws IllegalArgumentException {

        this.map = map;
        this.simulationConfig = simulationConfig;
        this.moveDelay = moveDelay;
        Bounds bounds = map.getBounds();
        int height = bounds.upperRight().y() - bounds.lowerLeft().y();
        int width = bounds.upperRight().x() - bounds.lowerLeft().x();
        Integer[] randomStartingPositions = RandomGenerator.generateDistRandomNumbers(numberOfInitAnimals, height * width);
        for (int i = 0; i < numberOfInitAnimals; i++) {
            Vector2d newPosition = new Vector2d(randomStartingPositions[i] % width, randomStartingPositions[i] / height);
            Animal animal = new Animal(map, newPosition, this.simulationConfig.animalStartEnergy());
            map.place(animal);
            this.animalsList.add(animal);
        }
    }

    @Override
    public void run() {
        while (true) {
            // 1. Usuniecie z mapy wszystkich martwych zwierząt
            removeDeadAnimals();
            // 2. Zrobienie ruchu
            simulateMove();
            // 3. Jedzenie
            feedAnimals();
            // 4. Rozmnażanie
            reproductAnimals();
            try {
                Thread.sleep(this.moveDelay);
            }
            catch (InterruptedException e) {
                return;
            }
        }
    }

    private void removeDeadAnimals() {
//        this.animalsList.forEach(animal -> animal.removeObserver());
        this.animalsList.removeIf(animal -> Objects.equals(animal.getStatus(), AnimalStatus.DEAD));
    }

    private void simulateMove() {
        this.animalsList.forEach(Animal::makeMove);
    }

    private void feedAnimals() {
        this.map.feedAnimals();

    }

    public void reproductAnimals() {
        this.map.getAnimalContainers().forEach((currPos, currAnimalContainer) -> {
            Optional<Pair<Animal, Animal>> greatestPair = currAnimalContainer.getTwoAnimalsWithGreatestEnergy();
            greatestPair.ifPresent((animalPair) -> {
                Animal firstAnimal = animalPair.getKey();
                Animal secondAnimal = animalPair.getValue();
                if (canReproduct(firstAnimal, secondAnimal)) {
                    // create animal
                    Animal child = createNewAnimal(firstAnimal, secondAnimal);
                    this.map.place(child);
                }
            });
        });
    }

    private Boolean canReproduct(Animal firstAnimal, Animal secondAnimal) {
        return firstAnimal.getEnergy() >= this.simulationConfig.energyToCopulation()
                && secondAnimal.getEnergy() >= this.simulationConfig.energyToCopulation();
    }

    private Animal createNewAnimal(Animal firstAnimal, Animal secondAnimal) {
        float energyRatio = (float) firstAnimal.getEnergy() / (firstAnimal.getEnergy() + secondAnimal.getEnergy());
        int indexOfCut = (int) energyRatio * this.simulationConfig.lengthGenome();
        decreateParentEnergy(firstAnimal, secondAnimal);
        Genotype childGenotype = new Genotype(firstAnimal.getGenotype().cutLeftSide(indexOfCut), secondAnimal.getGenotype().cutRightSide(indexOfCut));
        // domyślne ustawienie to pełna predestynacja

        if (!this.simulationConfig.isPredistination()) {
            childGenotype.applyABitOfMadness();
        }
        if (!this.simulationConfig.isRandomness()) {
            childGenotype.applySmallCorrect();
        } else {
            childGenotype.applyFullyRandomness();
        }

        return new Animal(this.map, firstAnimal.getPosition(), 2 * this.simulationConfig.energyToCopulation(), childGenotype);
    }

    private void decreateParentEnergy(Animal firstParent, Animal secondParent) {
        firstParent.setEnergy(firstParent.getEnergy() - this.simulationConfig.energyToCopulation());
        secondParent.setEnergy(secondParent.getEnergy() - this.simulationConfig.energyToCopulation());
    }

}
