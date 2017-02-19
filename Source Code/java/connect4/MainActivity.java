package amrsaber.connect4;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    Button play, options, stats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = (Button)findViewById(R.id.playGame);
        options = (Button)findViewById(R.id.options);
        stats = (Button)findViewById(R.id.stat);

        play.setOnClickListener(new PlayListener());

        options.setOnClickListener(new OptionListener());

        stats.setOnClickListener(new StatsListener());
    }

    //----------------------------------------------------------------------------------------------

    private class PlayListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
            startActivity(new Intent(MainActivity.this,GameBoard.class));
        }
    }

    //----------------------------------------------------------------------------------------------

    private class OptionListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
            startActivity(new Intent(MainActivity.this,GameOptions.class));
        }
    }

    //----------------------------------------------------------------------------------------------

    private class StatsListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
            startActivity(new Intent(MainActivity.this,GameStatistics.class));
        }
    }

    //----------------------------------------------------------------------------------------------


}
