package armand.deminer;

import android.widget.Button;

public class Cell {
    private Button button;
    private int buttonId;
    private boolean hasFlag = false;
    private boolean hasMine = false;
    private boolean revealed = false;
    private int mineCounter = 0;

    public Cell(Button button) {
        setButton(button);

    }

    public void reveal() {
        this.revealed = true;
        if (this.hasMine) {
            button.setText("X");
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
