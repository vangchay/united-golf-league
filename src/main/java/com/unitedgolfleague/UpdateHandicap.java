
package com.unitedgolfleague;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.ugl.common.Cout;
import com.ugl.handicap.Course;
import com.ugl.handicap.HMGReport;
import com.ugl.handicap.Player;

/**
 * @author e1042631
 *
 */
public class UpdateHandicap {
	private final static String CLASS_NAME = "updateHandicap.";

	private final static String MAIN = CLASS_NAME + "main() ";

	private final static String ARG1 = MAIN + "ARG1: NUMBER OF WEEKS ";

	private final static String ARG2 = MAIN + "ARG2: GOLF COURSE INDEX START AT 0 ";

	private final static String ARG3 = MAIN + "ARG3: NUMBER HOLES 9 OR 18 ";

	private final static String ARG4 = MAIN + "ARG4: PLAYER NAME TO UPDATE ";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Cout.outString(ARG1);
		Cout.outString(ARG2);
		Cout.outString(ARG3);
		Cout.outString(ARG4);

		if (args.length < 5) {
			Cout.outString("Not enough parameters!");
		} else {
			int index = 0;
			String playersFile = "C:/HMG/players.csv";
			String courseFile = "C:/HMG/course.csv";
			int numWeeks = Integer.parseInt(args[index++]);
			int courseIndex = Integer.parseInt(args[index++]);
			int holes = Integer.parseInt(args[index++]);
			boolean bNine = false;
			if (holes == 9) {
				bNine = true;
			}
			String playerName = args[index++];

			ArrayList<Player> listOfPlayers = new ArrayList<Player>();
			HMGReport.setTotalWeeks(numWeeks);
			HMGReport.setNineHoles(bNine);
			Player.parsePlayers(playersFile, listOfPlayers);

			Player myPlayer = HMGReport.findPlayer(playerName, listOfPlayers).get();
			if (myPlayer == null) {
				Cout.outString("Cannot find player " + playerName);
			} else {
				List<Course> courseList = new ArrayList<Course>();
				Course.parseCourse(courseFile, courseList);
				Course theCourse = courseList.get(courseIndex);
				// myPlayer.calculateScores(bNine, theCourse);
				for (Player p : listOfPlayers) {
					p.calculateScores(bNine, courseList.get(courseIndex));
				}
				List<String> var = HMGReport.generatePlayerCSV(theCourse, listOfPlayers, bNine);
				try {
					FileUtils.writeLines(new File(playersFile), var);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		Cout.outString("Bye!");
	}

}