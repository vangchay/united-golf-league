package com.ugl.common;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;

/**
 * Class to map out directory paths.
 * 
 * @author e1042631
 *
 */
public class DirectoryInfo {
	/**
	 * The time in which the directory was created.
	 */
	public long CreationTime = 0;
	/**
	 * The last time the file was written.
	 */
	public long LastWriteTime = 0;
	/**
	 * The path to the directory (not the full directory path).
	 */
	private String strPathName = "";
	/**
	 * The design name or the base directory name.
	 */
	private String strName = "";
	/**
	 * The name of the folder of the path.
	 */
	private String strFolderName = "";
	/**
	 * The class name.
	 */
	private static final String CLASS_NAME = "DirectoryInfo.";
	/**
	 * The get directory function name.
	 */
	private static final String STRING_GET_DIRECTORY = CLASS_NAME + "GetDirectories()";

	/**
	 * Default constructor.
	 */
	public DirectoryInfo() {
	}

	/**
	 * Constructor in which the input is the directory path.
	 * 
	 * @param path
	 */
	public DirectoryInfo(String path) {
		setPathName(path);
	}

	/**
	 * Set the path name. The folder name is the last item in the path name.
	 * 
	 * @param path
	 * @author e1042631
	 */
	public void setPathName(String path) {
		strPathName = path;
		strFolderName = FilenameUtils.getName(path);
	}

	/**
	 * Get the path name.
	 * 
	 * @return path name.
	 * @author e1042631
	 */
	public String getPathName() {
		return strPathName;
	}

	/**
	 * Set the directory name.
	 * 
	 * @param Name
	 * @author e1042631
	 */
	public void setName(String Name) {
		strName = Name;
	}

	/**
	 * Construct and return the full name which is the path combined with the
	 * directory name.
	 * 
	 * @return String - the full path name.
	 * @author e1042631
	 */
	public String FullName() {
		return FilenameUtils.concat(strPathName, strName);
	}

	/**
	 * Get the directory name.
	 * 
	 * @return String - the directory name.
	 * @author e1042631
	 */
	public String Name() {
		return strName;
	}

	/**
	 * Set the creation time and the last write time.
	 * 
	 * @author e1042631 - updated 20130625 e1042631 - 20150825 - Use java 1.7 to
	 *         resolve the creation and last write time.
	 */
	public void resolveCreationTime() {
		Path p = Paths.get(strPathName, strName);
		BasicFileAttributes fileAttributes = null;
		try {
			fileAttributes = Files.readAttributes(p, BasicFileAttributes.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		CreationTime = fileAttributes.creationTime().toMillis();
		LastWriteTime = fileAttributes.lastModifiedTime().toMillis();
	}

	/**
	 * Get a list of the directories in the path.
	 * 
	 * @param filter - The file name filter.
	 * @return - DirectoryInfo[] - An array of the DirectoryInfo in the full path.
	 * @author e1042631 - updated 20130625 e1042631 - 20150825 - Added Java 1.7 for
	 *         resolving the creation time.
	 */
	public DirectoryInfo[] GetDirectories(FilenameFilter filter) {
		try {
			String strFullName = FullName();
			File file = new File(strFullName);
			String[] subDir = file.list(filter);

			resolveCreationTime();

			// Build a collection of the sub directories
			int size = subDir.length;
			if (size > 0) {
				try {
					DirectoryInfo[] arrDir = new DirectoryInfo[size];
					for (int i = 0; i < size; i++) {
						arrDir[i] = new DirectoryInfo(strFullName);
						arrDir[i].setName(subDir[i]);
					}
					return arrDir;
				} catch (Exception e) {
					Utility.showError(STRING_GET_DIRECTORY, e.toString());
					throw e;
				}
			}
		} catch (Exception e) {
			Utility.showError(STRING_GET_DIRECTORY, e.toString());
		}
		return null;
	}

	/**
	 * Get the name of the base path. In the example if the path is
	 * "C:\test\Message", the return value would be "Message".
	 * 
	 * @return the strFolderName
	 */
	public String getstrFolderName() {
		return strFolderName;
	}

	/**
	 * Main test class.
	 * 
	 * @param args - pass the directory to test.
	 */
	public static void main(String[] args) {
		if (args.length > 0) {
			String Dir = args[0];
			DirectoryInfo Info = new DirectoryInfo(Dir);
			Info.resolveCreationTime();
			Utility.showError(CLASS_NAME, "Dir =" + Dir);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date curDate = new Date(Info.LastWriteTime);
			String strDate = dateFormat.format(curDate);
			Utility.showError(CLASS_NAME, "Last write: " + strDate);
			Date createDate = new Date(Info.CreationTime);
			strDate = dateFormat.format(createDate);
			Utility.showError(CLASS_NAME, "Create date: " + strDate);
		}
	}

}

/**
 * 
 */
