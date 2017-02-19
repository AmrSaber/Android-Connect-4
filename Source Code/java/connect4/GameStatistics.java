package amrsaber.connect4;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class GameStatistics extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_statistics);

        TextView easyWon = (TextView)findViewById(R.id.easyWon);
        TextView easyLost = (TextView)findViewById(R.id.easyLost);
        TextView easyTie = (TextView)findViewById(R.id.easyTie);

        TextView medWon = (TextView)findViewById(R.id.medWon);
        TextView medLost = (TextView)findViewById(R.id.medLost);
        TextView medTie = (TextView)findViewById(R.id.medTie);

        TextView hardWon = (TextView)findViewById(R.id.hardWon);
        TextView hardLost = (TextView)findViewById(R.id.hardLost);
        TextView hardTie = (TextView)findViewById(R.id.hardTie);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());


        easyWon.setText(String.valueOf(sp.getInt(MyConstants.Statistics.PREF_EASY_WON, 0)));
        easyLost.setText(String.valueOf(sp.getInt(MyConstants.Statistics.PREF_EASY_LOST, 0)));
        easyTie.setText(String.valueOf(sp.getInt(MyConstants.Statistics.PREF_EASY_TIE, 0)));

        medWon.setText(String.valueOf(sp.getInt(MyConstants.Statistics.PREF_MED_WON, 0)));
        medLost.setText(String.valueOf(sp.getInt(MyConstants.Statistics.PREF_MED_LOST, 0)));
        medTie.setText(String.valueOf(sp.getInt(MyConstants.Statistics.PREF_MED_TIE, 0)));

        hardWon.setText(String.valueOf(sp.getInt(MyConstants.Statistics.PREF_HARD_WON, 0)));
        hardLost.setText(String.valueOf(sp.getInt(MyConstants.Statistics.PREF_HARD_LOST, 0)));
        hardTie.setText(String.valueOf(sp.getInt(MyConstants.Statistics.PREF_HARD_TIE, 0)));
    }
}
