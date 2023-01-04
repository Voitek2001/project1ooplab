package agh.ics.oop.MapElements;

public interface IChangeEnergyObserver {

    void energyChanged(Animal animal, int oldEnergy, int newEnergy);
}