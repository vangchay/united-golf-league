package com.ugl.common;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * Helper class to deal with end-of-line markers in text files.
 * 
 * Loosely based on these examples: - http://stackoverflow.com/a/9456947/1084488
 * (cc by-sa 3.0) -
 * http://svn.apache.org/repos/asf/tomcat/trunk/java/org/apache/tomcat/buildutil/CheckEol.java
 * (Apache License v2.0)
 * 
 * This file is posted here to meet the "ShareAlike" requirement of cc by-sa
 * 3.0: http://stackoverflow.com/a/27930311/1084488
 * 
 * @author Matthias Stevens
 */
public class Utils {

	/**
	 * Unix-style end-of-line marker (LF)
	 */
	private static final String EOL_UNIX = "\n";

	/**
	 * Windows-style end-of-line marker (CRLF)
	 */
	private static final String EOL_WINDOWS = "\r\n";

	/**
	 * "Old Mac"-style end-of-line marker (CR)
	 */
	private static final String EOL_OLD_MAC = "\r";

	/**
	 * Default end-of-line marker on current system
	 */
	private static final String EOL_SYSTEM_DEFAULT = System.getProperty("line.separator");

	/**
	 * The support end-of-line marker modes
	 */
	public static enum Mode {
		/**
		 * Unix-style end-of-line marker ("\n")
		 */
		LF,

		/**
		 * Windows-style end-of-line marker ("\r\n")
		 */
		CRLF,

		/**
		 * "Old Mac"-style end-of-line marker ("\r")
		 */
		CR
	}

	/**
	 * The default end-of-line marker mode for the current system
	 */
	public static final Mode SYSTEM_DEFAULT = (EOL_SYSTEM_DEFAULT.equals(EOL_UNIX) ? Mode.LF
			: (EOL_SYSTEM_DEFAULT.equals(EOL_WINDOWS) ? Mode.CRLF
					: (EOL_SYSTEM_DEFAULT.equals(EOL_OLD_MAC) ? Mode.CR : null)));
	static {
		// Just in case...
		if (SYSTEM_DEFAULT == null) {
			throw new IllegalStateException("Could not determine system default end-of-line marker");
		}
	}

	/**
	 * Determines the end-of-line {@link Mode} of a text file.
	 * 
	 * @param textFile the file to investigate
	 * @return the end-of-line {@link Mode} of the given file, or {@code null} if it
	 *         could not be determined
	 * @throws Exception
	 */
	public static Mode determineEOL(File textFile) throws Exception {
		if (!textFile.exists()) {
			throw new IOException("Could not find file to open: " + textFile.getAbsolutePath());
		}

		FileInputStream fileIn = new FileInputStream(textFile);
		BufferedInputStream bufferIn = new BufferedInputStream(fileIn);
		try {
			int prev = -1;
			int ch;
			while ((ch = bufferIn.read()) != -1) {
				if (ch == '\n') {
					if (prev == '\r') {
						return Mode.CRLF;
					} else {
						return Mode.LF;
					}
				} else if (prev == '\r') {
					return Mode.CR;
				}
				prev = ch;
			}
			throw new Exception("Could not determine end-of-line marker mode");
		} catch (IOException ioe) {
			throw new Exception("Could not determine end-of-line marker mode", ioe);
		} finally {
			// Clean up:
			IOUtils.closeQuietly(bufferIn);
		}
	}

	/**
	 * Checks whether the given text file has Windows-style (CRLF) line endings.
	 * 
	 * @param textFile the file to investigate
	 * @return
	 * @throws Exception
	 */
	public static boolean hasWindowsEOL(File textFile) throws Exception {
		return Mode.CRLF.equals(determineEOL(textFile));
	}

	/**
	 * Checks whether the given text file has Unix-style (LF) line endings.
	 * 
	 * @param textFile the file to investigate
	 * @return
	 * @throws Exception
	 */
	public static boolean hasUnixEOL(File textFile) throws Exception {
		return Mode.LF.equals(determineEOL(textFile));
	}

