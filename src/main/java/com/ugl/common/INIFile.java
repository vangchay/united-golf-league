package com.ugl.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.Ordering;

/**
 * @author e1042631
 *
 */
public class INIFile {
	private static final String EQUALS = "=";

	/**
	 * Table of property settings. (setting and value)
	 */
	protected Multimap<String, String> settingsTable = null;

	/**
	 * The path of the INI file.
	 */
	private String iniDefaultPath = "./";

	/**
	 * The default INI file name.
	 */
	private String iniFileName;

	/**
	 * system dir
	 */
	private String sysDir = null;

	/**
	 * Constructor.
	 * 
	 * @param fileName
	 */
	public INIFile(String fileName) {
		settingsTable = ArrayListMultimap.create();
		iniFileName = fileName;
		// Read the settings
		// if (fileName != null) {
		// fileName = FilenameUtils.separatorsToSystem(fileName);
		// String drive = null;
		// String temp = fileName.substring(1, 2);
		// if (temp.contains(":")) {
		// drive = fileName.substring(0, 3);
		// }
		// iniDefaultPath = FilenameUtils.getPath(fileName); // set the path
		// if (drive != null) {
		// iniDefaultPath = FilenameUtils.concat(drive, iniDefaultPath);
		// }
		// iniFileName = fileName; // set the file name
		// }

		Properties myProperties = new Properties();
		try {
			myProperties.load(new FileInputStream(iniFileName));

			Enumeration<?> Keys = myProperties.propertyNames();
			while (Keys.hasMoreElements()) {
				String key = (String) Keys.nextElement();
				String value = myProperties.getProperty(key);
				if (value != null) {
					this.setSettings(key, value);
					// cout.outString(String.format("key=%s, val=%s", key, value));
				}
			}
		} catch (FileNotFoundException e) {
			Cout.outString(e.toString());
		} catch (IOException e) {
			Cout.outString(e.toString());
		}
	}

	/**
	 * Set the new properties setting.
	 * 
	 * @param Setting
	 * @param Value
	 * @return
	 */
	public void setSettings(String Setting, String Value) {
		if (settingsTable.containsKey(Setting)) {
			Collection<String> var = settingsTable.get(Setting);
			var.add(Value);
			return;
		}
		settingsTable.put(Setting, Value);
	}

	/**
	 * Get the property setting base on property string.
	 * 
	 * @param Setting - the properties setting.
	 * @return
	 */
	public String getSetting(String Setting) {
		Collection<String> col = settingsTable.get(Setting);
		if (col == null || col.isEmpty()) {
			return "";
		}
		Iterator<String> Iter = col.iterator();
		if (Iter == null) {
			return "";
		}
		String retStr = "";
		while (Iter.hasNext()) {
			retStr = Iter.next();
		}
		return retStr;
	}

	/**
	 * Updates the INI file.
	 */
	public void update() {
		// File f = new File(iniFileName);
		//
		// Save the old file
		// if (f.exists()) {
		// try {
		// File.createTempFile("$$$", ".bak");
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }

		// Add the settings
		List<String> lines = new ArrayList<String>();
		Multiset<String> Keys = settingsTable.keys();
		for (String key : Keys) {
			String value = getSetting(key);
			String curLine = key + EQUALS + value;
			if (lines.contains(curLine)) {
				continue;
			}
			lines.add(curLine);
			// cout.outString(curLine);
		}

		// Order it
		Ordering<String> order = Ordering.natural();
		Collections.sort(lines, order);

		// this part converts "\" to "\\" for Java
		// Java8
		/// lines.stream().map((s)->s.replace("\\",
		// "\\\\")).distinct().collect(Collectors.toList());

		// older java
		int nSize = lines.size();
		for (int index = 0; index < nSize; index++) {
			String curLine = lines.get(index);
			curLine = curLine.replace("\\", "\\\\");
			lines.set(index, curLine);
		}

		List<String> outputs = lines;

		// SAVE THE OLD FILE..
		String time = TranUtility.getSystemTime();
		String backUpFile = iniFileName + "." + time + ".bak";
		try {
			TranUtility.copyFileToFile(new File(iniFileName), new File(backUpFile));
		} catch (Exception ex) {
			Cout.outString(ex.toString());
		}

		// Write the properties file
		// try {
		TranUtility.WriteFile(outputs, iniFileName);
		// TranUtiliy.writeLines(new File(iniFileName), outputs);
		// } catch (IOException e) {
		// cout.outString(e.toString());
		// }
	}

	public void setSystemDir(String systemUserDirectory) {
		sysDir = systemUserDirectory;
	}
}