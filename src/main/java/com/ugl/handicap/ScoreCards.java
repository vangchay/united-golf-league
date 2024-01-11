package com.ugl.handicap;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.apache.commons.io.FileUtils;

import com.ugl.common.Utils;
import com.unitedgolfleague.HMGProperties;

public class ScoreCards implements Comparable<ScoreCards> {
    public final static int INCOMPLETE = 0;

    public final static String INC = "-";

    public final static String HEADER = "****ID,NAME,WEEK,H1,H2,H3,H4,H5,H6,H7,H8,H9,H10,H11,H12,H13,H14,H15,H16,H17,H18,TOTAL";

    public int ID = -1;

    public int Week = 0;

    public boolean bIncompleteScore = false;

    public String Name = "";

    public Integer Scores[] = null;

    public Integer Total = new Integer(0);

    public String toString() {
	String out = String.format("%d, %s, %d", ID, Name, Week);
	for (int i = 0; i < Scores.length; i++) {
	    out = out + ", " + Integer.toString(Scores[i]);
	}
	out += ", " + Integer.toString(Total);
	return out;
    }

    public void updateTotal() {
	// check to see if we have a complete scorecard?
	this.bIncompleteScore = checkBadScores(Scores);
	if (bIncompleteScore == true) {
	    return;
	}

	Total = 0;
	for (int i = 0; i < 18; i++) {
	    Integer s = Scores[i];
	    if (s == null) {
		continue;
	    }
	    Total += s;
	}
    }

    public ScoreCards() {
	Scores = new Integer[18];
	for (int i = 0; i < Scores.length; i++) {
	    Scores[i] = new Integer(0);
	}
    }

    @Override
    public int compareTo(ScoreCards o) {
	return this.ID - o.ID;
    }

