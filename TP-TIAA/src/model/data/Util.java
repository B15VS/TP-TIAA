// By B15
package model.data;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import model.DataStock;

import java.util.*;

import static model.DataStock.*;
import static model.DataStock.generator;

public class Util {
    static Tour finalBestC;
    static double finalT;
    static List<Integer> lastDrawn;
    final static double m = 1.2;

    public static double probability(double f1, double f2, double temp) {
        return Math.exp((f1 - f2) / temp);
    }

    public static double distance(City city1, City city2) {
        double xDist = city1.getX() - city2.getX();
        double yDist = city1.getY() - city2.getY();
        return Math.sqrt((xDist) * (xDist) + yDist * yDist);
    }
    public static double[][] getDistanceMatrix(List<City> cities){
        int numCities = cities.size();
        double[][] disMat = new double [numCities][numCities];
        for (int i=0; i<numCities;i++){
            for (int j=0;j<numCities;j++){
                disMat[i][j]= distance(cities.get(i),cities.get(j));
            }
        }
        return disMat;
    }

    public static Tour findMinRoute(double[][] tsp)
    {

        int counter = 0;
        int j = 0, i = 0;
        double min = Double.MAX_VALUE;
        List<Integer> visitedRouteList
                = new ArrayList<>();

        // Starting from the 0th indexed
        // city i.e., the first city
        visitedRouteList.add(0);
        int[] route = new int[tsp.length];

        // Traverse the adjacency
        // matrix tsp[][]
        while (i < tsp.length && j < tsp[i].length) {

            // Corner of the Matrix
            if (counter >= tsp[i].length - 1) {
                break;
            }

            // If this path is unvisited then
            // and if the cost is less then
            // update the cost
            if (j != i && !(visitedRouteList.contains(j))) {
                if (tsp[i][j] < min) {
                    min = tsp[i][j];
                    route[counter] = j+1;
                }
            }
            j++;

            // Check all paths from the
            // ith indexed city

            if (j == tsp[i].length) {
                min = Integer.MAX_VALUE;
                visitedRouteList.add(route[counter] - 1);
                j = 0;
                i = route[counter] - 1;
                counter++;
            }
        }
        // Update the ending city in array
        // from city which was last visited
        i = route[counter - 1] - 1;

        for (j = 0; j < tsp.length; j++) {

            if ((i != j) && tsp[i][j] < min) {
                min = tsp[i][j];
                route[counter] = j +1;
            }
        }
        return new Tour(visitedRouteList
        , false, generator);
    }
    public static void randomSwap(Tour neighbor,Random generator){
        int numCities = neighbor.numCities();
        Collections.swap(neighbor.getTour(), randomGenerator(numCities,generator), randomGenerator(numCities,generator));
    }
    public static void adjacentSwap(Tour neighbor,Random generator){
        int numCities = neighbor.numCities();
        int index = randomGenerator(numCities,generator);
        int index2 = randomGenerator(numCities,generator);

        if(index<index2){
            if(index==neighbor.numCities()-1)
                Collections
                        .swap(neighbor.getTour(), index, 0);
            else
                Collections
                        .swap(neighbor.getTour(), index, index+1);
        }else {
            if(index==0)
                Collections
                        .swap(neighbor.getTour(), index, numCities-1);
            else
                Collections
                        .swap(neighbor.getTour(), index, index-1);
        }
    }
    public static void reversSublist(Tour neighbor,Random generator){
        int index1;
        int index2;
        do {
             index1 = randomGenerator(neighbor.numCities(), generator);
             index2 = randomGenerator(neighbor.numCities(), generator);
        }while (index1==index2);

       if (index1<index2)
           Collections
                   .reverse(
                           neighbor.getTour()
                                   .subList( index1 , index2 + 1 )
                   ) ;
       else if (index2<index1)
           Collections
                   .reverse(
                           neighbor.getTour()
                                   .subList( index2 , index1 + 1 )
                   ) ;

    }
    public static void simulatedAnnealing(Group group,int methode) {
        Tour current = new Tour(true,generator);
        if(variableL==0){
            variableL=1;
            current = findMinRoute(distanceMatrix);
        }
        Tour best = current.duplicate();
        /////
        int IniCurrent = best.getTourLength();
        Tour finalBestI = best.duplicate();
        Platform.runLater(() -> {
            lITUpdater.set("Initial Tour :" + IniCurrent + " Km");
            drawTour(finalBestI, group);
        });
        ////
        //
        int b = 0;
        int l = 1;

        double t = iniTemperature;//10
        double currentLength;
        double neighborLength;
        Tour neighbor;
        while (t>1e-4) {
            neighbor = current.duplicate();

            switch (methode) {
                case 1:
                    randomSwap(neighbor,generator);
                    break;
                case 2:
                    adjacentSwap(neighbor,generator);
                    break;
                case 3:
                    reversSublist(neighbor,generator);
                    break;
            }

            currentLength = current.getTourLength();
            neighborLength = neighbor.getTourLength();
            if (b > 100000) break;
            if (neighborLength <= currentLength) {
                current = neighbor;
                if (neighborLength < best.getTourLength()) {
                    b = 0;
                    best = neighbor;
                    /////
                    finalBestC = best;
                    Platform.runLater(() -> {
                        lCTUpdater.setValue("Current Best Tour :" + finalBestC.getTourLength()+ " Km");
                        if (cities.size() < 301) {
                            drawTour(finalBestC, group);

                        }else if(drawInterval >1000){
                            drawTour(finalBestC, group);
                            drawInterval =0;
                        }else drawInterval++;
                    });
                }
            } else {
                if (generator.nextDouble()/*Q*/ <= /*P*/Util.probability(currentLength, neighborLength, t)) {
                    current = neighbor;
                }
            }
            if (l > variableL) {
                t *= coolingFactor;
                finalT = t;
                Platform.runLater(() -> {
                DataStock.barUpdater.set(finalT / DataStock.iniTemperature);
                lTPUpdater.setValue(String.format("%,.3f",(finalT / DataStock.iniTemperature) * 100) +"%");
                });
                l = 1;
                b++;
            } else {
                l++;
            }
            if (!count)return;
        }
        System.out.println("b="+b);
        ////////
        int finalBest = best.getTourLength();
        Tour finalBestF = best;
        Platform.runLater(() -> {
            lCTUpdater.set("Final Tour: " + finalBest + " Km");
            drawTour(finalBestF, group);
            DataStock.barUpdater.set(0);
            lTPUpdater.setValue("0.000%");
        });
        ////////
        System.out.println("Final tour length: " + best.getTourLength());
        if(bestScore==null||bestScore.getDistance()>best.getTourLength()) {
            bestScore = new Score(seed, best.getTourLength());
        }
        System.out.println("Number of Cities : " + cities.size());
        count=false;
    }

