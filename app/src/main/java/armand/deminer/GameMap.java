package armand.deminer;

import java.util.Random;

class GameMap {
    private int rows;
    private int cols;
    private boolean flagMode = false;
    private Cell[][] map;
    private int mineCount = 0;

    GameMap(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        map = new Cell[rows][cols];
    }

    int rng(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }

    Cell getCell(int id) {
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                if (map[i][j].getId() == id) {
                    return map[i][j];
                }
            }
        }
        return null;
    }

    void revealMap() {
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                map[i][j].reveal();
            }
        }
    }

    // should exclude diagonally adjacent neighbours
    // actually, discard that idea, minesweeper uses Moore neighbourhoods
    // excluding diagonally adjacent neighbors = von Neumann neighbourhood
    void revealCell(Cell cell) {
        cell.reveal();
        int row = cell.getRow();
        int col = cell.getCol();
        // cascade reveals recursively
        if (!cell.isMined()) {
            if (row - 1 >= 0) {
                if (col - 1 >= 0) {
                    if (!map[row - 1][col - 1].isMined() && !map[row - 1][col - 1].isRevealed()) {
                        if (map[row - 1][col - 1].getMineCounter() == 0) {
                            revealCell(map[row - 1][col - 1]);
                        }
                        else {
                            map[row - 1][col - 1].reveal();
                        }

                    }
                }
                if (!map[row - 1][col].isMined() && !map[row - 1][col].isRevealed()) {
                    if (map[row - 1][col].getMineCounter() == 0) {
                        revealCell(map[row - 1][col]);
                    }
                    else {
                        map[row - 1][col].reveal();
                    }
                }
                if (col + 1 < cols) {
                    if (!map[row - 1][col + 1].isMined() && !map[row - 1][col + 1].isRevealed()) {
                        if (map[row - 1][col + 1].getMineCounter() == 0) {
                            revealCell(map[row - 1][col + 1]);
                        }
                        else {
                            map[row - 1][col + 1].reveal();
                        }
                    }
                }
            }
            // left and right neighbors
            if (col - 1 >= 0) {
                if (!map[row][col - 1].isMined() && !map[row][col - 1].isRevealed()) {
                    if (map[row][col - 1].getMineCounter() == 0) {
                        revealCell(map[row][col - 1]);
                    }
                    else {
                        map[row][col - 1].reveal();
                    }
                }
            }

            if (col + 1 < cols) {
                if (!map[row][col + 1].isMined() && !map[row][col + 1].isRevealed()) {
                    if (map[row][col + 1].getMineCounter() == 0) {
                        revealCell(map[row][col + 1]);
                    }
                    else {
                        map[row][col + 1].reveal();
                    }
                }
            }
            // bottom columns
            if (row + 1 < rows) {
                if (col - 1 >= 0) {
                    if (!map[row + 1][col - 1].isMined() && !map[row + 1][col - 1].isRevealed()) {
                        if (map[row + 1][col - 1].getMineCounter() == 0) {
                            revealCell(map[row + 1][col - 1]);
                        }
                        else {
                            map[row + 1][col - 1].reveal();
                        }
                    }
                }
                if (!map[row + 1][col].isMined() && !map[row + 1][col].isRevealed()) {
                    if (map[row + 1][col].getMineCounter() == 0) {
                        revealCell(map[row + 1][col]);
                    }
                    else {
                        map[row + 1][col].reveal();
                    }
                }
                if (col + 1 < cols) {
                    if (!map[row + 1][col + 1].isMined() && !map[row + 1][col + 1].isRevealed()) {
                        if (map[row + 1][col + 1].getMineCounter() == 0) {
                            revealCell(map[row + 1][col + 1]);
                        }
                        else {
                            map[row + 1][col + 1].reveal();
                        }
                    }
                }
            }
        }
    }

    void addCellAt(Cell cell, int row, int col) {
        cell.setRow(row);
        cell.setCol(col);
        map[row][col] = cell;
    }

    private int[][] getSimplexNoiseArray(float scale) {
        SimplexNoise simplexNoise = new SimplexNoise();
        float currentValue;
        int currentInt;
        int[][] output = new int[rows][cols];
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                currentValue = (simplexNoise.noise(i * scale, j * scale) * 50) + 50;
                currentInt = (int)Math.abs(currentValue);
                output[i][j] = currentInt;
            }
        }
        return output;
    }

    private void intializeFirstIteration() {
        int[][] noiseArray1 = getSimplexNoiseArray(0.1f);
        /*
        generate a fixed number of mines
        go through all cells
        assign the mines based on data from the simplex noise array
        remove some of the mines randomly
         */
        int randomNumber;
        int x;
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                randomNumber = rng(1, 2);
                x = noiseArray1[i][j];
                if (x >= 40 && x <= 60 && randomNumber == 1) {
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

    void initialize() {
        intializeFirstIteration();
    }

    boolean checkWin() {
        int counter = 0;
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                if (map[i][j].isRevealed() && !map[i][j].isMined()) {
                    ++counter;
                }
            }
        }
        return counter == (rows * cols) - mineCount;
    }

    boolean checkLose() {
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
