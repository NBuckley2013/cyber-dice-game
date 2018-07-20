package uk.ac.ljmu.cwk2;

import java.util.List;
import java.util.LinkedList;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;

/**
 * The Class DBHandler Manages al SQLite Database operations
 * <p>
 * The DBHandler Class manages all database functions including
 * the creation of the SQLite database, the creation of users
 * user login and logout functionality and the saving and retrieval
 * of user high score data
 */
public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "UserDatabase";

    /**
     * At instantiation, passes global context to object
     * <p>
     * When the DBHandler is instantiated the Global context is
     * passed to it to allow access to the global context from
     * inside the instantiated object
     *
     * @param context Global context
     */
    public DBHandler(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * The onCreate method populates the SQLite Database
     * <p>
     * The onCreate method populates the database with it's
     * only table, 'user' which contains all of the users
     * personal information, password and records of their
     * wins and how often they have played the game
     *
     * @param db The SQLite Database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE user (user_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username VARCHAR(20), password CHAR(64), wins FLOAT, games_played FLOAT, " +
                "player_one BIT, player_two BIT)");
        System.out.println("db created");
    }

    /**
     * OnUpgrade Clears the database on upgrade
     * <p>
     * TODO: why is this the desired functionality
     *
     * @param db         the SQLite database
     * @param oldVersion old version number
     * @param newVersion new verion number
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        this.onCreate(db);
    }

    /**
     * getHighScores reads the high score information from the database
     * and returns it as a list of User objects
     *
     * @return List of User objects containing properties relating to high scores
     */
    public List<User> getHighScores() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<User> users = new LinkedList<>();
        Cursor c = db.rawQuery("SELECT username, wins, games_played, (wins / games_played) AS score FROM user " +
                "WHERE wins > 0 ORDER BY score DESC LIMIT 5", null);
        while (c.moveToNext()) {
            users.add(new User(c.getString(0), null, c.getInt(1), c.getInt(2), 0, 0));
        }
        return users;
    }

    /**
     * getLoggedUserScores takes a players login name
     * and returns the number of games they have won
     *
     * @param player String representing the player's name
     * @return A concatenation of the username and their number of wins
     */
    public String getLoggedUserScores(String player) {
        String wins = "";
        String username;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT wins FROM user WHERE " + player + " = 1", null);
        if (c.moveToFirst()) {
            wins = c.getString(c.getColumnIndex("wins"));
        }

        username = getLoggedUser(player);
        return username + " - " + wins;
    }

    /**
     * checkLogin checks the provided username and password against the database
     * and returns the user_id is there is a match for both the provided
     * username and password
     *
     * @param usernameString the username
     * @param passwordString the password
     * @return the user_id sed as a guid by the system
     */
    public String checkLogin(String usernameString, String passwordString) {
        SQLiteDatabase db = this.getReadableDatabase();
        String user_id = "-1";
        Cursor c = db.rawQuery("SELECT user_id FROM user WHERE username = '" + usernameString +
                "' AND password = '" + passwordString + "saltHash" + "'", null);
        if (c.getCount() < 1) {
            return "-1";
        }
        if (c.moveToFirst()) {
            user_id = c.getString(c.getColumnIndex("user_id"));
        }
        return user_id;
    }

    /**
     * userLogin allows the player to login as either player 1 or player 2
     *
     * @param user_id the User ID string returned by checkLogin
     * @param player  the user's choice of whether to log in as player 1 or 2
     */
    public void userLogin(String user_id, String player) {
        SQLiteDatabase db = this.getWritableDatabase();
        int userID = Integer.parseInt(user_id);

        if (player.equals("Player 1")) {
            db.execSQL("UPDATE user SET player_two = 0 WHERE user_id = " + userID);
            db.execSQL("UPDATE user SET player_one = 0");
            db.execSQL("UPDATE user SET player_one = 1 WHERE user_id = " + userID);
        }
        if (player.equals("Player 2")) {
            db.execSQL("UPDATE user SET player_two = 0");
            db.execSQL("UPDATE user SET player_one = 0 WHERE user_id = " + userID);
            db.execSQL("UPDATE user SET player_two = 1 WHERE user_id = " + userID);
        }
        db.close();
    }

    /**
     * Returns the username when passed details of whether player
     * 1 or 2 is being queried
     *
     * @param player A string passed to let the method know if it
     *               should return the username for the logged in
     *               player 1 or 2
     * @return a string containing the logged in user's username
     */
    public String getLoggedUser(String player) {
        SQLiteDatabase db = this.getReadableDatabase();
        String username = "";
        Cursor c = db.rawQuery("SELECT username FROM user WHERE " + player + " = 1", null);
        if (c.moveToFirst()) {
            username = c.getString(c.getColumnIndex("username"));
        }
        return username;
    }

    /**
     * When provided with a user object insertUser will write the
     * user information to the database
     *
     * @param user the user object to be inserted into the database
     */
    public void insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", user.getUsername());

        /**
         * This is a place holder, password functionality would be implemented
         * as a slated sha256 hash with salt in a proper application
         */
        cv.put("password", user.getPassword());
        cv.put("wins", user.getWins());
        cv.put("games_played", user.getGamesPlayed());
        cv.put("player_one", user.getPlayerOne());
        cv.put("player_two", user.getPlayerTwo());
        db.insert("user", null, cv);
        db.close();
    }

    /**
     * updateGamesPlayed updates the database with information about
     * how many games a player has played
     *
     * @param player      A string passed to let the method know if it
     *                    should update the information for the logged in
     *                    player 1 or 2
     * @param gamesPlayed the number of games played
     */
    public void updateGamesPlayed(String player, int gamesPlayed) {
        SQLiteDatabase db = this.getWritableDatabase();
        String gamesPlayedString;

        Cursor c = db.rawQuery("SELECT games_played FROM user WHERE " + player + " = 1", null);
        if (c.moveToFirst()) {
            gamesPlayedString = c.getString(c.getColumnIndex("games_played"));
            gamesPlayed = Integer.parseInt(gamesPlayedString) + gamesPlayed;
        }
        db.execSQL("UPDATE user SET games_played = " + gamesPlayed + " WHERE " + player + " = 1");
        db.close();
    }

    /**
     * updateWins updates the database with information about
     * how many games a player has won
     *
     * @param player A string passed to let the method know if it
     *               should update the information for the logged in
     *               player 1 or 2
     * @param wins   the number of games played
     */
    public void updateWins(String player, int wins) {
        SQLiteDatabase db = this.getWritableDatabase();
        String winsString;

        Cursor c = db.rawQuery("SELECT wins FROM user WHERE " + player + " = 1", null);
        if (c.moveToFirst()) {
            winsString = c.getString(c.getColumnIndex("wins"));
            wins = Integer.parseInt(winsString) + wins;
        }
        db.execSQL("UPDATE user SET wins = " + wins + " WHERE " + player + " = 1");
        db.close();
    }
}