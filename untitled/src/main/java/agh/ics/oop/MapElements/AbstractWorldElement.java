package agh.ics.oop.MapElements;

import agh.ics.oop.AnimalTracker.IGrassObserver;
import agh.ics.oop.WorldMapComp.IWorldMap;
import agh.ics.oop.Vector2d;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractWorldElement implements IMapElement {

    protected Vector2d position;
    protected IWorldMap map;
    protected List<IPositionChangeObserver> observers = new LinkedList<>();
    protected List<IChangeEnergyObserver> energyObservers = new LinkedList<>();

    protected List<ILifeObserver> lifeObservers = new LinkedList<>();
    protected List<IGrassObserver> grassObservers = new LinkedList<>();

    public Vector2d getPosition() {
        return position;
    }
    public void addObserver(IPositionChangeObserver observer) {
        observers.add(observer);
    }
    public void removeObserver(IPositionChangeObserver observer) {
        observers.remove(observer);
    }

    public void addEnergyObserver(IChangeEnergyObserver observer) {
        this.energyObservers.add(observer);
    }
    public void removeEnergyObserver(IChangeEnergyObserver observer) {
        this.energyObservers.remove(observer);
    }

    public void addLifeObserver(ILifeObserver observer) {
        this.lifeObservers.add(observer);
    }

    public void removeLifeObserver(ILifeObserver observer) {
        this.lifeObservers.remove(observer);
    }
    public void addGrassObserver(IGrassObserver observer) {
        this.grassObservers.add(observer);
    }
    public void removeGrassObserver(IGrassObserver observer) {
        this.grassObservers.remove(observer);
    }
}