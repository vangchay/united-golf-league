package com.ugl.handicap;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Vector;

import com.ugl.common.Cout;
import com.ugl.common.Utils;
import com.unitedgolfleague.HMGProperties;

/**
 * @author e1042631
 *
 */
public class HMGReport {
	/**
	 * Current Week...
	 */
	public static int CurrentWeek = -1;

	/**
	 * Update the players HPI ONLY...
	 */
	public static boolean UpdatePlayerHPIMode = true;

	/**
	 * 9 HOLES?
	 */
	private static boolean nineHoles;

	/**
	 * Number of weeks in league...
	 */
	public static int totalWeeks = 12;

	/**
	 * Number of scores to calculate HPI...
	 */
	private static int numHPIScores = 4;

	/**
	 * CSV header...
	 */
	private static String csvHeader;

	public final static String REPORT_FILE = "HMGReport.txt";

	public final static String REPORT_LINE1 = "<h1>***Match Play Results***</h1>";

	public final static String REPORT_LINE2 = "<ul><li>score/effective score with handicap applied<sub>win/draw</sub></li>";

	public final static String REPORT_LINE3 = "<li>w - win 2 points | d - draw 1 point</li>";

	public final static String REPORT_LINE4 = "<li>hpi win - 3 points</li>";

	public final static String REPORT_LINE5 = "<li>hpi - handicap index | pts - points in match | total pts - total points earned</li>";

	public final static String REPORT_LINE6 = "</ul>";

	public final static String CSV_HEADER = "****ID,NAME,EMAIL,PHONE,1ST HPI,HPI,18 HP,9 HP,POINTS,";// WK1,WK2,WK3,WK4,WK5,WK6,WK7,WK8,WK9,WK10,WK11,WK12,AVE,STD";

	public final static String CSV_TRAILER = "AVE,STD";

	public final static String PLAYER_LINE1 = "<h1>***Players Summery Report %s***</h1>";

	/**
	 * Generate players XML...
	 * 
	 * @param golfCourse
	 * @param players
	 * @param bNine
	 * @return
	 */
	public static List<String> generatePlayerHTML(Course golfCourse, List<Player> players, boolean bNine) {
		Timestamp todaytm = new Timestamp(new Date().getTime());
		String yyyymmdd = new SimpleDateFormat("MM-dd-yyyy").format(todaytm);

		Collections.sort(players);

		List<String> rpt = new ArrayList<String>();
		HtmlUtil.startHTML(rpt);
		HtmlUtil.bodyStartHTML();
		HtmlUtil.paraStartHTML(String.format(PLAYER_LINE1, yyyymmdd));
		HtmlUtil.tableStartHTML();
		HtmlUtil.tableRowStartHTML();

		genCsvHeader();

		Vector<String> vectHead = Utils.parseRec(csvHeader);
		int size = vectHead.size();
		for (int i = 0; i < size; i++) {
			// Skip email and phone #
			if (i == 2 || i == 3) {
				continue;
			}
			String s = vectHead.get(i);
			if (s == null) {
				s = "";
			}
			HtmlUtil.tableColumnHTML(s);
		}
		HtmlUtil.tableRowEndHTML();

		for (Player p : players) {
			HtmlUtil.tableRowStartHTML();
			p.calculateScores(bNine, golfCourse);
			String[] arrLine = p.generateCSVLine(golfCourse);
			int fieldNumber = 0;
			for (String s : arrLine) {
				// Skip email and phone #
				++fieldNumber;
				if (fieldNumber == 3 || fieldNumber == 4) {
					continue;
				}
				if (s == null) {
					continue;
				}
				HtmlUtil.tableColumnHTML(s);
			}
			HtmlUtil.tableRowEndHTML();
		}

		HtmlUtil.tableEndHTML();
		HtmlUtil.bodyEndHTML();
		HtmlUtil.endHTML();
		return rpt;
	}

	private static void genCsvHeader() {
		csvHeader = CSV_HEADER;
		for (int i = 0; i < totalWeeks; i++) {
			String tmp = String.format("WK%d,", i + 1);
			csvHeader = csvHeader + tmp;
		}
		csvHeader = csvHeader + CSV_TRAILER;
	}

