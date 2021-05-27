package controller;

import model.Communicator;
import model.data.*;
import view.ViewFactory;

public class BaseController {
    protected Communicator communicator;
    protected ViewFactory viewFactory;
    private String fxmlName;


    public BaseController(Communicator communicator, ViewFactory viewFactory, String fxmlName) {
        this.communicator = communicator;
        this.viewFactory = viewFactory;
        this.fxmlName = fxmlName;
    }

    public String getFxmlName() {
        return fxmlName;
    }
}
