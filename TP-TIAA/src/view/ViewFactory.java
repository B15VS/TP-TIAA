// By B15
package view;
import controller.BaseController;
import controller.MainWindowController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.DataStock;
import model.Communicator;

import java.io.IOException;
import java.util.ArrayList;

public class ViewFactory {

    private Communicator communicator;
    private ArrayList<Stage> activeStages;
    public static DataStock dataStock;

    public ViewFactory(Communicator communicator) {
        this.communicator = communicator;
        this.activeStages = new ArrayList<Stage>();
    }

    private Stage initializeStage(BaseController controller) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(controller.getFxmlName()));
        fxmlLoader.setController(controller);
        Parent root;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        activeStages.add(stage);
        return stage;
    }

    private Stage initializeStage(BaseController controller, boolean resizable) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(controller.getFxmlName()));
        fxmlLoader.setController(controller);
        Parent root;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(resizable);
        activeStages.add(stage);
        return stage;
    }

    public void closeStage(Stage stage) {
        stage.close();
        activeStages.remove(stage);
    }

    public void showMainWindow() {
        System.out.println("showMainWindow Called!");
        BaseController controller = new MainWindowController(this.communicator, this, "MainWindow.fxml");
        Stage stage = initializeStage(controller,false);
        assert stage != null;
        stage.show();
    }
}
