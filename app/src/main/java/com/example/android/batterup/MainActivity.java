package com.example.android.batterup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //Global Variables
    int currentInning = 1;
    int currentHalf = 2;
    int adjustedInning = 1;
    int inningScore = 0;
    //counts
    int ballCount = 0;
    int strikeCount = 0;
    int visitorScore = 0;
    int homeScore = 0;
    int outCount = 0;

    //Major text views
    private TextView ballView;
    private TextView strikeView;
    private TextView outView;
    private TableLayout tblLayout;
    TextView tvHomeTotal;
    TextView tvVisitorTotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ballView = (TextView) findViewById(R.id.tvBall);
        strikeView = (TextView) findViewById(R.id.tvStrike);
        outView = (TextView) findViewById(R.id.tvOut);
        tblLayout = (TableLayout) findViewById(R.id.tableLayout);
        tvHomeTotal = (TextView) findViewById(R.id.homeTotal);
        tvVisitorTotal = (TextView) findViewById(R.id.visitorTotal);

        TextView tvHomeTotal = (TextView) findViewById(R.id.homeTotal);
        TextView tvVisitorTotal = (TextView) findViewById(R.id.visitorTotal);
        tvHomeTotal.setText(String.valueOf(homeScore));
        tvVisitorTotal.setText(String.valueOf(visitorScore));

        displayCounts();
        updateCurrentInning();
    }

    //onclick events for Strike, Ball, Foul Ball, Hit and Out
    public void clickStrike(View v) {
        /*Strike -> Each click increases STRIKE Count, clicked three times OUT gets
        updated and rest zeroed out.*/
        updateStrikeCount();
    }

    public void clickBall(View v) {
    /*just keeps increasing. 4th click 1st base gets changed to yellow and
        any other consecutive base moves one. If third base highlighted it goes
        home score is updated.*/
        ballCount = ballCount + 1;
        if (ballCount == 4) {
            //take base for now manually move runners
            zeroOutCounts();
        }
        displayCounts();

    }

    public void clickFoulBall(View v) {
        /*Foul Ball -> If STRIKES less than 2 increase to two if not then don t do
            anything.*/
        if (strikeCount < 2) {
            updateStrikeCount();
        }

    }

    public void clickHit(View v) {
        /*Hit -> zero out count. Have to manually move runners as it won t be known
        how many bases.*/
        zeroOutCounts();

    }

    public void clickOut(View v) {
        //Out -> zero out count and add one to Outs. If Outs hits 3 increase inning
        updateOutCount();
    }

    public void clickAtBat(View v) {
        TextView tvAtBat = (TextView) findViewById(R.id.tvAtBat);
        tvAtBat.setCursorVisible(true);
        tvAtBat.setFocusableInTouchMode(true);
        tvAtBat.setInputType(InputType.TYPE_CLASS_TEXT);
        tvAtBat.requestFocus(); //to trigger the soft input
    }

    public void clickReset(View v){
        zeroOutCounts();
        currentInning = 1;
        homeScore = 0;
        visitorScore = 0;
        inningScore = 0;
        updateCurrentInning();
        clearScoreBoard();
        updateScoreTotal();
    }
    /*Onclick for Home Base
    * For each click of Home Button add 1 to current Inning (1=top of 1st Visitor,
    * 2 = bottom of 1st Home)*/

    public void baseClicked(View v) {
        updateInningScore();
    }

    //Display counts
    public void displayCounts() {
//        TextView ballView = (TextView) findViewById(R.id.tvBall);
//        TextView strikeView = (TextView) findViewById(R.id.tvStrike);
//        TextView outView = (TextView) findViewById(R.id.tvOut);
        ballView.setText(String.valueOf(ballCount));
        strikeView.setText(String.valueOf(strikeCount));
        outView.setText(String.valueOf(outCount));
    }

    //Zero out counts
    public void zeroOutCounts() {
        ballCount = 0;
        strikeCount = 0;
        //outCount = 0; //I can't zero this out for each batter only for 3 outs
        displayCounts();
    }

    //Update out count
    public void updateOutCount() {
        //Add one to out count
        outCount = outCount + 1;
        //check to see if 3 if so update inning and zero out counts
        if (outCount == 3) {
            //updateCurrentInning
            updateCurrentInning();
            outCount = 0;
            inningScore = 0;

        }
        zeroOutCounts();
        //then call display counts
        displayCounts();
    }

    //Update strike count
    public void updateStrikeCount() {
        strikeCount = strikeCount + 1;
        if (strikeCount == 3) {
            updateOutCount();
        } else {
            displayCounts();
        }
    }

    //Update Current Inning
    public void updateCurrentInning() {

        TableRow rowPrevious = (TableRow) tblLayout.getChildAt(currentHalf);
        TextView tvPrevious = (TextView) rowPrevious.getChildAt(adjustedInning);
        //tvPrevious.setBackgroundResource(R.color.white);
        tvPrevious.setBackgroundResource(R.drawable.border);
        //catAll.setBackgroundResource(R.drawable.myshape);

        //To figure out current row
        //Check if number is even if so then row is home Actually 3
        //To get cell number for Row 2 halve the number
        //To get cell number for Row 1 Visitor halve then round up.
        if ((currentInning % 2) == 0) {
            currentHalf = 2;
            adjustedInning = currentInning / 2;
        } else {
            currentHalf = 1;
            if (currentInning > 2) {
                adjustedInning = (int) (Math.ceil((double) currentInning / 2));
            } else {
                adjustedInning = currentInning; // should be 1
            }
        }

        TableRow row = (TableRow) tblLayout.getChildAt(currentHalf);
        TextView tv = (TextView) row.getChildAt(adjustedInning);
        tv.setBackgroundResource(R.color.yellow);
        if (currentInning < 19) {
            currentInning = currentInning + 1;
        }
    }

    public void updateInningScore() {

        inningScore = inningScore + 1;
        if (currentHalf == 2) {
            homeScore = homeScore + 1;
        } else {
            visitorScore = visitorScore + 1;
        }
        //get current row and half then update that currentInningScore
//        TableLayout tblLayout = (TableLayout) findViewById(R.id.tableLayout);
        TableRow row = (TableRow) tblLayout.getChildAt(currentHalf);
        TextView tv = (TextView) row.getChildAt(adjustedInning);
        tv.setText(String.valueOf(inningScore));
        updateScoreTotal();


    }

    public void clearScoreBoard(){
        TableRow row;
        TextView tv;
        //start at row 1 and column 2
        for (int i = 1; i <= 2; i++){
            row = (TableRow) tblLayout.getChildAt(i);
            for (int j = 1; j <= 9; j++){
                tv = (TextView) row.getChildAt(j);
                tv.setText("");
            }
        }

    }

    public void updateScoreTotal(){
        tvVisitorTotal.setText(String.valueOf(visitorScore));
        tvHomeTotal.setText(String.valueOf(homeScore));
    }

}
