package uk.ac.ljmu.cwk2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * UserScreen manages the user interface side of user creation
 */
public class UserScreen extends AppCompatActivity {
    private DBHandler vsoh;

    /**
     * This onCreate method dynamically creates The page title
     * and adds the new TextView to the layout for the page
     *
     * @param savedInstanceState passes the Bundle of current application
     *                           data for screen population
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_screen);
        vsoh = new DBHandler(this);

        //Intent intent = getIntent();
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(R.string.new_user);

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_user_screen);
        layout.addView(textView);
    }

    /**
     * Provides the interface components  for the user to
     * enter their details as well as calling the database function
     * to add the user to the database.
     *
     * @param view the relevant view
     */
    public void createUser(View view) {
        EditText username = (EditText) findViewById(R.id.editTextCreateUsername);
        EditText password = (EditText) findViewById(R.id.editTextCreatePassword);

        String usernameString = username.getText().toString();
        String passwordString = password.getText().toString();

        vsoh.insertUser(new User(usernameString, passwordString + "saltHash", 0, 0, 0, 0));

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, R.string.user_created, duration);
        toast.show();

        Intent home = new Intent(UserScreen.this, DiceGame.class);
        startActivity(home);
    }


}
