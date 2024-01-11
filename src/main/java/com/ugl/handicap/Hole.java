package com.ugl.handicap;

public class Hole implements Comparable<Hole> {
    /**
     * The hole number 1-18.
     */
    private int holeNumber;

    /**
     * Score to par 3, 4 or 5.
     */
    private int par;

    /**
     * Total yards of the hole.
     */
    private int yards;

    /**
     * The stroke index.
     */
    private int strokeIndex;

    /**
     * The score to par.
     */
    private int score;

    /**
     * The number of puts.
     */
    private int putts;

    public Hole(int Number, int Par, int Yardage, int StrokeIndex) {
	holeNumber = Number;
	par = Par;
	yards = Yardage;
	strokeIndex = StrokeIndex;
    }

    /**
     * to String.
     */
    public String toString() {
	String msg = String.format("[hole=%d, par=%d, yards=%d, stroke index=%d]", holeNumber, par, yards, strokeIndex);
	return msg;
    }

    /**
     * Compare...
     */
    public int compareTo(Hole o) {
	return strokeIndex - o.getStrokeIndex();
    }

    /**
     * @return the score
     */
    public int getScore() {
	return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(int score) {
	this.score = score;
    }

    /**
     * @return the puts
     */
    public int getPutts() {
	return putts;
    }

    /**
     * @param putts the putts to set
     */
    public void setPutts(int putts) {
	this.putts = putts;
    }

    /**
     * @return the no
     */
    public int getNo() {
	return holeNumber;
    }

    /**
     * @return the par
     */
    public int getPar() {
	return par;
    }

    /**
     * @return the yards
     */
    public int getYards() {
	return yards;
    }

    /**
     * @return the strokeIndex
     */
    public int getStrokeIndex() {
	return strokeIndex;
    }
}
