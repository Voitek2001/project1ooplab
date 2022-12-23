//package agh.ics.oop;
//
//import javafx.application.Application;
//import javafx.collections.FXCollections;
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.ChoiceBox;
//import javafx.scene.control.Label;
//import javafx.scene.control.TextField;
//import javafx.scene.layout.GridPane;
//import javafx.scene.text.Text;
//import javafx.stage.Stage;
//
//public class Menu extends Application{
//    private GridPane gridPane = new GridPane();
//    Button button = new Button("Start Simulation");
//    Label mapHeightText = new Label("Map height");
//    Label mapWidthText = new Label("Map width");
//  //  Label jungleHeightText = new Label("Jungle height");
//   // Label jungleWidthText = new Label("Jungle width");
//    Label plantEnergyProfitText = new Label("Plant energy profit");
//    Label EnergyToCopulationText = new Label("Energy to copulation");
//    Label animalStartEnergyText = new Label("Start energy");
//    Label energyNecessaryText = new Label("Energy necessary");
//    Label minimumMutationsText = new Label("Minimum mutation");
//    Label plantsStartText = new Label("Starting number of plants");
//    Label animalsStartText = new Label("Starting number of animals");
//    Label maximumMutationsText = new Label("Maximum mutation");
//    Label plantsEachDayText = new Label("Number of plants each day");
//    Label genomLengthText = new Label("Genome length");
//
//    TextField mapHeight = new TextField("400");
//    TextField mapWidth = new TextField("400");
// //   TextField jungleHeight= new TextField("50");
//  //  TextField jungleWidth = new TextField("50");
//    TextField plantEnergyProfit = new TextField("20");
//    TextField EnergyToCopulation = new TextField("50");
//    TextField animalStartEnergy = new TextField("220");
//    TextField energyNecessary = new TextField("500");
//    TextField minimumMutations = new TextField("0");
//    TextField plantsStart = new TextField("10");
//    TextField animalsStart = new TextField("10");
//    TextField maximumMutations = new TextField("10");
//    TextField plantsEachDay = new TextField("10");
//    TextField genomLength = new TextField("10");
//    Text text1 = new Text("Map properties");
//    Text text2 = new Text("Energy Properties");
//    Text text3 = new Text("Map variant");
//    Text text4 = new Text("Growth variant");
//    Text text5 = new Text("Mutation variant");
//    Text text6 = new Text("Behavior variant");
//    ChoiceBox mapVariant = new ChoiceBox(FXCollections.observableArrayList("Globe", "Magic Portal"));
//    ChoiceBox growthVariant = new ChoiceBox(FXCollections.observableArrayList("Forested equators", "Toxic corpses"));
//    ChoiceBox mutationVariant = new ChoiceBox(FXCollections.observableArrayList("Full randomness", "Slight correction"));
//    ChoiceBox madnessVariant = new ChoiceBox(FXCollections.observableArrayList("Full predestination", "Madness"));
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        gridPane.setAlignment(Pos.CENTER);
//        gridPane.addRow(0,text1);
//        gridPane.addRow(1,mapHeightText, mapHeight);
//        gridPane.addRow(2,mapWidthText, mapWidth);
//        gridPane.addRow(3, plantsEachDayText, plantsEachDay);
//        gridPane.addRow(4, plantsStartText, plantsStart);
//   //     gridPane.addRow(3,jungleHeightText, jungleHeight);
//     //   gridPane.addRow(4,jungleWidthText, jungleWidth);
//        gridPane.addRow(5,animalsStartText,animalsStart);
//        gridPane.addRow(6,plantEnergyProfitText, plantEnergyProfit);
//        gridPane.addRow(7,EnergyToCopulationText, EnergyToCopulation);
//        gridPane.addRow(8, animalStartEnergyText, animalStartEnergy);
//        gridPane.addRow(9, energyNecessaryText, energyNecessary);
//        gridPane.addRow(10, minimumMutationsText, minimumMutations);
//        gridPane.addRow(11, maximumMutationsText, maximumMutations);
//        gridPane.addRow(12, genomLengthText, genomLength);
//        gridPane.addRow(13, text3);
//        gridPane.addRow(14,mapVariant);
//        gridPane.addRow(15, text4);
//        gridPane.addRow(16,growthVariant);
//        gridPane.addRow(17, text5);
//        gridPane.addRow(18,mutationVariant);
//        gridPane.addRow(19, text6);
//        gridPane.addRow(20,madnessVariant);
//        gridPane.addRow(21,button);
//        text1.setStyle("-fx-font-weight: bold");
//        text2.setStyle("-fx-font-weight: bold");
//        text3.setStyle("-fx-font-weight: bold");
//        text4.setStyle("-fx-font-weight: bold");
//        text5.setStyle("-fx-font-weight: bold");
//        text6.setStyle("-fx-font-weight: bold");
//        button.setOnAction(event -> {
//            try {
//                App app = new App(simulationParameter());
//                app.start(new Stage());
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        });
//        Scene scene = new Scene(gridPane, 500, 600);
//        primaryStage.setTitle("Evolution simulator settings");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//        }
//
//
//    public SimulationConfig simulationParameter(){
//        return new SimulationConfig(
//                Integer.parseInt(mapHeight.getText()),
//                Integer.parseInt(mapWidth.getText()),
//                Integer.parseInt(plantsStart.getText()),
//                Integer.parseInt(animalsStart.getText()),
//                Integer.parseInt(plantEnergyProfit.getText()),
//                Integer.parseInt(animalStartEnergy.getText()),
//                Integer.parseInt(energyNecessary.getText()),
//                Integer.parseInt(EnergyToCopulation.getText()),
//                Integer.parseInt(minimumMutations.getText()),
//                Integer.parseInt(minimumMutations.getText()),
//                Integer.parseInt(genomLength.getText()),
//                mapVariant.getSelectionModel().getSelectedIndex()==0,
//                growthVariant.getSelectionModel().getSelectedIndex()==0,
//                mutationVariant.getSelectionModel().getSelectedIndex()==0,
//                madnessVariant.getSelectionModel().getSelectedIndex()==0
//        );
//    }
//        /*
//    public void simulationParameter(SimulationConfig simconfig){
//        simconfig.setHeight(Integer.parseInt(mapHeight.getText()));
//        simconfig.setWidth(Integer.parseInt(mapWidth.getText()));
//        simconfig.setIsGlobe(mapVariant.getSelectionModel().getSelectedIndex()==0);
//        simconfig.setIsToxicCorpses(growthVariant.getSelectionModel().getSelectedIndex()==1);
//        simconfig.setIsRandomness(mutationVariant.getSelectionModel().getSelectedIndex()==0);
//        simconfig.setIsMadness(madnessVariant.getSelectionModel().getSelectedIndex()==1);
//        simconfig.setAnimalStartEnergy(Integer.parseInt(animalStartEnergy.getText()));
//        simconfig.setEnergyNecessary(Integer.parseInt(energyNecessary.getText()));
//        simconfig.setPlantEnergyProfit(Integer.parseInt(plantEnergyProfit.getText()));
//        simconfig.setPlantsStarted(Integer.parseInt(plantsStart.getText()));
//        simconfig.setAnimalStarted(Integer.parseInt(animalsStart.getText()));
//        simconfig.setEnergyToCopulation(Integer.parseInt(EnergyToCopulation.getText()));
//        simconfig.setPlantEachDay(Integer.parseInt(plantsEachDay.getText()));
//        simconfig.setLengthGenome(Integer.parseInt(genomLength.getText()));
//        simconfig.setMaximumMuattions(Integer.parseInt(maximumMutations.getText()));
//        simconfig.setMinimumMutations(Integer.parseInt(minimumMutations.getText()));
//    }
//    */
//
//}
