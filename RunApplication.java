package project.olandi;

import project.olandi.controller.Controller;
import project.olandi.model.GameField;
import project.olandi.view.View;
import project.olandi.view.console.ConsoleView;
import project.olandi.view.gui.SwingView;


public class RunApplication {

    public static void main(String[] args) {

        GameField model = new GameField();
        model.restartGame();

        Controller controller = new Controller(model);


        View view = new SwingView(controller);
        //View view = new ConsoleView(controller);

        controller.setView(view);
        controller.launchGame();

    }
}
