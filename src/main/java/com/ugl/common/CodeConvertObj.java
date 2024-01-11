/**
 * 
 */
package com.ugl.common;

/**
 * @author e1042631
 *
 */
public class CodeConvertObj {
    public boolean bBase64Data = false;

    /**
     * The y offset...
     */
    public int yOffset = 0;

    /**
     * Ignore text...
     */
    public String ignoreText = "";

    /**
     * START text...
     */
    public String startText = null;

    /**
     * REPLACE text array...
     */
    public String[] replaceTextTbl = null;

    /**
     * Convert data to string...
     */
    public String toString() {
	String msg = String.format("START, %s, %d, %s", startText, yOffset, ignoreText, (bBase64Data ? "1" : "0"));
	for (String s : replaceTextTbl) {
	    msg = msg + "\n" + String.format("REPLACE, %s", s);
	}
	msg += "\nEND";
	return msg;
    }
}