    public static Score getBestScore(List<Score> scores){
        Score bestScore = new Score(System.currentTimeMillis(),Integer.MAX_VALUE);
        for (Score score:scores){
            if (score.getDistance()<bestScore.getDistance())
                bestScore=score;
        }
        return bestScore;
    }

    public static Score simulatedAnnealing(int methode) {
        long seed =System.currentTimeMillis();
        Random generator=new Random(seed);

        Tour current = new Tour(true,generator);
        Tour best = current.duplicate();

        int i = 0;
        int b = 0;
        int l = 1;

        double t = iniTemperature;
        while (i < 1000000&& t>0.04) {
            Tour neighbor = current.duplicate();

            switch (methode) {
                case 1:
                    randomSwap(neighbor,generator);
                    break;
                case 2:
                    adjacentSwap(neighbor,generator);
                    break;
                case 3:
                    reversSublist(neighbor,generator);
                    break;
            }

            int currentLength = current.getTourLength();
            int neighborLength = neighbor.getTourLength();
            if (b > 10000) break;
            if (neighborLength <= currentLength) {
                current = neighbor;
                if (neighborLength < best.getTourLength()) {
                    best = neighbor;
                    b = 0;
                }
            } else {
                if (generator.nextDouble()/*Q*/ <= Util.probability(currentLength, neighborLength, t)/*P*/) {
                    current = neighbor;
                }
            }
            if (l > variableL) {
                t *= coolingFactor;
                l = 1;
                i++;
                b++;
            } else {
                l++;
            }
        }
        return new Score(seed,best.getTourLength());
    }
    public static void simulatedAnnealingFixed(long seed,int methode) {

        Random generator=new Random(seed);

        Tour current = new Tour(true,generator);
        Tour best = current.duplicate();

        int i = 0;
        int b = 0;
        int l = 1;

        double t = iniTemperature;
        while (i < 1000000&& t>0.04) {
            Tour neighbor = current.duplicate();

            switch (methode) {
                case 1:
                    randomSwap(neighbor,generator);
                    break;
                case 2:
                    adjacentSwap(neighbor,generator);
                    break;
                case 3:
                    reversSublist(neighbor,generator);
                    break;
            }

            int currentLength = current.getTourLength();
            int neighborLength = neighbor.getTourLength();
            if (b > 10000) break;
            if (neighborLength <= currentLength) {
                current = neighbor;
                if (neighborLength < best.getTourLength()) {
                    best = neighbor;
                    b = 0;
                }
            } else {
                if (generator.nextDouble()/*Q*/ <= Util.probability(currentLength, neighborLength, t)/*P*/) {
                    current = neighbor;
                }
            }
            if (l > variableL) {
                t *= coolingFactor;
                l = 1;
                i++;
                b++;
            } else {
                l++;
            }
        }
        System.out.println("Thread : Final Tour = "+best.getTourLength());
    }




