package armand.deminer;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class GameActivity extends AppCompatActivity {
    private int r = 10;
    private int c = 10;
    private int m = 5;
    private int flags = 0;
    private boolean flagMode = false;

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // main layout of the game, this contains everything
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setId(0);
        // mainLayout.setWeightSum(5);

        // horizontal, top linear view, displays game-related stats and a button
        LinearLayout infoDisplayLayout = new LinearLayout(this);
        infoDisplayLayout.setOrientation(LinearLayout.HORIZONTAL);

        // add a flag counter and a flag / display cell mode toggle button
        TextView flagsNumberView = new TextView(this);
        flagsNumberView.setText(Integer.toString(flags));

        Button toggleButton = new Button(this);
        toggleButton.setText(Boolean.toString(flagMode));


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
        cellMapLayout.setColumnCount(c);
        cellMapLayout.setRowCount(r);
        GridLayout.LayoutParams cellMapLayoutParams = new GridLayout.LayoutParams();

        for (int row = 0; row < r; ++row) {
            for (int col = 0; col < c; ++col) {
                Button cell = new Button(this);
                cell.setMinimumWidth(0);
                cell.setMinimumHeight(0);
                cell.setPadding(0, 0, 0, 0);
                cell.setHeight(0);
                cell.setWidth(0);
                // cell.setBackgroundColor(Color.parseColor("#000000"));

                cellMapLayoutParams = new GridLayout.LayoutParams();
                cellMapLayoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, GridLayout.FILL, 1);
                cellMapLayoutParams.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, GridLayout.FILL, 1);
                cellMapLayoutParams.setMargins(1, 1, 1, 1);
                cellMapLayout.addView(cell, cellMapLayoutParams);
            }
        }

        mainLayoutChildParams = new LinearLayout.LayoutParams(MATCH_PARENT, 0, 9);
        mainLayout.addView(cellMapLayout, mainLayoutChildParams);

        // finishes setting up the entire view
        // set this activity to display the layout of the mainLayout ViewGroup
        LinearLayout.LayoutParams mainLayoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        setContentView(mainLayout, mainLayoutParams);
    }
}
