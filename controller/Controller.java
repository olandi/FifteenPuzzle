package project.olandi.controller;

import project.olandi.model.GameField;
import project.olandi.model.MovementDirection;
import project.olandi.view.View;

public class Controller {
    private GameField model;
    private View view;

    public Controller(GameField model) {
        this.model = model;
    }

    public void setModel(GameField model) {
        this.model = model;
    }

    public void setView(View view) {
        this.view = view;
    }

    public GameField getModel() {
        return model;
    }

    public View getView() {
        return view;
    }

    private void showGameOverDialog() {

        if (view.showGameOverDialog()) {
            model.restartGame();
            view.repaintView();
        } else
            System.exit(0);
    }

    public void moveTileInDirection(MovementDirection direction) {
        boolean isMoved = model.moveTile(direction);
        if (isMoved) {
            model.incrementScore();
            isGameWon();
        }
    }

    public void moveTileByIndex(int index) {
        boolean isMoved = model.moveTile(index);
        if (isMoved) {
            model.incrementScore();
            isGameWon();
        }
    }

    private void isGameWon() {
        view.repaintView();
        if (model.isWin()) showGameOverDialog();
    }

    public void launchGame() {
        view.BuildView();
    }
}
