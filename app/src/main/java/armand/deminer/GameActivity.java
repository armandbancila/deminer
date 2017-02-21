package armand.deminer;

import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private int r = 20;
    private int c = 20;
    // private int m = 5;
    private int flags = 0;
    private boolean flagMode = false;
    private boolean restartMode = false;
    private Button toggleButton;
    private GameMap gameMap;
    TextView flagsNumberView;

    @Override
    public void onClick(View view) {
        final int id = view.getId();
        System.out.println(id);
        if (gameMap.getCell(id) != null) {
            gameMap.revealCell(gameMap.getCell(id));
            if (gameMap.checkLose()) {
                gameMap.revealMap();
                flagsNumberView.setText("Loss. Click button to restart!");
                restartMode = true;
                toggleButton.setText("RESTART");

            } else if (gameMap.checkWin()) {
                flagsNumberView.setText("Win. Click button to restart!");
                restartMode = true;
                toggleButton.setText("RESTART");
            }
        } else if (toggleButton.getId() == id) {
            if (restartMode) {
                restartMode = false;
                toggleButton.setText("Set flag: " + Boolean.toString(flagMode));
                buildGUI();

            }
            else {
                flagMode = !flagMode;
                toggleButton.setText("Set flag: " + Boolean.toString(flagMode));

            }
        }
    }

    private void buildGUI() {
        // main layout of the game, this contains everything
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setId(View.generateViewId());
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        // mainLayout.setWeightSum(5);

        // horizontal, top linear view, displays game-related stats and a button
        LinearLayout infoDisplayLayout = new LinearLayout(this);
        infoDisplayLayout.setId(View.generateViewId());
        infoDisplayLayout.setOrientation(LinearLayout.HORIZONTAL);

        // add a flag counter and a flag / display cell mode toggle button
        flagsNumberView = new TextView(this);
        flagsNumberView.setId(View.generateViewId());
        flagsNumberView.setText(Integer.toString(flags));

        toggleButton = new Button(this);
        toggleButton.setId(View.generateViewId());
        toggleButton.setText("Set flag: " + Boolean.toString(flagMode));
        toggleButton.setOnClickListener(this);

        // add the views to the layouts, add the horizontal, inner, top linear layout to the bigger, main layout
        // have to use layout parameters, no other way
        // 0dp is recommended in xml when you use linear layouts with weights but here using 0 breaks the view
        LinearLayout.LayoutParams infoDisplayLayoutChildParams = new LinearLayout.LayoutParams(0, MATCH_PARENT);
        infoDisplayLayoutChildParams.weight = 1;
        infoDisplayLayout.addView(flagsNumberView, infoDisplayLayoutChildParams);
        infoDisplayLayout.addView(toggleButton, infoDisplayLayoutChildParams);

        LinearLayout.LayoutParams mainLayoutChildParams = new LinearLayout.LayoutParams(MATCH_PARENT, 0, 1);
        mainLayout.addView(infoDisplayLayout, mainLayoutChildParams);
        // TODO: put this ugly code into nice, neat methods / classes

        // create a GridLayout
        GridLayout cellMapLayout = new GridLayout(this);
        cellMapLayout.setId(View.generateViewId());
        cellMapLayout.setColumnCount(c);
        cellMapLayout.setRowCount(r);
        GridLayout.LayoutParams cellMapLayoutParams = new GridLayout.LayoutParams();

        gameMap = new GameMap(r, c);
        for (int row = 0; row < r; ++row) {
            for (int col = 0; col < c; ++col) {
                Button cellButton = new Button(this);
                cellButton.setMinimumWidth(0);
                cellButton.setMinimumHeight(0);
                cellButton.setPadding(0, 0, 0, 0);
                cellButton.setHeight(0);
                cellButton.setWidth(0);
                cellButton.setOnClickListener(this);
                cellButton.setId(View.generateViewId());

                cellMapLayoutParams = new GridLayout.LayoutParams();
                cellMapLayoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, GridLayout.FILL, 1);
                cellMapLayoutParams.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, GridLayout.FILL, 1);
                cellMapLayoutParams.setMargins(0, 0, 0, 0);

                Cell cell = new Cell(cellButton);
                gameMap.addCellAt(cell, row, col);
                cellMapLayout.addView(cellButton, cellMapLayoutParams);
            }
        }

        /*
        Initialize everything related to the gameMap
        have to think when to refresh it frequently, in what method
         */
        gameMap.initialize();

        mainLayoutChildParams = new LinearLayout.LayoutParams(MATCH_PARENT, 0, 9);
        mainLayout.addView(cellMapLayout, mainLayoutChildParams);
        // finishes setting up the entire view
        // set this activity to display the layout of the mainLayout ViewGroup
        LinearLayout.LayoutParams mainLayoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        setContentView(mainLayout, mainLayoutParams);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildGUI();
        gameMap.revealMap();
    }
}