package agh.ics.oop.AnimalTracker;

import agh.ics.oop.MapElements.Animal;
import agh.ics.oop.MapElements.Genotype;

import java.util.*;

public class AnimalStatisticTracker {
    private int noOfAnimals = 0;

    private int noOfDeadAnimals = 0;
    private int noOfGrass = 0;

    public int getNoOfAnimals() {
        return noOfAnimals;
    }

    private int noOfEmptyCells = 0;
    private final SortedMap<Integer, HashSet<Genotype>> genotypesCount = new TreeMap<>();
    private final HashMap<Genotype, Integer> gentypesHelper = new HashMap<>();
    private int energyOfALiveAnimals;
    private int howLongLivedDeadAnimals;

    public void animalBore(Animal animal) {
        addGenotypeFrequency(animal.getGenotype());
        this.energyOfALiveAnimals += animal.getEnergy();
        this.noOfAnimals++;
    }
    public void animalDied(Animal animal) {

        this.energyOfALiveAnimals -= animal.getEnergy();
        this.noOfAnimals--;
        this.noOfDeadAnimals++;
        this.howLongLivedDeadAnimals += animal.getDeathDate() - animal.getBornDate();

    }
    public void animalEnergyChanged(Animal animal, int oldEnergy, int newEnergy) {
        this.energyOfALiveAnimals += (newEnergy - oldEnergy);
    }

    public float getAverageEnergyOfAnimals() {
        return (float) this.energyOfALiveAnimals / this.noOfAnimals;
    }

    public float getAverageLifeTimeForDead() {
        return (float) this.howLongLivedDeadAnimals / this.noOfDeadAnimals;
    }

    public void grassEaten() {
        this.noOfGrass--;
    }

    public void grassGrow() {
        this.noOfGrass++;
    }

    public HashSet<Genotype> findMostPopularGenotype() {
        return this.genotypesCount.get(this.genotypesCount.lastKey());
    }

    public int getNoOfGrass() {
        return noOfGrass;
    }

    public int getNoOfEmptyCells() {
        return noOfEmptyCells;
    }

    public void setNoOfEmptyCells(int noOfEmptyCells) {
        this.noOfEmptyCells = noOfEmptyCells;
    }

    private void addGenotypeFrequency(Genotype genotype) {
        if (!gentypesHelper.containsKey(genotype)) {
            gentypesHelper.put(genotype, 1);
            HashSet<Genotype> currGenotypeSet = new HashSet<>();
            currGenotypeSet.add(genotype);
            genotypesCount.put(1, currGenotypeSet);
            return;
        }
        Integer genotypeCount = gentypesHelper.get(genotype);
        HashSet<Genotype> currGenotypeSet = this.genotypesCount.get(genotypeCount);
        HashSet<Genotype> newGenotypeSet = this.genotypesCount.get(genotypeCount + 1);
        currGenotypeSet.remove(genotype);
        newGenotypeSet.add(genotype);
        this.genotypesCount.put(genotypeCount, currGenotypeSet);
        this.genotypesCount.put(genotypeCount + 1, newGenotypeSet);
    }

}
