package model.data;

import java.util.List;

public class Graph {
    private final List<City> cityList;
    private final int numCities;

    public Graph(List<City> cityList) {
        this.cityList = cityList;
        this.numCities = cityList.size();
    }

    public List<City> getCityList() {
        return cityList;
    }

    public int getNumCities() {
        return numCities;
    }
}
