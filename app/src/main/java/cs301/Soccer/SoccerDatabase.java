package cs301.Soccer;

import android.util.Log;
import cs301.Soccer.soccerPlayer.SoccerPlayer;
import java.io.File;
import java.util.*;

/**
 * @author Anthony A
 * @version *** put date of completion here ***
 *
 */
public class SoccerDatabase implements SoccerDB {

    private Hashtable<String, SoccerPlayer> database = new Hashtable();

    /**
     * add a player
     *
     * @see SoccerDB#addPlayer(String, String, int, String)
     */
    @Override
    public boolean addPlayer(String firstName, String lastName,
                             int uniformNumber, String teamName) {
        String playerKey = firstName + "##" + lastName;
        if (database.get(playerKey) != null) {
            return false;
        } else {
            SoccerPlayer newPlayer = new SoccerPlayer(firstName, lastName, uniformNumber, teamName);
            database.put(playerKey, newPlayer);
         return true;
        }
    }

    /**remove a player
     * @see SoccerDB#removePlayer(String, String)*/
    @Override
    public boolean removePlayer(String firstName, String lastName) {
        String playerKey = firstName + "##" + lastName;
        if (database.get(playerKey) == null) {
            return false;
        } else {
            database.remove(playerKey);
            return true;
        }
    }

    /**look up a player
     * @see SoccerDB#getPlayer(String, String)*/
    @Override
    public SoccerPlayer getPlayer(String firstName, String lastName) {
        String playerKey = firstName + "##" + lastName;
        return database.get(playerKey);
    }

    /**
     * increment a player's goals
     *
     * @see SoccerDB#bumpGoals(String, String)
     */
    @Override
    public boolean bumpGoals(String firstName, String lastName) {
        String playerKey = firstName + "##" + lastName;
        if (database.get(playerKey) != null) {
            database.get(playerKey).bumpGoals();
            return true;
        } else {
            return false;
        }
    }

    /**
     * increment a player's yellow cards
     *
     * @see SoccerDB#bumpYellowCards(String, String)
     */
    @Override
    public boolean bumpYellowCards(String firstName, String lastName) {
        String playerKey = firstName + "##" + lastName;
        if (database.get(playerKey) != null) {
            database.get(playerKey).bumpYellowCards();
            return true;
        } else {
            return false;
        }
    }

    /**
     * increment a player's red cards
     *
     * @see SoccerDB#bumpRedCards(String, String)
     */
    @Override
    public boolean bumpRedCards(String firstName, String lastName) {
        String playerKey = firstName + "##" + lastName;
        if (database.get(playerKey) != null) {
            database.get(playerKey).bumpRedCards();
            return true;
        } else {
            return false;
        }
    }

    /**
     * tells the number of players on a given team
     *
     * @see SoccerDB#numPlayers(String)
     */
    @Override
    // report number of players on a given team (or all players, if null)
    public int numPlayers(String teamName) {
        if (teamName == null) {
            return database.size();
        } else {
            Collection<Map.Entry<String, SoccerPlayer>> iteratorSet = database.entrySet();
            int count = 0;
            for (Map.Entry<String, SoccerPlayer> entry : iteratorSet) {
                String entryTeamName = entry.getValue().getTeamName();
                if (entryTeamName.equals(teamName)) {
                    count++;
                }
            }
            return count;
        }
    }

    /**
     * gives the nth player on a the given team
     *
     * @see SoccerDB#playerIndex(int, String)
     */
    // get the nTH player
    @Override
    public SoccerPlayer playerIndex(int index, String teamName) {
        ArrayList<SoccerPlayer> players = new ArrayList<SoccerPlayer>();
        Collection<Map.Entry<String, SoccerPlayer>> iteratorSet = database.entrySet();
        if (teamName == null && index < database.size()) {
            for (Map.Entry<String, SoccerPlayer> entry : iteratorSet) {
                players.add(entry.getValue());
            }
            return players.get(index);
        } else if (teamName != null) {
            for (Map.Entry<String, SoccerPlayer> entry : iteratorSet) {
                if (entry.getValue().getTeamName().equals(teamName)) {
                    players.add(entry.getValue());
                }
            }
            if (index < players.size()) {
                return players.get(index);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * reads database data from a file
     *
     * @see SoccerDB#readData(java.io.File)
     */
    // read data from file
    @Override
    public boolean readData(File file) {
        return file.exists();
    }

    /**
     * write database data to a file
     *
     * @see SoccerDB#writeData(java.io.File)
     */
    // write data to file
    @Override
    public boolean writeData(File file) {
        return false;
    }

    /**
     * helper method that logcat-logs a string, and then returns the string.
     * @param s the string to log
     * @return the string s, unchanged
     */
    private String logString(String s) {
        Log.i("write string", s);
        return s;
    }

    /**
     * returns the list of team names in the database
     *
     * @see cs301.Soccer.SoccerDB#getTeams()
     */
    // return list of teams
    @Override
    public HashSet<String> getTeams() {
        return new HashSet<String>();
    }

    /**
     * Helper method to empty the database and the list of teams in the spinner;
     * this is faster than restarting the app
     */
    public boolean clear() {
        if(database != null) {
            database.clear();
            return true;
        }
        return false;
    }
}
