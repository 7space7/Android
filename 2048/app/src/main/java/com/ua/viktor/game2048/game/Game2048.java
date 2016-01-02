package com.ua.viktor.game2048.game;

import java.util.LinkedList;
import java.util.Random;


public class Game2048 {
    private final int DIMENSIONS_X = 4;
    private final int DIMENSIONS_Y = 4;
    private final int WIN_SCORE = 2048;
    private int[] START_VALUES = new int[]{2, 4};
    private int[] START_VALUES_PROBABILITY = new int[]{80, 20};
    private int[][] field = new int[DIMENSIONS_X][];
    private LinkedList<Changes> changes = new LinkedList<Changes>();
    private int score = 0;

    public Game2048() {

        for (int i = 0; i < DIMENSIONS_X; i++)
            field[i] = new int[DIMENSIONS_Y];
        setNewValue();
        setNewValue();
    }

    public boolean moveLeft() {
        changes = new LinkedList<Changes>();
        for (int i = 0; i < DIMENSIONS_Y; i++) {
            for (int j = 1; j < DIMENSIONS_X; j++) {
                if (field[i][j] != 0) {
                    Changes newChange = null;
                    for (int k = 1; k <= j; k++) {
                        if (field[i][j - k] != 0) {
                            if (field[i][j - k] == field[i][j]) {
                                field[i][j - k] = field[i][j] * 2;
                                field[i][j] = 0;
                                newChange = new Changes(i, j, i, j - k, field[i][j - k]);
                                score += newChange.newValue;
                            } else {
                                if (k != 1) {
                                    field[i][j - k + 1] = field[i][j];
                                    field[i][j] = 0;
                                    newChange = new Changes(i, j, i, j - k + 1, field[i][j - k + 1]);
                                }
                            }
                            break;
                        } else {
                            if (field[i][j - k] == 0 && j - k == 0) {
                                field[i][j - k] = field[i][j];
                                field[i][j] = 0;
                                newChange = new Changes(i, j, i, j - k, field[i][j - k]);
                                break;
                            }
                        }
                    }
                    if (newChange != null)
                        changes.addFirst(newChange);
                }
            }
        }

        if (changes.size() != 0) {
            setNewValue();
            return true;
        } else
            return false;
    }

    public boolean moveUp() {
        changes = new LinkedList<Changes>();
        for (int i = 1; i < DIMENSIONS_Y; i++) {
            for (int j = 0; j < DIMENSIONS_X; j++) {
                if (field[i][j] != 0) {
                    Changes newChange = null;
                    for (int k = 1; k <= i; k++) {
                        if (field[i - k][j] != 0) {
                            if (field[i - k][j] == field[i][j]) {
                                field[i - k][j] = field[i][j] * 2;
                                field[i][j] = 0;
                                newChange = new Changes(i, j, i - k, j, field[i - k][j]);
                                score += newChange.newValue;
                            } else {
                                if (k != 1) {
                                    field[i - k + 1][j] = field[i][j];
                                    field[i][j] = 0;
                                    newChange = new Changes(i, j, i - k + 1, j, field[i - k + 1][j]);
                                }
                            }
                            break;
                        } else {
                            if (field[i - k][j] == 0 && i - k == 0) {
                                field[i - k][j] = field[i][j];
                                field[i][j] = 0;
                                newChange = new Changes(i, j, i - k, j, field[i - k][j]);
                                break;
                            }
                        }
                    }
                    if (newChange != null)
                        changes.addFirst(newChange);
                }
            }
        }

        if (changes.size() != 0) {
            setNewValue();
            return true;
        } else
            return false;
    }

