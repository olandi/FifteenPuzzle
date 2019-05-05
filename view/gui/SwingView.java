package project.olandi.view.gui;

import project.olandi.controller.Controller;
import project.olandi.model.GameSerialization;
import project.olandi.model.MovementDirection;
import project.olandi.view.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class SwingView implements View {
    private Controller controller;

    private int frameWidth = 460;
    private int frameHeight = 520;
    private JFrame frame;
    private GameSerialization gameSerialization;
    private JPanel gameField;
    private JLabel scoreLabel;
    private MouseInput mouseInput;
    private KeyboardInput keyboardInput;

    public SwingView(Controller controller) {
        this.controller = controller;
        gameSerialization = new GameSerialization(controller);
        this.mouseInput = new MouseInput();
        this.keyboardInput = new KeyboardInput();
    }

    @Override
    public void BuildView() {
        EventQueue.invokeLater(() ->
        {
            frame = new JFrame("Fifteen Puzzle");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setSize(frameWidth, frameHeight);
            frame.setResizable(false);

            gameField = new GameField(controller);
            gameField.setFocusable(true);

            JMenuBar jMenuBar = createMenuBar();
            frame.setJMenuBar(jMenuBar);
            frame.add(gameField, BorderLayout.CENTER);
            JPanel gameScorePanel = createScorePanel();

            frame.add(gameScorePanel, BorderLayout.SOUTH);

            setKeyBoardInput();

            frame.setVisible(true);
        });
    }

    private JPanel createScorePanel() {
        Font font = new Font(GameField.FONT_NAME, Font.BOLD, 20);
        JPanel scorePanel = new JPanel();
        scorePanel.setBackground(GameField.BG_COLOR);

        scoreLabel = new JLabel();
        scoreLabel.setFont(font);

        scoreLabel.setText("Score: " + controller.getModel().getScore());
        scorePanel.add(scoreLabel);
        return scorePanel;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");

        JMenuItem restart = new JMenuItem("Restart");
        restart.addActionListener(e -> {
            controller.getModel().restartGame();
            frame.repaint();
        });

        JMenuItem save = new JMenuItem("Save");
        save.addActionListener(e -> {
            JFileChooser openFile = new JFileChooser();
            int ret = openFile.showDialog(null, "Save");
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = openFile.getSelectedFile();
                gameSerialization.saveGame(file.toString());
                JOptionPane.showMessageDialog(null, "Game saved");
            }
        });

        JMenuItem load = new JMenuItem("Load");
        load.addActionListener(e -> {
            JFileChooser openFile = new JFileChooser();
            int ret = openFile.showDialog(null, "Open");
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = openFile.getSelectedFile();
                controller.setModel(gameSerialization.loadGame(file.toString()));
                frame.repaint();
                repaintScorePanel();
            }
        });

        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> System.exit(0));

        gameMenu.add(restart);
        gameMenu.add(save);
        gameMenu.add(load);

        gameMenu.addSeparator();
        gameMenu.add(exit);

        JMenu inputControl = new JMenu("Input");

        ButtonGroup group = new ButtonGroup();
        JRadioButtonMenuItem keyboardControl = new JRadioButtonMenuItem("Keyboard");
        keyboardControl.setSelected(true);
        JRadioButtonMenuItem mouseControl = new JRadioButtonMenuItem("Mouse");
        mouseControl.addActionListener(e -> setMouseInput());
        keyboardControl.addActionListener(e -> setKeyBoardInput());

        group.add(keyboardControl);
        group.add(mouseControl);

        inputControl.add(mouseControl);
        inputControl.add(keyboardControl);

        menuBar.add(gameMenu);
        menuBar.add(inputControl);

        return menuBar;
    }


    public boolean showGameOverDialog() {
        String[] options = {"Restart", "Exit"};

        return JOptionPane.showOptionDialog(null, "You won!",
                "Click a button",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]) == 0;
    }


    private void repaintScorePanel() {
        scoreLabel.setText("Score: " + controller.getModel().getScore());
    }

    public void repaintView() {
        frame.repaint();
        repaintScorePanel();
    }

    private void setMouseInput() {
        enableMouseInput();
        disableKeyboardInput();
    }

    private void setKeyBoardInput() {
        enableKeyboardInput();
        disableMouseInput();
    }

    private void enableMouseInput() {
        gameField.addMouseListener(mouseInput);
    }

    private void disableMouseInput() {
        gameField.removeMouseListener(mouseInput);
    }

    private void enableKeyboardInput() {
        gameField.addKeyListener(keyboardInput);
    }

    private void disableKeyboardInput() {
        gameField.removeKeyListener(keyboardInput);
    }


    private class MouseInput extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            int gameFieldSize = controller.getModel().getGameFieldSize();
            int tileArea = GameField.TILE_MARGIN + GameField.TILE_SIZE;
            int a = (e.getX() / (tileArea)) + (gameFieldSize * (e.getY() / (tileArea)));
            controller.moveTileByIndex(a);
        }
    }


    private class KeyboardInput extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT: {
                    controller.moveTileInDirection(MovementDirection.LEFT);
                    break;
                }

                case KeyEvent.VK_RIGHT: {
                    controller.moveTileInDirection(MovementDirection.RIGHT);
                    break;
                }

                case KeyEvent.VK_UP: {
                    controller.moveTileInDirection(MovementDirection.UP);
                    break;
                }

                case KeyEvent.VK_DOWN: {
                    controller.moveTileInDirection(MovementDirection.DOWN);
                    break;
                }
            }
        }
    }


}


