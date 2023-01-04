package agh.ics.oop.gui;

import agh.ics.oop.MapElements.Animal;
import agh.ics.oop.MapElements.AnimalStatus;
import agh.ics.oop.MoveDirection;
import agh.ics.oop.Vector2d;
import agh.ics.oop.WorldMapComp.AnimalContainer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;


public class StatsAnimal extends Application {
    private final GridPane gridPane = new GridPane();
    private final HashMap<Vector2d, AnimalContainer> animalContainer;
    private final Vector2d vec;
    public StatsAnimal(Vector2d vec, HashMap<Vector2d, AnimalContainer> animalContainer){
        this.vec = vec;
        this.animalContainer = animalContainer;

    }

    @Override
    public void start(Stage primaryStage){
        Scene scene = new Scene(gridPane, 300,
                300);

        if (isFreeField()){
            emptyField();
        }
        else{
            animalField();
        }
        primaryStage.setTitle("Stats Animals");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void animalField() {
        Animal animal = animalContainer.get(vec).getGreatestEnergyAnimal().get();
        Text text0 = new Text("ID Animal: " +  animal.getId());
        Text text1 = new Text("Genome: " + geneticCode(animal.getGenotype().getGenes()));
        Text text2 = new Text("Active genome: " +animal.getGenotype().getCurrentMove().value);
        Text text3 = new Text("Energy: "+ animal.getEnergy());
        Text text4 = new Text("Eaten plants : " + animal.getNoOfEatenGrass());
        Text text5 = new Text("Children: " + animal.getChildren());
        Text text6 = new Text("Age (days) :" + animal.getAge());
        Text text7 = new Text("Death day: " + whenDead(animal));
        gridPane.addRow(0,text0);
        gridPane.addRow(1, text1);
        gridPane.addRow(2, text2);
        gridPane.addRow(3, text3);
        gridPane.addRow(4, text4);
        gridPane.addRow(5, text5);
        gridPane.addRow(6, text6);
        gridPane.addRow(7, text7);
    }


    public StringBuilder geneticCode(List<MoveDirection> code){
        StringBuilder genCode = new StringBuilder("[ ");
        for (MoveDirection gen: code){
            genCode.append(gen.value).append(" ");
        }
        genCode.append("]");
        return genCode;
    }
    public void emptyField(){
        Text text1 = new Text("The field has no animal");
        gridPane.addRow(0, text1);
    }

    public String whenDead(Animal animal){
        if (!animal.getStatus().equals(AnimalStatus.DEAD))
            return animal.getStatus().toString();
        else{
            return Integer.toString(animal.getDeathDate()-animal.getBornDate());
        }
    }

    public boolean isFreeField(){
        if (!animalContainer.containsKey(vec)) {
            return true;
        }
        return animalContainer.get(vec).getGreatestEnergyAnimal().isEmpty();
    }
}
