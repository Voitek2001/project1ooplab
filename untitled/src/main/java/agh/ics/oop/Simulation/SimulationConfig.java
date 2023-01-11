package agh.ics.oop.Simulation;


import agh.ics.oop.AfforestationType;
import agh.ics.oop.Behavior;
import agh.ics.oop.MapType;
import agh.ics.oop.Mutations;

public record SimulationConfig (
        int height,
        int width,
        int plantsStarted,
        int animalStarted,
        int plantEnergyProfit,
        int animalStartEnergy,
        int energyNecessary,
        int energyToCopulation,
        int minimumMutations,
        int maximumMuattions,
        int lengthGenome,
        int everydayPlantCount,
        MapType mapType,
        AfforestationType AfforestationType,
        Mutations mutations,
        Behavior behaviour
) {
}

