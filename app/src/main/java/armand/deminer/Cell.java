package armand.deminer;

import android.graphics.Color;
import android.widget.Button;

class Cell {
    private Button button;
    private int buttonId;
    private boolean hasFlag = false;
    private boolean hasMine = false;
    private boolean revealed = false;
    private int row;
    private int col;
    private int mineCounter = 0;

    Cell(Button button) {
        setButton(button);
    }

    void reveal() {
        this.revealed = true;
        if (this.hasMine) {
            button.setText("X");
            button.setBackgroundColor(Color.RED);
        } else {
            if (mineCounter != 0) {
                button.setText(Integer.toString(mineCounter));
            }
            button.setBackgroundColor(Color.GREEN);
        }
    }

    void incrementMineCounter() {
        ++mineCounter;
    }

    private void setButton(Button button) {
        this.button = button;
        this.buttonId = button.getId();
    }

    void toggleFlag() {
        this.hasFlag = !this.hasFlag;
    }

    void setMine(boolean hasMine) {
        this.hasMine = hasMine;
    }

    void setRow(int row) {
        this.row = row;
    }

    void setCol(int col) {
        this.col = col;
    }

    int getRow() {
        return this.row;
    }

    int getCol() {
        return this.col;
    }

    int getId() {
        return buttonId;
    }

    int getMineCounter() {
        return this.mineCounter;
    }

    boolean isMined() {
        return hasMine;
    }

    boolean isRevealed() {
        return revealed;
    }
}