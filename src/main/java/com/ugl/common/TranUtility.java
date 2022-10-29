/**
 * 
 */
package com.ugl.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author e1042631
 *
 */
public class TranUtility {

	/**
	 * 
	 */
	public TranUtility() {
		// TODO Auto-generated constructor stub
	}

	public static String getSystemTime() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmm-ss");
		LocalDateTime now = LocalDateTime.now();
		return dtf.format(now);
	}

	/**
	 * Write to file.
	 * 
	 * @param lines
	 * @param outputFileName
	 * @return
	 */
	public static boolean WriteFile(List<String> lines, String outputFileName) {
		boolean bRet = true;
		BufferedWriter wr = null;
		try {
			wr = new BufferedWriter(new FileWriter(new File(outputFileName)));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (String s : lines) {
			try {
				wr.write(s);
				wr.newLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			wr.flush();
			wr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bRet;
	}

	/**
	 * Append file.
	 * 
	 * @param lines
	 * @param outputFileName
	 * @return
	 */
	public static boolean AppendFile(List<String> lines, String outputFileName) {
		boolean bRet = true;
		BufferedWriter wr = null;

		try {
			wr = Files.newBufferedWriter(Paths.get(outputFileName), Charset.forName("UTF8"), StandardOpenOption.WRITE,
					StandardOpenOption.APPEND, StandardOpenOption.CREATE);
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}

		for (String s : lines) {
			try {
				wr.append(s);
				wr.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		try {
			wr.flush();
			wr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bRet;
	}

	/**
	 * Read all the lines of a file.
	 * 
	 * @param fileName
	 * @return
	 */
	public static List<String> ReadFile(String fileName) {
		List<String> arrayListLines = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
			String line = null;
			try {
				while ((line = br.readLine()) != null) {
					arrayListLines.add(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return arrayListLines;
	}

	/**
	 * Find the file base name.
	 * 
	 * @param Name
	 * @return
	 */
	public static String fileFileBaseName(String Name) {
		int Beg = Name.length() - 1;
		for (; Beg > 0; Beg--) {
			char c = Name.charAt(Beg);
			if (c == '.') {
				String s = Name.substring(0, Beg);
				return s;
			}
		}
		return Name;
	}

	/**
	 * find the file name from the path.
	 * 
	 * @param fileNameAndPath
	 * @return
	 */
	public static String fileFileName(String fileNameAndPath) {
		int Beg = fileNameAndPath.length() - 1;
		for (; Beg > 0; Beg--) {
			char c = fileNameAndPath.charAt(Beg);
			if (c == '/' || c == '\\') {
				String s = fileNameAndPath.substring(Beg + 1);
				return s;
			}
		}

		return fileNameAndPath;
	}

	public static void copyFileToFile(final File src, final File dest) throws IOException {
		copyInputStreamToFile(new FileInputStream(src), dest);
		dest.setLastModified(src.lastModified());
	}

	public static void copyInputStreamToFile(final InputStream in, final File dest) throws IOException {
		copyInputStreamToOutputStream(in, new FileOutputStream(dest));
	}

	public static void copyInputStreamToOutputStream(final InputStream in, final OutputStream out) throws IOException {
		try {
			try {
				final byte[] buffer = new byte[1024];
				int n;
				while ((n = in.read(buffer)) != -1)
					out.write(buffer, 0, n);
			} finally {
				out.close();
			}
		} finally {
			in.close();
		}
	}

}