	/**
	 * Generate the player's CSV...
	 * 
	 * @param golfCourse
	 * @param players
	 * @param bNine
	 * @return
	 */
	public static List<String> generatePlayerCSV(Course golfCourse, List<Player> players, boolean bNine) {
		List<String> rpt = new ArrayList<String>();
		genCsvHeader();
		rpt.add(csvHeader);

		Collections.sort(players);

		for (Player p : players) {
			p.calculateScores(bNine, golfCourse);
			String[] arrLine = p.generateCSVLine(golfCourse);
			String line = "";
			for (String s : arrLine) {
				if (s == null) {
					continue;
				}
				line = line + s + ", ";
			}
			rpt.add(line);
		}
		return rpt;
	}

	/**
	 * Generate the match report...
	 * 
	 * @param golfCourse   - the golf course
	 * @param scheduleList - the schedule
	 * @param Listscores   - the score card
	 * @param listPlayer   - the player
	 * @param weekNo       - the weekend number
	 * @return
	 */
	public static List<String> generateMatchReport(Course golfCourse, List<Schedule> scheduleList,
	        List<ScoreCards> Listscores, List<Player> listPlayer, int weekNo) {
		List<String> rpt = new ArrayList<String>();

		HtmlUtil.startHTML(rpt);
		HtmlUtil.bodyStartHTML();

		HtmlUtil.paraStartHTML(REPORT_LINE1);
		HtmlUtil.paraStartHTML(REPORT_LINE2);
		HtmlUtil.paraStartHTML(REPORT_LINE3);
		HtmlUtil.paraStartHTML(REPORT_LINE4);
		HtmlUtil.paraStartHTML(REPORT_LINE5);
		HtmlUtil.paraStartHTML(REPORT_LINE6);

		// Go thru the schedule
		int matchNo = 0;
		int Week = -1;
		for (Schedule currentSchedule : scheduleList) {
			// Only parse this week...
			if (currentSchedule.Week != weekNo) {
				continue;
			}

			matchNo++;
			String msg = null;
			msg = String.format("Processing Week %d, %d vs %d", currentSchedule.Week, currentSchedule.ID1,
			        currentSchedule.ID2);
			Cout.outString(msg);

			// find the players
			Player thePlayer1 = findPlayer(currentSchedule.ID1, listPlayer);
			Player thePlayer2 = findPlayer(currentSchedule.ID2, listPlayer);
			// DEBUG
			// if (currentSchedule.ID1 == 3) {
			// cout.outString("debug");
			// }

			// FIND THE BYE ?
			if (currentSchedule.ID1 == HMGProperties.fakePlayerIdForBye) {
				Cout.outString("PLAYER 1 BYE");
				// currentSchedule.ID1 = currentSchedule.ID2;
			}
			if (currentSchedule.ID2 == HMGProperties.fakePlayerIdForBye) {
				Cout.outString("PLAYER 2 BYE");
				// currentSchedule.ID2 = currentSchedule.ID1;
			}
			if (thePlayer1 == null || thePlayer2 == null) {
				Cout.outString("Error player doesn't exist.");
				continue;
			}

			// find the score card player 1 and player 2
			ScoreCards cardP1 = findScoreCard(currentSchedule.ID1, currentSchedule.Week, Listscores);
			if (cardP1 != null) {
				thePlayer1.UpdateScore(cardP1, currentSchedule.Week);
			}
			ScoreCards cardP2 = findScoreCard(currentSchedule.ID2, currentSchedule.Week, Listscores);
			if (cardP2 != null) {
				thePlayer2.UpdateScore(cardP2, currentSchedule.Week);

			}

			// boolean bHCP = false;
			// the player is playing himself...
			if (currentSchedule.ID1 == currentSchedule.ID2) {
				thePlayer2 = thePlayer1.clone(thePlayer1); // player 2 is a clone of player 1
				cardP2 = ScoreCards.GenHCPScore(thePlayer1, golfCourse, cardP1);
				// bHCP = true;
			}

			if (cardP1 != null && cardP2 == null) {
				thePlayer2 = thePlayer1.clone(thePlayer1);
				cardP2 = ScoreCards.GenHCPScore(thePlayer1, golfCourse, cardP1);
				// bHCP = true;
			} else if (cardP1 == null && cardP2 != null) {
				thePlayer1 = thePlayer2.clone(thePlayer2);
				cardP1 = ScoreCards.GenHCPScore(thePlayer2, golfCourse, cardP2);
				// bHCP = true;
			} else if (cardP1 == null || cardP2 == null) {
				msg = String.format("Missing score card for match %d, %s vs %s", matchNo, thePlayer1.getName(),
				        thePlayer2.getName());
				Cout.outString(msg);
				continue;
			}

			// Reset the week
			if (Week != currentSchedule.Week) {
				// myutx.paraStartHTML(String.format("Week %d", sch.Week));
				rpt.add(String.format("<h2>Week %d</h2>", currentSchedule.Week));
				Week = currentSchedule.Week;
				matchNo = 1;
			}

			// Parse the match
			HtmlUtil.breakHTML();
			int[] tblPt = parseMatch(rpt, golfCourse, thePlayer1, thePlayer2, cardP1, cardP2, matchNo);
			// if (bHCP == false) {
			thePlayer1.setTotalPoints(thePlayer1.getTotalPoints() + tblPt[0]);
			thePlayer2.setTotalPoints(thePlayer2.getTotalPoints() + tblPt[1]);
			// }
			// else {
			// thePlayer1.setTotalPoints(thePlayer1.getTotalPoints() + tblPt[0]);
			// }

		}

		HtmlUtil.bodyEndHTML();
		HtmlUtil.endHTML();
		return rpt;
	}