	/**
	 * Checks whether the given text file has "Old Mac"-style (CR) line endings.
	 * 
	 * @param textFile the file to investigate
	 * @return
	 * @throws Exception
	 */
	public static boolean hasOldMacEOL(File textFile) throws Exception {
		return Mode.CR.equals(determineEOL(textFile));
	}

	/**
	 * Checks whether the given text file has line endings that conform to the
	 * system default mode (e.g. LF on Unix).
	 * 
	 * @param textFile the file to investigate
	 * @return
	 * @throws Exception
	 */
	public static boolean hasSystemDefaultEOL(File textFile) throws Exception {
		return SYSTEM_DEFAULT.equals(determineEOL(textFile));
	}

	/**
	 * Convert the line endings in the given file to Unix-style (LF).
	 * 
	 * @param textFile the file to process
	 * @throws IOException
	 */
	public static void convertToUnixEOL(File textFile) throws IOException {
		convertLineEndings(textFile, EOL_UNIX);
	}

	/**
	 * Convert the line endings in the given file to Windows-style (CRLF).
	 * 
	 * @param textFile the file to process
	 * @throws IOException
	 */
	public static void convertToWindowsEOL(File textFile) throws IOException {
		convertLineEndings(textFile, EOL_WINDOWS);
	}

	/**
	 * Convert the line endings in the given file to "Old Mac"-style (CR).
	 * 
	 * @param textFile the file to process
	 * @throws IOException
	 */
	public static void convertToOldMacEOL(File textFile) throws IOException {
		convertLineEndings(textFile, EOL_OLD_MAC);
	}

	/**
	 * Convert the line endings in the given file to the system default mode.
	 * 
	 * @param textFile the file to process
	 * @throws IOException
	 */
	public static void convertToSystemEOL(File textFile) throws IOException {
		convertLineEndings(textFile, EOL_SYSTEM_DEFAULT);
	}

	/**
	 * Line endings conversion method.
	 * 
	 * @param textFile the file to process
	 * @param eol      the end-of-line marker to use (as a {@link String})
	 * @throws IOException
	 */
	private static void convertLineEndings(File textFile, String eol) throws IOException {
		File temp = null;
		BufferedReader bufferIn = null;
		BufferedWriter bufferOut = null;

		try {
			if (textFile.exists()) {
				// Create a new temp file to write to
				temp = new File(textFile.getAbsolutePath() + ".normalized");
				temp.createNewFile();

				// Get a stream to read from the file un-normalized file
				FileInputStream fileIn = new FileInputStream(textFile);
				DataInputStream dataIn = new DataInputStream(fileIn);
				bufferIn = new BufferedReader(new InputStreamReader(dataIn));

				// Get a stream to write to the normalized file
				FileOutputStream fileOut = new FileOutputStream(temp);
				DataOutputStream dataOut = new DataOutputStream(fileOut);
				bufferOut = new BufferedWriter(new OutputStreamWriter(dataOut));

				// For each line in the un-normalized file
				String line;
				while ((line = bufferIn.readLine()) != null) {
					// Write the original line plus the operating-system dependent newline
					bufferOut.write(line);
					bufferOut.write(eol); // write EOL marker
				}

				// Close buffered reader & writer:
				bufferIn.close();
				bufferOut.close();

				// Remove the original file
				textFile.delete();

				// And rename the original file to the new one
				temp.renameTo(textFile);
			} else {
				// If the file doesn't exist...
				throw new IOException("Could not find file to open: " + textFile.getAbsolutePath());
			}
		} finally {
			// Clean up, temp should never exist
			FileUtils.deleteQuietly(temp);
			IOUtils.closeQuietly(bufferIn);
			IOUtils.closeQuietly(bufferOut);
		}
	}

