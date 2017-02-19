package amrsaber.connect4;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

public class GameOptions extends AppCompatActivity {

    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

    private RadioButton easy, med, hard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_options);

        sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        spEditor = sp.edit();

        easy = (RadioButton)findViewById(R.id.setEasy);
        med = (RadioButton)findViewById(R.id.setMed);
        hard = (RadioButton)findViewById(R.id.setHard);

        setState(sp.getInt(MyConstants.GameOptions.DIFFICULTY, MyConstants.GameOptions.MODE_MEDIUM));

        easy.setOnClickListener(new SetMode(MyConstants.GameOptions.MODE_EASY));
        med.setOnClickListener(new SetMode(MyConstants.GameOptions.MODE_MEDIUM));
        hard.setOnClickListener(new SetMode(MyConstants.GameOptions.MODE_HARD));

        findViewById(R.id.resetStat).setOnClickListener(new ResetStat());


    }

    //----------------------------------------------------------------------------------------------

    private class ResetStat implements View.OnClickListener{
        @Override
        public void onClick(View v){
            spEditor.putInt(MyConstants.Statistics.PREF_EASY_WON, 0);
            spEditor.putInt(MyConstants.Statistics.PREF_EASY_LOST, 0);
            spEditor.putInt(MyConstants.Statistics.PREF_EASY_TIE, 0);

            spEditor.putInt(MyConstants.Statistics.PREF_MED_WON, 0);
            spEditor.putInt(MyConstants.Statistics.PREF_MED_LOST, 0);
            spEditor.putInt(MyConstants.Statistics.PREF_MED_TIE, 0);

            spEditor.putInt(MyConstants.Statistics.PREF_HARD_WON, 0);
            spEditor.putInt(MyConstants.Statistics.PREF_HARD_LOST, 0);
            spEditor.putInt(MyConstants.Statistics.PREF_HARD_TIE, 0);

            spEditor.apply();

            Toast.makeText(getBaseContext(), "Statistics Reset", Toast.LENGTH_SHORT).show();
        }
    }

    //----------------------------------------------------------------------------------------------

    private class SetMode implements View.OnClickListener{
        private final int mode;

        public SetMode(int m){
            mode = m;
        }
        @Override
        public void onClick(View v){
            spEditor.putInt(MyConstants.GameOptions.DIFFICULTY, mode);
            spEditor.apply();
        }
    }

    //----------------------------------------------------------------------------------------------

    private void setState(int m){

        //initialize the 3 radio buttons
        easy.setChecked(false);
        med.setChecked(false);
        hard.setChecked(false);

        switch(m){
            case MyConstants.GameOptions.MODE_EASY:
                easy.setChecked(true);
            break;
            case MyConstants.GameOptions.MODE_MEDIUM:
                med.setChecked(true);
            break;
            case MyConstants.GameOptions.MODE_HARD:
                hard.setChecked(true);
            break;
        }

    }

    //----------------------------------------------------------------------------------------------
}
