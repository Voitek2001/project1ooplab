package agh.ics.oop.WorldMapComp;

import agh.ics.oop.MapElements.Grass;
import agh.ics.oop.Simulation.SimulationConfig;
import agh.ics.oop.Vector2d;


import java.util.*;

public class GrassField extends AbstractWorldMap {

    private final int n;

    public GrassField(SimulationConfig simulationConfig) {
        super(simulationConfig);
        this.n = simulationConfig.plantsStarted();
        Integer[] randomCoordinates = generateDistRandomNumbers(n, (int) Math.sqrt(10 * n) * (int) Math.sqrt(10 * n));
        for (int i = 0; i < n; i++) {
            Vector2d pos = new Vector2d(randomCoordinates[i] / (int) Math.sqrt(10 * n), randomCoordinates[i] % (int) Math.sqrt(10 * n));
            Grass newGrass = new Grass(pos);
            grassMap.put(newGrass.getPosition(), newGrass);
        }

    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return false;
    }

    private Integer[] generateDistRandomNumbers(int count, int maxValue) {
        // if count > maxValue doesn't have sense to generate distinct number, raise error or return null
        Integer[] randomNum = new Integer[maxValue];
        Random rand = new Random();

        for (int i = 0; i < maxValue; i++) {
            randomNum[i] = i;
        }

        for (int i = 0; i < maxValue; i++) {
            int randomIndex = rand.nextInt(maxValue);
            int temp = randomNum[randomIndex];
            randomNum[randomIndex] = randomNum[i];
            randomNum[i] = temp;
        }
        return Arrays.copyOfRange(randomNum, 0, count);

    }

    private void findNewPositionForGrass(Vector2d oldPosition) {
        Optional<Vector2d> newGrassPos = generateNewNotOccupiedPosition(oldPosition);
        newGrassPos.ifPresent(// check if it is even possible to place grass on new place
                (newPos) -> {
                    this.grassMap.remove(oldPosition);
                    this.grassMap.put(newPos, new Grass(newPos));
                }
        );
    }


    private Optional<Vector2d> generateNewNotOccupiedPosition(Vector2d pos) {
        LinkedList<Vector2d> tmpList = new LinkedList<>();
        for (int i = 0; i < (int) Math.sqrt(10 * n); i++) {
            for (int j = 0; j < (int) Math.sqrt(10 * n); j++) {
                Vector2d newPos = new Vector2d(i, j);
                if (!isOccupied(newPos) && !newPos.equals(pos)) {
                    tmpList.add(newPos);
                }
            }
        }

        return Optional.ofNullable(tmpList.get(new Random().nextInt(tmpList.size()-1)));

    }

}