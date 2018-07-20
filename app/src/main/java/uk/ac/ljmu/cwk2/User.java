package uk.ac.ljmu.cwk2;

/**
 * The user class contains all of the variables which
 * make up a user object as well as relevant getter
 * methods to allow only the relevant data to be exposed
 */
public class User {

    private String username = "Login", password = null;
    private double wins = 0, gamesPlayed = 0;
    private int playerOne = 0, playerTwo = 0;


    /**
     * Instantiating the user class takes all of the information
     * that might be stored about the user in the database and
     * assigns it to a variable inside the user object, ready
     * to be written to the database for permanent storage or
     * amended by game play
     *
     * @param username    the user's username
     * @param password    the user's password
     * @param wins        the number of times the user has won
     * @param gamesPlayed the number of games the user has played
     * @param playerOne   if the player is logged in as player one
     * @param playerTwo   if the user is logged in as player two
     */
    public User(String username, String password, double wins, double gamesPlayed, int playerOne, int playerTwo) {
        this.username = username;
        this.password = password;
        this.wins = wins;
        this.gamesPlayed = gamesPlayed;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    /**
     * Returns the username
     *
     * @return the username as a string
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the password
     *
     * @return the password as a string
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the number of wins
     *
     * @return the number of wins as a double
     */
    public double getWins() {
        return wins;
    }

    /**
     * Returns the number of games played
     *
     * @return the number of games played as a double
     */
    public double getGamesPlayed() {
        return gamesPlayed;
    }

    /**
     * Returns a value indicating whether or not the
     * player is logged in as player one
     *
     * @return if the player is player one as an int
     */
    public int getPlayerOne() {
        return playerOne;
    }

    /**
     * Returns a value indicating whether or not the
     * player is logged in as player two
     *
     * @return if the player is player one as an int
     */
    public int getPlayerTwo() {
        return playerTwo;
    }
}