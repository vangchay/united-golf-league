/**
 * 
 */
package com.ugl.common;

import java.io.File;

/**
 * @author e1042631
 *
 */
public class ConfigFile {
	private final static String CLASS_NAME = "configFile.";

	private final static String SHOW = CLASS_NAME + "Show()";

	private final static String USS_PATH = "USSPath";

	private final static String NOTES = "Notes";

	private final static String COPY_FROM = "CopyFrom";

	private final static String ENVIRONMENT = "Environment";

	private final static String SOURCE = "Source";

	private final static String DOC_PATH_NAME = "DocPathName";

	private final static String INPUT_FILE = "InputFile";

	private final static String INPUT_FILE_BINARY = "InputFileBinary";

	private final static String GOLD_FOLDER = "GoldFolder";

	private final static String JOB_RUN_TIMEOUT = "JobRunTimeout";

	private final static String DEFAULT_VALUE = "./";

	/**
	 * USS Path.
	 */
	private String ussPath;

	/**
	 * Notes.
	 */
	private String notes;

	/**
	 * Copy From.
	 */
	private String copyFrom;

	/**
	 * Environment.
	 */
	private String environment;

	/**
	 * source.
	 */
	private String source;

	/**
	 * Document path name.
	 */
	private String documentPathName;

	/**
	 * Input file.
	 */
	private String inputFile;

	/**
	 * Binary input file.
	 */
	private String binaryInputFile;

	/**
	 * Gold folder.
	 */
	private String goldFolder;

	/**
	 * Job run timeout.
	 */
	private int jobRunTimeoutSecs = 0;

	/**
	 * File.
	 */
	private String fileName;

	/**
	 * Constructor.
	 * 
	 * @param batchFileName
	 */
	public ConfigFile(File myConfigFile) {
//		fileName = myConfigFile.getAbsolutePath();
//		@SuppressWarnings("unused")
//		ConfigFileHelper config = new ConfigFileHelper();
//		try {
//			ConfigFileHelper.setConfigFile(fileName);
//			ConfigFileHelper.init();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		try {
//			ussPath = ConfigFileHelper.LoadConfigSetting(USS_PATH, DEFAULT_VALUE);
//		} catch (Exception e) {
//		}
//		try {
//			notes = ConfigFileHelper.LoadConfigSetting(NOTES, DEFAULT_VALUE);
//		} catch (Exception e) {
//		}
//		try {
//			copyFrom = ConfigFileHelper.LoadConfigSetting(COPY_FROM, DEFAULT_VALUE);
//		} catch (Exception e) {
//		}
//		try {
//			environment = ConfigFileHelper.LoadConfigSetting(ENVIRONMENT, DEFAULT_VALUE);
//		} catch (Exception e) {
//		}
//		try {
//			source = ConfigFileHelper.LoadConfigSetting(SOURCE, DEFAULT_VALUE);
//		} catch (Exception e) {
//		}
//		try {
//			documentPathName = ConfigFileHelper.LoadConfigSetting(DOC_PATH_NAME, DEFAULT_VALUE);
//		} catch (Exception e) {
//		}
//		try {
//			inputFile = ConfigFileHelper.LoadConfigSetting(INPUT_FILE, DEFAULT_VALUE);
//		} catch (Exception e) {
//		}
//		try {
//			binaryInputFile = ConfigFileHelper.LoadConfigSetting(INPUT_FILE_BINARY, DEFAULT_VALUE);
//		} catch (Exception e) {
//		}
//		try {
//			goldFolder = ConfigFileHelper.LoadConfigSetting(GOLD_FOLDER, DEFAULT_VALUE);
//		} catch (Exception e) {
//		}
//		try {
//			String timeout = ConfigFileHelper.LoadConfigSetting(JOB_RUN_TIMEOUT, "0");
//			jobRunTimeoutSecs = Integer.parseInt(timeout);
//		} catch (Exception e) {
//		}
	}

	public void Show() {
		Cout.outString("File " + fileName);
		Cout.outString(USS_PATH + "=" + ussPath);
		Cout.outString(NOTES + "=" + notes);
		Cout.outString(COPY_FROM + "=" + copyFrom);
		Cout.outString(ENVIRONMENT + "=" + environment);
		Cout.outString(SOURCE + "=" + source);
		Cout.outString(DOC_PATH_NAME + "=" + documentPathName);
		Cout.outString(INPUT_FILE + "=" + inputFile);
		Cout.outString(INPUT_FILE_BINARY + "=" + binaryInputFile);
		Cout.outString(GOLD_FOLDER + "=" + goldFolder);
		Cout.outString(JOB_RUN_TIMEOUT + "=" + Integer.toString(jobRunTimeoutSecs));
	}

	public String getUssPath() {
		return ussPath;
	}

	public String getNotes() {
		return notes;
	}

	public String getCopyFrom() {
		return copyFrom;
	}

	public String getEnvironment() {
		return environment;
	}

	public String getSource() {
		return source;
	}

	public String getDocumentPathName() {
		return documentPathName;
	}

	public String getInputFile() {
		return inputFile;
	}

	public String getBinaryInputFile() {
		return binaryInputFile;
	}

	public String getGoldFolder() {
		return goldFolder;
	}

	public int getJobRunTimeoutSecs() {
		return jobRunTimeoutSecs;
	}

	public String getFileName() {
		return fileName;
	}

}