	public static String[] parseRecString(String line) {
		Vector<String> vRtn = new Vector<String>(10, 5);

		int begIndx = 0;
		int endIndx = -1;
		String data;

		boolean bContinue = false;
		do {
			bContinue = false;
			endIndx = line.indexOf(",", begIndx);

			if (endIndx == -1) {
				data = line.substring(begIndx);

				// Remove leading ' or "
				if ((data.startsWith("\'")) || (data.startsWith("\""))) {
					data = data.substring(1);
				}
				if ((data.endsWith("'")) || (data.endsWith("\""))) {
					data = data.substring(0, (data.length() - 1));
				}

				vRtn.add(data);
				bContinue = false;
			} else {
				data = line.substring(begIndx, endIndx);

				// Remove leading ' or "
				if ((data.startsWith("\'")) || (data.startsWith("\""))) {
					if (((data.endsWith("'")) || (data.endsWith("\""))) && (data.length() > 1)) {
						// There are no commas in the quoted string
						vRtn.add(data.substring(1, (data.length() - 1)));
						bContinue = true;
					} else {
						// There must be a comma in the quoted string. Look for the ending quotes
						endIndx = line.indexOf(data.charAt(0), begIndx + 1); // Plus 1 to skip the quote at the
																				// beginning
						if (endIndx > -1) {
							data = line.substring(begIndx + 1, endIndx); // Plus 1 to include the quote at the end
							vRtn.add(data);
							// Reset endIndx to comma after quote
							endIndx = line.indexOf(",", endIndx);
							if (endIndx == -1) {
								// There is no comma after the last quote
								bContinue = false;
							} else {
								bContinue = true;
							}
						} else {
							data = line.substring(begIndx + 1);
							vRtn.add(data);
							bContinue = false;
						}
					}
				} else {
					vRtn.add(data);
					bContinue = true;
				}

				begIndx = endIndx; // Skip the value
				begIndx += 1; // Skip the delimiter
			}
		} while (bContinue);
		int Len = vRtn.size();
		String retRec[] = new String[Len];
		int index = 0;
		for (int i = 0; i < Len; i++) {
			retRec[index++] = vRtn.get(i);
		}
		return retRec;
	}