    /**
     * Update scorecard file.
     * 
     * @param scoreCardFileName
     * @param scoresList
     */
    public static void updateScoreCard(String scoreCardFileName, List<ScoreCards> scoresList) {
	Collections.sort(scoresList);
	String backup = scoreCardFileName + ".bak";
	try {
	    FileUtils.copyFile(new File(scoreCardFileName), new File(backup));
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	FileUtils.deleteQuietly(new File(scoreCardFileName));
	List<String> lines = new ArrayList<String>();
	lines.add(HEADER);
	for (ScoreCards s : scoresList) {
	    lines.add(s.toString());
	}
	try {
	    FileUtils.writeLines(new File(scoreCardFileName), lines);
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    /**
     * parse the score card...
     * 
     * @param scoreCardFile
     */
    public static void parseScoreCard(String scoreCardFile, List<ScoreCards> scoresList) {
	List<String> lines = null;
	try {
	    lines = FileUtils.readLines(new File(scoreCardFile), "UTF-8");
	} catch (IOException e) {
	    e.printStackTrace();
	    return;
	}

	for (String s : lines) {
	    if (s.isEmpty()) {
		continue;
	    }
	    int len = s.length();
	    if (len < 4) {
		continue;
	    }
	    String comment = s.substring(0, 4);
	    if (comment.contentEquals("****")) {
		continue;
	    }
	    Vector<String> rec = Utils.parseRec(s);
	    int size = rec.size();
	    if (size < 3) {
		continue;
	    }

	    int index = 0;
	    String ID = rec.get(index++).trim();
	    String name = rec.get(index++).trim();
	    String week = rec.get(index++).trim();
	    Integer myScore[] = new Integer[18];
	    for (int i = 0; i < myScore.length; i++) {
		myScore[i] = new Integer(0);
	    }
	    // DEBUG
	    // cout.outString(String.format("%s %s %s", ID, name, week));

	    int hole = 0;
	    for (; index < size; index++) {
		String Val = rec.get(index).trim();
		if (Val.isEmpty()) {
		    myScore[hole] = new Integer(ScoreCards.INCOMPLETE);
		    if (++hole == 18) {
			break;
		    }
		    continue;
		}
		myScore[hole] = new Integer(Integer.parseInt(Val));
		// DEBUG
		// cout.outString(String.format(" %d %d", hole+1, myScore[hole]));
		if (++hole == 18) {
		    break;
		}
	    }

	    ScoreCards myCard = new ScoreCards();
	    myCard.Name = name;
	    myCard.ID = Integer.parseInt(ID);
	    myCard.Week = Integer.parseInt(week);
	    myCard.Scores = myScore;
	    myCard.bIncompleteScore = checkBadScores(myScore);
	    if (myCard.bIncompleteScore == false) {
		myCard.updateTotal();
	    }
	    scoresList.add(myCard);
	}
    }

    /**
     * Check scores...
     * 
     * @param myScore
     * @return true bad score / false ok
     */
    private static boolean checkBadScores(Integer[] myScore) {
	// front
	if (myScore[0] != ScoreCards.INCOMPLETE && myScore[1] != ScoreCards.INCOMPLETE
		&& myScore[2] != ScoreCards.INCOMPLETE && myScore[3] != ScoreCards.INCOMPLETE
		&& myScore[4] != ScoreCards.INCOMPLETE && myScore[5] != ScoreCards.INCOMPLETE
		&& myScore[6] != ScoreCards.INCOMPLETE && myScore[7] != ScoreCards.INCOMPLETE
		&& myScore[8] != ScoreCards.INCOMPLETE && myScore[9] == ScoreCards.INCOMPLETE
		&& myScore[10] == ScoreCards.INCOMPLETE && myScore[11] == ScoreCards.INCOMPLETE
		&& myScore[12] == ScoreCards.INCOMPLETE && myScore[13] == ScoreCards.INCOMPLETE
		&& myScore[14] == ScoreCards.INCOMPLETE && myScore[15] == ScoreCards.INCOMPLETE
		&& myScore[16] == ScoreCards.INCOMPLETE && myScore[17] == ScoreCards.INCOMPLETE) {
	    return false;
	}
	// back
	else if (myScore[0] == ScoreCards.INCOMPLETE && myScore[1] == ScoreCards.INCOMPLETE
		&& myScore[2] == ScoreCards.INCOMPLETE && myScore[3] == ScoreCards.INCOMPLETE
		&& myScore[4] == ScoreCards.INCOMPLETE && myScore[5] == ScoreCards.INCOMPLETE
		&& myScore[6] == ScoreCards.INCOMPLETE && myScore[7] == ScoreCards.INCOMPLETE
		&& myScore[8] == ScoreCards.INCOMPLETE && myScore[9] != ScoreCards.INCOMPLETE
		&& myScore[10] != ScoreCards.INCOMPLETE && myScore[11] != ScoreCards.INCOMPLETE
		&& myScore[12] != ScoreCards.INCOMPLETE && myScore[13] != ScoreCards.INCOMPLETE
		&& myScore[14] != ScoreCards.INCOMPLETE && myScore[15] != ScoreCards.INCOMPLETE
		&& myScore[16] != ScoreCards.INCOMPLETE && myScore[17] != ScoreCards.INCOMPLETE) {
	    return false;
	}
	// 18
	else if (myScore[0] != ScoreCards.INCOMPLETE && myScore[1] != ScoreCards.INCOMPLETE
		&& myScore[2] != ScoreCards.INCOMPLETE && myScore[3] != ScoreCards.INCOMPLETE
		&& myScore[4] != ScoreCards.INCOMPLETE && myScore[5] != ScoreCards.INCOMPLETE
		&& myScore[6] != ScoreCards.INCOMPLETE && myScore[7] != ScoreCards.INCOMPLETE
		&& myScore[8] != ScoreCards.INCOMPLETE && myScore[9] != ScoreCards.INCOMPLETE
		&& myScore[10] != ScoreCards.INCOMPLETE && myScore[11] != ScoreCards.INCOMPLETE
		&& myScore[12] != ScoreCards.INCOMPLETE && myScore[13] != ScoreCards.INCOMPLETE
		&& myScore[14] != ScoreCards.INCOMPLETE && myScore[15] != ScoreCards.INCOMPLETE
		&& myScore[16] != ScoreCards.INCOMPLETE && myScore[17] != ScoreCards.INCOMPLETE) {
	    return false;
	}
	return true;
    }

    public static ScoreCards GenHCPScore(Player player, Course golfCourse, ScoreCards score) {

	// Check for number of holes.. 9 or 18
	int startingHole = 1;
	int numHoles = 18;
	double div = 1;
	if (HMGProperties.numberHoles == 9) {
	    div = 2;
	    numHoles = 9;
	    if (score.Scores[0] == ScoreCards.INCOMPLETE && score.Scores[1] == ScoreCards.INCOMPLETE
		    && score.Scores[2] == ScoreCards.INCOMPLETE && score.Scores[3] == ScoreCards.INCOMPLETE
		    && score.Scores[4] == ScoreCards.INCOMPLETE && score.Scores[5] == ScoreCards.INCOMPLETE
		    && score.Scores[6] == ScoreCards.INCOMPLETE && score.Scores[7] == ScoreCards.INCOMPLETE
		    && score.Scores[8] == ScoreCards.INCOMPLETE) {
		startingHole = 10; // back
	    }
	}
	double hpi = player.getHPIndex();
	int hcp = HMGReport
		.RoundIt(HcpUtils.calculateHandicap(golfCourse.getRating(), golfCourse.getSlope(), hpi) / div);
	Hole[] arrStrokeIndex = new Hole[numHoles];
	int startIndex = startingHole - 1;
	for (int h = 0; h < numHoles; h++) {
	    Hole currentHole = golfCourse.getCourseInfo().get(startIndex++);
	    arrStrokeIndex[h] = currentHole;
	}

	Arrays.sort(arrStrokeIndex); // sort by toughest holes...

	int strokes[] = new int[18];
	while (hcp > 0) {
	    for (int h = 0; h < numHoles; h++) {
		Hole currentHole = arrStrokeIndex[h];
		int Num = currentHole.getNo();
		strokes[Num - 1]++;
		hcp--;
		if (hcp == 0) {
		    break;
		}
	    }
	}

	ScoreCards out = new ScoreCards();
	int index = 0;
	List<Hole> courseInfo = golfCourse.getCourseInfo();
	for (Hole h : courseInfo) {
	    int nScore = h.getPar() + strokes[index];
	    out.Scores[index] = new Integer(nScore);
	    index++;
	}

	index = 0;
	for (Integer s : score.Scores) {
	    if (s.intValue() > 0) {
	    } else {
		out.Scores[index] = ScoreCards.INCOMPLETE;
	    }
	    index++;
	}
	return out;
    }

}