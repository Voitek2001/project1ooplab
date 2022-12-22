package agh.ics.oop;

import java.util.NavigableSet;
import java.util.Optional;
import java.util.TreeSet;
import javafx.util.Pair;


public class AnimalContainer {

    NavigableSet<ElementOfAnimalsContainer> AnimalContainerAtCurrentPosition = new TreeSet<>(new AnimalsEnergyComparator());

    public void addNewAnimal(Animal animal) {
        ElementOfAnimalsContainer newSetElement = new ElementOfAnimalsContainer(animal.getEnergy(), animal);
        this.AnimalContainerAtCurrentPosition.add(newSetElement);
    }

    public void removeAnimal(Animal animal) {
        ElementOfAnimalsContainer setElement = new ElementOfAnimalsContainer(animal.getEnergy(), animal);
        this.AnimalContainerAtCurrentPosition.remove(setElement);
    }

    public Optional<Pair<Animal, Animal>> getTwoAnimalsWithGreatestEnergy() {
        if (this.AnimalContainerAtCurrentPosition.size() < 2) {
            return Optional.empty();
        }
        var animalContainerIterator = this.AnimalContainerAtCurrentPosition.descendingIterator();
        return Optional.of(new Pair<>(animalContainerIterator.next().animal(), animalContainerIterator.next().animal()));

    }

}
