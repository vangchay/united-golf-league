package com.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.ugl.common.Cout;
import com.ugl.handicap.Player;
import com.ugl.handicap.SchedUtil;
import com.ugl.handicap.Schedule;

public class jtestSchedule {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testSwitchMatch() {
		// fail("Not yet implemented");
		Schedule sch1 = new Schedule();
		Schedule sch2 = new Schedule();
		sch1.ID1 = 1;
		sch1.ID2 = 2;
		sch1.Week = 1;
		sch2.ID1 = 3;
		sch2.ID2 = 4;
		sch2.Week = 1;
		Cout.outString(sch1.toString());
		Cout.outString(sch2.toString());
		Schedule.switchMatch(sch1, sch2);
		Cout.outString(sch1.toString());
		Cout.outString(sch2.toString());
	}

	@Test
	public void testUpdateSchedule() {
		// fail("Not yet implemented");
		List<Schedule> lst = new ArrayList<Schedule>();
		List<Player> players = new ArrayList<Player>();
		Player.parsePlayers("C:\\HMG\\test\\players.csv", players);
		Schedule.parseSchedule("C:\\HMG\\test\\schedule.csv", lst);
		SchedUtil.generateSchedule(1, 13, players, lst);
		Cout.outString("new schedule");
		for (Schedule l : lst) {
			Cout.outString(l.toString());
		}
	}

	@Test
	public void testParseSchedule() {
		// fail("Not yet implemented");
	}

}