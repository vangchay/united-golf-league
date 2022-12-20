package com.ugl.handicap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.ugl.common.Cout;
import com.ugl.speadsheet.PlayersSpreedsheet;

public class SchedUtil {

	public SchedUtil() {

	}

	/**
	 * Generate the schedule...
	 * 
	 * @param nbrWeeks      (Number of weeks in schedule)...
	 * @param players
	 * @param listSchedules
	 * @return
	 */
	public static List<Schedule> generateSchedule(int startWeek, int nbrWeeks, List<Player> players,
	        List<Schedule> listSchedules) {
		List<Schedule> sch = new ArrayList<Schedule>();
		int size = players.size();
		if ((size % 2) == 0) {
		} else {
			size++; // add a dummy
		}

		int half = size / 2;
		int matchTop[] = new int[half];
		int matchBot[] = new int[half];
		int index = 0;
		int playerIndex = 0;
		for (int i = 1; i <= half; i++) {
			matchTop[index] = players.get(playerIndex++).getID();
			matchBot[index] = players.get(playerIndex++).getID();
			index++;
		}

		int endWeek = startWeek + nbrWeeks;
		for (int wk = startWeek; wk < endWeek; wk++) {
			List<Schedule> miniSch = new ArrayList<Schedule>();
			for (int match = 0; match < half; match++) {
				Schedule curSch = new Schedule();
				curSch.Week = wk;
				curSch.ID1 = matchTop[match];
				curSch.ID2 = matchBot[match];
				miniSch.add(curSch);
			}
			checkMatch(miniSch, listSchedules);
			for (Schedule sch2 : miniSch) {
				listSchedules.add(sch2);
				sch.add(sch2);
			}
			rotateClockWise(matchTop, matchBot);
		}

		// checkMatch(sch, listSchedules);

		return sch;
	}

	/**
	 * Check to see if the match has already been schedule?
	 * 
	 * @param theNewSch - new schedule...
	 * @param theOldSch - old schedule...
	 */
	private static void checkMatch(List<Schedule> theNewSch, List<Schedule> theOldSch) {
		if (theOldSch.isEmpty()) {
			return;
		}
		Random rand = new Random();
		boolean bRestart = false;
		int index = 0;
		int size = theNewSch.size();
		int counter = 1000;
		for (; index < size;) {
			Schedule s = theNewSch.get(index);
			if (InSchedule(s, theOldSch) == true) {
				Cout.outString("already scheduled: " + s.toString());
				int Next = index + rand.nextInt(size);
				// WRAPP AROUND
				if (index >= size) {
					bRestart = true;
					Next = 0;
				}
				if (Next >= index) {
					Next++;
					if (Next >= size) {
						Next = 0;
					}
				}
				Schedule oNext = theNewSch.get(Next);
				Schedule.switchMatch(s, oNext);
				// counter to prevent looping?
				if (--counter <= 0) {
					Cout.outString("WARNING - CANNOT SWITCH THIS MATCH: ");
					break; // done.... that's it, we can't find any new matches...
				}
				bRestart = true;
			}
			if (bRestart) {
				index = 0;
				bRestart = false;
			} else {
				index++;
			}
		}
	}

	/**
	 * Check to see if match is already scheduled previously?
	 * 
	 * @param sched
	 * @param prevSchedules
	 * @return
	 */
	static boolean InSchedule(Schedule sched, List<Schedule> prevSchedules) {
		for (Schedule prev : prevSchedules) {
			if (sched.IsEqual(prev)) {
				Cout.outString("Schedule already occured " + sched.toString(PlayersSpreedsheet.ListPlayers));
				return true;
			}
		}
		return false;
	}

	/**
	 * Rotate the schedule clockwise...
	 * 
	 * @param matchTop
	 * @param matchBot
	 */
	private static void rotateClockWise(int[] matchTop, int[] matchBot) {
		int idEnd = matchTop[matchTop.length - 1];
		int idBeg = matchBot[0];
		for (int i = matchTop.length - 2; i > 0; i--) {
			matchTop[i + 1] = matchTop[i];
		}
		for (int i = 1; i < matchBot.length; i++) {
			matchBot[i - 1] = matchBot[i];
		}
		matchTop[1] = idBeg;
		matchBot[matchBot.length - 1] = idEnd;
	}

}
