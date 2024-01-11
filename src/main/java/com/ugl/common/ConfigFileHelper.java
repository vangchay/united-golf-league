package com.ugl.common;

import java.io.File;
import java.util.Hashtable;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Intuition configuration helper class which can loads and stores the
 * AppSettings.config values.
 * 
 * @author e1042631
 *
 */
public class ConfigFileHelper {
    /**
     * The class name.
     */
    private static final String CLASS_NAME = "ConfigFileHelper.";
    private static final String GET_CONFIG_BOOL = CLASS_NAME + "GetConfigBool()";

    /** The Intuition AppConfig.config file location. */
    private static String strConfigFile = "C:\\Program Files\\FIS\\CSF Designer Intuition 1.5\\AppSettings.config";
    private static Document docConfigFile = null;
    private static Hashtable<String, String> AppSettings = null;

    /**
     * Initialize using the default config file (AppSettings.config). This file sets
     * the application settings of Intuition. The user must call setConfigFile()
     * method before calling this method. Note: The default config file is set to
     * "C:\Program Files\FIS\CSF Designer Intuition 1.5\AppSettings.config" must
     * exist on the file system.
     * 
     * @author e1042631 - updated 20130625
     */
    public static void init() throws Exception {
	try {
	    setConfigFile(strConfigFile);
	} catch (Exception e) {
	    throw e;
	}
    }

    /**
     * Set the config file.
     * 
     * @param configFile - The configuration file path + file. The config file is an
     *                   XML file which sets the application setting of Intuition.
     *                   This file is typically the AppSettings.config provided by
     *                   Intuition.
     * @return Throws an Exception if the file doesn't exist or error during the
     *         parsing of the XML file.
     * @author e1042631 - updated 20130625
     */
    public static void setConfigFile(String configFile) throws Exception {
	String strErr = null;

	strConfigFile = configFile;

	// Parse the file contents
	File file = new File(strConfigFile);
	if (file.exists() == false) {
	    strErr = "setConfigFile() - Config file " + configFile + " doesn't exist.";
	    Utility.showError(CLASS_NAME, strErr);
	    throw new Exception(strErr);
	}

	// Parse to the doc
	docConfigFile = StoreHelper.getDocFromFile(file);
	if (docConfigFile == null) {
	    strErr = "setConfigFile() - Could not parse XML file.";
	    Utility.showError(CLASS_NAME, strErr);
	    throw new Exception(strErr);
	}

	// Go thru all App settings and set the key and values
	AppSettings = new Hashtable<String, String>();
	NodeList nodes = docConfigFile.getElementsByTagName("appSettings");
	int size = nodes.getLength();
	if (size > 0) {
	    for (int i = 0; i < size; i++) {
		NodeList settings = nodes.item(i).getChildNodes();
		int nSettings = settings.getLength();
		if (nSettings > 0) {
		    for (int j = 0; j < nSettings; j++) {
			Node nd = settings.item(j);
			// Only look for "add" nodes
			if (nd.getNodeName() == "add") {
			    NamedNodeMap ndAttr = nd.getAttributes();
			    Node key = ndAttr.getNamedItem("key");
			    Node val = ndAttr.getNamedItem("value");
			    AppSettings.put(key.getNodeValue(), val.getNodeValue());
			}
		    }
		}
	    }
	}
    }

    /**
     * Read the value for the specified key from the config file
     * (AppSettings.config).
     * 
     * @param configKey    - The config key.
     * @param defaultValue - A default value in case the config key is not found in
     *                     the config file.
     * @return String - The key value.
     * @author e1042631 - updated 20130625
     */
    public static String LoadConfigSetting(String configKey, String defaultValue) throws Exception {
	String configValue = defaultValue;
	String strValue = AppSettings.get(configKey);
	if (strValue != null && !strValue.isEmpty()) {
	    configValue = strValue;
	} else {
	    throw new Exception(CLASS_NAME + "LoadConfigSetting() Invalid Arguments");
	}
	return configValue;
    }

    /**
     * LoadOptionalConfigSetting() - Load the key value.
     * 
     * @param configKey - the config key.
     * @return - The key value. If the key does not exist, then returns "" string.
     * @author e1042631 - updated 20130625
     */
    public static String LoadOptionalConfigSetting(String configKey) {
	if (AppSettings.isEmpty() == true) {
	    Utility.showError(CLASS_NAME + "LoadOptionalConfigSetting", "App settings not loaded.");
	    return "";
	}

	String retValue = AppSettings.get(configKey);
	if (retValue == null || retValue.isEmpty()) {
	    return "";
	}

	return retValue;
    }

    /**
     * Get a key value which set to either true or false.
     * 
     * @param key          - The key string.
     * @param defaultValue - The default "Boolean" value either true or false.
     * @return true or false, if error then returns the default value.
     * @author e1042631 - updated 20130625
     */
    public static Boolean GetConfigBool(String key, Boolean defaultValue) {
	String strValue = AppSettings.get(key);
	if (strValue != null && !strValue.isEmpty()) {
	    try {
		Boolean bRet = Boolean.parseBoolean(strValue);
		return bRet;
	    } catch (Exception ex) {
		Utility.showError(GET_CONFIG_BOOL, "Invalid setting " + key + "=" + strValue + ". " + ex.toString());
	    }
	}
	return defaultValue;
    }
}
