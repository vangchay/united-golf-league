/**
 * 
 */
package com.unitedgolfleague;

/**
 * @author e1042631
 *
 */
public class UnitedCup {
	private int matchHolePoints = 4;

	private int matchWinPoints = 4;

	private int matchBonusBirdiePoints = 2;

	private int matchBonusEaglePoints = 4;

	private int matchBonusAlbatrossPoints = 12;

	private int matchOnParPoints = 8;

	private double tourneyPointsTable[] = { 500, 300, 190, 135, 110, 100, 90, 85, 80, 75, 70, 65, 60, 57, 55, 53, 51,
			49, 47, 45, 43, 41, 39, 37, 35.5, 34, 32.5, 31, 29.5, 28, 26.5, 25, 23.5, 22, 21, 20, 19, 18, 17, 16, 15,
			14, 13, 12, 11, 10.5, 10, 9.5, 9, 8.5 };

	/**
	 * @return the matchHolePoints
	 */
	public int getMatchHolePoints() {
		return matchHolePoints;
	}

	/**
	 * @param matchHolePoints the matchHolePoints to set
	 */
	public void setMatchHolePoints(int matchHolePoints) {
		this.matchHolePoints = matchHolePoints;
	}

	/**
	 * @return the matchWinPoints
	 */
	public int getMatchWinPoints() {
		return matchWinPoints;
	}

	/**
	 * @param matchWinPoints the matchWinPoints to set
	 */
	public void setMatchWinPoints(int matchWinPoints) {
		this.matchWinPoints = matchWinPoints;
	}

	/**
	 * @return the matchBonusBirdiePoints
	 */
	public int getMatchBonusBirdiePoints() {
		return matchBonusBirdiePoints;
	}

	/**
	 * @param matchBonusBirdiePoints the matchBonusBirdiePoints to set
	 */
	public void setMatchBonusBirdiePoints(int matchBonusBirdiePoints) {
		this.matchBonusBirdiePoints = matchBonusBirdiePoints;
	}

	/**
	 * @return the matchBonusEaglePoints
	 */
	public int getMatchBonusEaglePoints() {
		return matchBonusEaglePoints;
	}

	/**
	 * @param matchBonusEaglePoints the matchBonusEaglePoints to set
	 */
	public void setMatchBonusEaglePoints(int matchBonusEaglePoints) {
		this.matchBonusEaglePoints = matchBonusEaglePoints;
	}

	/**
	 * @return the matchBonusAlbatrossPoints
	 */
	public int getMatchBonusAlbatrossPoints() {
		return matchBonusAlbatrossPoints;
	}

	/**
	 * @param matchBonusAlbatrossPoints the matchBonusAlbatrossPoints to set
	 */
	public void setMatchBonusAlbatrossPoints(int matchBonusAlbatrossPoints) {
		this.matchBonusAlbatrossPoints = matchBonusAlbatrossPoints;
	}

	/**
	 * @return the matchOnParPoints
	 */
	public int getMatchOnParPoints() {
		return matchOnParPoints;
	}

	/**
	 * @param matchOnParPoints the matchOnParPoints to set
	 */
	public void setMatchOnParPoints(int matchOnParPoints) {
		this.matchOnParPoints = matchOnParPoints;
	}

	/**
	 * @return the tourneyPointsTable
	 */
	public double[] getTourneyPointsTable() {
		return tourneyPointsTable;
	}

	/**
	 * @param tourneyPointsTable the tourneyPointsTable to set
	 */
	public void setTourneyPointsTable(double tourneyPointsTable[]) {
		this.tourneyPointsTable = tourneyPointsTable;
	}

}