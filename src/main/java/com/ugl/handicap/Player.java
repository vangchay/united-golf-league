package com.ugl.handicap;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.apache.commons.io.FileUtils;

import com.ugl.common.Cout;
import com.ugl.common.Utils;
import com.unitedgolfleague.HMGProperties;

/**
 * @author e1042631
 *
 */
public class Player implements Comparable<Player> {
	private int id = -1;

	private String playerName = "";

	private String playerEmail = "";

	private String phoneNumber = "";

	private double hcpIndexInitial = 0.0;

	private double hcpIndex = 0.0;

	private int playerTotalPoints = 0;

	private String lastUpdateDate = "";

	private Integer[] weeklyScores = null;

	private double averageScore = 0.0;

	private double scoreStandardDeviation = 0.0;

	private int calculatedGoodScores = 0;

	// public static
	public final static int SORT_ID = 0;

	public final static int SORT_HPI = 1;

	public final static int SORT_UNITED_CUP = 2;

	public static int nSortBy = SORT_UNITED_CUP;

	Player clone(Player o) {
		Player c = new Player();
		c.setID(o.getID());
		c.setName("**Yourself " + o.getName());
		c.setEmail(o.getEmail());
		c.setPhoneNumber(o.getPhoneNumber());
		c.setHPIndexInitial(o.getHPIndexInitial());
		c.setHPIndex(o.getHPIndex());
		c.setTotalPoints(o.getTotalPoints());
		c.setLastUpdate(o.getLastUpdate());
		return c;
	}

