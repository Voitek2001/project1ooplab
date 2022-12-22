package agh.ics.oop;

import java.util.Comparator;

public class AnimalsEnergyComparator implements Comparator<ElementOfAnimalsContainer> {

    @Override
    public int compare(ElementOfAnimalsContainer o1, ElementOfAnimalsContainer o2) {
        if (o1.animal().getId() == o2.animal().getId()) {
            return 0;
        }
        int diffEnergy = o1.animalEnergy() - o2.animalEnergy();
        if (diffEnergy == 0) {
            return o1.animal().getId() - o2.animal().getId(); // na pewno różne od 0 bo ID są unikatowe
        }
        return diffEnergy;
    }

}
