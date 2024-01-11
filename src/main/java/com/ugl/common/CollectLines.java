/**
 * 
 */
package com.ugl.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * @author e1042631
 *
 */
public class CollectLines {
    public Vector<List<String>> vectLines = new Vector<List<String>>();

    public static CollectLines parseJTbl(List<String> InputLines, String open, String close) {
	CollectLines collect = new CollectLines();
	List<String> ListLines = null;
	int state = 0;
	for (String line : InputLines) {
	    line = line.trim();
	    if (line.isEmpty()) {
		continue;
	    }
	    switch (state) {
	    case 0: // look for {
		if (line.equals(open)) {
		    ListLines = new ArrayList<String>();
		    state++;
		}
		break;
	    case 1: // collect lines until }
		if (line.substring(0, 1).equals(close)) {
		    state = 0;
		    collect.vectLines.add(ListLines);
		} else {
		    ListLines.add(line);
		}
		break;
	    default:
		break;
	    }
	}
	return collect;
    }
}