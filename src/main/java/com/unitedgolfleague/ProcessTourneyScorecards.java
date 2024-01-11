/**
 * 
 */
package com.unitedgolfleague;

import com.ugl.common.Cout;

/**
 * @author e1042631
 *
 */
public class ProcessTourneyScorecards {
    private final static String CLASS_NAME = "ProcessTourneyScorecards.";

    private final static String MAIN = CLASS_NAME + "main() ";

    private final static String ARG1 = MAIN + "ARG1: players properties file";

    private final static String ARG2 = MAIN + "ARG4: COURSE NAME";

    private final static String ARG3 = MAIN + "ARG5: COURSE TEE";

    private static String playersFileName = null;

    private static String courseFileName = null;

    private static String scorecardFileName = null;

    private static String courseName = null;

    private static String couresTee = null;

    /**
     * @param args
     */
    public static void main(String[] args) {
	Cout.outString(MAIN);
	Cout.outString(ARG1);
	Cout.outString(ARG2);
	Cout.outString(ARG3);

	if (args.length < 5) {
	    Cout.outString("Not enough parameters.");
	} else {
	    int index = 0;
	    String propsFileName = args[index++];
	    HMGProperties.load(propsFileName);

	    playersFileName = args[index++];
	    courseFileName = args[index++];
	    scorecardFileName = args[index++];
	    courseName = args[index++];
	    couresTee = args[index++];
	}
	Cout.outString("Bye!");

    }

}