	/**
	 * Parse the match...
	 * 
	 * @param report    string collection.
	 * @param golf      course.
	 * @param player    1.
	 * @param player    2.
	 * @param scorecard 1.
	 * @param scorecard 2.
	 * @param match     number.
	 * @return total points array [] for each player.
	 */
	private static int[] parseMatch(List<String> hmgReport, Course golfCourse, Player playerOne, Player playerTwo,
	        ScoreCards cardOne, ScoreCards cardTwo, int matchNo) {
		int totalPoints[] = new int[2];
		int strokes[][] = new int[2][18];

		HtmlUtil.tableStartHTML();
		HtmlUtil.tableRowStartHTML();
		HtmlUtil.tableColumnHTML(String.format("Match%d", matchNo));
		HtmlUtil.tableColumnHTML("hpi/hp18/hp9");
		HtmlUtil.tableColumnHTML(
		        "h1<sub>" + Integer.toString(golfCourse.getCourseInfo().get(0).getStrokeIndex()) + "</sub>");
		HtmlUtil.tableColumnHTML(
		        "h2<sub>" + Integer.toString(golfCourse.getCourseInfo().get(1).getStrokeIndex()) + "</sub>");
		HtmlUtil.tableColumnHTML(
		        "h3<sub>" + Integer.toString(golfCourse.getCourseInfo().get(2).getStrokeIndex()) + "</sub>");
		HtmlUtil.tableColumnHTML(
		        "h4<sub>" + Integer.toString(golfCourse.getCourseInfo().get(3).getStrokeIndex()) + "</sub>");
		HtmlUtil.tableColumnHTML(
		        "h5<sub>" + Integer.toString(golfCourse.getCourseInfo().get(4).getStrokeIndex()) + "</sub>");
		HtmlUtil.tableColumnHTML(
		        "h6<sub>" + Integer.toString(golfCourse.getCourseInfo().get(5).getStrokeIndex()) + "</sub>");
		HtmlUtil.tableColumnHTML(
		        "h7<sub>" + Integer.toString(golfCourse.getCourseInfo().get(6).getStrokeIndex()) + "</sub>");
		HtmlUtil.tableColumnHTML(
		        "h8<sub>" + Integer.toString(golfCourse.getCourseInfo().get(7).getStrokeIndex()) + "</sub>");
		HtmlUtil.tableColumnHTML(
		        "h9<sub>" + Integer.toString(golfCourse.getCourseInfo().get(8).getStrokeIndex()) + "</sub>");
		HtmlUtil.tableColumnHTML(
		        "h10<sub>" + Integer.toString(golfCourse.getCourseInfo().get(9).getStrokeIndex()) + "</sub>");
		HtmlUtil.tableColumnHTML(
		        "h11<sub>" + Integer.toString(golfCourse.getCourseInfo().get(10).getStrokeIndex()) + "</sub>");
		HtmlUtil.tableColumnHTML(
		        "h12<sub>" + Integer.toString(golfCourse.getCourseInfo().get(11).getStrokeIndex()) + "</sub>");
		HtmlUtil.tableColumnHTML(
		        "h13<sub>" + Integer.toString(golfCourse.getCourseInfo().get(12).getStrokeIndex()) + "</sub>");
		HtmlUtil.tableColumnHTML(
		        "h14<sub>" + Integer.toString(golfCourse.getCourseInfo().get(13).getStrokeIndex()) + "</sub>");
		HtmlUtil.tableColumnHTML(
		        "h15<sub>" + Integer.toString(golfCourse.getCourseInfo().get(14).getStrokeIndex()) + "</sub>");
		HtmlUtil.tableColumnHTML(
		        "h16<sub>" + Integer.toString(golfCourse.getCourseInfo().get(15).getStrokeIndex()) + "</sub>");
		HtmlUtil.tableColumnHTML(
		        "h17<sub>" + Integer.toString(golfCourse.getCourseInfo().get(16).getStrokeIndex()) + "</sub>");
		HtmlUtil.tableColumnHTML(
		        "h18<sub>" + Integer.toString(golfCourse.getCourseInfo().get(17).getStrokeIndex()) + "</sub>");
		HtmlUtil.tableColumnHTML("match pts");
		HtmlUtil.tableColumnHTML("score");
		HtmlUtil.tableColumnHTML("match hpi");
		HtmlUtil.tableColumnHTML("total pts");
		HtmlUtil.tableRowEndHTML();

		String[] arrMsgPlayer1 = new String[24];
		String[] arrMsgPlayer2 = new String[24];
		int index1 = 0;
		int index2 = 0;
		arrMsgPlayer1[index1++] = playerOne.getName();
		arrMsgPlayer2[index2++] = playerTwo.getName();
		// hp index
		double hpi = playerOne.getHPIndex();
		// 18 hole hp
		int hp = RoundIt(HcpUtils.calculateHandicap(golfCourse.getRating(), golfCourse.getSlope(), hpi));
		// 9 hole hp
		int hp9 = RoundIt(HcpUtils.calculateHandicap(golfCourse.getRating(), golfCourse.getSlope(), hpi) / 2);
		arrMsgPlayer1[index1++] = String.format("%.1f/ %d/ %d", playerOne.getHPIndex(), hp, hp9);
		hpi = playerTwo.getHPIndex();
		hp = RoundIt(HcpUtils.calculateHandicap(golfCourse.getRating(), golfCourse.getSlope(), hpi));
		hp9 = RoundIt(HcpUtils.calculateHandicap(golfCourse.getRating(), golfCourse.getSlope(), hpi) / 2);
		arrMsgPlayer2[index2++] = String.format("%.1f/ %d/ %d", playerTwo.getHPIndex(), hp, hp9);

		calStrokes(playerOne, playerTwo, cardOne, cardTwo, golfCourse, strokes);

		int totalPts1 = 0;
		int totalPts2 = 0;
		int playerScore1 = 0;
		int playerScore2 = 0;

		// TODO: limitations to 18 hole match...
		for (int hole = 0; hole < 18; hole++) {
			playerScore1 += cardOne.Scores[hole];
			playerScore2 += cardTwo.Scores[hole];

			if ((cardOne.Scores[hole] == null || cardTwo.Scores[hole] == null)
			        || (cardOne.Scores[hole] == ScoreCards.INCOMPLETE
			                && cardTwo.Scores[hole] == ScoreCards.INCOMPLETE)) {
				arrMsgPlayer1[index1++] = ScoreCards.INC;
				arrMsgPlayer2[index2++] = ScoreCards.INC;
				continue;
			}

			if (cardOne.Scores[hole] == ScoreCards.INCOMPLETE) {
				// Player 2 wins
				totalPts2 += 2;
				arrMsgPlayer1[index1++] = ScoreCards.INC;
				arrMsgPlayer2[index2++] = Integer.toString(cardTwo.Scores[hole]) + "<sub>w</sub>";
				continue;
			} else if (cardTwo.Scores[hole] == ScoreCards.INCOMPLETE) {
				// Player 1 winds
				totalPts1 += 2;
				arrMsgPlayer1[index1++] = Integer.toString(cardOne.Scores[hole]) + "<sub>w</sub>";
				arrMsgPlayer2[index2++] = ScoreCards.INC;
				continue;
			}

			Integer player1Score = cardOne.Scores[hole];
			Integer player2Score = cardTwo.Scores[hole];
			Integer player1EffScore = player1Score - strokes[0][hole];
			Integer player2EffScore = player2Score - strokes[1][hole];

			if (player1EffScore < player2EffScore) {
				totalPts1 += 2;
				arrMsgPlayer1[index1++] = formatScore(player1Score, player1EffScore, "<sub>w</sub>");
				arrMsgPlayer2[index2++] = formatScore(player2Score, player2EffScore, "");
			} else if (player2EffScore < player1EffScore) {
				totalPts2 += 2;
				arrMsgPlayer1[index1++] = formatScore(player1Score, player1EffScore, "");// Integer.toString(cardOne.Scores[hole]);
				arrMsgPlayer2[index2++] = formatScore(player2Score, player2EffScore, "<sub>w</sub>");// Integer.toString(cardTwo.Scores[hole])
				                                                                                     // +
				                                                                                     // "<sub>w</sub>";
			} else {
				totalPts1++;
				totalPts2++;
				arrMsgPlayer1[index1++] = formatScore(player1Score, player1EffScore, "<sub>d</sub>");// Integer.toString(cardOne.Scores[hole])
				                                                                                     // +
				                                                                                     // "<sub>d</sub>";
				arrMsgPlayer2[index2++] = formatScore(player2Score, player2EffScore, "<sub>d</sub>");// Integer.toString(cardTwo.Scores[hole])
				                                                                                     // +
				                                                                                     // "<sub>d</sub>";
			}
		}

		double hpi1 = 0;
		double hpi2 = 0;

		if (nineHoles == true) {
			hpi1 = HcpUtils.calculateHandicapIndex(golfCourse.getRating(), golfCourse.getSlope(), playerScore1 * 2);
			hpi2 = HcpUtils.calculateHandicapIndex(golfCourse.getRating(), golfCourse.getSlope(), playerScore2 * 2);
		} else {
			hpi1 = HcpUtils.calculateHandicapIndex(golfCourse.getRating(), golfCourse.getSlope(), playerScore1);
			hpi2 = HcpUtils.calculateHandicapIndex(golfCourse.getRating(), golfCourse.getSlope(), playerScore2);
		}

		int hpPts1 = 0;
		int hpPts2 = 0;
		if (cardOne.bIncompleteScore == false && hpi1 < playerOne.getHPIndex()) {
			hpPts1 = 3;
		}
		if (cardTwo.bIncompleteScore == false && hpi2 < playerTwo.getHPIndex()) {
			hpPts2 = 3;
		}

		totalPoints[0] = totalPts1 + hpPts1;
		totalPoints[1] = totalPts2 + hpPts2;

		// points
		arrMsgPlayer1[index1++] = Integer.toString(totalPts1);
		arrMsgPlayer2[index2++] = Integer.toString(totalPts2);
		// score
		if (cardOne.bIncompleteScore == false) {
			arrMsgPlayer1[index1++] = Integer.toString(playerScore1);
		} else {
			arrMsgPlayer1[index1++] = ScoreCards.INC;
		}
		if (cardTwo.bIncompleteScore == false) {
			arrMsgPlayer2[index2++] = Integer.toString(playerScore2);
		} else {
			arrMsgPlayer2[index2++] = ScoreCards.INC;
		}

		// hpi
		if (cardOne.bIncompleteScore == false) {
			arrMsgPlayer1[index1++] = String.format("%.1f", hpi1);
		} else {
			arrMsgPlayer1[index1++] = ScoreCards.INC;
		}
		if (cardTwo.bIncompleteScore == false) {
			arrMsgPlayer2[index2++] = String.format("%.1f", hpi2);
		} else {
			arrMsgPlayer2[index2++] = ScoreCards.INC;
		}

		// total pts
		arrMsgPlayer1[index1++] = Integer.toString(totalPoints[0]);
		arrMsgPlayer2[index2++] = Integer.toString(totalPoints[1]);

		// print results
		showPlayerRow(arrMsgPlayer1);
		showPlayerRow(arrMsgPlayer2);
		HtmlUtil.tableEndHTML();
		return totalPoints;
	}

