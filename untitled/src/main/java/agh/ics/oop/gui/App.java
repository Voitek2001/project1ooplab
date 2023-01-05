package agh.ics.oop.gui;
import agh.ics.oop.*;
import agh.ics.oop.AnimalTracker.AnimalStatisticTracker;
import agh.ics.oop.MapElements.Animal;
import agh.ics.oop.MapElements.Grass;
import agh.ics.oop.Simulation.*;
import agh.ics.oop.WorldMapComp.AbstractWorldMap;
import agh.ics.oop.WorldMapComp.AnimalContainer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import org.json.JSONObject;

public class App extends Application implements IRenderGridObserver{
    Stage stag;
    Stage primaryStage;
    private GridPane grid;
    private AbstractWorldMap worldMap;
    private JSONObject config;
    private SimulationEngine engine;
    private Thread threadToRunEngine;
    boolean paused = false;
    private int day;
    private  HashMap<Vector2d, AnimalContainer> animalContainer;
    private HashMap<Vector2d, Grass> grassMap;
    private List<Animal> animalList;
    private SimulationConfig simulationConfig;
    ChoiceBox mapVariant = new ChoiceBox(FXCollections.observableArrayList("Globe", "Magic Portal"));
    ChoiceBox growthVariant = new ChoiceBox(FXCollections.observableArrayList("Forested equators", "Toxic corpses"));
    ChoiceBox mutationVariant = new ChoiceBox(FXCollections.observableArrayList("Full randomness", "Slight correction"));
    ChoiceBox madnessVariant = new ChoiceBox(FXCollections.observableArrayList("Full predestination", "Madness"));
    private ArrayList<String> statsList = new ArrayList<>();
    private int sumAge = 0;
    private int death = 0;
    private int avg = 0;
    private AnimalStatisticTracker animalStats;
    @Override
    public void start(Stage primaryStage) throws IOException {
        grid = new GridPane();
        this.primaryStage = primaryStage;
        Scene configscene = menumap();
        String pathToConfig = "src/main/java/agh/ics/oop/config.json";
        String contents = new String((Files.readAllBytes(Paths.get(pathToConfig))));
        this.config = new JSONObject(contents).getJSONObject("ConfigurationFile");
        simulationConfig = simulationParameter();
        this.primaryStage.setScene(configscene);
        this.primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });

    }

    private int calcSizeOfBoxes(int lowerLeft, int upperRight, int lengthOfScene) {
        int howManyBoxes = upperRight - lowerLeft + 2;
        return lengthOfScene / howManyBoxes;

    }

    private void renderGrid() {
        grid = new GridPane();
        int height = 500;
        int width = 900;
        EventHandler<MouseEvent> eventHandler = event -> {
            double x = event.getX();
            double y = event.getY();
            double cellWidth =  0.6 * width / (simulationConfig.width() + 1);
            double cellHeight = 0.85 * (double) height / (simulationConfig.height() +1);
            int row = simulationConfig.height() +1 - (int) (y / cellHeight);
            int column = (int) (x / cellWidth) - 1;
            Vector2d vec = new Vector2d(column, row);
            StatsAnimal stat = new StatsAnimal(vec, animalContainer);
            if (paused) {
                try {
                   stat.start(new Stage());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
        grid.setOnMouseClicked(eventHandler);
        Pane leftPane = new Pane();
        VBox rightPane = rightPanelScene();
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(leftPane, rightPane);
        splitPane.setDividerPositions(0.7);
        this.grid.setGridLinesVisible(true);
        this.grid.setHgap(0);
        this.grid.setVgap(0);
        Vector2d lowerLeft = new Vector2d(0, 0);
        Vector2d upperRight = new Vector2d(simulationConfig.width(), simulationConfig.height());
        int verticalSize = calcSizeOfBoxes(lowerLeft.y(), upperRight.y(), height);
        int horizontalSize = calcSizeOfBoxes(lowerLeft.x(), upperRight.x(), (int) (width * 0.7));
        int lowerLeftX = lowerLeft.x();
        int lowerLeftY = lowerLeft.y();
        int upperRightX = upperRight.x();
        int upperRightY = upperRight.y();
        Label xyLabel = new Label("y\\n");
        GridPane.setHalignment(xyLabel, HPos.CENTER);
        grid.getColumnConstraints().add(new ColumnConstraints(horizontalSize));
        grid.getRowConstraints().add(new RowConstraints(verticalSize));
        grid.add(xyLabel, 0, 0, 1, 1);
        for (int i = lowerLeftY; i <= upperRightY; i++) {
            Label label = new Label(Integer.toString(i));
            grid.add(label, 0, upperRightY - i + 1, 1, 1);
            grid.getRowConstraints().add(new RowConstraints(verticalSize));
            GridPane.setHalignment(label, HPos.CENTER);
        }
        for (int i = lowerLeftX; i <= upperRightX; i++) {
            Label label = new Label(Integer.toString(i));
            grid.add(label, i - lowerLeftX + 1, 0, 1, 1);
            grid.getColumnConstraints().add(new ColumnConstraints(horizontalSize));
            GridPane.setHalignment(label, HPos.CENTER);
        }
        for (int x = lowerLeftX; x <= upperRightX; x++) {
            for (int y = lowerLeftY; y <= upperRightY; y++) {
                Vector2d position = new Vector2d(x, y);
                if (!animalContainer.containsKey(position)) {
                    continue;
                }
                AnimalContainer container = animalContainer.get(position);
                Optional<Animal> greatestEnergyAnimal = container.getGreatestEnergyAnimal();
                if (greatestEnergyAnimal.isPresent()) {
                    Animal animal = greatestEnergyAnimal.get();
                    GuiAnimalBox animalBox = new GuiAnimalBox(animal,this.config.getString("resourcesPath"));
                    VBox box = animalBox.getBox();
                    VBox.setMargin(box, new Insets(0, 0, 0, 0));
                    grid.add(box, position.x() - lowerLeftX + 1,
                            upperRightY - position.y() + 1, 1, 1);
                }
            }
        }

        for (int x = lowerLeftX; x <= upperRightX; x++) {
            for (int y = lowerLeftY; y <= upperRightY; y++) {
                Vector2d position = new Vector2d(x, y);
                worldMap.objectAt(position).ifPresent(
                        (value) -> {
                            GuiElementBox guiElementBox = new GuiElementBox(value, this.config.getString("resourcesPath"), 20, 20);
                            VBox guiElement = guiElementBox.getGUIElement();
                            GridPane.setHalignment(guiElement, HPos.CENTER);
                            if (!value.describePosition().equals("Grass")){VBox container = new VBox();
                                container.getChildren().addAll(guiElement);
                                grid.add(container,  position.x()- lowerLeftX + 1, upperRightY - position.y() + 1, 1, 1);}
                            else {grid.add(guiElement, position.x() - lowerLeftX + 1, upperRightY - position.y() + 1, 1, 1);

                            }
                        }
                );
            }
        }
        leftPane.getChildren().add(grid);
        stag.setOnCloseRequest(e -> {
            this.threadToRunEngine.suspend();
            stag.close();
        });

        Scene simScene = new Scene(splitPane,width,height);
        stag.setTitle("Simulation");
        stag.setScene(simScene);
        stag.show();

    }
    @Override
    public void renderNewGrid() {
        Platform.runLater(() -> {
            grid.getChildren().retainAll(grid.getChildren().get(0));
            renderGrid();
        });
    }
    private Scene menumap() {
        Button button = new Button("Start Simulation");
        Text text1 = new Text("Map variant");
        Text text2 = new Text("Growth variant");
        Text text3 = new Text("Mutation variant");
        Text text4 = new Text("Behavior variant");
        mapVariant.setValue(mapVariant.getItems().get(0));
        growthVariant.setValue(growthVariant.getItems().get(0));
        mutationVariant.setValue(mutationVariant.getItems().get(0));
        madnessVariant.setValue(madnessVariant.getItems().get(0));
        GridPane grid1 = new GridPane();
        grid1.addRow(0, text1);
        grid1.addRow(1, mapVariant);
        grid1.addRow(2, text2);
        grid1.addRow(3, growthVariant);
        grid1.addRow(4, text3);
        grid1.addRow(5, mutationVariant);
        grid1.addRow(6, text4);
        grid1.addRow(7, madnessVariant);
        grid1.addRow(8, button);
        text1.setStyle("-fx-font-weight: bold");
        text2.setStyle("-fx-font-weight: bold");
        text3.setStyle("-fx-font-weight: bold");
        text4.setStyle("-fx-font-weight: bold");
        button.setOnAction(event -> {
            stag = new Stage();
            this.engine = new SimulationEngine(simulationConfig, 1000);
            this.engine.addObserver(this);
            this.threadToRunEngine = new Thread(engine);
            this.worldMap = this.engine.getMap();
            this.paused = false;
            this.animalStats = this.engine.getAnimalStatTracker();
            this.day  = this.engine.getDays();
            this.animalList = this.engine.getAnimalsList();
            this.animalContainer = this.worldMap.getAnimalContainers();
            this.grassMap = this.worldMap.getGrassMap();
            this.renderGrid();
            this.threadToRunEngine.start();
        });
        return new Scene(grid1, 300, 300);
    }

    public SimulationConfig simulationParameter() {
        MapType mapType;
        AfforestationType afforestationType;
        Mutations mutations;
        Behavior behavior;
        if (mapVariant.getSelectionModel().getSelectedIndex() == 0) {
            mapType = MapType.GLOBE;
        } else {
            mapType = MapType.HELLPORTAL;
        }
        if (growthVariant.getSelectionModel().getSelectedIndex() == 0) {
            afforestationType = AfforestationType.FORESTEDEQUATORS;
        } else {
            afforestationType = AfforestationType.TOXICCORPSES;
        }
        if (mutationVariant.getSelectionModel().getSelectedIndex() == 0) {
            mutations = Mutations.TOTALYRANDOM;
        } else {
            mutations = Mutations.SLIGHTCORRECT;
        }
        if (madnessVariant.getSelectionModel().getSelectedIndex() == 0) {
            behavior = Behavior.TOTALPREDESTINATION;
        } else {
            behavior = Behavior.ABITOFMADNESS;
        }
        return new SimulationConfig(
                this.config.getInt("height"),
                this.config.getInt("width"),
                this.config.getInt("plantsStarted"),
                this.config.getInt("animalStarted"),
                this.config.getInt("plantEnergyProfit"),
                this.config.getInt("animalStartEnergy"),
                this.config.getInt("energyNecessary"),
                this.config.getInt("energyToCopulation"),
                this.config.getInt("minimumMutations"),
                this.config.getInt("maximumMutations"),
                this.config.getInt("lengthGenome"),
                this.config.getInt("everydayPlantCount"),
                mapType,
                afforestationType,
                mutations,
                behavior
        );
    }

    public VBox rightPanelScene(){
        Button button = new Button("Pause");
        Button buttonContinue = new Button("Continue");
        Button buttonCSV =  new Button("Save to CSV");

        button.setOnAction(event -> {
            if (!paused) {
                paused = true;
                this.threadToRunEngine.suspend();
                renderGrid();
            }
        });
        buttonContinue.setOnAction(event -> {
            paused = false;
            threadToRunEngine.resume();
        });
        buttonCSV.setOnAction(event -> {
            if (paused) {
                GenerateCSV genCSV = new GenerateCSV(statsList);
                genCSV.generateStats();

            }
        });


        StatsMap statsMap = new StatsMap(simulationConfig, animalList,
                grassMap, animalContainer, animalStats);
        statsMap.meanAge();
        sumAge += statsMap.getSumAge();
        death += statsMap.getDeathAnimal();
        if (death != 0){
            avg = sumAge/death;
        }
        Text textAnimalNumber = new Text("Animals: " +  animalList.size());
        Text textPlantsNumber = new Text("Plants: " + grassMap.size());
        Text textMeanEnergy = new Text("Mean Energy: " + statsMap.meanEnergy());
        Text textMostGenome = new Text("Most Genome: " + statsMap.mostGenotypeCode());
        Text textFreeField = new Text("Free field: " + statsMap.freeField());
        Text text10 = new Text("Mean life (day): " + avg);
        String[] stat = new String[]{String.valueOf(animalList.size()),
                String.valueOf(statsMap.meanEnergy()), String.valueOf(grassMap.size()),
                String.valueOf(avg)};
        statsList.add(Arrays.toString(stat));
        System.out.println(statsList);
        if (paused) {
            List<Vector2d> colourVector = statsMap.colorMostGenom();
            for (Vector2d vector : colourVector) {
                Pane container = new Pane();
                container.setStyle("-fx-background-color: rgba(255, 200, 200, 0.8);");
                grid.add(container, vector.x() + 1,
                        simulationConfig.height() - vector.y() + 1, 1, 1);
            }
        }
        VBox rightPane = new VBox();
        rightPane.getChildren().addAll(button,buttonContinue,buttonCSV, textAnimalNumber, textPlantsNumber,
                textMeanEnergy, textMostGenome, textFreeField, text10);
        return rightPane;
    }


}
