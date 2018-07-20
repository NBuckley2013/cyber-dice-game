package uk.ac.ljmu.cwk2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * LoginScreen Allows a user to log in either as player one or player two
 */
public class LoginScreen extends AppCompatActivity {
    private DBHandler vsoh;
    private User user;

    /**
     * This onCreate sets out the layout and dynamically rendered
     * visual objects to allow a user to choose whether they want
     * to log in as layer 1 or player 2
     *
     * @param savedInstanceState passes the Bundle of current application
     *                           data for screen population
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        vsoh = new DBHandler(this);

        Intent intent = getIntent();
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(R.string.login);

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_login_screen);
        layout.addView(textView);

        Spinner loginAs = (Spinner) findViewById(R.id.loginAs);
        String[] values = new String[] {"Player 1", "Player 2"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, values);
        loginAs.setAdapter(adapter);
    }

    /**
     * loginUser manages the interface side of the login process
     * <p>
     * loginUser creates some editable text boxes for the users to
     * enter their username and password as well as enacts the spinner
     * allowing for the choice to log in as player 1 or 2. It then
     * uses the various logon methods from DBHandler to check the details
     * against those in the database, eventually logging the user in if the
     * details are correct and popping up a toast to let the user
     * know if their login was successful
     *
     * @param view the relevant view
     */
    public void loginUser(View view) {
        EditText username = (EditText) findViewById(R.id.editTextLoginUsername);
        EditText password = (EditText) findViewById(R.id.editTextLoginPassword);
        Spinner player = (Spinner) findViewById(R.id.loginAs);

        String usernameString = username.getText().toString();
        String passwordString = password.getText().toString();
        String playerString = player.getSelectedItem().toString();

        String userDetails = vsoh.checkLogin(usernameString, passwordString);

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;

        if (!userDetails.equals("-1")) {
            vsoh.userLogin(userDetails, playerString);

            Toast toast = Toast.makeText(context, R.string.logged_in, duration);
            toast.show();
            Intent home = new Intent(LoginScreen.this, DiceGame.class);
            startActivity(home);
        } else {
            Toast toast = Toast.makeText(context, "Incorrect Login", duration);
            toast.show();
        }
    }
}
