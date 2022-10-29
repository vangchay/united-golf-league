package com.ugl.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
//import org.eclipse.swt.widgets.*;

/**
 * 
 */

/**
 * @author e1042631
 *
 */
public class UtilityTest {
	private final static String version = "1.0.0";

	private final static String className = "UtilityTest.";

	private final static String getVersionMethod = className + "getVersion() ";

	/*
	 * static { System.loadLibrary("UtilityTest"); }
	 */

	static void getVersion() {
		System.out.println(getVersionMethod + version);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		getVersion();
		String[] strings = new String[3];

		strings[0] = "eXamPLestring>1.67>>ReSTOfString";
		strings[1] = "eXamPLestring>0.57>>ReSTOfString";
		strings[2] = "C:\\Program Files (x86)\\FIS\\CSF Designer Engine 13.0";

		Pattern pattern = Pattern.compile("[-+]?[0-9]*\\.[0-9]+");

		for (String string : strings) {
			Matcher matcher = pattern.matcher(string);
			while (matcher.find()) {
				System.out.println("# float value: " + matcher.group());
			}
		}
		MyApp();
	}

	public static void MyApp() {
		/**
		 * Display display = new Display(); Shell shell = new Shell(display);
		 * shell.setText("Hello World"); shell.open(); while (!shell.isDisposed()) { if
		 * (!display.isDisposed()) { display.sleep(); } } display.dispose();
		 */
	}

}
