package com.unitedgolfleague;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ugl.common.Cout;
import com.ugl.handicap.ScoreCards;

/**
 * @author e1042631
 *
 */
public class TestScoreCards {
    private static final String TEST_FILE = "C:\\HMG\\test\\scorecards.csv";

    public void test() {
	List<ScoreCards> scoresList = new ArrayList<ScoreCards>();
	ScoreCards.parseScoreCard(TEST_FILE, scoresList);
	for (ScoreCards s : scoresList) {
	    coutScores(s);
	}
    }

    private void coutScores(ScoreCards s) {
	String msg = String.format("ID=%d, Name=%s, Total=%d, Week=%d", s.ID, s.Name, s.Total, s.Week);
	Cout.outString(msg);
	msg = "";
	for (Integer score : s.Scores) {
	    msg = msg + String.format("s=%d, ", score);
	}
	Cout.outString(msg);
    }

    public void test2() {
	ScoreCards score = new ScoreCards();
	score.ID = 3;
	score.Name = "chay";
	score.Week = 1;
	int index = 0;
	for (int count = 1; count <= 18; count++) {
	    score.Scores[index++] = count;
	}
	score.updateTotal();
	coutScores(score);
	List<ScoreCards> scoresList = new ArrayList<ScoreCards>();
	scoresList.add(score);
	score = new ScoreCards();
	score.ID = 1;
	score.Name = "lily";
	score.Week = 1;
	scoresList.add(score);
	score = new ScoreCards();
	score.ID = 2;
	score.Name = "boy";
	score.Week = 1;
	scoresList.add(score);
	Collections.sort(scoresList);
	for (ScoreCards s : scoresList) {
	    coutScores(s);
	}
    }

}
