package controller.service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.data.Score;
import model.data.Util;

public class SeedSearchService extends Service<Score> {
    int methode;
    public SeedSearchService(int methode) {
        this.methode=methode;
    }

    @Override
    protected Task<Score> createTask() {
        return new Task<>() {
            @Override
            protected Score call() {
                return Util.simulatedAnnealing(methode);
            }
        };
    }
}