	/**
	 * Format the results.
	 * 
	 * @param actualScore
	 * @param effectiveScore
	 * @param trailer
	 * @return
	 */
	private static String formatScore(Integer actualScore, Integer effectiveScore, String trailer) {
		return String.format("%d/%d%s", actualScore, effectiveScore, trailer);
	}

	/**
	 * show player html row...
	 * 
	 * @param player
	 */
	private static void showPlayerRow(String[] player) {
		HtmlUtil.tableRowStartHTML();
		for (int i = 0; i < player.length; i++) {
			HtmlUtil.tableColumnHTML(player[i]);
		}
		HtmlUtil.tableRowEndHTML();
	}

	/**
	 * calculate the strokes per hole for each player...
	 * 
	 * @param p1
	 * @param p2
	 * @param s1
	 * @param s2
	 * @param golfCourse
	 * @param strokes
	 */
	public static void calStrokes(Player p1, Player p2, ScoreCards s1, ScoreCards s2, Course golfCourse,
	        int[][] strokes) {

		// Check for number of holes.. 9 or 18
		int startingHole = 1;
		int numHoles = 18;
		double div = 1;
		if (nineHoles == true) {
			div = 2;
			numHoles = 9;
			if (s1.Scores[0] == ScoreCards.INCOMPLETE && s1.Scores[1] == ScoreCards.INCOMPLETE
			        && s1.Scores[2] == ScoreCards.INCOMPLETE && s1.Scores[3] == ScoreCards.INCOMPLETE
			        && s1.Scores[4] == ScoreCards.INCOMPLETE && s1.Scores[5] == ScoreCards.INCOMPLETE
			        && s1.Scores[6] == ScoreCards.INCOMPLETE && s1.Scores[7] == ScoreCards.INCOMPLETE
			        && s1.Scores[8] == ScoreCards.INCOMPLETE) {
				startingHole = 10; // back
			}
		}

		// figure out the handicap
		int hcp1 = RoundIt(
		        HcpUtils.calculateHandicap(golfCourse.getRating(), golfCourse.getSlope(), p1.getHPIndex()) / div);
		int hcp2 = RoundIt(
		        HcpUtils.calculateHandicap(golfCourse.getRating(), golfCourse.getSlope(), p2.getHPIndex()) / div);
		int diffabs = hcp1 - hcp2;
		if (diffabs == 0) {
			Cout.outString("Player same handicap");
			return;
		}

		Hole[] arrStrokeIndex = new Hole[numHoles];
		int startIndex = startingHole - 1;
		for (int h = 0; h < numHoles; h++) {
			Hole currentHole = golfCourse.getCourseInfo().get(startIndex++);
			arrStrokeIndex[h] = currentHole;
		}

		Arrays.sort(arrStrokeIndex); // sort by toughest holes...

		boolean bHPPlayer1 = true;
		if (diffabs < 0) {
			bHPPlayer1 = false;
		}
		diffabs = Math.abs(diffabs); // take the absolute value...

		while (diffabs > 0) {
			for (int h = 0; h < numHoles; h++) {
				Hole currentHole = arrStrokeIndex[h];
				int Num = currentHole.getNo();
				if (bHPPlayer1 == false) {
					strokes[1][Num - 1]++;
				} else {
					strokes[0][Num - 1]++;
				}
				diffabs--;
				if (diffabs == 0) {
					break;
				}
			}
		}

	}

