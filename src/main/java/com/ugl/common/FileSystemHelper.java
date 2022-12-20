/**
 * 
 */
package com.ugl.common;

import java.io.File;

import org.apache.commons.io.FileUtils;

/**
 * @author e1042631
 *
 */
public class FileSystemHelper {
	private static final String CLASS_NAME = "FileSystemHelper.";

	/**
	 * Save byte array data to the file. In the case of error check the trace log
	 * and find exception.
	 * 
	 * @param name     - The name is not used.
	 * @param fileName - The file name which includes the path.
	 * @param data     - The byte array data.
	 * @param service  - The calling service which is only used in diagnostics.
	 * @return true / false
	 * @author e1042631 - created 2030625
	 */
	public static Boolean Save(String name, String fileName, byte[] data, String service) {
		Boolean bRet = false;
		File file = null;
		try {
			file = new File(fileName);
			FileUtils.writeByteArrayToFile(file, data);
			bRet = true;
		} catch (Exception ex) {
			String errMsg = "Failed to save " + service + " file " + name + " in " + fileName + " - " + ex.toString();
			WriteTraceEntry(errMsg);
		}
		return bRet;
	}

	/**
	 * Delete the file name from the root folder + design.
	 * 
	 * @param name       - The file name.
	 * @param design     - The design or template name.
	 * @param rootFolder - The root folder.
	 * @param service    - The calling service which is only used in diagnostics.
	 * @return true / false.
	 * @author e1042631 - created 20130625
	 */
	public static Boolean Delete(String name, String design, String rootFolder, String service) {
		Boolean bRet = false;
		String fileName;
		try {
			fileName = Utility.ConcatRootFolder(rootFolder, design);
		} catch (Exception e) {
			WriteTraceEntry(e.toString());
			return false;
		}
		try {
			fileName = Utility.ConcatRootFolder(fileName, name);
		} catch (Exception e) {
			WriteTraceEntry(e.toString());
			return false;
		}

		try {
			File file = new File(fileName);
			bRet = file.delete();
		} catch (Exception ex) {
			String errMsg = "Failed to delete " + service + "file " + name + " in " + rootFolder + " - "
			        + ex.toString();
			WriteTraceEntry(errMsg);
		}
		return bRet;
	}

	/**
	 * Delete the directory. If the folder does not exist then this method will
	 * return false.
	 * 
	 * @param folder  - The folder to be deleted including that path.
	 * @param service - The calling service which is only used in diagnostics.
	 * @return true / false
	 * @author e1042631 - created 20130625
	 */
	public static Boolean DeleteAll(String folder, String service) {
		File file = new File(folder);
		if (!file.exists()) {
			WriteTraceEntry("Directory " + folder + " does not exist ");
			return false;
		}

		Boolean bRet = false;
		try {
			FileUtils.deleteDirectory(file);
			if (file.exists()) {
				bRet = false;
			} else {
				bRet = true;
			}
		} catch (Exception ex) {
			String errMsg = "Failed to delete " + service + " files in " + folder + " - " + ex.toString();
			WriteTraceEntry(errMsg);
		}
		return bRet;
	}

	/**
	 * Write an entry into the trace log which is defined in the Utility class.
	 * 
	 * @param logEntry - The log entry message.
	 */
	private static void WriteTraceEntry(String logEntry) {
		Utility.showError(CLASS_NAME, logEntry);
	}

}