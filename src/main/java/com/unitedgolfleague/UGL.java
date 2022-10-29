/**
 * 
 */
package com.unitedgolfleague;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.google.common.collect.Multimap;
import com.ugl.common.Cout;
import com.ugl.common.Utils;
import com.ugl.handicap.Course;
import com.ugl.handicap.HMGReport;
import com.ugl.handicap.Player;
import com.ugl.handicap.Schedule;
import com.ugl.handicap.ScoreCards;
import com.ugl.speadsheet.PlayersSpreedsheet;

/**
 * @author e1042631
 *
 */
public class UGL {
	private final static String CLASS_NAME = "UGL.";

	private static final String STARTUP_MESSAGE = "UGL.main() - United Golf League v2.0.1 Created from Initial Version \r\n"
			+ "Author: Chay Sayaovong\r\n" + "Date Modified: 20220423\r\n"
			+ "The program input parameters are specified with the prefix of the '-' character.\r\n"
			+ "And you can have more than 1 input prefix like '--input' or '-input' are the same.\r\n" + "\r\n"
			+ "Input Parameters:\r\n" + "  1. -props <specify this to override the default properties file>\r\n"
			+ "  2. -week <the current week in the league>\r\n" + "  3. -course <the course name>\r\n"
			+ "  4. -tee <the course tee like white, blue, red>\r\n"
			+ "  5. -player <the players spreadsheet file name and dir>\r\n"
			+ "  6. -playerno <used in conjunction to mode 3 to update a player's HCP>\r\n"
			+ "  7. -mode <mode number 1-4>\r\n" + "		MODES:\r\n" + "		1 - Calculate all HCP.\r\n"
			+ "		2 - Calculate Match Play Results.\r\n" + "		3 - Update a player HCP.\r\n"
			+ "		4 - Prints match play schedule.";

	private static String propertiesFileName = null;

	private static int TheWeek = -1;

	private static String courseName = "";

	private static String courseTee = "";

	private static String matchPlayReportHtmlFile = "";

	private static String playersReportHtmlFile = "";

	private static List<Course> courseList = new ArrayList<Course>();

	private static List<Player> playerList = null;

	private static List<Schedule> matchSchedule = null;

	private static Course uglCourse = null;

	private static String PlayersSpreadsheet = "";

	private static int Mode = -1;

	private static List<ScoreCards> ScoresList;

	private static String yyyymmdd;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// dump startup message
		Cout.outString(STARTUP_MESSAGE);

		// Set the time...
		Timestamp todaytm = new Timestamp(new Date().getTime());
		yyyymmdd = new SimpleDateFormat("MM-dd-yyyy").format(todaytm);

		Multimap<String, String> mapArgs = Utils.ScanArguments(args);
		String playerID = SetArguments(mapArgs, "playerno", "?");
		propertiesFileName = SetArguments(mapArgs, "props", "players.properties");
		String tmpVal = null;
		tmpVal = SetArguments(mapArgs, "week", "");
		if (tmpVal.isEmpty() == false) {
			TheWeek = Integer.parseInt(tmpVal);
			HMGReport.CurrentWeek = TheWeek;
		}
		courseName = SetArguments(mapArgs, "course", "");
		courseTee = SetArguments(mapArgs, "tee", "");
		PlayersSpreadsheet = SetArguments(mapArgs, "player", "");
		PlayersSpreedsheet.Init(PlayersSpreadsheet);

		// mode
		tmpVal = SetArguments(mapArgs, "mode", "");
		if (tmpVal.isEmpty() == false) {
			Mode = Integer.parseInt(tmpVal);
		}

		HMGProperties.load(propertiesFileName);

		// Set number of HPI samples
		int sample = HMGProperties.HPISample;
		HMGReport.setHPIScores(sample);

		// Get 9 holes?
		int nineHole = HMGProperties.numberHoles;
		boolean bNine = false;
		if (nineHole == 9) {
			HMGReport.setNineHoles(true);
			bNine = true;
		} else {
			HMGReport.setNineHoles(false);
		}

		// set total weeks in league
		HMGReport.setTotalWeeks(HMGProperties.numberOfWeeks);

		// load the golf course
		Course.parseCourse(HMGProperties.courseCsvFile, courseList);
		uglCourse = findGolfCourse(courseName, courseTee);

		// String test_directory = hmgProperties.defaultDir;
		String report_dir = HMGProperties.defaultReports;

		playerList = PlayersSpreedsheet.ListPlayers;

		// Update the player's HCP
		if (Mode == 1) {
			UpdatePlayersScores();
			for (Player p : playerList) {
				p.calculateScores(bNine, uglCourse);
			}
			genPlayersCSV();
			updateSpreedsheet();
		} else if (Mode == 2) {
			HMGReport.UpdatePlayerHPIMode = false;
			/**
			 * CUSTOM Set preference
			 */
			String strWeek = Integer.toString(TheWeek) + "-";
			matchPlayReportHtmlFile = FilenameUtils.concat(report_dir,
					"MatchPlayResult-Week-" + strWeek + yyyymmdd + ".html");
			playersReportHtmlFile = FilenameUtils.concat(report_dir, "Players-Week-" + strWeek + yyyymmdd + ".html");
			playerList = PlayersSpreedsheet.ListPlayers;
			ScoresList = PlayersSpreedsheet.ListScoreCards;
			matchSchedule = PlayersSpreedsheet.ListSchedules;
			if (uglCourse == null) {
				Cout.outString("Error invalid course!");
				System.exit(1);
			} else {
				generateReport();
				updateSpreedsheet();
			}
		}
		// Update a player's HPI...
		else if (Mode == 3) {
			int id = Integer.parseInt(playerID);
			Player p = HMGReport.findPlayer(id, playerList);
			p.calculateScores(bNine, uglCourse);
			genPlayersCSV();
			updateSpreedsheet();
		}
		// Print players schedule...
		else if (Mode == 4) {
			printPlayersShedule();
		}
		// mode 5 - touch...
		else if (Mode == 5) {
			genPlayersCSV();
			updateSpreedsheet();
		} else {
			Cout.outString("ERROR - Invalid Mode!");
		}