	/**
	 * Double double to integer...
	 * 
	 * @param d
	 * @return
	 */
	public static int RoundIt(double d) {
		// FUDGE FACTOR to increase the HPI to give low HPI more of a chance...
		if (d <= 10.0) {
			d += HMGProperties.percentPool;
		}
		double mul = Math.pow(10.0, 0);
		d = d * mul;
		d = Math.round(d);
		d = d / mul;
		return (int) d;
	}

	/**
	 * find the score card...
	 * 
	 * @param id
	 * @param week
	 * @param listscores
	 * @return
	 */
	public static ScoreCards findScoreCard(int id, int week, List<ScoreCards> listscores) {
		for (ScoreCards card : listscores) {
			if (card.ID == id && card.Week == week) {
				return card;
			}
		}
		return null;
	}

	/**
	 * find player...
	 * 
	 * @param id
	 * @param listPlayer
	 * @return
	 */
	public static Player findPlayer(int id, List<Player> listPlayer) {
		// if (id == hmgProperties.BYE_ID) {
		// return null;
		// }
		for (Player p : listPlayer) {
			if (p.getID() == id) {
				return p;
			}
		}
		return null;
	}

	/**
	 * Find the player based on name.
	 * 
	 * @param name
	 * @param listPlayers
	 * @return
	 */
	public static Optional<Player> findPlayer(String name, List<Player> listPlayers) {
		String upperName = name.toUpperCase();
		for (Player p : listPlayers) {
			if (p.getName().equalsIgnoreCase(upperName)) {
				return Optional.ofNullable(p);
			}
		}
		return Optional.empty();
	}