	/****************************************************************************
	 * A helper routine to parse the record into a vector of strings. We can't use a
	 * StringTokenizer because delimiters that are next to each other don't get
	 * recognized as a new field.
	 * 
	 * @param line
	 * @return
	 */
	public static Vector<String> parseRec(String line) {
		Vector<String> vRtn = new Vector<String>(10, 5);

		int begIndx = 0;
		int endIndx = -1;
		String data;

		boolean bContinue = false;
		do {
			bContinue = false;
			endIndx = line.indexOf(",", begIndx);

			if (endIndx == -1) {
				data = line.substring(begIndx);

				// Remove leading ' or "
				if ((data.startsWith("\'")) || (data.startsWith("\""))) {
					data = data.substring(1);
				}
				if ((data.endsWith("'")) || (data.endsWith("\""))) {
					data = data.substring(0, (data.length() - 1));
				}

				vRtn.add(data);
				bContinue = false;
			} else {
				data = line.substring(begIndx, endIndx);

				// Remove leading ' or "
				if ((data.startsWith("\'")) || (data.startsWith("\""))) {
					if (((data.endsWith("'")) || (data.endsWith("\""))) && (data.length() > 1)) {
						// There are no commas in the quoted string
						vRtn.add(data.substring(1, (data.length() - 1)));
						bContinue = true;
					} else {
						// There must be a comma in the quoted string. Look for the ending quotes
						endIndx = line.indexOf(data.charAt(0), begIndx + 1); // Plus 1 to skip the quote at the
																				// beginning
						if (endIndx > -1) {
							data = line.substring(begIndx + 1, endIndx); // Plus 1 to include the quote at the end
							vRtn.add(data);
							// Reset endIndx to comma after quote
							endIndx = line.indexOf(",", endIndx);
							if (endIndx == -1) {
								// There is no comma after the last quote
								bContinue = false;
							} else {
								bContinue = true;
							}
						} else {
							data = line.substring(begIndx + 1);
							vRtn.add(data);
							bContinue = false;
						}
					}
				} else {
					vRtn.add(data);
					bContinue = true;
				}

				begIndx = endIndx; // Skip the value
				begIndx += 1; // Skip the delimiter
			}
		} while (bContinue);

		return vRtn;
		/*
		 * StringTokenizer st = new StringTokenizer(line, MEConstants.DATA_DELIMITER +
		 * "\r\n", true);
		 * 
		 * //Load all of the elements into the Vector String szCurData = null; String
		 * szPrevData = null; boolean bQuotedData = false;
		 * 
		 * while(st.hasMoreElements()) { szCurData = (String) st.nextElement();
		 * 
		 * //If the first item in the vector is the delimiter, then no data was put in
		 * for the first field.
		 * if(szCurData.equalsIgnoreCase(MEConstants.DATA_DELIMITER)) { if(bQuotedData)
		 * { //Add the delimiter to the previous data szCurData = szPrevData +
		 * szCurData; } else { //If the szPrevData is null this is the first element in
		 * the line if(szPrevData == null) { vRtn.addElement(new String()); } else
		 * if(szPrevData.equalsIgnoreCase(MEConstants.DATA_DELIMITER)) {
		 * vRtn.addElement(new String()); } } } else { if(szCurData.startsWith("\"") ||
		 * szCurData.startsWith("'")) { if(szCurData.endsWith("\"") ||
		 * szCurData.endsWith("'")) { //The data is between quotes without a delimiter.
		 * Save the data without the quotes. if(szCurData.length() > 1) {
		 * vRtn.addElement(szCurData.substring(1, (szCurData.length() - 1))); } else {
		 * vRtn.addElement(szCurData); } } else { //This is the beginning of delimited
		 * data szCurData = szCurData.substring(1, szCurData.length()); bQuotedData =
		 * true; } } else if(szCurData.endsWith("\"") || szCurData.endsWith("'")) {
		 * if(bQuotedData) { //This is the end of the quoted data.
		 * vRtn.addElement(szPrevData + szCurData.substring(0, (szCurData.length()
		 * -1))); bQuotedData = false; } else { //We aren't processing quoted data so
		 * just save the data. vRtn.addElement(szCurData); } } else { if(bQuotedData) {
		 * //We are processing quoted data but we are still between the quotes. Just add
		 * the data. szCurData = szPrevData + szCurData; } else { //We aren't porcessing
		 * quoted data, so save the data vRtn.addElement(szCurData); } } } szPrevData =
		 * szCurData; }
		 * 
		 * return vRtn;
		 */
	}

	/**
	 * Check for UTF8 string...
	 * 
	 * @param input
	 * @return
	 */
	public static String checkUtf8(String input) {
		char[] charBuf = input.toCharArray();
		char c = charBuf[0];
		Cout.outString(String.format("%d", (int) c));
		if (c != 0) {
			return input;
		}
		char[] outputBuf = new char[charBuf.length - 1];
		int index = 0;
		for (int i = 1; i < charBuf.length; i++) {
			outputBuf[index++] = charBuf[i];
		}
		return String.valueOf(outputBuf);
	}

	public static Multimap<String, String> ScanArguments(String[] args) {
		Multimap<String, String> map = ArrayListMultimap.create();
		String key = null;
		boolean foundKey = false;
		for (String curArg : args) {
			if (foundKey == false) {
				// arguments can start with - or --, it filters all out
				key = curArg;
				if (key.startsWith("-")) {
					foundKey = true;
					key = key.substring(key.lastIndexOf('-') + 1);
					// System.out.println("Found key: " + key);
				} else {
					System.err.println("Invalid argument: " + curArg);

				}
			} else {
				foundKey = false;
				Collection<String> item = map.get(key);
				item.add(curArg);
				System.out.println(String.format("Add argument %s=%s", key, curArg));
			}
		}

		return map;
	}

}