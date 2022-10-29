/**
 * 
 */
package com.ugl.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.activation.DataHandler;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3.www._2005._05.xmlmime.Base64Binary;

/**
 * @author e1042631
 *
 */
public class FileUtility {
	static protected Logger log = LogManager.getLogger(FileUtility.class);
	private static final String CLASS_NAME = "FileUtility.";

	private static final String STRING_LOAD_BASE64_FILE = CLASS_NAME + "LoadBase64File()";

	private static final String STRING_LOAD_TEXT_FILE = CLASS_NAME + "LoadTextFile()";

	private static final String STRING_LOAD_BINARY_FILE = CLASS_NAME + "LoadBinaryFile()";

	private static final String STRING_WRITE_BINARY_FILE = CLASS_NAME + "WriteBinaryFile()";

	private static final String STRING_BASE64_BYTE_ARRAY = CLASS_NAME + "Base64ToByteArray()";

	private static final String STRING_CODE_PAGE = "UTF-8";

	/**
	 * Convert the DataHandler to a byte array.
	 * 
	 * @param dh - The data handler.
	 * @return byte[] - The byte array.
	 * @throws IOException
	 */
	public static byte[] ConvertDataHandlerToByteArray(DataHandler dh) throws IOException {
		int INITIAL_SIZE = 1024 * 1024;
		int BUFFER_SIZE = 1024;

		ByteArrayOutputStream bos = new ByteArrayOutputStream(INITIAL_SIZE);
		InputStream in = dh.getInputStream();
		byte[] buffer = new byte[BUFFER_SIZE];
		int bytesRead = 0;
		while ((bytesRead = in.read(buffer)) >= 0) {
			bos.write(buffer, 0, bytesRead);
		}
		return bos.toByteArray();
	}

	/**
	 * Load file to base 64 binary string format.
	 * 
	 * @param fileName - The file name including the path.
	 * @return String - Base 64 binary string or null if error.
	 */
	public static String LoadBase64File(String fileName) {
		File file = new File(fileName);
		String text = null;
		if (file.exists() == true) {
			try {
				byte[] data = FileUtils.readFileToByteArray(file);
				text = DatatypeConverter.printBase64Binary(data);
			} catch (FileNotFoundException e) {
				log.error(STRING_LOAD_BASE64_FILE, "File not found - " + e.toString());
			} catch (IOException e) {
				log.error(STRING_LOAD_BASE64_FILE, "I/O Error - " + e.toString());
			}
		} else {
			log.error(STRING_LOAD_BASE64_FILE, "File does not exist " + fileName);
		}
		return text;
	}

	/**
	 * Load a text file.
	 * 
	 * @param fileName - The file name including the path.
	 * @return String - Text file content or null if error.
	 */
	public static String LoadTextFile(String fileName) {
		File file = new File(fileName);
		String text = null;
		if (file.exists() == true) {
			try {
				text = FileUtils.readFileToString(file, STRING_CODE_PAGE);
			} catch (FileNotFoundException e) {
				log.error(STRING_LOAD_TEXT_FILE, "File not found - " + e.toString());
			} catch (IOException e) {
				log.error(STRING_LOAD_TEXT_FILE, "I/O Error - " + e.toString());
			}
		}
		return text;
	}

	/**
	 * Load a binary file.
	 * 
	 * @param fileName - The file name including the path.
	 * @return byte[] - byte array or null if error.
	 */
	public static byte[] LoadBinaryFile(String fileName) {
		File file = new File(fileName);
		byte[] data = null;
		if (file.exists() == true) {
			try {
				data = FileUtils.readFileToByteArray(file);
			} catch (FileNotFoundException e) {
				log.error(STRING_LOAD_BINARY_FILE, "File not found - " + e.toString());
			} catch (IOException e) {
				log.error(STRING_LOAD_BINARY_FILE, "I/O Error - " + e.toString());
			}
		}
		return data;
	}

