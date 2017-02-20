package armand.deminer;

import java.util.Random;

public class GameMap {
    private int rows;
    private int cols;
    private boolean flagMode = false;
    private Cell[][] map;
    private int mineCount = 0;

    public GameMap(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        map = new Cell[rows][cols];
    }

    public int rng(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }

    public Cell getCell(int id) {
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                if (map[i][j].getId() == id) {
                    return map[i][j];
                }
            }
        }
        return null;
    }

    // should exclude diagonally adjacent neighbours
    public void revealCell(Cell cell) {
        cell.reveal();
        int row = cell.getRow();
        int col = cell.getCol();
        // cascade reveals recursively
        if (!cell.isMined()) {
            if (row - 1 >= 0) {
                if (col - 1 >= 0) {
                    if (!map[row - 1][col - 1].isMined() && !map[row - 1][col - 1].isRevealed()) {
                        revealCell(map[row - 1][col - 1]);
                    }
                }
                if (!map[row - 1][col].isMined() && !map[row - 1][col].isRevealed()) {
                    revealCell(map[row - 1][col]);
                }
                if (col + 1 < cols) {
                    if (!map[row - 1][col + 1].isMined() && !map[row - 1][col + 1].isRevealed()) {
                        revealCell(map[row - 1][col + 1]);
                    }
                }
            }
            // left and right neighbors
            if (col - 1 >= 0) {
                if (!map[row][col - 1].isMined() && !map[row][col - 1].isRevealed()) {
                    revealCell(map[row][col - 1]);
                }
            }

            if (col + 1 < cols) {
                if (!map[row][col + 1].isMined() && !map[row][col + 1].isRevealed()) {
                    revealCell(map[row][col + 1]);
                }
            }
            // bottom columns
            if (row + 1 < rows) {
                if (col - 1 >= 0) {
                    if (!map[row + 1][col - 1].isMined() && !map[row + 1][col - 1].isRevealed()) {
                        revealCell(map[row + 1][col - 1]);
                    }
                }
                if (!map[row + 1][col].isMined() && !map[row + 1][col].isRevealed()) {
                    revealCell(map[row + 1][col]);
                }
                if (col + 1 < cols) {
                    if (!map[row + 1][col + 1].isMined() && !map[row + 1][col + 1].isRevealed()) {
                        revealCell(map[row + 1][col + 1]);
                    }
                }
            }
        }
    }

    public void addCellAt(Cell cell, int row, int col) {
        cell.setRow(row);
        cell.setCol(col);
        map[row][col] = cell;
    }

    public void initialize() {
        /*
        generate a fixed number of mines
        go through all cells
        assign the mines randomly, evenly
         */
        int randomNumber;
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                randomNumber = rng(1, 2);
                if (randomNumber != 1) {
                    ++mineCount;
                    map[i][j].setMine(true);
                    // increment adjacent mine counters
                    // start with mines above
                    if (i - 1 >= 0) {
                        if (j - 1 >= 0) {
                            map[i - 1][j - 1].incrementMineCounter();
                        }
                        map[i - 1][j].incrementMineCounter();
                        if (j + 1 < cols) {
                            map[i - 1][j + 1].incrementMineCounter();
                        }
                    }
                    // left and right neighbors
                    if (j - 1 >= 0) {
                        map[i][j - 1].incrementMineCounter();
                    }

                    if (j + 1 < cols) {
                        map[i][j + 1].incrementMineCounter();
                    }
                    // bottom columns
                    if (i + 1 < rows) {
                        if (j - 1 >= 0) {
                            map[i + 1][j - 1].incrementMineCounter();
                        }
                        map[i + 1][j].incrementMineCounter();
                        if (j + 1 < cols) {
                            map[i + 1][j + 1].incrementMineCounter();
                        }
                    }
                }
            }
        }
    }

    public boolean checkWin() {
        int counter = 0;
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                if (map[i][j].isRevealed() && !map[i][j].isMined()) {
                    ++counter;
                }
            }
        }
        if (counter == (rows * cols) - mineCount) {
            return true;
        }
        return false;
    }

    public boolean checkLose() {
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                if (map[i][j].isRevealed() && map[i][j].isMined()) {
                    return true;
                }
            }
        }
        return false;
    }
}
