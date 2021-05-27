// By B15
package model;
import javafx.beans.property.*;
import model.data.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class DataStock {
    public static boolean count=false;
    public static long startTime=0 ;



    public static long seed;
    public static Score bestScore ;
    public static List<Score> scores =new ArrayList<>();

    public static double[][] distanceMatrix;
    public static Random generator;
    public static double iniTemperature ;
    public static int drawInterval =0;
    public static int variableL = 15;
    public static int nThreads = 10;
    public static DoubleProperty barUpdater = new SimpleDoubleProperty(0);
    public static StringProperty lITUpdater = new SimpleStringProperty("Initial Tour :");
    public static StringProperty lCTUpdater = new SimpleStringProperty("Current Best Tour :");
    public static StringProperty lTPUpdater = new SimpleStringProperty("0%");
    public static double coolingFactor = 0.999;
    public static List<City> cities = new ArrayList<>();
    public static List<Graph> graphs = new ArrayList<>();

    public static List<City> loadCities(File file) throws IOException {
        List<City> cities=new ArrayList<>();
        int count = 0;
        String line;
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferReader = new BufferedReader(fileReader);
        while ((line = bufferReader.readLine()) != null) {
            try{ if (count>=4) cities.add(loadCity(line));
            }catch (Exception e){
                System.out.println("Error when reading !");
            }
            count++;
        }
        System.out.println("Successfully loaded "+cities.size()+" cities !");
        return cities;
    }
    public static void loadFiles() throws IOException{
        File folder = new File("src/view/res/DataSet");
        List<String> filenames= Arrays.asList(Objects.requireNonNull(folder.list()));
        filenames.sort(Comparator.comparing(a -> a.replaceAll("[\\d]", "")));
        for(String name:filenames){
            graphs.add(new Graph(loadCities(new File("src/view/res/DataSet/"+name))));
        }
    }
    public static City loadCity(String line){
        return new City(Double.parseDouble(line.split("\t")[0]),Double.parseDouble(line.split("\t")[1]));
    }

}