	/**
	 * write byte array to a file.
	 * 
	 * @param fileName - The file name including the path.
	 * @param byte[]   - byte data
	 * @return void
	 */
	public static void WriteBinaryFile(String fileName, byte[] data) {
		File file = new File(fileName);
		try {
			FileUtils.writeByteArrayToFile(file, data);
		} catch (FileNotFoundException e) {
			log.error(STRING_WRITE_BINARY_FILE, "File not found - " + e.toString());
		} catch (IOException e) {
			log.error(STRING_WRITE_BINARY_FILE, "I/O Error - " + e.toString());
		}
	}

	/**
	 * Base64 String to byte array
	 * 
	 * @param String - base64 encoded string.
	 * @return byte[] - byte array or null if error.
	 */
	public static byte[] Base64ToByteArray(String base64Data) {
		byte[] data = null;
		if (base64Data != null) {
			try {
				data = org.apache.commons.codec.binary.Base64.decodeBase64(base64Data.getBytes());
			} catch (Exception e) {
				log.error(STRING_BASE64_BYTE_ARRAY,
						"Exception converting base64 string to byte array - " + e.toString());
			}
		}
		return data;
	}

	/**
	 * Base64 Base64 binary data to byte array
	 * 
	 * @param Base64Binary - base64 encoded byte array.
	 * @return byte[] - byte array or null if error.
	 */
	public static byte[] Base64ToByteArray(Base64Binary base64Data) {
		DataHandler dataHandler = base64Data.getBase64Binary();
		byte[] data = null;
		try {
			InputStream in = dataHandler.getInputStream();
			data = org.apache.commons.io.IOUtils.toByteArray(in);
		} catch (IOException ioException) {
			log.error(STRING_BASE64_BYTE_ARRAY,
					"IOException converting base64Binary data to byte array - " + ioException.getMessage());
			ioException.printStackTrace();
		}
		return data;
	}

	/**
	 * Base64 String to String
	 * 
	 * @param String - base64 encoded string.
	 * @return String - String
	 */
	public static String Base64ToString(String base64Data) {
		byte[] data = null;
		String strData = null;
		if (base64Data != null) {
			try {
				data = org.apache.commons.codec.binary.Base64.decodeBase64(base64Data.getBytes());
				strData = new String(data, "utf-8");
			} catch (Exception e) {
				log.error(STRING_BASE64_BYTE_ARRAY, "File not found - " + e.toString());
			}
		}
		return strData;
	}

	/**
	 * Strip the ".\" or "./" for relative path in front of the file name.
	 * 
	 * @param path - The file path including the file name.
	 * @return - The file name only.
	 */
	public static String stripRelativePath(String path) {
		String strReturn;
		if (path.startsWith(".\\", 0) || path.startsWith("./", 0)) {
			strReturn = path.substring(2);
		} else {
			return path;
		}
		return strReturn;
	}

	/**
	 * Clean the path of invalid characters. Invalid path characters are as defined
	 * by Windows <>"|?*.
	 * 
	 * @param filePath
	 * @return String
	 */
	public static String sanitizePath(String filePath) {
		filePath = FilenameUtils.separatorsToSystem(filePath);
		filePath = filePath.replaceAll("[<>\"|?*]", "");
		// now replace path manipulation
		String illegal;
		if (isWindows()) {
			illegal = "..\\";
		} else {
			illegal = "../";
		}
		if (filePath.contains(illegal)) {
			filePath = filePath.replace(illegal, "");
		}
		return filePath;
	}

	public static String stripPath(String fileNameWithPath) {
		String fileName = FilenameUtils.getName(fileNameWithPath);
		return fileName;
	}

	/**
	 * OS is WIN.
	 * 
	 * @return true/false
	 * @author e1042631
	 */
	public static boolean isWindows() {
		String os = System.getProperty("os.name").toLowerCase();
		// windows
		return (os.indexOf("win") >= 0);
	}

}