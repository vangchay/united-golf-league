
package com.unitedgolfleague;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.google.common.collect.Multimap;
import com.ugl.common.Cout;
import com.ugl.common.Utils;
import com.ugl.handicap.HMGReport;
import com.ugl.handicap.Player;
import com.ugl.handicap.SchedUtil;
import com.ugl.handicap.Schedule;
import com.ugl.speadsheet.PlayersSpreedsheet;

/**
 * @author e1042631
 *
 */
public class GenRandomSched {
	private static final String CLASS_NAME = "genRandomSched.";

	private static final String MAIN = CLASS_NAME + "main() ";

	private static final String ARG1 = "-array <List of numbers example: 1,2,3,4>";

	private static final String ARG2 = "-weeks <Number of weeks>";

	private static final String ARG3 = "-props <properties file name>";

	private static final String ARG4 = "-week <starting week>";

	private static String propertiesFileName = null;

	private static int Weeks = 0;

	private static int StartingWeek = 0;

	private static List<Player> TotalPlayersList = null;

	private static int ScheduleIndex = 1;

	private static String sheet = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Cout.outString(MAIN);
		Cout.outString(ARG1);
		Cout.outString(ARG2);
		Cout.outString(ARG3);
		Cout.outString(ARG4);

		Multimap<String, String> mapArgs = Utils.ScanArguments(args);
		try {
			Weeks = Integer.parseInt(mapArgs.get("weeks").iterator().next());
			propertiesFileName = mapArgs.get("props").iterator().next();
			HMGProperties.load(propertiesFileName);
			sheet = mapArgs.get("sheet").iterator().next();
			StartingWeek = Integer.parseInt(mapArgs.get("week").iterator().next());
			PlayersSpreedsheet.Init(sheet);
		} catch (Exception ex) {
		}

		List<int[]> nTblPlayersID = new ArrayList<int[]>();
		Iterator<String> iter = mapArgs.get("array").iterator();
		while (iter.hasNext()) {
			String val = iter.next();
			String[] strTbl = Utils.parseRecString(val);
			int[] nTbl = new int[strTbl.length];
			for (int i = 0; i < strTbl.length; i++) {
				nTbl[i] = Integer.parseInt(strTbl[i].trim());
			}
			shuffleArray(nTbl);
			nTblPlayersID.add(nTbl);
		}

		// Now generate the schedule...
		HMGProperties.load(propertiesFileName);

		TotalPlayersList = PlayersSpreedsheet.ListPlayers;
		int nGroup = 1;
		for (int[] tbl : nTblPlayersID) {
			Cout.outString("****GROUP " + Integer.toString(nGroup++));
			createNewSchedule(Weeks, tbl, TotalPlayersList);
		}
		Cout.outString("bye!");

	}

	/**
	 * Create new schedule...
	 * 
	 * @param nbrWeeks
	 * @param playerNbrTbl
	 * @param listTotalPlayers
	 */
	private static void createNewSchedule(int nbrWeeks, int[] playerNbrTbl, List<Player> listTotalPlayers) {
		List<Player> groupPlayers = new ArrayList<Player>();
		for (int id : playerNbrTbl) {
			Player p = HMGReport.findPlayer(id, listTotalPlayers);
			groupPlayers.add(p);
		}
		List<Schedule> schedule = SchedUtil.generateSchedule(StartingWeek, nbrWeeks, groupPlayers,
		        PlayersSpreedsheet.ListSchedules);
		for (Schedule sh : schedule) {
			Cout.outString(sh.toString(groupPlayers));
		}
		List<String> out = new ArrayList<String>();
		for (Schedule sh : schedule) {
			String str = sh.toString() + ", " + sh.toString(groupPlayers);
			out.add(str);
		}
		String outFile = String.format("schedule-%d.csv", ScheduleIndex++);
		Cout.outString("****Generating output file : " + outFile);
		String outfile = FilenameUtils.concat(HMGProperties.defaultReports, outFile);
		try {
			FileUtils.writeLines(new File(outfile), out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Implementing FisherYates shuffle
	static void shuffleArray(int[] ar) {
		for (int i : ar) {
			Cout.outString(MAIN + "****OLD Order: " + Integer.toString(i));
		}
		// If running on Java 6 or older, use `new Random()` on RHS here
		Random rnd = ThreadLocalRandom.current();
		for (int counter = 0; counter < 4; counter++) {
			for (int i = ar.length - 1; i > 0; i--) {
				int index = rnd.nextInt(i + 1);
				if (index > ar.length) {
					index = 0;
				} else if (index == i) {
					continue;
				}
				// Simple swap
				int a = ar[index];
				ar[index] = ar[i];
				ar[i] = a;
			}
		}
		for (int i : ar) {
			Cout.outString(MAIN + "****New Order: " + Integer.toString(i));
		}
	}

}
