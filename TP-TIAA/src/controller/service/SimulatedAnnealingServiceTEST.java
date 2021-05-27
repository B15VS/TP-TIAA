
package controller.service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Group;
import model.data.Util;

public class SimulatedAnnealingServiceTEST extends Service<Void> {
    long seed;
    int methode;
    public SimulatedAnnealingServiceTEST(long seed,int methode) {
        this.seed = seed;
        this.methode = methode;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Util.simulatedAnnealingFixed(seed,methode);//70/1613140297550
                return null;
            }
        };
    }
}