package uk.ac.ljmu.cwk2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * DiceGame is the main menu screen for the game, it
 * shows some introductory text and displays the currently
 * logged in users as well as having buttons which lead to
 * all of the other screens
 */
public class DiceGame extends AppCompatActivity {

    private DBHandler vsoh;

    /**
     * This onCreate pops up a toast with some introductory text
     * as well as displaying the names of the logged in users if
     * any are logged in
     *
     * @param savedInstanceState passes the Bundle of current application
     *                           data for screen population
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice_game);
        vsoh = new DBHandler(this);

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;

        TextView p1 = (TextView) findViewById(R.id.playerOne);
        TextView p2 = (TextView) findViewById(R.id.playerTwo);

        p1.setText("1: " + vsoh.getLoggedUser("player_one"));
        p2.setText("2: " + vsoh.getLoggedUser("player_two"));

        Toast toast = Toast.makeText(context, R.string.intro, duration);
        toast.show();
    }

    /**
     * Uses an intent to start a new UserScreen Activity
     *
     * @param view the relevant view
     */
    public void userScreen(View view) {
        Intent intent = new Intent(this, UserScreen.class);
        startActivity(intent);
    }

    /**
     * Uses an intent to start a new logScreen Activity
     *
     * @param view the relevant view
     */
    public void logScreen(View view) {
        Intent intent = new Intent(this, LoginScreen.class);
        startActivity(intent);
    }

    /**
     * Uses an intent to start a new playScreen Activity
     *
     * @param view the relevant view
     */
    public void playScreen(View view) {
        Intent intent = new Intent(this, PlayScreen.class);
        startActivity(intent);
    }

    /**
     * Uses an intent to start a new highScreen Activity
     *
     * @param view the relevant view
     */
    public void highScreen(View view) {
        Intent intent = new Intent(this, HighScreen.class);
        startActivity(intent);
    }

    /**
     * Uses an intent to start a new bonusGame Activity
     *
     * @param view the relevant view
     */
    public void bonusGame(View view) {
        Intent intent = new Intent(this, RollItOnTopPlay.class);
        startActivity(intent);
    }

}
