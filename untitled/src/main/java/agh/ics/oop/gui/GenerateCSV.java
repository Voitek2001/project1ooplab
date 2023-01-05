package agh.ics.oop.gui;

import agh.ics.oop.Simulation.SimulationConfig;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GenerateCSV{
    private final String[] header = {"Animals", "Mean Energy", "Grasses", "Mean Life"};
    private final String[][] statsArray;
    public GenerateCSV(ArrayList<String> statsList){
        statsArray = statsList.stream()
                .map(s -> s.split(","))
                .toArray(String[][]::new);

    }
    public void generateStats(){
        String fileName = "stats.csv";
        try (FileWriter writer = new FileWriter(fileName)) {
            for (String column : header) {
                writer.append(column);
                writer.append(",");
            }
            writer.append("\n");

            for (String[] row : statsArray) {
                for (String element : row) {
                    writer.write(element + ",");
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

