package cs301.Soccer;

import android.util.Log;
import cs301.Soccer.soccerPlayer.SoccerPlayer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author Anthony A
 * @version *** put date of completion here ***
 *
 */
public class SoccerDatabase implements SoccerDB {

    private Hashtable<String, SoccerPlayer> database = new Hashtable();
    private HashSet<String> teamlist = new HashSet<String>();

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
        try {
            Scanner input = new Scanner(file);
            int numLine = 0;
            String firstName = "";
            String lastName = "";
            String teamName = "";
            int uniform = 0;
            int goals = 0;
            int yellowCards = 0;
            int redCards = 0;
            while (input.hasNextLine()) {
                Log.i("Line Number", "" + numLine);
                String currentLine = input.nextLine();
                switch (numLine) {
                    case 0:
                        firstName = currentLine;
                    break;
                    case 1:
                        lastName = currentLine;
                    break;
                    case 2:
                        teamName = currentLine;
                    break;
                    case 3:
                        uniform = Integer.parseInt(currentLine);
                    break;
                    case 4:
                        goals = Integer.parseInt(currentLine);
                    break;
                    case 5:
                        yellowCards = Integer.parseInt(currentLine);
                    break;
                    case 6:
                        redCards = Integer.parseInt(currentLine);
                    break;
                }
                numLine++;
                if (numLine >= 7) {
                    addPlayer(firstName, lastName, uniform, teamName);
                    teamlist.add(teamName);
                    for (int k = 0; k < goals; k++) {
                        bumpGoals(firstName, lastName);
                    }
                    for (int p = 0; p < yellowCards; p++) {
                        bumpYellowCards(firstName, lastName);
                    }
                    for (int l = 0; l < redCards; l++) {
                        bumpRedCards(firstName, lastName);
                    }
                    firstName = "";
                    lastName = "";
                    teamName = "";
                    uniform = 0;
                    yellowCards = 0;
                    goals = 0;
                    redCards = 0;
                    numLine = 0;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
        try {
            PrintWriter wrt = new PrintWriter(file);
            Log.i("fileInfo:",file.getAbsolutePath());
            Iterator iterator = database.values().iterator();
            SoccerPlayer currentPlayer;
            while (iterator.hasNext()) {
                currentPlayer = (SoccerPlayer) (iterator.next());
                wrt.println(logString(currentPlayer.getFirstName()));
                wrt.println(logString(currentPlayer.getLastName()));
                wrt.println(logString(currentPlayer.getTeamName()));
                wrt.println(logString(Integer.toString(currentPlayer.getUniform())));
                wrt.println(logString(Integer.toString(currentPlayer.getGoals())));
                wrt.println(logString(Integer.toString(currentPlayer.getYellowCards())));
                wrt.println(logString(Integer.toString(currentPlayer.getRedCards())));
            }
            wrt.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
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
     * @see cs301.Soccer.SoccerDB#getTeams()
     */
    // return list of teams
    @Override
    public HashSet<String> getTeams() {
        return teamlist;
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
