package com.unitedgolfleague;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

import com.ugl.common.Cout;
import com.ugl.common.Utils;
import com.ugl.handicap.HMGReport;

public class HMGProperties implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -7580615992503196808L;

    public final static String HMG_INI_FILE = "C:\\HMG\\properties\\settings.ini";

    public final static String HMG_PROPERTIES = "C:\\HMG\\properties\\players.properties";

    public final static String PLAYER_NUMBER_OF = "player-number-of";

    public final static String PLAYER_WEEKS = "player-weeks";

    public final static String PLAYER_DEFAULT_DIR = "player-default-dir";

    public final static String PLAYER_DEFAULT_REPORTS = "player-default-report-dir";

    public final static String PLAYER_HPI_SAMPLE = "player-hpi-sample";

    public final static String PLAYER_HOLES = "player-number-of-holes";

    public final static String PLAYER_FILE = "players.csv";

    public final static String SCHEDULE_FILE = "schedule.csv";

    public final static String TOURNAMENT_SCALE = "tournament-scale";

    public final static String TOURNAMENT_PTS = "tournament-points";

    public final static String TOURNAMENT_SLOPE = "tournament-slope";

    public final static String MATCH_HOLE_PTS = "matchplay-hole-points";

    public final static String MATCH_WIN_PTS = "playplay-winner-points";

    public final static String MATCH_POINTS_MATCH = "MATCH_POINTS_MATCH";

    private static final String PERCENT_POOL = "percent-pool";

    private static final Object GOLF_COURSE_CSV = "golf-course-csv";

    private static final Object BYE_NUMBER = "bye-number";

    public static int HPISample = 4;

    public static int numberOfPlayers = 0;

    public static int numberOfWeeks = 12;

    public static int numberHoles = 9;

    public static String defaultDir = "C:\\HMG";

    public static String defaultReports = "C:\\HMG\\reports";

    public static boolean bInit = false;

    private static int tournamentPoints = 0;

    private static String[] tournamentScale = null;

    public static Integer[] UnitedCupPointsTable = null;

    public static int matchMaxTotalPoints = 21;

    public static int matchPointsPerHole = 2;

    public static int matchWinPoints = 3;

    private static double unitedCupPoints = 0;

    public static double percentPool = .25;

    public static String courseCsvFile = null;

    /**
     * A player ID that is the bye week in the case where a league has an un-even
     * amount of players...
     */
    public static int fakePlayerIdForBye = -1;

    /**
     * The slope of the league tournament golf course...
     */
    public static int tournamentSlope = 113;

    /**
     * Load properties file.
     * 
     * @param propsFile
     */
    public static void load(String propsFile) {
	if (bInit == true) {
	    return;
	}
	bInit = true;
	if (propsFile == null || propsFile.isEmpty()) {
	    propsFile = HMG_PROPERTIES;
	}
	Properties props = new Properties();
	try {
	    props.load(new FileInputStream(propsFile));
	    numberOfPlayers = Integer.parseInt((String) props.getOrDefault(PLAYER_NUMBER_OF, "0"));
	    numberOfWeeks = Integer.parseInt((String) props.getOrDefault(PLAYER_WEEKS, "12"));
	    numberHoles = Integer.parseInt((String) props.getOrDefault(PLAYER_HOLES, "9"));
	    defaultDir = (String) props.getOrDefault(PLAYER_DEFAULT_DIR, defaultDir);
	    defaultReports = (String) props.getOrDefault(PLAYER_DEFAULT_REPORTS, defaultReports);
	    HPISample = Integer.parseInt((String) props.getOrDefault(PLAYER_HPI_SAMPLE, "4"));
	    matchMaxTotalPoints = Integer.parseInt((String) props.getOrDefault(MATCH_POINTS_MATCH, "21"));
	    matchPointsPerHole = Integer.parseInt((String) props.getOrDefault(MATCH_HOLE_PTS, "2"));
	    matchWinPoints = Integer.parseInt((String) props.getOrDefault(MATCH_WIN_PTS, "3"));
	    tournamentPoints = Integer.parseInt((String) props.getOrDefault(TOURNAMENT_PTS, "2"));
	    tournamentScale = Utils.parseRecString((String) props.getOrDefault(TOURNAMENT_SCALE, "100,0"));
	    tournamentSlope = Integer.parseInt((String) props.getOrDefault(TOURNAMENT_SLOPE, "113"));
	    percentPool = Double.parseDouble((String) props.getOrDefault(PERCENT_POOL, "0.25"));
	    courseCsvFile = (String) props.getOrDefault(GOLF_COURSE_CSV, "golf-courses.csv");
	    fakePlayerIdForBye = Integer.parseInt((String) props.getOrDefault(BYE_NUMBER, "-1"));
	    try {
		UnitedCupPointsTable = GenerateUnitedCupsPoints(tournamentScale);
		Cout.outString("***United Cup Tourney Points Table****");
		Cout.outString("place, points");
		int Place = 1;
		int Sum = 0;
		for (int i = 0; i < UnitedCupPointsTable.length; i++) {
		    Integer ii = UnitedCupPointsTable[i];
		    Sum = Sum + ii.intValue();
		    Cout.outString(Integer.toString(Place) + ", " + ii.toString());
		    Place++;
		}
		Cout.outString(Integer.toString(Sum));
		Cout.outString(Double.toString(unitedCupPoints));
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /***
     * This function generates the UNITED CUP points.
     * 
     * @param scale
     * @return
     */
    private static Integer[] GenerateUnitedCupsPoints(String[] scale) {
	Integer[] tbl = new Integer[numberOfPlayers];
	Double[] unitedTbl = new Double[scale.length];
	// points = (number of matches) * (match points) * (weeks) * .25 percent
	unitedCupPoints = numberOfPlayers / 2 * matchMaxTotalPoints * numberOfWeeks * percentPool;
	int index = 0;
	for (String s : scale) {
	    double d1 = Double.parseDouble(s.trim());
	    d1 = (d1 * unitedCupPoints) / (double) tournamentPoints;
	    unitedTbl[index++] = d1;
	}
	for (int i = 0; i < numberOfPlayers; i++) {
	    int val = HMGReport.RoundIt(unitedTbl[i]);
	    tbl[i] = new Integer(val);
	}
	return tbl;
    }

}