    public boolean moveDown() {
        changes = new LinkedList<Changes>();
        for (int i = DIMENSIONS_Y - 2; i >= 0; i--) {
            for (int j = 0; j < DIMENSIONS_X; j++) {
                if (field[i][j] != 0) {
                    Changes newChange = null;
                    for (int k = 1; k < DIMENSIONS_Y - i; k++) {
                        if (field[i + k][j] != 0) {
                            if (field[i + k][j] == field[i][j]) {
                                field[i + k][j] = field[i][j] * 2;
                                field[i][j] = 0;
                                newChange = new Changes(i, j, i + k, j, field[i + k][j]);
                                score += newChange.newValue;
                            } else {
                                if (k != 1) {
                                    field[i + k - 1][j] = field[i][j];
                                    field[i][j] = 0;
                                    newChange = new Changes(i, j, i + k - 1, j, field[i + k - 1][j]);
                                }
                            }
                            break;
                        } else {
                            if (field[i + k][j] == 0 && i + k == DIMENSIONS_Y - 1) {
                                field[i + k][j] = field[i][j];
                                field[i][j] = 0;
                                newChange = new Changes(i, j, i + k, j, field[i + k][j]);
                                break;
                            }
                        }
                    }
                    if (newChange != null)
                        changes.addFirst(newChange);
                }
            }
        }

        if (changes.size() != 0) {
            setNewValue();
            return true;
        } else
            return false;
    }

    public boolean won() {
        for (int i = 0; i < DIMENSIONS_Y; i++) {
            for (int j = 0; j < DIMENSIONS_Y; j++) {
                if (field[i][j] == WIN_SCORE)
                    return true;
            }
        }
        return false;
    }

    public boolean lost() {
        for (int i = 0; i < DIMENSIONS_Y; i++) {
            for (int j = 0; j < DIMENSIONS_Y; j++) {
                if (field[i][j] == 0)
                    return false;
            }
        }
        int[][] newField = field;
        if (moveDown() || moveLeft() || moveRight() || moveUp()) {
            field = newField;
            return false;
        }
        return true;
    }

    public boolean moveRight() {
        changes = new LinkedList<Changes>();
        for (int i = 0; i < DIMENSIONS_Y; i++) {
            for (int j = DIMENSIONS_X - 2; j >= 0; j--) {
                if (field[i][j] != 0) {
                    Changes newChange = null;
                    for (int k = 1; k < DIMENSIONS_X - j; k++) {
                        if (field[i][j + k] != 0) {
                            if (field[i][j + k] == field[i][j]) {
                                field[i][j + k] = field[i][j] * 2;
                                field[i][j] = 0;
                                newChange = new Changes(i, j, i, j + k, field[i][j + k]);
                                score += newChange.newValue;
                            } else {
                                if (k != 1) {
                                    field[i][j + k - 1] = field[i][j];
                                    field[i][j] = 0;
                                    newChange = new Changes(i, j, i, j + k - 1, field[i][j + k - 1]);
                                }
                            }
                            break;
                        } else {
                            if (field[i][j + k] == 0 && j + k == DIMENSIONS_X - 1) {
                                field[i][j + k] = field[i][j];
                                field[i][j] = 0;
                                newChange = new Changes(i, j, i, j + k, field[i][j + k]);
                                break;
                            }
                        }
                    }
                    if (newChange != null)
                        changes.addFirst(newChange);
                }
            }
        }

        if (changes.size() != 0) {
            setNewValue();
            return true;
        } else
            return false;
    }

    public int[][] getField() {
        return field;
    }

    public int getScore() {
        return score;
    }

    public Changes[] getChanges() {
        return changes.toArray(new Changes[changes.size()]);
    }

    private boolean setNewValue() {
        LinkedList<Integer> availableX = new LinkedList<Integer>();
        LinkedList<Integer> availableY = new LinkedList<Integer>();
        for (int i = 0; i < DIMENSIONS_X; i++) {
            for (int j = 0; j < DIMENSIONS_Y; j++) {
                if (field[i][j] == 0) {
                    availableX.addFirst(i);
                    availableY.addFirst(j);
                }
            }
        }
        if (availableX.size() == 0)
            return false;
        else {
            Random ran = new Random();
            int pick = ran.nextInt(availableX.size());
            int valuePick = ran.nextInt(100);
            int value = 0;
            for (int i = 0; i < START_VALUES.length; i++) {
                if (valuePick <= START_VALUES_PROBABILITY[i]) {
                    value = START_VALUES[i];
                    break;
                } else {
                    valuePick -= START_VALUES_PROBABILITY[i];
                }
            }
            field[availableX.get(pick)][availableY.get(pick)] = value;
            changes.addFirst(new Changes(-1, -1, availableX.get(pick), availableY.get(pick), value));
            return true;
        }
    }
}