    public static Circle genCircle(double x, double y, double m) {
        Circle circle = new Circle(x * m, y * m, 4, Color.web("#FFDE03"));
        circle.setStrokeWidth(0.3);
        circle.setStroke(Color.DARKSLATEGREY);
        return circle;
    }

    public static Line genLine(double sX, double sY, double dX, double dY, double m) {
        Line line = new Line(sX * m, sY * m, dX * m, dY * m);
        line.setStroke(Color.web("#C7C6C8"));
        return line;
    }

    public static int randomGenerator(int n, Random generator) {
        return generator.nextInt(n);
    }

    public static void drawTour(Tour tour, Group group) {
        if (!tour.getTour().equals(lastDrawn)) {
            group.getChildren().clear();
            for (int i = 0; i < tour.numCities(); i++) {

                if(i+1 < tour.numCities())group.getChildren().add(
                        genLine(
                                tour.getCity(tour.getTour().get(i)).getX(),
                                tour.getCity(tour.getTour().get(i)).getY(),
                                tour.getCity(tour.getTour().get(i+1)).getX(),//i+1 < tour.numCities() ?tour.getTour().get(i+1) : tour.getTour().get(0)
                                tour.getCity(tour.getTour().get(i+1)).getY(),
                                m));
                if(i+1 >= tour.numCities()) group.getChildren().add(
                        genLine(
                                tour.getCity(tour.getTour().get(i)).getX(),
                                tour.getCity(tour.getTour().get(i)).getY(),
                                tour.getCity(tour.getTour().get(0)).getX(),//i+1 < tour.numCities() ?tour.getTour().get(i+1) : tour.getTour().get(0)
                                tour.getCity(tour.getTour().get(0)).getY(),
                                m));
            }
            lastDrawn = new ArrayList<>(tour.getTour());
        }
        //System.out.println("skipped");
    }

    public static void drawCities(List<City> cities, Group group, Group groupL) {
        group.getChildren().clear();
        groupL.getChildren().clear();
        for (City city : cities) {
            group.getChildren().add(genCircle(city.getX(), city.getY(), m));
        }
    }

}