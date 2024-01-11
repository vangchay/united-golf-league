package com.ugl.common;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.activation.DataHandler;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility methods. Note: The rtstore.log is only created if the DEBUG flag and
 * the DebugFolder are set.
 * 
 * @author e1042631 created 20120417 20151016 - Added log4j.
 */
public class Utility {
    // ** Private Constants
    private static final String CLASS_NAME = "Utility.";
    private static String DEBUG_NAME = "rtstore";
    private static final String STRING_CLEAN_FOLDER = CLASS_NAME + "CleanFolder()";
    private static final String STRING_CHECK_FOLDER = CLASS_NAME + "CheckFolder()";
    private static final String STRING_CONCAT_ROOT_FOLDER = CLASS_NAME + "ConcatRootFolder()";
    private static String DebugFolder = ""; /** the debug folder */

    // ** Data
    /**
     * Debug log file name.
     */
    private static String utilityDebugLogFileName = null;
    /**
     * Logger
     */
    private static Logger my4JLogger = LogManager.getLogger(Utility.class);

    // ** Public

    /**
     * The DEBUG flag, indicates extra debugging.
     */
    public static Boolean DEBUG = false;
    /** the debug flag */

    // File input constant
    public static final String FileTypeInput = "inp";
    public static final String FileTypeLinq = "lnq";
    public static final String FileTypeXaml = "xaml";
    public static final String FileTypeXml = "xml";
    public static final String FileTypeExt = "ext";
    public static final String FileTypeInp = "inp";
    public static final String FileTypeIni = "ini";
    public static final String FileTypeIndex = "inx";
    public static final String FileTypePdf = "pdf";
    public static final String FileTypeNote = "note";
    public static final String FileTypeTif = "tif";
    public static final String FileTypeTiff = "tiff";
    public static final String FileTypeZip = "zip";
    public static final String FileTypeCsv = "csv";

    public static final String ElementIndex = "index";
    public static final String AttributeKey = "key";
    public static final String AttributeCurrent = "current";
    public static final String ElementFile = "file";
    public static final String AttributeField = "field";
    public static final String AttributeName = "name";

    // XML file element and attributes for store service results
    public static final String UserDocType = "userDocType";
    public static final String RootFolders = "folders";
    public static final String ChildFolder = "folder";
    public static final String FilterList = "list";

    /**
     * Set the debug folder.
     * 
     * @param path - This is the path to store the rtstore.log file.
     * @author e1042631
     */
    static public void setDebugFolder(String path) {
	DebugFolder = path;
    }

    /**
     * Returns the debug folder.
     * 
     * @return String
     * @author e1042631
     */
    static public String getDebugFolder() {
	return DebugFolder;
    }

    /**
     * Combines the folder, key and file type into an intuition usable file name.
     * 
     * @param folder   - The folder path.
     * @param key      - The design template.
     * @param fileType - The file type. Some types are xml or xaml.
     * @return String - the file name.
     * @author e1042631
     */
    public static String BuildFileName(String folder, String key, String fileType) {
	String fileName = String.format("%s.%s", key, fileType);
	fileName = FilenameUtils.concat(folder, fileName);
	return fileName;
    }

    /**
     * Remove the contents of the folder consisting with the directory "folder /
     * user / key". This will log an error to system.out and the rtstore.log.
     * 
     * @param user   - The user.
     * @param key    - The design template.
     * @param folder - The path.
     * @author e1042631
     */
    public static void CleanFolder(String user, String key, String folder) {
	String target1 = FilenameUtils.concat(folder, user);
	String targetFolder = FilenameUtils.concat(target1, key);
	try {
	    CleanFolder(targetFolder);
	} catch (Exception e) {
	    showError(STRING_CLEAN_FOLDER, e.toString());
	    e.printStackTrace();
	}
    }

    /**
     * Remove the contents of the target folder.
     * 
     * @param targetFolder - The target folder to be cleaned.
     * @throws Exception
     * @author e1042631
     */
    public static void CleanFolder(String targetFolder) throws Exception {
	File file = new File(targetFolder);
	if (file.exists() == true && file.isDirectory()) {
	    try {
		FileUtils.deleteDirectory(file);
	    } catch (IOException e) {
		showError(STRING_CLEAN_FOLDER, e.toString());
		e.printStackTrace();
	    }
	} else {
	    String err = "Folder " + targetFolder + " not deleted.";
	    showError(STRING_CLEAN_FOLDER, err);
	    throw new Exception(err);
	}
    }

