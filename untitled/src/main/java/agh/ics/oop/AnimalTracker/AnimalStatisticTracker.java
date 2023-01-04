package agh.ics.oop.AnimalTracker;

import agh.ics.oop.MapElements.Animal;
import agh.ics.oop.MapElements.Genotype;
import agh.ics.oop.MapElements.IChangeEnergyObserver;
import agh.ics.oop.MapElements.ILifeObserver;

import java.util.*;

public class AnimalStatisticTracker implements IChangeEnergyObserver, ILifeObserver, IGrassObserver {
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

    /**
     * Funkcje animalBorn i animalDied implementują interface ILifeObserver
     * przy narodzeniu, jak i śmierci zwierzęta aktualizowane są statystki symulacji
     */
    @Override
    public void animalBorn(Animal animal) {
        addGenotypeFrequency(animal.getGenotype());
        this.energyOfALiveAnimals += animal.getEnergy();
        this.noOfAnimals++;
//        animal.addLifeObserver(this);
    }

    @Override
    public void animalDied(Animal animal) {

        this.energyOfALiveAnimals -= animal.getEnergy();
        this.noOfAnimals--;
        this.noOfDeadAnimals++;
        this.howLongLivedDeadAnimals += animal.getDeathDate() - animal.getBornDate();

    }

    /**
     * energyChanged implementuje inferface IEnergyChanged
     * przelicza statystyki zmiany energi
     */
    @Override
    public void energyChanged(Animal animal, int oldEnergy, int newEnergy) {
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

    /**
     * Funkcja wykorzystuje dwa zbiory. Pierwszy z nich to sortedMap.
     * SortedMap - trzyma posortowane klucze które mapowane są na HashSet który zawiera
     * Genomy które występują tyle razy ile wynosi wartość klucza
     * Przykład:
     *      2: Genotype1, Genotype2
     *      3: Genotype3, Genotype4, Genotype5
     *      Powyższy SortedMap informuje nas że Genotype1 posiadają/posiadały 2 zwierzęta
     *      Genotyp2 posiadają/posiadały 2 zwierzęta
     *      Natomiast Genotyp3, Genotype4, Genotype5 posiadają/posiadały po 3 zwierzęta
     * Natomiast gentypesHelper mapuje Genotypy na liczbe ile razy występują na mapie
     * pomaga to w czasie stały aktualizować SortedMap
     * @param genotype - obecny genotyp do dodania
     */
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
        HashSet<Genotype> newGenotypeSet;
        if (this.genotypesCount.containsKey(genotypeCount + 1)) {
            newGenotypeSet = this.genotypesCount.get(genotypeCount + 1);
        } else {
            newGenotypeSet = new HashSet<>();
        }
        currGenotypeSet.remove(genotype);
        newGenotypeSet.add(genotype);
        this.genotypesCount.put(genotypeCount, currGenotypeSet);
        this.genotypesCount.put(genotypeCount + 1, newGenotypeSet);
    }


}