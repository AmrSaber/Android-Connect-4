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
	
	//the game board : 0 = not used; otherwise, each player has his own constant in MyConstants class
    int gameBoard[][] = new int[6][7];
    
    //flag to know whose turn is it (player can't play while the computer is thinking)
    static boolean playerTurn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

		//initially it's players turn (player goes first)
        playerTurn = true;

        //initializing the gameBoard, all zeros, i.e. no place used
        for (int i = 0 ; i < 6 ; i++) {Arrays.fill(gameBoard[i], 0);}

        //applying the listener to all the columns and the buttons inside of them
        //i.e. when the player touches any column at any place (the container or the circuls) the program will understand
        ViewGroup cols =(ViewGroup) findViewById(R.id.wrapper);
        //loop over the 6 columns
        for(int i = 0, k = 0; i < 6 ;k++){
        	//get the next child
            View child = cols.getChildAt(k);
            
            //if it's not a column, continue
            if (!(child instanceof ViewGroup)){continue;}
            ViewGroup col = (ViewGroup) child;
            
            //add the listener to the column itself
            col.setOnClickListener(new PlayListener(i));
            
            //add the listner to each of the column's children
            for(int j = 0 ; j < col.getChildCount() ; j++){
                col.getChildAt(j).setOnClickListener(new PlayListener(i));
            }
            
            //increment i => to count the finished column
            i++;
        }

        //add the listener for the restart button
        findViewById(R.id.restart).setOnClickListener(new RestartListener());

    }

	//take the player number, column number and wanted board and playes in the given place
	//by marking the board and the 2d array
    private void play(int player,int col, int board[][]){
        int colId = getColId(col);
        int row = getFirstAvailableRow(col);	//get row number of the first empty place in the given column
        
        //changes the cirul's color according to the player and mark the array
        if(player == MyConstants.GameBoard.PLAYER){
            ((ViewGroup)findViewById(colId)).getChildAt(6-row).setBackground(getResources().getDrawable(R.drawable.circle_blue));
            board[col][row] = MyConstants.GameBoard.PLAYER;
        }else if(player == MyConstants.GameBoard.DROID){
            ((ViewGroup)findViewById(colId)).getChildAt(6-row).setBackground(getResources().getDrawable(R.drawable.circle_red));
            board[col][row] = MyConstants.GameBoard.DROID;
        }
    }

	//searches in the given column for the first empty place, returns -1 if the column is full
    private int getFirstAvailableRow(int col){
        for(int i = 0 ; i < 7 ; i++){
            if(gameBoard[col][i] == 0){
                return i;
            }
        }
        return -1;
    }

	//given the column's number, return colomn's id in the layout
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
	
	//Searches the board if the some one has won, or if there is a tie, returns 0 if the game is not finished yet
	//PS: I kept the addition | subtraction in the loops' contiditions to make things more clear
    //PLAYER -> player wins, DROID -> droid wins, 0 -> not terminal, -1 -> Tie
    private int getWinner(int b[][]){
		
		//cheks horizontally
        for(int x = 0 ; x < 6-3 ; x++){
            for(int y = 0 ; y < 7 ; y++){
                if(b[x][y] != 0 && b[x][y] == b[x+1][y] && b[x+1][y] == b[x+2][y] && b[x+2][y] == b[x+3][y]){
                    return b[x][y];
                }
            }
        }
		
		//checks vertically
        for(int x = 0 ; x < 6 ; x++){
            for(int y = 0 ; y < 7-3 ; y++){
                if(b[x][y] != 0 && b[x][y] == b[x][y+1] && b[x][y+1] == b[x][y+2] && b[x][y+2] == b[x][y+3]){
                    return b[x][y];
                }
            }
        }
		
		//checks the right-ascending diagonal
        for(int x = 0 ; x < 6-3 ; x++){
            for(int y = 0 ; y < 7-3 ; y++){
                if(b[x][y] != 0 && b[x][y] == b[x+1][y+1] && b[x+1][y+1] == b[x+2][y+2] && b[x+2][y+2] == b[x+3][y+3]){
                    return b[x][y];
                }
            }
        }
		
		//checks the left-ascending diagonal
        for(int x = 0 ; x < 6-3 ; x++){
            for(int y = 3 ; y < 7 ; y++){
                if(b[x][y] != 0 && b[x][y] == b[x+1][y-1] && b[x+1][y-1] == b[x+2][y-2] && b[x+2][y-2] == b[x+3][y-3]){
                    return b[x][y];
                }
            }
        }
		
		//checks if all the columns are full
        boolean tie = true;
        for(int i = 0 ; i < 6 ; i++){
            tie = (getFirstAvailableRow(i) == -1) && tie;
        }

        if(tie) return -1;

        return 0;
    }
	
	//displays the message with the winner
    private boolean announceWinner(int b[][]){
        int state = getWinner(b);	//get the winner from the previous method
        if (state == 0) return false;	//return (false) if no one wins
        playerTurn = false;		//to disable player's interaction with the board
	
		//show restart button
        findViewById(R.id.restart).setVisibility(View.VISIBLE);
        TextView textView = (TextView) findViewById(R.id.state);	//state label
        textView.setVisibility(View.VISIBLE);	//make it visible
		
		//shared prefrence used to save permenant data on device, here used to save the scoreboard
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = sp.edit();
        int old;

        if(state == MyConstants.GameBoard.PLAYER){
			
			//set the content of the label
            textView.setText(R.string.won);
			
			//get the current difficulty and increment it with default value of easy (for the first time the game is opened)
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
                	//in case the switch came here (with the defualt value given), something must be really wrong (never happened)
                    Toast.makeText(this, "Error while submitting results - Easy", Toast.LENGTH_SHORT).show();
            }   //end switch
	
		//repeat the code in the previous case with the the other possibelities (player lost | tie)
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
		
		//apply changes to the shared prefrence
        editor.apply();
        
        //return true, indicates that the game is done
        return true;
    }

    //----------------------------------------------------------------------------------------------
	
	//listener for the player's touch
    private class PlayListener implements View.OnClickListener{

        int player, col, colId;
		
		//take column's numeber -> to play in that column when invoked
        public PlayListener(int c){
            col = c;
            player = MyConstants.GameBoard.PLAYER;
            colId = getColId(c);
        }

        @Override
        public void onClick(View v){
			//if the column is full or not the player's turn yet, do nothing
            if(getFirstAvailableRow(col) == -1 || !GameBoard.playerTurn) return;
            
            //play in given column
            play(player, col, GameBoard.this.gameBoard);
            
            //if the game is done, do nothing (no need for the program to play)
            if(announceWinner(GameBoard.this.gameBoard)) return;
            
            //Player's turn is done -> now program's turn
            GameBoard.playerTurn = false;
            
            //launch program's thinking thread (thread is used to avoid blocking the UI while doing calculations)
            new DroidThink().execute();

        }
    }

    //----------------------------------------------------------------------------------------------

	//listener for the restart button
    private class RestartListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
			
			//resets all the circuls
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
			
			//resets the board-array
            for (int i = 0 ; i < 6 ; i++) {Arrays.fill(gameBoard[i], 0);}

			//hides it self and the state's textView
            findViewById(R.id.state).setVisibility(View.INVISIBLE);
            v.setVisibility(View.INVISIBLE);
            
            //player's turn to play
            playerTurn = true;
        }
    }

    //----------------------------------------------------------------------------------------------
	
	//does a task in background, for this one it takes nothing (void) and returns integer and it sends no updates
    private class DroidThink extends AsyncTask <Void, Void, Integer>{

        //was made for optimization purposes, not used (yet)
        Map memo;
		
		//max depth for the minimax recursion
        int maxDepth;

		//loops over the columns and gets the final expected value of each column
        private int minimax(){
            List<Integer> list = new ArrayList<>();		//list to save the columns that have the maximum value
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

			//pick a random column from the list (to make the behavior unpredictable)
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
                if(c == cmax){  //if any play from here leads to winning, give greater maximum value (force playing here)
                    max = 200;
                }
                if(c == cmin){  //if any play from here leads to losing, give very low -ve value (force playing away)
                    min = -200;
                }
            }

            if(player == MyConstants.GameBoard.PLAYER){
                return min;
            }else{
                return max;
            }
        }
		
		//cheks the board and tells if the game is done or not
        private boolean isTerminal(int b[][]){
        	//if there is a winner or a tie, then the game is done
            return (getWinner(b) != 0);
        }
		
		
		//get the value of the current board state (the testing board this thread has, not the real board)
		//+ve number ==> good for the Computer (Computer wins), -ve number ==> bad for the Computer (computer loses)
		//0 ==> game not done or tie (nuetral state)
        private int utility(int cd){	//cd == current depth
            int state = getWinner(gameBoard);	//get the state
            //if computer loses returns -ve number that is lower as the computer loses sooner
            if(state == MyConstants.GameBoard.PLAYER){
                return -maxDepth - 1 + cd;
            
            //if the computer wins, returns +ve number that is greater as the computer wins sooner
            }else if(state == MyConstants.GameBoard.DROID){
                return maxDepth + 1 - cd;
            }
            
            //in case no one wins or tie
            return 0;
        }
		
		//intended optimization (with memoization), not implemented yet
        /*@Override
        protected void onPreExecute(){
            memo = new HashMap<Integer[][], Integer[]>();
        }*/

        @Override
        //the work that is actually done
        protected Integer doInBackground(Void ... params){
        	//the value of the difficulty constant determines the maxDepth of the recursion
            maxDepth = PreferenceManager.getDefaultSharedPreferences(getBaseContext())
            							.getInt(MyConstants.GameOptions.DIFFICULTY, MyConstants.GameOptions.MODE_EASY);
            return minimax();	//return the value of the minimax
        }

        @Override
		//executed when doInBackGround is done
        protected void onPostExecute(Integer col){
        	//plays in the place that the program decided
            play(MyConstants.GameBoard.DROID, col,  GameBoard.this.gameBoard);
            
            //if the game is done (Computer won or tie) return, no need to give the player access to the board
            if (announceWinner(GameBoard.this.gameBoard)) return;
            
            //player's turn, the listener will now play in the place he chooses
            GameBoard.playerTurn = true;
        }

    }


}
