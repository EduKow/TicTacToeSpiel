package com.example.tictactoespiel;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Namen der Textview werden verteilt
    private TextView playerOneScore, playerTwoScore, playerStatus;

    // Button wird erstellt
    private Button [] buttons = new Button[9];
    // Name für erstellten Button wird zugeteilt
    private Button resetGame;

    private int playerOneScoreCount, playerTwoScoreCount, rountCount;
    boolean activePlayer;

    //p1 => 0
    //p2 => 1
    // empty =>2
    int [] gameState = {2,2,2,2,2,2,2,2,2};

    // Gewinnmöglichkeiten werden erzeugt
    int[][] winningPositions = {
            {0,1,2}, {3,4,5}, {6,7,8},  //rows
            {0,3,6}, {1,4,7}, {2,5,8},  //columns
            {0,4,8}, {2,4,6}            //cross
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerOneScore = (TextView) findViewById(R.id.playerOneScore);
        playerTwoScore = (TextView) findViewById(R.id.playerTwoScore);
        playerStatus = (TextView) findViewById(R.id.playerStatus);

        resetGame = (Button) findViewById(R.id.resetGame);

        for(int i=0; i< buttons.length; i++){
            String buttonID = "btn_" + i;
            int rescourceID = getResources().getIdentifier(buttonID, "id", getPackageName());
            buttons[i] = (Button) findViewById(rescourceID);
            buttons[i].setOnClickListener(this);

        }

        rountCount= 0;
        playerOneScoreCount = 0;
        playerTwoScoreCount= 0;
        activePlayer= true;

    }

    @Override
    public void onClick(View v) {
        if(!((Button)v).getText().toString().equals("")){
            return;
        }
        String buttonID = v.getResources().getResourceEntryName(v.getId()); //btn_2
        int gameStatePointer = Integer.parseInt(buttonID.substring(buttonID.length()-1, buttonID.length())); //2

        // Farben vom Button werden eingerichtet
        // https://www.codexpedia.com/android/list-of-color-names-and-color-code-for-android/
        if(activePlayer) {
            ((Button) v).setText("X");
            ((Button) v).setTextColor(Color.parseColor("#FF0000"));  //#FFC34A
            gameState[gameStatePointer] = 0;
        }else{
            ((Button) v).setText("O");
            ((Button) v).setTextColor(Color.parseColor("#00008B")); //70FFEA
            gameState[gameStatePointer] = 1;
        }

        rountCount++;

        // Gewinner wird ausgewertet und angezeigt
        if(checkWinner()){
            if(activePlayer){
                playerOneScoreCount++;
                updatePlayerScore();
                Toast.makeText(this, "Player One Won!", Toast.LENGTH_SHORT).show();
                playAgain();
            }else {
                playerTwoScoreCount++;
                updatePlayerScore();
                Toast.makeText(this, "Player Two Won!", Toast.LENGTH_SHORT).show();
                playAgain();
            }
        }else if(rountCount == 9){
            playAgain();
            Toast.makeText(this, "No Winner!", Toast.LENGTH_SHORT).show();
        }else{
            activePlayer = !activePlayer;
        }

        // Gesamtepunktstand: Gibt an welcher Spieler in Führung liegt
        if(playerOneScoreCount > playerTwoScoreCount) {
            playerStatus.setText("Player One is Winning!");
        }else if (playerTwoScoreCount > playerOneScoreCount) {
            playerStatus.setText("Player Two is Winning!");
        }else{
            playerStatus.setText("");
        }

        // ScoreCount wird geupdatet wenn ein Spieler gewinnt mit +1 oder 0
        resetGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAgain();
                playerOneScoreCount = 0;
                playerTwoScoreCount = 0;
                playerStatus.setText("");
                updatePlayerScore();
            }
        });
    }

    // Überprüft ob ein Spieler gewonnen hat
    public boolean checkWinner(){
        boolean winnerResult = false;

        for(int[] winningPosition : winningPositions){
            if(gameState[winningPosition[0]] == gameState[winningPosition[1]] && gameState[winningPosition[1]] == gameState[winningPosition[2]] &&
                    gameState[winningPosition[0]] !=2){
                winnerResult = true;
            }
        }
        return winnerResult;
    }

    // Score wird geupdatet
    public void updatePlayerScore(){
        playerOneScore.setText(Integer.toString(playerOneScoreCount));
        playerTwoScore.setText(Integer.toString(playerTwoScoreCount));
    }

    // Neustart wird ausgeführt durch einen Sieg oder durch ein Unentschieden
    public void playAgain(){
        rountCount= 0;
        activePlayer= true;

        for (int i=0; i < buttons.length; i++ ){
            gameState[i] = 2;
            buttons[i].setText("");
        }
    }

}