		Cout.outString("Completed... bye!");
	}

	private static void UpdatePlayersScores() {
		// find the scores and update it...
		int nOffsetWeek = TheWeek;
		for (ScoreCards sch : PlayersSpreedsheet.ListScoreCards) {
			// debug
			Cout.outString(sch.toString());
			if (sch.Week != nOffsetWeek) {
				continue;
			}
			Player p = HMGReport.findPlayer(sch.ID, playerList);
			if (p == null) {
				continue;
			}
			// DEBUG
			// if (p.getID() == 22) {
			// cout.outString("break");
			// }
			Integer[] tbl = p.getWeeklyScores();
			int nScore = 0;
			int counter = 0;
			for (Integer myscores : sch.Scores) {
				if (myscores == null) {
					continue;
				}
				int val = myscores.intValue();
				if (val > 0) {
					counter++;
				}
				nScore += val;
			}
			// Good score?
			if (counter == HMGProperties.numberHoles) {
				tbl[TheWeek - 1] = new Integer(nScore);
			}
			// debug
			Cout.outString(p.toString());
		}
	}

	/**
	 * Update the spreadsheet...
	 */
	private static void updateSpreedsheet() {
		String backUp = PlayersSpreadsheet + "." + yyyymmdd + "-backup.xlsx";
		try {
			FileUtils.copyFile(new File(PlayersSpreadsheet), new File(backUp));
		} catch (IOException e) {
			e.printStackTrace();
		}
		PlayersSpreedsheet.uglCourse = uglCourse;
		PlayersSpreedsheet.Update(PlayersSpreadsheet);
	}

	/**
	 * Print the players schedule...
	 */
	private static void printPlayersShedule() {
		matchSchedule = PlayersSpreedsheet.ListSchedules;
		playerList = PlayersSpreedsheet.ListPlayers;
		String schedName = FilenameUtils.concat(HMGProperties.defaultReports, "Schedule-" + yyyymmdd + ".csv");
		List<String> out = new ArrayList<String>();
		for (Schedule l : matchSchedule) {

			String comment = l.toString(playerList);
			comment = l.toString() + ", " + comment;
			out.add(comment);
		}
		try {
			FileUtils.writeLines(new File(schedName), out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Generate report...
	 */
	private static void generateReport() {
		genMatchReportHTML();

		// Calculate HPI
		for (Player p : playerList) {
			p.calculateScores(true, uglCourse);
		}

		genPlayersCSV();
		genUnitedCupCSV();
	}

	/**
	 * Generate match report HTML...
	 */
	private static void genMatchReportHTML() {
		List<String> var = HMGReport.generateMatchReport(uglCourse, matchSchedule, ScoresList, playerList, TheWeek);

		try {
			FileUtils.writeLines(new File(matchPlayReportHtmlFile), var);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void genPlayersCSV() {
		List<String> var = HMGReport.generatePlayerCSV(uglCourse, playerList, true);
		String playersCSVFile = "PlayersUpdate-" + yyyymmdd + ".csv";
		playersCSVFile = FilenameUtils.concat(HMGProperties.defaultReports, playersCSVFile);
		try {
			FileUtils.writeLines(new File(playersCSVFile), var);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (playersReportHtmlFile.isEmpty()) {
			return;
		}
		var = HMGReport.generatePlayerHTML(uglCourse, playerList, true);
		try {
			FileUtils.writeLines(new File(playersReportHtmlFile), var);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void genUnitedCupCSV() {
		String unitedCupFile = "UnitedGolfLeagueStanding-" + yyyymmdd + ".csv";
		unitedCupFile = FilenameUtils.concat(HMGProperties.defaultReports, unitedCupFile);
		Collections.sort(playerList);
		List<String> out = new ArrayList<String>();
		out.add("****Player,Points,HPI");
		for (Player p : playerList) {
			out.add(p.toString());
		}
		try {
			FileUtils.writeLines(new File(unitedCupFile), out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Find the golf course...
	 * 
	 * @param name
	 * @param tee
	 * @return
	 */
	private static Course findGolfCourse(String name, String tee) {
		name = name.trim().toUpperCase();
		tee = tee.trim().toUpperCase();
		for (Course course : courseList) {
			String cName = course.getName().trim().toUpperCase();
			String cTee = course.getTee().trim().toUpperCase();
			if (cName.contentEquals(name) && cTee.contentEquals(tee)) {
				return course;
			}
		}
		return null;
	}

	/**
	 * Set the args...
	 * 
	 * @param mapArgs
	 * @param Property
	 * @param DefaultValue
	 * @return
	 */
	private static String SetArguments(Multimap<String, String> mapArgs, String Property, String DefaultValue) {
		String tmp = DefaultValue;
		try {
			tmp = mapArgs.get(Property).iterator().next();
		} catch (Exception ex) {
		}
		return tmp;
	}
}
