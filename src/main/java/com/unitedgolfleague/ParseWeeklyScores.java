package com.unitedgolfleague;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.ugl.common.Cout;
import com.ugl.common.Utils;
import com.ugl.handicap.HMGReport;
import com.ugl.handicap.Player;
import com.ugl.handicap.Schedule;
import com.ugl.handicap.ScoreCards;

/**
 * @author e1042631
 *
 */
public class ParseWeeklyScores {
    private final static String CLASS_NAME = "ParseWeeklyScores.";

    private final static String MAIN = CLASS_NAME + "main() ";

    private final static String ARG1 = MAIN + "ARG1: WEEKLY SCORECARD.CSV FILE - ";

    private final static String ARG2 = MAIN + "ARG2: SCORECARD.CSV FILE TO UPDATE - ";

    private final static String ARG3 = MAIN + "ARG3: SCHEDULE.CSV FILE TO UPDATE - ";

    private final static String ARG4 = MAIN + "ARG4: PLAYER.CSV FILE TO UPDATE - ";

    private final static String ARG5 = MAIN + "ARG5: NUMBER OF WEEKS IN LEAGUE - ";

    private final static String COMMENT = "****";

    private final static String WEEK = COMMENT + "week";

    private final static String END_MATCH = "!";

    /**
     * Weekly scorecard update file.
     */
    private static String weeklyScoreCardFile;

    private static String scoreCardFile;

    private static String scheduleFile;

    private static int leagueWeek = 0;

    private static int startingHole = 1;

    private static String playersFile;

    private static int numWeeks = 12;

    private static ArrayList<Player> listOfPlayers;

    private static ArrayList<ScoreCards> scoresList;

    private static ArrayList<Schedule> matchSchedule;

    /**
     * @param args
     */
    public static void main(String[] args) {

	if (args.length < 4) {
	    Cout.outString(ARG1);
	    Cout.outString(ARG2);
	    Cout.outString(ARG3);
	    Cout.outString(ARG4);
	    Cout.outString(ARG5);
	    Cout.outString("ERROR NOT ENOUGH ARGS... BYE...");
	} else {
	    weeklyScoreCardFile = args[0];
	    scoreCardFile = args[1];
	    scheduleFile = args[2];
	    playersFile = args[3];
	    numWeeks = Integer.parseInt(args[4]);
	    Cout.outString(ARG1 + weeklyScoreCardFile);
	    Cout.outString(ARG2 + scoreCardFile);
	    Cout.outString(ARG3 + scheduleFile);
	    Cout.outString(ARG4 + playersFile);
	    Cout.outString(ARG5 + args[4]);

	    scoresList = new ArrayList<ScoreCards>();
	    ScoreCards.parseScoreCard(scoreCardFile, scoresList);
	    matchSchedule = new ArrayList<Schedule>();
	    Schedule.parseSchedule(scheduleFile, matchSchedule);
	    listOfPlayers = new ArrayList<Player>();
	    Player.parsePlayers(playersFile, listOfPlayers);

	    // parse the weekly scores
	    processWeeklyScore(weeklyScoreCardFile);
	    // update score cards
	    ScoreCards.updateScoreCard(scoreCardFile, scoresList);
	    // update schedule
	    Schedule.updateSchedule(scheduleFile, matchSchedule);
	}
	Cout.outString("bye!");
	System.exit(0);
    }

    /**
     * Process weekly scores...
     * 
     * @param theScoreCardFile
     */
    private static void processWeeklyScore(String theScoreCardFile) {
	List<ScoreCards> schedulerScoreCard = new ArrayList<ScoreCards>();
	try {
	    List<String> fileLines = FileUtils.readLines(new File(theScoreCardFile), "UTF-8");
	    int state = 0;
	    for (String curLine : fileLines) {
		boolean isComment = false;
		curLine = curLine.trim();

		// Week
		if (state == 1) {
		    String[] fields = Utils.parseRecString(curLine);
		    leagueWeek = Integer.parseInt(fields[0].trim());
		    Cout.outString("League Week " + Integer.toString(leagueWeek));
		    startingHole = Integer.parseInt(fields[1].trim());
		    Cout.outString("Starting hole " + Integer.toString(startingHole));
		    state = 0;
		    continue;
		}

		String[] fields = Utils.parseRecString(curLine);
		fields[0] = fields[0].trim();
		int len = fields[0].length();
		if (len == 1) {
		    if (fields[0].contentEquals(END_MATCH)) {
			// end the match update the schedule
			int numPlayers = schedulerScoreCard.size();
			Schedule schd = new Schedule();
			ScoreCards p1, p2;
			switch (numPlayers) {
			case 1:
			    schd.Week = leagueWeek;
			    p1 = schedulerScoreCard.get(0);
			    schd.ID1 = p1.ID;
			    schd.ID2 = schd.ID1;
			    matchSchedule.add(schd);
			    Cout.outString("added schedule: " + schd.toString());
			    break;
			case 2:
			    schd.Week = leagueWeek;
			    p1 = schedulerScoreCard.get(0);
			    p2 = schedulerScoreCard.get(1);
			    schd.ID1 = p1.ID;
			    schd.ID2 = p2.ID;
			    matchSchedule.add(schd);
			    Cout.outString("added schedule: " + schd.toString());
			    break;
			default:
			    Cout.outString(
				    "ERROR: Invalid number of players in match : " + Integer.toString(numPlayers));
			    break;
			}
			schedulerScoreCard.clear();
			continue;
		    }
		} else if (len >= 4) {
		    String comment = fields[0];
		    // Look at each individual byte
		    char[] byteBuf = comment.toCharArray();
		    if ((byteBuf[0] == '*' && byteBuf[1] == '*' && byteBuf[2] == '*' && byteBuf[3] == '*')
			    || (byteBuf[4] == '*' && byteBuf[1] == '*' && byteBuf[2] == '*' && byteBuf[3] == '*')) {
			isComment = true;
		    }
		    // comment = comment.substring(0, 4);
		    // if (comment.contentEquals(COMMENT)) {
		    // isComment = true;
		    // }
		}

		if (isComment) {
		    // week
		    if (fields[0].equalsIgnoreCase(WEEK)) {
			state = 1;
		    } else {
			state = 0;
		    }
		} else {
		    // parse the player score
		    ScoreCards newScoreCard = new ScoreCards();
		    newScoreCard.Name = fields[0];
		    int index = startingHole - 1;
		    for (int i = 1; i < fields.length; i++) {
			newScoreCard.Scores[index++] = Integer.parseInt(fields[i].trim());
		    }

		    Player p = HMGReport.findPlayer(newScoreCard.Name, listOfPlayers).get();
		    if (p != null) {
			newScoreCard.Week = leagueWeek;
			newScoreCard.ID = p.getID();
			newScoreCard.updateTotal();
			Cout.outString(newScoreCard.toString());
			schedulerScoreCard.add(newScoreCard);
			scoresList.add(newScoreCard);
			Cout.outString("Add new scorecard " + newScoreCard.toString());
		    } else {
			Cout.outString("ERROR: CANNOT FIND PLAYER " + newScoreCard.Name);
		    }
		}
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

}
