package uk.ac.ljmu.cwk2;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class RollItOnTopPlay extends AppCompatActivity {


    //As an homage to Shenmue one of the greatest games ever we have added a simplified version opf Roll it on top! Highest dice wins
    private Button rollButton;
    private ImageView diceViewOne, diceViewTwo;

    // The following are used for the shake detection
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    private void animateDice(ImageView diceView) {
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(diceView, "rotationY", 0, 360));

        set.setStartDelay(0);
        set.setDuration(2000);
        set.start();
    }


    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    /**
     * @param savedInstanceState passes the Bundle of current application
     *                           data for screen population
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                rollButton.callOnClick();
            }
        });


        // set the button and text field for roll and results - cast to appropriate item
        final MediaPlayer rollSound = MediaPlayer.create(this, R.raw.rollsound);
        rollButton = (Button) findViewById(R.id.rollButton);

        diceViewOne = (ImageView) findViewById(R.id.diceViewOne);
        diceViewTwo = (ImageView) findViewById(R.id.diceViewTwo);

        Toast toast = Toast.makeText(getApplicationContext(), R.string.rot, Toast.LENGTH_LONG);
        toast.show();


        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rollSound.start();
                Random diceRandom = new Random();
                int diceRollOne = diceRandom.nextInt(6) + 1;
                int diceRollTwo = diceRandom.nextInt(6) + 1;


                switch (diceRollOne) {
                    case 1:
                        diceViewOne.setImageResource(R.drawable.cd1);

                        break;
                    case 2:
                        diceViewOne.setImageResource(R.drawable.cd2);

                        break;
                    case 3:
                        diceViewOne.setImageResource(R.drawable.cd3);

                        break;
                    case 4:
                        diceViewOne.setImageResource(R.drawable.cd4);

                        break;
                    case 5:
                        diceViewOne.setImageResource(R.drawable.cd5);

                        break;
                    case 6:
                        diceViewOne.setImageResource(R.drawable.cd6);

                        break;
                    default:
                        diceViewOne.setImageResource(R.drawable.cd1);
                }
                switch (diceRollTwo) {
                    case 1:
                        diceViewTwo.setImageResource(R.drawable.cdo1);

                        break;
                    case 2:
                        diceViewTwo.setImageResource(R.drawable.cdo2);

                        break;
                    case 3:
                        diceViewTwo.setImageResource(R.drawable.cdo3);

                        break;
                    case 4:
                        diceViewTwo.setImageResource(R.drawable.cdo4);

                        break;
                    case 5:
                        diceViewTwo.setImageResource(R.drawable.cdo5);

                        break;
                    case 6:
                        diceViewTwo.setImageResource(R.drawable.cdo6);

                        break;
                    default:
                        diceViewTwo.setImageResource(R.drawable.cdo1);
                }
                animateDice(diceViewOne);
                animateDice(diceViewTwo);
                if (diceRollOne > diceRollTwo) {

                    Toast toast2 = Toast.makeText(getApplicationContext(), "P1 胜利", Toast.LENGTH_SHORT);
                    toast2.show();
                } else if (diceRollOne == diceRollTwo) {
                    Toast toast3 = Toast.makeText(getApplicationContext(), "平", Toast.LENGTH_SHORT);
                    toast3.show();
                } else {
                    Toast toast3 = Toast.makeText(getApplicationContext(), "P2 胜利", Toast.LENGTH_SHORT);
                    toast3.show();
                }
                ;


            }

            ;
        });
    }
}
