package project.olandi.model;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


public class GameField implements Serializable {

    private static final int GAME_FIELD_SIZE = 4;
    private static final int EMPTY_TILE_VALUE = 0;
    private static final int[] WIN_COMBINATION = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0};

    private int[] gameField;
    private int gameFieldSize;
    private int score;

    public GameField() {
        this.gameFieldSize = GAME_FIELD_SIZE;
    }

    public int[] getGameField() {
        return gameField;
    }

    public int getGameFieldSize() {
        return gameFieldSize;
    }

    public int getTileIndexByName(int tileName) {
        return getTileIndexByName(tileName, gameField);
    }

    /**
     * Возвращает индекс плитки по указанному значению.
     *
     * @param tileName значение плитки
     * @param array    массив, в котором осуществляется поиск
     * @return Возвращает индекс плитки по указанному значению,
     * -1, если отсутствует значение в массиве
     */
    private int getTileIndexByName(int tileName, int[] array) {
        for (int i = 0; i < array.length; i++) {
            if (tileName == array[i]) return i;
        }
        return -1;
    }

    public int getScore() {
        return score;
    }

    public boolean isWin() {
        return Arrays.equals(gameField, WIN_COMBINATION);
    }

    public void incrementScore() {
        ++score;
    }

    public void restartGame() {
        this.gameField = blendTiles();
        //blendTilesByMovement(100);
        this.score = 0;
    }

    @Override
    public String toString() {
        return "GameField{" +
                "gameField=" + Arrays.toString(gameField) +
                ", gameFieldSize=" + gameFieldSize +
                ", score=" + score +
                '}';
    }

    private void swapWithEmptyTile(int movedTileIndex) {
        int emptyTileIndex = getTileIndexByName(EMPTY_TILE_VALUE);

        gameField[emptyTileIndex] = gameField[movedTileIndex];
        gameField[movedTileIndex] = EMPTY_TILE_VALUE;
    }

    private boolean isIndexValid(int index) {
        return (index >= 0) && (index < gameFieldSize * gameFieldSize);
    }

    private boolean isAbleToMoveLeft(int movedTileIndex) {
        int emptyTileIndex = getTileIndexByName(EMPTY_TILE_VALUE);

        return (isIndexValid(movedTileIndex)
                && movedTileIndex > 0
                && movedTileIndex - 1 == emptyTileIndex
                && movedTileIndex / gameFieldSize == (movedTileIndex - 1) / gameFieldSize);
    }

    private boolean isAbleToMoveRight(int movedTileIndex) {
        int emptyTileIndex = getTileIndexByName(EMPTY_TILE_VALUE);

        return (isIndexValid(movedTileIndex)
                && movedTileIndex < (gameFieldSize * gameFieldSize) - 1
                && movedTileIndex + 1 == emptyTileIndex
                && movedTileIndex / gameFieldSize == (movedTileIndex + 1) / gameFieldSize);
    }

    private boolean isAbleToMoveUp(int movedTileIndex) {
        int emptyTileIndex = getTileIndexByName(EMPTY_TILE_VALUE);

        return (isIndexValid(movedTileIndex)
                && movedTileIndex >= gameFieldSize
                && movedTileIndex - gameFieldSize == emptyTileIndex);
    }

    private boolean isAbleToMoveDown(int movedTileIndex) {
        int emptyTileIndex = getTileIndexByName(EMPTY_TILE_VALUE);

        return (isIndexValid(movedTileIndex)
                && movedTileIndex < gameFieldSize * gameFieldSize - gameFieldSize
                && movedTileIndex + gameFieldSize == emptyTileIndex);
    }

    private boolean isAbleToMove(int movedTileIndex) {
        return isAbleToMoveLeft(movedTileIndex)
                || isAbleToMoveRight(movedTileIndex)
                || isAbleToMoveUp(movedTileIndex)
                || isAbleToMoveDown(movedTileIndex);
    }

    public boolean moveTile(int movedTileIndex) {
        if (isAbleToMove(movedTileIndex)) {
            swapWithEmptyTile(movedTileIndex);
            return true;
        }
        return false;
    }

    public boolean moveTile(MovementDirection direction) {
        int emptyTileIndex = getTileIndexByName(EMPTY_TILE_VALUE);
        int movedTileIndex = 0;

        switch (direction) {
            case UP:
                movedTileIndex = emptyTileIndex + gameFieldSize;
                break;

            case DOWN:
                movedTileIndex = emptyTileIndex - gameFieldSize;
                break;

            case LEFT:
                movedTileIndex = emptyTileIndex + 1;
                break;

            case RIGHT:
                movedTileIndex = emptyTileIndex - 1;
                break;
        }
        return moveTile(movedTileIndex);
    }

    /**
     * Инизиализирует игровое поле головоломки.
     * Инициализация происходит за счет выполнения
     * n-ого числа перемещений плиток в случайном направлении.
     *
     * @param quantity количество случайных перемещений плиток.
     */
    private void blendTilesByMovement(int quantity) {
        gameField = Arrays.copyOf(WIN_COMBINATION, WIN_COMBINATION.length);
        int min = 0, max = MovementDirection.values().length;
        int randomNum;

        for (int i = 0; i < quantity; i++) {
            randomNum = ThreadLocalRandom.current().nextInt(min, max);
            moveTile(MovementDirection.values()[randomNum]);
        }
    }

    /**
     * Воозвращает игровое поле головоломки, сгенерированного с помощью массива,
     * заполненного случайными неповторяющимися значениями от 0 до 15.
     * Для данного игрового поля головоломки существует решение.
     *
     * @return игровое поле головоломки, для которого существует решение
     */
    private int[] blendTiles() {
        int[] initialStage = Arrays.copyOf(WIN_COMBINATION, WIN_COMBINATION.length);
        ArrayList<Integer> list = new ArrayList<>();

        do {
            Arrays.stream(initialStage).forEach(list::add);

            Collections.shuffle(list);

            initialStage = list.stream().mapToInt(i -> i).toArray();

            list.clear();

        } while (!isGameFieldEven(initialStage));

        return initialStage;
    }

    /**
     * метод возвращает true, если существует решение для указанного игрового поля (массива).
     * <p>
     * Ровно половину из всех возможных 20 922 789 888 000 (=16!)
     * начальных положений пятнашек невозможно привести к собранному виду.
     * <p>
     * Решение существует, если N - четное.
     * <p>
     * N = sum(CountOfTilesWhatLessThenT(i)) + e, i = 1 to 15
     * <p>
     * где:
     * 1) e - номер ряда пустой клетки (считая с 1).
     * <p>
     * 2) (CountOfTilesWhatLessThenT(i)) -
     * <p>
     * (если считать слева направо и сверху вниз)
     * Для каждого "квадратика со значением" T(i), i = 1 to 15,
     * за исключением пустого (T(0)),
     * посчитать количество "квадратиков со значением",
     * значение которых меньше T(i) и располагающихся после T(i).
     *
     * @param initialStage проверяемый на четность массив,
     *                     который выступает в качестве игрового поля
     * @return true, если существует решение для указанного игрового поля (массива)
     */
    private boolean isGameFieldEven(int[] initialStage) {
        int count = 0;
        for (int i = 0; i < initialStage.length; i++) {
            //System.out.println("Tile " + initialStage[i] + " with index " + i);

            for (int j = i; j < initialStage.length; j++) {

                if (initialStage[i] > initialStage[j]
                        && initialStage[j] != 0) {

                    //System.out.println(initialStage[i] + " > " + initialStage[j]);
                    count++;
                }
            }
            //System.out.println();
        }
        int emptyTileRow = (getTileIndexByName(0, initialStage) / gameFieldSize) + 1;
        //System.out.println("count is " + count);
        // System.out.println("emptyTileRow is " + (emptyTileRow));
        return (count + emptyTileRow) % 2 == 0;
    }
}