// By B15
import javafx.application.Application;
import javafx.stage.Stage;
import model.DataStock;
import model.Communicator;
import view.ViewFactory;

public class Launcher extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        DataStock dataStock;
        Communicator communicator = new Communicator();
        ViewFactory viewFactory = new ViewFactory(communicator);
        Communicator.viewFactory = viewFactory;
        viewFactory.showMainWindow();
    }
}
