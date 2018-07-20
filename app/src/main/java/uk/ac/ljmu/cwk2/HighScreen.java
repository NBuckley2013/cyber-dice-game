package uk.ac.ljmu.cwk2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * The HighScreen Displays high scores to the User
 */
public class HighScreen extends AppCompatActivity {
    private DBHandler vsoh;

    /**
     * This onCreate sets out the layout and dynamically rendered
     * visual objects and calls the displayHighScores method to populate
     * high score data from the database
     *
     * @param savedInstanceState passes the Bundle of current application
     *                           data for screen population
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_screen);
        vsoh = new DBHandler(this);

        // Intent intent = getIntent();
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setTextColor(getResources().getColor(R.color.colorAccent));
        textView.setText(R.string.high);

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_high_screen);
        layout.addView(textView);

        displayHighScores();
    }

    /**
     * Displays a ranked list of the top 5 high scores after puling
     * the information from the database
     */
    protected void displayHighScores() {
        TextView userList = (TextView) findViewById(R.id.userList);
        TextView scoreList = (TextView) findViewById(R.id.scoreList);
        TextView rankList = (TextView) findViewById(R.id.rankList);
        TextView gamesPlayedList = (TextView) findViewById(R.id.gamesPlayedList);
        TextView winsList = (TextView) findViewById(R.id.winsList);
        List<String> rank = new ArrayList<>();
        List<String> users = new ArrayList<>();
        List<String> scores = new ArrayList<>();
        List<String> gamesPlayed = new ArrayList<>();
        List<String> wins = new ArrayList<>();
        rank.add("1st\n");
        rank.add("2nd\n");
        rank.add("3rd\n");
        rank.add("4th\n");
        rank.add("5th\n");
        users.add("");
        scores.add("");
        gamesPlayed.add("");
        wins.add("");

        if (!vsoh.getHighScores().isEmpty()) {

            for (User user : vsoh.getHighScores()) {
                if (user.getWins() != 0) {
                    double winRate = (user.getWins() / user.getGamesPlayed());
                    String winRatePercent = MessageFormat.format("{0,number,percent}", winRate);

                    users.add(user.getUsername() + ": \n");
                    scores.add(winRatePercent + "\n");
                    gamesPlayed.add(Double.toString(user.getGamesPlayed()) + "\n");
                    wins.add(Double.toString(user.getWins()) + "\n");

                    rankList.setText(rank.toString().replace("[", "").replace("]", "").replace(",", ""));
                    userList.setText(users.toString().replace("[", "").replace("]", "").replace(",", ""));
                    scoreList.setText(scores.toString().replace("[", "").replace("]", "").replace(",", ""));
                    gamesPlayedList.setText(gamesPlayed.toString().replace("[", "").replace("]", "").replace(",", ""));
                    winsList.setText(wins.toString().replace("[", "").replace("]", "").replace(",", ""));
                }
            }
        } else {
            userList.setText("");
            scoreList.setText(R.string.no_scores);
        }
    }
}
