package agh.ics.oop.Simulation;


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
        boolean isGlobe,
        boolean isForestedEquators,
        boolean isRandomness,
        boolean isPredistination
) {
}

