// By B15
package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import controller.service.SeedSearchService;
import controller.service.SimulatedAnnealingService;
import controller.service.SimulatedAnnealingServiceTEST;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Communicator;
import model.DataStock;
import model.data.Graph;
import model.data.Util;
import view.ViewFactory;

import java.io.*;
import java.net.URL;
import java.util.*;

import static model.DataStock.*;

public class MainWindowController extends BaseController implements Initializable {
    @FXML
    private JFXButton bLoad;

    @FXML
    private JFXButton bStop;

    @FXML
    private JFXButton bPlay;

    @FXML
    private Label lCities;

    @FXML
    private JFXTextField tfIniTemp;

    @FXML
    private JFXTextField tfCR;

    @FXML
    private JFXTextField tfL;

    @FXML
    private Label lIT;

    @FXML
    private Label lTP;

    @FXML
    private Label lElapsedTime;

    @FXML
    private Label lCT;

    @FXML
    private Group gCdraw;

    @FXML
    private Group gLdraw;

    @FXML
    private ProgressBar pbTemp;

    @FXML
    private JFXTextField tfSeed;

    @FXML
    private JFXButton bFSeed;

    @FXML
    private JFXTextField tfNT;

    @FXML
    private ChoiceBox<Graph> chbGraphs;

    @FXML
    private JFXRadioButton rS;

    @FXML
    private ToggleGroup swapMG;

    @FXML
    private JFXRadioButton aS;

    @FXML
    private JFXRadioButton rRSL;

    @FXML
    private JFXButton bRefresh;

    @FXML
    private JFXSpinner psFS;


    SimulatedAnnealingService sAService;

    public MainWindowController(Communicator communicator, ViewFactory viewFactory, String fxmlName) {
        super(communicator, viewFactory, fxmlName);
    }

    @FXML
    void refreshSeed(ActionEvent event) {
        tfSeed.setText(String.valueOf(System.currentTimeMillis()));
    }

    @FXML
    void findBestSeed(ActionEvent event) {
        int i=0;
        int n=10;
        if (!tfNT.getText().isEmpty())n=Integer.parseInt(tfNT.getText());
        while (i<n){
            SeedSearchService seedSearchService = new SeedSearchService(getMethode());
            seedSearchService.start();
            int finalI = i;
            seedSearchService.setOnSucceeded(event1 -> {
                scores.add(seedSearchService.getValue());
                System.out.println("service num : "+ finalI +" done !\nTour = "+seedSearchService.getValue().getDistance()+"\nSeed = "+seedSearchService.getValue().getSeed());
            });
            i++;
        }
        if(bestScore==null) {
            bestScore = Util.getBestScore(scores);
            seed=bestScore.getSeed();
            tfSeed.setText(String.valueOf(seed));
        }
        if (bestScore.getDistance()>Util.getBestScore(scores).getDistance()){
                bestScore=Util.getBestScore(scores);
                seed=bestScore.getSeed();
                tfSeed.setText(String.valueOf(seed));
        }
    }
    void loadAction(){
        cities=chbGraphs.getValue().getCityList();
        DataStock.lITUpdater.set("Initial Tour :");
        DataStock.lCTUpdater.set("Current Tour :");
        lElapsedTime.setText("0s");
        Util.drawCities(DataStock.cities, gCdraw, gLdraw);
        iniTemperature =cities.size()*cities.size();
        tfIniTemp.setPromptText(String.valueOf(iniTemperature));
        distanceMatrix=Util.getDistanceMatrix(cities);
        tfCR.setDisable(false);
        tfL.setDisable(false);
        tfIniTemp.setDisable(false);
        bPlay.setDisable(false);
        bStop.setDisable(false);
    }
    @FXML
    void load(ActionEvent event) throws IOException {
        loadAction();
        FileChooser fileChooser = new FileChooser();
        Stage stage = (Stage) bLoad.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        DataStock.loadCities(selectedFile);

    }