	/**
	 * 
	 */
	public Player() {
		weeklyScores = new Integer[HMGReport.getTotalWeeks()];
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * Set phone number...
	 * 
	 * @param phone
	 */
	public void setPhoneNumber(String phone) {
		phoneNumber = phone;
	}

	/**
	 * @return the lastUpdate
	 */
	public String getLastUpdate() {
		return lastUpdateDate;
	}

	/**
	 * @param lastUpdate the lastUpdate to set
	 */
	public void setLastUpdate(String lastUpdate) {
		lastUpdateDate = lastUpdate;
	}

	/**
	 * @return the iD
	 */
	public int getID() {
		return id;
	}

	/**
	 * @param iD the iD to set
	 */
	public void setID(int iD) {
		id = iD;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return playerName;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		playerName = name;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return playerEmail;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		playerEmail = email;
	}

	/**
	 * @return the hPIndexInitial
	 */
	public double getHPIndexInitial() {
		return hcpIndexInitial;
	}

	/**
	 * @param hPIndexInitial the hPIndexInitial to set
	 */
	public void setHPIndexInitial(double hPIndexInitial) {
		hcpIndexInitial = hPIndexInitial;
	}

	/**
	 * @return the hPIndex
	 */
	public double getHPIndex() {
		return hcpIndex;
	}

	/**
	 * @param hPIndex the hPIndex to set
	 */
	public void setHPIndex(double hPIndex) {
		hcpIndex = hPIndex;
	}

	/**
	 * @return the totalPoints
	 */
	public int getTotalPoints() {
		return playerTotalPoints;
	}

	/**
	 * @param points the totalPoints to set
	 */
	public void setTotalPoints(int points) {
		playerTotalPoints = points;
	}

	/**
	 * @return the weeklyScores
	 */
	public Integer[] getWeeklyScores() {
		return weeklyScores;
	}

	public double getScoreSTD() {
		return scoreStandardDeviation;
	}

	public double getAverageScore() {
		return averageScore;
	}

	/**
	 * Calculate the scores.
	 * 
	 * @param bNineHole - true (9 hole)/false (18 hole) scores
	 * @param theCourse - the golf course.
	 */
	public void calculateScores(boolean bNineHole, Course theCourse) {
		averageScore = 0.0;

		if (numGoodScores() == 0) {
			return;
		}

		double[] arrScores = new double[calculatedGoodScores];
		double[] arrHPI = new double[calculatedGoodScores];
		int index = 0;
		double mul = 1.0;
		if (bNineHole) {
			mul = 2.0;
		}

		// 18 hole scores...
		for (int weekIndex = 0; weekIndex < weeklyScores.length; weekIndex++) {
			if (weeklyScores[weekIndex] == null || weeklyScores[weekIndex] == ScoreCards.INCOMPLETE) {
				continue;
			}
			Integer i = weeklyScores[weekIndex];
			arrScores[index++] = i.doubleValue() * mul;
		}

		// Sort the scores low to high
		Arrays.sort(arrScores);

		// Calculate averages...
		averageScore = HcpUtils.mean(arrScores);
		double aveHpi = HcpUtils.calculateHandicapIndex(theCourse.getRating(), theCourse.getSlope(), averageScore);
		scoreStandardDeviation = HcpUtils.standardDeviation(arrScores);

		// Calculate the handicaps...
		final int HPI_SAMPLE = HMGProperties.HPISample;

		if (HMGReport.UpdatePlayerHPIMode == false
		        && (HMGReport.CurrentWeek > 0 && HMGReport.CurrentWeek <= HPI_SAMPLE)) {
			return;
		}

		// if we don't have enough scores, just calculate what we have...
		if (calculatedGoodScores <= HPI_SAMPLE && HMGReport.CurrentWeek <= HPI_SAMPLE) {
			for (int i = 0; i < calculatedGoodScores; i++) {
				arrHPI[i] = HcpUtils.calculateHandicapIndex(theCourse.getRating(), theCourse.getSlope(), arrScores[i]);
			}
			hcpIndex = HcpUtils.mean(arrHPI);
		}
		// only take the necessary samples...
		else {
			int arrSize = HPI_SAMPLE;
			if (calculatedGoodScores < HPI_SAMPLE) {
				arrSize = calculatedGoodScores;
			}
			double[] arrMean = new double[arrSize];
			for (int i = 0; i < arrSize; i++) {
				arrMean[i] = HcpUtils.calculateHandicapIndex(theCourse.getRating(), theCourse.getSlope(),
				        arrScores[i]);
			}
			hcpIndex = HcpUtils.mean(arrMean);
			double sumOfBest = hcpIndex;
			// Use your AVE HPI score in the calculation... so two PPL will never get the
			// same score...
			double fraction = 1.0 - hcpIndex / aveHpi;
			hcpIndex += fraction;
			Cout.outString(String.format("%s ave hpi : ", this.getName()) + Double.toString(aveHpi));
			Cout.outString(String.format("%s sum best hpi : ", this.getName()) + Double.toString(sumOfBest));
			Cout.outString(String.format("%s hpi : ", this.getName()) + Double.toString(hcpIndex));
		}
	}

	/**
	 * Print the ranking...
	 * 
	 * @return
	 */
	public String toUnitCupString() {
		String msg = String.format("%s, %d, %f", playerName, playerTotalPoints, hcpIndex);
		return msg;
	}

	/**
	 * Convert to string.
	 */
	public String toString() {
		String msg = String.format(
		        "name=%s, ave=%f, email=%s, hpi=%f, hpi(init)=%f, id=%d, lastdate=%s, phone=%s, std=%f, pts=%d",
		        playerName, averageScore, playerEmail, hcpIndex, hcpIndexInitial, id, lastUpdateDate, phoneNumber,
		        scoreStandardDeviation, playerTotalPoints);
		for (Integer var : weeklyScores) {
			if (var == null) {
				continue;
			}
			msg = msg + ", " + var.toString();
		}
		return msg;
	}

	/**
	 * Generate the player entry field.
	 * 
	 * @return
	 */
	public String[] generateCSVLine(Course theCourse) {
		String[] line = new String[50];
		int index = 0;
		// ID
		line[index++] = Integer.toString(id);
		// name
		line[index++] = playerName;
		// email
		line[index++] = playerEmail;
		// phone
		line[index++] = phoneNumber;
		// 1st hpi
		line[index++] = String.format("%.2f", hcpIndexInitial);
		// hpi
		line[index++] = String.format("%.2f", hcpIndex);
		// diff
		// line[index++] = String.format("%.2f", HPIndexDiff);
		// 18 hp
		line[index++] = String.format("%d",
		        HMGReport.RoundIt(HcpUtils.calculateHandicap(theCourse.getRating(), theCourse.getSlope(), hcpIndex)));
		// 9 hp
		line[index++] = String.format("%d", HMGReport
		        .RoundIt(HcpUtils.calculateHandicap(theCourse.getRating(), theCourse.getSlope(), hcpIndex) / 2));
		// points
		line[index++] = Integer.toString(playerTotalPoints);

		for (int count = 0; count < weeklyScores.length;) {
			Integer i = weeklyScores[count++];
			if (i == null || i == ScoreCards.INCOMPLETE) {
				line[index++] = "";
			} else {
				line[index++] = i.toString();
			}
		}

		// insert the AVE and STD...
		line[index++] = String.format("%.2f", averageScore);
		line[index++] = String.format("%.2f", scoreStandardDeviation);
		line[index] = null;
		return line;
	}

	/**
	 * Update the score with the score card info...
	 * 
	 * @param currentCard
	 * @param week
	 */
	public boolean UpdateScore(ScoreCards currentCard, int week) {
		if (week == 0 || currentCard.bIncompleteScore == true) {
			return false;
		}

		int sum = 0;
		for (Integer i : currentCard.Scores) {
			if (i == null) {
				continue;
			}
			sum += i;
		}
		int num = weeklyScores.length;
		int index = week - 1;
		if (week <= num) {
			weeklyScores[index] = sum;
		}
		return true;
	}

	@Override
	public int compareTo(Player o) {
		double hpi1 = this.getHPIndex();
		double hpi2 = o.getHPIndex();
		if (nSortBy == SORT_HPI) {
			if (hpi1 != 0.0 && hpi2 != 0.0) {
				return (int) ((100 * hpi1) - (100 * hpi2));
			}
		} else if (nSortBy == SORT_UNITED_CUP) {
			int n1 = this.getTotalPoints();
			int n2 = o.getTotalPoints();
			if (n1 == n2) {
				double d1 = this.getHPIndex();
				double d2 = o.getHPIndex();
				if (d1 < d2) {
					return 1;
				} else if (d1 == d2) {
					return 0;
				} else {
					return -1;
				}
			}
			return n2 - n1;
		}
		return this.getID() - o.getID();
	}

	/**
	 * Parse the player file.
	 * 
	 * @param playerFileName
	 * @param listOfPlayers
	 * @param numWeeks
	 */
	public static void parsePlayers(String playerFileName, List<Player> listOfPlayers) {
		List<String> lines = null;
		try {
			lines = FileUtils.readLines(new File(playerFileName), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		int numWeeks = 0;
		// find out the number of weeks (the 1st line of the players file define it!
		String firstLine = lines.get(0);
		String[] fields = Utils.parseRecString(firstLine);
		int recordSize = fields.length;

		for (String theLine : fields) {
			if (theLine.contains("WK")) {
				numWeeks++;
			}
		}

		HMGReport.setTotalWeeks(numWeeks);

		for (String s : lines) {
			if (s.isEmpty() == false) {
				int len = s.length();
				if (len < 4) {
					continue;
				} else {
					String comment = s.substring(0, 4);
					if (comment.contentEquals("****")) {
						continue;
					}

					Vector<String> rec = Utils.parseRec(s);
					int len2 = rec.size();
					if (len2 < recordSize) {
						continue;
					}

					int index = 0;
					String ID = rec.get(index++).trim();
					String Name = rec.get(index++).trim();
					String email = rec.get(index++).trim();
					String phone = rec.get(index++).trim();
					Player playerObj = new Player();
					playerObj.setName(Name);
					playerObj.setEmail(email);
					playerObj.setID(Integer.parseInt(ID));
					playerObj.setPhoneNumber(phone);
					String dbl = rec.get(index++).trim();
					double d;
					if (dbl.isEmpty() == false) {
						d = Double.parseDouble(dbl);
						playerObj.setHPIndexInitial(d);
					}
					dbl = rec.get(index++).trim();
					if (dbl.isEmpty() == false) {
						d = Double.parseDouble(dbl);
						playerObj.setHPIndex(d);
					}

					index++; // 18hp
					index++; // 9hp

					String tot = rec.get(index++).trim();
					if (tot.isEmpty() == false) {
						int total = Integer.parseInt(tot);
						playerObj.setTotalPoints(total);
					}

					Integer[] score = playerObj.getWeeklyScores();
					for (int i = 0; i < numWeeks; i++) {
						String tmp = rec.get(index++).trim();
						if (tmp.isEmpty()) {
							score[i] = ScoreCards.INCOMPLETE;
							continue;
						}
						Integer weekScore = new Integer(tmp);
						score[i] = weekScore;
					}
					listOfPlayers.add(playerObj);
				}
			}
		}
	}

	/**
	 * Calculate the number of good scores.
	 * 
	 * @return
	 */
	private int numGoodScores() {
		calculatedGoodScores = 0;
		for (int i = 0; i < weeklyScores.length; i++) {
			if (weeklyScores[i] == null || weeklyScores[i] == ScoreCards.INCOMPLETE) {
				continue;
			}
			calculatedGoodScores++;
		}
		return calculatedGoodScores;
	}

	/**
	 * Override equals...
	 * 
	 * @param playerObj
	 * @return
	 */
	public boolean equals(Player playerObj) {
		if (id == playerObj.getID() && playerName.equalsIgnoreCase(playerObj.getName())) {
			return true;
		} else {
			return false;
		}
	}
}