    /**
     * Get the current time.
     * 
     * @return - TimeStamp
     * @author e1042631
     */
    public static Timestamp getCurrentTime() {
	// Instantiates a Date object and calls
	// the getTime method, and creates and
	// returns the Timestamp object with the
	// current time.
	return new Timestamp(new Date().getTime());
    }

    /**
     * Use to display error message to the System.out and to the rtstore.log if the
     * debug setting is used. The time will be concatenated to the final output
     * message. The format is "yyyy-mm-dd Class.function(): Log Message".
     * 
     * @param msg    - The message.
     * @param errmsg - The error.
     * @author e1042631
     */
    public static void showError(String msg, String errmsg) {
	String strDebug = getCurrentTime() + " " + msg + " " + errmsg;
	System.out.println(strDebug);
	strDebug = strDebug + "\n";

	// If debugging, throw into a log
	if (DEBUG == true && DebugFolder.isEmpty() == false) {
	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    Date date = new Date();
	    String debugName = DEBUG_NAME + dateFormat.format(date) + ".log";
	    String folder = FilenameUtils.concat(DebugFolder, debugName);
	    utilityDebugLogFileName = folder;
	    try {
		BufferedWriter out = new BufferedWriter(new FileWriter(folder, true));
		out.write(strDebug);
		out.close();
	    } catch (FileNotFoundException e) {
		e.printStackTrace();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
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

    /**
     * OS is MAC.
     * 
     * @return true/false
     * @author e1042631
     */
    public static boolean isMac() {
	String os = System.getProperty("os.name").toLowerCase();
	// Mac
	return (os.indexOf("mac") >= 0);
    }

    /**
     * OS is UNIX.
     * 
     * @return true/false
     * @author e1042631
     */
    public static boolean isUnix() {
	String os = System.getProperty("os.name").toLowerCase();
	// linux or unix
	return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);
    }

    /**
     * OS is SUN.
     * 
     * @return true/false
     * @author e1042631
     */
    public static boolean isSolaris() {
	String os = System.getProperty("os.name").toLowerCase();
	// Solaris
	return (os.indexOf("sunos") >= 0);
    }

    /**
     * Get the local host machine name.
     * 
     * @return String - Machine name.
     * @author e1042631
     */
    public static String getMachineName() {
	String computername = "";
	try {
	    computername = InetAddress.getLocalHost().getHostName();
	} catch (Exception e) {
	    showError(CLASS_NAME + "getMachineName()", e.getMessage());
	}
	return computername;
    }

    /**
     * Check the folder to see if it exist, if it doesn't then try and create it.
     * This method will log an error if an exception occurs to System.out and
     * rtstore.log.
     * 
     * @param File - Folder.
     * @return
     */
    public static Boolean CheckFolder(File fFolder) {
	Boolean bRetVal = false;

	if (fFolder.exists() == true && fFolder.isDirectory() == true) {
	    bRetVal = true;
	} else {
	    try {
		FileUtils.forceMkdir(fFolder);
		bRetVal = true;
	    } catch (IOException e) {
		Utility.showError(STRING_CHECK_FOLDER, e.toString());
		e.printStackTrace();
	    }
	}

	return bRetVal;
    }

    /**
     * Validate folder base on EnumValidationActions.
     * 
     * @param callerName - The functional caller name.
     * @param folderName - The folder.
     * @param actions    - The EnumValidationActions action.
     * @return Error string or "" if not error.
     */
    public static String ValidateFolder(String callerName, String folderName, EnumValidationActions actions) {
	String strVersion = "";
	int nActions = 0;// actions.getValue();
	int nFileExist = 0;// ((EnumValidationActions) EnumValidationActions.FileExists).getValue();
	int nEditable = 0;// EnumValidationActions.Editable.value();
	int nFolderExist = 0;// EnumValidationActions.FolderExists.value();
	int nEnumerate = 0;// 'EnumValidationActions.Enumerate.getValue();
	int nWriteable = 0;// EnumValidationActions.Writeable.getValue();

	// check to see if folder exist?
	if ((nActions & nFileExist) == nFileExist) {
	    File file = new File(folderName);
	    if (!file.exists()) {
		strVersion = String.format("%s %s %s not found", ProviderStrings.Errors, callerName, folderName);
	    }
	}

	// check to see if folder is writable?
	if (strVersion.isEmpty() && (nActions & nEditable) == nEditable) {
	    File file = new File(folderName);
	    if (!file.canWrite()) {
		strVersion = String.format("%s %s %s - FileNotFoundException: Cannot write file.",
			ProviderStrings.Errors, callerName, folderName);
	    }
	}

	// check for folder exist?
	if (strVersion.isEmpty() && (nActions & nFolderExist) == nFolderExist) {
	    File file = new File(folderName);
	    if (file.exists() && !file.isDirectory()) {
		strVersion = String.format("%s %s %s not found", ProviderStrings.Errors, callerName, folderName);
	    }
	}

	// check to see if we can read/write folder?
	if (strVersion.isEmpty() && (nActions & nEnumerate) == nEnumerate) {
	    // check access to the folder
	    File file = new File(folderName);
	    if (file.canRead() && file.canWrite()) {

	    } else {
		strVersion = String.format("%s %s %s cannot read/write", ProviderStrings.Errors, callerName,
			folderName);
	    }
	}

	// check folder if writable
	if (strVersion.isEmpty() && (nActions & nWriteable) == nWriteable) {
	    String testFile = FilenameUtils.concat(folderName, "test.txt");
	    File file = null;
	    try {
		file = new File(testFile);
		FileUtils.writeStringToFile(file, "Test");
	    } catch (Exception ex) {
		strVersion = String.format("%s %s %s File IOException %s", ProviderStrings.Errors, callerName,
			folderName, ex.toString());
	    } finally {
		FileUtils.deleteQuietly(file);
	    }
	}

	return strVersion;
    }

    /**
     * Convert the DataHandler to a byte array.
     * 
     * @param dh - The data handler.
     * @return byte[] - The byte array.
     * @throws IOException
     */
    public static byte[] ConvertDataHandlerToByeArray(DataHandler dh) throws IOException {
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
     * Concatenate the folder with the design name. The design name must contain
     * valid characters. Invalid characters in the design are *:<>|?.
     * 
     * @param folder - The folder or path.
     * @param design - The design name.
     * @return
     */
    public static String ConcatRootFolder(String folder, String design) throws Exception {
	if (isUnix() && design.length() > 0) {
	    char cFileSeparator = '/';
	    if (design.charAt(0) == cFileSeparator) {
		String err = "File separator char (/) cannot be the first parameter in the design parameter=" + design
			+ ".";
		showError(STRING_CONCAT_ROOT_FOLDER, err);
		throw new Exception(err);
	    }
	}
	if (design.contains("*")) {
	    String err = "Invalid char * in design parameter=" + design + ".";
	    showError(STRING_CONCAT_ROOT_FOLDER, err);
	    throw new Exception(err);
	} else if (design.contains(":")) {
	    String err = "Invalid char : in design parameter=" + design + ".";
	    showError(STRING_CONCAT_ROOT_FOLDER, err);
	    throw new Exception(err);
	} else if (design.contains("<")) {
	    String err = "Invalid char < in design parameter=" + design + ".";
	    showError(STRING_CONCAT_ROOT_FOLDER, err);
	    throw new Exception(err);
	} else if (design.contains(">")) {
	    String err = "Invalid char > in design parameter=" + design + ".";
	    showError(STRING_CONCAT_ROOT_FOLDER, err);
	    throw new Exception(err);
	} else if (design.contains("|")) {
	    String err = "Invalid char | in design parameter=" + design + ".";
	    showError(STRING_CONCAT_ROOT_FOLDER, err);
	    throw new Exception(err);
	} else if (design.contains("?")) {
	    String err = "Invalid char ? in design parameter=" + design + ".";
	    showError(STRING_CONCAT_ROOT_FOLDER, err);
	    throw new Exception(err);
	}
	String strReturn = FilenameUtils.concat(folder, design);
	return strReturn;
    }

    /**
     * Set the debug file prefix. The default debug log name is "rtstore" which can
     * be overrided by this command.
     * 
     * @param fileName
     */
    public static void setDebugFileName(String fileName) {
	DEBUG_NAME = fileName;
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

    /**
     * Get the log4j logger.
     * 
     * @return
     */
    public static Logger get4JLogger() {
	return my4JLogger;
    }

    /**
     * Test the log4j logger.
     */
    public static void testLogger() {
	my4JLogger.debug("hello world debug");
	my4JLogger.info("Hello this is an info message");
    }

    /**
     * Returns the debug log file name.
     * 
     * @return
     */
    public static String getDebugLogFile() {
	return utilityDebugLogFileName;
    }
}