/**
 * 
 */
package com.ugl.common;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * XML Helper class.
 * 
 * @author e1042631 created 20120417
 */
public class XmlFileHelper {
    // private
    private static final String STRING_CODEPAGE = "utf-8";
    private static final String STRING_TRUE = "true";
    private static final String STRING_STREAM = "stream";
    private static final String STRING_FOLDERS = "folders";
    private static final String STRING_FOLDER = "folder";
    private static final String STRING_LIST = "list";
    private static final String CLASS_NAME = "XmlFileHelper.";

    private static final String STRING_GET_XML_FILE_CONTENT = CLASS_NAME + "GetXmlFileContent()";
    private static final String STRING_GET_XML_FILE_BYTES = CLASS_NAME + "GetXmlFileBytes()";
    private static final String STRING_GET_FILE_BYTES = CLASS_NAME + "GetFileBytes()";
    private static final String STRING_LIST_FOLDERS_TO_STRING = CLASS_NAME + "ListFoldersToString()";
    private static final String STRING_GET_OUTPUT_STREAM_NAME = CLASS_NAME + "GetOutputStreamName()";

    /**
     * Get the XML file contents.
     * 
     * @param rootFolder - The root folder.
     * @param key        - The design template or sub directory.
     * @param fileType   - The file extension type.
     * @return String containing XML contents.
     * @author e1042631
     */
    public static String GetXmlFileContent(String rootFolder, String key, String fileType) {
	String text = "";
	String folder;
	try {
	    folder = Utility.ConcatRootFolder(rootFolder, key);
	} catch (Exception e1) {
	    e1.printStackTrace();
	    Utility.showError(STRING_GET_XML_FILE_CONTENT, e1.toString());
	    return text;
	}
	String file = Utility.BuildFileName(folder, key, fileType);
	File fFile = new File(file);

	if (fFile.exists()) {
	    try {
		text = FileUtils.readFileToString(fFile, STRING_CODEPAGE);
	    } catch (IOException e) {
		Utility.showError(STRING_GET_XML_FILE_CONTENT, e.toString());
	    }
	}
	return text;
    }

    /**
     * Get the XML content in a byte array.
     * 
     * @param rootFolder - The root folder.
     * @param key        - The design template or sub directory.
     * @param fileType   - The file extension type.
     * @return byte[] - Returns an empty array if error or the file does not exist.
     * @author e1042631
     */
    public static byte[] GetXmlFileBytes(String rootFolder, String key, String fileType) {
	byte[] data = new byte[] {};
	String folder;
	try {
	    folder = Utility.ConcatRootFolder(rootFolder, key);
	} catch (Exception e1) {
	    Utility.showError(STRING_GET_XML_FILE_BYTES, e1.toString());
	    return data;
	}

	String file = Utility.BuildFileName(folder, key, fileType);
	File fFile = new File(file);
	if (fFile.exists()) {
	    try {
		data = FileUtils.readFileToByteArray(fFile);
	    } catch (IOException e) {
		Utility.showError(STRING_GET_XML_FILE_BYTES, e.toString());
	    }
	}

	return data;
    }

    /**
     * Get the file into a byte array.
     * 
     * @param file - The file which includes the path and file name.
     * @return byte[] - Returns an empty array if error or the file does not exist.
     * @author e1042631
     */
    public static byte[] GetFileBytes(String file) {
	byte[] data = new byte[] {};
	File fFile = new File(file);

	if (fFile.exists()) {
	    try {
		data = FileUtils.readFileToByteArray(fFile);
	    } catch (IOException e) {
		Utility.showError(STRING_GET_FILE_BYTES, e.toString());
	    }
	}

	return data;
    }

    /**
     * Create an XML String containing the list of the folders.
     * 
     * @param list    - A list of the folder.
     * @param listing - The list attribute setting.
     * @return String containing XML content. If error then returns an empty string.
     * @author e1042631
     */
    public static String ListFoldersToString(List<String> list, String listing) {
	try {
	    Document doc = StoreHelper.createDoc();
	    Element root = doc.createElement(STRING_FOLDERS);
	    root.setAttribute(STRING_LIST, listing);
	    doc.appendChild(root);

	    Iterator<String> iterator = list.iterator();

	    while (iterator.hasNext()) {
		String strVal = iterator.next();
		Element nextElem = doc.createElement(STRING_FOLDER);
		nextElem.appendChild(doc.createTextNode(strVal));
		root.appendChild(nextElem);
	    }

	    return StoreHelper.doc2String(doc);
	} catch (Exception e) {
	    Utility.showError(STRING_LIST_FOLDERS_TO_STRING, e.toString());
	}

	return "";
    }

    /**
     * Parses the design file to get the output stream name for the specified
     * target.
     * 
     * @param design - The design file (path + filename).
     * @param target - The target parameter in the XML.
     * @return String - The output stream name.
     * @author e1042631
     * 
     */
    public static String GetOutputStreamName(String design, String target) {
	try {
	    DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
	    File file = new File(design);
	    DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
	    Document doc = docBuilder.parse(file);
	    NodeList list = doc.getElementsByTagName(ProviderStrings.OutputSection);

	    // Look thru list to find the element that matches the target
	    if (list != null) {
		for (int i = 0; i < list.getLength(); i++) {
		    Element output = (Element) list.item(i);
		    if (output != null) {
			NodeList subList = output.getElementsByTagName(STRING_STREAM);
			if (subList != null) {
			    for (int subi = 0; subi < subList.getLength(); subi++) {
				Element e = (Element) subList.item(subi);
				String di = e.getAttribute(ProviderStrings.OutputIntuition);
				String type = e.getAttribute(ProviderStrings.OutputType);
				if (di.compareTo(STRING_TRUE) == 0 && type.compareTo(target) == 0) {
				    return e.getAttribute(ProviderStrings.OutputName);
				}
			    }
			}
		    }
		}
	    }
	} catch (SAXException e) {
	    Utility.showError(STRING_GET_OUTPUT_STREAM_NAME, e.toString());
	} catch (IOException e) {
	    Utility.showError(STRING_GET_OUTPUT_STREAM_NAME, e.toString());
	} catch (ParserConfigurationException e) {
	    Utility.showError(STRING_GET_OUTPUT_STREAM_NAME, e.toString());
	}
	return "";
    }
}