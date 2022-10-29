package com.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ugl.handicap.Course;
import com.ugl.handicap.HMGReport;
import com.ugl.handicap.Player;
import com.ugl.handicap.Schedule;
import com.ugl.handicap.ScoreCards;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author e1042631
 *
 */
public class testHMGReport {
	private static List<Schedule> scheduleList = new ArrayList<Schedule>();

	private static List<ScoreCards> Listscores = new ArrayList<ScoreCards>();

	private static List<Player> listPlayer = new ArrayList<Player>();

	private static List<Course> golfCourses = new ArrayList<Course>();

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		HMGReport.setHPIScores(4);
		HMGReport.setTotalWeeks(12);
		HMGReport.setNineHoles(true);
		Schedule.parseSchedule(test.SCHEDULE_CSV, scheduleList);
		ScoreCards.parseScoreCard(test.SCORECARDS_CSV, Listscores);
		Player.parsePlayers(test.PLAYER_CSV, listPlayer);
		Course.parseCourse(test.COURSE_CSV, golfCourses);
	}

	// /**
	// * Test method for {@link
	// com.handicap.HMGReport#generatePlayerHTML(com.handicap.Course,
	// java.util.List,
	// boolean)}.
	// */
	// @Test
	// public void testGeneratePlayerHTML() {
	// HMGReport.generatePlayerHTML(golfCourses.get(0), listPlayer, true);
	// }

	/**
	 * Test method for
	 * {@link com.ugl.handicap.HMGReport#generatePlayerCSV(com.ugl.handicap.Course, java.util.List, boolean)}.
	 */
	@Test
	public void testGeneratePlayerCSV() {
		// fail("Not yet implemented");
		// fail("Not yet implemented");
		Course course = golfCourses.get(0);
		List<String> match = HMGReport.generateMatchReport(course, scheduleList, Listscores, listPlayer, 1);
		try {
			FileUtils.writeLines(new File("C:\\HMG\\test\\match.html"), match);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<String> play = HMGReport.generatePlayerCSV(course, listPlayer, true);
		try {
			FileUtils.writeLines(new File(test.PLAYER_CSV + "_testout.csv"), play);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<String> rpt = HMGReport.generatePlayerHTML(course, listPlayer, true);
		try {
			FileUtils.writeLines(new File("C:\\HMG\\test\\playertest.html"), rpt);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// /**
	// * Test method for {@link
	// com.handicap.HMGReport#generateMatchReport(com.handicap.Course,
	// java.util.List,
	// java.util.List, java.util.List, int)}.
	// */
	// @Test
	// public void testGenerateMatchReport() {
	// //fail("Not yet implemented");
	// HMGReport.generateMatchReport(golfCourses.get(0), scheduleList, Listscores,
	// listPlayer, 1);
	// }

	// /**
	// * Test method for {@link
	// com.handicap.HMGReport#calStrokes(com.handicap.Player, com.handicap.Player,
	// com.handicap.ScoreCards, com.handicap.ScoreCards, com.handicap.Course,
	// int[][])}.
	// */
	// @Test
	// public void testCalStrokes() {
	// fail("Not yet implemented");
	// }
	//
	// /**
	// * Test method for {@link com.handicap.HMGReport#RoundIt(double)}.
	// */
	// @Test
	// public void testRoundIt() {
	// fail("Not yet implemented");
	// }
	//
	// /**
	// * Test method for {@link com.handicap.HMGReport#findScoreCard(int, int,
	// java.util.List)}.
	// */
	// @Test
	// public void testFindScoreCard() {
	// fail("Not yet implemented");
	// }
	//
	// /**
	// * Test method for {@link com.handicap.HMGReport#findPlayer(int,
	// java.util.List)}.
	// */
	// @Test
	// public void testFindPlayer() {
	// fail("Not yet implemented");
	// }
	//
	// /**
	// * Test method for {@link
	// com.handicap.HMGReport#calculateHPI(com.handicap.Player, com.handicap.Course,
	// boolean)}.
	// */
	// @Test
	// public void testCalculateHPI() {
	// fail("Not yet implemented");
	// }
	//
	// /**
	// * Test method for {@link com.handicap.HMGReport#setHPIScores(int)}.
	// */
	// @Test
	// public void testSetHPIScores() {
	// fail("Not yet implemented");
	// }
	//
	// /**
	// * Test method for {@link com.handicap.HMGReport#setTotalWeeks(int)}.
	// */
	// @Test
	// public void testSetTotalWeeks() {
	// fail("Not yet implemented");
	// }
	//
	// /**
	// * Test method for {@link com.handicap.HMGReport#getTotalWeeks()}.
	// */
	// @Test
	// public void testGetTotalWeeks() {
	// fail("Not yet implemented");
	// }
	//
	// /**
	// * Test method for {@link com.handicap.HMGReport#getNumberHPIScores()}.
	// */
	// @Test
	// public void testGetNumberHPIScores() {
	// fail("Not yet implemented");
	// }

	/**
	 * Test method for {@link com.ugl.handicap.HMGReport#HMGReport()}.
	 */
	@Test
	public void testHMGReport() {
		// fail("Not yet implemented");
	}

}
