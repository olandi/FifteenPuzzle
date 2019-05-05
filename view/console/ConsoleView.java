package project.olandi.view.console;

import project.olandi.controller.Controller;
import project.olandi.model.MovementDirection;
import project.olandi.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleView implements View {
    private Controller controller;

    public ConsoleView(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void BuildView() {

        String userInput = "";

        while (!"exit".equalsIgnoreCase(userInput)) {
            repaintView();
            userInput = translateUserInput();
        }
    }


    private void printGameField() {
        int fieldSize = controller.getModel().getGameFieldSize();

        System.out.println("--------------");
        for (int i = 0; i < fieldSize * fieldSize; i++) {
            System.out.print(controller.getModel().getGameField()[i] + "\t");
            if ((i + 1) % fieldSize == 0) System.out.println();
        }
        System.out.println("--------------\n Score: "+controller.getModel().getScore()+"\n--------------");

    }

    private String getUserInput() {
        System.out.println("Enter command:");
        String result = "";
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            result = bufferedReader.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private String translateUserInput() {
        String userInput = getUserInput();

        if ("right".equalsIgnoreCase(userInput)) controller.moveTileInDirection(MovementDirection.RIGHT);
        if ("left".equalsIgnoreCase(userInput)) controller.moveTileInDirection(MovementDirection.LEFT);
        if ("up".equalsIgnoreCase(userInput)) controller.moveTileInDirection(MovementDirection.UP);
        if ("down".equalsIgnoreCase(userInput)) controller.moveTileInDirection(MovementDirection.DOWN);


        if (userInput.matches("1|2|3|4|5|6|7|8|9|10|11|12|13|14|15")) {
            controller.moveTileByIndex(
                    controller.getModel().getTileIndexByName(Integer.valueOf(userInput))
            );
        }
        return userInput;
    }

    @Override
    public void repaintView() {
        printGameField();
    }

    @Override
    public boolean showGameOverDialog() {
        System.out.println("You won\nContinue? (y/n)");
        return "y".equalsIgnoreCase(getUserInput());
    }

}
