package com.ugl.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author e1042631
 *
 */
public class Cout {
	static protected Logger log = LogManager.getLogger(Cout.class);

	private final static String CR = "\n";

	/**
	 * 
	 */
	public Cout() {
	}

	/**
	 * Print standard out
	 * 
	 * @param s
	 */
	public static void outString(String s) {
		if (log != null) {
			log.info(s);
		}
		System.out.print(s + CR);
	}
}
