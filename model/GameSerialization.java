package project.olandi.model;

import project.olandi.controller.Controller;

import java.io.*;

public class GameSerialization {

    private Controller controller;

    public GameSerialization(Controller controller) {
        this.controller = controller;
    }

    public void saveGame(String path) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(path);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {

            objectOutputStream.writeObject(controller.getModel());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public GameField loadGame(String path) {
        GameField gameField = null;
        try (FileInputStream fileInputStream = new FileInputStream(path);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

            gameField = (GameField) objectInputStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return gameField;
    }
}