    @FXML
    void play(ActionEvent event) {
        loadAction();
        blockInputBPlay();
        startTime=System.currentTimeMillis();
        count=true;
        if (!tfIniTemp.getText().isEmpty())
            DataStock.iniTemperature = Double.parseDouble(tfIniTemp.getText());
        if (!tfCR.getText().isEmpty())
            DataStock.coolingFactor = Double.parseDouble(tfCR.getText());
        if (!tfL.getText().isEmpty())
            DataStock.variableL = Integer.parseInt(tfL.getText());

        seed=Long.parseLong(tfSeed.getText());
        generator=new Random();//seed

        sAService = new SimulatedAnnealingService(gLdraw,getMethode());
        sAService.start();
        sAService.setOnSucceeded(event1 -> {
            System.out.println("Done !");
            unblockInputBpPlay();
            count=false;
        });
        sAService.setOnFailed(event1 -> {
            System.out.println("Failed !");
            unblockInputBpPlay();

            DataStock.lITUpdater.set("Initial Tour :");
            DataStock.lCTUpdater.set("Current Tour :");

            count=false;
        });
    }

    @FXML
    void stop(ActionEvent event) {
        count=false;
        tfCR.setEditable(true);
        tfL.setEditable(true);
        tfIniTemp.setEditable(true);
    }

    void blockInputBPlay(){
        bRefresh.setDisable(true);
        rRSL.setDisable(true);
        rS.setDisable(true);
        aS.setDisable(true);
        bFSeed.setDisable(true);
        bLoad.setDisable(true);
        bPlay.setDisable(true);
        tfSeed.setEditable(false);
        tfCR.setEditable(false);
        tfL.setEditable(false);
        tfIniTemp.setEditable(false);
    }
    void unblockInputBpPlay(){
        bRefresh.setDisable(true);
        rRSL.setDisable(false);
        rS.setDisable(false);
        aS.setDisable(false);
        bFSeed.setDisable(false);
        bLoad.setDisable(false);
        bPlay.setDisable(false);
        tfSeed.setEditable(true);
        tfCR.setEditable(true);
        tfL.setEditable(true);
        tfIniTemp.setEditable(true);
    }
    int getMethode(){
        RadioButton selectedRadioButton = (RadioButton) swapMG.getSelectedToggle();
        switch (selectedRadioButton.getText()){
            case "Adjacent Swap":
                return 2;
            case "Reverse Random SubList":
                return 3;
            default:
                return 1;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
        chbGraphs.getItems().setAll(graphs);
        chbGraphs.setConverter(new StringConverter<Graph>() {
            @Override
            public String toString(Graph graph) {
                return String.valueOf(graph.getNumCities());
            }
            @Override
            // not used...
            public Graph fromString(String s) {
                return null ;
            }
        });
        chbGraphs.setValue(graphs.get(0));
        cities=graphs.get(0).getCityList();
        seed= System.currentTimeMillis();

        tfSeed.setText(Long.toString(seed));

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (count) {
                    long elapsedMillis = System.currentTimeMillis() - startTime;
                    lElapsedTime.setText(Long.toString(elapsedMillis / 1000)+"s");
                }
            }
        }.start();
        tfIniTemp.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfIniTemp.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        tfSeed.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfSeed.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        tfNT.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfNT.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        tfCR.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d\\.\\d*")) {
                tfCR.setText(newValue.replaceAll("[^.\\d]", ""));
            }
        });

        tfL.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfL.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        pbTemp.progressProperty().bind(DataStock.barUpdater);
        lIT.textProperty().bind(lITUpdater);
        lCT.textProperty().bind(lCTUpdater);
        lTP.textProperty().bind(lTPUpdater);
        tfCR.setDisable(true);
        tfCR.setPromptText(String.valueOf(coolingFactor));
        tfL.setDisable(true);
        tfL.setPromptText(String.valueOf(variableL));
        tfIniTemp.setDisable(true);
        bPlay.setDisable(true);
        bStop.setDisable(true);
        loadAction();
    }
}