package agh.ics.oop.Simulation;


import agh.ics.oop.WorldMapComp.AbstractWorldMap;
import agh.ics.oop.MapElements.Animal;
import agh.ics.oop.MoveDirection;
import agh.ics.oop.Vector2d;
import agh.ics.oop.gui.IRenderGridObserver;

import java.util.ArrayList;
import java.util.LinkedList;

public class SimulationEngineWithThread implements IEngine, Runnable {
    private final int moveDelay;
    private MoveDirection[] instructions;
    private final ArrayList<Animal> animalsPos = new ArrayList<>();

    private LinkedList<IRenderGridObserver> threadEngineObservers = new LinkedList<>();

    public ArrayList<Animal> getAnimalsPos() {
        return animalsPos;
    }

    public SimulationEngineWithThread(AbstractWorldMap map, Vector2d[] animalsInitPos, int moveDelay) {
        this.moveDelay = moveDelay;
        for (Vector2d pos : animalsInitPos) {
            Animal currAnimal = new Animal(map, pos);
            map.place(currAnimal);
            this.animalsPos.add(currAnimal);
        }
    }

    // zwierze zmieniło pozycje powiadom o tym wszystkich obserwatorów silnika

    void renderNewGrid() {
        for (IRenderGridObserver observer : this.threadEngineObservers) {
            observer.renderNewGrid();
        }
    }

    public void addObserver(IRenderGridObserver observer) {
        this.threadEngineObservers.add(observer);
    }

    public void removeObserver(IRenderGridObserver observer) {
        this.threadEngineObservers.remove(observer);
    }

    public void setNewMoves(MoveDirection[] moves) {
        this.instructions = moves;
    }

    @Override
    public void run() {
        try {
            int num_of_operation = this.instructions.length;
            int num_of_valid_animals = this.animalsPos.size();
            for (int i = 0; i < num_of_operation; i++) {
                this.animalsPos.get(i % num_of_valid_animals).makeMove();
                this.renderNewGrid();
                Thread.sleep(this.moveDelay);
            }
        } catch(InterruptedException e){
            throw new RuntimeException(e);
        }
    }
}