	/**
	 * Calculate the handicap index...
	 * 
	 * @param play
	 * @param 9    hole scores
	 * @return
	 */
	// public static double calculateHPI(Player play, Course golfCourse, boolean
	// bNine) {
	// double hpi = 0.0;
	//
	// List<Double> Scores = new ArrayList<Double>();
	// Integer[] var = play.getWeeklyScores();
	// int len = 0;
	// for (int mIndex = 0; mIndex < var.length; mIndex++) {
	// Integer nVal = var[mIndex];
	// if (nVal == null || nVal == 0 || nVal == ScoreCards.INCOMPLETE) {
	// continue;
	// }
	// len++;
	// double d = nVal.doubleValue()*2;
	// Scores.add(d);
	// }
	//
	// // Minimum of scores...
	// //testcode(vs) beg
	// if (len < numHPIScores) {
	// double[] tbl = new double[Scores.size()];
	// int index = 0;
	// for (double d: Scores) {
	// double cal = calculate.calculateHandicapIndex(golfCourse.getRating(),
	// golfCourse.getSlope(),
	// d);
	// tbl[index++] = cal;
	// }
	// hpi = calculate.mean(tbl);
	// return hpi;
	// }
	// //testcode(vs) end
	//
	// if (len >= numHPIScores) {
	// int mul = 1;
	// if (bNine == true) {
	// mul = 2;
	// }
	// Integer[] arrScore = new Integer[len];
	// int index = 0;
	// for (Integer i: var) {
	// if (i == ScoreCards.INCOMPLETE) {
	// continue;
	// }
	// arrScore[index++] = i*mul;
	// }
	// Arrays.sort(arrScore);
	// //testcode(vs) 20171027
	// double[] arrTotal = new double[index];
	// int j = 0;
	// for (Integer i: arrScore) {
	// arrTotal[j++] = calculate.calculateHandicapIndex(golfCourse.getRating(),
	// golfCourse.getSlope(),
	// i.doubleValue());
	// }
	// double aveHPI = calculate.mean(arrTotal);
	// //testcode(vs) end
	//
	// // Take the best scores
	// double[] arrBest = new double[numHPIScores];
	// for (int i = 0; i < numHPIScores; i++) {
	// arrBest[i] = new Integer(arrScore[i]).doubleValue();
	// }
	//
	// // calculate HPI
	// double[] arrHPI = new double[numHPIScores];
	// for (int i = 0; i < numHPIScores; i++) {
	// arrHPI[i] = calculate.calculateHandicapIndex(golfCourse.getRating(),
	// golfCourse.getSlope(), arrBest[i]);
	// }
	// double sumOfBest = calculate.mean(arrHPI);
	// hpi = sumOfBest + (aveHPI/200);
	// cout.outString(String.format("%s ave hpi : ", play.getName()) +
	// Double.toString(aveHPI));
	// cout.outString(String.format("%s sum best hpi : ", play.getName()) +
	// Double.toString(sumOfBest));
	// cout.outString(String.format("%s sum hpi : ", play.getName()) +
	// Double.toString(hpi));
	//
	// }
	// return hpi;
	// }

	/**
	 * Set the number of scores for HPI.
	 * 
	 * @param scores
	 */
	public static void setHPIScores(int scores) {
		numHPIScores = scores;
	}

	/**
	 * total weeks.
	 * 
	 * @param wk
	 */
	public static void setTotalWeeks(int wk) {
		totalWeeks = wk;
	}

	/**
	 * Get the total number of weeks.
	 * 
	 * @return
	 */
	public static int getTotalWeeks() {
		return totalWeeks;
	}

	/**
	 * Returns the number of scores to consider for handicaps.
	 * 
	 * @return
	 */
	public static int getNumberHPIScores() {
		return numHPIScores;
	}

	/**
	 * Set 9 hole league...
	 * 
	 * @param nineHole
	 */
	public static void setNineHoles(boolean nineHole) {
		nineHoles = nineHole;
	}

	/**
	 * 
	 */
	public HMGReport() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

}
