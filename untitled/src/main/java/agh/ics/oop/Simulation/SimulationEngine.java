package agh.ics.oop.Simulation;

import agh.ics.oop.*;
import agh.ics.oop.MapElements.Animal;
import agh.ics.oop.MapElements.AnimalStatus;
import agh.ics.oop.MapElements.Genotype;
import agh.ics.oop.MapElements.Grass;
import agh.ics.oop.WorldMapComp.AbstractWorldMap;
import agh.ics.oop.WorldMapComp.AnimalContainer;
import agh.ics.oop.WorldMapComp.GrassField;
import agh.ics.oop.gui.IRenderGridObserver;
import javafx.util.Pair;

import java.util.*;


public class SimulationEngine implements IEngine {

    private final AbstractWorldMap map;

    public List<Animal> getAnimalsList() {
        return animalsList;
    }

    private final List<Animal> animalsList = new ArrayList<>();
    private final List<IRenderGridObserver> renderGridobservers = new ArrayList<>();
    private final int moveDelay;
    private final SimulationConfig simulationConfig;

    public SimulationEngine(SimulationConfig simulationConfig, int moveDelay) throws IllegalArgumentException {

        this.map = new GrassField(simulationConfig);

        this.simulationConfig = simulationConfig;
        this.moveDelay = moveDelay;
        int height = simulationConfig.height();
        int width = simulationConfig.width();
        MoveDirection[] directions = MoveDirection.values();
        Integer[] randomStartingPositions = RandomGenerator.generateDistRandomNumbers(simulationConfig.animalStarted(), height * width);
        for (int i = 0; i < simulationConfig.animalStarted(); i++) {
            Vector2d newPosition = new Vector2d(randomStartingPositions[i] % width, randomStartingPositions[i] / width);
            List<MoveDirection> newGen = new ArrayList<>(simulationConfig.lengthGenome());
            for(int j = 0; j < simulationConfig.lengthGenome(); j++) {
                Random rand = new Random();
                newGen.add(directions[rand.nextInt(directions.length)]);
            }
            Animal animal = new Animal(map, newPosition, this.simulationConfig.animalStartEnergy(), new Genotype(newGen));
//            Animal todeleteanimal = new Animal(map, newPosition, this.simulationConfig.animalStartEnergy(), new Genotype(newGen));
//            map.place(todeleteanimal);
//            this.animalsList.add(todeleteanimal);
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
            // 5. Porost nowych roślin
            growNewGrasses();

            printChangesForDebug();
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
        Map<Vector2d, AnimalContainer> animCont = this.map.getAnimalContainers();
        this.animalsList
                .stream()
                .filter((animal -> animal.getStatus().equals(AnimalStatus.DEAD)))
                .forEach((animal) -> {
                    animCont.get(animal.getPosition()).removeAnimal(animal);
                    this.map.addDeadAtPosition(animal.getPosition());
                });

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
                && secondAnimal.getEnergy() >= this.simulationConfig.energyToCopulation()
                && firstAnimal.getEnergy() - this.simulationConfig.energyToCopulation() > this.simulationConfig.energyNecessary()
                && secondAnimal.getEnergy() - this.simulationConfig.energyToCopulation() > this.simulationConfig.energyNecessary();
    }

    private Animal createNewAnimal(Animal firstAnimal, Animal secondAnimal) {
        float energyRatio = (float) firstAnimal.getEnergy() / (firstAnimal.getEnergy() + secondAnimal.getEnergy());
        int indexOfCut = (int) energyRatio * this.simulationConfig.lengthGenome();
        decreateParentEnergy(firstAnimal, secondAnimal);
        Genotype childGenotype = new Genotype(firstAnimal.getGenotype().cutLeftSide(indexOfCut), secondAnimal.getGenotype().cutRightSide(indexOfCut));
        // domyślne ustawienie to pełna predestynacja

        if (this.simulationConfig.behaviour().equals(Behavior.ABITOFMADNESS)) {
            childGenotype.applyABitOfMadness();
        }
        if (this.simulationConfig.mutations().equals(Mutations.SLIGHTCORRECT)) {
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

    private void growNewGrasses() {
        this.map.generateNewGrasses();
    }

    private void printChangesForDebug() {
        Map<Vector2d, Grass> grassMap = this.map.getGrassMap();
        System.out.println("Wszystkie zwierzata po obecnej iteracji");
        for(Animal animal : this.animalsList) {
            System.out.println(animal);
        }
        System.out.println("Wszystkie rośliny po obecnej iteracji");
        for (Map.Entry<Vector2d, Grass> entry : grassMap.entrySet()) {
            System.out.println(entry);
        }
    }

}
