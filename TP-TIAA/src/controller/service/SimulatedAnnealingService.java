
package controller.service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Group;
import model.data.Util;

public class SimulatedAnnealingService extends Service<Void> {
    Group group;
    int methode;

    public SimulatedAnnealingService(Group group,int methode) {
        this.group = group;
        this.methode=methode;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Util.simulatedAnnealing(group,methode);
                return null;
            }
        };
    }
}