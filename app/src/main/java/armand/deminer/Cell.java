package armand.deminer;

import android.graphics.Color;
import android.widget.Button;

public class Cell {
    private Button button;
    private int buttonId;
    private boolean hasFlag = false;
    private boolean hasMine = false;
    private boolean revealed = false;
    private int row;
    private int col;
    private int mineCounter = 0;

    public Cell(Button button) {
        setButton(button);

    }

    public void reveal() {
        this.revealed = true;
        if (this.hasMine) {
            button.setText("X");
            button.setBackgroundColor(Color.RED);
        }
        else {
            button.setText(Integer.toString(mineCounter));
        }
    }

    public void incrementMineCounter(){
        ++mineCounter;

    }

    public void setButton(Button button){
        this.button = button;
        this.buttonId = button.getId();
    }

    public void toggleFlag() {
        this.hasFlag = !this.hasFlag;
    }

    public void setMine(boolean hasMine) {
        this.hasMine = hasMine;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

    public int getId () {
        return buttonId;
    }

    public Button getButton() {
        return button;
    }

    public boolean isFlagged() {
        return hasFlag;
    }

    public boolean isMined() {
        return hasMine;
    }

    public boolean isRevealed() {
        return revealed;
    }
}
