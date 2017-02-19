package amrsaber.connect4;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GameBoard extends AppCompatActivity {

    int gameBoard[][] = new int[6][7];
    static boolean playerTurn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        playerTurn = true;

        //initializing the gameBoard
        for (int i = 0 ; i < 6 ; i++) {Arrays.fill(gameBoard[i], 0);}

        //applying the listener to all the columns and the buttons inside of them
        ViewGroup cols =(ViewGroup) findViewById(R.id.wrapper);
        for(int i = 0, k = 0; i < 6 ;k++){
            View child = cols.getChildAt(k);
            if (!(child instanceof ViewGroup)){continue;}
            ViewGroup col = (ViewGroup) child;
            col.setOnClickListener(new PlayListener(i));
            for(int j = 0 ; j < col.getChildCount() ; j++){
                col.getChildAt(j).setOnClickListener(new PlayListener(i));
            }
            i++;
        }

        //add the listener for the restart button
        findViewById(R.id.restart).setOnClickListener(new RestartListener());

    }


    private void play(int player,int col, int board[][]){
        int colId = getColId(col);
        int row = getFirstAvailableRow(col);
        if(player == MyConstants.GameBoard.PLAYER){
            ((ViewGroup)findViewById(colId)).getChildAt(6-row).setBackground(getResources().getDrawable(R.drawable.circle_blue));
            board[col][row] = MyConstants.GameBoard.PLAYER;
        }else if(player == MyConstants.GameBoard.DROID){
            ((ViewGroup)findViewById(colId)).getChildAt(6-row).setBackground(getResources().getDrawable(R.drawable.circle_red));
            board[col][row] = MyConstants.GameBoard.DROID;
        }
    }


    private int getFirstAvailableRow(int col){

        for(int i = 0 ; i < 7 ; i++){
            if(gameBoard[col][i] == 0){
                return i;
            }
        }
        return -1;
    }


    private int getColId(int col){
        // no need for break, return terminates the switch anyway
        switch(col){
            case 0:
                return R.id.col1;
            case 1:
                return R.id.col2;
            case 2:
                return R.id.col3;
            case 3:
                return R.id.col4;
            case 4:
                return R.id.col5;
            case 5:
                return R.id.col6;
            default:
                return -1;
        }
    }

    //PLAYER -> player wins, DROID -> droid wins, 0 -> not terminal, -1 -> Tie
    private int getWinner(int b[][]){

        for(int x = 0 ; x < 6-3 ; x++){
            for(int y = 0 ; y < 7 ; y++){
                if(b[x][y] != 0 && b[x][y] == b[x+1][y] && b[x+1][y] == b[x+2][y] && b[x+2][y] == b[x+3][y]){
                    return b[x][y];
                }
            }
        }

        for(int x = 0 ; x < 6 ; x++){
            for(int y = 0 ; y < 7-3 ; y++){
                if(b[x][y] != 0 && b[x][y] == b[x][y+1] && b[x][y+1] == b[x][y+2] && b[x][y+2] == b[x][y+3]){
                    return b[x][y];
                }
            }
        }

        for(int x = 0 ; x < 6-3 ; x++){
            for(int y = 0 ; y < 7-3 ; y++){
                if(b[x][y] != 0 && b[x][y] == b[x+1][y+1] && b[x+1][y+1] == b[x+2][y+2] && b[x+2][y+2] == b[x+3][y+3]){
                    return b[x][y];
                }
            }
        }

        for(int x = 0 ; x < 6-3 ; x++){
            for(int y = 3 ; y < 7 ; y++){
                if(b[x][y] != 0 && b[x][y] == b[x+1][y-1] && b[x+1][y-1] == b[x+2][y-2] && b[x+2][y-2] == b[x+3][y-3]){
                    return b[x][y];
                }
            }
        }

        boolean tie = true;
        for(int i = 0 ; i < 6 ; i++){
            tie = (getFirstAvailableRow(i) == -1) && tie;
        }

        if(tie) return -1;

        return 0;
    }

    private boolean announceWinner(int b[][]){
        int state = getWinner(b);
        if (state == 0) return false;
        playerTurn = false;

        findViewById(R.id.restart).setVisibility(View.VISIBLE);
        TextView textView = (TextView) findViewById(R.id.state);
        textView.setVisibility(View.VISIBLE);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = sp.edit();
        int old;

        if(state == MyConstants.GameBoard.PLAYER){

            textView.setText(R.string.won);

            switch (sp.getInt(MyConstants.GameOptions.DIFFICULTY, MyConstants.GameOptions.MODE_EASY)){
                case MyConstants.GameOptions.MODE_EASY:
                    old = sp.getInt(MyConstants.Statistics.PREF_EASY_WON, 0);
                    editor.putInt(MyConstants.Statistics.PREF_EASY_WON, old + 1);
                    break;
                case MyConstants.GameOptions.MODE_MEDIUM:
                    old = sp.getInt(MyConstants.Statistics.PREF_MED_WON, 0);
                    editor.putInt(MyConstants.Statistics.PREF_MED_WON, old + 1);
                    break;
                case MyConstants.GameOptions.MODE_HARD:
                    old = sp.getInt(MyConstants.Statistics.PREF_HARD_WON, 0);
                    editor.putInt(MyConstants.Statistics.PREF_HARD_WON, old + 1);
                    break;
                default:
                    Toast.makeText(this, "Error while submitting results - Easy", Toast.LENGTH_SHORT).show();
            }   //end switch

        }else if (state == MyConstants.GameBoard.DROID){

            textView.setText(R.string.lost);

            switch (sp.getInt(MyConstants.GameOptions.DIFFICULTY, MyConstants.GameOptions.MODE_EASY)){
                case MyConstants.GameOptions.MODE_EASY:
                    old = sp.getInt(MyConstants.Statistics.PREF_EASY_LOST, 0);
                    editor.putInt(MyConstants.Statistics.PREF_EASY_LOST, old + 1);
                    break;
                case MyConstants.GameOptions.MODE_MEDIUM:
                    old = sp.getInt(MyConstants.Statistics.PREF_MED_LOST, 0);
                    editor.putInt(MyConstants.Statistics.PREF_MED_LOST, old + 1);
                    break;
                case MyConstants.GameOptions.MODE_HARD:
                    old = sp.getInt(MyConstants.Statistics.PREF_HARD_LOST, 0);
                    editor.putInt(MyConstants.Statistics.PREF_HARD_LOST, old + 1);
                    break;
                default:
                    Toast.makeText(this, "Error while submitting results - Medium", Toast.LENGTH_SHORT).show();
            }//end switch

        }else{

            textView.setText(R.string.tie);

            switch (sp.getInt(MyConstants.GameOptions.DIFFICULTY, MyConstants.GameOptions.MODE_EASY)){
                case MyConstants.GameOptions.MODE_EASY:
                    old = sp.getInt(MyConstants.Statistics.PREF_EASY_TIE, 0);
                    editor.putInt(MyConstants.Statistics.PREF_EASY_TIE, old + 1);
                    break;
                case MyConstants.GameOptions.MODE_MEDIUM:
                    old = sp.getInt(MyConstants.Statistics.PREF_MED_TIE, 0);
                    editor.putInt(MyConstants.Statistics.PREF_MED_TIE, old + 1);
                    break;
                case MyConstants.GameOptions.MODE_HARD:
                    old = sp.getInt(MyConstants.Statistics.PREF_HARD_TIE, 0);
                    editor.putInt(MyConstants.Statistics.PREF_HARD_TIE, old + 1);
                    break;
                default:
                    Toast.makeText(this, "Error while submitting results - Hard", Toast.LENGTH_SHORT).show();
            }//end switch

        }//end if

        editor.apply();
        return true;
    }

    //----------------------------------------------------------------------------------------------

    private class PlayListener implements View.OnClickListener{

        int player, col, colId;

        public PlayListener(int c){
            col = c;
            player = MyConstants.GameBoard.PLAYER;
            colId = getColId(c);
        }

        @Override
        public void onClick(View v){

            if(getFirstAvailableRow(col) == -1 || !GameBoard.playerTurn) return;
            play(player, col, GameBoard.this.gameBoard);
            if(announceWinner(GameBoard.this.gameBoard)) return;
            GameBoard.playerTurn = false;
            new DroidThink().execute();

        }
    }

    //----------------------------------------------------------------------------------------------

    private class RestartListener implements View.OnClickListener{
        @Override
        public void onClick(View v){

            ViewGroup cols =(ViewGroup) findViewById(R.id.wrapper);
            for(int i = 0, k = 0; i < 6 ;k++){
                View child = cols.getChildAt(k);
                if (!(child instanceof ViewGroup)){continue;}
                ViewGroup col = (ViewGroup) child;
                for(int j = 0 ; j < col.getChildCount() ; j++){
                    col.getChildAt(j).setBackground(getResources().getDrawable(R.drawable.circle_white));
                }
                i++;
            }

            for (int i = 0 ; i < 6 ; i++) {Arrays.fill(gameBoard[i], 0);}

            findViewById(R.id.state).setVisibility(View.INVISIBLE);
            v.setVisibility(View.INVISIBLE);
            playerTurn = true;
        }
    }

    //----------------------------------------------------------------------------------------------

    private class DroidThink extends AsyncTask <Void, Void, Integer>{

        Map memo;

        int maxDepth;

        private int minimax(){
            List<Integer> list = new ArrayList<>();
            int max = -1000000, m;
            int row;
            int d = MyConstants.GameBoard.DROID;
            for(int col = 0 ; col < 6 ; col++){
                row = getFirstAvailableRow(col);
                if(row == -1) continue;
                gameBoard[col][row] = d;
                m = minimax_rec(MyConstants.GameBoard.PLAYER, 0);
                gameBoard[col][row] = 0;
                if(m > max || max == -1000000){
                    max = m;
                    list.clear();
                    list.add(col);
                }else if (m ==  max){
                    list.add(col);
                }
            }

            return list.get(new Random().nextInt(list.size()));
        }

        private int minimax_rec(int player,int cDepth){

            if(isTerminal(gameBoard) || cDepth == maxDepth){
                return utility(cDepth);
            }

            int min = 1000000, max = -1000000, m;
            int cmax = 0, cmin = 0, c = 0;

            for(int col = 0 ; col < 6 ; col++) {
                int row = getFirstAvailableRow(col);
                if (row == -1) continue;
                gameBoard[col][row] = player;
                m = minimax_rec(player ^ 3, cDepth + 1);  //1 ^ 3 = 2 , 2 ^ 3 = 1
                c++;
                gameBoard[col][row] = 0;

                if (m == max && max > 0) {
                    cmax++;
                } else {
                    cmax = 0;
                }
                if (m == min && min < 0) {
                    cmin++;
                } else {
                    cmin = 0;
                }

                max = Math.max(m, max);
                min = Math.min(m, min);

            }

            if(maxDepth > MyConstants.GameOptions.MODE_EASY){   //to adjust difficulty
                if(c == cmax){  //all lead to winning
                    max = 200;
                }
                if(c == cmin){  //all lead to losing
                    min = -200;
                }
            }

            if(player == MyConstants.GameBoard.PLAYER){
                return min;
            }else{
                return max;
            }
        }

        private boolean isTerminal(int b[][]){
            return (getWinner(b) != 0);
        }

        private int utility(int cd){
            int state = getWinner(gameBoard);
            if(state == MyConstants.GameBoard.PLAYER){
                return -maxDepth - 1 + cd;
            }else if(state == MyConstants.GameBoard.DROID){
                return maxDepth + 1 - cd;
            }
            return 0;
        }

        @Override   //@TODO Use memoization to improve performance
        protected void onPreExecute(){
            memo = new HashMap<Integer[][], Integer[]>();
        }

        @Override
        protected Integer doInBackground(Void ... params){
            maxDepth = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt(MyConstants.GameOptions.DIFFICULTY, MyConstants.GameOptions.MODE_EASY);

            return minimax();
        }

        @Override
        protected void onPostExecute(Integer col){
            play(MyConstants.GameBoard.DROID, col,  GameBoard.this.gameBoard);
            if (announceWinner(GameBoard.this.gameBoard)) return;
            GameBoard.playerTurn = true;
        }

    }


}
