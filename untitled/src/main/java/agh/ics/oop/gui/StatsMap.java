package agh.ics.oop.gui;

import agh.ics.oop.MapElements.Animal;
import agh.ics.oop.MapElements.AnimalStatus;
import agh.ics.oop.MapElements.Grass;
import agh.ics.oop.MoveDirection;
import agh.ics.oop.Simulation.SimulationConfig;
import agh.ics.oop.Vector2d;
import agh.ics.oop.WorldMapComp.AnimalContainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class StatsMap {

    private final SimulationConfig simconfig;
    private final List<Animal> animalList;
    private final HashMap<Vector2d, Grass> grassMap;
    private final HashMap<Vector2d, AnimalContainer> animalContainer;

    private int sumAge =0;
    private int deathAnimal = 0;
    public StatsMap(SimulationConfig simconfig, List<Animal> animalList, HashMap<Vector2d, Grass> grassMap,
                    HashMap<Vector2d, AnimalContainer> animalContainer){
        this.simconfig = simconfig;
        this.animalList = animalList;
        this.grassMap = grassMap;
        this.animalContainer = animalContainer;
    }

    public double meanEnergy() {
        int sum = 0;
        for (Animal animal : animalList) {
            sum += animal.getEnergy();
        }
        return Math.round((double) sum / animalList.size());
    }


    public MoveDirection mostGenom() {
        int[] counter = new int[8];
        Arrays.fill(counter, 0);
        for (Animal animal : animalList) {
            counter[animal.getGenotype().getCurrentMove().value] +=1;
        }
        int max = counter[0];
        int maxIndex = 0;

        for (int i = 1; i < counter.length; i++) {
            if (counter[i] > max) {
                max = counter[i];
                maxIndex = i;
            }
        }
        return MoveDirection.getByValue(maxIndex).get();
    }

    public List<Vector2d> colorMostGenom(MoveDirection mostGen){
        List<Vector2d> colorVectors = new ArrayList<>();
        for (Animal animal : animalList) {
            if (animal.getGenotype().getCurrentMove().equals(mostGen)){
                Vector2d vec = animal.getPosition();
                colorVectors.add(vec);
            }
        }
        return colorVectors;
    }

public int freeField(){
    int freeFieldsCount = 0;
    for (int x = 0; x <= simconfig.width(); x++) {
        for (int y = 0; y <= simconfig.height(); y++) {
            Vector2d vec = new Vector2d(x, y);
            if (isPositionFree(vec)) {
                freeFieldsCount++;
            }
        }
    }
    return freeFieldsCount;
}

    public boolean isPositionFree(Vector2d position) {
        for (Animal animal : animalList) {
            if (animal.getPosition().equals(position)) {
                return false;
            }
        }
        return !grassMap.containsKey(position);
    }

    public void meanAge(){
        for (Animal animal : animalList) {
            if (animal.getStatus().equals(AnimalStatus.DEAD)) {
                deathAnimal++;
                sumAge+=animal.getAge();
            }
        }
    }
    public int getSumAge() {
        return sumAge;
    }
    public int getDeathAnimal() {
        return deathAnimal;
    }

}
