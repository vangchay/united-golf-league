package com.ugl.handicap;

/**
 * @author e1042631
 *
 */
public class UnitedCupStandings implements Comparable<UnitedCupStandings> {
	public Player thePlayer = null;

	public int points = -1;

	public double hpi = 0;

	public int rank = -1;

	public int lastWeekRank = -1;

	public int TourneyHCP = 0;

	public int TourneyScore = 0;

	public int EffectiveScore = 0;

	public int TourneyPoints = 0;

	public int TotalPoints = 0;

	public static int Sort = 0; // 0 - normal
	                            // 1 - sort by EffectiveScore
	                            // 2 - sort by totol points

	/**
	 * Update the player rank...
	 * 
	 * @param NewRank
	 */
	public void SetRanking(int NewRank) {
		lastWeekRank = rank;
		rank = NewRank;
		hpi = thePlayer.getHPIndex();
		points = thePlayer.getTotalPoints();
	}

	/**
	 * To String...
	 */
	public String toString() {
		String str = String.format("%s, %d, %f, %d, %d, %d, %d, %d, %d, %d", thePlayer.getName(), points, hpi, rank,
		        lastWeekRank, TourneyHCP, TourneyScore, EffectiveScore, TourneyPoints, TotalPoints);
		return str;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

	@Override
	public int compareTo(UnitedCupStandings o) {
		if (Sort == 1) {
			int s1 = EffectiveScore;
			int s2 = o.EffectiveScore;
			if (s1 == s2) {
				double d1 = this.hpi;
				double d2 = o.hpi;
				if (d1 < d2) {
					return 1;
				} else if (d1 == d2) {
					return 0;
				} else {
					return -1;
				}
			}
			return s1 - s2;
		}
		// sort by total points
		else if (Sort == 2) {
			int p1 = this.TotalPoints;
			int p2 = o.TotalPoints;
			if (p1 == p2) {
				double d1 = this.hpi;
				double d2 = o.hpi;
				if (d1 < d2) {
					return 1;
				} else if (d1 == d2) {
					return 0;
				} else {
					return -1;
				}
			}
			return p2 - p1;
		}
		int n1 = thePlayer.getTotalPoints();
		int n2 = o.thePlayer.getTotalPoints();
		if (n1 == n2) {
			double d1 = thePlayer.getHPIndex();
			double d2 = o.thePlayer.getHPIndex();
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

}
