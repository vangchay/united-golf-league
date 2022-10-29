package com.test;

import java.util.ArrayList;
import java.util.List;

import com.ugl.common.Cout;
import com.ugl.handicap.Course;
import com.ugl.handicap.Player;

/**
 * @author e1042631
 *
 */
public class testPlayer {
	private final static String PARAM1 = "param1> player.csv file.";

	private final static String PARMA2 = "param2> course.csv file.";

	/**
	 * 
	 */
	public testPlayer() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length <= 1) {
			Cout.outString("Missing input file name argument.");
			Cout.outString(PARAM1);
			Cout.outString(PARMA2);
		} else {
			List<Player> playList = new ArrayList<Player>();
			List<Course> courseList = new ArrayList<Course>();
			String fileName = args[0];
			String courseFileName = args[1];
			Player.parsePlayers(fileName, playList);
			Course.parseCourse(courseFileName, courseList);
			for (Player p : playList) {
				Cout.outString(p.toString());
				p.calculateScores(true, courseList.get(0));
				String[] line = p.generateCSVLine(courseList.get(0));
				String msg = "";
				for (String s : line) {
					msg = msg + String.format("%s, ", s);
				}
				Cout.outString(msg);
				// cout.outString(p.toString());
			}

		}
	}

}
