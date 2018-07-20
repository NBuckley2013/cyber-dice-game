package uk.ac.ljmu.cwk2;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * PlayScreen Displays the main game screen where game-play takes place
 * <p>
 * The PlayScreen class contains all of the game logic and animations as
 * well as calls out to the methods of an instantiation of the DBHandler
 * class
 */
public class PlayScreen extends AppCompatActivity {

    // TODO: Can we remove diceResultOne and diceResultTwo as they are showing as not accessed
    private Button rollButton;
    private TextView diceResultOne, diceResultTwo, currentPlayer, winningPlayer, currentScore;
    private ImageView diceViewOne, diceViewTwo, flashBang;
    private int player = 1,playerOneScore = 0,playerTwoScore = 0,gamesPlayed = 0, duration = Toast.LENGTH_LONG;

    private DBHandler vsoh;

    MediaPlayer rollSound;

    /**
     * Animates the Dice Rolls
     * <p>
     * Changes the X rotation and alpha of the die images
     * to simulate the effect of a die rolling into view
     * @param diceView the ImageView showing the image of the
     *                 die to animate
     */
    private void animateDice(ImageView diceView){
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(diceView, "rotationX", 0, 360))
                .with(ObjectAnimator.ofFloat(diceView, "alpha", 0, 1));

