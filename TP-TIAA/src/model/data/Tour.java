// By B15
package model.data;

import model.DataStock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Tour {
    private static List<City> cities=DataStock.cities;
    private List<Integer> tour;
    private int distance;

    public Tour(List<Integer> tour, boolean shuffle, Random random) {
        if(!(this.cities==DataStock.cities))
            this.cities=DataStock.cities;
        this.tour = new ArrayList<>(tour);

        if (shuffle)
        Collections.shuffle(this.tour,random);
    }
    public Tour(boolean shuffle, Random random) {
        if(!(this.cities==DataStock.cities))
            this.cities=DataStock.cities;
        this.tour = IntStream.rangeClosed(0, cities.size()-1)
                .boxed().collect(Collectors.toList());
        if (shuffle)
            Collections.shuffle(this.tour,random);
    }

    public City getCity(int index) {
        return cities.get(index);
    }

    public int getTourLength() {
        if (distance != 0) return distance;

        int totalDistance = 0;

        for (int i = 0; i < numCities(); i++) {
           /* City start = getCity(i);
            City end = getCity(i + 1 < numCities() ? i + 1 : 0);
            totalDistance += Util.distance(start, end);*/
            totalDistance+=DataStock.distanceMatrix[tour.get(i)][i+1 < numCities() ? tour.get(i+1) : tour.get(0)];
        }

        distance = totalDistance;
        return totalDistance;
    }

    public Tour duplicate() {
        return new Tour(tour,false, DataStock.generator);
    }
    public Tour duplicateShuffle() {
        return new Tour(tour,true, DataStock.generator);
    }

    public int numCities() {
        return cities.size();
    }

    // Getters and toString()

    public List<City> getCities() {
        return cities;
    }

    public int getDistance() {
        return distance;
    }

    public List<Integer> getTour() {
        return tour;
    }

    @Override
    public String toString() {
        return "Tour{" +
                "cities=" + cities +
                ", distance=" + distance +
                '}';
    }
}