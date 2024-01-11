package com.ugl.handicap;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.apache.commons.io.FileUtils;

import com.ugl.common.Utils;
import com.unitedgolfleague.HMGProperties;

public class Schedule implements Comparable<Schedule> {
    private static final String HEADER = "****WEEK,ID1,ID2";

    public int Week;

    public int ID1;

    public int ID2;

    public String toString() {
	return String.format("%d, %d, %d", Week, ID1, ID2);
    }

    public String toString(List<Player> playerLst) {
	Player p = HMGReport.findPlayer(ID1, playerLst);
	String name;
	if (p.getID() == HMGProperties.fakePlayerIdForBye) {
	    name = "HCP";
	} else {
	    name = p.getName();
	}
	p = HMGReport.findPlayer(ID2, playerLst);
	String name2;
	if (p.getID() == HMGProperties.fakePlayerIdForBye) {
	    name2 = "HCP";
	} else {
	    name2 = p.getName();
	}
	return String.format("Week %d Match: %s vs %s", Week, name, name2);
    }

    public Schedule() {
	// TODO Auto-generated constructor stub
    }

    /**
     * Switch Match...
     * 
     * @param sch1
     * @param sch2
     */
    public static void switchMatch(Schedule sch1, Schedule sch2) {

	int n1, n2, n3, n4;

	n1 = sch1.ID1;
	n2 = sch1.ID2;
	n3 = sch2.ID1;
	n4 = sch2.ID2;
	int tmp = n1;
	n1 = n2;
	n2 = n3;
	n3 = n4;
	n4 = tmp;

	sch1.ID1 = n1;
	sch1.ID2 = n2;
	sch2.ID1 = n3;
	sch2.ID2 = n4;
    }

    /**
     * Match is the same?
     * 
     * @param sch
     * @return
     */
    public boolean IsEqual(Schedule sch) {
	if (ID1 == sch.ID1 && ID2 == sch.ID2) {
	    return true;
	} else if (ID2 == sch.ID1 && ID1 == sch.ID2) {
	    return true;
	}
	return false;
    }

    @Override
    public int compareTo(Schedule o) {
	return this.Week - o.Week;
    }

    /**
     * Update the schedule.
     * 
     * @param fileName
     * @param matchSchedule
     */
    public static void updateSchedule(String fileName, List<Schedule> matchSchedule) {
	Collections.sort(matchSchedule);
	String backup = fileName + ".bak";
	try {
	    FileUtils.copyFile(new File(fileName), new File(backup));
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	FileUtils.deleteQuietly(new File(fileName));
	List<String> lines = new ArrayList<String>();
	lines.add(HEADER);
	for (Schedule s : matchSchedule) {
	    lines.add(s.toString());
	}
	try {
	    FileUtils.writeLines(new File(fileName), lines);
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    /**
     * parse the schedule...
     * 
     * @param fileName
     * @param matchSchedule
     */
    public static void parseSchedule(String fileName, List<Schedule> matchSchedule) {
	List<String> lines = null;
	try {
	    lines = FileUtils.readLines(new File(fileName), "UTF-8");
	} catch (IOException e) {
	    e.printStackTrace();
	    return;
	}

	for (String s : lines) {
	    if (s.contains("****WEEK")) {
		continue;
	    }

	    if (s.isEmpty() == false) {
		Vector<String> rec = Utils.parseRec(s);
		if (rec.size() < 3) {
		    continue;
		}
		int Week = 0;
		int ID1 = 0;
		int ID2 = 0;
		int index = 0;
		Week = Integer.parseInt(rec.get(index++).trim());
		ID1 = Integer.parseInt(rec.get(index++).trim());
		ID2 = Integer.parseInt(rec.get(index++).trim());
		Schedule obj = new Schedule();
		obj.Week = Week;
		obj.ID1 = ID1;
		obj.ID2 = ID2;
		matchSchedule.add(obj);
	    }
	}
    }

}