        set.setStartDelay(0);
        set.setDuration(600);
        set.start();
    }

    /**
     * AsyncCaller Allows the methods inside to run in
     * their own threads
     */
    private class AsyncCaller extends AsyncTask<Void, Void, Void>
    {

        /**
         * onPreExecute animates a flash effect for when a player
         * wins
         *
         * @see PlayScreen#onCreate(Bundle) The flash animation is
         * called from the onCreate method
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            AnimatorSet set2 = new AnimatorSet();
            set2.play(ObjectAnimator.ofFloat(flashBang, "alpha", 0, 1))
                    .with(ObjectAnimator.ofFloat(flashBang, "alpha", 1, 0));

            set2.setStartDelay(0);
            set2.setDuration(500);
            set2.start();

        }

        /**
         * Required ASync method
         * @param params void
         * @return null
         */
        @Override
        protected Void doInBackground(Void... params) {

            /**
             * Commented out, see report.
             *
             * vsoh.updateGamesPlayed("player_one", gamesPlayed);
             * vsoh.updateGamesPlayed("player_two", gamesPlayed);
             *
             * vsoh.updateWins("player_one", playerOneScore);
             * vsoh.updateWins("player_two", playerTwoScore);
             */
            return null;
        }

        /**
         * Required ASync Method
         * @param result void
         */
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

    }

    /**
     * AsyncCaller Allows the methods inside to run in
     * their own threads
     * */
    private class AsyncAnimate2 extends AsyncTask<Void, Void, Void>
    {


        /**
         * onPreExecute calls the animateDice method which Animates
         * each of the ImageView objects which have had the images
         * swapped for the result of the Random number generator
         *
         * @see PlayScreen#animateDice for animation method
         */
        @Override
        protected void onPreExecute() {
            animateDice(diceViewOne);
            animateDice(diceViewTwo);
        }

        /**
         * Required ASync Method
         * @param params void
         * @return null
         */
        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        /**
         * onPostExecute is a required ASync method which also
         * updates the onscreen player scores
         * @param result void
         */
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            currentScore.setText(vsoh.getLoggedUser("player_one")+ ": "
                    + String.valueOf(playerOneScore) + " - "
                    + vsoh.getLoggedUser("player_two") + ": "
                    + String.valueOf(playerTwoScore));
        }

    }

    /**
     * This OnResume method shows the player scores on screen
     * when the game is started or if it is resumed due to being
     * paused or stopped
     */
    @Override
    protected void onResume() {
        super.onResume();

        Context context = getApplicationContext();

        Toast currentScoreP1 = Toast.makeText(context, vsoh.getLoggedUserScores("player_one"), duration);
        Toast currentScoreP2 = Toast.makeText(context, vsoh.getLoggedUserScores("player_two"), duration);
        currentScoreP1.show();
        currentScoreP2.show();
    }

    /**
     * This onPause method ensures that the die roll sound stops
     * and ensures that the user game-play data is saved to the database
     */
    @Override
    protected void onPause() {
        super.onPause();
        rollSound.release();

        vsoh.updateGamesPlayed("player_one", gamesPlayed);
        vsoh.updateGamesPlayed("player_two", gamesPlayed);

        vsoh.updateWins("player_one", playerOneScore);
        vsoh.updateWins("player_two", playerTwoScore);
    }

    /**
     * This OnStop method ensures that the die roll sound stop
     */
    @Override
    protected void onStop() {
        super.onStop();
        rollSound.release();
    }

    /**
     * This onCreate method instantiates all of the database instances
     * buttons and TextView And ImageView Objects then sets up an onClick
     * listener to allow the player to click the button to roll the dice
     * and play the game
     *
     * @param savedInstanceState passes the Bundle of current application
     *                           data for screen population
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_screen);

        vsoh = new DBHandler(this);

        rollSound = MediaPlayer.create(this, R.raw.rollsound);
        rollButton = (Button)findViewById(R.id.rollButton);

        diceResultOne = (TextView) findViewById(R.id.diceResultOne);
        diceResultTwo = (TextView) findViewById(R.id.diceResultTwo);
        currentScore = (TextView) findViewById(R.id.currentScore);

        currentPlayer = (TextView) findViewById(R.id.currentPlayer);
        winningPlayer = (TextView) findViewById(R.id.winningPlayer);

        diceViewOne = (ImageView) findViewById(R.id.diceViewOne);
        diceViewTwo = (ImageView) findViewById(R.id.diceViewTwo);
        flashBang = (ImageView) findViewById(R.id.flashBang);

        currentPlayer.setText(vsoh.getLoggedUser("player_one"));




        rollButton.setOnClickListener(new View.OnClickListener() {

            /**
             * onClick makes the button which starts the game-play clickable
             * <p>
             * The onClick method stops the "Roll the Dice" button from being clickable
             * triggers the rolling dice sound and calls the recordWinner
             * method, which in turn calls the rollDice method which
             * generates the random results and passes them back to recordWinner
             * to be saved
             *
             * @param view Passes the relevant view
             */
            @Override
            public void onClick(View view) {

                rollButton.setClickable(false);

                rollSound.start();
                winningPlayer.setText(R.string.winnerWait);
                recordWinner(rollDice());

            }


                /**
                 * rollDice deals with the random number generation
                 * and image swapping to show the correct die face
                 * <p>
                 * Uses an instance of the Random class to generate two
                 * numbers between one and six, uses those numbers to
                 * set the die face images, calls the Asynchronous Animation
                 * makes the "roll the dice" button clickable again for the
                 * next round and returns the two numbers to be used in the
                 * recordWinner method.
                 *
                 * @return int[] Returns an array containing the
                 *                  two randomly generated integers
                 */
                private int[] rollDice(){

                    Random diceRandom = new Random();
                    int diceRollOne = diceRandom.nextInt(6)+1;
                    int diceRollTwo = diceRandom.nextInt(6)+1;


                    switch(diceRollOne) {
                        case 1:
                            diceViewOne.setImageResource(R.drawable.p1);

                            break;
                        case 2:
                            diceViewOne.setImageResource(R.drawable.p2);

                            break;
                        case 3:
                            diceViewOne.setImageResource(R.drawable.p3);

                            break;
                        case 4:
                            diceViewOne.setImageResource(R.drawable.p4);

                            break;
                        case 5:
                            diceViewOne.setImageResource(R.drawable.p5);

                            break;
                        case 6:
                            diceViewOne.setImageResource(R.drawable.p6);

                            break;
                        default:
                            diceViewOne.setImageResource(R.drawable.p1);
                    }

                    switch(diceRollTwo) {
                        case 1:
                            diceViewTwo.setImageResource(R.drawable.p1);

                            break;
                        case 2:
                            diceViewTwo.setImageResource(R.drawable.p2);

                            break;
                        case 3:
                            diceViewTwo.setImageResource(R.drawable.p3);

                            break;
                        case 4:
                            diceViewTwo.setImageResource(R.drawable.p4);

                            break;
                        case 5:
                            diceViewTwo.setImageResource(R.drawable.p5);

                            break;
                        case 6:
                            diceViewTwo.setImageResource(R.drawable.p6);

                            break;
                        default:
                            diceViewTwo.setImageResource(R.drawable.p2);
                    }

                    new AsyncAnimate2().execute();

                    rollButton.setClickable(true);

                    return new int[] {diceRollOne, diceRollTwo};
                }


                /**
                 * recordWinner takes the results of the dice rolls and
                 * determines if the player has scored a point, incrementing
                 * the player score and calling AsyncCaller to display an image
                 * if the current player has scored a point
                 *
                 * @param diceResults the results of the randomised dice rolls
                 */

                private void recordWinner(int[] diceResults){

                    int diceRollOne = diceResults[0];
                    int diceRollTwo = diceResults[1];

                    if(diceRollOne == diceRollTwo){
                        winningPlayer.setText(R.string.wins);
                        gamesPlayed++;
                        if(player == 1){
                            playerOneScore++;
                        }
                        else{
                            playerTwoScore++;
                        }
                        new AsyncCaller().execute();
                    }

                    if (player == 1)
                    {
                        player = 2;
                        currentPlayer.setText(vsoh.getLoggedUser("player_two"));
                    }
                    else
                    {
                        player = 1;
                        currentPlayer.setText(vsoh.getLoggedUser("player_one"));
                    }
                }


        });
    }
}
