package agh.ics.oop.gui;


import agh.ics.oop.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.util.Pair;
import org.json.JSONObject;

public class App extends Application implements IRenderGridObserver {

    private GridPane grid;
    private AbstractWorldMap worldMap;
    private JSONObject config;
    private SimulationEngineWithThread engine;
    private Thread threadToRunEngine;

    @Override
    public void init() throws Exception {
        super.init();
        try {
            GrassField testMap = new GrassField(10);
            Animal testAnimal = new Animal(testMap, new Vector2d(2, 2), 50);
            Animal testAnimal2 = new Animal(testMap, new Vector2d(2, 2), 70);
            Animal testAnimal3 = new Animal(testMap, new Vector2d(2, 2), 100);
            Animal testAnimal4 = new Animal(testMap, new Vector2d(2, 2), 30);
            AnimalContainer newcont = new AnimalContainer();
            newcont.addNewAnimal(testAnimal);
            newcont.addNewAnimal(testAnimal2);
            newcont.addNewAnimal(testAnimal3);
            newcont.addNewAnimal(testAnimal4);
            Optional<Pair<Animal, Animal>> jd = newcont.getTwoAnimalsWithGreatestEnergy();
            System.out.println(jd.get().getKey().describePosition());
            System.out.println(jd.get().getKey().getEnergy());
            System.out.println(jd.get().getValue().describePosition());
            System.out.println(jd.get().getValue().getEnergy());

            List<MapDirection> a = new ArrayList<>();
            a.add(MapDirection.NORTH);
            a.add(MapDirection.SOUTH);
            Genotype gen = new Genotype(a);
            gen.applySmallCorrectVariant();


            System.out.println("tu");
            //lab7
            System.out.println("LAB7");
            //test JSON
            String pathToConfig = "src/main/java/agh/ics/oop/config.json";
            String contents = new String((Files.readAllBytes(Paths.get(pathToConfig))));
            this.config = new JSONObject(contents).getJSONObject("ConfigurationFile");


            this.worldMap = new GrassField(10);
            Vector2d[] positions = { new Vector2d(2,2), new Vector2d(3,4) };
            this.engine = new SimulationEngineWithThread(this.worldMap, positions, 3000);
            this.engine.addObserver(this);
            this.grid = new GridPane();

            System.out.println(this.worldMap);


//            JSONObject configFile = o.getJSONObject("ConfigurationFile");
//            this.config = o.getJSONObject("ConfigurationFile");
            System.out.println(this.config.getString("resourcesPath"));

        }
        catch (IllegalArgumentException | IOException ex) {
            ex.printStackTrace(System.out);
        }
    }

    @Override
    public void start(Stage primaryStage) {



        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
        grid.setGridLinesVisible(true);
        grid.setHgap(0);
        grid.setVgap(0);
        Button button = new Button("Run");
        button.setMinWidth(20);
        button.setMinHeight(20);
        VBox root = new VBox();
        TextField textField = new TextField();

        button.setOnAction(event -> {
            String[] instructions = textField.getCharacters().toString().split(" ");
            MoveDirection[] directions  = OptionsParser.parse(instructions);
            this.engine.setNewMoves(directions);
            this.threadToRunEngine = new Thread(this.engine);
            this.threadToRunEngine.start();
        });
        root.getChildren().addAll(textField, button);
        root.getChildren().add(grid);

        renderGrid();

        Scene scene = new Scene(root, this.config.getInt("widthOfScene"), this.config.getInt("heightOfScene"));
        primaryStage.setTitle("Zwierzak!");
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    private int calcSizeOfBoxes(int lowerLeft, int upperRight, int lengthOfScene) {
        int howManyBoxes = upperRight - lowerLeft + 2;
        return lengthOfScene / howManyBoxes;

    }
    void renderGrid() {
        this.grid.setGridLinesVisible(true);
        this.grid.setHgap(0);
        this.grid.setVgap(0);
        Bounds bounds = this.worldMap.getBounds();
        int height = this.config.getInt("heightOfScene") - 2 * this.config.getInt("heightOfButton");
        int width = this.config.getInt("widthOfScene");
        Vector2d lowerLeft = bounds.lowerLeft();
        Vector2d upperRight = bounds.upperRight();
        int verticalSize = calcSizeOfBoxes(lowerLeft.y(), upperRight.y(), height);
        int horizontalSize = calcSizeOfBoxes(lowerLeft.x(), upperRight.x(), width);
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
                int finalX = x;
                int finalY = y;
                worldMap.objectAt(position).ifPresent(
                            (value) -> {
                                GuiElementBox guiElementBox = new GuiElementBox(value, this.config.getString("resourcesPath"), 20, 20);
                                VBox guiElement = guiElementBox.getGUIElement();
                                GridPane.setHalignment(guiElement, HPos.CENTER);
                                grid.add(guiElement, finalX - lowerLeftX + 1, upperRightY - finalY + 1, 1, 1);
//                                Label label = new Label(value.toString());
//                                grid.add(label, finalX - lowerLeftX + 1, upperRightY - finalY + 1, 1, 1);
                            }
                    );

                }
            }
        }
        @Override
        public void renderNewGrid() {
            Platform.runLater(() -> {
                grid.getChildren().retainAll(grid.getChildren().get(0));
                this.renderGrid();
            });
        }

    }

