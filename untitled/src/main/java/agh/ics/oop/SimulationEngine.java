package agh.ics.oop;

import agh.ics.oop.gui.IRenderGridObserver;
import javafx.util.Pair;

import java.util.*;


public class SimulationEngine implements IEngine {

    private final AbstractWorldMap map;
    private final List<Animal> animalsList = new ArrayList<>();
    private final List<IRenderGridObserver> renderGridobservers = new ArrayList<>();
    private final int moveDelay;

    public SimulationEngine(AbstractWorldMap map, EvolutionType evolutionType, int numberOfInitAnimals, int moveDelay) throws IllegalArgumentException {

        this.map = map;
        int initEnergy = 50;
        this.moveDelay = moveDelay;
        Bounds bounds = map.getBounds();
        int height = bounds.upperRight().y() - bounds.lowerLeft().y();
        int width = bounds.upperRight().x() - bounds.lowerLeft().x();
        Integer[] randomStartingPositions = RandomGenerator.generateDistRandomNumbers(numberOfInitAnimals, height * width);
        for (int i = 0; i < numberOfInitAnimals; i++) {
            Vector2d newPosition = new Vector2d(randomStartingPositions[i] % width, randomStartingPositions[i] / height);
            Animal animal = new Animal(map, newPosition, initEnergy);
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
            reproductAnimals(20, 10);
            try {
                Thread.sleep(this.moveDelay);
            }
            catch (InterruptedException e) {
                return;
            }

        }
    }

    private void removeDeadAnimals() {
        this.animalsList.removeIf(animal -> Objects.equals(animal.getStatus(), AnimalStatus.DEAD));

    }

    private void simulateMove() {
        this.animalsList.forEach(Animal::makeMove);
    }

    private void feedAnimals() {
        this.map.feedAnimals();

    }

    public void reproductAnimals(int minimumEnergyToReproduction, int lengthOfGenotype) {
        this.map.getAnimalContainers().forEach((currPos, currAnimalContainer) -> {
            Optional<Pair<Animal, Animal>> greatestPair = currAnimalContainer.getTwoAnimalsWithGreatestEnergy();
            greatestPair.ifPresent((animalPair) -> {
                Animal firstAnimal = animalPair.getKey();
                Animal secondAnimal = animalPair.getValue();
                if (canReproduct(firstAnimal, secondAnimal, minimumEnergyToReproduction)) {
                    // create animal
                    Animal child = createNewAnimal(firstAnimal, secondAnimal, lengthOfGenotype);
                    this.map.place(child);
                }

            });
        });
    }

    private Boolean canReproduct(Animal firstAnimal, Animal secondAnimal, int minimumEnergyToReproduction) {
        return firstAnimal.getEnergy() >= minimumEnergyToReproduction
                && secondAnimal.getEnergy() >= minimumEnergyToReproduction;
    }

    private Genotype mutateGenotype(Genotype genotype) {
        // if lekka korekta
        int length = genotype.getGenes().size();
        int[] operations = new int[length];
        Random rand = new Random();

        for (int i = 0; i < length; i++) {
            //TODO:change hardcoded bound
            operations[i] = rand.nextInt(1);

        }


        return genotype;

    }

    private Animal createNewAnimal(Animal firstAnimal, Animal secondAnimal, int lengthOfGenotype) {
        float energyRatio = (float) firstAnimal.getEnergy() / (firstAnimal.getEnergy() + secondAnimal.getEnergy());
        int EnergyToReproductChild = 20;
        int indexOfCut = (int) energyRatio * lengthOfGenotype;
        Genotype childGenotype = new Genotype(firstAnimal.getGenotype().cutLeftSide(indexOfCut), secondAnimal.getGenotype().cutRightSide(indexOfCut));
        Genotype mutatedChildGenotype = mutateGenotype(childGenotype);
        firstAnimal.setEnergy(firstAnimal.getEnergy() - EnergyToReproductChild);
        secondAnimal.setEnergy(secondAnimal.getEnergy() - EnergyToReproductChild);
        return new Animal(this.map, firstAnimal.getPosition(), 2 * EnergyToReproductChild, mutatedChildGenotype);

    }

}
