package amrsaber.connect4;

/**
 * Created by AmrSaber on 10/05/2016.
 */
public class MyConstants {

    public final class Statistics{
        public static final String PREF_EASY_WON = "easyWon";
        public static final String PREF_EASY_LOST = "easyLost";
        public static final String PREF_EASY_TIE = "easyTie";

        public static final String PREF_MED_WON = "medWon";
        public static final String PREF_MED_LOST = "medLost";
        public static final String PREF_MED_TIE = "medTie";

        public static final String PREF_HARD_WON = "hardWon";
        public static final String PREF_HARD_LOST = "hardLost";
        public static final String PREF_HARD_TIE = "hardTie";
    }

    public final class GameOptions{
        public static final String DIFFICULTY = "difficulty";
        public static final int MODE_EASY = 2;
        public static final int MODE_MEDIUM = 4;
        public static final int MODE_HARD = 6;
    }

    public final class GameBoard{
        public static final int PLAYER = 1;
        public static final int DROID = 2;
    }

}
